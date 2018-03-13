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
import android.graphics.drawable.ColorDrawable;
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
 * A {@link RecyclerViewItemDecoration} implementation that may be used to draw a <b>divider</b>
 * (drawable or color), vertically or horizontally, between items displayed in a {@link RecyclerView}
 * widget.
 *
 * <h3>Xml attributes</h3>
 * {@link R.styleable#Recycler_ItemDecoration_Divider ItemDividerDecoration Attributes}
 *
 * <h3>Default style attribute</h3>
 * {@code none}
 *
 * @author Martin Albedinsky
 * @see RecyclerView#addItemDecoration(RecyclerView.ItemDecoration)
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
	 * Horizontal orientation flag copied from {@link LinearLayout#HORIZONTAL} for convenience.
	 */
	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;

	/**
	 * Vertical orientation flag copied from {@link LinearLayout#VERTICAL} for convenience.
	 */
	public static final int VERTICAL = LinearLayout.VERTICAL;

	/**
	 * Defines an annotation for determining allowed orientations for {@link ItemDividerDecoration}.
	 *
	 * @see #ItemDividerDecoration(int, Drawable)
	 * @see #setOrientation(int)
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
	 * Orientation in which should be the divider drawn.
	 */
	@Orientation
	private int mOrientation = VERTICAL;

	/**
	 * Divider drawable to be drawn in a specified orientation and thickness (if specified).
	 */
	private Drawable mDivider;

	/**
	 * Thickness in which should be the specified divider drawn. This is either one of
	 * {@link Drawable#getIntrinsicWidth()} or {@link Drawable#getIntrinsicHeight()} depending on
	 * the specified orientation or desired value specified via {@link #setDividerThickness(int)}.
	 *
	 * @see #updateItemOffsets(Rect, boolean)
	 */
	private int mDividerThickness;

	/**
	 * Amount by which to offset the divider at the start.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView} and
	 * also with respect to the orientation specified for this decoration.
	 *
	 * @see #updateItemOffsets(Rect, boolean)
	 */
	private int mDividerOffsetStart;

	/**
	 * Amount by which to offset the divider at the end.
	 * <p>
	 * This value is used with respect to layout direction of the parent {@link RecyclerView} and
	 * also with respect to the orientation specified for this decoration.
	 *
	 * @see #updateItemOffsets(Rect, boolean)
	 */
	private int mDividerOffsetEnd;

	/**
	 * Bounds instance used when obtaining decorated bounds for a concrete item view when drawing
	 * a divider for it.
	 *
	 * @see #onDrawHorizontally(Canvas, RecyclerView, RecyclerView.State)
	 * @see #onDrawVertically(Canvas, RecyclerView, RecyclerView.State)
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
	 * Creates a new instance of ItemDividerDecoration with the specified <var>orientation</var>
	 * and <var>divider</var>.
	 *
	 * @param orientation The desired orientation in which should be the divider drawn. One of
	 *                    orientation defined by {@link Orientation @Orientation} annotation.
	 * @param divider     Drawable of the desired divider to be drawn. If this is a {@link ColorDrawable}
	 *                    the divider's thickness need to be specified via {@link #setDividerThickness(int)}
	 *                    as the color drawable does not have its intrinsic dimensions specified.
	 * @see #setOrientation(int)
	 * @see #setDivider(Drawable)
	 */
	public ItemDividerDecoration(@Orientation final int orientation, @NonNull final Drawable divider) {
		this(null);
		this.mOrientation = orientation;
		this.mDivider = divider;
		this.resolveDividerDrawThickness();
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet)} with {@code null} <var>attrs</var>.
	 */
	public ItemDividerDecoration(@Nullable final Context context) {
		this(context, null);
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet, int)} with {@code 0} <var>defStyleAttr</var>.
	 */
	public ItemDividerDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Same as {@link #ItemDividerDecoration(Context, AttributeSet, int, int)} with {@code 0} <var>defStyleRes</var>.
	 */
	public ItemDividerDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
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
	@SuppressWarnings("ResourceType")
	public ItemDividerDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		setSkipLast(true);
		if (context != null) {
			final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ItemDecoration_Divider, defStyleAttr, defStyleRes);
			final int attributeCount = attributes.getIndexCount();
			for (int i = 0; i < attributeCount; i++) {
				final int attrIndex = attributes.getIndex(i);
				if (attrIndex == R.styleable.Recycler_ItemDecoration_Divider_recyclerDividerOrientation) {
					setOrientation(attributes.getInt(attrIndex, mOrientation));
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Divider_recyclerDivider) {
					setDivider(attributes.getDrawable(attrIndex));
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Divider_recyclerDividerThickness) {
					setDividerThickness(attributes.getDimensionPixelSize(attrIndex, 0));
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Divider_recyclerDividerSkipFirst) {
					setSkipFirst(attributes.getBoolean(attrIndex, skipsFirst()));
				} else if (attrIndex == R.styleable.Recycler_ItemDecoration_Divider_recyclerDividerSkipLast) {
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
	 * Sets an orientation in which should be the divider specified for this decoration drawn.
	 * <p>
	 * Default value: {@link #VERTICAL}
	 *
	 * @param orientation The desired orientation. Should be one of orientations defined by
	 *                    {@link Orientation @Orientation} annotation.
	 * @see #getOrientation()
	 */
	public void setOrientation(@Orientation final int orientation) {
		this.mOrientation = orientation;
		this.resolveDividerDrawThickness();
	}

	/**
	 * Returns the orientation in which is the divider drawable drawn between items.
	 *
	 * @return This decoration's orientation.
	 * @see #setOrientation(int)
	 */
	@Orientation
	public int getOrientation() {
		return mOrientation;
	}

	/**
	 * Sets a drawable of the divider to be drawn between items of the associated {@link RecyclerView}
	 * in the orientation specified for this decoration.
	 * <p>
	 * Note that calling this method will reset any thickness value specified via
	 * {@link #setDividerThickness(int)} before and the thickness of the given divider drawable will
	 * be used instead depending on the orientation specified for this decoration.
	 *
	 * @param divider The desired divider drawable. May be {@code null} to to not draw any divider.
	 * @see #getDivider()
	 */
	public void setDivider(@Nullable final Drawable divider) {
		this.mDivider = divider;
		this.resolveDividerDrawThickness();
	}

	/**
	 * Returns the divider drawable that is drawn by this decoration between items.
	 *
	 * @return This decorations's divider drawable. May by {@code null} if no divider has been specified.
	 * @see #setDivider(Drawable)
	 */
	@Nullable
	public Drawable getDivider() {
		return mDivider;
	}

	/**
	 * Sets a thickness in which should be the divider specified for this decoration drawn.
	 * <p>
	 * Note that calling this method will override the intrinsic thickness obtained form the divider
	 * drawable when {@link #setDivider(Drawable)} has been called.
	 *
	 * @param thickness The desired thickness. May be {@code 0} to not draw the divider.
	 * @see #getDividerThickness()
	 */
	public void setDividerThickness(@IntRange(from = 0) final int thickness) {
		this.mDividerThickness = thickness;
	}

	/**
	 * Returns the thickness in which should be the divider drawn between items.
	 *
	 * @return The divider's thickness.
	 * @see #setDividerThickness(int)
	 */
	@IntRange(from = 0)
	public int getDividerThickness() {
		return mDividerThickness;
	}

	/**
	 * Resolves thickness in which should be the divider drawn based on its intrinsic dimensions and
	 * the current orientation specified for this decoration.
	 */
	private void resolveDividerDrawThickness() {
		if (mDivider == null) {
			this.mDividerThickness = 0;
		} else {
			switch (mOrientation) {
				case HORIZONTAL:
					this.mDividerThickness = mDivider.getIntrinsicWidth();
					break;
				case VERTICAL:
				default:
					this.mDividerThickness = mDivider.getIntrinsicHeight();
					break;
			}
		}
	}

	/**
	 * todo:
	 *
	 * @param start
	 * @param end
	 */
	public void setDividerOffset(@IntRange(from = 0) final int start, @IntRange(from = 0) final int end) {
		this.mDividerOffsetStart = start;
		this.mDividerOffsetEnd = end;
	}

	/**
	 */
	@Override
	public void getItemOffsets(@NonNull final Rect rect, @NonNull final View view, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		if (mDividerThickness > 0) {
			final boolean hasRtlDirection = ViewCompat.getLayoutDirection(parent) == ViewCompat.LAYOUT_DIRECTION_RTL;
			if (mSkipFirst || mSkipLast) {
				final int position = parent.getChildAdapterPosition(view);
				if (position == RecyclerView.NO_POSITION) {
					return;
				}
				if ((mSkipFirst && position == 0) || (mSkipLast && position == state.getItemCount() - 1)) {
					rect.set(0, 0, 0, 0);
				} else {
					this.updateItemOffsets(rect, hasRtlDirection);
				}
			} else {
				this.updateItemOffsets(rect, hasRtlDirection);
			}
		}
	}

	/**
	 * Updates the given <var>rect</var> with the current divider thickness specified for this
	 * decoration according to the orientation also specified for this decoration.
	 *
	 * @param rect         The desired item offsets rect to be updated.
	 * @param rtlDirection {@code True} if offsets should be updated for <i>RTL</i> layout direction,
	 *                     {@code false} for <i>LTR</i> layout direction.
	 */
	private void updateItemOffsets(final Rect rect, final boolean rtlDirection) {
		switch (mOrientation) {
			case HORIZONTAL:
				rect.set(0, mDividerOffsetStart, mDividerThickness, mDividerOffsetEnd);
				break;
			case VERTICAL:
			default:
				rect.set(
						rtlDirection ? mDividerOffsetEnd : mDividerOffsetStart,
						0,
						rtlDirection ? mDividerOffsetStart : mDividerOffsetEnd,
						mDividerThickness
				);
				break;
		}
	}

	/**
	 */
	@Override
	public void onDraw(@NonNull final Canvas canvas, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		if (shouldDecorate(parent, state)) {
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
	 */
	@Override
	protected boolean shouldDecorate(@NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		return super.shouldDecorate(parent, state) && mDivider != null && mDividerThickness > 0;
	}

	/**
	 * Called from {@link #onDrawOver(Canvas, RecyclerView, RecyclerView.State)} in order to draw
	 * this decoration in vertical orientation.
	 *
	 * @param canvas Canvas on which to draw.
	 * @param parent RecyclerView into which is this decoration added.
	 * @param state  Current state of the parent RecyclerView.
	 */
	@SuppressLint("NewApi")
	protected void onDrawHorizontally(@NonNull final Canvas canvas, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		canvas.save();
		int top;
		int bottom;
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
		top += mDividerOffsetStart;
		bottom += mDividerOffsetEnd;
		final int childCount = parent.getChildCount();
		for (int childIndex = 0; childIndex < childCount; childIndex++) {
			if ((mSkipFirst && childIndex == 0) || (mSkipLast && childIndex == childCount - 1)) {
				continue;
			}
			final View child = parent.getChildAt(childIndex);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int right = mBounds.right + Math.round(child.getTranslationX());
			final int left = right - mDividerThickness;
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * Called from {@link #onDrawOver(Canvas, RecyclerView, RecyclerView.State)} in order to draw
	 * this decoration in horizontal orientation.
	 *
	 * @param canvas Canvas on which to draw.
	 * @param parent RecyclerView into which is this decoration added.
	 * @param state  Current state of the parent RecyclerView.
	 */
	@SuppressLint("NewApi")
	protected void onDrawVertically(@NonNull final Canvas canvas, @NonNull final RecyclerView parent, @NonNull final RecyclerView.State state) {
		final boolean hasRtlDirection = ViewCompat.getLayoutDirection(parent) == ViewCompat.LAYOUT_DIRECTION_RTL;
		canvas.save();
		int left;
		int right;
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
		left += hasRtlDirection ? mDividerOffsetEnd : mDividerOffsetStart;
		right += hasRtlDirection ? mDividerOffsetStart : mDividerOffsetEnd;
		final int childCount = parent.getChildCount();
		for (int childIndex = 0; childIndex < childCount; childIndex++) {
			if ((mSkipFirst && childIndex == 0) || (mSkipLast && childIndex == childCount - 1)) {
				continue;
			}
			final View child = parent.getChildAt(childIndex);
			parent.getDecoratedBoundsWithMargins(child, mBounds);
			final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
			final int top = bottom - mDividerThickness;
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(canvas);
		}
		canvas.restore();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
