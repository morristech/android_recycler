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

	private static final int MOCK_ITEMS_COUNT = 10;
    
	private Canvas mMockCanvas = mock(Canvas.class);
	private RecyclerView mMockRecyclerView;
	private RecyclerView.State mMockRecyclerViewState;
	private View mItemView;

	public ItemSpaceDecorationTest() {
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

	@Test
	public void testEmptyInstantiation() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration();
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithGlobalSpacings() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(16, 8);
		assertThat(decoration.getHorizontalStart(), is(16));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(8));
		assertThat(decoration.getVerticalEnd(), is(8));
	}

	@Test
	public void testInstantiationWithSeparateSpacings() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(4, 16, 8, 24);
		assertThat(decoration.getHorizontalStart(), is(4));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(8));
		assertThat(decoration.getVerticalEnd(), is(24));
	}

	@Test
	public void testInstantiationWithContext() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSet() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application, null);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(application, null, 0);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testGetItemOffsets() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(decoration.getHorizontalStart()));
		assertThat(rect.right, is(decoration.getHorizontalEnd()));
		assertThat(rect.top, is(decoration.getVerticalStart()));
		assertThat(rect.bottom, is(decoration.getVerticalEnd()));
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForRTLLayoutDirection() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getLayoutDirection()).thenReturn(ViewCompat.LAYOUT_DIRECTION_RTL);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(decoration.getHorizontalEnd()));
		assertThat(rect.right, is(decoration.getHorizontalStart()));
		assertThat(rect.top, is(decoration.getVerticalStart()));
		assertThat(rect.bottom, is(decoration.getVerticalEnd()));
		verify(mMockRecyclerView).getLayoutDirection();
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipFirst() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(false);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(i == 0 ? 0 : decoration.getHorizontalStart()));
			assertThat(rect.right, is(i == 0 ? 0 : decoration.getHorizontalEnd()));
			assertThat(rect.top, is(i == 0 ? 0 : decoration.getVerticalStart()));
			assertThat(rect.bottom, is(i == 0 ? 0 : decoration.getVerticalEnd()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipLast() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(false);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(i == itemCount - 1 ? 0 : decoration.getHorizontalStart()));
			assertThat(rect.right, is(i == itemCount - 1 ? 0 : decoration.getHorizontalEnd()));
			assertThat(rect.top, is(i == itemCount - 1 ? 0 : decoration.getVerticalStart()));
			assertThat(rect.bottom, is(i == itemCount - 1 ? 0 : decoration.getVerticalEnd()));
		}
		verify(mockPrecondition, times(itemCount - 1)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsSkipBoth() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		final int itemCount = mMockRecyclerViewState.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(i == 0 || i == itemCount - 1 ? 0 : 1));
			assertThat(rect.right, is(i == 0 || i  == itemCount - 1 ? 0 : 2));
			assertThat(rect.top, is(i == 0 || i  == itemCount - 1 ? 0 : 3));
			assertThat(rect.bottom, is(i == 0 || i  == itemCount - 1 ? 0 : 4));
		}
		verify(mockPrecondition, times(itemCount - 2)).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsForUnknownAdapterPosition() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(true);
		decoration.setPrecondition(mockPrecondition);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(RecyclerView.NO_POSITION);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mMockRecyclerViewState, mockPrecondition);
	}

	@Test
	public void testGetItemOffsetsWithUnsatisfiedPrecondition() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		final ItemSpaceDecoration.Precondition mockPrecondition = mock(ItemSpaceDecoration.Precondition.class);
		when(mockPrecondition.check(mItemView, mMockRecyclerView, mMockRecyclerViewState)).thenReturn(false);
		decoration.setPrecondition(mockPrecondition);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verify(mockPrecondition).check(mItemView, mMockRecyclerView, mMockRecyclerViewState);
		verifyNoMoreInteractions(mockPrecondition);
	}
}