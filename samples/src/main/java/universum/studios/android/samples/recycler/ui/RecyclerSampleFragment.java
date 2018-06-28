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
package universum.studios.android.samples.recycler.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.ui.adapter.SampleAdapter;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolderBinder;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolderFactory;
import universum.studios.android.samples.ui.SamplesRecyclerViewFragment;
import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public abstract class RecyclerSampleFragment<A extends SampleAdapter> extends SamplesRecyclerViewFragment<A, RecyclerView, TextView> {

	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		requestFeature(FEATURES_DATA_SET);
		super.onCreate(savedInstanceState);
		setAdapter(createAdapterWithHolderFactory(SampleViewHolderFactory.create(R.layout.item_list_flat)));
	}

	@Override public View onCreateView(
			@NonNull final LayoutInflater inflater,
			@Nullable final ViewGroup container,
			@Nullable final Bundle savedInstanceState
	) {
		return inflater.inflate(R.layout.fragment_recycler, container, false);
	}

	@NonNull protected abstract A createAdapterWithHolderFactory(@NonNull AdapterHolder.Factory<SampleViewHolder> factory);

	@Override protected void onAttachAdapter(@NonNull final A adapter) {
		adapter.setHolderBinder(SampleViewHolderBinder.create());
		super.onAttachAdapter(adapter);
	}

	@Override public void onDataSetChanged() {
		super.onDataSetChanged();
		updateEmptyViewVisibility();
	}
}