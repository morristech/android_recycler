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
import static org.mockito.internal.util.MockUtil.resetMock;

/**
 * @author Martin Albedinsky
 */
public final class ItemDragHelperInteractorTest extends RobolectricTestCase {

	// Arrange:
	// Act:
	// Assert:
    
	private RecyclerView mMockRecyclerView;

	public ItemDragHelperInteractorTest() {
		this.mMockRecyclerView = mock(RecyclerView.class);
	}

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		resetMock(mMockRecyclerView);
	}

	@Test
	public void testSetIsLongPressDragEnabled() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setLongPressDragEnabled(false);
		assertThat(interactor.isLongPressDragEnabled(), is(false));
		interactor.setLongPressDragEnabled(true);
		assertThat(interactor.isLongPressDragEnabled(), is(true));
	}

	@Test
	public void testIsLongPressDragEnabledDefault() {
		assertThat(new ItemDragHelper.Interactor().isLongPressDragEnabled(), is(true));
	}

	@Test
	public void testSetGetDragThreshold() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setDragThreshold(0.25f);
		assertThat(interactor.getDragThreshold(), is(0.25f));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testSetDragThresholdOutOfRange() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setDragThreshold(1.15f);
		assertThat(interactor.getDragThreshold(), is(1.0f));
		interactor.setDragThreshold(-0.75f);
		assertThat(interactor.getDragThreshold(), is(0.0f));
	}

	@Test
	public void testGetDragThresholdDefault() {
		assertThat(new ItemDragHelper.Interactor().getDragThreshold(), is(ItemDragHelper.MOVE_THRESHOLD));
	}

	@Test
	public void testGetMoveThreshold() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		assertThat(interactor.getMoveThreshold(mock(RecyclerView.ViewHolder.class)), is(interactor.getDragThreshold()));
	}

	@Test
	public void testAddOnDragListener() {
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verify(firstMockListener, times(1)).onDragStarted(helper, mockViewHolder);
		verify(secondMockListener, times(1)).onDragStarted(helper, mockViewHolder);
	}

	@Test
	public void testRemoveOnDragListener() {
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		interactor.removeOnDragListener(firstMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener, times(1)).onDragStarted(helper, mockViewHolder);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testRemoveOnDragListenerNotAdded() {
		new ItemDragHelper.Interactor().removeOnDragListener(mock(ItemDragHelper.OnDragListener.class));
	}

	@Test
	public void testNotifyDragStarted() {
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragStarted(mockViewHolder);
		}
		verify(firstMockListener, times(10)).onDragStarted(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(firstMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(10)).onDragStarted(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(secondMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragStarted(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifyDragStartedWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemDragHelper.Interactor().notifyDragStarted(mock(RecyclerView.ViewHolder.class));
	}

	@Test
	public void testNotifyDragFinished() {
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragFinished(mockViewHolder, i, i + 1);
			verify(firstMockListener, times(1)).onDragFinished(helper, mockViewHolder, i, i + 1);
			verify(secondMockListener, times(1)).onDragFinished(helper, mockViewHolder, i, i + 1);
		}
		verify(firstMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragCanceled(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragFinished(mockViewHolder, 0, 1);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifyDragFinishedWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemDragHelper.Interactor().notifyDragFinished(mock(RecyclerView.ViewHolder.class), 0, 1);
	}

	@Test
	public void testNotifyDragCanceled() {
		final ItemDragHelper helper = new ItemDragHelper();
		final RecyclerView.ViewHolder mockViewHolder = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.OnDragListener firstMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.OnDragListener secondMockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.addOnDragListener(firstMockListener);
		interactor.addOnDragListener(secondMockListener);
		for (int i = 0; i < 10; i++) {
			interactor.notifyDragCanceled(mockViewHolder);
		}
		verify(firstMockListener, times(10)).onDragCanceled(helper, mockViewHolder);
		verify(firstMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(firstMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		verify(secondMockListener, times(10)).onDragCanceled(helper, mockViewHolder);
		verify(secondMockListener, times(0)).onDragStarted(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class));
		verify(secondMockListener, times(0)).onDragFinished(any(ItemDragHelper.class), any(RecyclerView.ViewHolder.class), anyInt(), anyInt());
		interactor.removeOnDragListener(firstMockListener);
		interactor.removeOnDragListener(secondMockListener);
		interactor.notifyDragCanceled(mockViewHolder);
		verifyNoMoreInteractions(firstMockListener);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testNotifyDragCanceledWithoutRegisteredListeners() {
		// Only ensure that invocation of the method without registered listeners does not cause
		// any troubles.
		new ItemDragHelper.Interactor().notifyDragCanceled(mock(RecyclerView.ViewHolder.class));
	}

	@Test
	public void testCanAttachAdapter() {
		assertThat(new ItemDragHelper.Interactor().canAttachAdapter(mock(RecyclerView.Adapter.class)), is(false));
		assertThat(new ItemDragHelper.Interactor().canAttachAdapter(mock(TestAdapter.class)), is(true));
	}

	@Test
	public void testOnAdapterAttached() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		assertThat(interactor.dragAdapter, Is.<ItemDragHelper.DragAdapter>is(mockAdapter));
	}

	@Test
	public void testOnAdapterDetached() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.onAdapterAttached(mockAdapter);
		interactor.dragging = true;
		interactor.draggingFromPosition = 0;
		interactor.movingFromPosition = 1;
		interactor.movingToPosition = 2;
		interactor.onAdapterDetached(mockAdapter);
		assertThat(interactor.dragAdapter, is(nullValue()));
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
	}

	@Test
	public void testSetEnabled() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
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

	@Test
	public void testIsActive() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.dragging = true;
		assertThat(interactor.isActive(), is(true));
	}

	@Test
	public void testIsActiveDefault() {
		assertThat(new ItemDragHelper.Interactor().isActive(), is(false));
	}

	@Test
	public void testGetMovementFlags() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		final TestHolder mockHolder = createMockHolder(mMockRecyclerView, new View(application), 0);
		when(mockAdapter.getItemDragFlags(0)).thenReturn(ItemDragHelper.makeDragFlags(ItemDragHelper.UP));
		assertThat(
				interactor.getMovementFlags(mMockRecyclerView, mock(TestHolder.class)),
				is(ItemDragHelper.makeDragFlags(ItemDragHelper.UP))
		);
		verify(mockAdapter, times(1)).getItemDragFlags(0);
	}

	@Test
	public void testGetMovementFlagsForNotDragHolder() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test
	public void testGetMovementFlagsWhenDisabled() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
		verifyZeroInteractions(mockAdapter);
	}

	@Test
	public void testGetMovementFlagsWithoutAttachedAdapter() {
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setEnabled(false);
		assertThat(interactor.getMovementFlags(mMockRecyclerView, mock(RecyclerView.ViewHolder.class)), is(0));
	}

	@Test
	public void testOnSelectedChanged() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper helper = new ItemDragHelper();
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachToHelper(helper);
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		assertThat(interactor.isActive(), is(true));
		assertThat(interactor.draggingFromPosition, is(mockHolder.getAdapterPosition()));
		verify(mockHolder, times(1)).onDragStarted();
		verify(mockAdapter, times(1)).onItemDragStarted(mockHolder.getAdapterPosition());
		verify(mockListener, times(1)).onDragStarted(helper, mockHolder);
	}

	@Test
	public void testOnSelectedChangedForNotDragInteraction() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_SWIPE);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedForNotDragHolder() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedWhenDisabled() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.setEnabled(false);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnSelectedChangedWithoutAttachedAdapter() throws Exception {
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.addOnDragListener(mockListener);
		interactor.onSelectedChanged(mockHolder, ItemTouchHelper.ACTION_STATE_DRAG);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testOnMoveWhenAdapterReturnsTrue() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = createMockHolder(mMockRecyclerView, new View(application), 0);
		final TestHolder mockHolderTarget = createMockHolder(mMockRecyclerView, new View(application), 1);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(true);
		assertThat(interactor.onMove(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(true));
		assertThat(interactor.movingFromPosition, is(mockHolderCurrent.getAdapterPosition()));
		assertThat(interactor.movingToPosition, is(mockHolderTarget.getAdapterPosition()));
		verify(mockAdapter, times(1)).onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
	}

	@Test
	public void testOnMoveWhenAdapterReturnsFalse() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = createMockHolder(mMockRecyclerView, new View(application), 0);
		final TestHolder mockHolderTarget = createMockHolder(mMockRecyclerView, new View(application), 1);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(false);
		assertThat(interactor.onMove(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.movingFromPosition, is(mockHolderCurrent.getAdapterPosition()));
		assertThat(interactor.movingToPosition, is(mockHolderTarget.getAdapterPosition()));
		verify(mockAdapter, times(1)).onMoveItem(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
	}

	@Test
	public void testOnMoveForNotDragHolders() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final RecyclerView.ViewHolder mockViewHolderCurrent = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder mockViewHolderTarget = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		assertThat(interactor.onMove(mMockRecyclerView, mockViewHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.onMove(mMockRecyclerView, mockHolderCurrent, mockViewHolderTarget), is(false));
		assertThat(interactor.onMove(mMockRecyclerView, mockViewHolderCurrent, mockViewHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockHolderCurrent);
		verifyZeroInteractions(mockHolderTarget);
	}

	@Test
	public void testOnMoveWhenDisabled() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		assertThat(interactor.onMove(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockHolderCurrent);
		verifyZeroInteractions(mockHolderTarget);
	}

	@Test
	public void testOnMoveWithoutAttachedAdapter() {
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		assertThat(new ItemDragHelper.Interactor().onMove(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
	}

	@Test
	public void testOnSwiped() {
		// Only ensure that invocation of the method does not cause any troubles.
		new ItemDragHelper.Interactor().onSwiped(mock(RecyclerView.ViewHolder.class), ItemDragHelper.UP);
	}

	@Test
	public void testCanDropOverWhenAdapterReturnsTrue() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(true);
		assertThat(interactor.canDropOver(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(true));
		verify(mockAdapter, times(1)).canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
	}

	@Test
	public void testCanDropOverWhenAdapterReturnsFalse() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		when(mockAdapter.canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition())).thenReturn(false);
		assertThat(interactor.canDropOver(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verify(mockAdapter, times(1)).canDropItemOver(mockHolderCurrent.getAdapterPosition(), mockHolderTarget.getAdapterPosition());
	}

	@Test
	public void testCanDropOverForNotDragHolders() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final RecyclerView.ViewHolder mockViewHolderCurrent = mock(RecyclerView.ViewHolder.class);
		final RecyclerView.ViewHolder mockViewHolderTarget = mock(RecyclerView.ViewHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		assertThat(interactor.canDropOver(mMockRecyclerView, mockViewHolderCurrent, mockHolderTarget), is(false));
		assertThat(interactor.canDropOver(mMockRecyclerView, mockHolderCurrent, mockViewHolderTarget), is(false));
		assertThat(interactor.canDropOver(mMockRecyclerView, mockViewHolderCurrent, mockViewHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockHolderCurrent);
		verifyZeroInteractions(mockHolderTarget);
	}

	@Test
	public void testCanDropOverWhenDisabled() {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.setEnabled(false);
		assertThat(interactor.canDropOver(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockHolderCurrent);
		verifyZeroInteractions(mockHolderTarget);
	}

	@Test
	public void testCanDropOverWithoutAttachedAdapter() {
		final TestHolder mockHolderCurrent = mock(TestHolder.class);
		final TestHolder mockHolderTarget = mock(TestHolder.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.setEnabled(false);
		assertThat(interactor.canDropOver(mMockRecyclerView, mockHolderCurrent, mockHolderTarget), is(false));
		verifyZeroInteractions(mockHolderCurrent);
		verifyZeroInteractions(mockHolderTarget);
	}

	@Test
	public void testClearView() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
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
		interactor.clearView(mMockRecyclerView, mockHolder);
		assertThat(interactor.isActive(), is(false));
		assertThat(interactor.draggingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingFromPosition, is(RecyclerView.NO_POSITION));
		assertThat(interactor.movingToPosition, is(RecyclerView.NO_POSITION));
		verify(mockHolder, times(1)).onDragFinished(0, mockHolder.getAdapterPosition());
		verify(mockAdapter, times(1)).onItemDragFinished(0, mockHolder.getAdapterPosition());
		verify(mockListener, times(1)).onDragFinished(helper, mockHolder, 0, mockHolder.getAdapterPosition());
	}

	@Test
	public void testClearViewForNotDragHolder() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final RecyclerView.ViewHolder mockHolder = createMockViewHolder(new View(application));
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testClearViewWhenDisabled() throws Exception {
		final TestAdapter mockAdapter = mock(TestAdapter.class);
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.attachAdapter(mockAdapter);
		interactor.addOnDragListener(mockListener);
		interactor.setEnabled(false);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockAdapter);
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testClearViewWithoutAttachedAdapter() throws Exception {
		final TestHolder mockHolder = createMockHolder(new View(application), 0);
		final ItemDragHelper.OnDragListener mockListener = mock(ItemDragHelper.OnDragListener.class);
		final ItemDragHelper.Interactor interactor = new ItemDragHelper.Interactor();
		interactor.addOnDragListener(mockListener);
		interactor.clearView(mMockRecyclerView, mockHolder);
		verifyZeroInteractions(mockHolder);
		verifyZeroInteractions(mockListener);
	}

	private static TestHolder createMockHolder(View itemView, int position) throws Exception {
		final TestHolder mockHolder = mock(TestHolder.class);
		final Field itemViewField = TestHolder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		when(mockHolder.getAdapterPosition()).thenReturn(position);
		return mockHolder;
	}

	private static TestHolder createMockHolder(RecyclerView recyclerView, View itemView, int position) throws Exception {
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

	private static RecyclerView.ViewHolder createMockViewHolder(View itemView) throws Exception {
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