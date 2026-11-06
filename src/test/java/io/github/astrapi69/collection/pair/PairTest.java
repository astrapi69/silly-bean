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
package io.github.astrapi69.collection.pair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link Pair}. The cases hold the contents only; every test
 * builds its pairs itself, so that PIT gives each case the code it runs
 */
public class PairTest
{

	/**
	 * The hash code the formula of {@link Pair#hashCode()} takes for a null content
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link Pair#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given content as {@link Pair#hashCode()} takes it
	 *
	 * @param content
	 *            the content, may be null
	 * @return the hash code of the content, or 43 for null
	 */
	private static int hash(final Object content)
	{
		return content == null ? NULL_HASH : content.hashCode();
	}

	/**
	 * The contents of two pairs, and whether the pairs are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstLeft
	 *            the left content of the first pair, may be null
	 * @param firstRight
	 *            the right content of the first pair, may be null
	 * @param secondLeft
	 *            the left content of the second pair, may be null
	 * @param secondRight
	 *            the right content of the second pair, may be null
	 * @param equal
	 *            true if the pairs are equal
	 */
	record EqualsCase(String description, String firstLeft, String firstRight, String secondLeft,
		String secondRight, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two pairs are equal if their left contents are equal and their right contents are equal, null
	 * equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(new EqualsCase("the same contents: equal", "a", "b", "a", "b", true),
			new EqualsCase("both contents null in both: equal", null, null, null, null, true),
			new EqualsCase("another left content: not equal", "a", "b", "x", "b", false),
			new EqualsCase("another right content: not equal", "a", "b", "a", "x", false),
			new EqualsCase("left null in the first only: not equal", null, "b", "a", "b", false),
			new EqualsCase("left null in the second only: not equal", "a", "b", null, "b", false),
			new EqualsCase("right null in the first only: not equal", "a", null, "a", "b", false),
			new EqualsCase("right null in the second only: not equal", "a", "b", "a", null, false),
			new EqualsCase("left and right swapped: not equal", "a", "b", "b", "a", false));
	}

	/**
	 * {@link Pair#equals(Object)} compares the left and the right content, both ways round; equal
	 * pairs have the same {@link Pair#hashCode()}, and for these contents unequal pairs have
	 * different ones
	 *
	 * @param testCase
	 *            the contents and whether the pairs are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesLeftAndRightContent(final EqualsCase testCase)
	{
		final Pair<String, String> first = new Pair<>(testCase.firstLeft(), testCase.firstRight());
		final Pair<String, String> second = Pair.<String, String> builder()
			.leftContent(testCase.secondLeft()).rightContent(testCase.secondRight()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * A {@link Pair} whose {@link Pair#canEqual(Object)} accepts only its own class, as a subclass
	 * that adds a field to {@link Pair#equals(Object)} would
	 */
	static class OwnClassPair extends Pair<String, String>
	{
		private static final long serialVersionUID = 1L;

		OwnClassPair(final String leftContent, final String rightContent)
		{
			super(leftContent, rightContent);
		}

		@Override
		protected boolean canEqual(final Object other)
		{
			return other instanceof OwnClassPair;
		}
	}

