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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import org.junit.Test;

import java.lang.reflect.Field;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelperTest extends RobolectricTestCase {

	@Test public void testConstants() {
		// Assert:
		assertThat(ItemSwipeHelper.INTERACTION, is(ItemTouchHelper.ACTION_STATE_SWIPE));
		assertThat(ItemSwipeHelper.SWIPE_THRESHOLD, is(0.5f));
	}

	@Test public void testMakeSwipeFlags() {
		// Act + Assert:
		assertThat(
				ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START),
				is(ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.START))
		);
		assertThat(
				ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.LEFT | ItemSwipeHelper.RIGHT),
				is(ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT))
		);
	}

	@Test public void testInstantiation() {
		// Act:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Assert:
		assertThat(helper.getInteractor(), instanceOf(ItemSwipeHelper.Interactor.class));
		assertThat(helper.getRestoreHolderAnimationDuration(), is(ItemSwipeHelper.RESTORE_HOLDER_ANIMATION_DURATION));
		assertThat(helper.getRestoreHolderAnimationInterpolator(), is(notNullValue()));
	}

	@Test public void testRestoreHolderAnimationDuration() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		helper.setRestoreHolderAnimationDuration(500L);
		assertThat(helper.getRestoreHolderAnimationDuration(), is(500L));
	}

	@Test public void testSetRestoreHolderAnimationDurationOutOfRange() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act:
		helper.setRestoreHolderAnimationDuration(-100L);
		// Assert:
		assertThat(helper.getRestoreHolderAnimationDuration(), is(0L));
	}

	@Test public void testRestoreHolderAnimationInterpolator() {
		// Arrange:
		final Interpolator mockInterpolator = mock(Interpolator.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		helper.setRestoreHolderAnimationInterpolator(mockInterpolator);
		assertThat(helper.getRestoreHolderAnimationInterpolator(), is(mockInterpolator));
	}

	@Test public void testRestoreHolderForHorizontalSwipe() throws Throwable {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		itemView.setTranslationX(1f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(0L);
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test public void testRestoreHolderForHorizontalSwipeWithoutTranslation() throws Throwable {
		// Arrange:
		final RecyclerView.Adapter adapter = new TestAdapter();
		final RecyclerView.AdapterDataObserver mockAdapterObserver = mock(RecyclerView.AdapterDataObserver.class);
		adapter.registerAdapterDataObserver(mockAdapterObserver);
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		itemView.setTranslationX(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.getInteractor().attachAdapter(adapter);
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAdapterObserver).onItemRangeChanged(0, 1, null);
	}

	@Test public void testRestoreHolderForVerticalSwipe() throws Throwable {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		itemView.setTranslationY(1f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(0L);
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.DOWN), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test public void testRestoreHolderForVerticalSwipeWithoutTranslation() throws Throwable {
		// Arrange:
		final RecyclerView.Adapter adapter = new TestAdapter();
		final RecyclerView.AdapterDataObserver mockAdapterObserver = mock(RecyclerView.AdapterDataObserver.class);
		adapter.registerAdapterDataObserver(mockAdapterObserver);
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		itemView.setTranslationY(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.getInteractor().attachAdapter(adapter);
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.DOWN), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAdapterObserver).onItemRangeChanged(0, 1, null);
	}

	@Test public void testRestoreHolderWithAnimationCallback() throws Throwable {
		// Arrange:
		final Runnable mockAnimationCallback = mock(Runnable.class);
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		itemView.setTranslationX(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START, mockAnimationCallback), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAnimationCallback).run();
	}

	@Test public void testRestoreHolderWithUnknownDirection() throws Throwable {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, 100), is(false));
	}

	@Test public void testRestoreHolderWithUnknownPosition() throws Exception {
		// Arrange:
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(null, itemView, RecyclerView.NO_POSITION);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(false));
		verify(mockViewHolder, times(0)).getInteractiveView(anyInt());
	}

	@Test public void testRestoreHolderWithoutInteractiveView() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View itemView = new View(application);
		final TestHolder mockViewHolder = createMockHolder(mockRecyclerView, itemView, 0);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(false));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test public void testRestoreHolderNotTypeOfSwipeHolder() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		// Act + Assert:
		assertThat(helper.restoreHolder(new RecyclerView.ViewHolder(new View(application)) {}, ItemSwipeHelper.START), is(false));
	}

	private static TestHolder createMockHolder(RecyclerView ownerRecyclerView, View itemView, int position) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		final Field ownerRecyclerViewField = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
		ownerRecyclerViewField.setAccessible(true);
		ownerRecyclerViewField.set(mockHolder, ownerRecyclerView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static class TestAdapter extends RecyclerView.Adapter implements ItemSwipeHelper.SwipeAdapter {

		@Override @NonNull public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			throw new UnsupportedOperationException();
		}

		@Override public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {}

		@Override public int getItemCount() {
			return 0;
		}

		@Override public int getItemSwipeFlags(final int position) {
			return 0;
		}
	}

	private static abstract class TestHolder extends RecyclerView.ViewHolder implements ItemSwipeHelper.SwipeViewHolder {

		TestHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}