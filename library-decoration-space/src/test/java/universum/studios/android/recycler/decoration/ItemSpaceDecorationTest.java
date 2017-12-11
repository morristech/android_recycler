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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemSpaceDecorationTest extends RobolectricTestCase {
    
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
		this.mItemView = new TextView(mApplication);
		resetMock(mMockCanvas);
		resetMock(mMockRecyclerView);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(mApplication));
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(10);
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
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(mApplication);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSet() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(mApplication, null);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(mApplication, null, 0);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testSkipsFirstDefault() {
		assertThat(new ItemSpaceDecoration().skipsFirst(), is(false));
	}

	@Test
	public void testSkipsLastDefault() {
		assertThat(new ItemSpaceDecoration().skipsLast(), is(false));
	}

	@Test
	public void testGetItemOffsets() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		decoration.setSkipFirst(false);
		decoration.setSkipLast(false);
		final Rect rect = new Rect();
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.left, is(decoration.getHorizontalStart()));
		assertThat(rect.right, is(decoration.getHorizontalEnd()));
		assertThat(rect.top, is(decoration.getVerticalStart()));
		assertThat(rect.bottom, is(decoration.getVerticalEnd()));
	}

	@Test
	public void testGetItemOffsetsSkipFirst() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(false);
		final Rect rect = new Rect();
		for (int i = 0; i < mMockRecyclerViewState.getItemCount(); i++) {
			when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(i);
			decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
			assertThat(rect.left, is(i == 0 ? 0 : decoration.getHorizontalStart()));
			assertThat(rect.right, is(i == 0 ? 0 : decoration.getHorizontalEnd()));
			assertThat(rect.top, is(i == 0 ? 0 : decoration.getVerticalStart()));
			assertThat(rect.bottom, is(i == 0 ? 0 : decoration.getVerticalEnd()));
		}
	}

	@Test
	public void testGetItemOffsetsSkipLast() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
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
	}

	@Test
	public void testGetItemOffsetsSkipBoth() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
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
	}

	@Test
	public void testGetItemOffsetsForUnknownAdapterPosition() {
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(1, 2, 3, 4);
		decoration.setSkipFirst(true);
		decoration.setSkipLast(true);
		final Rect rect = new Rect();
		when(mMockRecyclerView.getChildAdapterPosition(mItemView)).thenReturn(RecyclerView.NO_POSITION);
		decoration.getItemOffsets(rect, mItemView, mMockRecyclerView, mMockRecyclerViewState);
		assertThat(rect.isEmpty(), is(true));
		verifyZeroInteractions(mMockRecyclerViewState);
	}
}
