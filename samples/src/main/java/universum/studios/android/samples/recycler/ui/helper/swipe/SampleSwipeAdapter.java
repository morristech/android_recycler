package universum.studios.android.samples.recycler.ui.helper.swipe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import universum.studios.android.recycler.helper.ItemSwipeHelper;
import universum.studios.android.recycler.helper.RecyclerViewItemHelper;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.model.AdapterItem;
import universum.studios.android.samples.recycler.databinding.ItemListSwipeableBinding;
import universum.studios.android.samples.recycler.ui.adapter.SampleAdapter;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;
import universum.studios.android.ui.util.ResourceUtils;

/**
 * @author Martin Albedinsky
 */
final class SampleSwipeAdapter extends SampleAdapter implements ItemSwipeHelper.SwipeAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = "SampleSwipeAdapter";

	static final int INTERACTION_HANDLING_VIA_DRAW = 0x00;
	static final int INTERACTION_HANDLING_VIA_SWIPE_VIEW = 0x01;

	@IntDef({
			INTERACTION_HANDLING_VIA_DRAW,
			INTERACTION_HANDLING_VIA_SWIPE_VIEW
	})
	@Retention(RetentionPolicy.SOURCE)
	@interface InteractionHandling {}

	private int mInteractionHandling = INTERACTION_HANDLING_VIA_DRAW;

	SampleSwipeAdapter(@NonNull Context context, @NonNull List<AdapterItem> items) {
		super(context, items);
		setHasStableIds(true);
	}

	void setInteractionHandling(@InteractionHandling int handling) {
		this.mInteractionHandling = handling;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ItemHolder(inflateView(R.layout.item_list_swipeable, parent));
	}

	@Override
	public void onBindViewHolder(@NonNull SampleViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
		super.onBindViewHolder(viewHolder, position, payloads);
		((ItemHolder) viewHolder).setInteractionHandling(mInteractionHandling);
	}

	@Override
	public int getItemSwipeFlags(int position) {
		return ItemSwipeHelper.makeSwipeFlags(ItemSwipeHelper.START | ItemSwipeHelper.END);
	}

	private static final class ItemHolder extends SampleViewHolder<ItemListSwipeableBinding> implements ItemSwipeHelper.SwipeViewHolder {

		private static final int ACTION_ICON_TINT = Color.WHITE;

		private static final int ACTION_NONE = 0x00;
		private static final int ACTION_DONE = 0x01;
		private static final int ACTION_DELETE = 0x02;

		private final Paint actionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private final int actionColorDone, actionColorDelete;
		private final Drawable actionDrawableDone, actionDrawableDelete;
		private final int actionDrawableSize;
		private final int actionDrawableOffset;
		private int activeAction = ACTION_NONE;
		private int interactionHandling = INTERACTION_HANDLING_VIA_DRAW;

		@SuppressWarnings({"deprecation", "ConstantConditions"})
		ItemHolder(@NonNull View itemView) {
			super(ItemListSwipeableBinding.bind(itemView));
			final Resources resources = itemView.getResources();
			this.actionColorDone = resources.getColor(R.color.action_tint_done);
			this.actionColorDelete = resources.getColor(R.color.action_tint_delete);
			this.actionDrawableDone = ResourceUtils.getVectorDrawable(resources, R.drawable.vc_ic_done_24dp, null);
			DrawableCompat.setTint(actionDrawableDone, ACTION_ICON_TINT);
			this.actionDrawableDelete = ResourceUtils.getVectorDrawable(resources, R.drawable.vc_ic_delete_24dp, null);
			DrawableCompat.setTint(actionDrawableDelete, ACTION_ICON_TINT);
			this.actionDrawableSize = actionDrawableDone.getIntrinsicWidth();
			this.actionDrawableOffset = resources.getDimensionPixelSize(R.dimen.ui_spacing_secondary);
			this.actionDrawableDone.setBounds(0, 0, actionDrawableSize, actionDrawableSize);
			this.actionDrawableDelete.setBounds(0, 0, actionDrawableSize, actionDrawableSize);
		}

		@Override
		public void bind(@NonNull SampleAdapter adapter, int position, @Nullable List<Object> payloads) {
			super.bind(adapter, position, payloads);
			setVisibleAction(activeAction);
		}

		void setInteractionHandling(@InteractionHandling int handling) {
			this.interactionHandling = handling;
		}

		@Nullable
		@Override
		public View getInteractiveView(@RecyclerViewItemHelper.Interaction int interaction) {
			if (interaction == ItemSwipeHelper.INTERACTION) {
				switch (interactionHandling) {
					case INTERACTION_HANDLING_VIA_SWIPE_VIEW: return binding.itemContentContainer;
					case INTERACTION_HANDLING_VIA_DRAW:
					default: return itemView;
				}
			}
			return null;
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
		@SuppressWarnings("ConstantConditions")
		public void onDraw(@NonNull Canvas canvas, float dX, float dY, @RecyclerViewItemHelper.Interaction int interaction, boolean isCurrentlyActive) {
			final View swipeView = getInteractiveView(interaction);
			switch (interactionHandling) {
				case INTERACTION_HANDLING_VIA_SWIPE_VIEW:
					if (isCurrentlyActive) {
						if (swipeView.getTranslationX() < 0 && activeAction != ACTION_DELETE) {
							setVisibleAction(activeAction = ACTION_DELETE);
						} else if (swipeView.getTranslationX() > 0 && activeAction != ACTION_DONE) {
							setVisibleAction(activeAction = ACTION_DONE);
						}
					}
					break;
				case INTERACTION_HANDLING_VIA_DRAW:
				default:
					int actionColor = Color.TRANSPARENT;
					Drawable actionIconDrawable = null;
					int actionIconOffset = 0;
					if (swipeView.getTranslationX() < 0) {
						actionColor = actionColorDelete;
						actionIconDrawable = actionDrawableDelete;
						actionIconOffset = itemView.getRight() - actionDrawableOffset - actionDrawableSize;
					} else if (swipeView.getTranslationX() > 0) {
						actionColor = actionColorDone;
						actionIconDrawable = actionDrawableDone;
						actionIconOffset = actionDrawableOffset;
					}
					if (actionColor != Color.TRANSPARENT) {
						// Draw action specific color in the background.
						actionPaint.setColor(actionColor);
						canvas.drawRect(
								itemView.getLeft(),
								itemView.getTop(),
								itemView.getRight(),
								itemView.getBottom(),
								actionPaint
						);
						// Draw action specific icon.
						canvas.save();
						canvas.translate(actionIconOffset, itemView.getTop() + itemView.getHeight() / 2f - actionDrawableSize / 2f);
						actionIconDrawable.draw(canvas);
						canvas.restore();
					}
					break;
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
			// Nothing to draw over.
		}
	}
}
