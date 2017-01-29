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
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import universum.studios.android.recycler.R;

/**
 * todo: description
 *
 * @author Martin Albedinsky
 */
public class ItemSpaceDecoration extends RecyclerViewItemDecoration {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemSpaceDecoration";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * todo:
	 */
	private boolean mSkipFirst;

	/**
	 * todo:
	 */
	private boolean mSkipLast;

	/**
	 * Amount of space to apply at the start of an item in horizontal direction.
	 */
	private int mHorizontalStart;

	/**
	 * Amount of space to apply at the end of an item in horizontal direction.
	 */
	private int mHorizontalEnd;

	/**
	 * Amount of space to apply at the start of an item in vertical direction.
	 */
	private int mVerticalStart;

	/**
	 * Amount of space to apply at the end of an item in vertical direction.
	 */
	private int mVerticalEnd;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #ItemSpaceDecoration(Context)} with {@code null} <var>context</var>.
	 */
	public ItemSpaceDecoration() {
		this(null);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet)} with {@code null} <var>attrs</var>.
	 */
	public ItemSpaceDecoration(@Nullable Context context) {
		this(context, null);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet, int)} with {@code 0} <var>defStyleAttr</var>.
	 */
	public ItemSpaceDecoration(@Nullable Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Same as {@link #ItemSpaceDecoration(Context, AttributeSet, int, int)} with {@code 0} <var>defStyleRes</var>.
	 */
	public ItemSpaceDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
	public ItemSpaceDecoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (context != null) {
			final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ItemDecoration_Space, defStyleAttr, defStyleRes);
			for (int i = 0; i < typedArray.getIndexCount(); i++) {
				final int index = typedArray.getIndex(i);
				if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemDecorationSkipFirst) {
					this.mSkipFirst = typedArray.getBoolean(index, false);
				} else if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemDecorationSkipLast) {
					this.mSkipLast = typedArray.getBoolean(index, false);
				} else if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingHorizontalStart) {
					this.mHorizontalStart = typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingHorizontalEnd) {
					this.mHorizontalEnd = typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingVerticalStart) {
					this.mVerticalStart = typedArray.getDimensionPixelSize(index, 0);
				} else if (index == R.styleable.Recycler_ItemDecoration_Space_recyclerItemSpacingVerticalEnd) {
					this.mVerticalEnd = typedArray.getDimensionPixelSize(index, 0);
				}
			}
			typedArray.recycle();
		}
	}

	/**
	 * todo:
	 */
	public ItemSpaceDecoration(int horizontal, int vertical) {
		this(horizontal, horizontal, vertical, vertical);
	}

	/**
	 * todo:
	 *
	 * @param horizontalStart
	 * @param horizontalEnd
	 * @param verticalStart
	 * @param verticalEnd
	 */
	public ItemSpaceDecoration(int horizontalStart, int horizontalEnd, int verticalStart, int verticalEnd) {
		this.mHorizontalStart = horizontalStart;
		this.mHorizontalEnd = horizontalEnd;
		this.mVerticalStart = verticalStart;
		this.mVerticalEnd = verticalEnd;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public int getHorizontalStart() {
		return mHorizontalStart;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public int getHorizontalEnd() {
		return mHorizontalEnd;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public int getVerticalStart() {
		return mVerticalStart;
	}

	/**
	 * todo:
	 *
	 * @return
	 */
	public int getVerticalEnd() {
		return mVerticalEnd;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		if (mSkipFirst || mSkipLast) {
			final int position = parent.getChildAdapterPosition(view);
			if (position == RecyclerView.NO_POSITION) {
				return;
			}
			if ((mSkipFirst && position == 0) || (mSkipLast && position == state.getItemCount() - 1)) {
				outRect.set(0, 0, 0, 0);
			} else {
				outRect.set(mHorizontalStart, mVerticalStart, mHorizontalEnd, mVerticalEnd);
			}
		} else {
			outRect.set(mHorizontalStart, mVerticalStart, mHorizontalEnd, mVerticalEnd);
		}
	}

	/**
	 * Inner classes ===============================================================================
	 */
}