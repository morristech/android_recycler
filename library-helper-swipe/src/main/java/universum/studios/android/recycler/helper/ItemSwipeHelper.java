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
 * A {@link RecyclerViewItemHelper} implementation ...
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

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * todo
	 *
	 * @author Martin Albedinsky
	 */
	public interface SwipeableAdapter {

		/**
		 * todo:
		 *
		 * @param position
		 * @return
		 */
		int getItemSwipeFlags(int position);
	}

	/**
	 * todo
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
	 * todo
	 *
	 * @author Martin Albedinsky
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
	 * List containing registered swipe listeners.
	 */
	private List<OnSwipeListener> mSwipeListeners;

	/**
	 * todo:
	 */
	private long mRestoreHolderAnimationDuration = 300;

	/**
	 * todo:
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
	private ItemSwipeHelper(SwipeCallback callback) {
		super(callback);
		callback.setHelper(this);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * todo:
	 *
	 * @param movementFlags
	 * @return
	 */
	public static int makeSwipeFlags(@Movement int movementFlags) {
		return SwipeCallback.makeMovementFlags(0, movementFlags);
	}

	/**
	 * todo:
	 *
	 * @param adapter
	 */
	public void attachAdapter(@Nullable SwipeableAdapter adapter) {
		mCallback.attachAdapter(adapter);
	}

	/**
	 * Registers a callback to be invoked when an item swipe related event occurs.
	 *
	 * @param listener The desired listener callback to add.
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public void addOnSwipeListener(@NonNull OnSwipeListener listener) {
		if (mSwipeListeners == null) mSwipeListeners = new ArrayList<>(1);
		if (!mSwipeListeners.contains(listener)) mSwipeListeners.add(listener);
	}

	/**
	 * Removes the given <var>listener</var> from the registered listeners.
	 *
	 * @param listener The desired listener callback to remove.
	 * @see #addOnSwipeListener(OnSwipeListener)
	 */
	public void removeOnSwipeListener(@NonNull OnSwipeListener listener) {
		if (mSwipeListeners != null) mSwipeListeners.remove(listener);
	}

	/**
	 * todo:
	 *
	 * @param viewHolder
	 */
	void notifySwipeStarted(RecyclerView.ViewHolder viewHolder) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeStarted(this, viewHolder);
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param viewHolder
	 * @param direction
	 */
	void notifySwipeFinished(RecyclerView.ViewHolder viewHolder, int direction) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeFinished(this, viewHolder, direction);
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param viewHolder
	 */
	void notifySwipeCanceled(RecyclerView.ViewHolder viewHolder) {
		if (mSwipeListeners != null && !mSwipeListeners.isEmpty()) {
			for (OnSwipeListener listener : mSwipeListeners) {
				listener.onSwipeCanceled(this, viewHolder);
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param duration
	 */
	public void setRestoreHolderAnimationDuration(long duration) {
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
	public void setRestoreHolderAnimationInterpolator(@NonNull Interpolator interpolator) {
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
	public void restoreHolder(@NonNull RecyclerView.ViewHolder viewHolder, @Direction int direction) {
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
	public void restoreHolderForAdapter(@NonNull RecyclerView.ViewHolder viewHolder, @Direction int direction, @Nullable final RecyclerView.Adapter adapter, @Nullable final Runnable callback) {
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
				default:
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
	 * todo:
	 */
	static final class SwipeCallback extends RecyclerViewItemHelper.BaseCallback {

		/**
		 * Parent helper which uses this callback instance.
		 */
		private ItemSwipeHelper helper;

		/**
		 * Data set with swipe items attached to this callback.
		 */
		private SwipeableAdapter dataSet;

		/**
		 * Sets a parent helper for this callback.
		 *
		 * @param helper The helper which uses this callback.
		 */
		void setHelper(ItemSwipeHelper helper) {
			this.helper = helper;
		}

		/**
		 * todo:
		 *
		 * @param dataSet
		 */
		void attachAdapter(SwipeableAdapter dataSet) {
			this.dataSet = dataSet;
		}

		/**
		 */
		@Override
		public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			return enabled && dataSet != null ? dataSet.getItemSwipeFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
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
		public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
			// Ignored for this helper-callback implementation.
			return false;
		}

		/**
		 */
		@Override
		public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().onDraw(c, recyclerView, swipeableViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeableViewHolder.onDraw(c, dX, dY, isCurrentlyActive);
		}

		/**
		 */
		@Override
		public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().onDrawOver(c, recyclerView, swipeableViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeableViewHolder.onDrawOver(c, dX, dY, isCurrentlyActive);
		}

		/**
		 */
		@Override
		public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			swipeableViewHolder.onSwipeFinished(direction);
			helper.notifySwipeFinished(viewHolder, direction);
		}

		/**
		 */
		@Override
		public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			final SwipeableViewHolder swipeableViewHolder = (SwipeableViewHolder) viewHolder;
			getDefaultUIUtil().clearView(swipeableViewHolder.getSwipeView());
			swipeableViewHolder.onSwipeCanceled();
			helper.notifySwipeCanceled(viewHolder);
		}
	}

	/**
	 * todo
	 *
	 * @author Martin Albedinsky
	 */
	public static class SwipeItemAnimator extends DefaultItemAnimator {

		/**
		 */
		@Override
		public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
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
