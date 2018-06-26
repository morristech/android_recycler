/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
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
 * A {@link OnDragListener} may be registered for the helper's {@link ItemDragHelper.Interactor} via
 * {@link ItemDragHelper.Interactor#addOnDragListener(OnDragListener)} in order to receive callbacks
 * about <b>started</b>, <b>finished</b> or <b>canceled</b> drag gesture for a particular
 * {@link RecyclerView.ViewHolder ViewHolder}. If the listener is no more needed it should be
 * unregistered via {@link ItemDragHelper.Interactor#removeOnDragListener(OnDragListener)}.
 *
 * @author Martin Albedinsky
 */
public final class ItemDragHelper extends RecyclerViewItemHelper<ItemDragHelper.Interactor> {

    /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemDragHelper";

	/**
	 * Interaction constant specific for {@link ItemDragHelper}.
	 *
	 * @see #ACTION_STATE_DRAG
	 */
	public static final int INTERACTION = ACTION_STATE_DRAG;

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
		 * data set from the specified <var>currentPosition</var> to the specified <var>targetPosition</var>.
		 *
		 * @param currentPosition The position from which should be the item moved in this adapter's
		 *                        data set.
		 * @param targetPosition  The position to which should be the item moved in this adapter's
		 *                        data set.
		 * @return {@code True} if item has been moved form the current position to the target one,
		 * {@code false} otherwise.
		 */
		boolean onMoveItem(int currentPosition, int targetPosition);

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
		this(new Interactor());
	}

	/**
	 * Creates a new instance of ItemDragHelper with the specified <var>interactor</var>.
	 *
	 * @param interactor The interactor that will receive and handle drag gesture related events.
	 */
	private ItemDragHelper(@NonNull Interactor interactor) {
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
		return Interactor.makeMovementFlags(movementFlags, 0);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ItemInteractor} implementation used by {@link ItemDragHelper} to handle drag gesture
	 * related callbacks and to delegate drag events to the view holder that is being dragged and
	 * also to it parent adapter in order to properly move dragged items in the adapter's data set.
	 */
	public static final class Interactor extends RecyclerViewItemHelper.ItemInteractor {

		/**
		 * Fraction that the user should move the View to be considered as it is dragged.
		 *
		 * @see #getMoveThreshold(RecyclerView.ViewHolder)
		 */
		private float dragThreshold = MOVE_THRESHOLD;

		/**
		 * Boolean flag indicating whether drag should be started whenever an item view is long
		 * pressed or not.
		 *
		 * @see #setLongPressDragEnabled(boolean)
		 */
		private boolean longPressDragEnabled = true;

		/**
		 * Adapter providing draggable item views attached to this interactor.
		 *
		 * @see #onAdapterAttached(RecyclerView.Adapter)
		 */
		@VisibleForTesting
		DragAdapter dragAdapter;

		/**
		 * List containing all registered {@link OnDragListener}.
		 *
		 * @see #addOnDragListener(OnDragListener)
		 * @see #removeOnDragListener(OnDragListener)
		 */
		private List<OnDragListener> listeners;

		/**
		 * Boolean flag indicating whether the drag gesture is active at this time or not.
		 * If active, the user is dragging one of items in the associated {@link RecyclerView}.
		 *
		 * @see #onSelectedChanged(RecyclerView.ViewHolder, int)
		 * @see #clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		@VisibleForTesting
		boolean dragging;

		/**
		 * Position for which has been the drag gesture started.
		 *
		 * @see #onSelectedChanged(RecyclerView.ViewHolder, int)
		 */
		@VisibleForTesting
		int draggingFromPosition = RecyclerView.NO_POSITION;

		/**
		 * Position from which is the view holder moving during the current "move session".
		 * The "move session" is only as "long" as move between two neighbor items.
		 *
		 * @see #onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)
		 */
		@VisibleForTesting
		int movingFromPosition = RecyclerView.NO_POSITION;

		/**
		 * Position to which is the view holder moving during the current "move session".
		 * The "move session" is only as "long" as move between two neighbor items.
		 *
		 * @see #onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)
		 */
		@VisibleForTesting
		int movingToPosition = RecyclerView.NO_POSITION;

		/**
		 * Creates a new instance of drag gesture Interactor.
		 */
		Interactor() {
			super();
		}

		/**
		 * Sets a boolean flag indicating whether the drag should be started whenever an item view
		 * is long pressed or not.
		 * <p>
		 * If disabled, a drag for a particular view holder needs to be started via
		 * {@link ItemDragHelper#startDrag(RecyclerView.ViewHolder)} manually.
		 * <p>
		 * Default value: {@code true}
		 *
		 * @param enabled {@code True} to enable automatic drag on long press, {@code false} to
		 *                disable it.
		 * @see #isLongPressDragEnabled()
		 */
		public void setLongPressDragEnabled(boolean enabled) {
			this.longPressDragEnabled = enabled;
		}

		/**
		 */
		@Override
		public boolean isLongPressDragEnabled() {
			return longPressDragEnabled;
		}

		/**
		 * Sets a fraction that the user should move the holder's {@link android.view.View View} to
		 * be considered as it is dragged.
		 * <p>
		 * Default value: {@link #MOVE_THRESHOLD}
		 *
		 * @param threshold The desired threshold from the range {@code [0.0, 1.0]}.
		 * @see #getDragThreshold()
		 * @see #getMoveThreshold(RecyclerView.ViewHolder)
		 * @see Interactor#getMoveThreshold(RecyclerView.ViewHolder)
		 */
		public void setDragThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
			this.dragThreshold = Math.min(Math.max(threshold, 0.0f), 1.0f);
		}

		/**
		 * Returns the fraction that the user should move the holder's {@link android.view.View View}
		 * to be considered as it is dragged.
		 *
		 * @return The drag threshold from the range {@code [0.0, 1.0]}.
		 * @see #setDragThreshold(float)
		 */
		@FloatRange(from = 0.0f, to = 1.0f)
		public float getDragThreshold() {
			return dragThreshold;
		}

		/**
		 */
		@Override
		public float getMoveThreshold(@NonNull final RecyclerView.ViewHolder viewHolder) {
			return dragThreshold;
		}

		/**
		 * Registers a callback to be invoked whenever drag gesture is <b>started</b>, <b>finished</b>
		 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
		 *
		 * @param listener The desired listener callback to add.
		 * @see #removeOnDragListener(OnDragListener)
		 */
		public void addOnDragListener(@NonNull final OnDragListener listener) {
			if (listeners == null) listeners = new ArrayList<>(1);
			if (!listeners.contains(listener)) listeners.add(listener);
		}

		/**
		 * Notifies all registered {@link OnDragListener} that the drag gesture for the given
		 * <var>viewHolder</var> has been started.
		 *
		 * @param viewHolder The view holder for which the drag has started.
		 */
		@VisibleForTesting
		void notifyDragStarted(final RecyclerView.ViewHolder viewHolder) {
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
		void notifyDragCanceled(final RecyclerView.ViewHolder viewHolder) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnDragListener listener : listeners) {
					listener.onDragCanceled((ItemDragHelper) helper, viewHolder);
				}
			}
		}

		/**
		 * Removes the given drag <var>listener</var> from the registered listeners.
		 *
		 * @param listener The desired listener to remove.
		 * @see #addOnDragListener(OnDragListener)
		 */
		public void removeOnDragListener(@NonNull final OnDragListener listener) {
			if (listeners != null) listeners.remove(listener);
		}

		/**
		 */
		@Override
		protected boolean canAttachAdapter(@NonNull final RecyclerView.Adapter adapter) {
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
			this.resetState();
		}

		/**
		 */
		@Override
		public void setEnabled(final boolean enabled) {
			super.setEnabled(enabled);
			this.resetState();
		}

		/**
		 * Resets current state of this interactor to the idle one.
		 */
		@VisibleForTesting
		void resetState() {
			this.dragging = false;
			this.draggingFromPosition = movingFromPosition = movingToPosition = RecyclerView.NO_POSITION;
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
			return shouldHandleInteraction() && viewHolder instanceof DragViewHolder ? dragAdapter.getItemDragFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(@Nullable final RecyclerView.ViewHolder viewHolder, final int actionState) {
			super.onSelectedChanged(viewHolder, actionState);
			if (shouldHandleInteraction() && viewHolder instanceof DragViewHolder) {
				switch (actionState) {
					case INTERACTION:
						this.dragging = true;
						((DragViewHolder) viewHolder).onDragStarted();
						this.dragAdapter.onItemDragStarted(draggingFromPosition = viewHolder.getAdapterPosition());
						notifyDragStarted(viewHolder);
						break;
					default:
						// Do not handle other action states.
						break;
				}
			}
		}

		/**
		 */
		@Override
		public boolean onMove(
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder current,
				@NonNull final RecyclerView.ViewHolder target
		) {
			if (shouldHandleInteraction() && current instanceof DragViewHolder && target instanceof DragViewHolder) {
				final int fromPosition = current.getAdapterPosition();
				final int toPosition = target.getAdapterPosition();
				if (fromPosition != toPosition && (fromPosition != movingFromPosition || toPosition != movingToPosition)) {
					return dragAdapter.onMoveItem(movingFromPosition = fromPosition, movingToPosition = toPosition);
				}
			}
			return false;
		}

		/**
		 */
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
			// Ignored for this interactor implementation.
		}

		/**
		 */
		@Override
		public boolean canDropOver(
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder current,
				@NonNull final RecyclerView.ViewHolder target
		) {
			return shouldHandleInteraction() && current instanceof DragViewHolder && target instanceof DragViewHolder && dragAdapter.canDropItemOver(
					current.getAdapterPosition(),
					target.getAdapterPosition()
			);
		}

		/**
		 */
		@Override
		public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			super.clearView(recyclerView, viewHolder);
			if (shouldHandleInteraction() && viewHolder instanceof DragViewHolder) {
				final int draggingToPosition = viewHolder.getAdapterPosition();
				((DragViewHolder) viewHolder).onDragFinished(draggingFromPosition, draggingToPosition);
				this.dragAdapter.onItemDragFinished(draggingFromPosition, draggingToPosition);
				notifyDragFinished(viewHolder, draggingFromPosition, draggingToPosition);
				this.resetState();
			}
		}
	}
}