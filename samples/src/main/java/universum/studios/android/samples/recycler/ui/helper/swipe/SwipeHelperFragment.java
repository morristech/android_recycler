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
package universum.studios.android.samples.recycler.ui.helper.swipe;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import universum.studios.android.recycler.helper.ItemSwipeHelper;
import universum.studios.android.recycler.helper.RecyclerViewItemHelper;
import universum.studios.android.samples.recycler.R;
import universum.studios.android.samples.recycler.data.AdapterItems;
import universum.studios.android.samples.recycler.ui.RecyclerSampleFragment;
import universum.studios.android.samples.recycler.ui.adapter.SampleViewHolder;
import universum.studios.android.ui.util.ResourceUtils;
import universum.studios.android.widget.adapter.holder.AdapterHolder;

/**
 * @author Martin Albedinsky
 */
public class SwipeHelperFragment extends RecyclerSampleFragment<SampleSwipeAdapter> implements ItemSwipeHelper.OnSwipeListener {

	@SuppressWarnings("unused") private static final String TAG = "SwipeHelperFragment";

	private ItemSwipeHelper swipeHelper;

	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override @NonNull protected SampleSwipeAdapter createAdapterWithHolderFactory(@NonNull final AdapterHolder.Factory<SampleViewHolder> factory) {
		final SampleSwipeAdapter adapter = new SampleSwipeAdapter(getActivity(), AdapterItems.createSampleList(getResources()));
		adapter.setHolderFactory(factory);
		adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

			@Override
			public void onItemRangeRemoved(int positionStart, int itemCount) {
				Log.d(TAG, "onItemRangeRemoved(positionStart: " + positionStart + ", itemCount: " + itemCount + ", remainingItemCount: " + adapter.getItemCount() + ")");
			}
		});
		return adapter;
	}

	@Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.collectionView.setItemAnimator(new ItemSwipeHelper.SwipeItemAnimator());
		this.swipeHelper = new ItemSwipeHelper();
		this.swipeHelper.getInteractor().addOnSwipeListener(this);
		this.swipeHelper.attachToRecyclerView(collectionView);
	}

	@SuppressWarnings("ConstantConditions")
	@Override public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.recycler_helper_swipe, menu);
		final Drawable refreshIcon = ResourceUtils.getVectorDrawable(
				getResources(),
				R.drawable.vc_ic_refresh_24dp,
				getActivity().getTheme()
		);
		DrawableCompat.setTint(refreshIcon, Color.WHITE);
		menu.findItem(R.id.menu_refresh).setIcon(refreshIcon);
	}

	@Override public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				adapter.changeItems(AdapterItems.createSampleList(getResources()));
				return true;
			case R.id.menu_enabled:
				item.setChecked(!item.isChecked());
				swipeHelper.getInteractor().setEnabled(item.isChecked());
				return true;
			case R.id.menu_swipe_interaction_via_draw:
				item.setChecked(true);
				adapter.setInteractionHandling(SampleSwipeAdapter.INTERACTION_HANDLING_VIA_DRAW);
				return true;
			case R.id.menu_swipe_interaction_via_swipe_view:
				item.setChecked(true);
				adapter.setInteractionHandling(SampleSwipeAdapter.INTERACTION_HANDLING_VIA_SWIPE_VIEW);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override public void onSwipeStarted(@NonNull final ItemSwipeHelper swipeHelper, @NonNull final RecyclerView.ViewHolder viewHolder) {
		Log.d(TAG, "Swipe STARTED for position(" + viewHolder.getAdapterPosition() + ").");
	}

	@Override public void onSwipeFinished(
			@NonNull final ItemSwipeHelper swipeHelper,
			@NonNull final RecyclerView.ViewHolder viewHolder,
			@RecyclerViewItemHelper.Direction final int direction
	) {
		Log.d(TAG, "Swipe FINISHED for position(" + viewHolder.getAdapterPosition() + ").");
		final int itemPosition = viewHolder.getAdapterPosition();
		switch (direction) {
			// Delete item.
			case ItemSwipeHelper.START:
				adapter.removeItem(itemPosition);
				break;
			// Done with the item.
			case ItemSwipeHelper.END:
				adapter.removeItem(itemPosition);
				break;
		}
	}

	@Override public void onSwipeCanceled(@NonNull final ItemSwipeHelper swipeHelper, @NonNull final RecyclerView.ViewHolder viewHolder) {
		Log.d(TAG, "Swipe CANCELED for position(" + viewHolder.getAdapterPosition() + ").");
		swipeHelper.restoreHolder(viewHolder, ItemSwipeHelper.START);
	}
}