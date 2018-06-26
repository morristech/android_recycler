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
package universum.studios.android.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import universum.studios.android.recycler.R;

/**
 * A {@link RecyclerViewItemDecoration} implementation that may be used to add a <b>space</b>,
 * vertically and horizontally, between items displayed in a {@link RecyclerView}.
 *
 * <h3>Xml attributes</h3>
 * {@link R.styleable#Recycler_ItemDecoration_Space ItemSpaceDecoration Attributes}
 *
 * <h3>Default style attribute</h3>
 * {@code none}
 *
 * @author Martin Albedinsky
 * @see RecyclerView#addItemDecoration(RecyclerView.ItemDecoration)
 */
public class ItemSpaceDecoration extends RecyclerViewItemDecoration {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemSpaceDecoration";

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
	 * Amount of space by which to offset an item at the start in horizontal direction.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView}.
	 *
	 * @see #updateItemOffsets(Rect, boolean)
	 */
	private int mHorizontalStart;

	/**
	 * Amount of space by which to offset an item at the end in horizontal direction.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView}.
	 *
	 * @see #updateItemOffsets(Rect, boolean)
	 */
	private int mHorizontalEnd;

	/**
	 * Amount of space by which to offset an item at the start in vertical direction.
	 */
	private int mVerticalStart;

	/**
	 * Amount of space by which to offset an item at the end in vertical direction.
	 */
	private int mVerticalEnd;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #ItemSpaceDecoration(Context)} with {@code null} <var>context</var>.
	 */
	public ItemSpaceDecoration() {
		this(null);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(int, int, int, int)} where the specified <var>horizontal</var>
	 * amount will be used for both, horizontal start and horizontal end amounts, and the <var>vertical</var>
	 * amount will be used similarly for both, vertical start and vertical end amounts.
	 */
	public ItemSpaceDecoration(final int horizontal, final int vertical) {
		this(horizontal, horizontal, vertical, vertical);
	}

	/**
	 * Creates a new instance of ItemSpaceDecoration with the specified spacing amounts.
	 *
	 * @param horizontalStart Amount to be applied at the start of an item in horizontal direction.
	 * @param horizontalEnd   Amount to be applied at the end of an item in horizontal direction.
	 * @param verticalStart   Amount to be applied at the start of an item in vertical direction.
	 * @param verticalEnd     Amount to be applied at the end of an item in vertical direction.
	 */
	public ItemSpaceDecoration(final int horizontalStart, final int horizontalEnd, final int verticalStart, final int verticalEnd) {
		super();
		this.mHorizontalStart = horizontalStart;
		this.mHorizontalEnd = horizontalEnd;
		this.mVerticalStart = verticalStart;
		this.mVerticalEnd = verticalEnd;
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet)} with {@code null} <var>attrs</var>.
	 */
	public ItemSpaceDecoration(@Nullable final Context context) {
		this(context, null);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet, int)} with {@code 0} <var>defStyleAttr</var>.
	 */
	public ItemSpaceDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet, int, int)} with {@code 0} <var>defStyleRes</var>.
	 */
	public ItemSpaceDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	/**
	 * Creates a new instance of ItemSpaceDecoration for the given context.
	 *
	 * @param context      Context in which will be the new decoration presented.
	 * @param attrs        Set of Xml attributes used to configure the new instance of this decoration.
	 * @param defStyleAttr An attribute which contains a reference to a default style resource for
	 *                     this decoration within a theme of the given context.
	 * @param defStyleRes  Resource id of the default style for the new decoration.
	 * @see #ItemSpaceDecoration(int, int, int, int)
	 */
	public ItemSpaceDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (context != null) {
			final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ItemDecoration_Space, defStyleAttr, defStyleRes);
			final int attributeCount = attributes.getIndexCount();
			for (int i = 0; i < attributeCount; i++) {
				final int attrIndex = attributes.getIndex(i);
				if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingHorizontalStart) {
					this.mHorizontalStart = attributes.getDimensionPixelSize(attrIndex, 0);
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingHorizontalEnd) {
					this.mHorizontalEnd = attributes.getDimensionPixelSize(attrIndex, 0);
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingVerticalStart) {
					this.mVerticalStart = attributes.getDimensionPixelSize(attrIndex, 0);
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingVerticalEnd) {
					this.mVerticalEnd = attributes.getDimensionPixelSize(attrIndex, 0);
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingSkipFirst) {
					setSkipFirst(attributes.getBoolean(attrIndex, skipsFirst()));
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingSkipLast) {
					setSkipLast(attributes.getBoolean(attrIndex, skipsLast()));
				}
			}
			attributes.recycle();
		}
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the amount of space by which to offset each item at the start in horizontal direction.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView}.
	 *
	 * @return Amount of space in pixels.
	 * @see #getHorizontalEnd()
	 */
	public int getHorizontalStart() {
		return mHorizontalStart;
	}

	/**
	 * Returns the amount of space by which to offset each item at the end in horizontal direction.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView}.
	 *
	 * @return Amount of space in pixels.
	 * @see #getHorizontalStart()
	 */
	public int getHorizontalEnd() {
		return mHorizontalEnd;
	}

	/**
	 * Returns the amount of space by which to offset each item at the start in vertical direction.
	 *
	 * @return Amount of space in pixels.
	 * @see #getVerticalEnd()
	 */
	public int getVerticalStart() {
		return mVerticalStart;
	}

	/**
	 * Returns the amount of space by which to offset each item at the end in vertical direction.
	 *
	 * @return Amount of space in pixels.
	 * @see #getVerticalStart()
	 */
	public int getVerticalEnd() {
		return mVerticalEnd;
	}

	/**
	 */
	@Override
	public void getItemOffsets(@NonNull final Rect rect, @NonNull final View view, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		if (mSkipFirst || mSkipLast) {
			final int position = parent.getChildAdapterPosition(view);
			if (position == RecyclerView.NO_POSITION) {
				rect.setEmpty();
				return;
			}
			if ((mSkipFirst && position == 0) || (mSkipLast && position == state.getItemCount() - 1)) {
				rect.setEmpty();
				return;
			}
		}
		if (mPrecondition.check(view, parent, state)) {
			this.updateItemOffsets(rect, ViewCompat.getLayoutDirection(parent) == ViewCompat.LAYOUT_DIRECTION_RTL);
		} else {
			rect.setEmpty();
		}
	}

	/**
	 * Called to update the given <var>rect</var> with the current spacing offsets specified for this
	 * decoration.
	 *
	 * @param rect         The desired item offsets rect to be updated.
	 * @param rtlDirection {@code True} if offsets should be updated for <i>RTL</i> layout direction,
	 *                     {@code false} for <i>LTR</i> layout direction.
	 */
	protected void updateItemOffsets(@NonNull final Rect rect, final boolean rtlDirection) {
		rect.set(rtlDirection ? mHorizontalEnd : mHorizontalStart, mVerticalStart, rtlDirection ? mHorizontalStart : mHorizontalEnd, mVerticalEnd);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}