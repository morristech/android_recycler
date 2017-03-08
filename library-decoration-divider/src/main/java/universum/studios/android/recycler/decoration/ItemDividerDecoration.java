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
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import universum.studios.android.recycler.R;

/**
 * A {@link RecyclerViewItemDecoration} implementation that may be used to add a divider between
 * items presented in a {@link RecyclerView}.
 *
 * @author Martin Albedinsky
 */
class ItemDividerDecoration extends RecyclerViewItemDecoration {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemDividerDecoration";

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
	 * Same as {@link #ItemDividerDecoration(Context)} with {@code null} <var>context</var>.
	 */
	public ItemDividerDecoration() {
		this(null);
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet)} with {@code null} <var>attrs</var>.
	 */
	public ItemDividerDecoration(@Nullable Context context) {
		this(context, null);
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet, int)} with {@code 0} <var>defStyleAttr</var>.
	 */
	public ItemDividerDecoration(@Nullable Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet, int, int)} with {@code 0} <var>defStyleRes</var>.
	 */
	public ItemDividerDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	/**
	 * Creates a new instance of ItemDividerDecoration for the given context.
	 *
	 * @param context      Context in which will be the new decoration presented.
	 * @param attrs        Set of Xml attributes used to configure the new instance of this decoration.
	 * @param defStyleAttr An attribute which contains a reference to a default style resource for
	 *                     this decoration within a theme of the given context.
	 * @param defStyleRes  Resource id of the default style for the new decoration.
	 */
	public ItemDividerDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (context != null) {
			final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ItemDecoration_Divider, defStyleAttr, defStyleRes);
			for (int i = 0; i < attributes.getIndexCount(); i++) {
				final int index = attributes.getIndex(i);
				// todo: process attr indexes here
			}
			attributes.recycle();
		}
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Inner classes ===============================================================================
	 */
}
