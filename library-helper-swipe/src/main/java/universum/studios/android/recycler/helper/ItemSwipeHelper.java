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

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerViewItemHelper} that may be used to support <b>swipe</b> feature for items
 * displayed in a {@link RecyclerView} widget.
 *
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelper extends RecyclerViewItemHelper<ItemSwipeHelper.SwipeCallback> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemSwipeHelper";

	/**
	 * Default duration of animation used to restore position of the holder's item view.
	 *
	 * @see #restoreHolderForAdapter(RecyclerView.ViewHolder, int, RecyclerView.Adapter, Runnable)
	 */
	private static final long RESTORE_HOLDER_ANIMATION_DURATION = 300;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * todo:
	 *
	 * @author Martin Albedinsky
	 *
	 * @see #attachAdapter(SwipeableAdapter)
	 */
	public interface SwipeableAdapter {

		/**
		 * todo:
		 *
		 * @param position
		 * @return
		 * @see ItemSwipeHelper#makeSwipeFlags(int)
		 */
		int getItemSwipeFlags(int position);
	}

	/**
	 * todo:
	 *
	 * @author Martin Albedinsky
	 */
	public interface SwipeableViewHolder {

		/**
		 * todo:
		 *
		 * @return
		 */
		@NonNull
		View getSwipeView();

		/**
		 * todo:
		 *
		 * @param canvas
		 * @param dX
		 * @param dY
		 * @param isCurrentlyActive
		 */
		void onDraw(@NonNull Canvas canvas, float dX, float dY, boolean isCurrentlyActive);

		/**
		 * todo:
		 *
		 * @param canvas
		 * @param dX
		 * @param dY
		 * @param isCurrentlyActive
		 */
		void onDrawOver(@NonNull Canvas canvas, float dX, float dY, boolean isCurrentlyActive);

		/**
		 * todo:
		 */
		void onSwipeStarted();

		/**
		 * todo:
		 *
		 * @param direction
		 */
		void onSwipeFinished(@Direction int direction);

		/**
		 * todo:
		 */
		void onSwipeCanceled();
	}

	/**
	 * todo:
	 *
	 * @author Martin Albedinsky
	 * @see #addOnSwipeListener(OnSwipeListener)
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public interface OnSwipeListener {

		/**
		 * todo:
		 *
		 * @param swipeHelper
		 * @param viewHolder
		 */
		void onSwipeStarted(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder);

		/**
		 * todo:
		 *
		 * @param swipeHelper
		 * @param viewHolder
		 * @param direction
		 */
		void onSwipeFinished(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder, @Direction int direction);

		/**
		 * todo:
		 *
		 * @param swipeHelper
		 * @param viewHolder
		 */
		void onSwipeCanceled(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * List containing all registered {@link OnSwipeListener}.
	 *
	 * @see #addOnSwipeListener(OnSwipeListener)
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	private List<OnSwipeListener> mSwipeListeners;

	/**
	 * Duration of animation used to restore position of the holder's item view to its initial state.
	 */
	private long mRestoreHolderAnimationDuration = RESTORE_HOLDER_ANIMATION_DURATION;

	/**
	 * Interpolator for restore holder's item view animation.
	 */
	private Interpolator mRestoreHolderAnimationInterpolator = new FastOutSlowInInterpolator();

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ItemSwipeHelper.
	 */
	public ItemSwipeHelper() {
		this(new SwipeCallback());
	}

	/**
	 * Creates a new instance of ItemSwipeHelper with the specified <var>callback</var>.
	 *
	 * @param callback The callback used to receive and handle swipe related events.
	 */
	private ItemSwipeHelper(final SwipeCallback callback) {
		super(callback);
		callback.setHelper(this);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Makes flags for the swipe gesture.
	 *
	 * @param movementFlags The desired movement flags. One of {@link #START}, {@link #END} or theirs
	 *                      combination.
	 * @return Swipe flags according to the given movement flags.
	 * @see android.support.v7.widget.helper.ItemTouchHelper.Callback#makeMovementFlags(int, int)
	 */
	public static int makeSwipeFlags(@Movement final int movementFlags) {
		return SwipeCallback.makeMovementFlags(0, movementFlags);
	}

	/**
	 * Attaches the given swipeable <var>adapter</var> to this helper.
	 * <p>
	 * This helper will delegate to the specified adapter or to its associated view holders all swipe
	 * gesture related events/callbacks whenever appropriate in order to support swipe feature for
	 * the specified adapter.
	 * <p>
	 * All view holders of the specified adapter which want to support swipe feature for theirs
	 * corresponding item views must implement {@link SwipeableViewHolder} interface. Other view
	 * holder implementation will be ignored by the ItemSwipeHelper API.
	 *
	 * @param adapter The desired adapter of which item views can be swiped. May be {@code null} to
	 *                clear the current adapter attached to this helper.
	 * @see SwipeableViewHolder
	 * @see OnSwipeListener
	 */
	public void attachAdapter(@Nullable final SwipeableAdapter adapter) {
		mCallback.attachAdapter(adapter);
	}

	/**
	 * Registers a callback to be invoked when an item swipe related event occurs.
	 *
	 * @param listener The desired listener callback to add.
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public void addOnSwipeListener(@NonNull final OnSwipeListener listener) {
		if (mSwipeListeners == null) mSwipeListeners = new ArrayList<>(1);
		if (!mSwipeListeners.contains(listener)) mSwipeListeners.add(listener);
	}

	/**
	 * Removes the given <var>listener</var> from the registered listeners.
	 *
	 * @param listener The desired listener callback to remove.
	 * @see #addOnSwipeListener(OnSwipeListener)
	 */
	public void removeOnSwipeListener(@NonNull final OnSwipeListener listener) {
		if (mSwipeListeners != null) mSwipeListeners.remove(listener);
	}

	/**
	 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
	 * <var>viewHolder</var> has been started.
	 *
	 * @param viewHolder The view holder for which the swipe has started.
	 */
	@SuppressWarnings("WeakerAccess")
	void notifySwipeStarted(final RecyclerView.ViewHolder viewHolder) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (final OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeStarted(this, viewHolder);
			}
		}
	}

	/**
	 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
	 * <var>viewHolder</var> has been finished/completed.
	 *
	 * @param viewHolder The view holder for which the swipe has finished.
	 * @param direction  Direction in which the holder has been swiped.
	 */
	@SuppressWarnings("WeakerAccess")
	void notifySwipeFinished(final RecyclerView.ViewHolder viewHolder, final int direction) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (final OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeFinished(this, viewHolder, direction);
			}
		}
	}

	/**
	 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
	 * <var>viewHolder</var> has been canceled.
	 *
	 * @param viewHolder The view holder for which the swipe has canceled.
	 */
	@SuppressWarnings("WeakerAccess")
	void notifySwipeCanceled(final RecyclerView.ViewHolder viewHolder) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (final OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeCanceled(this, viewHolder);
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param duration
	 */
	public void setRestoreHolderAnimationDuration(final long duration) {
		this.mRestoreHolderAnimationDuration = duration;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public long getRestoreHolderAnimationDuration() {
		return mRestoreHolderAnimationDuration;
	}

	/**
	 * todo:
	 *
	 * @param interpolator
	 */
	public void setRestoreHolderAnimationInterpolator(@NonNull final Interpolator interpolator) {
		this.mRestoreHolderAnimationInterpolator = interpolator;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	@NonNull
	public Interpolator getRestoreHolderAnimationInterpolator() {
		return mRestoreHolderAnimationInterpolator;
	}

	/**
	 * Same as {@link #restoreHolderForAdapter(RecyclerView.ViewHolder, int, RecyclerView.Adapter, Runnable)}
	 * with {@code null} <var>adapter</var> and <var>callback</var>.
	 */
	public void restoreHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @Direction final int direction) {
		restoreHolderForAdapter(viewHolder, direction, null, null);
	}

	/**
	 * todo:
	 *
	 * @param viewHolder
	 * @param direction
	 * @param adapter
	 * @param callback
	 */
	public void restoreHolderForAdapter(
			@NonNull final RecyclerView.ViewHolder viewHolder,
			@Direction final int direction,
			@Nullable final RecyclerView.Adapter adapter,
			@Nullable final Runnable callback
	) {
		if (viewHolder instanceof SwipeableViewHolder) {
			// Restore holder's swipe view and when restore animation finishes notify the adapter
			// that item at the holder's position has changed so view for the item is again properly
			// drawn by the parent RecyclerView.
			final View swipeView = ((SwipeableViewHolder) viewHolder).getSwipeView();
			final ViewPropertyAnimator animator;
			switch (direction) {
				case START:
				case END:
					if (swipeView.getTranslationX() == 0) return;
					animator = swipeView.animate().translationX(0);
					break;
				case DOWN:
				case UP:
				case LEFT:
				case RIGHT:
				default:
					// Do not handle these directions.
					return;
			}
			animator.setDuration(mRestoreHolderAnimationDuration).setInterpolator(mRestoreHolderAnimationInterpolator).start();
			if (adapter != null) {
				final int itemPosition = viewHolder.getAdapterPosition();
				swipeView.postDelayed(new Runnable() {

					/**
					 */
					@Override
					public void run() {
						adapter.notifyItemChanged(itemPosition);
						if (callback != null) {
							callback.run();
						}
					}
				}, mRestoreHolderAnimationDuration);
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link RecyclerViewItemHelper.BaseCallback} implementation used by {@link ItemSwipeHelper}
	 * to control swipe gesture and delegate swipe events to the view holder that is being swiped.
	 */
	static final class SwipeCallback extends RecyclerViewItemHelper.BaseCallback {

		/**
		 * Parent helper which uses this callback instance.
		 */
		private ItemSwipeHelper helper;

		/**
		 * Adapter, attached to the parent helper, providing swipeable item views.
		 */
		private SwipeableAdapter adapter;

		/**
		 * Sets a parent helper for this callback.
		 *
		 * @param helper The helper which uses this callback.
		 */
		void setHelper(final ItemSwipeHelper helper) {
			this.helper = helper;
		}

		/**
		 * Attaches the given <var>adapter</var> to this callback.
		 *
		 * @param adapter The adapter of which item views can be swiped.
		 */
		void attachAdapter(final SwipeableAdapter adapter) {
			this.adapter = adapter;
		}

		/**
		 */
		@Override
		public int getMovementFlags(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			return enabled && adapter != null ? adapter.getItemSwipeFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(@Nullable final RecyclerView.ViewHolder viewHolder, final int actionState) {
			if (viewHolder != null) {
				final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
				getDefaultUIUtil().onSelected(swipeableViewHolder.getSwipeView());
				swipeableViewHolder.onSwipeStarted();
				helper.notifySwipeStarted(viewHolder);
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
			// Ignored for this helper-callback implementation.
			return false;
		}

		/**
		 */
		@Override
		public void onChildDraw(
				@NonNull final Canvas canvas,
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				final float dX,
				final float dY,
				final int actionState,
				final boolean isCurrentlyActive
		) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().onDraw(canvas, recyclerView, swipeableViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeableViewHolder.onDraw(canvas, dX, dY, isCurrentlyActive);
		}

		/**
		 */
		@Override
		public void onChildDrawOver(
				@NonNull final Canvas canvas,
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				final float dX,
				final float dY,
				final int actionState,
				final boolean isCurrentlyActive
		) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().onDrawOver(canvas, recyclerView, swipeableViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeableViewHolder.onDrawOver(canvas, dX, dY, isCurrentlyActive);
		}

		/**
		 */
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			swipeableViewHolder.onSwipeFinished(direction);
			helper.notifySwipeFinished(viewHolder, direction);
		}

		/**
		 */
		@Override
		public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().clearView(swipeableViewHolder.getSwipeView());
			swipeableViewHolder.onSwipeCanceled();
			helper.notifySwipeCanceled(viewHolder);
		}
	}

	/**
	 * A {@link DefaultItemAnimator} extension which overrides default implementation of
	 * {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}
	 * in a way that does not cause any drawing artifacts to occur when state of item view for swiped
	 * view holder is about to be restored via {@link #restoreHolderForAdapter(RecyclerView.ViewHolder, int, RecyclerView.Adapter, Runnable)}.
	 * <p>
	 * This animator should be used for {@link RecyclerView} to which is {@link ItemSwipeHelper} attached.
	 *
	 * @author Martin Albedinsky
	 */
	public static class SwipeItemAnimator extends DefaultItemAnimator {

		/**
		 */
		@Override
		public boolean animateChange(
				@Nullable final RecyclerView.ViewHolder oldHolder,
				@Nullable final RecyclerView.ViewHolder newHolder,
				final int fromX,
				final int fromY,
				final int toX,
				final int toY
		) {
			if (fromX == toX && fromY == toY && oldHolder != null && newHolder != null) {
				if (newHolder.equals(oldHolder)) {
					dispatchChangeFinished(newHolder, false);
				} else {
					dispatchChangeFinished(oldHolder, true);
					dispatchChangeFinished(newHolder, false);
				}
				return false;
			}
			return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
		}
	}
}
