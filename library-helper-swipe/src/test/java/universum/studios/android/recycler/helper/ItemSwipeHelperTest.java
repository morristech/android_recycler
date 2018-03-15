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
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelperTest extends RobolectricTestCase {

	private RecyclerView mMockRecyclerView;

	public ItemSwipeHelperTest() {
		this.mMockRecyclerView = mock(RecyclerView.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		resetMock(mMockRecyclerView);
	}

	@Test
	public void testInteractionConstant() {
		assertThat(ItemSwipeHelper.INTERACTION, is(ItemTouchHelper.ACTION_STATE_SWIPE));
	}

	@Test
	public void testSwipeTresholdConstant() {
		assertThat(ItemSwipeHelper.SWIPE_THRESHOLD, is(0.5f));
	}

	@Test
	public void testMakeSwipeFlags() {
		assertThat(
				ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START),
				is(ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.START))
		);
		assertThat(
				ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.LEFT | ItemSwipeHelper.RIGHT),
				is(ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT))
		);
	}

	@Test
	public void testGetInteractor() {
		assertThat(new ItemSwipeHelper().getInteractor(), instanceOf(ItemSwipeHelper.Interactor.class));
	}

	@Test
	public void testSetGetRestoreHolderAnimationDuration() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(500L);
		assertThat(helper.getRestoreHolderAnimationDuration(), is(500L));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetRestoreHolderAnimationDurationOutOfRange() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(-100L);
		assertThat(helper.getRestoreHolderAnimationDuration(), is(0L));
	}

	@Test
	public void testGetRestoreHolderAnimationDurationDefault() {
		assertThat(new ItemSwipeHelper().getRestoreHolderAnimationDuration(), is(ItemSwipeHelper.RESTORE_HOLDER_ANIMATION_DURATION));
	}

	@Test
	public void testSetGetRestoreHolderAnimationInterpolator() {
		final Interpolator mockInterpolator = mock(Interpolator.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationInterpolator(mockInterpolator);
		assertThat(helper.getRestoreHolderAnimationInterpolator(), is(mockInterpolator));
	}

	@Test
	public void testGetRestoreHolderAnimationInterpolatorDefault() {
		assertThat(new ItemSwipeHelper().getRestoreHolderAnimationInterpolator(), is(not(nullValue())));
	}

	@Test
	public void testRestoreHolderForHorizontalSwipe() throws Throwable {
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		itemView.setTranslationX(1f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(0L);
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test
	public void testRestoreHolderForHorizontalSwipeWithoutTranslation() throws Throwable {
		final View itemView = new View(mApplication);
		final RecyclerView.Adapter adapter = new Adapter();
		final RecyclerView.AdapterDataObserver mockAdapterObserver = mock(RecyclerView.AdapterDataObserver.class);
		adapter.registerAdapterDataObserver(mockAdapterObserver);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		itemView.setTranslationX(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.getInteractor().attachAdapter(adapter);
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAdapterObserver).onItemRangeChanged(0, 1, null);
	}

	@Test
	public void testRestoreHolderForVerticalSwipe() throws Throwable {
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		itemView.setTranslationY(1f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.setRestoreHolderAnimationDuration(0L);
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.DOWN), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test
	public void testRestoreHolderForVerticalSwipeWithoutTranslation() throws Throwable {
		final View itemView = new View(mApplication);
		final RecyclerView.Adapter adapter = new Adapter();
		final RecyclerView.AdapterDataObserver mockAdapterObserver = mock(RecyclerView.AdapterDataObserver.class);
		adapter.registerAdapterDataObserver(mockAdapterObserver);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		itemView.setTranslationY(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		helper.getInteractor().attachAdapter(adapter);
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.DOWN), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAdapterObserver).onItemRangeChanged(0, 1, null);
	}

	@Test
	public void testRestoreHolderWithAnimationCallback() throws Throwable {
		final Runnable mockAnimationCallback = mock(Runnable.class);
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		itemView.setTranslationX(0f);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		assertThat(helper.restoreHolder(mockViewHolder, ItemSwipeHelper.START, mockAnimationCallback), is(true));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockAnimationCallback).run();
	}

	@Test
	public void testRestoreHolderWithUnknownDirection() throws Throwable {
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(itemView);
		assertThat(new ItemSwipeHelper().restoreHolder(mockViewHolder, 100), is(false));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testRestoreHolderWithUnknownPosition() throws Exception {
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(null, itemView, RecyclerView.NO_POSITION);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		assertThat(new ItemSwipeHelper().restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(false));
		verify(mockViewHolder, times(0)).getInteractiveView(anyInt());
	}

	@Test
	public void testRestoreHolderWithoutInteractiveView() throws Exception {
		final View itemView = new View(mApplication);
		final Holder mockViewHolder = createMockHolder(mMockRecyclerView, itemView, 0);
		when(mockViewHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		assertThat(new ItemSwipeHelper().restoreHolder(mockViewHolder, ItemSwipeHelper.START), is(false));
		verify(mockViewHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
	}

	@Test
	public void testRestoreHolderNotTypeOfSwipeHolder() throws Exception {
		assertThat(new ItemSwipeHelper().restoreHolder(new RecyclerView.ViewHolder(new View(mApplication)) {}, ItemSwipeHelper.START), is(false));
	}

	private static Holder createMockHolder(RecyclerView ownerRecyclerView, View itemView, int position) throws Exception {
		final Holder mockHolder = mock(Holder.class);
		final Field itemViewField = Holder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		final Field ownerRecyclerViewField = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
		ownerRecyclerViewField.setAccessible(true);
		ownerRecyclerViewField.set(mockHolder, ownerRecyclerView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static class Adapter extends RecyclerView.Adapter implements ItemSwipeHelper.SwipeAdapter {

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return null;
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		}

		@Override
		public int getItemCount() {
			return 0;
		}

		@Override
		public int getItemSwipeFlags(int position) {
			return 0;
		}
	}

	private static abstract class Holder extends RecyclerView.ViewHolder implements ItemSwipeHelper.SwipeViewHolder {

		Holder(View itemView) {
			super(itemView);
		}
	}
}
