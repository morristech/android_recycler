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
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

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
public final class RecyclerViewItemDecorationTest extends InstrumentedTestCase {

	// Arrange:
	// Act:
	// Assert:
    
	private RecyclerView mMockRecyclerView;
	private RecyclerView.State mMockRecyclerViewState;

	public RecyclerViewItemDecorationTest() {
		this.mMockRecyclerView = mock(RecyclerView.class);
		this.mMockRecyclerViewState = mock(RecyclerView.State.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		resetMock(mMockRecyclerView);
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(context));
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(10);
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleRes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final TestDecoration decoration = new TestDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipNone"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipFirst() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final TestDecoration decoration = new TestDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipFirst"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipLast() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final TestDecoration decoration = new TestDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipLast"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipBoth() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		final TestDecoration decoration = new TestDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipBoth"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	private static final class TestDecoration extends RecyclerViewItemDecoration {

		TestDecoration() {
			super();
		}

		TestDecoration(
				@Nullable final Context context,
				@Nullable final AttributeSet attrs,
				@AttrRes final int defStyleAttr,
				@StyleRes final int defStyleRes
		) {
			super(context, attrs, defStyleAttr, defStyleRes);
		}
	}
}