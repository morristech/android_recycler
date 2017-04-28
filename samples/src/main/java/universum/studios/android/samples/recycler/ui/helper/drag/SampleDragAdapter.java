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

	@SuppressWarnings("unused")
	private static final String TAG = "SampleDragAdapter";

	static final int ACTION_DRAG_INITIATE = 0x01;

	SampleDragAdapter(@NonNull Context context, @NonNull List<AdapterItem> items) {
		super(context, items);
	}

	@Override
	public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_dragable, parent));
	}

	@Override
	public int getItemDragFlags(int position) {
		return ItemDragHelper.makeDragFlags(ItemDragHelper.UP | ItemDragHelper.DOWN);
	}

	@Override
	public void onItemDragStarted(int position) {
		// Ignored.
	}

	@Override
	public boolean onMoveItem(int currentPosition, int targetPosition) {
		moveItem(currentPosition, targetPosition);
		return true;
	}

	@Override
	public boolean canDropItemOver(int currentPosition, int targetPosition) {
		return targetPosition < getItemCount();
	}

	@Override
	public void onItemDragFinished(int fromPosition, int toPosition) {
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

		ItemHolder(@NonNull View itemView) {
			super(ItemListDragableBinding.bind(itemView));
			binding.dragHandle.setOnLongClickListener(this);
		}

		@Override
		public boolean onLongClick(@NonNull View view) {
			return notifyDataSetActionSelected(ACTION_DRAG_INITIATE, getAdapterPosition(), this);
		}

		@Nullable
		@Override
		public View getInteractiveView(@RecyclerViewItemHelper.Interaction int interaction) {
			return null;
		}

		@Override
		public void onDragStarted() {
			itemView.setAlpha(0.5f);
		}

		@Override
		public void onDragFinished(int fromPosition, int toPosition) {
			itemView.setAlpha(1.0f);
		}

		@Override
		public void onDragCanceled() {
			itemView.setAlpha(1.0f);
		}

		@Override
		public void onDraw(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			// Ignored.
		}

		@Override
		public void onDrawOver(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			// Ignored.
		}
	}
}
