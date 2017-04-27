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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestResources;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ItemDividerDecorationTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "ItemDividerDecorationTest";

	private Canvas mMockCanvas = mock(Canvas.class);
	private RecyclerView mMockRecyclerView;
	private RecyclerView.State mMockRecyclerViewState;
	private View mItemView;
	//private ItemDividerDecoration decoration;

	public ItemDividerDecorationTest() {
		this.mMockCanvas = mock(Canvas.class);
		this.mMockRecyclerView = mock(RecyclerView.class);
		this.mMockRecyclerViewState = mock(RecyclerView.State.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mItemView = new TextView(mContext);
		resetMock(mMockCanvas);
		resetMock(mMockRecyclerView);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(mContext));
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(10);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mItemView = null;
	}

	@Test
	public void testInstantiationWithDividerAndVerticalOrientation() {
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		final ItemDividerDecoration decoration = new ItemDividerDecoration(ItemDividerDecoration.VERTICAL, mockDivider);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
		assertThat(decoration.getDivider(), is(mockDivider));
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test
	public void testInstantiationWithDividerAndHorizontalOrientation() {
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		final ItemDividerDecoration decoration = new ItemDividerDecoration(ItemDividerDecoration.HORIZONTAL, mockDivider);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.HORIZONTAL));
		assertThat(decoration.getDivider(), is(mockDivider));
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
	}

	@Test
	public void testInstantiationWithContext() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSet() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	@SuppressWarnings({"deprecation", "ConstantConditions"})
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleRes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.Divider"
		));
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.HORIZONTAL));
		assertThat(decoration.getDivider(), instanceOf(ColorDrawable.class));
		assertThat(((ColorDrawable) decoration.getDivider()).getColor(), is(mContext.getResources().getColor(android.R.color.darker_gray)));
		assertThat(decoration.getDividerThickness(), is(2));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipNone() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.Divider.SkipNone"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipFirst() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.Divider.SkipFirst"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipLast() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.Divider.SkipLast"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipBoth() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.Divider.SkipBoth"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testSkipsFirstDefault() {
		assertThat(new ItemDividerDecoration().skipsFirst(), is(false));
	}

	@Test
	public void testSkipsLastDefault() {
		assertThat(new ItemDividerDecoration().skipsLast(), is(true));
	}

	@Test
	public void testSetGetOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.HORIZONTAL));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
	}

	@Test
	public void testSetOrientationAfterDivider() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		decoration.setDivider(mockDivider);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test
	public void testGetOrientationDefault() {
		assertThat(new ItemDividerDecoration().getOrientation(), is(ItemDividerDecoration.VERTICAL));
	}

	@Test
	public void testSetDividerForVerticalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		decoration.setDivider(mockDivider);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test
	public void testSetDividerForHorizontalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		decoration.setDivider(mockDivider);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
	}

	@Test
	public void testSetDividerAfterThickness() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDividerThickness(4);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		decoration.setDivider(mockDivider);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test
	public void testGetDividerDefault() {
		assertThat(new ItemDividerDecoration().getDivider(), is(nullValue()));
	}

	@Test
	public void testSetGetDividerThickness() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDividerThickness(10);
		assertThat(decoration.getDividerThickness(), is(10));
	}

	@Test
	public void testSetDividerThicknessAfterDivider() {
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		assertThat(decoration.getDividerThickness(), is(4));
	}

	@Test
	public void testGetDividerThicknessDefault() {
		assertThat(new ItemDividerDecoration().getDividerThickness(), is(0));
	}

	@Test
	public void testGetItemOffsetsForVerticalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(false);
		decoration.setSkipLast(false);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(0));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(decoration.getDividerThickness()));
	}

	@Test
	public void testGetItemOffsetsForHorizontalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(false);
		decoration.setSkipLast(false);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(decoration.getDividerThickness()));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(0));
	}

	@Test
	public void testGetItemOffsetsSkipFirst() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(true);
		decoration.setSkipLast(false);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == 0 ? 0 : decoration.getDividerThickness()));
		}
	}

	@Test
	public void testGetItemOffsetsSkipLast() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(false);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == itemCount - 1 ? 0 : decoration.getDividerThickness()));
		}
	}

	@Test
	public void testGetItemOffsetsSkipBoth() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == 0 || i == itemCount - 1 ? 0 : decoration.getDividerThickness()));
		}
	}

	@Test
	public void testGetItemOffsetsWithZeroDividerThickness() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(false);
		decoration.setSkipLast(false);
		decoration.setDividerThickness(0);
		final Rect rect = new Rect();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
	}

	@Test
	public void testGetItemOffsetsForUnknownAdapterPosition() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(RecyclerView.NO_POSITION);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mMockRecyclerViewState);
	}

	@Test
	public void testShouldDecorate() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(2);
		assertThat(decoration.shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(true));
	}

	@Test
	public void testShouldDecorateWithoutDivider() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(null);
		decoration.setDividerThickness(2);
		assertThat(decoration.shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
	}

	@Test
	public void testShouldDecorateWithZeroDividerThickness() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(mContext));
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(0);
		assertThat(decoration.shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
	}

	@Test
	public void testShouldDecorateWithoutDividerAndThicknessAndValidRecyclerViewAndState() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(null);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(0);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(null);
		decoration.setDividerThickness(0);
		assertThat(decoration.shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
	}

	@Test
	public void testOnDrawForVerticalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		decoration.onDraw(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
	}

	@Test
	public void testOnDrawForHorizontalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		decoration.onDraw(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
	}

	@Test
	public void testOnDrawForRecyclerViewWithoutLayoutManager() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(null);
		decoration.onDraw(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verifyZeroInteractions(mMockCanvas);
	}

	@Test
	public void testOnDrawHorizontally() {
		this.testOnDrawHorizontallyInner(false, false);
	}

	@Test
	public void testOnDrawHorizontallySkipFirst() {
		this.testOnDrawHorizontallyInner(true, false);
	}

	@Test
	public void testOnDrawHorizontallySkipLast() {
		this.testOnDrawHorizontallyInner(false, true);
	}

	@Test
	public void testOnDrawHorizontallySkipBoth() {
		this.testOnDrawHorizontallyInner(true, true);
	}

	private void testOnDrawHorizontallyInner(boolean skipFirst, boolean skipLast) {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		when(mMockRecyclerView.getChildCount()).thenReturn(10);
		when(mMockRecyclerView.getChildAt(anyInt())).thenReturn(new View(mContext));
		decoration.onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
		verify(mMockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mMockRecyclerView, times(1)).getChildCount();
		int verifyTimes = mMockRecyclerView.getChildCount();
		verifyTimes -= skipFirst ? 1 : 0;
		verifyTimes -= skipLast ? 1 : 0;
		verify(mockDivider, times(verifyTimes)).setBounds(
				-decoration.getDividerThickness(),
				0,
				0,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mMockCanvas);
		verify(mMockCanvas, times(1)).restore();
	}

	@Test
	@SuppressLint("NewApi")
	public void testOnDrawClippedHorizontally() {
		when(mMockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mMockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mMockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mMockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mMockRecyclerView.getPaddingBottom()).thenReturn(8);
		new ItemDividerDecoration().onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
		verify(mMockCanvas, times(1)).clipRect(
				mMockRecyclerView.getPaddingLeft(),
				mMockRecyclerView.getPaddingTop(),
				mMockRecyclerView.getWidth() - mMockRecyclerView.getPaddingRight(),
				mMockRecyclerView.getHeight() - mMockRecyclerView.getPaddingBottom()
		);
		verify(mMockCanvas, times(1)).restore();
	}

	@Test
	public void testOnDrawVertically() {
		this.testOnDrawVerticallyInner(false, false);
	}

	@Test
	public void testOnDrawVerticallySkipFirst() {
		this.testOnDrawVerticallyInner(true, false);
	}

	@Test
	public void testOnDrawVerticallySkipLast() {
		this.testOnDrawVerticallyInner(false, true);
	}

	@Test
	public void testOnDrawVerticallySkipBoth() {
		this.testOnDrawVerticallyInner(true, true);
	}

	private void testOnDrawVerticallyInner(boolean skipFirst, boolean skipLast) {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		when(mMockRecyclerView.getChildCount()).thenReturn(10);
		when(mMockRecyclerView.getChildAt(anyInt())).thenReturn(new View(mContext));
		decoration.onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
		verify(mMockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mMockRecyclerView, times(1)).getChildCount();
		int verifyTimes = mMockRecyclerView.getChildCount();
		verifyTimes -= skipFirst ? 1 : 0;
		verifyTimes -= skipLast ? 1 : 0;
		verify(mockDivider, times(verifyTimes)).setBounds(
				0,
				-decoration.getDividerThickness(),
				0,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mMockCanvas);
		verify(mMockCanvas, times(1)).restore();
	}

	@Test
	@SuppressLint("NewApi")
	public void testOnDrawClippedVertically() {
		when(mMockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mMockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mMockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mMockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mMockRecyclerView.getPaddingBottom()).thenReturn(8);
		new ItemDividerDecoration().onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas, times(1)).save();
		verify(mMockCanvas, times(1)).clipRect(
				mMockRecyclerView.getPaddingLeft(),
				mMockRecyclerView.getPaddingTop(),
				mMockRecyclerView.getWidth() - mMockRecyclerView.getPaddingRight(),
				mMockRecyclerView.getHeight() - mMockRecyclerView.getPaddingBottom()
		);
		verify(mMockCanvas, times(1)).restore();
	}
}
