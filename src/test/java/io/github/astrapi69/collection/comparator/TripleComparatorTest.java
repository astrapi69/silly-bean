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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.pair.Triple;

/**
 * The unit test class for the class {@link TripleComparator}. The cases hold the contents only; the
 * test builds the triples itself, so that PIT gives each case the code it runs
 */
public class TripleComparatorTest
{

	/**
	 * The contents of two triples, and the sign of their comparison
	 *
	 * @param description
	 *            the name of the case
	 * @param firstLeft
	 *            the left content of the first triple, may be null
	 * @param firstMiddle
	 *            the middle content of the first triple, may be null
	 * @param firstRight
	 *            the right content of the first triple, may be null
	 * @param secondLeft
	 *            the left content of the second triple, may be null
	 * @param secondMiddle
	 *            the middle content of the second triple, may be null
	 * @param secondRight
	 *            the right content of the second triple, may be null
	 * @param sign
	 *            the sign of the comparison of the first with the second triple
	 */
	record TripleCase(String description, String firstLeft, String firstMiddle, String firstRight,
		String secondLeft, String secondMiddle, String secondRight, int sign) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Triples that differ in one position or more
	 *
	 * @return the cases
	 */
	static Stream<TripleCase> triples()
	{
		return Stream.of(new TripleCase("the same contents: zero", "a", "b", "c", "a", "b", "c", 0),
			new TripleCase("all null in both: zero", null, null, null, null, null, null, 0),
			new TripleCase("the smaller left first: negative", "a", "b", "c", "b", "b", "c", -1),
			new TripleCase("the same left, the smaller middle first: negative", "a", "a", "c", "a",
				"b", "c", -1),
			new TripleCase("the same left and middle, the smaller right first: negative", "a", "b",
				"a", "a", "b", "b", -1),
			new TripleCase("the smaller left wins over a greater middle and right: negative", "a",
				"z", "z", "b", "a", "a", -1),
			new TripleCase("the smaller middle wins over a greater right: negative", "a", "a", "z",
				"a", "b", "a", -1),
			new TripleCase("a null left before a value: negative", null, "b", "c", "a", "b", "c",
				-1),
			new TripleCase("a null middle before a value: negative", "a", null, "c", "a", "b", "c",
				-1),
			new TripleCase("a null right before a value: negative", "a", "b", null, "a", "b", "c",
				-1));
	}

	/**
	 * {@link TripleComparator#compare(Triple, Triple)} orders by the left content, then by the
	 * middle, then by the right, null before a value, as commons-lang3's
	 * {@link org.apache.commons.lang3.builder.CompareToBuilder} does; the other way round the sign
	 * turns
	 *
	 * @param testCase
	 *            the contents and the sign
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("triples")
	void compareOrdersByLeftThenMiddleThenRight(final TripleCase testCase)
	{
		final Triple<String, String, String> first = Triple.<String, String, String> builder()
			.left(testCase.firstLeft()).middle(testCase.firstMiddle()).right(testCase.firstRight())
			.build();
		final Triple<String, String, String> second = Triple.<String, String, String> builder()
			.left(testCase.secondLeft()).middle(testCase.secondMiddle())
			.right(testCase.secondRight()).build();
		final TripleComparator<String, String, String> comparator = new TripleComparator<>();
		assertEquals(testCase.sign(), Integer.signum(comparator.compare(first, second)));
		assertEquals(-testCase.sign(), Integer.signum(comparator.compare(second, first)));
	}

	/**
	 * Test method for {@link TripleComparator}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(TripleComparator.class);
	}

}
