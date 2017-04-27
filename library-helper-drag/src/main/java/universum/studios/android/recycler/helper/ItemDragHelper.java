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
 * Class that todo:
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
	 * todo:
	 */
	public interface DragAdapter {

		/**
		 * todo:
		 *
		 * @param position
		 * @return
		 */
		int getItemDragFlags(int position);

		/**
		 * todo:
		 *
		 * @param position
		 */
		void onItemDragStarted(int position);

		/**
		 * todo:
		 *
		 * @param fromPosition
		 * @param toPosition
		 */
		void onMoveItem(int fromPosition, int toPosition);

		/**
		 * todo:
		 *
		 * @param currentPosition
		 * @param targetPosition
		 * @return
		 */
		boolean canDropItemOver(int currentPosition, int targetPosition);

		/**
		 * todo:
		 *
		 * @param fromPosition
		 * @param toPosition
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
		 * todo:
		 */
		void onDragStarted();

		/**
		 * todo:
		 */
		void onDragFinished();

		/**
		 * todo:
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
		 */
		void onDragStarted(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder);

		/**
		 * Invoked whenever drag gesture is finished for the given <var>viewHolder</var>.
		 *
		 * @param dragHelper   The item helper controlling the drag gesture for the view holder.
		 * @param viewHolder   The view holder for which has been the drag gesture finished.
		 * @param fromPosition todo:
		 * @param toPosition   todo:
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
	 * @see android.support.v7.widget.helper.ItemTouchHelper.Callback#makeMovementFlags(int, int)
	 */
	public static int makeDragFlags(@Movement final int movementFlags) {
		return ItemSwipeHelper.SwipeInteractor.makeMovementFlags(movementFlags, 0);
	}

	/**
	 * Sets a fraction that the user should move the holder's {@link android.view.View View} to be
	 * considered as it is dragged.
	 * <p>
	 * Default value: {@link #MOVE_THRESHOLD}
	 *
	 * @param threshold The desired threshold from the range {@code [0.0, 1.0]}.
	 * @see #getDragThreshold()
	 * @see ItemSwipeHelper.Callback#getMoveThreshold(RecyclerView.ViewHolder)
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
	 * todo:
	 *
	 * @return
	 */
	public boolean startDrag() {
		return mInteractor.activate();
	}
	 
	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * todo:
	 */
	static final class DragInteractor extends RecyclerViewItemHelper.ItemInteractor {

		/**
		 * todo:
		 */
		float dragThreshold = MOVE_THRESHOLD;

		/**
		 * todo:
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
		 * todo:
		 */
		private boolean dragging;

		/**
		 * todo:
		 */
		private int draggingFromPosition;

		/**
		 * todo:
		 */
		private int movingFromPosition;

		/**
		 * todo:
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
		void notifySwipeStarted(final RecyclerView.ViewHolder viewHolder) {
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
		 * @param fromPosition todo:
		 * @param toPosition   todo:
		 */
		@VisibleForTesting
		void notifySwipeFinished(final RecyclerView.ViewHolder viewHolder, final int fromPosition, final int toPosition) {
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
		void notifySwipeCanceled(final RecyclerView.ViewHolder viewHolder) {
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
			this.dragging = false;
		}

		/**
		 * todo:
		 */
		boolean activate() {
			if (enabled && !dragging) {
				this.dragging = true;
			}
			return dragging;
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
			return dragging && dragAdapter != null ? dragAdapter.getItemDragFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(@Nullable final RecyclerView.ViewHolder viewHolder, final int actionState) {
			super.onSelectedChanged(viewHolder, actionState);
			switch (actionState) {
				case ItemDragHelper.ACTION_STATE_DRAG:
					if (viewHolder instanceof DragViewHolder) {
						((DragViewHolder) viewHolder).onDragStarted();
						this.dragAdapter.onItemDragStarted(draggingFromPosition = viewHolder.getAdapterPosition());
						notifySwipeStarted(viewHolder);
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
				((DragViewHolder) viewHolder).onDragFinished();
				this.dragging = false;
				final int draggingToPosition = viewHolder.getAdapterPosition();
				this.dragAdapter.onItemDragFinished(draggingFromPosition, draggingToPosition);
				notifySwipeFinished(viewHolder, draggingFromPosition, draggingToPosition);
				this.draggingFromPosition = movingFromPosition = movingToPosition = RecyclerView.NO_POSITION;
			}
		}
	}
}
