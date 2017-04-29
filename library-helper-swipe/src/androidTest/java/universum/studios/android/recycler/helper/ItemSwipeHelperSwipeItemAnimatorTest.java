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
package universum.studios.android.recycler.helper;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ItemSwipeHelperSwipeItemAnimatorTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ItemSwipeHelperSwipeItemAnimatorTest";

	@Rule public final UiThreadTestRule UI_RULE = new UiThreadTestRule();

	@Test
	@UiThreadTest
	public void testAnimateChange() {
		final RecyclerView.ViewHolder oldMockViewHolder = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder newMockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 0), is(false));
	}

	@Test
	@UiThreadTest
	public void testAnimateChangeForVerticalTranslation() throws Exception {
		final RecyclerView.ViewHolder oldMockViewHolder = createMockViewHolder(new View(mContext));
		final RecyclerView.ViewHolder newMockViewHolder = createMockViewHolder(new View(mContext));
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(
				animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 1),
				is(new DefaultItemAnimator().animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 1))
		);
	}

	@Test
	@UiThreadTest
	public void testAnimateChangeForHorizontalTranslation() throws Exception {
		final RecyclerView.ViewHolder oldMockViewHolder = createMockViewHolder(new View(mContext));
		final RecyclerView.ViewHolder newMockViewHolder = createMockViewHolder(new View(mContext));
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(
				animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 1, 0),
				is(new DefaultItemAnimator().animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 1, 0))
		);
	}

	@Test
	@UiThreadTest
	public void testAnimateChangeForSameHolders() throws Exception {
		final RecyclerView.ViewHolder mockViewHolder = createMockViewHolder(new View(mContext));
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(animator.animateChange(mockViewHolder, mockViewHolder, 0, 0, 0, 0), is(false));
	}

	private static RecyclerView.ViewHolder createMockViewHolder(View itemView) throws Exception {
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final Field itemViewField = RecyclerView.ViewHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		return mockHolder;
	}
}
