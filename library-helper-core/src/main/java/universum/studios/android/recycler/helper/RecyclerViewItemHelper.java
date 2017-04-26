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

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An {@link ItemTouchHelper} implementation that is used as base class by all item helpers from the
 * Recycler library and is also encouraged to be used as base class by custom helper implementations.
 *
 * @param <I> Type of the interactor used by this helper.
 * @author Martin Albedinsky
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
	 */
	@IntDef({
			START, END,
			LEFT, RIGHT,
			UP, DOWN
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Direction {
	}

	/**
	 * Defines an annotation for determining supported movement flags by {@link ItemTouchHelper} API.
	 */
	@IntDef(flag = true, value = {
			START, END,
			LEFT, RIGHT,
			UP, DOWN
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Movement {
	}

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Interactor used by this item helper.
	 */
	final I mInteractor;

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
		this.mInteractor = interactor;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a boolean flag indicating whether this helper should be enabled or not.
	 * <p>
	 * When a specific instance of helper is disabled it should not provide any functionality.
	 * <p>
	 * Default value: {@code true}
	 *
	 * @param enabled {@code True} to enable this helper, {@code false} otherwise.
	 * @see #isEnabled()
	 * @see ItemInteractor#setEnabled(boolean)
	 */
	public final void setEnabled(final boolean enabled) {
		mInteractor.setEnabled(enabled);
	}

	/**
	 * Returns boolean flag indicating whether this helper is enabled or not.
	 *
	 * @return {@code True} if this helper instance is enabled, {@code false} otherwise.
	 * @see #setEnabled(boolean)
	 * @see ItemInteractor#isEnabled()
	 */
	public final boolean isEnabled() {
		return mInteractor.isEnabled();
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ItemTouchHelper.Callback} implementation which should be used as base class for
	 * all interactors used by {@link RecyclerViewItemHelper} implementations.
	 *
	 * @author Martin Albedinsky
	 */
	protected static abstract class ItemInteractor extends ItemTouchHelper.Callback {

		/**
		 * Boolean flag indicating whether this interactor is enabled or not. If interactor is
		 * disabled it should not dispatch any events to the parent helper.
		 */
		boolean enabled = true;

		/**
		 * Sets a boolean flag indicating whether this interactor should be enabled or not.
		 *
		 * @param enabled {@code True} to enable this interactor, {@code false} otherwise.
		 * @see #isEnabled()
		 */
		protected final void setEnabled(final boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * Returns boolean flag indicating whether this interactor is enabled or not.
		 *
		 * {@code True} if this interactor is enabled, {@code false} otherwise.
		 *
		 * @see #setEnabled(boolean)
		 */
		protected final boolean isEnabled() {
			return enabled;
		}
	}
}
