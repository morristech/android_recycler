package universum.studios.android.samples.recycler.ui.helper.swipe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import universum.studios.android.recycler.helper.ItemSwipeHelper;
import universum.studios.android.recycler.helper.RecyclerViewItemHelper;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.model.AdapterItem;
import universum.studios.android.samples.recycler.databinding.ItemListSwipeableBinding;
import universum.studios.android.samples.recycler.ui.adapter.SampleAdapter;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;

/**
 * @author Martin Albedinsky
 */
final class SampleSwipeAdapter extends SampleAdapter implements ItemSwipeHelper.SwipeAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = "SampleSwipeAdapter";

	SampleSwipeAdapter(@NonNull Context context, @NonNull List<AdapterItem> items) {
		super(context, items);
	}

	@Override
	public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_swipeable, parent));
	}

	@Override
	public int getItemSwipeFlags(int position) {
		return ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START | ItemSwipeHelper.END);
	}

	private static final class ItemHolder extends SampleViewHolder<ItemListSwipeableBinding> implements ItemSwipeHelper.SwipeViewHolder {

		private static final int ACTION_NONE = 0x00;
		private static final int ACTION_DONE = 0x01;
		private static final int ACTION_DELETE = 0x02;

		private final int actionColorDone, actionColorDelete;
		private int activeAction = ACTION_NONE;

		@SuppressWarnings("deprecation")
		ItemHolder(@NonNull View itemView) {
			super(ItemListSwipeableBinding.bind(itemView));
			final Resources resources = itemView.getResources();
			this.actionColorDone = resources.getColor(R.color.action_tint_done);
			this.actionColorDelete = resources.getColor(R.color.action_tint_delete);
		}

		@Nullable
		@Override
		public View getInteractiveView(@RecyclerViewItemHelper.Interaction int interaction) {
			return interaction == RecyclerViewItemHelper.ACTION_STATE_SWIPE ? binding.itemContentContainer : null;
		}

		@Override
		public void onSwipeStarted() {
			// Ignored.
		}

		@Override
		public void onSwipeFinished(@RecyclerViewItemHelper.Direction int direction) {
			this.activeAction = ACTION_NONE;
		}

		@Override
		public void onSwipeCanceled() {
			this.activeAction = ACTION_NONE;
		}

		@Override
		public void onDraw(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			if (isCurrentlyActive) {
				final View swipeView = binding.itemContentContainer;
				if (swipeView.getTranslationX() < 0 && activeAction != ACTION_DELETE) {
					setVisibleAction(activeAction = ACTION_DELETE);
				} else if (swipeView.getTranslationX() > 0 && activeAction != ACTION_DONE) {
					setVisibleAction(activeAction = ACTION_DONE);
				}
			}
		}

		private void setVisibleAction(int action) {
			switch (action) {
				case ACTION_DONE:
					binding.itemActionsContainer.setBackgroundColor(actionColorDone);
					binding.itemActionDone.setVisibility(View.VISIBLE);
					binding.itemActionDelete.setVisibility(View.INVISIBLE);
					break;
				case ACTION_DELETE:
					binding.itemActionsContainer.setBackgroundColor(actionColorDelete);
					binding.itemActionDelete.setVisibility(View.VISIBLE);
					binding.itemActionDone.setVisibility(View.INVISIBLE);
					break;
				case ACTION_NONE:
					binding.itemActionsContainer.setBackgroundColor(Color.WHITE);
					binding.itemActionDelete.setVisibility(View.INVISIBLE);
					binding.itemActionDone.setVisibility(View.INVISIBLE);
				default:
					break;
			}
		}

		@Override
		public void onDrawOver(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			// Ignored.
		}
	}
}
