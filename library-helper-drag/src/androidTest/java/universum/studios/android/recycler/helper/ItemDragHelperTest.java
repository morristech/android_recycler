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
package universum.studios.android.recycler.helper;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ItemDragHelperTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ItemDragHelperTest";

	@Test
	public void testInteractionConstant() {
		assertThat(ItemDragHelper.INTERACTION, is(ItemTouchHelper.ACTION_STATE_DRAG));
	}

	@Test
	public void testMoveTresholdConstant() {
		assertThat(ItemDragHelper.MOVE_THRESHOLD, is(0.5f));
	}

	@Test
	public void testMakeDragFlags() {
		assertThat(
				ItemDragHelper.makeDragFlags(ItemDragHelper.UP),
				is(ItemTouchHelper.Callback.makeMovementFlags(ItemTouchHelper.UP, 0))
		);
		assertThat(
				ItemDragHelper.makeDragFlags(ItemDragHelper.START | ItemDragHelper.END),
				is(ItemTouchHelper.Callback.makeMovementFlags(ItemTouchHelper.START | ItemTouchHelper.END, 0))
		);
	}

	@Test
	public void testGetInteractor() {
		assertThat(new ItemDragHelper().getInteractor(), instanceOf(ItemDragHelper.Interactor.class));
	}
}
