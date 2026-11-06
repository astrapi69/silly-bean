/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.bean.enumeration.DecoratedEnum;
import io.github.astrapi69.bean.enumeration.ModeContext;
import io.github.astrapi69.collection.comparator.KeyValuePairKeyComparator;
import io.github.astrapi69.collection.pair.ImmutableBox;
import io.github.astrapi69.collection.pair.KeyValuePair;
import io.github.astrapi69.collection.pair.KeyValuesPair;
import io.github.astrapi69.collection.pair.Pair;
import io.github.astrapi69.collection.pair.Quattro;
import io.github.astrapi69.collection.pair.Triple;
import io.github.astrapi69.collection.pair.ValueBox;

/**
 * The examples of the README's Quick start, each block as a test, with the values its comments name
 * as assertions. A change here belongs into the README too, and the other way round. One
 * {@code @Test} per block and not a parameterized test, as the other test classes have for the
 * cases of one behaviour: each test mirrors a block of the README, whose statements test different
 * methods.
 */
class ReadmeExamplesTest
{

	/**
	 * README: "Pairs, triples and quattros"
	 */
	@Test
	void pairsTriplesAndQuattros()
	{
		Pair<String, Integer> pair = new Pair<>("one", 1);
		String left = pair.getLeftContent(); // "one"
		Integer right = pair.getRightContent(); // 1

		Triple<String, Integer, Boolean> triple = Triple.<String, Integer, Boolean> builder()
			.left("a").middle(2).right(true).build();
		// a new triple a, 3, true
		Triple<String, Integer, Boolean> changed = triple.toBuilder().middle(3).build();
		Integer middle = triple.getMiddle(); // 2: toBuilder leaves the triple as it is

		Quattro<String, String, String, String> corners = Quattro
			.<String, String, String, String> builder().topLeft("nw").topRight("ne")
			.bottomLeft("sw").bottomRight("se").build();
		String northEast = corners.getTopRight(); // "ne"

		assertEquals("one", left);
		assertEquals(1, right);
		assertEquals(new Triple<>("a", 3, true), changed);
		assertEquals(2, middle);
		assertEquals("ne", northEast);
	}

	/**
	 * README: "A key with its values"
	 */
	@Test
	void aKeyWithItsValues()
	{
		Map<String, Integer> numbers = new LinkedHashMap<>();
		numbers.put("one", 1);
		numbers.put("two", 2);
		// [one=1, two=2], in the order of the map
		List<KeyValuePair<String, Integer>> pairs = KeyValuePair.toKeyValuePairs(numbers);
		Map<String, Integer> map = KeyValuePair.toMap(pairs); // {one=1, two=2}, a HashMap

		KeyValuesPair<String, String> vowels = KeyValuesPair.<String, String> builder()
			.key("vowels").value("a").value("e").build();
		List<String> values = new ArrayList<>(vowels.getValues()); // [a, e]

		assertEquals(List.of("one", "two"), pairs.stream().map(KeyValuePair::getKey).toList());
		assertEquals(List.of(1, 2), pairs.stream().map(KeyValuePair::getValue).toList());
		assertEquals(Map.of("one", 1, "two", 2), map);
		assertSame(HashMap.class, map.getClass());
		assertEquals("vowels", vowels.getKey());
		assertEquals(List.of("a", "e"), values);
	}

	/**
	 * README: "Sorting by the key"
	 */
	@Test
	void sortingByTheKey()
	{
		List<KeyValuePair<String, Integer>> list = new ArrayList<>();
		list.add(new KeyValuePair<>("b", 1));
		list.add(new KeyValuePair<>("a", 2));
		list.sort(new KeyValuePairKeyComparator<>()); // a=2, b=1

		assertEquals(List.of("a", "b"), list.stream().map(KeyValuePair::getKey).toList());
		assertEquals(List.of(2, 1), list.stream().map(KeyValuePair::getValue).toList());
	}

	/**
	 * README: "Boxes"
	 */
	@Test
	void boxes()
	{
		ValueBox<String> box = new ValueBox<>("first");
		box.setValue("second"); // a ValueBox changes its value
		ImmutableBox<String> fixed = new ImmutableBox<>("only"); // an ImmutableBox has no setter
		ImmutableBox<String> other = fixed.toBuilder().value("other").build(); // a new box

		assertEquals("second", box.getValue());
		assertEquals("only", fixed.getValue());
		assertEquals("other", other.getValue());
	}

	/**
	 * README: "An enum with a value it does not have yet"
	 */
	@Test
	void anEnumWithAValueItDoesNotHaveYet()
	{
		DecoratedEnum<ModeContext, String> known = new DecoratedEnum<>(ModeContext.CREATE, null);
		String knownName = known.name(); // "CREATE"
		DecoratedEnum<ModeContext, String> added = new DecoratedEnum<>(null, "ARCHIVE");
		String addedName = added.name(); // "ARCHIVE", a mode ModeContext does not have

		assertEquals("CREATE", knownName);
		assertEquals("ARCHIVE", addedName);
	}
}
