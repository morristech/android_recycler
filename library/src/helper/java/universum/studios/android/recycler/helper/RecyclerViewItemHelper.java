/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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
 * todo: description
 *
 * @param <C>
 * @author Martin Albedinsky
 */
public abstract class RecyclerViewItemHelper<C extends RecyclerViewItemHelper.BaseCallback> extends ItemTouchHelper {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "RecyclerViewItemHelper";

	/**
	 * todo
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
	 * todo
	 */
	@IntDef(flag = true, value = {
			START, END,
			LEFT, RIGHT,
			UP, DOWN
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Movement {
	}

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * todo:
	 */
	final C mCallback;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * todo:
	 *
	 * @param callback
	 */
	public RecyclerViewItemHelper(@NonNull C callback) {
		super(callback);
		this.mCallback = callback;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * todo:
	 *
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		mCallback.setEnabled(enabled);
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public boolean isEnabled() {
		return mCallback.isEnabled();
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * todo:
	 *
	 * @author Martin Albedinsky
	 */
	public static abstract class BaseCallback extends ItemTouchHelper.Callback {

		/**
		 * Boolean flag indicating whether this callback is enabled or not. If callback is disabled
		 * it should not dispatch any callbacks to the parent helper.
		 */
		boolean enabled;

		/**
		 * todo:
		 *
		 * @param enabled
		 * @see #isEnabled()
		 */
		protected void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * todo:
		 *
		 * @return
		 * @see #setEnabled(boolean)
		 */
		protected boolean isEnabled() {
			return enabled;
		}
	}
}
