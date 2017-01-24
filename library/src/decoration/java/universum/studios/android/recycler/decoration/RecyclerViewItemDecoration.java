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
package universum.studios.android.recycler.decoration;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * todo: description
 *
 * @author Martin Albedinsky
 */
public abstract class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "RecyclerViewItemDecoration";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #RecyclerViewItemDecoration(Context)} with {@code null} <var>context</var>.
	 */
	public RecyclerViewItemDecoration() {
		this(null);
	}

	/**
	 * Same as {@link #RecyclerViewItemDecoration(Context, AttributeSet)} with {@code null} <var>attrs</var>.
	 */
	public RecyclerViewItemDecoration(@Nullable Context context) {
		this(context, null);
	}

	/**
	 * Same as {@link #RecyclerViewItemDecoration(Context, AttributeSet, int)} with {@code 0}
	 * <var>defStyleAttr</var>.
	 */
	public RecyclerViewItemDecoration(@Nullable Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Same as {@link #RecyclerViewItemDecoration(Context, AttributeSet, int, int)} with {@code 0}
	 * <var>defStyleRes</var>.
	 */
	public RecyclerViewItemDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	/**
	 * todo:
	 *
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 */
	public RecyclerViewItemDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
		// todo:
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Inner classes ===============================================================================
	 */
}
