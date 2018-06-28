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
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class RecyclerViewItemDecorationTest extends RobolectricTestCase {

	@Test public void testEmptyPrecondition() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final RecyclerView.State mockRecyclerViewState = mock(RecyclerView.State.class);
		when(mockRecyclerViewState.getItemCount()).thenReturn(10);
		// Act + Assert:
		assertThat(RecyclerViewItemDecoration.Precondition.EMPTY, is(notNullValue()));
		assertThat(RecyclerViewItemDecoration.Precondition.EMPTY.check(new View(application), mockRecyclerView, mockRecyclerViewState), is(true));
	}

	@Test public void testEmptyInstantiation() {
		// Act:
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
		assertThat(decoration.getPrecondition(), is(TestDecoration.Precondition.EMPTY));
	}

	@Test public void testInstantiationWithContext() {
		// Act:
		final RecyclerViewItemDecoration decoration = new TestDecoration(application);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSet() {
		// Act:
		final RecyclerViewItemDecoration decoration = new TestDecoration(application, null);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		// Act:
		final RecyclerViewItemDecoration decoration = new TestDecoration(application, null, 0);
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testSkipFirst() {
		// Arrange:
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		decoration.setSkipFirst(true);
		assertThat(decoration.skipsFirst(), is(true));
		decoration.setSkipFirst(false);
		assertThat(decoration.skipsFirst(), is(false));
	}

	@Test public void testSkipLast() {
		// Arrange:
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		decoration.setSkipLast(true);
		assertThat(decoration.skipsLast(), is(true));
		decoration.setSkipLast(false);
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testPrecondition() {
		// Arrange:
		final RecyclerViewItemDecoration.Precondition mockPrecondition = mock(RecyclerViewItemDecoration.Precondition.class);
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		decoration.setPrecondition(mockPrecondition);
		assertThat(decoration.getPrecondition(), is(mockPrecondition));
		verifyZeroInteractions(mockPrecondition);
	}

	@Test public void testShouldDecorate() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final RecyclerView.State mockRecyclerViewState = mock(RecyclerView.State.class);
		when(mockRecyclerViewState.getItemCount()).thenReturn(10);
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(true));
		verify(mockRecyclerView).getLayoutManager();
		verify(mockRecyclerViewState).getItemCount();
	}

	@Test public void testShouldDecorateForRecyclerViewWithoutLayoutManager() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		when(mockRecyclerView.getLayoutManager()).thenReturn(null);
		final RecyclerView.State mockRecyclerViewState = mock(RecyclerView.State.class);
		when(mockRecyclerViewState.getItemCount()).thenReturn(10);
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
		verify(mockRecyclerView, times(1)).getLayoutManager();
		verify(mockRecyclerViewState, times(0)).getItemCount();
	}

	@Test public void testShouldDecorateForRecyclerViewStateWithoutItems() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		when(mockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(application));
		final RecyclerView.State mockRecyclerViewState = mock(RecyclerView.State.class);
		when(mockRecyclerViewState.getItemCount()).thenReturn(0);
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
		verify(mockRecyclerView, times(1)).getLayoutManager();
		verify(mockRecyclerViewState, times(1)).getItemCount();
	}

	@Test public void testShouldDecorateForRecyclerViewWithoutLayoutManagerAndStateWithoutItems() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		when(mockRecyclerView.getLayoutManager()).thenReturn(null);
		final RecyclerView.State mockRecyclerViewState = mock(RecyclerView.State.class);
		when(mockRecyclerViewState.getItemCount()).thenReturn(0);
		final RecyclerViewItemDecoration decoration = new TestDecoration();
		// Act + Assert:
		assertThat(decoration.shouldDecorate(mockRecyclerView, mockRecyclerViewState), is(false));
		verify(mockRecyclerView).getLayoutManager();
		verify(mockRecyclerViewState, times(0)).getItemCount();
	}

	private static final class TestDecoration extends RecyclerViewItemDecoration {

		TestDecoration() {
			super();
		}

		TestDecoration(@Nullable final Context context) {
			super(context);
		}

		TestDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs) {
			super(context, attrs);
		}

		TestDecoration(@Nullable final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}
	}
}