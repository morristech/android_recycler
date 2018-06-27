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
package universum.studios.android.recycler.helper;

import android.support.v7.widget.helper.ItemTouchHelper;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author Martin Albedinsky
 */
public final class ItemDragHelperTest extends RobolectricTestCase {

	@Test public void testConstants() {
		// Assert:
		assertThat(ItemDragHelper.INTERACTION, is(ItemTouchHelper.ACTION_STATE_DRAG));
		assertThat(ItemDragHelper.MOVE_THRESHOLD, is(0.5f));
	}

	@Test public void testMakeDragFlags() {
		// Act + Assert:
		assertThat(
				ItemDragHelper.makeDragFlags(ItemDragHelper.UP),
				is(ItemTouchHelper.Callback.makeMovementFlags(ItemTouchHelper.UP, 0))
		);
		assertThat(
				ItemDragHelper.makeDragFlags(ItemDragHelper.START | ItemDragHelper.END),
				is(ItemTouchHelper.Callback.makeMovementFlags(ItemTouchHelper.START | ItemTouchHelper.END, 0))
		);
	}

	@Test public void testInstantiation() {
		// Act:
		final ItemDragHelper helper = new ItemDragHelper();
		// Act + Assert:
		assertThat(helper.getInteractor(), instanceOf(ItemDragHelper.Interactor.class));
	}
}