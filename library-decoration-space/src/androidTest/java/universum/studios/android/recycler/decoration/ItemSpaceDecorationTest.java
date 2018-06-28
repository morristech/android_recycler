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
package universum.studios.android.recycler.decoration;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
public final class ItemSpaceDecorationTest extends InstrumentedTestCase {

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleRes() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space"
		));
		// Assert:
		assertThat(decoration.getHorizontalStart(), is(8));
		assertThat(decoration.getHorizontalEnd(), is(16));
		assertThat(decoration.getVerticalStart(), is(4));
		assertThat(decoration.getVerticalEnd(), is(2));
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipNone() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipNone"
		));
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipFirst() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipFirst"
		));
		// Assert:
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(false));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipLast() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipLast"
		));
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResSkipBoth() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.SkipBoth"
		));
		// Assert:
		assertThat(decoration.skipsFirst(), is(true));
		assertThat(decoration.skipsLast(), is(true));
	}

	@Test public void testInstantiationWithContextAttrsSetDefStyleAttrDefStyleResEmpty() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		// Act:
		final ItemSpaceDecoration decoration = new ItemSpaceDecoration(context, null, 0, TestResources.resourceIdentifier(
				context,
				TestResources.STYLE,
				"Test.ItemDecoration.Space.Empty"
		));
		// Assert:
		assertThat(decoration.skipsFirst(), is(false));
		assertThat(decoration.skipsLast(), is(false));
	}
}