	/**
	 * An object to compare a pair of "a" and "b" with, and whether it is equal to the pair
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param equal
	 *            true if the pair equals the object
	 */
	record OtherCase(String description, Function<Pair<String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a pair of other contents
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the pair itself: equal", pair -> pair, true),
			new OtherCase("null: not equal", pair -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				pair -> pair.toString(), false),
			new OtherCase("a subclass of the same contents: equal",
				pair -> new Pair<String, String>(pair.getLeftContent(), pair.getRightContent())
				{
					private static final long serialVersionUID = 1L;
				}, true),
			new OtherCase("a subclass whose canEqual refuses a Pair: not equal",
				pair -> new OwnClassPair(pair.getLeftContent(), pair.getRightContent()), false));
	}

	/**
	 * {@link Pair#equals(Object)} of a pair and another object
	 *
	 * @param testCase
	 *            the object and whether the pair equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final Pair<String, String> pair = new Pair<>("a", "b");
		assertEquals(testCase.equal(), pair.equals(testCase.other().apply(pair)));
	}

	/**
	 * An object to give to {@link Pair#canEqual(Object)}, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param canEqual
	 *            the result of canEqual
	 */
	record CanEqualCase(String description, Function<Pair<String, String>, Object> other,
		boolean canEqual) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * {@link Pair#canEqual(Object)} accepts any {@link Pair}
	 *
	 * @return the cases
	 */
	static Stream<CanEqualCase> canEqualCases()
	{
		return Stream.of(new CanEqualCase("a pair: true", pair -> new Pair<>("x", "y"), true),
			new CanEqualCase("a subclass: true", pair -> new OwnClassPair("x", "y"), true),
			new CanEqualCase("a String: false", pair -> "a", false),
			new CanEqualCase("null: false", pair -> null, false));
	}

	/**
	 * {@link Pair#canEqual(Object)} is true for an instance of {@link Pair} only
	 *
	 * @param testCase
	 *            the object and the result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("canEqualCases")
	void canEqualAcceptsAPair(final CanEqualCase testCase)
	{
		final Pair<String, String> pair = new Pair<>("a", "b");
		assertEquals(testCase.canEqual(), pair.canEqual(testCase.other().apply(pair)));
	}

	/**
	 * The contents of a pair, its {@link Pair#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param left
	 *            the left content, may be null
	 * @param right
	 *            the right content, may be null
	 * @param pairText
	 *            the toString of the pair
	 * @param builderText
	 *            the toString of a builder of these contents
	 */
	record ContentCase(String description, String left, String right, String pairText,
		String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs with and without contents
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("both contents", "a", "b", "Pair(leftContent=a, rightContent=b)",
				"Pair.PairBuilder(leftContent=a, rightContent=b)"),
			new ContentCase("the left content null", null, "b",
				"Pair(leftContent=null, rightContent=b)",
				"Pair.PairBuilder(leftContent=null, rightContent=b)"),
			new ContentCase("the right content null", "a", null,
				"Pair(leftContent=a, rightContent=null)",
				"Pair.PairBuilder(leftContent=a, rightContent=null)"),
			new ContentCase("both contents null", null, null,
				"Pair(leftContent=null, rightContent=null)",
				"Pair.PairBuilder(leftContent=null, rightContent=null)"));
	}

	/**
	 * {@link Pair#hashCode()} is the formula Lombok writes, {@code (59 + hash(left)) * 59 +
	 * hash(right)} with 43 for null; the contract of {@link Object#hashCode()} would allow any
	 * value that agrees with equals, the test pins this one so that a change shows
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfBothContents(final ContentCase testCase)
	{
		final Pair<String, String> pair = new Pair<>(testCase.left(), testCase.right());
		assertEquals((PRIME + hash(testCase.left())) * PRIME + hash(testCase.right()),
			pair.hashCode());
	}

	/**
	 * {@link Pair#toString()} and {@link Pair.PairBuilder#toString()} name both contents
	 *
	 * @param testCase
	 *            the contents and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesBothContents(final ContentCase testCase)
	{
		final Pair.PairBuilder<String, String> builder = Pair.<String, String> builder()
			.leftContent(testCase.left()).rightContent(testCase.right());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.pairText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link Pair#toBuilder()} give the same contents, and the
	 * getters return them
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepTheContents(final ContentCase testCase)
	{
		final Pair<String, String> built = Pair.<String, String> builder()
			.leftContent(testCase.left()).rightContent(testCase.right()).build();
		final Pair<String, String> constructed = new Pair<>(testCase.left(), testCase.right());
		final Pair<String, String> copy = constructed.toBuilder().build();
		assertEquals(testCase.left(), built.getLeftContent());
		assertEquals(testCase.right(), built.getRightContent());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link Pair#toBuilder()} starts from the contents of the pair and leaves the pair as it is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final Pair<String, String> pair = new Pair<>("a", "b");
		final Pair<String, String> changed = pair.toBuilder().rightContent("c").build();
		assertEquals(new Pair<>("a", "c"), changed);
		assertEquals(new Pair<>("a", "b"), pair);
	}

	/**
	 * The no-argument constructor leaves both contents null, and the setters set them
	 */
	@Test
	void settersSetTheContentsOfAnEmptyPair()
	{
		final Pair<String, String> pair = new Pair<>();
		assertNull(pair.getLeftContent());
		assertNull(pair.getRightContent());
		pair.setLeftContent("a");
		pair.setRightContent("b");
		assertEquals("a", pair.getLeftContent());
		assertEquals("b", pair.getRightContent());
	}

	/**
	 * A {@link Pair} is {@link java.io.Serializable}: a serialized and read pair equals the pair
	 *
	 * @throws IOException
	 *             if the pair cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read pair is not found
	 */
	@Test
	void aSerializedPairReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final Pair<String, String> pair = new Pair<>("a", "b");
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes))
		{
			out.writeObject(pair);
		}
		try (ObjectInputStream in = new ObjectInputStream(
			new ByteArrayInputStream(bytes.toByteArray())))
		{
			assertEquals(pair, in.readObject());
		}
	}

	/**
	 * Test method for {@link Pair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Pair.class);
	}

}
