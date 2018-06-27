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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemSpaceDecorationTest extends InstrumentedTestCase {

	// Arrange:
	// Act:
	// Assert:
    
	private Canvas mMockCanvas = mock(Canvas.class);
	private RecyclerView mMockRecyclerView;
	private RecyclerView.State mMockRecyclerViewState;

	public ItemSpaceDecorationTest() {
		this.mMockCanvas = mock(Canvas.class);
		this.mMockRecyclerView = mock(RecyclerView.class);
		this.mMockRecyclerViewState = mock(RecyclerView.State.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		resetMock(mMockCanvas);
		resetMock(mMockRecyclerView);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(context));
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(10);
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleRes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space"
		));
		assertThat(decoration.getHorizontalStart(), is(8));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(4));
		assertThat(decoration.getVerticalEnd(), is(2));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipNone() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipNone"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipFirst() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipFirst"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipLast() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipLast"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipBoth() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipBoth"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResEmpty() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.Empty"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}
}