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
import android.support.test.annotation.UiThreadTest;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class RecyclerViewItemHelperTest extends InstrumentedTestCase {

	@Test
	@UiThreadTest
	public void testAttachToRecyclerView() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView recyclerView = new RecyclerView(context);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		recyclerView.setAdapter(mockAdapter);
		when(mockInteractor.canAttachAdapter(mockAdapter)).thenReturn(true);
		new Helper(mockInteractor).attachToRecyclerView(recyclerView);
		verify(mockInteractor, times(1)).canAttachAdapter(mockAdapter);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@UiThreadTest
	@Test(expected = IllegalArgumentException.class)
	public void testAttachToRecyclerViewWithUnsupportedAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView recyclerView = new RecyclerView(context);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		recyclerView.setAdapter(mockAdapter);
		when(mockInteractor.canAttachAdapter(mockAdapter)).thenReturn(false);
		new Helper(mockInteractor).attachToRecyclerView(recyclerView);
	}

	@Test
	@UiThreadTest
	public void testAttachToRecyclerViewWithoutAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView recyclerView = new RecyclerView(context);
		new Helper(mockInteractor).attachToRecyclerView(recyclerView);
		verify(mockInteractor, times(0)).canAttachAdapter(any(RecyclerView.Adapter.class));
		verify(mockInteractor, times(0)).onAdapterAttached(any(RecyclerView.Adapter.class));
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	private static class Helper extends RecyclerViewItemHelper<Interactor> {

		Helper(@NonNull Interactor interactor) {
			super(interactor);
		}
	}

	private static class Interactor extends RecyclerViewItemHelper.ItemInteractor {

		@Override
		protected boolean canAttachAdapter(@NonNull RecyclerView.Adapter adapter) {
			return false;
		}

		@Override
		public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			return 0;
		}

		@Override
		public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
			return false;
		}

		@Override
		public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		}

		@Override
		public boolean isActive() {
			return false;
		}
	}
}