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
package universum.studios.android.recycler.helper;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Test;

import java.lang.reflect.Field;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelperSwipeItemAnimatorTest extends RobolectricTestCase {
    
	@Test
	public void testAnimateChange() {
		final RecyclerView.ViewHolder oldMockViewHolder = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder newMockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 0), is(false));
	}

	@Test
	public void testAnimateChangeForVerticalTranslation() throws Exception {
		final RecyclerView.ViewHolder oldMockViewHolder = createMockViewHolder(new View(application));
		final RecyclerView.ViewHolder newMockViewHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(
				animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 1),
				is(new DefaultItemAnimator().animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 0, 1))
		);
	}

	@Test
	public void testAnimateChangeForHorizontalTranslation() throws Exception {
		final RecyclerView.ViewHolder oldMockViewHolder = createMockViewHolder(new View(application));
		final RecyclerView.ViewHolder newMockViewHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.SwipeItemAnimator animator = new ItemSwipeHelper.SwipeItemAnimator();
		assertThat(
				animator.animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 1, 0),
				is(new DefaultItemAnimator().animateChange(oldMockViewHolder, newMockViewHolder, 0, 0, 1, 0))
		);
	}

	@Test
	public void testAnimateChangeForSameHolders() throws Exception {
		final RecyclerView.ViewHolder mockViewHolder = createMockViewHolder(new View(application));
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