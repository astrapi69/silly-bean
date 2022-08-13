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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;
import io.github.astrapi69.test.object.Customer;
import io.github.astrapi69.test.object.Employee;
import io.github.astrapi69.test.object.Person;
import io.github.astrapi69.test.object.enumtype.Brand;

/**
 * The unit test class for the class {@link Quattro}.
 */
public class QuattroTest
{

	/**
	 * Test method for {@link Quattro#equals(Object)} , {@link Quattro#hashCode()} and
	 * {@link Quattro#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{

		boolean expected;
		boolean actual;

		final Customer customer = Customer.builder().build();

		final Brand ferrari = Brand.FERRARI;

		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();

		final Employee employee = Employee.builder().id("20").person(person).build();

		final Quattro<Integer, Employee, Customer, Brand> first = Quattro
			.<Integer, Employee, Customer, Brand> builder().topLeft(1).topRight(employee)
			.bottomLeft(customer).bottomRight(ferrari).build();
		final Quattro<String, Employee, Customer, Brand> second = Quattro
			.<String, Employee, Customer, Brand> builder().topLeft("left").topRight(employee)
			.bottomLeft(customer).bottomRight(ferrari).build();
		Integer topLeft = 1;
		final Quattro<Integer, Employee, Customer, Brand> third = new Quattro<>(customer, ferrari,
			topLeft, employee);
		final Quattro<Integer, Employee, Customer, Brand> fourth = new Quattro<>();
		fourth.setTopLeft(1);
		fourth.setTopRight(employee);
		fourth.setBottomLeft(customer);
		fourth.setBottomRight(ferrari);

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
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
