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

	private Menu menu;
	private RecyclerView.ItemDecoration itemDecoration;

	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override @NonNull protected SampleAdapter createAdapterWithHolderFactory(@NonNull final AdapterHolder.Factory<SampleViewHolder> factory) {
		final SampleAdapter adapter = new SampleAdapter(getActivity(), AdapterItems.createSampleList(getResources()));
		adapter.setHolderFactory(factory);
		return adapter;
	}

	@Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onChangeLayoutManager(collectionView, createLayoutManagerForCurrentLayoutSetting());
	}

	@Override public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recycler_decoration, this.menu = menu);
	}

	@Override public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
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
		if (menu == null) {
			return createLinearLayoutManager(context, LinearLayoutManager.VERTICAL);
		}
		final boolean isLinear = menu.findItem(R.id.menu_recycler_layout_linear).isChecked();
		final boolean isVertical = menu.findItem(R.id.menu_recycler_orientation_vertical).isChecked();
		final int orientation = isVertical ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL;
		return isLinear ? createLinearLayoutManager(context, orientation) : createGridLayoutManager(context, orientation);
	}

	@NonNull protected RecyclerView.LayoutManager createLinearLayoutManager(@NonNull final Context context, final int orientation) {
		return new LinearLayoutManager(context, orientation, false);
	}

	@NonNull protected RecyclerView.LayoutManager createGridLayoutManager(@NonNull final Context context, final int orientation) {
		return new GridLayoutManager(context, 2, orientation, false);
	}

	protected void onChangeLayoutManager(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.LayoutManager layoutManager) {
		recyclerView.setLayoutManager(layoutManager);
	}

	protected void setItemDecoration(@Nullable final RecyclerView.ItemDecoration decoration) {
		if (itemDecoration != null) {
			collectionView.removeItemDecoration(itemDecoration);
		}
		this.itemDecoration = decoration;
		if (decoration != null) {
			collectionView.addItemDecoration(decoration);
		}
	}
}