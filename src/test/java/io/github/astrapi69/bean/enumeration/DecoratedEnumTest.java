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
package io.github.astrapi69.bean.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.test.object.enumeration.Brand;
import io.github.astrapi69.test.object.enumeration.Gender;

/**
 * The unit test class for the class {@link DecoratedEnum}. The cases hold the enum constant and the
 * value only; every test builds its objects itself, so that PIT gives each case the code it runs
 */
public class DecoratedEnumTest
{

	/**
	 * The hash code the formula of {@link DecoratedEnum#hashCode()} takes for null
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link DecoratedEnum#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given field as {@link DecoratedEnum#hashCode()} takes it
	 *
	 * @param field
	 *            the field, may be null
	 * @return the hash code of the field, or 43 for null
	 */
	private static int hash(final Object field)
	{
		return field == null ? NULL_HASH : field.hashCode();
	}

	/**
	 * An enum constant, a value, and the texts of the decorated enum of them
	 *
	 * @param description
	 *            the name of the case
	 * @param enumtype
	 *            the enum constant, may be null
	 * @param value
	 *            the value, may be null
	 * @param name
	 *            the result of {@link DecoratedEnum#name()}
	 * @param text
	 *            the result of {@link DecoratedEnum#toString()}
	 * @param builderText
	 *            the toString of a builder of the constant and the value
	 */
	record ContentCase(String description, Brand enumtype, Object value, String name, String text,
		String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Decorated enums of an enum constant, a value or both
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("an enum constant alone: its name", Brand.FERRARI, null, "FERRARI",
				"DecoratedEnum(enumtype=FERRARI, value=null)",
				"DecoratedEnum.DecoratedEnumBuilder(enumtype=FERRARI, value=null)"),
			new ContentCase("a value alone: its toString", null, "JAGUAR", "JAGUAR",
				"DecoratedEnum(enumtype=null, value=JAGUAR)",
				"DecoratedEnum.DecoratedEnumBuilder(enumtype=null, value=JAGUAR)"),
			new ContentCase("a value of another type alone: its toString", null, 42, "42",
				"DecoratedEnum(enumtype=null, value=42)",
				"DecoratedEnum.DecoratedEnumBuilder(enumtype=null, value=42)"),
			new ContentCase("both: the name of the enum constant", Brand.FERRARI, "JAGUAR",
				"FERRARI", "DecoratedEnum(enumtype=FERRARI, value=JAGUAR)",
				"DecoratedEnum.DecoratedEnumBuilder(enumtype=FERRARI, value=JAGUAR)"));
	}

	/**
	 * {@link DecoratedEnum#name()} gives the name of the enum constant, and the toString of the
	 * value when there is no enum constant
	 *
	 * @param testCase
	 *            the constant, the value and the name
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void nameIsTheEnumConstantsOrTheValues(final ContentCase testCase)
	{
		final DecoratedEnum<Brand, Object> decorated = new DecoratedEnum<>(testCase.enumtype(),
			testCase.value());
		assertEquals(testCase.name(), decorated.name());
	}

	/**
	 * The builder and the constructor give the same object, and the getters return what was given
	 *
	 * @param testCase
	 *            the constant and the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderAndConstructorKeepTheConstantAndTheValue(final ContentCase testCase)
	{
		final DecoratedEnum<Brand, Object> built = DecoratedEnum.<Brand, Object> builder()
			.enumtype(testCase.enumtype()).value(testCase.value()).build();
		final DecoratedEnum<Brand, Object> constructed = new DecoratedEnum<>(testCase.enumtype(),
			testCase.value());
		assertEquals(testCase.enumtype(), built.getEnumtype());
		assertEquals(testCase.value(), built.getValue());
		assertEquals(built, constructed);
	}

	/**
	 * {@link DecoratedEnum#toString()} and the builder's toString name the constant and the value
	 *
	 * @param testCase
	 *            the constant, the value and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesTheConstantAndTheValue(final ContentCase testCase)
	{
		final DecoratedEnum.DecoratedEnumBuilder<Brand, Object> builder = DecoratedEnum
			.<Brand, Object> builder().enumtype(testCase.enumtype()).value(testCase.value());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.text(), builder.build().toString());
	}

	/**
	 * {@link DecoratedEnum#hashCode()} is the formula Lombok writes, {@code (59 + hash(enumtype)) *
	 * 59 + hash(value)} with 43 for null; the contract of {@link Object#hashCode()} would allow any
	 * value that agrees with equals, the test pins this one so that a change shows
	 *
	 * @param testCase
	 *            the constant and the value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfTheConstantAndTheValue(final ContentCase testCase)
	{
		final DecoratedEnum<Brand, Object> decorated = new DecoratedEnum<>(testCase.enumtype(),
			testCase.value());
		assertEquals((PRIME + hash(testCase.enumtype())) * PRIME + hash(testCase.value()),
			decorated.hashCode());
	}

	/**
	 * A way to make a decorated enum of neither an enum constant nor a value
	 *
	 * @param description
	 *            the name of the case
	 * @param creation
	 *            makes the decorated enum, in the test
	 */
	record NeitherCase(String description, Supplier<DecoratedEnum<Brand, String>> creation) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The constructor and the builder
	 *
	 * @return the cases
	 */
	static Stream<NeitherCase> neitherCases()
	{
		return Stream.of(new NeitherCase("the constructor", () -> new DecoratedEnum<>(null, null)),
			new NeitherCase("the builder", () -> DecoratedEnum.<Brand, String> builder().build()));
	}

	/**
	 * A decorated enum needs an enum constant or a value: without both, the constructor and
	 * {@link DecoratedEnum.DecoratedEnumBuilder#build()} throw a {@link NullPointerException}
	 * without a message, the one of {@link java.util.Objects#requireNonNull(Object)}
	 *
	 * @param testCase
	 *            the way to make it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("neitherCases")
	void neitherAConstantNorAValueThrowsANullPointerException(final NeitherCase testCase)
	{
		final NullPointerException exception = assertThrows(NullPointerException.class,
			() -> testCase.creation().get());
		assertNull(exception.getMessage());
	}

	/**
	 * The constants and values of two decorated enums, and whether they are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstEnumtype
	 *            the enum constant of the first, may be null
	 * @param firstValue
	 *            the value of the first, may be null
	 * @param secondEnumtype
	 *            the enum constant of the second, may be null
	 * @param secondValue
	 *            the value of the second, may be null
	 * @param equal
	 *            true if the two are equal
	 */
	record EqualsCase(String description, Brand firstEnumtype, String firstValue,
		Brand secondEnumtype, String secondValue, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two decorated enums are equal if their enum constants are equal and their values are equal,
	 * null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same constant and value: equal", Brand.FERRARI, "a", Brand.FERRARI,
				"a", true),
			new EqualsCase("the same constant, no value: equal", Brand.FERRARI, null, Brand.FERRARI,
				null, true),
			new EqualsCase("no constant, the same value: equal", null, "a", null, "a", true),
			new EqualsCase("another constant: not equal", Brand.FERRARI, "a", Brand.PORSCHE, "a",
				false),
			new EqualsCase("another value: not equal", Brand.FERRARI, "a", Brand.FERRARI, "b",
				false),
			new EqualsCase("the constant in the first only: not equal", Brand.FERRARI, "a", null,
				"a", false),
			new EqualsCase("the constant in the second only: not equal", null, "a", Brand.FERRARI,
				"a", false),
			new EqualsCase("the value in the first only: not equal", Brand.FERRARI, "a",
				Brand.FERRARI, null, false),
			new EqualsCase("the value in the second only: not equal", Brand.FERRARI, null,
				Brand.FERRARI, "a", false));
	}

	/**
	 * {@link DecoratedEnum#equals(Object)} compares the enum constant and the value, both ways
	 * round; equal objects have the same {@link DecoratedEnum#hashCode()}
	 *
	 * @param testCase
	 *            the constants, the values and whether they are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesTheConstantAndTheValue(final EqualsCase testCase)
	{
		final DecoratedEnum<Brand, String> first = new DecoratedEnum<>(testCase.firstEnumtype(),
			testCase.firstValue());
		final DecoratedEnum<Brand, String> second = DecoratedEnum.<Brand, String> builder()
			.enumtype(testCase.secondEnumtype()).value(testCase.secondValue()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An object to compare the decorated enum of {@link Brand#FERRARI} and "a" with, and whether it
	 * is equal
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the decorated enum in the test
	 * @param equal
	 *            true if the decorated enum equals the object
	 */
	record OtherCase(String description, Function<DecoratedEnum<Brand, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Objects other than a decorated enum of another constant or value
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("itself: equal", decorated -> decorated, true),
			new OtherCase("null: not equal", decorated -> null, false),
			new OtherCase("its name as a String: not equal", decorated -> decorated.name(), false),
			new OtherCase("the enum constant itself: not equal",
				decorated -> decorated.getEnumtype(), false),
			new OtherCase("a decorated enum of another enum type: not equal",
				decorated -> new DecoratedEnum<Gender, String>(Gender.MALE, "a"), false));
	}

	/**
	 * {@link DecoratedEnum#equals(Object)} of a decorated enum and another object
	 *
	 * @param testCase
	 *            the object and whether the decorated enum equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final DecoratedEnum<Brand, String> decorated = new DecoratedEnum<>(Brand.FERRARI, "a");
		assertEquals(testCase.equal(), decorated.equals(testCase.other().apply(decorated)));
	}

	/**
	 * Test method for {@link DecoratedEnum} with {@link BeanTester}
	 */
	@Test
	@Disabled
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(DecoratedEnum.class);
	}
}
