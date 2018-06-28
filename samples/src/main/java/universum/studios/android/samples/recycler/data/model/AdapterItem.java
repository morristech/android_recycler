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
package universum.studios.android.samples.recycler.data.model;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import universum.studios.android.samples.model.SampleItem;

/**
 * @author Martin Albedinsky
 */
public final class AdapterItem extends SampleItem {

	AdapterItem(@NonNull final Builder builder) {
		super(builder);
	}

	public static final class Builder extends SampleItem.Builder<Builder> {

		public Builder(@NonNull final Resources resources) {
			super(resources);
		}

		@Override @NonNull public AdapterItem build() {
			return new AdapterItem(this);
		}
	}
}