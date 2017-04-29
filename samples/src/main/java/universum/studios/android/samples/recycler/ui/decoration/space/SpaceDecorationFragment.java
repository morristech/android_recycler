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
package universum.studios.android.samples.recycler.ui.decoration.space;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import butterknife.BindDimen;
import universum.studios.android.recycler.decoration.ItemSpaceDecoration;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolderFactory;
import universum.studios.android.samples.recycler.ui.decoration.DecorationSampleFragment;

/**
 * @author Martin Albedinsky
 */
public class SpaceDecorationFragment extends DecorationSampleFragment {

	@SuppressWarnings("unused")
	private static final String TAG = "SpaceDecorationFragment";

	@BindDimen(R.dimen.spacing_secondary) int secondarySpacing;
	@BindDimen(R.dimen.spacing_tertiary) int tertiarySpacing;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
		setAdapter(createAdapterWithHolderFactory(SampleViewHolderFactory.create(R.layout.item_list_card)));
	}

	@Override
	protected void onChangeLayoutManager(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.LayoutManager layoutManager) {
		super.onChangeLayoutManager(recyclerView, layoutManager);
		setItemDecoration(new ItemSpaceDecoration(secondarySpacing, tertiarySpacing));
	}
}
