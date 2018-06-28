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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class RecyclerViewItemHelperTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final TestHelper helper = new TestHelper(mockInteractor);
		// Act + Assert:
		assertThat(helper.getInteractor(), is(not(nullValue())));
		verify(mockInteractor).onAttachedToHelper(helper);
	}

	@Test public void testAttachToNullRecyclerView() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final TestHelper helper = new TestHelper(mockInteractor);
		// Act:
		helper.attachToRecyclerView(null);
		// Assert:
		verify(mockInteractor, times(0)).canAttachAdapter(any(RecyclerView.Adapter.class));
		verify(mockInteractor, times(0)).onAdapterAttached(any(RecyclerView.Adapter.class));
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	private static class TestHelper extends RecyclerViewItemHelper<TestInteractor> {

		TestHelper(@NonNull final TestInteractor interactor) {
			super(interactor);
		}
	}

	private static class TestInteractor extends RecyclerViewItemHelper.ItemInteractor {

		@Override protected boolean canAttachAdapter(@NonNull final RecyclerView.Adapter adapter) {
			return false;
		}

		@Override public int getMovementFlags(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
			return 0;
		}

		@Override public boolean onMove(
				@NonNull final RecyclerView recyclerView,
				@NonNull final RecyclerView.ViewHolder viewHolder,
				@NonNull final RecyclerView.ViewHolder target
		) {
			return false;
		}

		@Override public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int direction) {}

		@Override public boolean isActive() {
			return false;
		}
	}
}