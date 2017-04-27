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
import android.support.annotation.Nullable;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class RecyclerViewItemHelperTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "RecyclerViewItemHelperTest";

	@Rule
	public final UiThreadTestRule UI_RULE = new UiThreadTestRule();

	private Interactor mMockInteractor;
	private RecyclerViewItemHelper<Interactor> mHelper;
	private RecyclerViewItemHelper.ItemInteractor mInteractor;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockInteractor = mock(Interactor.class);
		this.mHelper = new Helper(mMockInteractor);
		this.mInteractor = new Interactor();
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		this.mMockInteractor = null;
		this.mHelper = null;
		this.mInteractor = null;
	}

	@Test
	public void testInstantiation() {
		final Helper helper = new Helper(mMockInteractor);
		assertThat(helper.mInteractor, is(not(nullValue())));
		verify(mMockInteractor, times(1)).onAttachedToHelper(helper);
	}

	@Test
	@UiThreadTest
	public void testAttachToRecyclerView() {
		final RecyclerView recyclerView = new RecyclerView(mContext);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		recyclerView.setAdapter(mockAdapter);
		when(mMockInteractor.canAttachToAdapter(mockAdapter)).thenReturn(true);
		mHelper.attachToRecyclerView(recyclerView);
		verify(mMockInteractor, times(1)).canAttachToAdapter(mockAdapter);
		verify(mMockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mMockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@UiThreadTest
	@Test(expected = IllegalArgumentException.class)
	public void testAttachToRecyclerViewWithUnsupportedAdapter() {
		final RecyclerView recyclerView = new RecyclerView(mContext);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		recyclerView.setAdapter(mockAdapter);
		when(mMockInteractor.canAttachToAdapter(mockAdapter)).thenReturn(false);
		mHelper.attachToRecyclerView(recyclerView);
	}

	@Test
	@UiThreadTest
	public void testAttachToRecyclerViewWithoutAdapter() {
		final RecyclerView recyclerView = new RecyclerView(mContext);
		mHelper.attachToRecyclerView(recyclerView);
		verify(mMockInteractor, times(0)).canAttachToAdapter(any(RecyclerView.Adapter.class));
		verify(mMockInteractor, times(0)).onAdapterAttached(any(RecyclerView.Adapter.class));
		verify(mMockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testAttachToNullRecyclerView() {
		mHelper.attachToRecyclerView(null);
		verify(mMockInteractor, times(0)).canAttachToAdapter(any(RecyclerView.Adapter.class));
		verify(mMockInteractor, times(0)).onAdapterAttached(any(RecyclerView.Adapter.class));
		verify(mMockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testSetEnabled() {
		mHelper.setEnabled(true);
		verify(mMockInteractor, times(1)).setEnabled(true);
		mHelper.setEnabled(false);
		verify(mMockInteractor, times(1)).setEnabled(false);
	}

	@Test
	public void testIsEnabled() {
		mHelper.isEnabled();
		verify(mMockInteractor, times(1)).isEnabled();
	}

	@Test
	public void testIsActive() {
		mHelper.isActive();
		verify(mMockInteractor, times(1)).isActive();
	}

	@Test
	public void testInteractorAttachToHelper() {
		final Interactor mockInteractor = mock(Interactor.class);
		final Helper mockHelper = mock(Helper.class);
		mockInteractor.attachToHelper(mockHelper);
		verify(mockInteractor, times(1)).onAttachedToHelper(mockHelper);
	}

	@Test
	public void testInteractorAttachAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		mockInteractor.attachAdapter(mockAdapter);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testInteractorAttachAdapterWithPreviousAttached() {
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
	public void testInteractorAttachSameAdapter() {
		final Interactor mockInteractor = mock(Interactor.class);
		final RecyclerView.Adapter mockAdapter = mock(RecyclerView.Adapter.class);
		mockInteractor.attachAdapter(mockAdapter);
		mockInteractor.attachAdapter(mockAdapter);
		verify(mockInteractor, times(1)).onAdapterAttached(mockAdapter);
		verify(mockInteractor, times(0)).onAdapterDetached(any(RecyclerView.Adapter.class));
	}

	@Test
	public void testInteractorOnAttachedToHelper() {
		// Only test that invocation of this method does not cause any troubles.
		mInteractor.onAttachedToHelper(mock(Helper.class));
	}

	@Test
	public void testInteractorOnAdapterAttached() {
		// Only test that invocation of this method does not cause any troubles.
		mInteractor.onAdapterAttached(mock(RecyclerView.Adapter.class));
	}

	@Test
	public void testInteractorOnAdapterDetached() {
		// Only test that invocation of this method does not cause any troubles.
		mInteractor.onAdapterDetached(mock(RecyclerView.Adapter.class));
	}

	@Test
	public void testInteractorSetIsEnabled() {
		mInteractor.setEnabled(false);
		assertThat(mInteractor.isEnabled(), is(false));
		mInteractor.setEnabled(true);
		assertThat(mInteractor.isEnabled(), is(true));
	}

	@Test
	public void testInteractorIsEnabledDefault() {
		assertThat(mInteractor.isEnabled(), is(true));
	}

	@Test
	@UiThreadTest
	public void testInteractorOnChildDraw() throws Exception {
		final Holder mockHolder = createMockHolderWithItemView(new View(mContext));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
		final View view = new View(mContext);
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
	public void testInteractorOnChildDrawWithNotInteractiveViewHolder() {
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
		interactor.onChildDraw(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(mContext)) {},
				0, 0,
				Helper.ACTION_STATE_SWIPE,
				true
		);
	}

	@Test
	public void testInteractorOnChildDrawWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		final Holder mockHolder = createMockHolderWithItemView(new View(mContext));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
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
	public void testInteractorOnChildDrawOver() throws Exception {
		final Holder mockHolder = createMockHolderWithItemView(new View(mContext));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
		final View view = new View(mContext);
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
	public void testInteractorOnChildDrawOverWithNotInteractiveViewHolder() {
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
		interactor.onChildDrawOver(
				canvas,
				recyclerView,
				new RecyclerView.ViewHolder(new View(mContext)) {},
				0, 0,
				Helper.ACTION_STATE_DRAG,
				true
		);
	}

	@Test
	public void testInteractorOnChildDrawOverWithInteractiveViewHolderWithoutInteractiveView() throws Exception {
		final Holder mockHolder = createMockHolderWithItemView(new View(mContext));
		final Interactor interactor = new Interactor();
		final Canvas canvas = new Canvas();
		final RecyclerView recyclerView = new RecyclerView(mContext);
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

	private static Holder createMockHolderWithItemView(View view) throws Exception {
		final Holder mockHolder = mock(Holder.class);
		final Field itemViewField = Holder.class.getField("itemView");
		itemViewField.setAccessible(true);
		itemViewField.set(mockHolder, view);
		return mockHolder;
	}

	private static class Helper extends RecyclerViewItemHelper<Interactor> {

		Helper(@NonNull Interactor interactor) {
			super(interactor);
		}
	}

	private static class Interactor extends RecyclerViewItemHelper.ItemInteractor {

		@Override
		protected boolean canAttachToAdapter(@NonNull RecyclerView.Adapter adapter) {
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
		protected boolean isActive() {
			return false;
		}
	}

	private static class Holder extends RecyclerView.ViewHolder implements RecyclerViewItemHelper.InteractiveViewHolder {

		Holder(View itemView) {
			super(itemView);
		}

		@Nullable
		@Override
		public View getInteractiveView(@RecyclerViewItemHelper.Interaction int interaction) {
			return null;
		}

		@Override
		public void onDraw(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
		}

		@Override
		public void onDrawOver(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
		}
	}
}
