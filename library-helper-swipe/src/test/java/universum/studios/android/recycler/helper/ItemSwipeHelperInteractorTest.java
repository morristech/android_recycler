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

/**
 * @author Martin Albedinsky
 */
public final class ItemSwipeHelperInteractorTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Act:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.isItemViewSwipeEnabled(), is(true));
		assertThat(interactor.getSwipeThreshold(), is(ItemSwipeHelper.SWIPE_THRESHOLD));
	}

	@Test public void testIsItemViewSwipeEnabled() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act + Assert:
		interactor.setItemViewSwipeEnabled(false);
		assertThat(interactor.isItemViewSwipeEnabled(), is(false));
		interactor.setItemViewSwipeEnabled(true);
		assertThat(interactor.isItemViewSwipeEnabled(), is(true));
	}

	@Test public void testSwipeThreshold() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act + Assert:
		interactor.setSwipeThreshold(0.25f);
		assertThat(interactor.getSwipeThreshold(), is(0.25f));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testSetSwipeThresholdOutOfRange() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act + Assert:
		interactor.setSwipeThreshold(1.15f);
		assertThat(interactor.getSwipeThreshold(), is(1.0f));
		interactor.setSwipeThreshold(-0.75f);
		assertThat(interactor.getSwipeThreshold(), is(0.0f));
	}

	@Test public void testGetSwipeThreshold() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act + Assert:
		assertThat(interactor.getSwipeThreshold(mock(RecyclerView.ViewHolder.class)), is(interactor.getSwipeThreshold()));
	}

	@Test public void testAddOnSwipeListener() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		// Act:
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		// Assert:
		interactor.notifySwipeStarted(mockViewHolder);
		verify(firstMockListener).onSwipeStarted(helper, mockViewHolder);
		verify(secondMockListener).onSwipeStarted(helper, mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testRemoveOnSwipeListener() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		// Act:
		interactor.removeOnSwipeListener(firstMockListener);
		// Assert:
		interactor.notifySwipeStarted(mockViewHolder);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener).onSwipeStarted(helper, mockViewHolder);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test public void testRemoveOnSwipeListenerNotAdded() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act:
		interactor.removeOnSwipeListener(mock(ItemSwipeHelper.OnSwipeListener.class));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testNotifySwipeStarted() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		// Act:
		for (int i = 0; i < 10; i++) {
			interactor.notifySwipeStarted(mockViewHolder);
		}
		// Assert:
		verify(firstMockListener, times(10)).onSwipeStarted(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(firstMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(10)).onSwipeStarted(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(secondMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeStarted(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifySwipeStartedWithoutRegisteredListeners() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifySwipeStarted(mock(RecyclerView.ViewHolder.class));
	}

	@Test public void testNotifySwipeFinished() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		// Act:
		interactor.notifySwipeFinished(mockViewHolder, ItemTouchHelper.START);
		// Assert:
		verify(firstMockListener).onSwipeFinished(helper, mockViewHolder, ItemTouchHelper.START);
		verify(firstMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener).onSwipeFinished(helper, mockViewHolder, ItemTouchHelper.START);
		verify(secondMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeFinished(mockViewHolder, ItemTouchHelper.END);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifySwipeFinishedWithoutRegisteredListeners() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifySwipeFinished(mock(RecyclerView.ViewHolder.class), ItemTouchHelper.START);
	}

	@SuppressWarnings("ResourceType")
	@Test public void testNotifySwipeCanceled() {
		// Arrange:
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener firstMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.OnSwipeListener secondMockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnSwipeListener(firstMockListener);
		interactor.addOnSwipeListener(secondMockListener);
		// Act:
		for (int i = 0; i < 10; i++) {
			interactor.notifySwipeCanceled(mockViewHolder);
		}
		// Assert:
		verify(firstMockListener, times(10)).onSwipeCanceled(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		verify(secondMockListener, times(10)).onSwipeCanceled(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onSwipeStarted(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onSwipeFinished(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class), anyInt());
		interactor.removeOnSwipeListener(firstMockListener);
		interactor.removeOnSwipeListener(secondMockListener);
		interactor.notifySwipeCanceled(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifySwipeCanceledWithoutRegisteredListeners() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifySwipeCanceled(mock(RecyclerView.ViewHolder.class));
	}

	@Test public void testCanAttachAdapter() {
		// Act + Assert:
		assertThat(new ItemSwipeHelper.Interactor().canAttachAdapter(mock(RecyclerView.Adapter.class)), is(false));
		assertThat(new ItemSwipeHelper.Interactor().canAttachAdapter(mock(TestAdapter.class)), is(true));
	}

	@Test public void testOnAdapterAttached() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act:
		interactor.onAdapterAttached(mockAdapter);
		// Assert:
		assertThat(interactor.swipeAdapter, Is.<ItemSwipeHelper.SwipeAdapter>is(mockAdapter));
	}

	@Test public void testOnAdapterDetached() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		interactor.swiping = true;
		// Act:
		interactor.onAdapterDetached(mockAdapter);
		// Assert:
		assertThat(interactor.swipeAdapter, is(nullValue()));
	}

	@Test public void testSetEnabled() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		// Act + Assert:
		interactor.swiping = true;
		interactor.setEnabled(false);
		assertThat(interactor.isActive(), is(false));
		interactor.swiping = true;
		interactor.setEnabled(true);
		assertThat(interactor.isActive(), is(false));
	}

	@Test public void testIsActive() {
		// Arrange:
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.swiping = true;
		// Act + Assert:
		assertThat(interactor.isActive(), is(true));
	}

	@Test public void testGetMovementFlags() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		final int itemSwipeFlags = ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START | ItemSwipeHelper.END);
		when(mockAdapter.getItemSwipeFlags(0)).thenReturn(itemSwipeFlags);
		final TestHolder mockViewHolder = createMockHolder(new View(application), 0);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mockViewHolder), is(itemSwipeFlags));
		verify(mockAdapter).getItemSwipeFlags(0);
	}

	@Test public void testGetMovementFlagsForNotSwipeHolder() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test public void testGetMovementFlagsWhenDisabled() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test public void testGetMovementFlagsWithoutAttachedAdapter() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
	}

	@Test public void testOnSelectedChanged() throws Exception {
		// Arrange:
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(view);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		// Assert:
		assertThat(interactor.isActive(), is(true));
		verify(mockHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder).onSwipeStarted();
		verify(mockListener).onSwipeStarted(helper, mockHolder);
		verifyNoMoreInteractions(mockAdapter, mockListener);
	}

	@Test public void testOnSelectedChangedWithoutInteractiveView() throws Exception {
		// Arrange:
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		// Assert:
		assertThat(interactor.isActive(), is(true));
		verify(mockHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder).onSwipeStarted();
		verify(mockListener).onSwipeStarted(helper, mockHolder);
	}

	@Test public void testOnSelectedChangedForNotSwipeInteraction() throws Exception {
		// Arrange:
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(view, 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder, mockListener);
	}

	@Test public void testOnSelectedChangedForNotSwipeHolder() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockListener);
	}

	@Test public void testOnSelectedChangedWhenDisabled() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder, mockListener);
	}

	@Test public void testOnSelectedChangedWithoutAttachedAdapter() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemSwipeHelper.INTERACTION);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verifyZeroInteractions(mockHolder, mockListener);
	}

	@Test public void testOnMove() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		// Act + Assert:
		// Only ensure that invocation of the method does not cause any troubles.
		assertThat(
				new ItemSwipeHelper.Interactor().onMove(
						mockRecyclerView,
						mock(RecyclerView.ViewHolder.class),
						mock(RecyclerView.ViewHolder.class)
				),
				is(false)
		);
	}

	@Test public void testOnSwiped() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = mock(TestHolder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		// Act:
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder).onSwipeFinished(ItemSwipeHelper.START);
		verify(mockListener).onSwipeFinished(helper, mockHolder, ItemSwipeHelper.START);
	}

	@Test public void testOnSwipedForNotSwipeHolder() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		// Act:
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		// Assert:
		assertThat(interactor.isActive(), is(true));
		verifyZeroInteractions(mockListener);
	}

	@Test public void testOnSwipedWhenDisabled() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = mock(TestHolder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		// Assert:
		verifyZeroInteractions(mockListener);
	}

	@Test public void testOnSwipedWithoutAttachedAdapter() {
		// Arrange:
		final TestHolder mockHolder = mock(TestHolder.class);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		// Act:
		interactor.onSwiped(mockHolder, ItemSwipeHelper.START);
		// Assert:
		verifyZeroInteractions(mockListener);
	}

	@Test public void testClearView() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(mockRecyclerView, view);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(view);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder).onSwipeCanceled();
		verify(mockListener).onSwipeCanceled(helper, mockHolder);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testClearViewNotInteractive() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(mockRecyclerView, view);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder).onSwipeCanceled();
		verify(mockListener).onSwipeCanceled(helper, mockHolder);
	}

	@Test public void testClearViewForHolderWithUnknownPosition() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final View view = new View(application);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(view, RecyclerView.NO_POSITION);
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper helper = new ItemSwipeHelper();
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.swiping = true;
		when(mockHolder.getInteractiveView(ItemSwipeHelper.INTERACTION)).thenReturn(null);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		verify(mockHolder).getInteractiveView(ItemSwipeHelper.INTERACTION);
		verify(mockHolder, times(0)).onSwipeCanceled();
		verify(mockListener, times(0)).onSwipeCanceled(any(ItemSwipeHelper.class), any(RecyclerView.ViewHolder.class));
	}

	@Test public void testClearViewForNotSwipeHolder() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testClearViewWhenDisabled() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnSwipeListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testClearViewWithoutAttachedAdapter() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemSwipeHelper.OnSwipeListener mockListener = mock(ItemSwipeHelper.OnSwipeListener.class);
		final ItemSwipeHelper.Interactor interactor = new ItemSwipeHelper.Interactor();
		interactor.addOnSwipeListener(mockListener);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockListener);
	}

	private static TestHolder createMockHolder(final View itemView, final int position) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static TestHolder createMockHolder(final RecyclerView recyclerView, final View itemView) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		final Field recyclerViewField = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
		recyclerViewField.setAccessible(true);
		recyclerViewField.set(mockHolder, recyclerView);
		when(mockHolder.getAdapterPosition()).thenReturn(0);
		return mockHolder;
	}

	private static RecyclerView.ViewHolder createMockViewHolder(final View itemView) throws Exception {
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final Field itemViewField = RecyclerView.ViewHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(0);
		return mockHolder;
	}

	private static abstract class TestAdapter extends RecyclerView.Adapter implements ItemSwipeHelper.SwipeAdapter {}

	private static abstract class TestHolder extends RecyclerView.ViewHolder implements ItemSwipeHelper.SwipeViewHolder {

		TestHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}