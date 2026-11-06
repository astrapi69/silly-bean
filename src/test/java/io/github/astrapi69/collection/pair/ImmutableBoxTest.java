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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link ImmutableBox}. The cases hold the values only; every
 * test builds its boxes itself, so that PIT gives each case the code it runs
 */
public class ImmutableBoxTest
{

	/**
	 * The hash code the formula of {@link ImmutableBox#hashCode()} takes for a null value
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The message of the {@link NullPointerException} the box and its builder throw for a null
	 * value
	 */
	private static final String NULL_MESSAGE = "value is marked non-null but is null";

	/**
	 * The prime the formula of {@link ImmutableBox#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given value as {@link ImmutableBox#hashCode()} takes it
	 *
	 * @param value
	 *            the value, may be null
	 * @return the hash code of the value, or 43 for null
	 */
	private static int hash(final Object value)
	{
		return value == null ? NULL_HASH : value.hashCode();
	}

	/**
	 * An {@link ImmutableBox} whose {@link ImmutableBox#getValue()} returns null. The constructor
	 * and the builder refuse a null value, but {@link ImmutableBox#equals(Object)},
	 * {@link ImmutableBox#hashCode()} and {@link ImmutableBox#toString()} read the value through
	 * the getter, so a subclass is the only way to their null branches
	 */
	static class NullValueBox extends ImmutableBox<String>
	{
		private static final long serialVersionUID = 1L;

		NullValueBox()
		{
			super("not read");
		}

		@Override
		public String getValue()
		{
			return null;
		}
	}

	/**
	 * A box of the given value, or a {@link NullValueBox} for null
	 *
	 * @param value
	 *            the value, or null for a {@link NullValueBox}
	 * @return the box
	 */
	private static ImmutableBox<String> boxOrNullValueBox(final String value)
	{
		return value == null ? new NullValueBox() : new ImmutableBox<>(value);
	}

	/**
	 * The values of two boxes, and whether the boxes are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstValue
	 *            the value of the first box, or null for a {@link NullValueBox}
	 * @param secondValue
	 *            the value of the second box, or null for a {@link NullValueBox}
	 * @param equal
	 *            true if the boxes are equal
	 */
	record EqualsCase(String description, String firstValue, String secondValue, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two boxes are equal if their values are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(new EqualsCase("the same value: equal", "a", "a", true),
			new EqualsCase("another value: not equal", "a", "x", false),
			new EqualsCase("getValue null in both (a subclass): equal", null, null, true),
			new EqualsCase("getValue null in the first only (a subclass): not equal", null, "a",
				false),
			new EqualsCase("getValue null in the second only (a subclass): not equal", "a", null,
				false));
	}

	/**
	 * {@link ImmutableBox#equals(Object)} compares the value, both ways round; equal boxes have the
	 * same {@link ImmutableBox#hashCode()}, and for these values unequal boxes have different ones
	 *
	 * @param testCase
	 *            the values and whether the boxes are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesTheValue(final EqualsCase testCase)
	{
		final ImmutableBox<String> first = boxOrNullValueBox(testCase.firstValue());
		final ImmutableBox<String> second = testCase.secondValue() == null
			? new NullValueBox()
			: ImmutableBox.<String> builder().value(testCase.secondValue()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An {@link ImmutableBox} whose {@link ImmutableBox#canEqual(Object)} accepts only its own
	 * class, as a subclass that adds a field to {@link ImmutableBox#equals(Object)} would
	 */
	static class OwnClassImmutableBox extends ImmutableBox<String>
	{
		private static final long serialVersionUID = 1L;

		OwnClassImmutableBox(final String value)
		{
			super(value);
		}

		@Override
		protected boolean canEqual(final Object other)
		{
			return other instanceof OwnClassImmutableBox;
		}
	}

