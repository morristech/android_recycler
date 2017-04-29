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
package universum.studios.android.samples.recycler.ui.decoration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.AdapterItems;
import universum.studios.android.samples.recycler.ui.RecyclerSampleFragment;
import universum.studios.android.samples.recycler.ui.adapter.SampleAdapter;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;
import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public abstract class DecorationSampleFragment extends RecyclerSampleFragment<SampleAdapter> {

	@SuppressWarnings("unused")
	private static final String TAG = "DecorationSampleFragment";

	private Menu mMenu;
	private RecyclerView.ItemDecoration mItemDecoration;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@NonNull
	@Override
	protected SampleAdapter createAdapterWithHolderFactory(@NonNull AdapterHolder.Factory<SampleViewHolder> factory) {
		final SampleAdapter adapter = new SampleAdapter(getActivity(), AdapterItems.createSampleList(getResources()));
		adapter.setHolderFactory(factory);
		return adapter;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onChangeLayoutManager(collectionView, createLayoutManagerForCurrentLayoutSetting());
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recycler_decoration, mMenu = menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.isChecked()) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.menu_recycler_layout_linear:
			case R.id.menu_recycler_layout_grid:
			case R.id.menu_recycler_orientation_vertical:
			case R.id.menu_recycler_orientation_horizontal:
				item.setChecked(true);
				onChangeLayoutManager(collectionView, createLayoutManagerForCurrentLayoutSetting());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private RecyclerView.LayoutManager createLayoutManagerForCurrentLayoutSetting() {
		final Context context = getActivity();
		if (mMenu == null) {
			return createLinearLayoutManager(context, LinearLayoutManager.VERTICAL);
		}
		final boolean isLinear = mMenu.findItem(R.id.menu_recycler_layout_linear).isChecked();
		final boolean isVertical = mMenu.findItem(R.id.menu_recycler_orientation_vertical).isChecked();
		final int orientation = isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL;
		return isLinear ? createLinearLayoutManager(context, orientation) : createGridLayoutManager(context, orientation);
	}

	@NonNull
	protected RecyclerView.LayoutManager createLinearLayoutManager(@NonNull Context context, int orientation) {
		return new LinearLayoutManager(context, orientation, false);
	}

	@NonNull
	protected RecyclerView.LayoutManager createGridLayoutManager(@NonNull Context context, int orientation) {
		return new GridLayoutManager(context, 2, orientation, false);
	}

	protected void onChangeLayoutManager(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.LayoutManager layoutManager) {
		recyclerView.setLayoutManager(layoutManager);
	}

	protected void setItemDecoration(@Nullable RecyclerView.ItemDecoration decoration) {
		if (mItemDecoration != null) {
			collectionView.removeItemDecoration(mItemDecoration);
		}
		this.mItemDecoration = decoration;
		if (decoration != null) {
			collectionView.addItemDecoration(decoration);
		}
	}
}
