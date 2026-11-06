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
 * The unit test class for the class {@link Quattro}. The cases hold the contents only; every test
 * builds its quattros itself, so that PIT gives each case the code it runs. The contents are given
 * in the order of the constructor and of the formula of {@link Quattro#hashCode()}: bottom left,
 * bottom right, top left, top right
 */
public class QuattroTest
{

	/**
	 * The hash code the formula of {@link Quattro#hashCode()} takes for a null content
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link Quattro#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given content as {@link Quattro#hashCode()} takes it
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
	 * The contents of two quattros, and whether the quattros are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstBottomLeft
	 *            the bottom left content of the first quattro, may be null
	 * @param firstBottomRight
	 *            the bottom right content of the first quattro, may be null
	 * @param firstTopLeft
	 *            the top left content of the first quattro, may be null
	 * @param firstTopRight
	 *            the top right content of the first quattro, may be null
	 * @param secondBottomLeft
	 *            the bottom left content of the second quattro, may be null
	 * @param secondBottomRight
	 *            the bottom right content of the second quattro, may be null
	 * @param secondTopLeft
	 *            the top left content of the second quattro, may be null
	 * @param secondTopRight
	 *            the top right content of the second quattro, may be null
	 * @param equal
	 *            true if the quattros are equal
	 */
	record EqualsCase(String description, String firstBottomLeft, String firstBottomRight,
		String firstTopLeft, String firstTopRight, String secondBottomLeft,
		String secondBottomRight, String secondTopLeft, String secondTopRight, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two quattros are equal if their four contents are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same contents: equal", "a", "b", "c", "d", "a", "b", "c", "d",
				true),
			new EqualsCase("all contents null in both: equal", null, null, null, null, null, null,
				null, null, true),
			new EqualsCase("another bottom left: not equal", "a", "b", "c", "d", "x", "b", "c", "d",
				false),
			new EqualsCase("another bottom right: not equal", "a", "b", "c", "d", "a", "x", "c",
				"d", false),
			new EqualsCase("another top left: not equal", "a", "b", "c", "d", "a", "b", "x", "d",
				false),
			new EqualsCase("another top right: not equal", "a", "b", "c", "d", "a", "b", "c", "x",
				false),
			new EqualsCase("bottom left null in the first only: not equal", null, "b", "c", "d",
				"a", "b", "c", "d", false),
			new EqualsCase("bottom left null in the second only: not equal", "a", "b", "c", "d",
				null, "b", "c", "d", false),
			new EqualsCase("bottom right null in the first only: not equal", "a", null, "c", "d",
				"a", "b", "c", "d", false),
			new EqualsCase("bottom right null in the second only: not equal", "a", "b", "c", "d",
				"a", null, "c", "d", false),
			new EqualsCase("top left null in the first only: not equal", "a", "b", null, "d", "a",
				"b", "c", "d", false),
			new EqualsCase("top left null in the second only: not equal", "a", "b", "c", "d", "a",
				"b", null, "d", false),
			new EqualsCase("top right null in the first only: not equal", "a", "b", "c", null, "a",
				"b", "c", "d", false),
			new EqualsCase("top right null in the second only: not equal", "a", "b", "c", "d", "a",
				"b", "c", null, false),
			new EqualsCase("bottom and top swapped: not equal", "a", "b", "c", "d", "c", "d", "a",
				"b", false));
	}

	/**
	 * {@link Quattro#equals(Object)} compares the four contents, both ways round; equal quattros
	 * have the same {@link Quattro#hashCode()}, and for these contents unequal quattros have
	 * different ones
	 *
	 * @param testCase
	 *            the contents and whether the quattros are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesTheFourContents(final EqualsCase testCase)
	{
		final Quattro<String, String, String, String> first = new Quattro<>(
			testCase.firstBottomLeft(), testCase.firstBottomRight(), testCase.firstTopLeft(),
			testCase.firstTopRight());
		final Quattro<String, String, String, String> second = Quattro
			.<String, String, String, String> builder().bottomLeft(testCase.secondBottomLeft())
			.bottomRight(testCase.secondBottomRight()).topLeft(testCase.secondTopLeft())
			.topRight(testCase.secondTopRight()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * A {@link Quattro} whose {@link Quattro#canEqual(Object)} accepts only its own class, as a
	 * subclass that adds a field to {@link Quattro#equals(Object)} would
	 */
	static class OwnClassQuattro extends Quattro<String, String, String, String>
	{
		private static final long serialVersionUID = 1L;

		OwnClassQuattro(final String bottomLeft, final String bottomRight, final String topLeft,
			final String topRight)
		{
			super(bottomLeft, bottomRight, topLeft, topRight);
		}

		@Override
		protected boolean canEqual(final Object other)
		{
			return other instanceof OwnClassQuattro;
		}
	}

	/**
	 * An object to compare a quattro of "a", "b", "c" and "d" with, and whether it is equal to the
	 * quattro
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the quattro in the test
	 * @param equal
	 *            true if the quattro equals the object
	 */
	record OtherCase(String description,
		Function<Quattro<String, String, String, String>, Object> other, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a quattro of other contents
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the quattro itself: equal", quattro -> quattro, true),
			new OtherCase("null: not equal", quattro -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				quattro -> quattro.toString(), false),
			new OtherCase("a subclass of the same contents: equal",
				quattro -> new Quattro<String, String, String, String>(quattro.getBottomLeft(),
					quattro.getBottomRight(), quattro.getTopLeft(), quattro.getTopRight())
				{
					private static final long serialVersionUID = 1L;
				}, true),
			new OtherCase("a subclass whose canEqual refuses a Quattro: not equal",
				quattro -> new OwnClassQuattro(quattro.getBottomLeft(), quattro.getBottomRight(),
					quattro.getTopLeft(), quattro.getTopRight()),
				false));
	}

	/**
	 * {@link Quattro#equals(Object)} of a quattro and another object
	 *
	 * @param testCase
	 *            the object and whether the quattro equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>("a", "b", "c", "d");
		assertEquals(testCase.equal(), quattro.equals(testCase.other().apply(quattro)));
	}

	/**
	 * An object to give to {@link Quattro#canEqual(Object)}, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the quattro in the test
	 * @param canEqual
	 *            the result of canEqual
	 */
	record CanEqualCase(String description,
		Function<Quattro<String, String, String, String>, Object> other, boolean canEqual) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * {@link Quattro#canEqual(Object)} accepts any {@link Quattro}
	 *
	 * @return the cases
	 */
	static Stream<CanEqualCase> canEqualCases()
	{
		return Stream.of(
			new CanEqualCase("a quattro: true", quattro -> new Quattro<>("w", "x", "y", "z"), true),
			new CanEqualCase("a subclass: true", quattro -> new OwnClassQuattro("w", "x", "y", "z"),
				true),
			new CanEqualCase("a Triple: false", quattro -> new Triple<>("x", "y", "z"), false),
			new CanEqualCase("a String: false", quattro -> "a", false),
			new CanEqualCase("null: false", quattro -> null, false));
	}

	/**
	 * {@link Quattro#canEqual(Object)} is true for an instance of {@link Quattro} only
	 *
	 * @param testCase
	 *            the object and the result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("canEqualCases")
	void canEqualAcceptsAQuattro(final CanEqualCase testCase)
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>("a", "b", "c", "d");
		assertEquals(testCase.canEqual(), quattro.canEqual(testCase.other().apply(quattro)));
	}

	/**
	 * The contents of a quattro, its {@link Quattro#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param bottomLeft
	 *            the bottom left content, may be null
	 * @param bottomRight
	 *            the bottom right content, may be null
	 * @param topLeft
	 *            the top left content, may be null
	 * @param topRight
	 *            the top right content, may be null
	 * @param quattroText
	 *            the toString of the quattro
	 * @param builderText
	 *            the toString of a builder of these contents
	 */
	record ContentCase(String description, String bottomLeft, String bottomRight, String topLeft,
		String topRight, String quattroText, String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Quattros with and without contents
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("all contents", "a", "b", "c", "d",
				"Quattro(bottomLeft=a, bottomRight=b, topLeft=c, topRight=d)",
				"Quattro.QuattroBuilder(bottomLeft=a, bottomRight=b, topLeft=c, topRight=d)"),
			new ContentCase("the bottom left null", null, "b", "c", "d",
				"Quattro(bottomLeft=null, bottomRight=b, topLeft=c, topRight=d)",
				"Quattro.QuattroBuilder(bottomLeft=null, bottomRight=b, topLeft=c, topRight=d)"),
			new ContentCase("the bottom right null", "a", null, "c", "d",
				"Quattro(bottomLeft=a, bottomRight=null, topLeft=c, topRight=d)",
				"Quattro.QuattroBuilder(bottomLeft=a, bottomRight=null, topLeft=c, topRight=d)"),
			new ContentCase("the top left null", "a", "b", null, "d",
				"Quattro(bottomLeft=a, bottomRight=b, topLeft=null, topRight=d)",
				"Quattro.QuattroBuilder(bottomLeft=a, bottomRight=b, topLeft=null, topRight=d)"),
			new ContentCase("the top right null", "a", "b", "c", null,
				"Quattro(bottomLeft=a, bottomRight=b, topLeft=c, topRight=null)",
				"Quattro.QuattroBuilder(bottomLeft=a, bottomRight=b, topLeft=c, topRight=null)"),
			new ContentCase("all contents null", null, null, null, null,
				"Quattro(bottomLeft=null, bottomRight=null, topLeft=null, topRight=null)",
				"Quattro.QuattroBuilder(bottomLeft=null, bottomRight=null, topLeft=null, topRight=null)"));
	}

	/**
	 * {@link Quattro#hashCode()} is the formula Lombok writes, in the order of the fields,
	 * {@code (((59 + hash(bottomLeft)) * 59 + hash(bottomRight)) * 59 + hash(topLeft)) * 59 +
	 * hash(topRight)} with 43 for null; the contract of {@link Object#hashCode()} would allow any
	 * value that agrees with equals, the test pins this one so that a change shows
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfTheFourContents(final ContentCase testCase)
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>(testCase.bottomLeft(),
			testCase.bottomRight(), testCase.topLeft(), testCase.topRight());
		assertEquals(
			(((PRIME + hash(testCase.bottomLeft())) * PRIME + hash(testCase.bottomRight())) * PRIME
				+ hash(testCase.topLeft())) * PRIME + hash(testCase.topRight()),
			quattro.hashCode());
	}

	/**
	 * {@link Quattro#toString()} and {@link Quattro.QuattroBuilder#toString()} name the four
	 * contents, in the order of the fields
	 *
	 * @param testCase
	 *            the contents and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesTheFourContents(final ContentCase testCase)
	{
		final Quattro.QuattroBuilder<String, String, String, String> builder = Quattro
			.<String, String, String, String> builder().bottomLeft(testCase.bottomLeft())
			.bottomRight(testCase.bottomRight()).topLeft(testCase.topLeft())
			.topRight(testCase.topRight());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.quattroText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link Quattro#toBuilder()} give the same contents, and the
	 * getters return them
	 *
	 * @param testCase
	 *            the contents
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepTheContents(final ContentCase testCase)
	{
		final Quattro<String, String, String, String> built = Quattro
			.<String, String, String, String> builder().bottomLeft(testCase.bottomLeft())
			.bottomRight(testCase.bottomRight()).topLeft(testCase.topLeft())
			.topRight(testCase.topRight()).build();
		final Quattro<String, String, String, String> constructed = new Quattro<>(
			testCase.bottomLeft(), testCase.bottomRight(), testCase.topLeft(), testCase.topRight());
		final Quattro<String, String, String, String> copy = constructed.toBuilder().build();
		assertEquals(testCase.bottomLeft(), built.getBottomLeft());
		assertEquals(testCase.bottomRight(), built.getBottomRight());
		assertEquals(testCase.topLeft(), built.getTopLeft());
		assertEquals(testCase.topRight(), built.getTopRight());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * The constructor takes bottom left, bottom right, top left and top right, in this order, which
	 * is not the order of the type parameters {@code <TL, TR, BL, BR>}
	 */
	@Test
	void theConstructorTakesTheBottomContentsFirst()
	{
		final Quattro<Integer, Long, String, Character> quattro = new Quattro<>("bottom left", 'r',
			1, 2L);
		assertEquals("bottom left", quattro.getBottomLeft());
		assertEquals('r', quattro.getBottomRight());
		assertEquals(1, quattro.getTopLeft());
		assertEquals(2L, quattro.getTopRight());
	}

	/**
	 * {@link Quattro#toBuilder()} starts from the contents of the quattro and leaves the quattro as
	 * it is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>("a", "b", "c", "d");
		final Quattro<String, String, String, String> changed = quattro.toBuilder().topLeft("x")
			.build();
		assertEquals(new Quattro<>("a", "b", "x", "d"), changed);
		assertEquals(new Quattro<>("a", "b", "c", "d"), quattro);
	}

	/**
	 * The no-argument constructor leaves the four contents null, and the setters set them
	 */
	@Test
	void settersSetTheContentsOfAnEmptyQuattro()
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>();
		assertNull(quattro.getBottomLeft());
		assertNull(quattro.getBottomRight());
		assertNull(quattro.getTopLeft());
		assertNull(quattro.getTopRight());
		quattro.setBottomLeft("a");
		quattro.setBottomRight("b");
		quattro.setTopLeft("c");
		quattro.setTopRight("d");
		assertEquals("a", quattro.getBottomLeft());
		assertEquals("b", quattro.getBottomRight());
		assertEquals("c", quattro.getTopLeft());
		assertEquals("d", quattro.getTopRight());
	}

	/**
	 * A {@link Quattro} is {@link java.io.Serializable}: a serialized and read quattro equals the
	 * quattro
	 *
	 * @throws IOException
	 *             if the quattro cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read quattro is not found
	 */
	@Test
	void aSerializedQuattroReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final Quattro<String, String, String, String> quattro = new Quattro<>("a", "b", "c", "d");
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes))
		{
			out.writeObject(quattro);
		}
		try (ObjectInputStream in = new ObjectInputStream(
			new ByteArrayInputStream(bytes.toByteArray())))
		{
			assertEquals(quattro, in.readObject());
		}
	}

	/**
	 * Test method for {@link Quattro}
	 */
	@Test
	@Disabled
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Quattro.class);
	}

}
