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

import android.support.annotation.NonNull;

import java.util.List;

import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public final class SampleViewHolderBinder implements AdapterHolder.Binder<SampleAdapter, SampleViewHolder> {

	private SampleViewHolderBinder() {}

	@NonNull public static SampleViewHolderBinder create() {
		return new SampleViewHolderBinder();
	}

	@Override public void bindHolder(
			@NonNull final SampleAdapter adapter,
			@NonNull final SampleViewHolder holder,
			final int position,
			@NonNull final List<Object> payloads
	) {
		holder.bind(adapter, position, payloads);
	}
}