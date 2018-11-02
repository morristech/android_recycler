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

	private static final int MOCK_ITEMS_COUNT = 10;

	private final Canvas mockCanvas;
	private final RecyclerView mockRecyclerView;
	private final RecyclerView.State mockRecyclerViewState;
	private View itemView;

	public ItemDividerDecorationTest() {
		this.mockCanvas = mock(Canvas.class);
		this.mockRecyclerView = mock(RecyclerView.class);
		this.mockRecyclerViewState = mock(RecyclerView.State.class);
	}

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		this.itemView = new TextView(application);
		resetMock(mockCanvas);
		resetMock(mockRecyclerView);
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		when(mockRecyclerView.getChildCount()).thenReturn(MOCK_ITEMS_COUNT);
		when(mockRecyclerView.getChildAt(anyInt())).thenReturn(itemView);
		resetMock(mockRecyclerViewState);
		when(mockRecyclerViewState.getItemCount()).thenReturn(MOCK_ITEMS_COUNT);
	}

	@Override public void afterTest() throws Exception {
		super.afterTest();
		this.itemView = null;
	}

	@Test public void testInstantiation() {
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
		assertThat(decoration.getDivider(), is(nullValue()));
		assertThat(decoration.getDividerThickness(), is(0));
		assertThat(decoration.getDividerOffsetStart(), is(0));
		assertThat(decoration.getDividerOffsetEnd(), is(0));
		assertThat(decoration.getPrecondition(), is(RecyclerViewItemDecoration.Precondition.EMPTY));
	}

	@Test public void testInstantiationWithDividerAndVerticalOrientation() {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration(ItemDividerDecoration.VERTICAL, mockDivider);
		// Assert:
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
		assertThat(decoration.getDivider(), is(mockDivider));
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test public void testInstantiationWithDividerAndHorizontalOrientation() {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration(ItemDividerDecoration.HORIZONTAL, mockDivider);
		// Assert:
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.HORIZONTAL));
		assertThat(decoration.getDivider(), is(mockDivider));
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
	}

	@Test public void testInstantiationWithContext() {
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testInstantiationWithContextAttrsSet() {
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application, null);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		// Act:
		final ItemDividerDecoration decoration = new ItemDividerDecoration(application, null, 0);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		// Act + Assert:
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.HORIZONTAL));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		assertThat(decoration.getOrientation(), is(ItemDividerDecoration.VERTICAL));
	}

	@Test public void testSetOrientationAfterDivider() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		decoration.setDivider(mockDivider);
		// Act + Assert:
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test public void testSetDividerForVerticalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		// Act:
		decoration.setDivider(mockDivider);
		// Assert:
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test public void testSetDividerForHorizontalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicWidth()).thenReturn(4);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		// Act:
		decoration.setDivider(mockDivider);
		// Assert:
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicWidth()));
	}

	@Test public void testSetDividerAfterThickness() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDividerThickness(4);
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		// Act:
		decoration.setDivider(mockDivider);
		// Assert:
		assertThat(decoration.getDividerThickness(), is(mockDivider.getIntrinsicHeight()));
	}

	@Test public void testDividerThickness() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		// Act + Assert:
		decoration.setDividerThickness(10);
		assertThat(decoration.getDividerThickness(), is(10));
	}

	@Test public void testSetDividerThicknessAfterDivider() {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		when(mockDivider.getIntrinsicHeight()).thenReturn(2);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mockDivider);
		// Act:
		decoration.setDividerThickness(4);
		// Assert:
		assertThat(decoration.getDividerThickness(), is(4));
	}

	@Test public void testDividerOffsets() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		// Act + Assert:
		decoration.setDividerOffset(10, 5);
		assertThat(decoration.getDividerOffsetStart(), is(10));
		assertThat(decoration.getDividerOffsetEnd(), is(5));
		decoration.setDividerOffset(1, 2);
		assertThat(decoration.getDividerOffsetStart(), is(1));
		assertThat(decoration.getDividerOffsetEnd(), is(2));
	}

	@Test public void testGetItemOffsetsForVerticalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(0));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(decoration.getDividerThickness()));
		verify(mockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsForVerticalOrientationAndRTLLayoutDirection() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		when(mockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(0));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(decoration.getDividerThickness()));
		verify(mockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsForHorizontalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.left, is(0));
		assertThat(rect.right, is(decoration.getDividerThickness()));
		assertThat(rect.top, is(0));
		assertThat(rect.bottom, is(0));
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipFirst() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(false);
		decoration.setDividerThickness(4);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == 0 ? 0 : decoration.getDividerThickness()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipLast() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(false);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == itemCount - 1 ? 0 : decoration.getDividerThickness()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipBoth() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(0));
			assertThat(rect.right, is(0));
			assertThat(rect.top, is(0));
			assertThat(rect.bottom, is(i == 0 || i == itemCount - 1 ? 0 : decoration.getDividerThickness()));
		}
		verify(mockPrecondition, times(itemCount - 2)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsWithUnsatisfiedPrecondition() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDividerThickness(4);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.isEmpty(), is(true));
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsForZeroDividerThickness() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setDividerThickness(0);
		final Rect rect = new Rect();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		// Act + Assert:
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mockRecyclerView, mockRecyclerViewState, mockPrecondition);
	}

	@Test public void testGetItemOffsetsForUnknownAdapterPosition() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		decoration.setDividerThickness(4);
		final Rect rect = new Rect();
		when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(RecyclerView.NO_POSITION);
		// Act:
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mockRecyclerViewState, mockPrecondition);
	}

	@Test public void testShouldDecorate() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(2);
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(true));
	}

	@Test public void testShouldDecorateWithoutDivider() {
		// Arrange:
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(null);
		decoration.setDividerThickness(2);
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
	}

	@Test public void testShouldDecorateWithZeroDividerThickness() {
		// Arrange:
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(0);
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
	}

	@Test public void testShouldDecorateWithoutDividerAndThicknessAndValidRecyclerViewAndState() {
		// Arrange:
		when(mockRecyclerView.getLayoutManager()).thenReturn(null);
		when(mockRecyclerViewState.getItemCount()).thenReturn(0);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(null);
		decoration.setDividerThickness(0);
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
	}

	@Test public void testOnDrawForVerticalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.VERTICAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		// Act:
		decoration.onDraw(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testOnDrawForHorizontalOrientation() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		decoration.setDivider(mock(Drawable.class));
		decoration.setDividerThickness(4);
		// Act:
		decoration.onDraw(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testOnDrawForRecyclerViewWithoutLayoutManager() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setOrientation(ItemDividerDecoration.HORIZONTAL);
		when(mockRecyclerView.getLayoutManager()).thenReturn(null);
		// Act:
		decoration.onDraw(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verifyZeroInteractions(mockCanvas);
	}

	@Test public void testOnDrawHorizontally() {
		this.testOnDrawHorizontallyInner(false, false);
	}

	@Test public void testOnDrawHorizontallyForRTLLayoutDirection() {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		when(mockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		// Act:
		decoration.onDrawHorizontally(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mockRecyclerView).getChildCount();
		final int verifyTimes = mockRecyclerView.getChildCount() - 1;
		verify(mockDivider, times(verifyTimes)).setBounds(
				-decoration.getDividerThickness(),
				10,
				0,
				5
		);
		verify(mockDivider, times(verifyTimes)).draw(mockCanvas);
		verify(mockPrecondition, times(verifyTimes)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mockCanvas).restore();
	}

	@Test public void testOnDrawHorizontallySkipFirst() {
		this.testOnDrawHorizontallyInner(true, false);
	}

	@Test public void testOnDrawHorizontallySkipLast() {
		this.testOnDrawHorizontallyInner(false, true);
	}

	@Test public void testOnDrawHorizontallySkipBoth() {
		this.testOnDrawHorizontallyInner(true, true);
	}

	@Test public void testOnDrawHorizontallyWithUnsatisfiedPrecondition() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipLast(false);
		final Drawable mockDivider = mock(Drawable.class);
		decoration.setDivider(mockDivider);
		// Act:
		decoration.onDrawHorizontally(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mockDivider, times(0)).draw(mockCanvas);
		verify(mockCanvas).restore();
	}

	private void testOnDrawHorizontallyInner(boolean skipFirst, boolean skipLast) {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		// Act:
		decoration.onDrawHorizontally(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mockRecyclerView).getChildCount();
		int verifyTimes = mockRecyclerView.getChildCount();
		verifyTimes -= skipFirst ? 1 : 0;
		verifyTimes -= skipLast ? 1 : 0;
		verify(mockDivider, times(verifyTimes)).setBounds(
				-decoration.getDividerThickness(),
				0,
				0,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mockCanvas);
		verify(mockCanvas).restore();
	}

	@SuppressLint("NewApi")
	@Test public void testOnDrawClippedHorizontally() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		when(mockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mockRecyclerView.getPaddingBottom()).thenReturn(8);
		// Act:
		decoration.onDrawHorizontally(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockCanvas).clipRect(
				mockRecyclerView.getPaddingLeft(),
				mockRecyclerView.getPaddingTop(),
				mockRecyclerView.getWidth() - mockRecyclerView.getPaddingRight(),
				mockRecyclerView.getHeight() - mockRecyclerView.getPaddingBottom()
		);
		verify(mockCanvas).restore();
	}

	@Test public void testOnDrawVertically() {
		this.testOnDrawVerticallyInner(false, false);
	}

	@Test public void testOnDrawVerticallyForRTLLayoutDirection() {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		decoration.setDividerOffset(10, 5);
		when(mockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		// Act:
		decoration.onDrawVertically(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockRecyclerView).getLayoutDirection();
		verify(mockCanvas).save();
		verify(mockRecyclerView).getChildCount();
		int verifyTimes = mockRecyclerView.getChildCount() - 1;
		verify(mockDivider, times(verifyTimes)).setBounds(
				5,
				-decoration.getDividerThickness(),
				10,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mockCanvas);
		verify(mockCanvas).restore();
	}

	@Test public void testOnDrawVerticallySkipFirst() {
		this.testOnDrawVerticallyInner(true, false);
	}

	@Test public void testOnDrawVerticallySkipLast() {
		this.testOnDrawVerticallyInner(false, true);
	}

	@Test public void testOnDrawVerticallySkipBoth() {
		this.testOnDrawVerticallyInner(true, true);
	}

	@Test public void testOnDrawVerticallyWithUnsatisfiedPrecondition() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		final ItemDividerDecoration.Precondition mockPrecondition = mock(ItemDividerDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipLast(false);
		final Drawable mockDivider = mock(Drawable.class);
		decoration.setDivider(mockDivider);
		// Act:
		decoration.onDrawVertically(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockPrecondition, times(MOCK_ITEMS_COUNT)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
		verify(mockDivider, times(0)).draw(mockCanvas);
		verify(mockCanvas).restore();
	}

	private void testOnDrawVerticallyInner(boolean skipFirst, boolean skipLast) {
		// Arrange:
		final Drawable mockDivider = mock(Drawable.class);
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setSkipFirst(skipFirst);
		decoration.setSkipLast(skipLast);
		decoration.setDivider(mockDivider);
		decoration.setDividerThickness(4);
		// Act:
		decoration.onDrawVertically(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockCanvas, times(0)).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mockRecyclerView).getChildCount();
		int verifyTimes = mockRecyclerView.getChildCount();
		verifyTimes -= skipFirst ? 1 : 0;
		verifyTimes -= skipLast ? 1 : 0;
		verify(mockDivider, times(verifyTimes)).setBounds(
				0,
				-decoration.getDividerThickness(),
				0,
				0
		);
		verify(mockDivider, times(verifyTimes)).draw(mockCanvas);
		verify(mockCanvas).restore();
	}

	@SuppressLint("NewApi")
	@Test public void testOnDrawClippedVertically() {
		// Arrange:
		final ItemDividerDecoration decoration = new ItemDividerDecoration();
		decoration.setDivider(mock(Drawable.class));
		when(mockRecyclerView.getClipToPadding()).thenReturn(true);
		when(mockRecyclerView.getPaddingLeft()).thenReturn(16);
		when(mockRecyclerView.getPaddingRight()).thenReturn(16);
		when(mockRecyclerView.getPaddingTop()).thenReturn(8);
		when(mockRecyclerView.getPaddingBottom()).thenReturn(8);
		// Act:
		decoration.onDrawVertically(mockCanvas, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		verify(mockCanvas).save();
		verify(mockCanvas).clipRect(
				mockRecyclerView.getPaddingLeft(),
				mockRecyclerView.getPaddingTop(),
				mockRecyclerView.getWidth() - mockRecyclerView.getPaddingRight(),
				mockRecyclerView.getHeight() - mockRecyclerView.getPaddingBottom()
		);
		verify(mockCanvas).restore();
	}
}