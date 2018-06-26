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

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import universum.studios.android.samples.recycler.data.model.AdapterItem;
import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;

/**
 * @author Martin Albedinsky
 */
public class SampleAdapter extends SimpleRecyclerAdapter<SampleAdapter, SampleViewHolder, AdapterItem> {

	public SampleAdapter(@NonNull final Context context) {
		super(context);
	}

	public SampleAdapter(@NonNull final Context context, @NonNull final AdapterItem[] items) {
		super(context, items);
	}

	public SampleAdapter(@NonNull final Context context, @NonNull final List<AdapterItem> items) {
		super(context, items);
	}
}