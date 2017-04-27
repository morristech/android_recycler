/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License 
 * you may obtain at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You can redistribute, modify or publish any part of the code written within this file but as it 
 * is described in the License, the software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.recycler.helper;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerViewItemHelper} that may be used to support <b>drag</b> feature for items
 * displayed in a {@link RecyclerView} widget. Instance of this helper may be simply created via
 * {@link #ItemDragHelper()} constructor.
 * <p>
 * In order to support drag feature for a desired adapter, such adapter must implement {@link DragAdapter}
 * interface and needs to be attached to its corresponding {@link RecyclerView} before call to
 * {@link #attachToRecyclerView(RecyclerView)} which will attach such adapter to the drag helper
 * automatically. Also all {@link RecyclerView.ViewHolder ViewHolders} of that adapter of which item
 * views are desired to be dragged, must implement {@link DragViewHolder} interface as other view
 * holder implementations will be ignored by the ItemDragHelper API. When the {@link DragAdapter}
 * is properly attached, the item helper will delegate to it all drag gesture related callbacks/events
 * whenever appropriate via the adapter's interface.
 *
 * <h3>Drag Callbacks</h3>
 * A {@link OnDragListener} may be registered via {@link #addOnDragListener(OnDragListener)} in
 * order to receive callbacks about <b>started</b>, <b>finished</b> or <b>canceled</b> drag gesture
 * for a particular {@link RecyclerView.ViewHolder ViewHolder}. If the listener is no more needed it
 * should be unregistered via {@link #removeOnDragListener(OnDragListener)}.
 *
 * @author Martin Albedinsky
 */
public final class ItemDragHelper extends RecyclerViewItemHelper<ItemDragHelper.DragInteractor> {

    /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemDragHelper";

	/**
	 * Default move threshold for the drag gesture.
	 */
	public static final float MOVE_THRESHOLD = 0.5f;

    /*
	 * Interface ===================================================================================
	 */

	/**
	 * Required interface for adapters which want to support drag feature for theirs
	 * {@link RecyclerView.ViewHolder ViewHolders}.
	 *
	 * @author Martin Albedinsky
	 */
	public interface DragAdapter {

		/**
		 * Called by the drag helper to obtain drag movement flags for an item at the specified
		 * <var>position</var>.
		 *
		 * @param position Position of the item for which to obtain its drag flags.
		 * @return Movement flags determining in which direction can be the item dragged.
		 * @see ItemDragHelper#makeDragFlags(int)
		 */
		int getItemDragFlags(int position);

		/**
		 * Called by the drag helper whenever drag gesture for item associated with the specified
		 * <var>position</var> has started.
		 *
		 * @param position Position of the view holder of which view started to be dragged by the user.
		 * @see #onItemDragFinished(int, int)
		 * @see #onMoveItem(int, int)
		 */
		void onItemDragStarted(int position);

		/**
		 * Called by the drag helper to inform this adapter that it should move its item in its
		 * data set from the specified <var>fromPosition</var> to the specified <var>toPosition</var>.
		 *
		 * @param fromPosition The position from which should be the item moved in this adapter's
		 *                     data set.
		 * @param toPosition   The position to which should be the item moved in this adapter's
		 *                     data set.
		 */
		void onMoveItem(int fromPosition, int toPosition);

		/**
		 * Called by the drag helper to check if item associated with the <var>currentPosition</var>
		 * can be dropped at the specified <var>targetPosition</var>.
		 * <p>
		 * If this method returns {@code true}, this adapter receives {@link #onItemDragFinished(int, int)}
		 * callback in a near feature.
		 *
		 * @param currentPosition Current position of the associated view holder.
		 * @param targetPosition  Target position at which could be the associated view holder dropped.
		 * @return {@code True} if the holder can be dropped at the target position, {@code false}
		 * otherwise.
		 * @see #onItemDragFinished(int, int)
		 */
		boolean canDropItemOver(int currentPosition, int targetPosition);

		/**
		 * Called by the drag helper whenever drag gesture for item associated with the specified
		 * <var>fromPosition</var> has finished.
		 *
		 * @param fromPosition The position from which has been the associated view holder started
		 *                     to be dragged.
		 * @param toPosition   The position at which has been the associated view holder dropped.
		 * @see #canDropItemOver(int, int)
		 * @see #onItemDragStarted(int)
		 */
		void onItemDragFinished(int fromPosition, int toPosition);
	}

	/**
	 * Required interface for all {@link RecyclerView.ViewHolder ViewHolders} which want to support
	 * drag feature for theirs corresponding item views.
	 *
	 * @author Martin Albedinsky
	 */
	public interface DragViewHolder extends InteractiveViewHolder {

		/**
		 * Called by the drag helper whenever drag gesture for view of this holder is started.
		 *
		 * @see #onDragFinished(int, int)
		 * @see #onDragCanceled()
		 * @see ItemDragHelper.Callback#onSelectedChanged(RecyclerView.ViewHolder, int)
		 */
		void onDragStarted();

		/**
		 * Called by the drag helper whenever drag gesture for view of this holder is finished.
		 *
		 * @param fromPosition The position from which has been the view holder dragged.
		 * @param toPosition   The position at which has been the view holder dropped.
		 * @see #onDragStarted()
		 * @see ItemDragHelper.Callback#clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		void onDragFinished(int fromPosition, int toPosition);

		/**
		 * This method is never called by the drag helper, but is left here only for convenience.
		 */
		void onDragCanceled();
	}

	/**
	 * Listener which may be used to receive callbacks about <b>started</b>, <b>finished</b> or
	 * <b>canceled</b> drag gesture for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @author Martin Albedinsky
	 */
	public interface OnDragListener {

		/**
		 * Invoked whenever drag gesture is started for the given <var>viewHolder</var>.
		 *
		 * @param dragHelper The item helper controlling the drag gesture for the view holder.
		 * @param viewHolder The view holder for which has been the drag gesture started.
		 * @see #onDragFinished(ItemDragHelper, RecyclerView.ViewHolder, int, int)
		 */
		void onDragStarted(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder);

		/**
		 * Invoked whenever drag gesture is finished for the given <var>viewHolder</var>.
		 *
		 * @param dragHelper   The item helper controlling the drag gesture for the view holder.
		 * @param viewHolder   The view holder for which has been the drag gesture finished.
		 * @param fromPosition The position from which has been the view holder dragged.
		 * @param toPosition   The position at which has been the view holder dropped.
		 * @see #onDragStarted(ItemDragHelper, RecyclerView.ViewHolder)
		 */
		void onDragFinished(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder, int fromPosition, int toPosition);

		/**
		 * Invoked whenever drag gesture is canceled for the given <var>viewHolder</var>.
		 *
		 * @param dragHelper The item helper controlling the drag gesture for the view holder.
		 * @param viewHolder The view holder for which has been the drag gesture canceled.
		 */
		void onDragCanceled(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ItemDragHelper.
	 */
	public ItemDragHelper() {
		this(new DragInteractor());
	}

	/**
	 * Creates a new instance of ItemDragHelper with the specified <var>interactor</var>.
	 *
	 * @param interactor The interactor that will receive and handle drag gesture related events.
	 */
	private ItemDragHelper(@NonNull DragInteractor interactor) {
		super(interactor);
	}
	 
	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Makes movement flags for the drag gesture.
	 *
	 * @param movementFlags The desired movement flags. One of flags defined by
	 *                      {@link Movement @Movement} annotation.
	 * @return Drag flags according to the given movement flags.
	 * @see ItemDragHelper.Callback#makeMovementFlags(int, int)
	 */
	public static int makeDragFlags(@Movement final int movementFlags) {
		return DragInteractor.makeMovementFlags(movementFlags, 0);
	}

	/**
	 * Sets a fraction that the user should move the holder's {@link android.view.View View} to be
	 * considered as it is dragged.
	 * <p>
	 * Default value: {@link #MOVE_THRESHOLD}
	 *
	 * @param threshold The desired threshold from the range {@code [0.0, 1.0]}.
	 * @see #getDragThreshold()
	 * @see ItemDragHelper.Callback#getMoveThreshold(RecyclerView.ViewHolder)
	 */
	public void setDragThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
		this.mInteractor.dragThreshold = threshold;
	}

	/**
	 * Returns the fraction that the user should move the holder's {@link android.view.View View} to
	 * be considered as it is dragged.
	 *
	 * @return The drag threshold from the range {@code [0.0, 1.0]}.
	 * @see #setDragThreshold(float)
	 */
	@FloatRange(from = 0.0f, to = 1.0f)
	public float getDragThreshold() {
		return mInteractor.dragThreshold;
	}

	/**
	 * Registers a callback to be invoked whenever drag gesture is <b>started</b>, <b>finished</b>
	 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @param listener The desired listener callback to add.
	 * @see #removeOnDragListener(OnDragListener)
	 */
	public void addOnDragListener(@NonNull final OnDragListener listener) {
		mInteractor.addListener(listener);
	}

	/**
	 * Removes the given swipe <var>listener</var> from the registered listeners.
	 *
	 * @param listener The desired listener to remove.
	 * @see #addOnDragListener(OnDragListener)
	 */
	public void removeOnDragListener(@NonNull final OnDragListener listener) {
		mInteractor.removeListener(listener);
	}

	/**
	 * Starts tracking of the drag gesture if it is not active at this time.
	 * <p>
	 * This method should be called whenever a drag handle associated with a specific {@link RecyclerView.ViewHolder}
	 * has been selected by the user in order to initiate drag for the view of that holder.
	 * <p>
	 * The gesture tracking will be stopped automatically whenever the user releases the dragged
	 * view so this method need to be called for each new "drag session".
	 *
	 * @return {@code True} if drag gesture tracking has been started, {@code false} otherwise.
	 * @see #isActive()
	 */
	public boolean startDragTracking() {
		return mInteractor.startTracking();
	}
	 
	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ItemInteractor} implementation used by {@link ItemDragHelper} to handle drag gesture
	 * related callbacks and to delegate drag events to the view holder that is being dragged and
	 * also to it parent adapter in order to properly move dragged items in the adapter's data set.
	 */
	static final class DragInteractor extends RecyclerViewItemHelper.ItemInteractor {

		/**
		 * Fraction that the user should move the View to be considered as it is dragged.
		 *
		 * @see #getMoveThreshold(RecyclerView.ViewHolder)
		 */
		float dragThreshold = MOVE_THRESHOLD;

		/**
		 * Adapter providing draggable item views attached to this interactor.
		 *
		 * @see #onAdapterAttached(RecyclerView.Adapter)
		 */
		private DragAdapter dragAdapter;

		/**
		 * List containing all registered {@link OnDragListener}.
		 *
		 * @see #addListener(OnDragListener)
		 * @see #removeListener(OnDragListener)
		 */
		private List<OnDragListener> listeners;

		/**
		 * Boolean flag indicating whether tracking of the drag gesture is active or not.
		 * If active, {@link #getMovementFlags(RecyclerView, RecyclerView.ViewHolder)} is delegated
		 * to the attached adapter.
		 *
		 * @see #startDragTracking()
		 */
		private boolean tracking;

		/**
		 * Boolean flag indicating whether the drag gesture is active at this time or not.
		 * If active, the user is dragging one of items in the associated {@link RecyclerView}.
		 *
		 * @see #onSelectedChanged(RecyclerView.ViewHolder, int)
		 * @see #clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		private boolean dragging;

		/**
		 * Position for which has been the drag gesture started.
		 *
		 * @see #onSelectedChanged(RecyclerView.ViewHolder, int)
		 */
		private int draggingFromPosition;

		/**
		 * Position from which is the view holder moving during the current "move session".
		 * The "move session" is only as "long" as move between two neighbor items.
		 *
		 * @see #onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)
		 */
		private int movingFromPosition;

		/**
		 * Position to which is the view holder moving during the current "move session".
		 * The "move session" is only as "long" as move between two neighbor items.
		 *
		 * @see #onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)
		 */
		private int movingToPosition;

		/**
		 */
		@Override
		protected boolean canAttachToAdapter(@NonNull final RecyclerView.Adapter adapter) {
			return adapter instanceof DragAdapter;
		}

		/**
		 */
		@Override
		protected void onAdapterAttached(@NonNull final RecyclerView.Adapter adapter) {
			super.onAdapterAttached(adapter);
			this.dragAdapter = (DragAdapter) adapter;
		}

		/**
		 */
		@Override
		protected void onAdapterDetached(@NonNull final RecyclerView.Adapter adapter) {
			super.onAdapterDetached(adapter);
			this.dragAdapter = null;
		}

		/**
		 * Registers a callback to be invoked whenever drag gesture is <b>started</b>, <b>finished</b>
		 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
		 *
		 * @param listener The desired listener to add.
		 * @see #removeListener(OnDragListener)
		 */
		void addListener(@NonNull final OnDragListener listener) {
			if (listeners == null) listeners = new ArrayList<>(1);
			if (!listeners.contains(listener)) listeners.add(listener);
		}

		/**
		 * Removes the given drag <var>listener</var> from the registered listeners.
		 *
		 * @param listener The desired listener to remove.
		 * @see #addListener(OnDragListener)
		 */
		void removeListener(@NonNull final OnDragListener listener) {
			if (listeners != null) listeners.remove(listener);
		}

		/**
		 * Notifies all registered {@link OnDragListener} that the drag gesture for the given
		 * <var>viewHolder</var> has been started.
		 *
		 * @param viewHolder The view holder for which the drag has started.
		 */
		@VisibleForTesting
		void notifyDragFStarted(final RecyclerView.ViewHolder viewHolder) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnDragListener listener : listeners) {
					listener.onDragStarted((ItemDragHelper) helper, viewHolder);
				}
			}
		}

		/**
		 * Notifies all registered {@link OnDragListener} that the drag gesture for the given
		 * <var>viewHolder</var> has been finished/completed.
		 *
		 * @param viewHolder   The view holder for which the drag has finished.
		 * @param fromPosition The position from which has been the view holder dragged.
		 * @param toPosition   The position at which has been the view holder dropped.
		 */
		@VisibleForTesting
		void notifyDragFinished(final RecyclerView.ViewHolder viewHolder, final int fromPosition, final int toPosition) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnDragListener listener : listeners) {
					listener.onDragFinished((ItemDragHelper) helper, viewHolder, fromPosition, toPosition);
				}
			}
		}

		/**
		 * Notifies all registered {@link OnDragListener} that the drag gesture for the given
		 * <var>viewHolder</var> has been canceled.
		 *
		 * @param viewHolder The view holder for which the drag has canceled.
		 */
		@VisibleForTesting
		@SuppressWarnings("unused")
		void notifyDragFCanceled(final RecyclerView.ViewHolder viewHolder) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnDragListener listener : listeners) {
					listener.onDragCanceled((ItemDragHelper) helper, viewHolder);
				}
			}
		}

		/**
		 */
		@Override
		protected void setEnabled(final boolean enabled) {
			super.setEnabled(enabled);
			this.tracking = false;
			this.dragging = false;
		}

		/**
		 * Starts tracking of the drag gesture if it is not active at this time. This method should
		 * be called whenever a drag gesture is about to be initiated.
		 *
		 * @return {@code True} if tracking has been activated, {@code false} otherwise.
		 * @see #isActive()
		 */
		boolean startTracking() {
			if (enabled && !tracking) {
				this.tracking = true;
			}
			return tracking;
		}

		/**
		 */
		@Override
		public boolean isActive() {
			return dragging;
		}

		/**
		 */
		@Override
		public int getMovementFlags(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			return tracking && dragAdapter != null ? dragAdapter.getItemDragFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(@Nullable final RecyclerView.ViewHolder viewHolder, final int actionState) {
			super.onSelectedChanged(viewHolder, actionState);
			switch (actionState) {
				case ItemDragHelper.ACTION_STATE_DRAG:
					if (viewHolder instanceof DragViewHolder) {
						this.dragging = true;
						((DragViewHolder) viewHolder).onDragStarted();
						this.dragAdapter.onItemDragStarted(draggingFromPosition = viewHolder.getAdapterPosition());
						notifyDragFStarted(viewHolder);
					}
					break;
			}
		}

		/**
		 */
		@Override
		public boolean onMove(
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				@NonNull final RecyclerView.ViewHolder target
		) {
			if (viewHolder instanceof DragViewHolder && target instanceof DragViewHolder) {
				final int fromPosition = viewHolder.getAdapterPosition();
				final int toPosition = target.getAdapterPosition();
				if (fromPosition != toPosition && (fromPosition != movingFromPosition || toPosition != movingToPosition)) {
					dragAdapter.onMoveItem(movingFromPosition = fromPosition, movingToPosition = toPosition);
				}
				return true;
			}
			return false;
		}

		/**
		 */
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
			// Ignored for this interactor-callback implementation.
		}

		/**
		 */
		@Override
		public float getMoveThreshold(@NonNull final RecyclerView.ViewHolder viewHolder) {
			return dragThreshold;
		}

		/**
		 */
		@Override
		public boolean canDropOver(
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder current,
				@NonNull final RecyclerView.ViewHolder target
		) {
			return current instanceof DragViewHolder && target instanceof DragViewHolder && dragAdapter.canDropItemOver(
					current.getAdapterPosition(),
					target.getAdapterPosition()
			);
		}

		/**
		 */
		@Override
		public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			super.clearView(recyclerView, viewHolder);
			if (viewHolder instanceof DragViewHolder) {
				this.dragging = false;
				this.tracking = false;
				final int draggingToPosition = viewHolder.getAdapterPosition();
				((DragViewHolder) viewHolder).onDragFinished(draggingFromPosition, draggingToPosition);
				this.dragAdapter.onItemDragFinished(draggingFromPosition, draggingToPosition);
				notifyDragFinished(viewHolder, draggingFromPosition, draggingToPosition);
				this.draggingFromPosition = movingFromPosition = movingToPosition = RecyclerView.NO_POSITION;
			}
		}
	}
}
