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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerViewItemHelper} that may be used to support <b>swipe</b> feature for items
 * displayed in a {@link RecyclerView} widget. Instance of this helper may be simply created via
 * {@link #ItemSwipeHelper()} constructor.
 * <p>
 * In order to support swipe feature for a desired adapter, such adapter must implement {@link SwipeAdapter}
 * interface and needs to be attached to the helper via {@link #attachAdapter(SwipeAdapter)}. Also
 * all {@link RecyclerView.ViewHolder ViewHolders} of that adapter of which item views can be swiped
 * must implement {@link SwipeViewHolder} interface as other view holder implementations will be
 * ignored by the ItemSwipeHelper API.
 *
 * <h3>Swipe Callbacks</h3>
 * A {@link OnSwipeListener} may be registered via {@link #addOnSwipeListener(OnSwipeListener)} in
 * order to receive callbacks about <b>started</b>, <b>finished</b> or <b>canceled</b> swipe gesture
 * for a particular {@link RecyclerView.ViewHolder ViewHolder}. If the listener is no more needed it
 * should be unregistered via {@link #removeOnSwipeListener(OnSwipeListener)}.
 *
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelper extends RecyclerViewItemHelper<ItemSwipeHelper.SwipeInteractor> {

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
	public static final long RESTORE_HOLDER_ANIMATION_DURATION = 300;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Required interface for adapters which want to support swipe feature for its {@link RecyclerView.ViewHolder ViewHolders}.
	 *
	 * @author Martin Albedinsky
	 * @see #attachAdapter(SwipeAdapter)
	 */
	public interface SwipeAdapter {

		/**
		 * Called by the swipe helper to which is this adapter attached to obtain swipe flags for
		 * an item at the specified <var>position</var>.
		 *
		 * @param position Position of the item for which to obtain its swipe flags.
		 * @return Movement flags determining in which direction can be the item swiped.
		 * @see ItemSwipeHelper#makeSwipeFlags(int)
		 */
		int getItemSwipeFlags(int position);
	}

	/**
	 * Required interface for all {@link RecyclerView.ViewHolder ViewHolders} which want to support
	 * swipe feature for theirs corresponding item views.
	 *
	 * @author Martin Albedinsky
	 */
	public interface SwipeViewHolder {

		/**
		 * Returns the view that may be swiped.
		 * <p>
		 * Note that the swipe view must be either the item view of this holder or one of the item
		 * view's descendants.
		 *
		 * @return This holder's view.
		 */
		@NonNull
		View getSwipeView();

		/**
		 * Called by the swipe helper to allow this holder to draw its current swipe state in order
		 * to respond to user interactions.
		 *
		 * @param canvas            The canvas on which to draw.
		 * @param dX                The amount of horizontal displacement caused by user's swipe action.
		 * @param dY                The amount of vertical displacement caused by user's swipe action.
		 * @param isCurrentlyActive {@code True} if view of this holder is currently being controlled
		 *                          by the user, {@code false} if it is simply animating back to its
		 *                          original state.
		 * @see ItemTouchHelper.Callback#onChildDraw(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)
		 */
		void onDraw(@NonNull Canvas canvas, float dX, float dY, boolean isCurrentlyActive);

		/**
		 * Called by the swipe helper to allow this holder to draw its current swipe state in order
		 * to respond to user interactions.
		 *
		 * @param canvas            The canvas on which to draw.
		 * @param dX                The amount of horizontal displacement caused by user's swipe action.
		 * @param dY                The amount of vertical displacement caused by user's swipe action.
		 * @param isCurrentlyActive {@code True} if view of this holder is currently being controlled
		 *                          by the user, {@code false} if it is simply animating back to its
		 *                          original state.
		 * @see ItemTouchHelper.Callback#onChildDrawOver(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)
		 */
		void onDrawOver(@NonNull Canvas canvas, float dX, float dY, boolean isCurrentlyActive);

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is started.
		 *
		 * @see #onSwipeFinished(int)
		 * @see #onSwipeCanceled()
		 * @see ItemTouchHelper.Callback#onSelectedChanged(RecyclerView.ViewHolder, int)
		 */
		void onSwipeStarted();

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is finished in
		 * the specified <var>direction</var>.
		 *
		 * @param direction The direction in which has the swipe gestured finished. One of directions
		 *                  defined by {@link Direction @Direction} annotation.
		 * @see #onSwipeStarted()
		 * @see ItemTouchHelper.Callback#onSwiped(RecyclerView.ViewHolder, int)
		 */
		void onSwipeFinished(@Direction int direction);

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is canceled.
		 *
		 * @see #onSwipeStarted()
		 * @see ItemTouchHelper.Callback#clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		void onSwipeCanceled();
	}

	/**
	 * Listener which may be used to receive callbacks about <b>started</b>, <b>finished</b> or
	 * <b>canceled</b> swipe gesture for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @author Martin Albedinsky
	 * @see #addOnSwipeListener(OnSwipeListener)
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public interface OnSwipeListener {

		/**
		 * Invoked whenever swipe gesture is started for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The swipe helper controlling the swipe gesture for the view holder.
		 * @param viewHolder  The view holder for which has been the swipe gesture started.
		 */
		void onSwipeStarted(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder);

		/**
		 * Invoked whenever swipe gesture is finished for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The swipe helper controlling the swipe gesture for the view holder.
		 * @param viewHolder  The view holder for which has been the swipe gesture finished.
		 * @param direction   The direction in which has been the swipe gesture finished. One of
		 *                    directions defined by {@link Direction @Direction} annotation.
		 */
		void onSwipeFinished(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder, @Direction int direction);

		/**
		 * Invoked whenever swipe gesture is canceled for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The swipe helper controlling the swipe gesture for the view holder.
		 * @param viewHolder  The view holder for which has been the swipe gesture canceled.
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
	 * Duration of animation used to restore position of holder's item view to its initial state.
	 */
	private long mRestoreHolderAnimationDuration = RESTORE_HOLDER_ANIMATION_DURATION;

	/**
	 * Interpolator for animation used to restore position of holder's item view.
	 */
	private Interpolator mRestoreHolderAnimationInterpolator = new FastOutSlowInInterpolator();

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ItemSwipeHelper.
	 */
	public ItemSwipeHelper() {
		this(new SwipeInteractor());
	}

	/**
	 * Creates a new instance of ItemSwipeHelper with the specified <var>callback</var>.
	 *
	 * @param callback The callback used to receive and handle swipe related events.
	 */
	private ItemSwipeHelper(final SwipeInteractor callback) {
		super(callback);
		callback.setHelper(this);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Makes swipe movement flags for the swipe gesture.
	 *
	 * @param movementFlags The desired movement flags. One of {@link #START}, {@link #END} or theirs
	 *                      combination.
	 * @return Swipe flags according to the given movement flags.
	 * @see android.support.v7.widget.helper.ItemTouchHelper.Callback#makeMovementFlags(int, int)
	 */
	public static int makeSwipeFlags(@Movement final int movementFlags) {
		return SwipeInteractor.makeMovementFlags(0, movementFlags);
	}

	/**
	 * Attaches the given swipe <var>adapter</var> to this helper.
	 * <p>
	 * This helper will delegate to the specified adapter or to its associated view holders all swipe
	 * gesture related events/callbacks whenever appropriate in order to support swipe feature for
	 * the specified adapter.
	 * <p>
	 * All view holders of the specified adapter which want to support swipe feature for theirs
	 * corresponding item views must implement {@link SwipeViewHolder} interface. Other view
	 * holder implementation will be ignored by the ItemSwipeHelper API.
	 *
	 * @param adapter The desired adapter of which item views can be swiped. May be {@code null} to
	 *                clear the current adapter attached to this helper.
	 * @see SwipeViewHolder
	 * @see OnSwipeListener
	 */
	public void attachAdapter(@Nullable final SwipeAdapter adapter) {
		mInteractor.attachAdapter(adapter);
	}

	/**
	 * Registers a callback to be invoked whenever swipe gesture is <b>started</b>, <b>finished</b>
	 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @param listener The desired listener callback to add.
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public void addOnSwipeListener(@NonNull final OnSwipeListener listener) {
		mInteractor.addOnSwipeListener(listener);
	}

	/**
	 * Removes the given swipe <var>listener</var> from the registered listeners.
	 *
	 * @param listener The desired listener to remove.
	 * @see #addOnSwipeListener(OnSwipeListener)
	 */
	public void removeOnSwipeListener(@NonNull final OnSwipeListener listener) {
		mInteractor.removeOnSwipeListener(listener);
	}

	/**
	 * Sets a duration for animation used to restore position of holder's item view after swipe
	 * gesture for that holder has been canceled.
	 * <p>
	 * Default value: {@link #RESTORE_HOLDER_ANIMATION_DURATION}
	 *
	 * @param duration The desired duration.
	 * @see #getRestoreHolderAnimationDuration()
	 * @see #restoreHolderForAdapter(RecyclerView.ViewHolder, int, RecyclerView.Adapter, Runnable)
	 * @see OnSwipeListener#onSwipeCanceled(ItemSwipeHelper, RecyclerView.ViewHolder)
	 */
	public void setRestoreHolderAnimationDuration(final long duration) {
		this.mRestoreHolderAnimationDuration = duration;
	}

	/**
	 * Returns the duration of holder's restore animation.
	 *
	 * @return Duration of restore animation.
	 * @see #setRestoreHolderAnimationDuration(long)
	 */
	public long getRestoreHolderAnimationDuration() {
		return mRestoreHolderAnimationDuration;
	}

	/**
	 * Sets an interpolator for animation used to restore position of holder's item view after swipe
	 * gesture for that holder has been canceled.
	 * <p>
	 * Default value: {@link FastOutSlowInInterpolator}
	 *
	 * @param interpolator The desired interpolator.
	 * @see #getRestoreHolderAnimationInterpolator()
	 * @see OnSwipeListener#onSwipeCanceled(ItemSwipeHelper, RecyclerView.ViewHolder)
	 */
	public void setRestoreHolderAnimationInterpolator(@NonNull final Interpolator interpolator) {
		this.mRestoreHolderAnimationInterpolator = interpolator;
	}

	/**
	 * Returns the interpolator for holder's restore animation.
	 *
	 * @return Animator for restore animation.
	 * @see #setRestoreHolderAnimationInterpolator(Interpolator)
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
	 * Restores position of the given <var>viewHolder</var> (its item view) to its initial state.
	 * <p>
	 * This method should be called to restore holder's position whenever swipe gesture for such
	 * holder is canceled to prevent case when holder remains in the swiped state, that is off screen.
	 *
	 * @param viewHolder The view holder of which position to restore.
	 * @param direction  The direction for which to restore the holder's state.
	 * @param adapter    The adapter associated with the holder. If not {@code null}, the adapter
	 *                   will be notified via {@link RecyclerView.Adapter#notifyItemChanged(int)}
	 *                   with position of the holder after the restore animation finishes.
	 * @param callback   Callback to be fired when the restore animation finishes. May be {@code null}.
	 * @see OnSwipeListener#onSwipeCanceled(ItemSwipeHelper, RecyclerView.ViewHolder)
	 */
	public void restoreHolderForAdapter(
			@NonNull final RecyclerView.ViewHolder viewHolder,
			@Direction final int direction,
			@Nullable final RecyclerView.Adapter adapter,
			@Nullable final Runnable callback
	) {
		if (viewHolder instanceof SwipeViewHolder) {
			// Restore holder's swipe view and when restore animation finishes notify the adapter
			// that item at the holder's position has changed so view for the item is again properly
			// drawn by the parent RecyclerView.
			final View swipeView = ((SwipeViewHolder) viewHolder).getSwipeView();
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
	 * A {@link ItemInteractor} implementation used by {@link ItemSwipeHelper}
	 * to control swipe gesture and delegate swipe events to the view holder that is being swiped.
	 */
	static final class SwipeInteractor extends RecyclerViewItemHelper.ItemInteractor {

		/**
		 * Parent helper which uses this callback instance.
		 */
		private ItemSwipeHelper helper;

		/**
		 * List containing all registered {@link OnSwipeListener}.
		 *
		 * @see #addOnSwipeListener(OnSwipeListener)
		 * @see #removeOnSwipeListener(OnSwipeListener)
		 */
		private List<OnSwipeListener> swipeListeners;

		/**
		 * Adapter, attached to the parent helper, providing swipe item views.
		 */
		private SwipeAdapter adapter;

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
		void attachAdapter(final SwipeAdapter adapter) {
			this.adapter = adapter;
		}

		/**
		 * Registers a callback to be invoked whenever swipe gesture is <b>started</b>, <b>finished</b>
		 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
		 *
		 * @param listener The desired listener to add.
		 * @see #removeOnSwipeListener(OnSwipeListener)
		 */
		void addOnSwipeListener(@NonNull final OnSwipeListener listener) {
			if (swipeListeners == null) swipeListeners = new ArrayList<>(1);
			if (!swipeListeners.contains(listener)) swipeListeners.add(listener);
		}

		/**
		 * Removes the given swipe <var>listener</var> from the registered listeners.
		 *
		 * @param listener The desired listener to remove.
		 * @see #addOnSwipeListener(OnSwipeListener)
		 */
		void removeOnSwipeListener(@NonNull final OnSwipeListener listener) {
			if (swipeListeners != null) swipeListeners.remove(listener);
		}

		/**
		 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
		 * <var>viewHolder</var> has been started.
		 *
		 * @param viewHolder The view holder for which the swipe has started.
		 */
		private void notifySwipeStarted(final RecyclerView.ViewHolder viewHolder) {
			if (swipeListeners != null && !swipeListeners.isEmpty()) {
				for (final OnSwipeListener listener : swipeListeners) {
					listener.onSwipeStarted(helper, viewHolder);
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
		private void notifySwipeFinished(final RecyclerView.ViewHolder viewHolder, final int direction) {
			if (swipeListeners != null && !swipeListeners.isEmpty()) {
				for (final OnSwipeListener listener : swipeListeners) {
					listener.onSwipeFinished(helper, viewHolder, direction);
				}
			}
		}

		/**
		 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
		 * <var>viewHolder</var> has been canceled.
		 *
		 * @param viewHolder The view holder for which the swipe has canceled.
		 */
		private void notifySwipeCanceled(final RecyclerView.ViewHolder viewHolder) {
			if (swipeListeners != null && !swipeListeners.isEmpty()) {
				for (final OnSwipeListener listener : swipeListeners) {
					listener.onSwipeCanceled(helper, viewHolder);
				}
			}
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
				final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
				getDefaultUIUtil().onSelected(swipeViewHolder.getSwipeView());
				swipeViewHolder.onSwipeStarted();
				notifySwipeStarted(viewHolder);
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
			final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
			getDefaultUIUtil().onDraw(canvas, recyclerView, swipeViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeViewHolder.onDraw(canvas, dX, dY, isCurrentlyActive);
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
			final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
			getDefaultUIUtil().onDrawOver(canvas, recyclerView, swipeViewHolder.getSwipeView(), dX, dY, actionState, isCurrentlyActive);
			swipeViewHolder.onDrawOver(canvas, dX, dY, isCurrentlyActive);
		}

		/**
		 */
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
			final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
			swipeViewHolder.onSwipeFinished(direction);
			notifySwipeFinished(viewHolder, direction);
		}

		/**
		 */
		@Override
		public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
			getDefaultUIUtil().clearView(swipeViewHolder.getSwipeView());
			swipeViewHolder.onSwipeCanceled();
			notifySwipeCanceled(viewHolder);
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
