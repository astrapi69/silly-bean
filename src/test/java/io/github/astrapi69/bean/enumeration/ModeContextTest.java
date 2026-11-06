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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * The unit test class for the enum {@link ModeContext}
 */
public class ModeContextTest
{

	/**
	 * {@link ModeContext#values()} gives the four modes in the order of their declaration, which
	 * gives their ordinals
	 */
	@Test
	void valuesAreTheFourModesInTheirOrder()
	{
		final List<String> names = Arrays.stream(ModeContext.values()).map(ModeContext::name)
			.toList();
		assertEquals(List.of("CREATE", "DELETE", "NONE", "UPDATE"), names);
	}

	/**
	 * {@link ModeContext#valueOf(String)} gives the constant of each name
	 *
	 * @param mode
	 *            the constant
	 */
	@ParameterizedTest(name = "{0}")
	@EnumSource(ModeContext.class)
	void valueOfGivesTheConstantOfItsName(final ModeContext mode)
	{
		assertSame(mode, ModeContext.valueOf(mode.name()));
	}

	/**
	 * {@link ModeContext#valueOf(String)} knows no other name, also not in another case
	 *
	 * @param name
	 *            a name that is no constant
	 */
	@ParameterizedTest(name = "\"{0}\"")
	@ValueSource(strings = { "create", "READ", "" })
	void valueOfThrowsForAnUnknownName(final String name)
	{
		assertThrows(IllegalArgumentException.class, () -> ModeContext.valueOf(name));
	}
}
