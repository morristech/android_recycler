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
public final class ItemDragHelperInteractorTest extends RobolectricTestCase {

	// Arrange:
	// Act:
	// Assert:

	@Test public void testInstantiation() {
		// Act:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.isLongPressDragEnabled(), is(true));
		assertThat(interactor.getDragThreshold(), is(ItemDragHelper.MOVE_THRESHOLD));
	}
    
	@Test public void testIsLongPressDragEnabled() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		interactor.setLongPressDragEnabled(false);
		assertThat(interactor.isLongPressDragEnabled(), is(false));
		interactor.setLongPressDragEnabled(true);
		assertThat(interactor.isLongPressDragEnabled(), is(true));
	}

	@Test public void testDragThreshold() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		interactor.setDragThreshold(0.25f);
		assertThat(interactor.getDragThreshold(), is(0.25f));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testSetDragThresholdOutOfRange() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		interactor.setDragThreshold(1.15f);
		assertThat(interactor.getDragThreshold(), is(1.0f));
		interactor.setDragThreshold(-0.75f);
		assertThat(interactor.getDragThreshold(), is(0.0f));
	}

	@Test public void testGetMoveThreshold() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		assertThat(interactor.getMoveThreshold(mock(RecyclerView.ViewHolder.class)), is(interactor.getDragThreshold()));
	}

	@Test public void testAddOnDragListener() {
		// Arrange:
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		// Act:
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		// Assert:
		interactor.notifyDragStarted(mockViewHolder);
		verify(firstMockListener).onDragStarted(helper, mockViewHolder);
		verify(secondMockListener).onDragStarted(helper, mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testRemoveOnDragListener() {
		// Arrange:
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		// Act:
		interactor.removeOnDragListener(firstMockListener);
		// Assert:
		interactor.notifyDragStarted(mockViewHolder);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener).onDragStarted(helper, mockViewHolder);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test public void testRemoveOnDragListenerNotAdded() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		interactor.removeOnDragListener(mock(ItemDragHelper.OnDragListener.class));
	}

	@Test public void testNotifyDragStarted() {
		// Arrange:
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		// Act:
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragStarted(mockViewHolder);
		}
		// Assert:
		verify(firstMockListener, times(10)).onDragStarted(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(firstMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(10)).onDragStarted(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(secondMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifyDragStartedWithoutRegisteredListeners() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifyDragStarted(mock(RecyclerView.ViewHolder.class));
	}

	@Test public void testNotifyDragFinished() {
		// Arrange:
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		// Act + Assert:
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragFinished(mockViewHolder, i, i + 1);
			verify(firstMockListener).onDragFinished(helper, mockViewHolder, i, i + 1);
			verify(secondMockListener).onDragFinished(helper, mockViewHolder, i, i + 1);
		}
		verify(firstMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragFinished(mockViewHolder, 0, 1);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifyDragFinishedWithoutRegisteredListeners() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifyDragFinished(mock(RecyclerView.ViewHolder.class), 0, 1);
	}

	@Test public void testNotifyDragCanceled() {
		// Arrange:
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		// Act:
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragCanceled(mockViewHolder);
		}
		// Assert:
		verify(firstMockListener, times(10)).onDragCanceled(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(secondMockListener, times(10)).onDragCanceled(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragCanceled(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testNotifyDragCanceledWithoutRegisteredListeners() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		interactor.notifyDragCanceled(mock(RecyclerView.ViewHolder.class));
	}

	@Test public void testCanAttachAdapter() {
		// Act + Assert:
		assertThat(new ItemDragHelper.Interactor().canAttachAdapter(mock(RecyclerView.Adapter.class)), is(false));
		assertThat(new ItemDragHelper.Interactor().canAttachAdapter(mock(TestAdapter.class)), is(true));
	}

	@Test public void testOnAdapterAttached() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		interactor.onAdapterAttached(mockAdapter);
		// Assert:
		assertThat(interactor.dragAdapter, Is.<ItemDragHelper.DragAdapter>is(mockAdapter));
	}

	@Test public void testOnAdapterDetached() {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		interactor.dragging = true;
		interactor.draggingFromPosition = 0;
		interactor.movingFromPosition = 1;
		interactor.movingToPosition = 2;
		// Act:
		interactor.onAdapterDetached(mockAdapter);
		// Assert:
		assertThat(interactor.dragAdapter, is(nullValue()));
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
	}

	@Test public void testSetEnabled() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		interactor.dragging = true;
		interactor.draggingFromPosition = 0;
		interactor.movingFromPosition = 1;
		interactor.movingToPosition = 2;
		interactor.setEnabled(false);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
		interactor.dragging = true;
		interactor.draggingFromPosition = 0;
		interactor.movingFromPosition = 1;
		interactor.movingToPosition = 2;
		interactor.setEnabled(true);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
	}

	@Test public void testIsActive() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.dragging = true;
		// Act + Assert:
		assertThat(interactor.isActive(), is(true));
	}

	@Test public void testGetMovementFlags() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.getItemDragFlags(0)).thenReturn(ItemDragHelper.makeDragFlags(ItemDragHelper.UP));
		// Act + Assert:
		assertThat(
				interactor.getMovementFlags(mockRecyclerView, mock(TestHolder.class)),
				is(ItemDragHelper.makeDragFlags(ItemDragHelper.UP))
		);
		verify(mockAdapter).getItemDragFlags(0);
		verifyNoMoreInteractions(mockAdapter);
	}

	@Test public void testGetMovementFlagsForNotDragHolder() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test public void testGetMovementFlagsWhenDisabled() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test public void testGetMovementFlagsWithoutAttachedAdapter() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.getMovementFlags(mockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
	}

	@Test public void testOnSelectedChanged() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper helper = new ItemDragHelper();
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		// Assert:
		assertThat(interactor.isActive(), is(true));
		assertThat(interactor.draggingFromPosition, is(mockHolder.getAdapterPosition()));
		verify(mockHolder).onDragStarted();
		verify(mockAdapter).onItemDragStarted(mockHolder.getAdapterPosition());
		verify(mockListener).onDragStarted(helper, mockHolder);
		verifyNoMoreInteractions(mockAdapter, mockListener);
	}

	@Test public void testOnSelectedChangedForNotDragInteraction() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_SWIPE);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testOnSelectedChangedForNotDragHolder() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testOnSelectedChangedWhenDisabled() throws Exception {
		// Arrange:
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testOnSelectedChangedWithoutAttachedAdapter() throws Exception {
		// Arrange:
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder, mockListener);
	}

	@Test public void testOnMoveWhenAdapterReturnsTrue() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = createMockHolder(mockRecyclerView, new View(application), 0);
		final TestHolder mockHolderTarget = createMockHolder(mockRecyclerView, new View(application), 1);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(true);
		// Act + Assert:
		assertThat(interactor.onMove(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(true));
		assertThat(interactor.movingFromPosition, is(mockHolderCurrent.getAdapterPosition()));
		assertThat(interactor.movingToPosition, is(mockHolderTarget.getAdapterPosition()));
		verify(mockAdapter).onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
		verifyNoMoreInteractions(mockAdapter);
	}

	@Test public void testOnMoveWhenAdapterReturnsFalse() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = createMockHolder(mockRecyclerView, new View(application), 0);
		final TestHolder mockHolderTarget = createMockHolder(mockRecyclerView, new View(application), 1);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(false);
		// Act + Assert:
		assertThat(interactor.onMove(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.movingFromPosition, is(mockHolderCurrent.getAdapterPosition()));
		assertThat(interactor.movingToPosition, is(mockHolderTarget.getAdapterPosition()));
		verify(mockAdapter).onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
		verifyNoMoreInteractions(mockAdapter);
	}

	@Test public void testOnMoveForNotDragHolders() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final RecyclerView.ViewHolder mockViewHolderCurrent = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder mockViewHolderTarget = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		// Act + Assert:
		assertThat(interactor.onMove(mockRecyclerView, mockViewHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.onMove(mockRecyclerView, mockHolderCurrent, mockViewHolderTarget), is(false));
		assertThat(interactor.onMove(mockRecyclerView, mockViewHolderCurrent, mockViewHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter, mockHolderCurrent, mockHolderTarget);
	}

	@Test public void testOnMoveWhenDisabled() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.onMove(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter, mockHolderCurrent, mockHolderTarget);
	}

	@Test public void testOnMoveWithoutAttachedAdapter() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act + Assert:
		assertThat(interactor.onMove(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
	}

	@Test public void testOnSwiped() {
		// Arrange:
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		// Act:
		// Only ensure that invocation of the method does not cause any troubles.
		interactor.onSwiped(mock(RecyclerView.ViewHolder.class), ItemDragHelper.UP);
	}

	@Test public void testCanDropOverWhenAdapterReturnsTrue() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(true);
		// Act + Assert:
		assertThat(interactor.canDropOver(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(true));
		verify(mockAdapter).canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
		verifyNoMoreInteractions(mockAdapter);
	}

	@Test public void testCanDropOverWhenAdapterReturnsFalse() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(false);
		// Act + :
		assertThat(interactor.canDropOver(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verify(mockAdapter).canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
		verifyNoMoreInteractions(mockAdapter);
	}

	@Test public void testCanDropOverForNotDragHolders() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final RecyclerView.ViewHolder mockViewHolderCurrent = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder mockViewHolderTarget = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		// Act + Assert:
		assertThat(interactor.canDropOver(mockRecyclerView, mockViewHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.canDropOver(mockRecyclerView, mockHolderCurrent, mockViewHolderTarget), is(false));
		assertThat(interactor.canDropOver(mockRecyclerView, mockViewHolderCurrent, mockViewHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter, mockHolderCurrent, mockHolderTarget);
	}

	@Test public void testCanDropOverWhenDisabled() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.canDropOver(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter, mockHolderCurrent, mockHolderTarget);
	}

	@Test public void testCanDropOverWithoutAttachedAdapter() {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setEnabled(false);
		// Act + Assert:
		assertThat(interactor.canDropOver(mockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockHolderCurrent, mockHolderTarget);
	}

	@Test public void testClearView() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper helper = new ItemDragHelper();
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.dragging = true;
		interactor.draggingFromPosition = 0;
		interactor.movingFromPosition = 1;
		interactor.movingToPosition = 2;
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
		verify(mockHolder).onDragFinished(0, mockHolder.getAdapterPosition());
		verify(mockAdapter).onItemDragFinished(0, mockHolder.getAdapterPosition());
		verify(mockListener).onDragFinished(helper, mockHolder, 0, mockHolder.getAdapterPosition());
		verifyNoMoreInteractions(mockAdapter, mockListener);
	}

	@Test public void testClearViewForNotDragHolder() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testClearViewWhenDisabled() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.setEnabled(false);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockAdapter, mockListener);
	}

	@Test public void testClearViewWithoutAttachedAdapter() throws Exception {
		// Arrange:
		final RecyclerView mockRecyclerView = mock(RecyclerView.class);
		final TestHolder mockHolder = createMockHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.addOnDragListener(mockListener);
		// Act:
		interactor.clearView(mockRecyclerView, mockHolder);
		// Assert:
		verifyZeroInteractions(mockHolder, mockListener);
	}

	private static TestHolder createMockHolder(final View itemView) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(0);
		return mockHolder;
	}

	private static TestHolder createMockHolder(final RecyclerView recyclerView, final View itemView, final int position) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		final Field recyclerViewField = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
		recyclerViewField.setAccessible(true);
		recyclerViewField.set(mockHolder, recyclerView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static RecyclerView.ViewHolder createMockViewHolder(final View itemView) throws Exception {
		final RecyclerView.ViewHolder mockHolder = mock(RecyclerView.ViewHolder.class);
		final Field itemViewField = RecyclerView.ViewHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		return mockHolder;
	}

	private static abstract class TestAdapter extends RecyclerView.Adapter implements ItemDragHelper.DragAdapter {}

	private static abstract class TestHolder extends RecyclerView.ViewHolder implements ItemDragHelper.DragViewHolder {

		TestHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}