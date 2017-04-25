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

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import universum.studios.android.samples.recycler.data.model.AdapterItem;
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;

/**
 * @author Martin Albedinsky
 */
public class SampleAdapter extends SimpleRecyclerAdapter<SampleAdapter, SampleViewHolder, AdapterItem> {

	@SuppressWarnings("unused")
	private static final String TAG = "SampleAdapter";

	public SampleAdapter(@NonNull Context context) {
		super(context);
	}

	public SampleAdapter(@NonNull Context context, @NonNull AdapterItem[] items) {
		super(context, items);
	}

	public SampleAdapter(@NonNull Context context, @NonNull List<AdapterItem> items) {
		super(context, items);
	}
}
