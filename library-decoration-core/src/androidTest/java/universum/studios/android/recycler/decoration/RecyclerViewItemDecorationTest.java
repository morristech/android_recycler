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

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class RecyclerViewItemDecorationTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "RecyclerViewItemDecorationTest";

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
		when(mMockRecyclerView.getLayoutManager()).thenReturn(new LinearLayoutManager(mContext));
		resetMock(mMockRecyclerViewState);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(10);
	}

	@Test
	public void testInstantiationWithContext() {
		final Decoration decoration = new Decoration(mContext);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSet() {
		final Decoration decoration = new Decoration(mContext, null);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttr() {
		final Decoration decoration = new Decoration(mContext, null, 0);
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleRes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Decoration decoration = new Decoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipNone"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipFirst() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Decoration decoration = new Decoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipFirst"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipLast() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Decoration decoration = new Decoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipLast"
		));
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResWithSkipBoth() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final Decoration decoration = new Decoration(mContext, null, 0, TestResources.resourceIdentifier(
				mContext,
				TestResources.STYLE,
				"Test.ItemDecoration.SkipBoth"
		));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test
	public void testSetSkipFirst() {
		final RecyclerViewItemDecoration decoration = new Decoration();
		decoration.setSkipFirst(true);
		assertThat(decoration.skipsFirst(), is(true));
		decoration.setSkipFirst(false);
		assertThat(decoration.skipsFirst(), is(false));
	}

	@Test
	public void testSkipsFirstDefault() {
		assertThat(new Decoration().skipsFirst(), is(false));
	}

	@Test
	public void testSetSkipLast() {
		final RecyclerViewItemDecoration decoration = new Decoration();
		decoration.setSkipLast(true);
		assertThat(decoration.skipsLast(), is(true));
		decoration.setSkipLast(false);
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test
	public void testSkipsLastDefault() {
		assertThat(new Decoration().skipsLast(), is(false));
	}

	@Test
	public void testShouldDecorate() {
		assertThat(new Decoration().shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(true));
		verify(mMockRecyclerView, times(1)).getLayoutManager();
		verify(mMockRecyclerViewState, times(1)).getItemCount();
	}

	@Test
	public void testShouldDecorateForRecyclerViewWithoutLayoutManager() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(null);
		assertThat(new Decoration().shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
		verify(mMockRecyclerView, times(1)).getLayoutManager();
		verify(mMockRecyclerViewState, times(0)).getItemCount();
	}

	@Test
	public void testShouldDecorateForRecyclerViewStateWithoutItems() {
		when(mMockRecyclerViewState.getItemCount()).thenReturn(0);
		assertThat(new Decoration().shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
		verify(mMockRecyclerView, times(1)).getLayoutManager();
		verify(mMockRecyclerViewState, times(1)).getItemCount();
	}

	@Test
	public void testShouldDecorateForRecyclerViewWithoutLayoutManagerAndStateWithoutItems() {
		when(mMockRecyclerView.getLayoutManager()).thenReturn(null);
		when(mMockRecyclerViewState.getItemCount()).thenReturn(0);
		assertThat(new Decoration().shouldDecorate(mMockRecyclerView, mMockRecyclerViewState), is(false));
		verify(mMockRecyclerView, times(1)).getLayoutManager();
		verify(mMockRecyclerViewState, times(0)).getItemCount();
	}

	private static final class Decoration extends RecyclerViewItemDecoration {

		Decoration() {
			super();
		}

		Decoration(@Nullable Context context) {
			super(context);
		}

		Decoration(@Nullable Context context, @Nullable AttributeSet attrs) {
			super(context, attrs);
		}

		Decoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		Decoration(@Nullable Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
			super(context, attrs, defStyleAttr, defStyleRes);
		}
	}
}
