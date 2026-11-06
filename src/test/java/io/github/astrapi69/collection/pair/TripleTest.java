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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link Triple}. The cases hold the contents only; every test
 * builds its triples itself, so that PIT gives each case the code it runs
 */
public class TripleTest
{

	/**
	 * The hash code the formula of {@link Triple#hashCode()} takes for a null content
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link Triple#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given content as {@link Triple#hashCode()} takes it
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
	 * The contents of two triples, and whether the triples are equal
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
	 * @param equal
	 *            true if the triples are equal
	 */
	record EqualsCase(String description, String firstLeft, String firstMiddle, String firstRight,
		String secondLeft, String secondMiddle, String secondRight, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two triples are equal if their left, middle and right contents are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same contents: equal", "a", "b", "c", "a", "b", "c", true),
			new EqualsCase("all contents null in both: equal", null, null, null, null, null, null,
				true),
			new EqualsCase("another left: not equal", "a", "b", "c", "x", "b", "c", false),
			new EqualsCase("another middle: not equal", "a", "b", "c", "a", "x", "c", false),
			new EqualsCase("another right: not equal", "a", "b", "c", "a", "b", "x", false),
			new EqualsCase("left null in the first only: not equal", null, "b", "c", "a", "b", "c",
				false),
			new EqualsCase("left null in the second only: not equal", "a", "b", "c", null, "b", "c",
				false),
			new EqualsCase("middle null in the first only: not equal", "a", null, "c", "a", "b",
				"c", false),
			new EqualsCase("middle null in the second only: not equal", "a", "b", "c", "a", null,
				"c", false),
			new EqualsCase("right null in the first only: not equal", "a", "b", null, "a", "b", "c",
				false),
			new EqualsCase("right null in the second only: not equal", "a", "b", "c", "a", "b",
				null, false),
			new EqualsCase("left and right swapped: not equal", "a", "b", "c", "c", "b", "a",
				false));
	}

	/**
	 * {@link Triple#equals(Object)} compares the left, the middle and the right content, both ways
	 * round; equal triples have the same {@link Triple#hashCode()}, and for these contents unequal
	 * triples have different ones
	 *
	 * @param testCase
	 *            the contents and whether the triples are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesLeftMiddleAndRight(final EqualsCase testCase)
	{
		final Triple<String, String, String> first = new Triple<>(testCase.firstLeft(),
			testCase.firstMiddle(), testCase.firstRight());
		final Triple<String, String, String> second = Triple.<String, String, String> builder()
			.left(testCase.secondLeft()).middle(testCase.secondMiddle())
			.right(testCase.secondRight()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * A {@link Triple} whose {@link Triple#canEqual(Object)} accepts only its own class, as a
	 * subclass that adds a field to {@link Triple#equals(Object)} would
	 */
	static class OwnClassTriple extends Triple<String, String, String>
	{
		private static final long serialVersionUID = 1L;

		OwnClassTriple(final String left, final String middle, final String right)
		{
			super(left, middle, right);
		}

		@Override
		protected boolean canEqual(final Object other)
		{
			return other instanceof OwnClassTriple;
		}
	}

	/**
	 * An object to compare a triple of "a", "b" and "c" with, and whether it is equal to the triple
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the triple in the test
	 * @param equal
	 *            true if the triple equals the object
	 */
	record OtherCase(String description, Function<Triple<String, String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a triple of other contents
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the triple itself: equal", triple -> triple, true),
			new OtherCase("null: not equal", triple -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				triple -> triple.toString(), false),
			new OtherCase("a subclass of the same contents: equal",
				triple -> new Triple<String, String, String>(triple.getLeft(), triple.getMiddle(),
					triple.getRight())
				{
					private static final long serialVersionUID = 1L;
				}, true),
			new OtherCase("a subclass whose canEqual refuses a Triple: not equal",
				triple -> new OwnClassTriple(triple.getLeft(), triple.getMiddle(),
					triple.getRight()),
				false));
	}

	/**
	 * {@link Triple#equals(Object)} of a triple and another object
	 *
	 * @param testCase
	 *            the object and whether the triple equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final Triple<String, String, String> triple = new Triple<>("a", "b", "c");
		assertEquals(testCase.equal(), triple.equals(testCase.other().apply(triple)));
	}

	/**
	 * An object to give to {@link Triple#canEqual(Object)}, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the triple in the test
	 * @param canEqual
	 *            the result of canEqual
	 */
	record CanEqualCase(String description, Function<Triple<String, String, String>, Object> other,
		boolean canEqual) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * {@link Triple#canEqual(Object)} accepts any {@link Triple}
	 *
	 * @return the cases
	 */
	static Stream<CanEqualCase> canEqualCases()
	{
		return Stream
			.of(new CanEqualCase("a triple: true", triple -> new Triple<>("x", "y", "z"), true),
				new CanEqualCase("a subclass: true", triple -> new OwnClassTriple("x", "y", "z"),
					true),
				new CanEqualCase("a Pair: false", triple -> new Pair<>("x", "y"), false),
				new CanEqualCase("a String: false", triple -> "a", false),
				new CanEqualCase("null: false", triple -> null, false));
	}

	/**
	 * {@link Triple#canEqual(Object)} is true for an instance of {@link Triple} only
	 *
	 * @param testCase
	 *            the object and the result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("canEqualCases")
	void canEqualAcceptsATriple(final CanEqualCase testCase)
	{
		final Triple<String, String, String> triple = new Triple<>("a", "b", "c");
		assertEquals(testCase.canEqual(), triple.canEqual(testCase.other().apply(triple)));
	}

	/**
	 * The contents of a triple, its {@link Triple#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param left
	 *            the left content, may be null
	 * @param middle
	 *            the middle content, may be null
	 * @param right
	 *            the right content, may be null
	 * @param tripleText
	 *            the toString of the triple
	 * @param builderText
	 *            the toString of a builder of these contents
	 */
	record ContentCase(String description, String left, String middle, String right,
		String tripleText, String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Triples with and without contents
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("all contents", "a", "b", "c", "Triple(left=a, middle=b, right=c)",
				"Triple.TripleBuilder(left=a, middle=b, right=c)"),
			new ContentCase("the left null", null, "b", "c", "Triple(left=null, middle=b, right=c)",
				"Triple.TripleBuilder(left=null, middle=b, right=c)"),
			new ContentCase("the middle null", "a", null, "c",
				"Triple(left=a, middle=null, right=c)",
				"Triple.TripleBuilder(left=a, middle=null, right=c)"),
			new ContentCase("the right null", "a", "b", null,
				"Triple(left=a, middle=b, right=null)",
				"Triple.TripleBuilder(left=a, middle=b, right=null)"),
			new ContentCase("all contents null", null, null, null,
				"Triple(left=null, middle=null, right=null)",
				"Triple.TripleBuilder(left=null, middle=null, right=null)"));
	}

	/**
	 * {@link Triple#hashCode()} is the formula Lombok writes,
	 * {@code ((59 + hash(left)) * 59 + hash(middle)) * 59 + hash(right)} with 43 for null; the
	 * contract of {@link Object#hashCode()} would allow any value that agrees with equals, the test
	 * pins this one so that a change shows
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfTheThreeContents(final ContentCase testCase)
	{
		final Triple<String, String, String> triple = new Triple<>(testCase.left(),
			testCase.middle(), testCase.right());
		assertEquals(((PRIME + hash(testCase.left())) * PRIME + hash(testCase.middle())) * PRIME
			+ hash(testCase.right()), triple.hashCode());
	}

	/**
	 * {@link Triple#toString()} and {@link Triple.TripleBuilder#toString()} name the three contents
	 *
	 * @param testCase
	 *            the contents and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesTheThreeContents(final ContentCase testCase)
	{
		final Triple.TripleBuilder<String, String, String> builder = Triple
			.<String, String, String> builder().left(testCase.left()).middle(testCase.middle())
			.right(testCase.right());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.tripleText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link Triple#toBuilder()} give the same contents, and the
	 * getters return them
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepTheContents(final ContentCase testCase)
	{
		final Triple<String, String, String> built = Triple.<String, String, String> builder()
			.left(testCase.left()).middle(testCase.middle()).right(testCase.right()).build();
		final Triple<String, String, String> constructed = new Triple<>(testCase.left(),
			testCase.middle(), testCase.right());
		final Triple<String, String, String> copy = constructed.toBuilder().build();
		assertEquals(testCase.left(), built.getLeft());
		assertEquals(testCase.middle(), built.getMiddle());
		assertEquals(testCase.right(), built.getRight());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link Triple#toBuilder()} starts from the contents of the triple and leaves the triple as it
	 * is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final Triple<String, String, String> triple = new Triple<>("a", "b", "c");
		final Triple<String, String, String> changed = triple.toBuilder().middle("x").build();
		assertEquals(new Triple<>("a", "x", "c"), changed);
		assertEquals(new Triple<>("a", "b", "c"), triple);
	}

	/**
	 * The no-argument constructor leaves the three contents null, and the setters set them
	 */
	@Test
	void settersSetTheContentsOfAnEmptyTriple()
	{
		final Triple<String, String, String> triple = new Triple<>();
		assertNull(triple.getLeft());
		assertNull(triple.getMiddle());
		assertNull(triple.getRight());
		triple.setLeft("a");
		triple.setMiddle("b");
		triple.setRight("c");
		assertEquals("a", triple.getLeft());
		assertEquals("b", triple.getMiddle());
		assertEquals("c", triple.getRight());
	}

	/**
	 * A {@link Triple} is {@link java.io.Serializable}: a serialized and read triple equals the
	 * triple
	 *
	 * @throws IOException
	 *             if the triple cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read triple is not found
	 */
	@Test
	void aSerializedTripleReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final Triple<String, String, String> triple = new Triple<>("a", "b", "c");
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes))
		{
			out.writeObject(triple);
		}
		try (ObjectInputStream in = new ObjectInputStream(
			new ByteArrayInputStream(bytes.toByteArray())))
		{
			assertEquals(triple, in.readObject());
		}
	}

	/**
	 * Test method for {@link Triple}
	 */
	@Test
	@Disabled
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Triple.class);
	}
}
