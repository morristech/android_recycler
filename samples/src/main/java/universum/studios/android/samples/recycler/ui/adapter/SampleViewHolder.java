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

	@SuppressWarnings("unused")
	private static final String TAG = "SampleViewHolder";

	@NonNull
	protected final VB binding;

	@SuppressWarnings("unchecked")
	protected SampleViewHolder(@NonNull View itemView) {
		this((VB) DataBindingUtil.bind(itemView));
	}

	protected SampleViewHolder(@NonNull VB viewBinding) {
		super(viewBinding.getRoot());
		this.binding = viewBinding;
	}

	@Override
	public void bind(@NonNull SampleAdapter adapter, int position, @Nullable List<Object> payloads) {
		binding.setVariable(BR.item, adapter.getItem(position));
		binding.executePendingBindings();
	}
}
