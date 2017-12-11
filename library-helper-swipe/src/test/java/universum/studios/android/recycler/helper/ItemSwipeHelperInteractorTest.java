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

import org.hamcrest.core.Is;
import org.junit.Test;

import java.lang.reflect.Field;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelperInteractorTest extends RobolectricTestCase {
    
	private RecyclerView mMockRecyclerView;

	public ItemSwipeHelperInteractorTest() {
		this.mMockRecyclerView = mock(RecyclerView.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		resetMock(mMockRecyclerView);
	}

	@Test
	public void testSetIsItemViewSwipeEnabled() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.setItemViewSwipeEnabled(false);
		assertThat(interactor.isItemViewSwipeEnabled(), is(false));
		interactor.setItemViewSwipeEnabled(true);
		assertThat(interactor.isItemViewSwipeEnabled(), is(true));
	}

	@Test
	public void testIsItemViewSwipeEnabled() {
		assertThat(new ItemSwipeHelper.Interactor().isItemViewSwipeEnabled(), is(true));
	}

	@Test
	public void testSetGetSwipeThreshold() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.setSwipeThreshold(0.25f);
		assertThat(interactor.getSwipeThreshold(), is(0.25f));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetSwipeThresholdOutOfRange() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.setSwipeThreshold(1.15f);
		assertThat(interactor.getSwipeThreshold(), is(1.0f));
		interactor.setSwipeThreshold(-0.75f);
		assertThat(interactor.getSwipeThreshold(), is(0.0f));
	}

	@Test
	public void testGetSwipeThresholdDefault() {
		assertThat(new ItemSwipeHelper.Interactor().getSwipeThreshold(), is(ItemSwipeHelper.SWIPE_THRESHOLD));
	}

	@Test
	public void testGetSwipeThreshold() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		assertThat(interactor.getSwipeThreshold(mock(RecyclerView.ViewHolder.class)), is(interactor.getSwipeThreshold()));
	}

	@Test
	public void testAddOnSwipeListener() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verify(firstMockListener, times(1)).onSwipeStarted(helper, mockViewHolder);
		verify(secondMockListener, times(1)).onSwipeStarted(helper, mockViewHolder);
	}

	@Test
	public void testRemoveOnSwipeListener() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener, times(1)).onSwipeStarted(helper, mockViewHolder);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testRemoveOnSwipeListenerNotAdded() {
		new ItemSwipeHelper.Interactor().removeOnSwipeListener(mock(ItemSwipeHelper.OnSwipeListener.class));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testNotifySwipeStarted() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		for (int i = 0; i < 10; i++) {
			interactor.notifySwipeStarted(mockViewHolder);
		}
		verify(firstMockListener, times(10)).onSwipeStarted(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(firstMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(10)).onSwipeStarted(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(secondMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifySwipeStartedWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemSwipeHelper.Interactor().notifySwipeStarted(mock(RecyclerView.ViewHolder.class));
	}

	@Test
	public void testNotifySwipeFinished() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		interactor.notifySwipeFinished(mockViewHolder, ItemTouchHelper.START);
		verify(firstMockListener, times(1)).onSwipeFinished(helper, mockViewHolder, ItemTouchHelper.START);
		verify(firstMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(1)).onSwipeFinished(helper, mockViewHolder, ItemTouchHelper.START);
		verify(secondMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeFinished(mockViewHolder, ItemTouchHelper.END);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifySwipeFinishedWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemSwipeHelper.Interactor().notifySwipeFinished(mock(RecyclerView.ViewHolder.class), ItemTouchHelper.START);
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testNotifySwipeCanceled() {
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		for (int i = 0; i < 10; i++) {
			interactor.notifySwipeCanceled(mockViewHolder);
		}
		verify(firstMockListener, times(10)).onSwipeCanceled(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(secondMockListener, times(10)).onSwipeCanceled(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeCanceled(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifySwipeCanceledWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemSwipeHelper.Interactor().notifySwipeCanceled(mock(RecyclerView.ViewHolder.class));
	}

	@Test
	public void testCanAttachAdapter() {
		assertThat(new ItemSwipeHelper.Interactor().canAttachAdapter(mock(RecyclerView.Adapter.class)), is(false));
		assertThat(new ItemSwipeHelper.Interactor().canAttachAdapter(mock(Adapter.class)), is(true));
	}

	@Test
	public void testOnAdapterAttached() {
		final Adapter mockAdapter = mock(Adapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		assertThat(interactor.swipeAdapter, Is.<ItemSwipeHelper.SwipeAdapter>is(mockAdapter));
	}

	@Test
	public void testOnAdapterDetached() {
		final Adapter mockAdapter = mock(Adapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		interactor.swiping = true;
		interactor.onAdapterDetached(mockAdapter);
		assertThat(interactor.swipeAdapter, is(nullValue()));
	}

	@Test
	public void testSetEnabled() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.swiping = true;
		interactor.setEnabled(false);
		assertThat(interactor.isActive(), is(false));
		interactor.swiping = true;
		interactor.setEnabled(true);
		assertThat(interactor.isActive(), is(false));
	}

	@Test
	public void testIsActive() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.swiping = true;
		assertThat(interactor.isActive(), is(true));
	}

	@Test
	public void testIsActiveDefault() {
		assertThat(new ItemSwipeHelper.Interactor().isActive(), is(false));
	}

	@Test
	public void testGetMovementFlags() throws Exception {
		final Adapter mockAdapter = mock(Adapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		final int itemSwipeFlags = ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START | ItemSwipeHelper.END);
		when(mockAdapter.getItemSwipeFlags(0)).thenReturn(itemSwipeFlags);
		final Holder mockViewHolder = createMockHolder(new View(mApplication), 0);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mockViewHolder), is(itemSwipeFlags));
		verify(mockAdapter, times(1)).getItemSwipeFlags(0);
	}

	@Test
	public void testGetMovementFlagsForNotSwipeHolder() {
		final Adapter mockAdapter = mock(Adapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test
	public void testGetMovementFlagsWhenDisabled() {
		final Adapter mockAdapter = mock(Adapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test
	public void testGetMovementFlagsWithoutAttachedAdapter() {
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.setEnabled(false);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
	}

	@Test
	public void testOnSelectedChanged() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(view);
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		assertThat(interactor.isActive(), is(true));
		verify(mockHolder, times(1)).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(1)).onSwipeStarted();
		verify(mockListener, times(1)).onSwipeStarted(helper, mockHolder);
	}

	@Test
	public void testOnSelectedChangedWithoutInteractiveView() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		assertThat(interactor.isActive(), is(true));
		verify(mockHolder, times(1)).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(1)).onSwipeStarted();
		verify(mockListener, times(1)).onSwipeStarted(helper, mockHolder);
	}

	@Test
	public void testOnSelectedChangedForNotSwipeInteraction() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedForNotSwipeHolder() throws Exception {
		final Adapter mockAdapter = mock(Adapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedWhenDisabled() throws Exception {
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedWithoutAttachedAdapter() throws Exception {
		final Holder mockHolder = createMockHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnMove() {
		// Only ensure that invocation of the method does not cause any troubles.
		assertThat(
				new ItemSwipeHelper.Interactor().onMove(
						mMockRecyclerView,
						mock(RecyclerView.ViewHolder.class),
						mock(RecyclerView.ViewHolder.class)
				),
				is(false)
		);
	}

	@Test
	public void testOnSwiped() {
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = mock(Holder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder, times(1)).onSwipeFinished(ItemSwipeHelper.START);
		verify(mockListener, times(1)).onSwipeFinished(helper, mockHolder, ItemSwipeHelper.START);
	}

	@Test
	public void testOnSwipedForNotSwipeHolder() {
		final Adapter mockAdapter = mock(Adapter.class);
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		assertThat(interactor.isActive(), is(true));
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSwipedWhenDisabled() {
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = mock(Holder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSwipedWithoutAttachedAdapter() {
		final Holder mockHolder = mock(Holder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testClearView() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(mMockRecyclerView, view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(view);
		interactor.clearView(mMockRecyclerView, mockHolder);
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder, times(1)).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(1)).onSwipeCanceled();
		verify(mockListener, times(1)).onSwipeCanceled(helper, mockHolder);
	}

	@Test
	public void testClearViewNotInteractive() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(mMockRecyclerView, view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		interactor.clearView(mMockRecyclerView, mockHolder);
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder, times(1)).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(1)).onSwipeCanceled();
		verify(mockListener, times(1)).onSwipeCanceled(helper, mockHolder);
	}

	@Test
	public void testClearViewForHolderWithUnknownPosition() throws Exception {
		final View view = new View(mApplication);
		final Adapter mockAdapter = mock(Adapter.class);
		final Holder mockHolder = createMockHolder(view, RecyclerView.NO_POSITION);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		interactor.clearView(mMockRecyclerView, mockHolder);
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder, times(1)).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(0)).onSwipeCanceled();
		verify(mockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
	}

	@Test
	public void testClearViewForNotSwipeHolder() throws Exception {
		final Adapter mockAdapter = mock(Adapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testClearViewWhenDisabled() throws Exception {
		final Adapter mockAdapter = mock(Adapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testClearViewWithoutAttachedAdapter() throws Exception {
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(mApplication), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	private static Holder createMockHolder(View itemView, int position) throws Exception {
		final Holder mockHolder = mock(Holder.class);
		final Field itemViewField = Holder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static Holder createMockHolder(RecyclerView recyclerView, View itemView, int position) throws Exception {
		final Holder mockHolder = mock(Holder.class);
		final Field itemViewField = Holder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		final Field recyclerViewField = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
		recyclerViewField.setAccessible(true);
		recyclerViewField.set(mockHolder, recyclerView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static RecyclerView.ViewHolder createMockViewHolder(View itemView, int position) throws Exception {
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final Field itemViewField = RecyclerView.ViewHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static abstract class Adapter extends RecyclerView.Adapter implements ItemSwipeHelper.SwipeAdapter {}

	private static abstract class Holder extends RecyclerView.ViewHolder implements ItemSwipeHelper.SwipeViewHolder {

		Holder(View itemView) {
			super(itemView);
		}
	}
}
