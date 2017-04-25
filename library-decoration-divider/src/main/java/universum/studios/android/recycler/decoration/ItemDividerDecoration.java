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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.recycler.R;

/**
 * A {@link RecyclerViewItemDecoration} implementation that may be used to add a divider between
 * items presented in a {@link RecyclerView}.
 *
 * @author Martin Albedinsky
 */
public class ItemDividerDecoration extends RecyclerViewItemDecoration {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ItemDividerDecoration";

	/**
	 * todo:
	 */
	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;

	/**
	 * todo:
	 */
	public static final int VERTICAL = LinearLayout.VERTICAL;

	/**
	 * todo:
	 */
	@IntDef({HORIZONTAL, VERTICAL})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Orientation {
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
	 * todo:
	 */
	@Orientation
	private int mOrientation = VERTICAL;

	/**
	 * todo:
	 */
	private Drawable mDivider;

	/**
	 * todo:
	 */
	private int mDividerThickness;

	/**
	 * todo:
	 */
	private int mDividerDrawThickness;

	/**
	 * todo:
	 */
	private final Rect mBounds = new Rect();

	/*
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
	 * todo:
	 *
	 * @param orientation
	 * @param divider
	 */
	public ItemDividerDecoration(@Orientation int orientation, @NonNull Drawable divider) {
		this.mOrientation = orientation;
		this.mDivider = divider;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * todo:
	 *
	 * @param orientation
	 * @see #getOrientation()
	 */
	public void setOrientation(@Orientation int orientation) {
		this.mOrientation = orientation;
		this.resolveDividerDrawThickness();
	}

	/**
	 * todo:
	 *
	 * @return
	 * @see #setOrientation(int)
	 */
	@Orientation
	public int getOrientation() {
		return mOrientation;
	}

	/**
	 * todo:
	 *
	 * @param divider
	 * @see #getDivider()
	 */
	public void setDivider(@Nullable Drawable divider) {
		this.mDivider = divider;
		this.resolveDividerDrawThickness();
	}

	/**
	 * todo:
	 *
	 * @return
	 * @see #setDivider(Drawable)
	 */
	@Nullable
	public Drawable getDivider() {
		return mDivider;
	}

	/**
	 * todo:
	 *
	 * @param thickness
	 * @see #getDividerThickness()
	 */
	public void setDividerThickness(@IntRange(from = 0) int thickness) {
		this.mDividerThickness = thickness;
		this.mDividerDrawThickness = thickness;
	}

	/**
	 * todo:
	 *
	 * @return
	 * @see #setDividerThickness(int)
	 */
	@IntRange(from = 0)
	public int getDividerThickness() {
		return mDividerThickness;
	}

	/**
	 * todo:
	 */
	private void resolveDividerDrawThickness() {
		if (mDivider == null) {
			this.mDividerDrawThickness = 0;
		} else {
			switch (mOrientation) {
				case HORIZONTAL:
					this.mDividerDrawThickness = mDivider.getIntrinsicWidth();
					break;
				case VERTICAL:
				default:
					this.mDividerDrawThickness = mDivider.getIntrinsicHeight();
					break;
			}
		}
	}

	/**
	 */
	@Override
	public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		if (shouldDecorate(parent)) {
			switch (mOrientation) {
				case HORIZONTAL:
					onDrawHorizontally(canvas, parent, state);
					break;
				case VERTICAL:
				default:
					onDrawVertically(canvas, parent, state);
					break;
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param parent
	 * @return
	 */
	private boolean shouldDecorate(RecyclerView parent) {
		return parent.getLayoutManager() != null && mDivider != null && mDividerDrawThickness > 0;
	}

	/**
	 * todo:
	 *
	 * @param canvas
	 * @param parent
	 * @param state
	 */
	@SuppressLint("NewApi")
	protected void onDrawHorizontally(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		canvas.save();
		final int top;
		final int bottom;
		if (parent.getClipToPadding()) {
			top = parent.getPaddingTop();
			bottom = parent.getHeight() - parent.getPaddingBottom();
			canvas.clipRect(
					parent.getPaddingLeft(),
					top,
					parent.getWidth() - parent.getPaddingRight(),
					bottom
			);
		} else {
			top = 0;
			bottom = parent.getHeight();
		}
		for (int i = 0; i < parent.getChildCount(); i++) {
			final View child = parent.getChildAt(i);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
			final int left = right - mDividerDrawThickness;
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * todo:
	 *
	 * @param canvas
	 * @param parent
	 * @param state
	 */
	@SuppressLint("NewApi")
	protected void onDrawVertically(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		canvas.save();
		final int left;
		final int right;
		if (parent.getClipToPadding()) {
			left = parent.getPaddingLeft();
			right = parent.getWidth() - parent.getPaddingRight();
			canvas.clipRect(
					left,
					parent.getPaddingTop(),
					right,
					parent.getHeight() - parent.getPaddingBottom()
			);
		} else {
			left = 0;
			right = parent.getWidth();
		}
		for (int i = 0; i < parent.getChildCount(); i++) {
			final View child = parent.getChildAt(i);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
			final int top = bottom - mDividerDrawThickness;
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 */
	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		if (shouldDecorate(parent)) {
			switch (mOrientation) {
				case HORIZONTAL:
					outRect.set(0, 0, mDividerDrawThickness, 0);
					break;
				case VERTICAL:
				default:
					outRect.set(0, 0, 0, mDividerDrawThickness);
					break;
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
