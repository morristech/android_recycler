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
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ItemDragHelperInteractorTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ItemDragHelperInteractorTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
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
	public void test() {
		// todo:
	}
}
