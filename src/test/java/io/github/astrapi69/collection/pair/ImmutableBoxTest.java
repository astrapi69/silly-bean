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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.evaluate.object.evaluators.EqualsEvaluator;
import io.github.astrapi69.evaluate.object.evaluators.HashcodeEvaluator;
import io.github.astrapi69.evaluate.object.evaluators.ToStringEvaluator;
import io.github.astrapi69.evaluate.object.verifier.ContractVerifier;

/**
 * The unit test class for the class {@link ImmutableBox}.
 */
public class ImmutableBoxTest
{

	/**
	 * Test method for {@link ImmutableBox#equals(Object)}
	 */
	@Test
	public void testEqualsObject()
	{
		final ImmutableBox<Integer> expected = ImmutableBox.<Integer> builder().value(2).build();
		final ImmutableBox<String> actual = new ImmutableBox<>("Hello");

		assertNotSame(expected, actual);
		final ImmutableBox<Integer> integerBox = new ImmutableBox<>(2);
		assertEquals(expected, integerBox);
		assertTrue(
			EqualsEvaluator.evaluateReflexivityNonNullSymmetricAndConsistency(expected, actual));
		assertTrue(EqualsEvaluator.evaluateReflexivityNonNullSymmetricConsistencyAndTransitivity(
			expected, integerBox, new ImmutableBox<>(2)));
	}

	/**
	 * Test method for {@link ImmutableBox#hashCode()}
	 */
	@Test
	public void testHashcode()
	{
		boolean expected;
		boolean actual;
		final ImmutableBox<Integer> integerBox = ImmutableBox.<Integer> builder().value(2).build();
		actual = HashcodeEvaluator.evaluateEquality(integerBox, new ImmutableBox<>(2));
		expected = true;
		assertEquals(expected, actual);

		actual = HashcodeEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);

		expected = true;
		ImmutableBox<String> stringBox = ImmutableBox.<String> builder().value("Hello").build();
		actual = HashcodeEvaluator.evaluateUnequality(integerBox, stringBox);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ImmutableBox}
	 */
	@Test
	public void testImmutableBoxNullValue()
	{

		String actual;
		String expected;

		NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
			() -> ImmutableBox.<Integer> builder().build());
		expected = "value is marked non-null but is null";
		actual = exception.getMessage();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ImmutableBox#toString()}
	 */
	@Test
	public void testToString()
	{
		boolean expected;
		boolean actual;
		actual = ToStringEvaluator.evaluate(ImmutableBox.class);
		expected = true;
		assertEquals(expected, actual);

		final ImmutableBox<Integer> integerBox = ImmutableBox.<Integer> builder().value(2).build();

		actual = ToStringEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for verify the contracts of {@link ImmutableBox#equals(Object)} ,
	 * {@link ImmutableBox#hashCode()} and {@link ImmutableBox#toString()}
	 */
	@Test
	@Disabled
	public void verifyEqualsHashcodeAndToStringContracts()
	{
		ContractVerifier.of(ImmutableBox.class).verify();
	}

}
