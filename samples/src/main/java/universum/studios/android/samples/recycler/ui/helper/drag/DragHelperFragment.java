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
public class DragHelperFragment extends RecyclerSampleFragment<SampleDragAdapter>
		implements
		ItemDragHelper.OnDragListener {

	@SuppressWarnings("unused")
	private static final String TAG = "DragHelperFragment";

	private ItemDragHelper mDragHelper;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@NonNull
	@Override
	protected SampleDragAdapter createAdapterWithHolderFactory(@NonNull AdapterHolder.Factory<SampleViewHolder> factory) {
		final SampleDragAdapter adapter = new SampleDragAdapter(getActivity(), AdapterItems.createSampleList(getResources()));
		adapter.setHolderFactory(factory);
		return adapter;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.collectionView.setItemAnimator(new ItemSwipeHelper.SwipeItemAnimator());
		this.mDragHelper = new ItemDragHelper();
		this.mDragHelper.addOnDragListener(this);
		this.mDragHelper.attachToRecyclerView(collectionView);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recycler_helper, menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_enabled:
				item.setChecked(!item.isChecked());
				mDragHelper.setEnabled(item.isChecked());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onDataSetActionSelected(int action, int position, long id, @Nullable Object payload) {
		switch (action) {
			case SampleDragAdapter.ACTION_DRAG_INITIATE:
				return mDragHelper.startDrag();
		}
		return super.onDataSetActionSelected(action, position, id, payload);
	}

	@Override
	public void onDragStarted(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder) {
		Log.d(TAG, "Drag STARTED for position(" + viewHolder.getAdapterPosition() + ").");
	}

	@Override
	public void onDragFinished(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder, int fromPosition, int toPosition) {
		Log.d(TAG, "Drag FINISHED from position(" + fromPosition + ") to position(" + toPosition + ").");
	}

	@Override
	public void onDragCanceled(@NonNull ItemDragHelper dragHelper, @NonNull RecyclerView.ViewHolder viewHolder) {
		Log.d(TAG, "Drag CANCELED for position(" + viewHolder.getAdapterPosition() + ").");
	}
}
