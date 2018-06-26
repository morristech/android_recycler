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
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class RecyclerViewItemHelperInteractorTest extends RobolectricTestCase {

	@Test
	public void testAttachToHelper() {
		final Interactor mockInteractor = mock(Interactor.class);
		final Helper mockHelper = mock(Helper.class);
		mockInteractor.attachToHelper(mockHelper);
		verify(mockInteractor, times(1)).onAttachedToHelper(mockHelper);
	}

	@Test
	public void testAttachAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		mockInteractor.attachAdapter(mockAdapter);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testAttachAdapterWithPreviousAttached() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView.Adapter mockAdapterFirst = mock(RecyclerView.Adapter.class);
		final RecyclerView.Adapter mockAdapterSecond = mock(RecyclerView.Adapter.class);
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
	}

	@Test
	public void testAttachSameAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		mockInteractor.attachAdapter(mockAdapter);
		mockInteractor.attachAdapter(mockAdapter);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testOnAttachedToHelper() {
		// Only test that invocation of this method does not cause any troubles.
		new Interactor().onAttachedToHelper(mock(Helper.class));
	}

	@Test
	public void testOnAdapterAttached() {
		// Only test that invocation of this method does not cause any troubles.
		new Interactor().onAdapterAttached(mock(RecyclerView.Adapter.class));
	}

	@Test
	public void testOnAdapterDetached() {
		// Only test that invocation of this method does not cause any troubles.
		new Interactor().onAdapterDetached(mock(RecyclerView.Adapter.class));
	}

	@Test
	public void testSetIsEnabled() {
		final Interactor interactor = new Interactor();
		interactor.setEnabled(false);
		assertThat(interactor.isEnabled(), is(false));
		interactor.setEnabled(true);
		assertThat(interactor.isEnabled(), is(true));
	}

	@Test
	public void testIsEnabledDefault() {
		assertThat(new Interactor().isEnabled(), is(true));
	}

	@Test
	public void testShouldHandleInteraction() {
		final Interactor interactor = new Interactor();
		interactor.attachAdapter(mock(RecyclerView.Adapter.class));
		assertThat(interactor.shouldHandleInteraction(), is(true));
		interactor.setEnabled(false);
		assertThat(interactor.shouldHandleInteraction(), is(false));
		interactor.setEnabled(true);
		interactor.attachAdapter(null);
		assertThat(interactor.shouldHandleInteraction(), is(false));
	}

	@Test
	public void testShouldHandleInteractionDefault() {
		assertThat(new Interactor().shouldHandleInteraction(), is(false));
	}

	@Test
	public void testOnChildDraw() throws Exception {
		final Holder mockHolder = createMockHolder(new View(application));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		final View view = new View(application);
		when(mockHolder.getInteractiveView(Helper.ACTION_STATE_SWIPE)).thenReturn(view);
		interactor.onChildDraw(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				Helper.ACTION_STATE_SWIPE,
				true
		);
		verify(mockHolder, times(1)).getInteractiveView(Helper.ACTION_STATE_SWIPE);
		verify(mockHolder, times(1)).onDraw(canvas, 0, 0, Helper.ACTION_STATE_SWIPE, true);
	}

	@Test
	public void testOnChildDrawWithNotInteractiveViewHolder() {
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		interactor.onChildDraw(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(application)) {},
				0, 0,
				Helper.ACTION_STATE_SWIPE,
				true
		);
	}

	@Test
	public void testOnChildDrawWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		final Holder mockHolder = createMockHolder(new View(application));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		when(mockHolder.getInteractiveView(Helper.ACTION_STATE_SWIPE)).thenReturn(null);
		interactor.onChildDraw(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				Helper.ACTION_STATE_SWIPE,
				true
		);
		verify(mockHolder, times(1)).getInteractiveView(Helper.ACTION_STATE_SWIPE);
		verify(mockHolder, times(1)).onDraw(canvas, 0, 0, Helper.ACTION_STATE_SWIPE, true);
	}

	@Test
	public void testOnChildDrawOver() throws Exception {
		final Holder mockHolder = createMockHolder(new View(application));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		final View view = new View(application);
		when(mockHolder.getInteractiveView(Helper.ACTION_STATE_DRAG)).thenReturn(view);
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				Helper.ACTION_STATE_DRAG,
				true
		);
		verify(mockHolder, times(1)).getInteractiveView(Helper.ACTION_STATE_DRAG);
		verify(mockHolder, times(1)).onDrawOver(canvas, 0, 0, Helper.ACTION_STATE_DRAG, true);
	}

	@Test
	public void testOnChildDrawOverWithNotInteractiveViewHolder() {
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(application)) {},
				0, 0,
				Helper.ACTION_STATE_DRAG,
				true
		);
	}

	@Test
	public void testOnChildDrawOverWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		final Holder mockHolder = createMockHolder(new View(application));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(application);
		when(mockHolder.getInteractiveView(Helper.ACTION_STATE_DRAG)).thenReturn(null);
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				mockHolder,
				0, 0,
				Helper.ACTION_STATE_DRAG,
				true
		);
		verify(mockHolder, times(1)).getInteractiveView(Helper.ACTION_STATE_DRAG);
		verify(mockHolder, times(1)).onDrawOver(canvas, 0, 0, Helper.ACTION_STATE_DRAG, true);
	}

	private static Holder createMockHolder(View itemView) throws Exception {
		final Holder mockHolder = mock(Holder.class);
		final Field itemViewField = Holder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, itemView);
		return mockHolder;
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

	private static abstract class Holder extends RecyclerView.ViewHolder implements RecyclerViewItemHelper.InteractiveViewHolder {

		Holder(View itemView) {
			super(itemView);
		}
	}
}
