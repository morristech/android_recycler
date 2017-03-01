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
package universum.studios.android.recycler.decoration;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * A {@link RecyclerView.ItemDecoration} implementation that should be used as base class for all
 * item decorations from the Recycler library.
 *
 * @author Martin Albedinsky
 */
public abstract class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "RecyclerViewItemDecoration";

	/**
	 * Interface ===================================================================================
	 */

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
	 * Creates a new instance of RecyclerViewItemDecoration for the given context.
	 *
	 * @param context      Context in which will be the new decoration presented.
	 * @param attrs        Set of Xml attributes used to configure the new instance of this decoration.
	 * @param defStyleAttr An attribute which contains a reference to a default style resource for
	 *                     this decoration within a theme of the given context.
	 * @param defStyleRes  Resource id of the default style for the new decoration.
	 */
	public RecyclerViewItemDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
		// No configuration required for the base implementation.
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Inner classes ===============================================================================
	 */
}
