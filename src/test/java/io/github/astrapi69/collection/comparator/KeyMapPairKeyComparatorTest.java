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
package io.github.astrapi69.collection.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.pair.KeyMapPair;

/**
 * The unit test class for the class {@link KeyMapPairKeyComparator}.
 */
class KeyMapPairKeyComparatorTest
{

	/**
	 * The keys of two {@link KeyMapPair} objects, and the sign of their comparison
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair
	 * @param secondKey
	 *            the key of the second pair
	 * @param sign
	 *            the sign of the comparison of the first with the second pair
	 */
	record KeyCase(String description, String firstKey, String secondKey, int sign) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The keys and the sign of their comparison. The first pair always holds the map {z=1}, the
	 * second {a=1}, in the reverse order of the keys of the first case
	 *
	 * @return the cases
	 */
	static Stream<KeyCase> keys()
	{
		return Stream.of(new KeyCase("the smaller key first: negative", "a", "b", -1),
			new KeyCase("equal keys: zero, the map does not count", "a", "a", 0),
			new KeyCase("the greater key first: positive", "b", "a", 1));
	}

	/**
	 * {@link KeyMapPairKeyComparator#compare} orders two pairs by their keys alone, as the
	 * {@link Comparable#compareTo(Object)} of the key does
	 *
	 * @param testCase
	 *            the keys and the sign of their comparison
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("keys")
	void compareOrdersByTheKeyAlone(final KeyCase testCase)
	{
		final KeyMapPair<String, String, Integer> first = KeyMapPair
			.<String, String, Integer> builder().key(testCase.firstKey()).values(Map.of("z", 1))
			.build();
		final KeyMapPair<String, String, Integer> second = KeyMapPair
			.<String, String, Integer> builder().key(testCase.secondKey()).values(Map.of("a", 1))
			.build();
		final KeyMapPairKeyComparator<String, String, Integer> comparator = new KeyMapPairKeyComparator<>();
		assertEquals(testCase.sign(), Integer.signum(comparator.compare(first, second)));
		assertEquals(-testCase.sign(), Integer.signum(comparator.compare(second, first)));
	}

	/**
	 * A null key in one of the two pairs
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair, may be null
	 * @param secondKey
	 *            the key of the second pair, may be null
	 */
	record NullKeyCase(String description, String firstKey, String secondKey) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The pairs with a null key
	 *
	 * @return the cases
	 */
	static Stream<NullKeyCase> nullKeys()
	{
		return Stream.of(new NullKeyCase("the key of the first pair null", null, "a"),
			new NullKeyCase("the key of the second pair null", "a", null));
	}

	/**
	 * {@link KeyMapPairKeyComparator#compare} throws a {@link NullPointerException} for a null key,
	 * as the {@link Comparable#compareTo(Object)} of {@link String} does: the class compares keys
	 * that implement {@link Comparable} and checks no null
	 *
	 * @param testCase
	 *            the keys
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("nullKeys")
	void compareThrowsANullPointerExceptionForANullKey(final NullKeyCase testCase)
	{
		final KeyMapPair<String, String, Integer> first = KeyMapPair
			.<String, String, Integer> builder().key(testCase.firstKey()).values(Map.of("z", 1))
			.build();
		final KeyMapPair<String, String, Integer> second = KeyMapPair
			.<String, String, Integer> builder().key(testCase.secondKey()).values(Map.of("a", 1))
			.build();
		final KeyMapPairKeyComparator<String, String, Integer> comparator = new KeyMapPairKeyComparator<>();
		assertThrows(NullPointerException.class, () -> comparator.compare(first, second));
	}
}
