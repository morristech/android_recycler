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

import android.graphics.Canvas;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An {@link ItemTouchHelper} implementation that is used as base class by all item helpers from the
 * Recycler library and is also encouraged to be used as base class by custom helper implementations.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @param <I> Type of the item interactor that will be used by this helper.
 */
public abstract class RecyclerViewItemHelper<I extends RecyclerViewItemHelper.ItemInteractor> extends ItemTouchHelper {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "RecyclerViewItemHelper";

	/**
	 * Defines an annotation for determining supported directions by {@link ItemTouchHelper} API.
	 *
	 * <ul>
	 * <li>{@link #START}</li>
	 * <li>{@link #END}</li>
	 * <li>{@link #LEFT}</li>
	 * <li>{@link #RIGHT}</li>
	 * <li>{@link #UP}</li>
	 * <li>{@link #DOWN}</li>
	 * </ul>
	 */
	@IntDef({
			START, END,
			LEFT, RIGHT,
			UP, DOWN
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Direction {}

	/**
	 * Defines an annotation for determining supported movement flags by {@link ItemTouchHelper} API.
	 *
	 * <ul>
	 * <li>{@link #START}</li>
	 * <li>{@link #END}</li>
	 * <li>{@link #LEFT}</li>
	 * <li>{@link #RIGHT}</li>
	 * <li>{@link #UP}</li>
	 * <li>{@link #DOWN}</li>
	 * </ul>
	 */
	@IntDef(flag = true, value = {
			START, END,
			LEFT, RIGHT,
			UP, DOWN
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Movement {}

	/**
	 * Defines an annotation for determining available interaction states for {@link ItemTouchHelper} API.
	 *
	 * <ul>
	 * <li>{@link #ACTION_STATE_IDLE}</li>
	 * <li>{@link #ACTION_STATE_SWIPE}</li>
	 * <li>{@link #ACTION_STATE_DRAG}</li>
	 * </ul>
	 */
	@IntDef(flag = true, value = {
			ACTION_STATE_IDLE,
			ACTION_STATE_SWIPE,
			ACTION_STATE_DRAG
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Interaction {}

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Required interface for {@link RecyclerView.ViewHolder ViewHolders} with which may user interact
	 * via a concrete {@link RecyclerViewItemHelper} implementation.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 */
	public interface InteractiveViewHolder {

		/**
		 * Returns the view that a user may interact with for the specified <var>interaction</var>.
		 * <p>
		 * Note that the interactive view must be either the item view of this holder or one of the
		 * item view's descendants.
		 *
		 * @param interaction The type of interaction for which to return the View. Either
		 *                    {@link #ACTION_STATE_SWIPE} or {@link #ACTION_STATE_DRAG}.
		 * @return This holder's interactive view or {@code null} if this holder does not have any
		 * special view for the specified interaction so its item view should be considered as
		 * interactive view.
		 */
		@Nullable View getInteractiveView(@Interaction int interaction);

		/**
		 * Called by the item helper to allow this holder to draw its current state in order to
		 * respond to user interactions.
		 *
		 * @param canvas            The canvas on which to draw.
		 * @param dX                The amount of horizontal displacement caused by user's interaction.
		 * @param dY                The amount of vertical displacement caused by user's interaction.
		 * @param interaction       The type of interaction on the holder's View.
		 *                          Either {@link #ACTION_STATE_SWIPE} or {@link #ACTION_STATE_DRAG}.
		 * @param isCurrentlyActive {@code True} if view of this holder is currently being controlled
		 *                          by the user, {@code false} if it is simply animating back to its
		 *                          original state.
		 *
		 * @see ItemTouchHelper.Callback#onChildDraw(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)
		 */
		void onDraw(@NonNull Canvas canvas, float dX, float dY, @Interaction int interaction, boolean isCurrentlyActive);

		/**
		 * Called by the item helper to allow this holder to draw its current state in order to
		 * respond to user interactions.
		 *
		 * @param canvas            The canvas on which to draw.
		 * @param dX                The amount of horizontal displacement caused by user's interaction.
		 * @param dY                The amount of vertical displacement caused by user's interaction.
		 * @param interaction       The type of interaction on the holder's View.
		 *                          Either {@link #ACTION_STATE_SWIPE} or {@link #ACTION_STATE_DRAG}.
		 * @param isCurrentlyActive {@code True} if view of this holder is currently being controlled
		 *                          by the user, {@code false} if it is simply animating back to its
		 *                          original state.
		 *
		 * @see ItemTouchHelper.Callback#onChildDrawOver(Canvas, RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)
		 */
		void onDrawOver(@NonNull Canvas canvas, float dX, float dY, @Interaction int interaction, boolean isCurrentlyActive);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Interactor used by this item helper to support/handle its specific provided feature.
	 */
	final I interactor;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of RecyclerViewItemHelper with the specified <var>interactor</var>.
	 *
	 * @param interactor The interactor that will be used by the new helper to support is specific
	 *                   feature.
	 */
	protected RecyclerViewItemHelper(@NonNull final I interactor) {
		super(interactor);
		this.interactor = interactor;
		this.interactor.attachToHelper(this);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the interactor instance used by this helper to support its specific feature for
	 * items of the {@link RecyclerView} to which is this helper instance attached.
	 * <p>
	 * The returned interactor may be used to configure the provided feature.
	 *
	 * @return This helper's interactor.
	 */
	@NonNull public final I getInteractor() {
		return interactor;
	}

	/**
	 */
	@Override public void attachToRecyclerView(@Nullable final RecyclerView recyclerView) {
		super.attachToRecyclerView(recyclerView);
		if (recyclerView == null) {
			this.interactor.attachAdapter(null);
		} else {
			final RecyclerView.Adapter adapter = recyclerView.getAdapter();
			if (adapter == null || interactor.canAttachAdapter(adapter)) {
				this.interactor.attachAdapter(adapter);
			} else {
				throw new IllegalArgumentException("Cannot attach adapter(" + adapter + ") to this(" + this + ") item helper.");
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ItemTouchHelper.Callback} implementation which should be used as base class for
	 * all interactors used by {@link RecyclerViewItemHelper} implementations.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 */
	public static abstract class ItemInteractor extends ItemTouchHelper.Callback {

		/**
		 * Boolean flag indicating whether this interactor is enabled or not. If interactor is
		 * disabled it should not dispatch any events to the parent helper.
		 */
		boolean enabled = true;

		/**
		 * Parent helper which uses this interactor.
		 */
		RecyclerViewItemHelper helper;

		/**
		 * Adapter to which is this interactor attached via {@link #attachAdapter(RecyclerView.Adapter)}.
		 * May be {@code null} if no adapter is attached or the previous one has been detached.
		 */
		RecyclerView.Adapter adapter;

		/**
		 * Attaches this interactor to the parent <var>helper</var>.
		 *
		 * @param helper The helper that will use this interactor to support its specific feature.
		 */
		final void attachToHelper(RecyclerViewItemHelper helper) {
			onAttachedToHelper(this.helper = helper);
		}

		/**
		 * Invoked whenever {@link #attachToHelper(RecyclerViewItemHelper)} is called for this interactor
		 * instance.
		 *
		 * @param helper The helper that will use this interactor to support its specific feature.
		 */
		protected void onAttachedToHelper(@NonNull RecyclerViewItemHelper helper) {
			// Inheritance hierarchies may perform here operations related to attachment of this
			// interactor instance to its parent helper.
		}

		/**
		 * Called by the parent helper to check if the given <var>adapter</var> may be attached
		 * to this interactor instance.
		 * <p>
		 * If this method returns {@code true}, {@link #onAdapterAttached(RecyclerView.Adapter)}
		 * will follow. If this interactor has attached any previous adapter, such adapter will be
		 * detached via {@link #onAdapterDetached(RecyclerView.Adapter)} before the new one is
		 * attached.
		 *
		 * @param adapter The adapter to be checked if it may be attached.
		 * @return {@code True} if the parent helper may attach the adapter to this interactor,
		 * {@code false} otherwise.
		 */
		protected abstract boolean canAttachAdapter(@NonNull RecyclerView.Adapter adapter);

		/**
		 * Attaches the given <var>adapter</var> to this interactor.
		 *
		 * @param adapter The adapter to attach. May be {@code null} to detach previous adapter.
		 */
		final void attachAdapter(RecyclerView.Adapter adapter) {
			if (this.adapter == adapter) {
				return;
			}
			if (this.adapter != null) {
				onAdapterDetached(this.adapter);
			}
			this.adapter = adapter;
			if (adapter != null) {
				onAdapterAttached(adapter);
			}
		}

		/**
		 * Invoked whenever a new <var>adapter</var> is attached to this interactor instance.
		 * <p>
		 * This method is called as result of call to {@link RecyclerViewItemHelper#attachToRecyclerView(RecyclerView)}
		 * for the parent helper.
		 *
		 * @param adapter The adapter of the associated {@link RecyclerView} to which is the parent
		 *                helper attached.
		 *
		 * @see #onAdapterDetached(RecyclerView.Adapter)
		 */
		protected void onAdapterAttached(@NonNull RecyclerView.Adapter adapter) {
			// Inheritance hierarchies may perform here operations related to attachment of the
			// specified adapter.
		}

		/**
		 * Invoked before a new <var>adapter</var> is attached to this interactor instance if there
		 * is already previous adapter attached.
		 *
		 * @param adapter The previous adapter that has been detached from this interactor.
		 *
		 * @see #onAdapterAttached(RecyclerView.Adapter)
		 */
		protected void onAdapterDetached(@NonNull RecyclerView.Adapter adapter) {
			// Inheritance hierarchies may perform here operations related to detachment of the
			// specified adapter.
		}

		/**
		 * Sets a boolean flag indicating whether this interactor should be enabled or not.
		 *
		 * @param enabled {@code True} to enable this interactor, {@code false} otherwise.
		 *
		 * @see #isEnabled()
		 */
		public void setEnabled(final boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * Returns boolean flag indicating whether this interactor is enabled or not.
		 *
		 * @return {@code True} if this interactor is enabled, {@code false} otherwise.
		 *
		 * @see #setEnabled(boolean)
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Returns boolean flag indicating whether this interactor is currently active.
		 *
		 * @return {@code True} if this interactor performs some interaction logic at this time,
		 * {@code false} otherwise.
		 */
		public abstract boolean isActive();

		/**
		 * Checks whether this interactor should handle interaction due to received callback from
		 * the parent helper.
		 * <p>
		 * This implementation checks whether this interactor instance is enabled at this time and
		 * also has valid adapter attached. However inheritance hierarchies may override this behaviour
		 * as they desire.
		 *
		 * @return {@code True} if interaction should be handled, {@code false} otherwise.
		 */
		protected boolean shouldHandleInteraction() {
			return enabled && adapter != null;
		}

		/**
		 */
		@Override public void onChildDraw(
				@NonNull final Canvas canvas,
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				final float dX,
				final float dY,
				final int actionState,
				final boolean isCurrentlyActive
		) {
			if (viewHolder instanceof RecyclerViewItemHelper.InteractiveViewHolder) {
				final InteractiveViewHolder interactiveViewHolder = (InteractiveViewHolder) viewHolder;
				final View interactiveView = interactiveViewHolder.getInteractiveView(actionState);
				if (interactiveView == null) {
					super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				} else {
					getDefaultUIUtil().onDraw(canvas, recyclerView, interactiveView, dX, dY, actionState, isCurrentlyActive);
				}
				interactiveViewHolder.onDraw(canvas, dX, dY, actionState, isCurrentlyActive);
			} else {
				super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		}

		/**
		 */
		@Override public void onChildDrawOver(
				@NonNull final Canvas canvas,
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				final float dX,
				final float dY,
				final int actionState,
				final boolean isCurrentlyActive
		) {
			if (viewHolder instanceof RecyclerViewItemHelper.InteractiveViewHolder) {
				final InteractiveViewHolder interactiveViewHolder = (InteractiveViewHolder) viewHolder;
				final View interactiveView = interactiveViewHolder.getInteractiveView(actionState);
				if (interactiveView == null) {
					super.onChildDrawOver(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
				} else {
					getDefaultUIUtil().onDrawOver(canvas, recyclerView, interactiveView, dX, dY, actionState, isCurrentlyActive);
				}
				interactiveViewHolder.onDrawOver(canvas, dX, dY, actionState, isCurrentlyActive);
			} else {
				super.onChildDrawOver(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
			}
		}
	}
}