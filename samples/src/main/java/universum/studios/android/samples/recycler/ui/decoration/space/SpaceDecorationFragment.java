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

	@BindDimen(R.dimen.spacing_secondary) int secondarySpacing;
	@BindDimen(R.dimen.spacing_tertiary) int tertiarySpacing;

	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
		setAdapter(createAdapterWithHolderFactory(SampleViewHolderFactory.create(R.layout.item_list_card)));
	}

	@Override protected void onChangeLayoutManager(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.LayoutManager layoutManager) {
		super.onChangeLayoutManager(recyclerView, layoutManager);
		setItemDecoration(new ItemSpaceDecoration(secondarySpacing, tertiarySpacing));
	}
}