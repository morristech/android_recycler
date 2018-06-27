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

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemDividerDecorationTest extends RobolectricTestCase {

	// Arrange:
	// Act:
	// Assert:

	private static final int MOCK_ITEMS_COUNT = 10;

	private Canvas mMockCanvas = mock(Canvas.class);
	private RecyclerView mMockRecyclerView;
	private RecyclerView.State mMockRecyclerViewState;
	private View mItemView;

	public ItemDividerDecorationTest() {
		this.mMockCanvas = mock(Canvas.class);
		this.mMockRecyclerView = mock(RecyclerView.class);
		this.mMockRecyclerViewState = mock(RecyclerView.State.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mItemView = new TextView(application);
		resetMock(mMockCanvas);
		resetMock(mMockRecyclerView);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		when(mMockRecyclerView.getChildCount()).thenReturn(MOCK_ITEMS_COUNT);
		when(mMockRecyclerView.getChildAt(anyInt())).thenReturn(mItemView);
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(MOCK_ITEMS_COUNT);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mItemView = null;
	}

	@Test
	public void testEmptyInstantiation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
		assertThat(decoration.getDivider(), is(nullValue()));
		assertThat(decoration.getDividerThickness(), is(0));
		assertThat(decoration.getDividerOffsetStart(), is(0));
		assertThat(decoration.getDividerOffsetEnd(), is(0));
		assertThat(decoration.getPrecondition(), is(RecyclerViewItemDecoration.Precondition.EMPTY));
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
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSet() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application, null);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application, null, 0);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
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
	public void testSetGetDividerOffsets() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDividerOffset(10, 5);
		assertThat(decoration.getDividerOffsetStart(), is(10));
		assertThat(decoration.getDividerOffsetEnd(), is(5));
		decoration.setDividerOffset(1, 2);
		assertThat(decoration.getDividerOffsetStart(), is(1));
		assertThat(decoration.getDividerOffsetEnd(), is(2));
	}

	@Test
	public void testGetItemOffsetsForVerticalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(10));
		assertThat(rect.right, is(5));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(decoration.getDividerThickness()));
		verify(mMockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForVerticalOrientationAndRTLLayoutDirection() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(5));
		assertThat(rect.right, is(10));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(decoration.getDividerThickness()));
		verify(mMockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForHorizontalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(decoration.getDividerThickness()));
		assertThat(rect.top, is(10));
		assertThat(rect.bottom, is(5));
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipFirst() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
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
		verify(mockPrecondition, times(itemCount - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipLast() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
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
		verify(mockPrecondition, times(itemCount - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipBoth() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
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
			assertThat(rect.bottom, is(i == 0 || i == itemCount - 1 ? 0 : decoration.getDividerThickness()));
		}
		verify(mockPrecondition, times(itemCount - 2)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsWithUnsatisfiedPrecondition() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForZeroDividerThickness() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setDividerThickness(0);
		final Rect rect = new Rect();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mMockRecyclerView, mMockRecyclerViewState, mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForUnknownAdapterPosition() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(RecyclerView.NO_POSITION);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mMockRecyclerViewState, mockPrecondition);
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
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(null);
		decoration.setDividerThickness(2);
		assertThat(decoration.shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
	}

	@Test
	public void testShouldDecorateWithZeroDividerThickness() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
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
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		decoration.onDraw(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testOnDrawForHorizontalOrientation() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		decoration.onDraw(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
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
	public void testOnDrawHorizontallyForRTLLayoutDirection() {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		when(mMockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		decoration.onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mMockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mMockRecyclerView).getChildCount();
		final int verifyTimes = mMockRecyclerView.getChildCount() - 1;
		verify(mockDivider, times(verifyTimes)).setBounds(
				-decoration.getDividerThickness(),
				10,
				0,
				5
		);
		verify(mockDivider, times(verifyTimes)).draw(mMockCanvas);
		verify(mockPrecondition, times(verifyTimes)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mMockCanvas).restore();
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

	@Test
	public void testOnDrawHorizontallyWithUnsatisfiedPrecondition() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipLast(false);
		final Drawable mockDivider = mock(Drawable.class);
		decoration.setDivider(mockDivider);
		decoration.onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mockDivider, times(0)).draw(mMockCanvas);
		verify(mMockCanvas).restore();
	}

	private void testOnDrawHorizontallyInner(boolean skipFirst, boolean skipLast) {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
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
		verify(mMockCanvas).restore();
	}

	@Test
	@SuppressLint("NewApi")
	public void testOnDrawClippedHorizontally() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		when(mMockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mMockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mMockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mMockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mMockRecyclerView.getPaddingBottom()).thenReturn(8);
		decoration.onDrawHorizontally(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mMockCanvas).clipRect(
				mMockRecyclerView.getPaddingLeft(),
				mMockRecyclerView.getPaddingTop(),
				mMockRecyclerView.getWidth() - mMockRecyclerView.getPaddingRight(),
				mMockRecyclerView.getHeight() - mMockRecyclerView.getPaddingBottom()
		);
		verify(mMockCanvas).restore();
	}

	@Test
	public void testOnDrawVertically() {
		this.testOnDrawVerticallyInner(false, false);
	}

	@Test
	public void testOnDrawVerticallyForRTLLayoutDirection() {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		when(mMockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		decoration.onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockRecyclerView).getLayoutDirection();
		verify(mMockCanvas).save();
		verify(mMockRecyclerView).getChildCount();
		int verifyTimes = mMockRecyclerView.getChildCount() - 1;
		verify(mockDivider, times(verifyTimes)).setBounds(
				5,
				-decoration.getDividerThickness(),
				10,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mMockCanvas);
		verify(mMockCanvas).restore();
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

	@Test
	public void testOnDrawVerticallyWithUnsatisfiedPrecondition() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipLast(false);
		final Drawable mockDivider = mock(Drawable.class);
		decoration.setDivider(mockDivider);
		decoration.onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mockDivider, times(0)).draw(mMockCanvas);
		verify(mMockCanvas).restore();
	}

	private void testOnDrawVerticallyInner(boolean skipFirst, boolean skipLast) {
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mMockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mMockRecyclerView).getChildCount();
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
		verify(mMockCanvas).restore();
	}

	@Test
	@SuppressLint("NewApi")
	public void testOnDrawClippedVertically() {
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		when(mMockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mMockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mMockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mMockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mMockRecyclerView.getPaddingBottom()).thenReturn(8);
		decoration.onDrawVertically(mMockCanvas, mMockRecyclerView, mMockRecyclerViewState);
		verify(mMockCanvas).save();
		verify(mMockCanvas).clipRect(
				mMockRecyclerView.getPaddingLeft(),
				mMockRecyclerView.getPaddingTop(),
				mMockRecyclerView.getWidth() - mMockRecyclerView.getPaddingRight(),
				mMockRecyclerView.getHeight() - mMockRecyclerView.getPaddingBottom()
		);
		verify(mMockCanvas).restore();
	}
}