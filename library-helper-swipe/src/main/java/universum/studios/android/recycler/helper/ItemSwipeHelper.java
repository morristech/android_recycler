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
 * displayed in a {@link RecyclerView} widget. Instance of this helper may be simply created via
 * {@link #ItemSwipeHelper()} constructor.
 * <p>
 * In order to support swipe feature for a desired adapter, such adapter must implement {@link SwipeAdapter}
 * interface and needs to be attached to its corresponding {@link RecyclerView} before call to
 * {@link #attachToRecyclerView(RecyclerView)} which will attach such adapter to the swipe helper
 * automatically. Also all {@link RecyclerView.ViewHolder ViewHolders} of that adapter of which item
 * views are desired to be swiped, must implement {@link SwipeViewHolder} interface as other view
 * holder implementations will be ignored by the ItemSwipeHelper API. When the {@link SwipeAdapter}
 * is properly attached, the item helper will delegate to it all swipe gesture related callbacks/events
 * whenever appropriate via the adapter's interface.
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
	 * Interaction constant specific for {@link ItemSwipeHelper}.
	 *
	 * @see #ACTION_STATE_SWIPE
	 */
	public static final int INTERACTION = ACTION_STATE_SWIPE;

	/**
	 * Default swipe threshold for the swipe gesture.
	 */
	public static final float SWIPE_THRESHOLD = 0.5f;

	/**
	 * Default duration for animation used to restore position of the holder's item view.
	 *
	 * @see #restoreHolder(RecyclerView.ViewHolder, int, Runnable)
	 */
	public static final long RESTORE_HOLDER_ANIMATION_DURATION = 300;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Required interface for adapters which want to support swipe feature for theirs
	 * {@link RecyclerView.ViewHolder ViewHolders}.
	 *
	 * @author Martin Albedinsky
	 */
	public interface SwipeAdapter {

		/**
		 * Called by the swipe helper to obtain swipe movement flags for an item at the specified
		 * <var>position</var>.
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
	public interface SwipeViewHolder extends InteractiveViewHolder {

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is started.
		 *
		 * @see #onSwipeFinished(int)
		 * @see #onSwipeCanceled()
		 * @see ItemSwipeHelper.Callback#onSelectedChanged(RecyclerView.ViewHolder, int)
		 */
		void onSwipeStarted();

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is finished in
		 * the specified <var>direction</var>.
		 *
		 * @param direction The direction in which has the swipe gestured finished. One of directions
		 *                  defined by {@link Direction @Direction} annotation.
		 * @see #onSwipeStarted()
		 * @see ItemSwipeHelper.Callback#onSwiped(RecyclerView.ViewHolder, int)
		 */
		void onSwipeFinished(@Direction int direction);

		/**
		 * Called by the swipe helper whenever swipe gesture for view of this holder is canceled.
		 *
		 * @see #onSwipeStarted()
		 * @see ItemSwipeHelper.Callback#clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		void onSwipeCanceled();
	}

	/**
	 * Listener which may be used to receive callbacks about <b>started</b>, <b>finished</b> or
	 * <b>canceled</b> swipe gesture for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @author Martin Albedinsky
	 */
	public interface OnSwipeListener {

		/**
		 * Invoked whenever swipe gesture is started for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The item helper controlling the swipe gesture for the view holder.
		 * @param viewHolder  The view holder for which has been the swipe gesture started.
		 * @see #onSwipeFinished(ItemSwipeHelper, RecyclerView.ViewHolder, int)
		 */
		void onSwipeStarted(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder);

		/**
		 * Invoked whenever swipe gesture is finished for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The item helper controlling the swipe gesture for the view holder.
		 * @param viewHolder  The view holder for which has been the swipe gesture finished.
		 * @param direction   The direction in which has been the swipe gesture finished. One of
		 *                    directions defined by {@link Direction @Direction} annotation.
		 * @see #onSwipeStarted(ItemSwipeHelper, RecyclerView.ViewHolder)
		 */
		void onSwipeFinished(@NonNull ItemSwipeHelper swipeHelper, @NonNull RecyclerView.ViewHolder viewHolder, @Direction int direction);

		/**
		 * Invoked whenever swipe gesture is canceled for the given <var>viewHolder</var>.
		 *
		 * @param swipeHelper The item helper controlling the swipe gesture for the view holder.
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
	 * Creates a new instance of ItemSwipeHelper with the specified <var>interactor</var>.
	 *
	 * @param interactor The interactor that will receive and handle swipe gesture related events.
	 */
	private ItemSwipeHelper(final SwipeInteractor interactor) {
		super(interactor);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Makes movement flags for the swipe gesture.
	 *
	 * @param movementFlags The desired movement flags. One of flags defined by
	 *                      {@link Movement @Movement} annotation.
	 * @return Swipe flags according to the given movement flags.
	 * @see ItemSwipeHelper.Callback#makeMovementFlags(int, int)
	 */
	public static int makeSwipeFlags(@Movement final int movementFlags) {
		return SwipeInteractor.makeMovementFlags(0, movementFlags);
	}

	/**
	 * Sets a fraction that the user should move the holder's {@link View} to be considered as swiped.
	 * <p>
	 * Default value: {@link #SWIPE_THRESHOLD}
	 *
	 * @param threshold The desired threshold from the range {@code [0.0, 1.0]}.
	 * @see #getSwipeThreshold()
	 * @see ItemSwipeHelper.Callback#getSwipeThreshold(RecyclerView.ViewHolder)
	 */
	public void setSwipeThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
		this.mInteractor.swipeThreshold = threshold;
	}

	/**
	 * Returns the fraction that the user should move the holder's {@link View} to be considered as
	 * swiped.
	 *
	 * @return The swipe threshold from the range {@code [0.0, 1.0]}.
	 * @see #setSwipeThreshold(float)
	 */
	@FloatRange(from = 0.0f, to = 1.0f)
	public float getSwipeThreshold() {
		return mInteractor.swipeThreshold;
	}

	/**
	 * Registers a callback to be invoked whenever swipe gesture is <b>started</b>, <b>finished</b>
	 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
	 *
	 * @param listener The desired listener callback to add.
	 * @see #removeOnSwipeListener(OnSwipeListener)
	 */
	public void addOnSwipeListener(@NonNull final OnSwipeListener listener) {
		mInteractor.addListener(listener);
	}

	/**
	 * Removes the given swipe <var>listener</var> from the registered listeners.
	 *
	 * @param listener The desired listener to remove.
	 * @see #addOnSwipeListener(OnSwipeListener)
	 */
	public void removeOnSwipeListener(@NonNull final OnSwipeListener listener) {
		mInteractor.removeListener(listener);
	}

	/**
	 * Sets a duration for animation used to restore position of holder's item view after swipe
	 * gesture for that holder has been canceled.
	 * <p>
	 * Default value: {@link #RESTORE_HOLDER_ANIMATION_DURATION}
	 *
	 * @param duration The desired duration.
	 * @see #getRestoreHolderAnimationDuration()
	 * @see #restoreHolder(RecyclerView.ViewHolder, int, Runnable)
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
	 * Same as {@link #restoreHolder(RecyclerView.ViewHolder, int, Runnable)} with {@code null}
	 * <var>callback</var>.
	 */
	public boolean restoreHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @Direction final int direction) {
		return restoreHolder(viewHolder, direction, null);
	}

	/**
	 * Restores position of the given <var>viewHolder</var> (of its item view) to its initial state.
	 * <p>
	 * When the restore animation finishes, the adapter of the {@link RecyclerViewItemHelper} to which
	 * is this item helper attached, will be notified via {@link RecyclerView.Adapter#notifyItemChanged(int)}
	 * with position of the restored holder.
	 * <p>
	 * This method should be called to restore holder's position whenever swipe gesture for such
	 * holder is canceled to prevent case when holder remains in the swiped state, that is off screen.
	 *
	 * @param viewHolder        The view holder of which position to restore.
	 * @param direction         The direction for which to restore the holder's state.
	 * @param animationCallback Callback to be fired when the restore animation finishes.
	 *                          May be {@code null}.
	 * @return {@code True} restoring of the holder has been performed, {@code false} otherwise.
	 * @see OnSwipeListener#onSwipeCanceled(ItemSwipeHelper, RecyclerView.ViewHolder)
	 */
	public boolean restoreHolder(
			@NonNull final RecyclerView.ViewHolder viewHolder,
			@Direction final int direction,
			@Nullable final Runnable animationCallback
	) {
		if (viewHolder instanceof SwipeViewHolder) {
			// Restore holder's swipe view and when restore animation finishes notify the adapter
			// that item at the holder's position has changed so view for the item is again properly
			// drawn by the parent RecyclerView.
			final View swipeView = ((SwipeViewHolder) viewHolder).getInteractiveView(ACTION_STATE_SWIPE);
			if (swipeView == null) {
				return false;
			}
			ViewPropertyAnimator animator = null;
			switch (direction) {
				case LEFT:
				case RIGHT:
				case START:
				case END:
					if (swipeView.getTranslationX() != 0) {
						animator = swipeView.animate().translationX(0);
					}
					break;
				case UP:
				case DOWN:
					if (swipeView.getTranslationY() != 0) {
						animator = swipeView.animate().translationY(0);
					}
					break;
				default:
					// Unknown direction specified.
					break;
			}
			final int itemPosition = viewHolder.getAdapterPosition();
			final Runnable notify = new Runnable() {

				/**
				 */
				@Override
				public void run() {
					if (mInteractor.adapter != null) {
						mInteractor.adapter.notifyItemChanged(itemPosition);
					}
					if (animationCallback != null) {
						animationCallback.run();
					}
				}
			};
			if (animator == null) {
				notify.run();
			} else {
				animator.setDuration(mRestoreHolderAnimationDuration).setInterpolator(mRestoreHolderAnimationInterpolator).start();
				swipeView.postDelayed(notify, mRestoreHolderAnimationDuration);
			}
			return true;
		}
		return false;
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ItemInteractor} implementation used by {@link ItemSwipeHelper} to handle swipe
	 * gesture related callbacks and to delegate swipe events to the view holder that is being swiped.
	 */
	static final class SwipeInteractor extends RecyclerViewItemHelper.ItemInteractor {

		/**
		 * Fraction that the user should move the View to be considered as swiped.
		 *
		 * @see #getSwipeThreshold(RecyclerView.ViewHolder)
		 */
		float swipeThreshold = SWIPE_THRESHOLD;

		/**
		 * Adapter providing swipeable item views attached to this interactor.
		 *
		 * @see #onAdapterAttached(RecyclerView.Adapter)
		 */
		private SwipeAdapter swipeAdapter;

		/**
		 * List containing all registered {@link OnSwipeListener}.
		 *
		 * @see #addListener(OnSwipeListener)
		 * @see #removeListener(OnSwipeListener)
		 */
		private List<OnSwipeListener> listeners;

		/**
		 * Boolean flag indicating whether the swipe gesture is active at this time or not.
		 * If active, the user is swiping one of items in the associated {@link RecyclerView}.
		 *
		 * @see #onSelectedChanged(RecyclerView.ViewHolder, int)
		 * @see #onSwiped(RecyclerView.ViewHolder, int)
		 * @see #clearView(RecyclerView, RecyclerView.ViewHolder)
		 */
		private boolean swiping;

		/**
		 */
		@Override
		protected boolean canAttachToAdapter(@NonNull final RecyclerView.Adapter adapter) {
			return adapter instanceof SwipeAdapter;
		}

		/**
		 */
		@Override
		protected void onAdapterAttached(@NonNull final RecyclerView.Adapter adapter) {
			super.onAdapterAttached(adapter);
			this.swipeAdapter = (SwipeAdapter) adapter;
		}

		/**
		 */
		@Override
		protected void onAdapterDetached(@NonNull final RecyclerView.Adapter adapter) {
			super.onAdapterDetached(adapter);
			this.swipeAdapter = null;
		}

		/**
		 * Registers a callback to be invoked whenever swipe gesture is <b>started</b>, <b>finished</b>
		 * or <b>canceled</b> for a specific {@link RecyclerView.ViewHolder} instance.
		 *
		 * @param listener The desired listener to add.
		 * @see #removeListener(OnSwipeListener)
		 */
		void addListener(@NonNull final OnSwipeListener listener) {
			if (listeners == null) listeners = new ArrayList<>(1);
			if (!listeners.contains(listener)) listeners.add(listener);
		}

		/**
		 * Removes the given swipe <var>listener</var> from the registered listeners.
		 *
		 * @param listener The desired listener to remove.
		 * @see #addListener(OnSwipeListener)
		 */
		void removeListener(@NonNull final OnSwipeListener listener) {
			if (listeners != null) listeners.remove(listener);
		}

		/**
		 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
		 * <var>viewHolder</var> has been started.
		 *
		 * @param viewHolder The view holder for which the swipe has started.
		 */
		@VisibleForTesting
		void notifySwipeStarted(final RecyclerView.ViewHolder viewHolder) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnSwipeListener listener : listeners) {
					listener.onSwipeStarted((ItemSwipeHelper) helper, viewHolder);
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
		@VisibleForTesting
		void notifySwipeFinished(final RecyclerView.ViewHolder viewHolder, final int direction) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnSwipeListener listener : listeners) {
					listener.onSwipeFinished((ItemSwipeHelper) helper, viewHolder, direction);
				}
			}
		}

		/**
		 * Notifies all registered {@link OnSwipeListener} that the swipe gesture for the given
		 * <var>viewHolder</var> has been canceled.
		 *
		 * @param viewHolder The view holder for which the swipe has canceled.
		 */
		@VisibleForTesting
		void notifySwipeCanceled(final RecyclerView.ViewHolder viewHolder) {
			if (listeners != null && !listeners.isEmpty()) {
				for (final OnSwipeListener listener : listeners) {
					listener.onSwipeCanceled((ItemSwipeHelper) helper, viewHolder);
				}
			}
		}

		/**
		 */
		@Override
		public boolean isActive() {
			return swiping;
		}

		/**
		 */
		@Override
		public int getMovementFlags(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			return enabled && swipeAdapter != null && viewHolder instanceof SwipeViewHolder ? swipeAdapter.getItemSwipeFlags(viewHolder.getAdapterPosition()) : 0;
		}

		/**
		 */
		@Override
		public void onSelectedChanged(@Nullable final RecyclerView.ViewHolder viewHolder, final int actionState) {
			switch (actionState) {
				case ItemSwipeHelper.ACTION_STATE_SWIPE:
					if (viewHolder instanceof SwipeViewHolder) {
						this.swiping = true;
						final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
						final View interactiveView = swipeViewHolder.getInteractiveView(ACTION_STATE_SWIPE);
						if (interactiveView == null) {
							super.onSelectedChanged(viewHolder, actionState);
						} else {
							getDefaultUIUtil().onSelected(interactiveView);
						}
						swipeViewHolder.onSwipeStarted();
						notifySwipeStarted(viewHolder);
						break;
					}
				default:
					super.onSelectedChanged(viewHolder, actionState);
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
			// Ignored for this interactor-callback implementation.
			return false;
		}

		/**
		 */
		@Override
		public float getSwipeThreshold(@NonNull final RecyclerView.ViewHolder viewHolder) {
			return swipeThreshold;
		}

		/**
		 */
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {
			if (viewHolder instanceof SwipeViewHolder) {
				this.swiping = false;
				final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
				swipeViewHolder.onSwipeFinished(direction);
				notifySwipeFinished(viewHolder, direction);
			}
		}

		/**
		 */
		@Override
		public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			if (viewHolder instanceof SwipeViewHolder) {
				this.swiping = false;
				final SwipeViewHolder swipeViewHolder = (SwipeViewHolder) viewHolder;
				final View interactiveView = swipeViewHolder.getInteractiveView(ACTION_STATE_SWIPE);
				if (interactiveView == null) {
					super.clearView(recyclerView, viewHolder);
				} else {
					getDefaultUIUtil().clearView(interactiveView);
				}
				final int adapterPosition = viewHolder.getAdapterPosition();
				if (adapterPosition != RecyclerView.NO_POSITION) {
					swipeViewHolder.onSwipeCanceled();
					notifySwipeCanceled(viewHolder);
				}
			} else {
				super.clearView(recyclerView, viewHolder);
			}
		}
	}

	/**
	 * A {@link DefaultItemAnimator} extension which overrides default implementation of
	 * {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}
	 * in a way that does not cause any drawing artifacts to occur when state of item view for swiped
	 * view holder is about to be restored via {@link #restoreHolder(RecyclerView.ViewHolder, int, Runnable)}.
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
