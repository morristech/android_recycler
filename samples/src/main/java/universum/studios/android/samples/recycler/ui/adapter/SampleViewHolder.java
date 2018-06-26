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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import universum.studios.android.samples.recycler.BR;
import universum.studios.android.widget.adapter.holder.AdapterBindingHolder;
import universum.studios.android.widget.adapter.holder.RecyclerViewHolder;

/**
 * @author Martin Albedinsky
 */
public class SampleViewHolder<VB extends ViewDataBinding> extends RecyclerViewHolder implements AdapterBindingHolder<SampleAdapter> {

	@NonNull protected final VB binding;

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	protected SampleViewHolder(@NonNull View itemView) {
		this((VB) DataBindingUtil.bind(itemView));
	}

	protected SampleViewHolder(@NonNull final VB viewBinding) {
		super(viewBinding.getRoot());
		this.binding = viewBinding;
	}

	@Override public void bind(@NonNull final SampleAdapter adapter, final int position, @Nullable final List<Object> payloads) {
		binding.setVariable(BR.item, adapter.getItem(position));
		binding.executePendingBindings();
	}
}