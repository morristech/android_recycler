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
package universum.studios.android.samples.recycler.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public final class SampleViewHolderFactory extends AdapterHolder.InflaterFactory<SampleViewHolder> {

	private final int layoutResource;

	private SampleViewHolderFactory(final int layoutResource) {
		this.layoutResource = layoutResource;
	}

	@NonNull public static SampleViewHolderFactory create(@LayoutRes final int layoutResource) {
		return new SampleViewHolderFactory(layoutResource);
	}

	@Override @NonNull public SampleViewHolder createHolder(@NonNull final ViewGroup parent, final int viewType) {
		return new SampleViewHolder(inflateView(layoutResource, parent));
	}
}