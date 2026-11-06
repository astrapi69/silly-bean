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
 * The unit test class for the class {@link ValueBox}. The cases hold the values only; every test
 * builds its boxes itself, so that PIT gives each case the code it runs
 */
public class ValueBoxTest
{

	/**
	 * The hash code the formula of {@link ValueBox#hashCode()} takes for a null value
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link ValueBox#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given value as {@link ValueBox#hashCode()} takes it
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
	 * The values of two boxes, and whether the boxes are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstValue
	 *            the value of the first box, may be null
	 * @param secondValue
	 *            the value of the second box, may be null
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
			new EqualsCase("the value null in both: equal", null, null, true),
			new EqualsCase("another value: not equal", "a", "x", false),
			new EqualsCase("the value null in the first only: not equal", null, "a", false),
			new EqualsCase("the value null in the second only: not equal", "a", null, false));
	}

	/**
	 * {@link ValueBox#equals(Object)} compares the value, both ways round; equal boxes have the
	 * same {@link ValueBox#hashCode()}, and for these values unequal boxes have different ones
	 *
	 * @param testCase
	 *            the values and whether the boxes are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesTheValue(final EqualsCase testCase)
	{
		final ValueBox<String> first = new ValueBox<>(testCase.firstValue());
		final ValueBox<String> second = ValueBox.<String> builder().value(testCase.secondValue())
			.build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * A {@link ValueBox} whose {@link ValueBox#canEqual(Object)} accepts only its own class, as a
	 * subclass that adds a field to {@link ValueBox#equals(Object)} would
	 */
	static class OwnClassValueBox extends ValueBox<String>
	{
		private static final long serialVersionUID = 1L;

		OwnClassValueBox(final String value)
		{
			super(value);
		}

		@Override
		protected boolean canEqual(final Object other)
		{
			return other instanceof OwnClassValueBox;
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
	record OtherCase(String description, Function<ValueBox<String>, Object> other, boolean equal) {
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
			new OtherCase("the value itself: not equal", box -> box.getValue(), false),
			new OtherCase("a subclass of the same value: equal",
				box -> new ValueBox<String>(box.getValue())
				{
					private static final long serialVersionUID = 1L;
				}, true),
			new OtherCase("a subclass whose canEqual refuses a ValueBox: not equal",
				box -> new OwnClassValueBox(box.getValue()), false));
	}

	/**
	 * {@link ValueBox#equals(Object)} of a box and another object
	 *
	 * @param testCase
	 *            the object and whether the box equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final ValueBox<String> box = new ValueBox<>("a");
		assertEquals(testCase.equal(), box.equals(testCase.other().apply(box)));
	}

	/**
	 * An object to give to {@link ValueBox#canEqual(Object)}, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the box in the test
	 * @param canEqual
	 *            the result of canEqual
	 */
	record CanEqualCase(String description, Function<ValueBox<String>, Object> other,
		boolean canEqual) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * {@link ValueBox#canEqual(Object)} accepts any {@link ValueBox}
	 *
	 * @return the cases
	 */
	static Stream<CanEqualCase> canEqualCases()
	{
		return Stream.of(new CanEqualCase("a box: true", box -> new ValueBox<>("x"), true),
			new CanEqualCase("a subclass: true", box -> new OwnClassValueBox("x"), true),
			new CanEqualCase("an ImmutableBox: false", box -> new ImmutableBox<>("x"), false),
			new CanEqualCase("a String: false", box -> "a", false),
			new CanEqualCase("null: false", box -> null, false));
	}

	/**
	 * {@link ValueBox#canEqual(Object)} is true for an instance of {@link ValueBox} only
	 *
	 * @param testCase
	 *            the object and the result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("canEqualCases")
	void canEqualAcceptsAValueBox(final CanEqualCase testCase)
	{
		final ValueBox<String> box = new ValueBox<>("a");
		assertEquals(testCase.canEqual(), box.canEqual(testCase.other().apply(box)));
	}

	/**
	 * The value of a box, its {@link ValueBox#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param value
	 *            the value, may be null
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
	 * Boxes with and without a value
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("a value", "a", "ValueBox(value=a)",
				"ValueBox.ValueBoxBuilder(value=a)"),
			new ContentCase("the value null", null, "ValueBox(value=null)",
				"ValueBox.ValueBoxBuilder(value=null)"));
	}

	/**
	 * {@link ValueBox#hashCode()} is the formula Lombok writes, {@code 59 + hash(value)} with 43
	 * for null; the contract of {@link Object#hashCode()} would allow any value that agrees with
	 * equals, the test pins this one so that a change shows
	 *
	 * @param testCase
	 *            the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfTheValue(final ContentCase testCase)
	{
		final ValueBox<String> box = new ValueBox<>(testCase.value());
		assertEquals(PRIME + hash(testCase.value()), box.hashCode());
	}

	/**
	 * {@link ValueBox#toString()} and {@link ValueBox.ValueBoxBuilder#toString()} name the value
	 *
	 * @param testCase
	 *            the value and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesTheValue(final ContentCase testCase)
	{
		final ValueBox.ValueBoxBuilder<String> builder = ValueBox.<String> builder()
			.value(testCase.value());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.boxText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link ValueBox#toBuilder()} give the same value, and the
	 * getter returns it
	 *
	 * @param testCase
	 *            the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepTheValue(final ContentCase testCase)
	{
		final ValueBox<String> built = ValueBox.<String> builder().value(testCase.value()).build();
		final ValueBox<String> constructed = new ValueBox<>(testCase.value());
		final ValueBox<String> copy = constructed.toBuilder().build();
		assertEquals(testCase.value(), built.getValue());
		assertEquals(testCase.value(), copy.getValue());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link ValueBox#toBuilder()} starts from the value of the box and leaves the box as it is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final ValueBox<String> box = new ValueBox<>("a");
		final ValueBox<String> changed = box.toBuilder().value("b").build();
		assertEquals(new ValueBox<>("b"), changed);
		assertEquals(new ValueBox<>("a"), box);
	}

	/**
	 * The no-argument constructor leaves the value null, and the setter sets it
	 */
	@Test
	void setterSetsTheValueOfAnEmptyBox()
	{
		final ValueBox<String> box = new ValueBox<>();
		assertNull(box.getValue());
		box.setValue("a");
		assertEquals("a", box.getValue());
	}

	/**
	 * A {@link ValueBox} is {@link java.io.Serializable}: a serialized and read box equals the box
	 *
	 * @throws IOException
	 *             if the box cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read box is not found
	 */
	@Test
	void aSerializedValueBoxReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final ValueBox<String> box = new ValueBox<>("a");
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

	/**
	 * Test method for {@link ValueBox}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ValueBox.class);
	}

}
