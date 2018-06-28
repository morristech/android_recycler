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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
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
public final class ItemSpaceDecorationTest extends RobolectricTestCase {

	// Arrange:
	// Act:
	// Assert:

	private static final int MOCK_ITEMS_COUNT = 10;
    
	private final Canvas mockCanvas;
	private final RecyclerView mockRecyclerView;
	private final RecyclerView.State mockRecyclerViewState;
	private View itemView;

	public ItemSpaceDecorationTest() {
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

	@Test public void testInstantiation() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration();
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithGlobalSpacings() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(16, 8);
		// Assert:
		assertThat(decoration.getHorizontalStart(), is(16));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(8));
		assertThat(decoration.getVerticalEnd(), is(8));
	}

	@Test public void testInstantiationWithSeparateSpacings() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(4, 16, 8, 24);
		// Assert:
		assertThat(decoration.getHorizontalStart(), is(4));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(8));
		assertThat(decoration.getVerticalEnd(), is(24));
	}

	@Test public void testInstantiationWithContext() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSet() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application, null);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application, null, 0);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testGetItemOffsets() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.left, is(decoration.getHorizontalStart()));
		assertThat(rect.right, is(decoration.getHorizontalEnd()));
		assertThat(rect.top, is(decoration.getVerticalStart()));
		assertThat(rect.bottom, is(decoration.getVerticalEnd()));
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsForRTLLayoutDirection() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		when(mockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		// Act:
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.left, is(decoration.getHorizontalEnd()));
		assertThat(rect.right, is(decoration.getHorizontalStart()));
		assertThat(rect.top, is(decoration.getVerticalStart()));
		assertThat(rect.bottom, is(decoration.getVerticalEnd()));
		verify(mockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipFirst() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(false);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(i == 0 ? 0 : decoration.getHorizontalStart()));
			assertThat(rect.right, is(i == 0 ? 0 : decoration.getHorizontalEnd()));
			assertThat(rect.top, is(i == 0 ? 0 : decoration.getVerticalStart()));
			assertThat(rect.bottom, is(i == 0 ? 0 : decoration.getVerticalEnd()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipLast() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(false);
		decoration.setSkipLast(true);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(i == itemCount - 1 ? 0 : decoration.getHorizontalStart()));
			assertThat(rect.right, is(i == itemCount - 1 ? 0 : decoration.getHorizontalEnd()));
			assertThat(rect.top, is(i == itemCount - 1 ? 0 : decoration.getVerticalStart()));
			assertThat(rect.bottom, is(i == itemCount - 1 ? 0 : decoration.getVerticalEnd()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsSkipBoth() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		// Act + Assert:
		final Rect rect = new Rect();
		final int itemCount = mockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(i);
			decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
			assertThat(rect.left, is(i == 0 || i == itemCount - 1 ? 0 : 1));
			assertThat(rect.right, is(i == 0 || i  == itemCount - 1 ? 0 : 2));
			assertThat(rect.top, is(i == 0 || i  == itemCount - 1 ? 0 : 3));
			assertThat(rect.bottom, is(i == 0 || i  == itemCount - 1 ? 0 : 4));
		}
		verify(mockPrecondition, times(itemCount - 2)).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test public void testGetItemOffsetsForUnknownAdapterPosition() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		when(mockRecyclerView.getChildAdapterPosition(itemView)).thenReturn(RecyclerView.NO_POSITION);
		// Act:
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mockRecyclerViewState, mockPrecondition);
	}

	@Test public void testGetItemOffsetsWithUnsatisfiedPrecondition() {
		// Arrange:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(itemView, mockRecyclerView, mockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		final Rect rect = new Rect();
		// Act:
		decoration.getItemOffsets(rect, itemView, mockRecyclerView, mockRecyclerViewState);
		// Assert:
		assertThat(rect.isEmpty(), is(true));
		verify(mockPrecondition).check(itemView, mockRecyclerView, mockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}
}