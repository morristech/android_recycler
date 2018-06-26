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
package universum.studios.android.samples.recycler.ui.helper.drag;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import universum.studios.android.recycler.helper.ItemDragHelper;
import universum.studios.android.recycler.helper.RecyclerViewItemHelper;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.model.AdapterItem;
import universum.studios.android.samples.recycler.databinding.ItemListDragableBinding;
import universum.studios.android.samples.recycler.ui.adapter.SampleAdapter;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;

/**
 * @author Martin Albedinsky
 */
final class SampleDragAdapter extends SampleAdapter implements ItemDragHelper.DragAdapter {

	static final int ACTION_DRAG_INITIATE = 0x01;

	SampleDragAdapter(@NonNull final Context context, @NonNull final List<AdapterItem> items) {
		super(context, items);
	}

	@Override public SampleViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_dragable, parent));
	}

	@Override public int getItemDragFlags(final int position) {
		return ItemDragHelper.makeDragFlags(ItemDragHelper.UP | ItemDragHelper.DOWN);
	}

	@Override public void onItemDragStarted(int position) {
		// Ignored.
	}

	@Override public boolean onMoveItem(final int currentPosition, final int targetPosition) {
		moveItem(currentPosition, targetPosition);
		return true;
	}

	@Override public boolean canDropItemOver(final int currentPosition, final int targetPosition) {
		return targetPosition < getItemCount();
	}

	@Override public void onItemDragFinished(final int fromPosition, final int toPosition) {
		if (fromPosition < toPosition) {
			notifyItemRangeChanged(fromPosition, toPosition - fromPosition + 1);
		} else {
			notifyItemChanged(fromPosition);
			notifyItemChanged(toPosition);
		}
	}

	private final class ItemHolder extends SampleViewHolder<ItemListDragableBinding>
			implements
			ItemDragHelper.DragViewHolder,
			View.OnLongClickListener {

		ItemHolder(@NonNull final View itemView) {
			super(ItemListDragableBinding.bind(itemView));
			binding.dragHandle.setOnLongClickListener(this);
		}

		@Override public boolean onLongClick(@NonNull final View view) {
			return notifyDataSetActionSelected(ACTION_DRAG_INITIATE, getAdapterPosition(), this);
		}

		@Override @Nullable public View getInteractiveView(@RecyclerViewItemHelper.Interaction int interaction) {
			return null;
		}

		@Override public void onDragStarted() {
			this.itemView.setAlpha(0.5f);
		}

		@Override public void onDragFinished(final int fromPosition, final int toPosition) {
			this.itemView.setAlpha(1.0f);
		}

		@Override public void onDragCanceled() {
			this.itemView.setAlpha(1.0f);
		}

		@Override public void onDraw(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			// Ignored.
		}

		@Override public void onDrawOver(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			// Ignored.
		}
	}
}