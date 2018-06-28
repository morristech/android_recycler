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

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Test;

import java.lang.reflect.Field;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class RecyclerViewItemHelperInteractorTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final TestInteractor interactor = new TestInteractor();
		// Assert:
		assertThat(interactor.isEnabled(), is(true));
		assertThat(interactor.shouldHandleInteraction(), is(false));
	}

	@Test public void testAttachToHelper() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final TestHelper mockHelper = mock(TestHelper.class);
		// Act:
		mockInteractor.attachToHelper(mockHelper);
		// Assert:
		verify(mockInteractor).onAttachedToHelper(mockHelper);
	}

	@Test public void testAttachAdapter() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		// Act:
		mockInteractor.attachAdapter(mockAdapter);
		// Assert:
		verify(mockInteractor).onAdapterAttached(mockAdapter);
		verifyNoMoreInteractions(mockInteractor);
	}

	@Test public void testAttachAdapterWithPreviousAttached() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final RecyclerView.Adapter mockAdapterFirst = mock(RecyclerView.Adapter.class);
		final RecyclerView.Adapter mockAdapterSecond = mock(RecyclerView.Adapter.class);
		// Act + Assert:
		mockInteractor.attachAdapter(mockAdapterFirst);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapterFirst);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
		mockInteractor.attachAdapter(mockAdapterSecond);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapterFirst);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapterSecond);
		verify(mockInteractor, times(1)).onAdapterDetached(mockAdapterFirst);
		mockInteractor.attachAdapter(null);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapterFirst);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapterSecond);
		verify(mockInteractor, times(1)).onAdapterDetached(mockAdapterFirst);
		verify(mockInteractor, times(1)).onAdapterDetached(mockAdapterSecond);
		verifyNoMoreInteractions(mockInteractor);
	}

	@Test public void testAttachSameAdapter() {
		// Arrange:
		final TestInteractor mockInteractor = mock(TestInteractor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		mockInteractor.attachAdapter(mockAdapter);
		// Act:
		mockInteractor.attachAdapter(mockAdapter);
		// Assert:
		verify(mockInteractor).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
		verifyNoMoreInteractions(mockInteractor);
	}

	@Test public void testOnAttachedToHelper() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		// Act:
		// Only ensure that invocation of this method does not cause any troubles.
		interactor.onAttachedToHelper(mock(TestHelper.class));
	}

	@Test public void testOnAdapterAttached() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		// Act:
		// Only ensure that invocation of this method does not cause any troubles.
		interactor.onAdapterAttached(mock(RecyclerView.Adapter.class));
	}

	@Test public void testOnAdapterDetached() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		// Act:
		// Only ensure that invocation of this method does not cause any troubles.
		interactor.onAdapterDetached(mock(RecyclerView.Adapter.class));
	}

	@Test public void testIsEnabled() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		// Act + Assert:
		interactor.setEnabled(false);
		assertThat(interactor.isEnabled(), is(false));
		interactor.setEnabled(true);
		assertThat(interactor.isEnabled(), is(true));
	}

	@Test public void testShouldHandleInteraction() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		interactor.attachAdapter(mock(RecyclerView.Adapter.class));
		// Act + Assert:
		assertThat(interactor.shouldHandleInteraction(), is(true));
		interactor.setEnabled(false);
		assertThat(interactor.shouldHandleInteraction(), is(false));
		interactor.setEnabled(true);
		interactor.attachAdapter(null);
		assertThat(interactor.shouldHandleInteraction(), is(false));
	}

	@Test public void testOnChildDraw() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application));
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		final View view = new View(application);
		when(mockHolder.getInteractiveView(TestHelper.ACTION_STATE_SWIPE)).thenReturn(view);
		// Act:
		interactor.onChildDraw(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				TestHelper.ACTION_STATE_SWIPE,
				true
		);
		// Assert:
		verify(mockHolder).getInteractiveView(TestHelper.ACTION_STATE_SWIPE);
		verify(mockHolder).onDraw(canvas, 0, 0, TestHelper.ACTION_STATE_SWIPE, true);
		verifyNoMoreInteractions(mockHolder);
	}

	@Test public void testOnChildDrawWithNotInteractiveViewHolder() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		// Act:
		interactor.onChildDraw(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(application)) {},
				0, 0,
				TestHelper.ACTION_STATE_SWIPE,
				true
		);
	}

	@Test public void testOnChildDrawWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application));
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		when(mockHolder.getInteractiveView(TestHelper.ACTION_STATE_SWIPE)).thenReturn(null);
		// Act:
		interactor.onChildDraw(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				TestHelper.ACTION_STATE_SWIPE,
				true
		);
		// Assert:
		verify(mockHolder).getInteractiveView(TestHelper.ACTION_STATE_SWIPE);
		verify(mockHolder).onDraw(canvas, 0, 0, TestHelper.ACTION_STATE_SWIPE, true);
		verifyNoMoreInteractions(mockHolder);
	}

	@Test public void testOnChildDrawOver() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application));
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		final View view = new View(application);
		when(mockHolder.getInteractiveView(TestHelper.ACTION_STATE_DRAG)).thenReturn(view);
		// Act:
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				TestHelper.ACTION_STATE_DRAG,
				true
		);
		// Assert:
		verify(mockHolder).getInteractiveView(TestHelper.ACTION_STATE_DRAG);
		verify(mockHolder).onDrawOver(canvas, 0, 0, TestHelper.ACTION_STATE_DRAG, true);
	}

	@Test public void testOnChildDrawOverWithNotInteractiveViewHolder() {
		// Arrange:
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		// Act:
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(application)) {},
				0, 0,
				TestHelper.ACTION_STATE_DRAG,
				true
		);
	}

	@Test public void testOnChildDrawOverWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application));
		final TestInteractor interactor = new TestInteractor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		when(mockHolder.getInteractiveView(TestHelper.ACTION_STATE_DRAG)).thenReturn(null);
		// Act:
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				TestHelper.ACTION_STATE_DRAG,
				true
		);
		// Assert:
		verify(mockHolder).getInteractiveView(TestHelper.ACTION_STATE_DRAG);
		verify(mockHolder).onDrawOver(canvas, 0, 0, TestHelper.ACTION_STATE_DRAG, true);
		verifyNoMoreInteractions(mockHolder);
	}

	private static TestHolder createMockHolder(final View itemView) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		return mockHolder;
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

	private static abstract class TestHolder extends RecyclerView.ViewHolder implements RecyclerViewItemHelper.InteractiveViewHolder {

		TestHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}