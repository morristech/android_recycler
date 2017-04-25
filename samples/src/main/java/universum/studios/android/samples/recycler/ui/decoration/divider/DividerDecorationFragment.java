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
package universum.studios.android.samples.recycler.ui.decoration.divider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import universum.studios.android.recycler.decoration.ItemDividerDecoration;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.ui.decoration.DecorationSampleFragment;

/**
 * @author Martin Albedinsky
 */
public class DividerDecorationFragment extends DecorationSampleFragment {

	@SuppressWarnings("unused")
	private static final String TAG = "DividerDecorationFragment";

	@BindDrawable(R.drawable.divider) Drawable divider;
	@BindDimen(R.dimen.divider_thickness) int dividerThickness;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		requestFeature(FEATURE_DEPENDENCIES_INJECTION);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onChangeLayoutManager(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.LayoutManager layoutManager) {
		super.onChangeLayoutManager(recyclerView, layoutManager);
		if (layoutManager instanceof GridLayoutManager) {
			setItemDecoration(null);
		} else if (layoutManager instanceof LinearLayoutManager) {
			final ItemDividerDecoration decoration = new ItemDividerDecoration(
					((LinearLayoutManager) layoutManager).getOrientation(),
					divider
			);
			decoration.setDividerThickness(dividerThickness);
			setItemDecoration(decoration);
		} else {
			setItemDecoration(null);
		}
	}
}