	/**
	 * An object to compare a box of "a" with, and whether it is equal to the box
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the box in the test
	 * @param equal
	 *            true if the box equals the object
	 */
	record OtherCase(String description, Function<ImmutableBox<String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a box of another value
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the box itself: equal", box -> box, true),
			new OtherCase("null: not equal", box -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				box -> box.toString(), false),
			new OtherCase("a ValueBox of the same value: not equal",
				box -> new ValueBox<>(box.getValue()), false),
			new OtherCase("a subclass of the same value: equal",
				box -> new ImmutableBox<String>(box.getValue())
				{
					private static final long serialVersionUID = 1L;
				}, true),
			new OtherCase("a subclass whose canEqual refuses an ImmutableBox: not equal",
				box -> new OwnClassImmutableBox(box.getValue()), false));
	}

	/**
	 * {@link ImmutableBox#equals(Object)} of a box and another object
	 *
	 * @param testCase
	 *            the object and whether the box equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final ImmutableBox<String> box = new ImmutableBox<>("a");
		assertEquals(testCase.equal(), box.equals(testCase.other().apply(box)));
	}

	/**
	 * An object to give to {@link ImmutableBox#canEqual(Object)}, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the box in the test
	 * @param canEqual
	 *            the result of canEqual
	 */
	record CanEqualCase(String description, Function<ImmutableBox<String>, Object> other,
		boolean canEqual) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * {@link ImmutableBox#canEqual(Object)} accepts any {@link ImmutableBox}
	 *
	 * @return the cases
	 */
	static Stream<CanEqualCase> canEqualCases()
	{
		return Stream.of(new CanEqualCase("a box: true", box -> new ImmutableBox<>("x"), true),
			new CanEqualCase("a subclass: true", box -> new OwnClassImmutableBox("x"), true),
			new CanEqualCase("a ValueBox: false", box -> new ValueBox<>("x"), false),
			new CanEqualCase("a String: false", box -> "a", false),
			new CanEqualCase("null: false", box -> null, false));
	}

	/**
	 * {@link ImmutableBox#canEqual(Object)} is true for an instance of {@link ImmutableBox} only
	 *
	 * @param testCase
	 *            the object and the result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("canEqualCases")
	void canEqualAcceptsAnImmutableBox(final CanEqualCase testCase)
	{
		final ImmutableBox<String> box = new ImmutableBox<>("a");
		assertEquals(testCase.canEqual(), box.canEqual(testCase.other().apply(box)));
	}

	/**
	 * The value of a box, its {@link ImmutableBox#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param value
	 *            the value
	 * @param boxText
	 *            the toString of the box
	 * @param builderText
	 *            the toString of a builder of this value
	 */
	record ContentCase(String description, String value, String boxText, String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Boxes of a value; the box takes no null value
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("a value", "a", "ImmutableBox(value=a)",
				"ImmutableBox.ImmutableBoxBuilder(value=a)"),
			new ContentCase("an empty text", "", "ImmutableBox(value=)",
				"ImmutableBox.ImmutableBoxBuilder(value=)"));
	}

	/**
	 * {@link ImmutableBox#hashCode()} is the formula Lombok writes, {@code 59 + hash(value)} with
	 * 43 for null; the contract of {@link Object#hashCode()} would allow any value that agrees with
	 * equals, the test pins this one so that a change shows
	 *
	 * @param testCase
	 *            the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfTheValue(final ContentCase testCase)
	{
		final ImmutableBox<String> box = new ImmutableBox<>(testCase.value());
		assertEquals(PRIME + hash(testCase.value()), box.hashCode());
	}

	/**
	 * {@link ImmutableBox#hashCode()} and {@link ImmutableBox#toString()} of a subclass whose
	 * getter returns null: the formula takes 43 for the null, the text names null
	 */
	@Test
	void hashCodeAndToStringOfANullFromTheGetter()
	{
		final ImmutableBox<String> box = new NullValueBox();
		assertEquals(PRIME + NULL_HASH, box.hashCode());
		assertEquals("ImmutableBox(value=null)", box.toString());
	}

	/**
	 * {@link ImmutableBox#toString()} and {@link ImmutableBox.ImmutableBoxBuilder#toString()} name
	 * the value
	 *
	 * @param testCase
	 *            the value and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesTheValue(final ContentCase testCase)
	{
		final ImmutableBox.ImmutableBoxBuilder<String> builder = ImmutableBox.<String> builder()
			.value(testCase.value());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.boxText(), builder.build().toString());
	}

	/**
	 * {@link ImmutableBox.ImmutableBoxBuilder#toString()} of a builder that has no value yet names
	 * null
	 */
	@Test
	void toStringOfAnEmptyBuilderNamesNull()
	{
		assertEquals("ImmutableBox.ImmutableBoxBuilder(value=null)",
			ImmutableBox.<String> builder().toString());
	}

	/**
	 * The builder, the constructor and {@link ImmutableBox#toBuilder()} give the same value, and
	 * the getter returns it
	 *
	 * @param testCase
	 *            the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepTheValue(final ContentCase testCase)
	{
		final ImmutableBox<String> built = ImmutableBox.<String> builder().value(testCase.value())
			.build();
		final ImmutableBox<String> constructed = new ImmutableBox<>(testCase.value());
		final ImmutableBox<String> copy = constructed.toBuilder().build();
		assertEquals(testCase.value(), built.getValue());
		assertEquals(testCase.value(), copy.getValue());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link ImmutableBox#toBuilder()} starts from the value of the box and leaves the box as it is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final ImmutableBox<String> box = new ImmutableBox<>("a");
		final ImmutableBox<String> changed = box.toBuilder().value("b").build();
		assertEquals(new ImmutableBox<>("b"), changed);
		assertEquals(new ImmutableBox<>("a"), box);
	}

	/**
	 * A way to give an {@link ImmutableBox} or its builder a null value
	 *
	 * @param description
	 *            the name of the case
	 * @param action
	 *            the call that gets the null value
	 */
	record NullCase(String description, Executable action) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The ways to a null value: the constructor, the builder method, and a builder that never got a
	 * value
	 *
	 * @return the cases
	 */
	static Stream<NullCase> nullCases()
	{
		return Stream.of(new NullCase("the constructor with null", () -> new ImmutableBox<>(null)),
			new NullCase("the builder's value with null, before build",
				() -> ImmutableBox.<String> builder().value(null)),
			new NullCase("build without a value", () -> ImmutableBox.<String> builder().build()),
			new NullCase("toBuilder, then the builder's value with null",
				() -> new ImmutableBox<>("a").toBuilder().value(null)));
	}

	/**
	 * The constructor and {@link ImmutableBox.ImmutableBoxBuilder#value(Object)} refuse null at
	 * once, with the message Lombok's {@code @NonNull} writes; neither Javadoc says so
	 *
	 * @param testCase
	 *            the call that gets the null value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("nullCases")
	void aNullValueThrowsANullPointerException(final NullCase testCase)
	{
		final NullPointerException exception = assertThrows(NullPointerException.class,
			testCase.action());
		assertEquals(NULL_MESSAGE, exception.getMessage());
	}

	/**
	 * An {@link ImmutableBox} is {@link java.io.Serializable}: a serialized and read box equals the
	 * box
	 *
	 * @throws IOException
	 *             if the box cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read box is not found
	 */
	@Test
	void aSerializedImmutableBoxReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final ImmutableBox<String> box = new ImmutableBox<>("a");
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes))
		{
			out.writeObject(box);
		}
		try (ObjectInputStream in = new ObjectInputStream(
			new ByteArrayInputStream(bytes.toByteArray())))
		{
			assertEquals(box, in.readObject());
		}
	}

}
