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
package universum.studios.android.samples.recycler.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public final class SampleViewHolderFactory extends AdapterHolder.InflaterFactory<SampleViewHolder> {

	@SuppressWarnings("unused")
	private static final String TAG = "SampleViewHolderFactory";

	private final int layoutResource;

	private SampleViewHolderFactory(int layoutResource) {
		this.layoutResource = layoutResource;
	}

	@NonNull
	public static SampleViewHolderFactory create(@LayoutRes int layoutResource) {
		return new SampleViewHolderFactory(layoutResource);
	}

	@NonNull
	@Override
	public SampleViewHolder createHolder(@NonNull ViewGroup parent, int viewType) {
		return new SampleViewHolder(inflateView(layoutResource, parent));
	}
}
