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
package io.github.astrapi69.bean.enumtype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.evaluate.object.evaluators.EqualsEvaluator;
import io.github.astrapi69.evaluate.object.evaluators.HashcodeEvaluator;
import io.github.astrapi69.evaluate.object.evaluators.ToStringEvaluator;
import io.github.astrapi69.test.object.enumeration.Brand;
import io.github.astrapi69.test.object.enumeration.Gender;

/**
 * The unit test class for the class {@link DecoratedEnum}
 */
public class DecoratedEnumTest
{

	/**
	 * Test method for check if only the enum of the decorated enum is set so the name of the
	 * decorated enum is the name of the enum
	 */
	@Test
	public void testDecoratedEnum()
	{
		String expected;
		String actual;
		Brand ferrari = Brand.FERRARI;
		DecoratedEnum<Brand, String> cenum = DecoratedEnum.<Brand, String> builder()
			.enumtype(ferrari).build();
		actual = cenum.name();
		expected = ferrari.name();
		assertEquals(actual, expected);
	}

	/**
	 * Test method for check if only the value of the decorated enum is set so the name of the
	 * decorated enum is the value
	 */
	@Test
	public void testDecoratedEnumNewValue()
	{
		String expected;
		String actual;
		String newEnumValue = "JAGUAR";
		DecoratedEnum<Brand, String> cenum = DecoratedEnum.<Brand, String> builder()
			.value(newEnumValue).build();
		actual = cenum.name();
		expected = newEnumValue;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for check if both are not null the value of the decorated enum will be taken as
	 * name
	 */
	@Test
	public void testDecoratedEnumNewValueWithEnumValue()
	{
		String expected;
		String actual;
		String newEnumValue = "JAGUAR";
		Brand ferrari = Brand.FERRARI;
		DecoratedEnum<Brand, String> decoratedEnum = DecoratedEnum.<Brand, String> builder()
			.value(newEnumValue).enumtype(ferrari).build();

		actual = decoratedEnum.name();
		expected = ferrari.name();
		assertEquals(actual, expected);
	}

	/**
	 * Test method if both are null
	 */
	@Test
	public void testDecoratedEnumNullCheck()
	{
		String actual;
		String expected;

		NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
			() -> DecoratedEnum.<Brand, String> builder().build());
		expected = null;
		actual = exception.getMessage();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link DecoratedEnum#equals(Object)}
	 */
	@Test
	public void testEqualsObject()
	{
		Brand ferrari = Brand.FERRARI;
		final DecoratedEnum<Brand, String> expected = DecoratedEnum.<Brand, String> builder()
			.enumtype(ferrari).build();
		final DecoratedEnum<Gender, String> actual = DecoratedEnum.<Gender, String> builder()
			.enumtype(Gender.MALE).build();

		assertNotSame(expected, actual);
		final DecoratedEnum<Brand, String> brandEnum = new DecoratedEnum<>(ferrari, null);
		assertEquals(expected, brandEnum);
		assertTrue(
			EqualsEvaluator.evaluateReflexivityNonNullSymmetricAndConsistency(expected, brandEnum));
		assertTrue(EqualsEvaluator.evaluateReflexivityNonNullSymmetricConsistencyAndTransitivity(
			expected, brandEnum, new DecoratedEnum<Brand, String>(Brand.FERRARI, null)));
	}

	/**
	 * Test method for {@link DecoratedEnum#hashCode()}
	 */
	@Test
	public void testHashcode()
	{
		boolean expected;
		boolean actual;
		Brand ferrari = Brand.FERRARI;
		final DecoratedEnum<Brand, String> integerBox = DecoratedEnum.<Brand, String> builder()
			.enumtype(ferrari).build();
		DecoratedEnum<Gender, String> stringBox = DecoratedEnum.<Gender, String> builder()
			.enumtype(Gender.MALE).build();
		actual = HashcodeEvaluator.evaluateEquality(integerBox, new DecoratedEnum<>(ferrari, null));
		expected = true;
		assertEquals(expected, actual);

		expected = true;
		actual = HashcodeEvaluator.evaluateUnequality(integerBox, stringBox);
		assertEquals(expected, actual);

		actual = HashcodeEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link DecoratedEnum#toString()}
	 */
	@Test
	public void testToString()
	{
		boolean expected;
		boolean actual;
		actual = ToStringEvaluator.evaluate(DecoratedEnum.class);
		expected = true;
		assertEquals(expected, actual);

		Brand ferrari = Brand.FERRARI;
		final DecoratedEnum<Brand, String> integerBox = DecoratedEnum.<Brand, String> builder()
			.enumtype(ferrari).build();
		actual = ToStringEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);
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
