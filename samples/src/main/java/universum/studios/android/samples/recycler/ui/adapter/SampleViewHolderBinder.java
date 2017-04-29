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

import android.support.annotation.NonNull;

import java.util.List;

import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public final class SampleViewHolderBinder implements AdapterHolder.Binder<SampleAdapter, SampleViewHolder> {

	@SuppressWarnings("unused")
	private static final String TAG = "SampleViewHolderBinder";

	private SampleViewHolderBinder() {
	}

	@NonNull
	public static SampleViewHolderBinder create() {
		return new SampleViewHolderBinder();
	}

	@Override
	public void bindHolder(@NonNull SampleAdapter adapter, @NonNull SampleViewHolder holder, int position, @NonNull List<Object> payloads) {
		holder.bind(adapter, position, payloads);
	}
}
