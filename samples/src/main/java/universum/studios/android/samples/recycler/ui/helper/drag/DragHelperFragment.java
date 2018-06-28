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
package universum.studios.android.samples.recycler.ui.helper.drag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import universum.studios.android.recycler.helper.ItemDragHelper;
import universum.studios.android.recycler.helper.ItemSwipeHelper;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.AdapterItems;
import universum.studios.android.samples.recycler.ui.RecyclerSampleFragment;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;
import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public class DragHelperFragment extends RecyclerSampleFragment<SampleDragAdapter> implements ItemDragHelper.OnDragListener {

	@SuppressWarnings("unused") private static final String TAG = "DragHelperFragment";

	private ItemDragHelper dragHelper;

	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override @NonNull protected SampleDragAdapter createAdapterWithHolderFactory(@NonNull final AdapterHolder.Factory<SampleViewHolder> factory) {
		final SampleDragAdapter adapter = new SampleDragAdapter(getActivity(), AdapterItems.createSampleList(getResources()));
		adapter.setHolderFactory(factory);
		return adapter;
	}

	@Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.collectionView.setItemAnimator(new ItemSwipeHelper.SwipeItemAnimator());
		this.dragHelper = new ItemDragHelper();
		this.dragHelper.getInteractor().setLongPressDragEnabled(false);
		this.dragHelper.getInteractor().addOnDragListener(this);
		this.dragHelper.attachToRecyclerView(collectionView);
	}

	@Override public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recycler_helper, menu);
	}

	@Override public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_enabled:
				item.setChecked(!item.isChecked());
				dragHelper.getInteractor().setEnabled(item.isChecked());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override public boolean onDataSetActionSelected(final int action, final int position, final long id, @Nullable final Object payload) {
		switch (action) {
			case SampleDragAdapter.ACTION_DRAG_INITIATE:
				dragHelper.startDrag((RecyclerView.ViewHolder) payload);
				return true;
			default:
				return super.onDataSetActionSelected(action, position, id, payload);
		}
	}

	@Override public void onDragStarted(
			@NonNull final ItemDragHelper dragHelper,
			@NonNull final RecyclerView.ViewHolder viewHolder
	) {
		Log.d(TAG, "Drag STARTED for position(" + viewHolder.getAdapterPosition() + ").");
	}

	@Override public void onDragFinished(
			@NonNull final ItemDragHelper dragHelper,
			@NonNull final RecyclerView.ViewHolder viewHolder,
			final int fromPosition,
			final int toPosition
	) {
		Log.d(TAG, "Drag FINISHED from position(" + fromPosition + ") to position(" + toPosition + ").");
	}

	@Override public void onDragCanceled(
			@NonNull final ItemDragHelper dragHelper,
			@NonNull final RecyclerView.ViewHolder viewHolder
	) {
		Log.d(TAG, "Drag CANCELED for position(" + viewHolder.getAdapterPosition() + ").");
	}
}