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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link KeySetPair}. The cases hold the keys, and lambdas that
 * make the sets or call the builder; every test builds its pairs itself, so that PIT gives each
 * case the code it runs
 */
public class KeySetPairTest
{

	/**
	 * The hash code the formula of {@link KeySetPair#hashCode()} takes for a null key or set
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link KeySetPair#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given key or set as {@link KeySetPair#hashCode()} takes it
	 *
	 * @param content
	 *            the key or set, may be null
	 * @return the hash code of the key or set, or 43 for null
	 */
	private static int hash(final Object content)
	{
		return content == null ? NULL_HASH : content.hashCode();
	}

	/**
	 * A modifiable {@link LinkedHashSet} of the given values, in this order
	 *
	 * @param values
	 *            the values, null is allowed
	 * @return the set
	 */
	private static Set<String> linkedSet(final String... values)
	{
		return new LinkedHashSet<>(Arrays.asList(values));
	}

	/**
	 * The keys and sets of two pairs, and whether the pairs are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair, may be null
	 * @param firstValues
	 *            makes the set of the first pair in the test, may make null
	 * @param secondKey
	 *            the key of the second pair, may be null
	 * @param secondValues
	 *            makes the set of the second pair in the test, may make null
	 * @param equal
	 *            true if the pairs are equal
	 */
	record EqualsCase(String description, String firstKey, Supplier<Set<String>> firstValues,
		String secondKey, Supplier<Set<String>> secondValues, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two pairs are equal if their keys are equal and their sets are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same key and set: equal", "k", () -> Set.of("a"), "k",
				() -> Set.of("a"), true),
			new EqualsCase("key and set null in both: equal", null, () -> null, null, () -> null,
				true),
			new EqualsCase("sets of the same values, of other classes: equal", "k",
				() -> new HashSet<>(Set.of("a", "b")), "k", () -> new TreeSet<>(Set.of("a", "b")),
				true),
			new EqualsCase("the same values in another order: equal", "k",
				() -> linkedSet("a", "b"), "k", () -> linkedSet("b", "a"), true),
			new EqualsCase("another key: not equal", "k", () -> Set.of("a"), "x", () -> Set.of("a"),
				false),
			new EqualsCase("another set: not equal", "k", () -> Set.of("a"), "k", () -> Set.of("b"),
				false),
			new EqualsCase("key null in the first only: not equal", null, () -> Set.of("a"), "k",
				() -> Set.of("a"), false),
			new EqualsCase("key null in the second only: not equal", "k", () -> Set.of("a"), null,
				() -> Set.of("a"), false),
			new EqualsCase("set null in the first only: not equal", "k", () -> null, "k",
				() -> Set.of("a"), false),
			new EqualsCase("set null in the second only: not equal", "k", () -> Set.of("a"), "k",
				() -> null, false),
			new EqualsCase("an empty set and a null set: not equal", "k", () -> Set.of(), "k",
				() -> null, false));
	}

	/**
	 * {@link KeySetPair#equals(Object)} compares the key and the set, both ways round; equal pairs
	 * have the same {@link KeySetPair#hashCode()}, and for these keys and sets unequal pairs have
	 * different ones
	 *
	 * @param testCase
	 *            the keys, the sets and whether the pairs are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesKeyAndSet(final EqualsCase testCase)
	{
		final KeySetPair<String, String> first = new KeySetPair<>(testCase.firstKey(),
			testCase.firstValues().get());
		final KeySetPair<String, String> second = new KeySetPair<>();
		second.setKey(testCase.secondKey());
		second.setValues(testCase.secondValues().get());
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An object to compare a pair of key "k" and the set [a] with, and whether it is equal to the
	 * pair
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param equal
	 *            true if the pair equals the object
	 */
	record OtherCase(String description, Function<KeySetPair<String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a pair of another key or set; {@link KeySetPair} is final, there is no
	 * subclass
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the pair itself: equal", pair -> pair, true),
			new OtherCase("null: not equal", pair -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				pair -> pair.toString(), false),
			new OtherCase("a KeyValuesPair of the same key and set: not equal",
				pair -> new KeyValuesPair<>(pair.getKey(), pair.getValues()), false));
	}

	/**
	 * {@link KeySetPair#equals(Object)} of a pair and another object
	 *
	 * @param testCase
	 *            the object and whether the pair equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final KeySetPair<String, String> pair = new KeySetPair<>("k", Set.of("a"));
		assertEquals(testCase.equal(), pair.equals(testCase.other().apply(pair)));
	}

	/**
	 * The key and set of a pair, and its {@link KeySetPair#toString()}
	 *
	 * @param description
	 *            the name of the case
	 * @param key
	 *            the key, may be null
	 * @param values
	 *            makes the set in the test, may make null
	 * @param pairText
	 *            the toString of the pair
	 */
	record ContentCase(String description, String key, Supplier<Set<String>> values,
		String pairText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs with and without key and set
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("key and set", "k", () -> linkedSet("a", "b"),
				"KeySetPair(key=k, values=[a, b])"),
			new ContentCase("the key null", null, () -> linkedSet("a"),
				"KeySetPair(key=null, values=[a])"),
			new ContentCase("the set null", "k", () -> null, "KeySetPair(key=k, values=null)"),
			new ContentCase("an empty set", "k", () -> linkedSet(), "KeySetPair(key=k, values=[])"),
			new ContentCase("key and set null", null, () -> null,
				"KeySetPair(key=null, values=null)"));
	}

	/**
	 * {@link KeySetPair#hashCode()} is the formula Lombok writes,
	 * {@code (59 + hash(key)) * 59 + hash(values)} with 43 for null; the contract of
	 * {@link Object#hashCode()} would allow any value that agrees with equals, the test pins this
	 * one so that a change shows
	 *
	 * @param testCase
	 *            the key and set
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfKeyAndSet(final ContentCase testCase)
	{
		final Set<String> values = testCase.values().get();
		final KeySetPair<String, String> pair = new KeySetPair<>(testCase.key(), values);
		assertEquals((PRIME + hash(testCase.key())) * PRIME + hash(values), pair.hashCode());
	}

	/**
	 * {@link KeySetPair#toString()} names the key and the set; the constructor and the getters keep
	 * both, the set itself
	 *
	 * @param testCase
	 *            the key, the set and the text
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesKeyAndSet(final ContentCase testCase)
	{
		final Set<String> values = testCase.values().get();
		final KeySetPair<String, String> pair = new KeySetPair<>(testCase.key(), values);
		assertEquals(testCase.pairText(), pair.toString());
		assertEquals(testCase.key(), pair.getKey());
		assertSame(values, pair.getValues());
	}

	/**
	 * Calls of the builder methods on a builder of key "k", and the texts of the builder and of the
	 * pair it builds
	 *
	 * @param description
	 *            the name of the case
	 * @param calls
	 *            the calls of the builder methods
	 * @param builderText
	 *            the toString of the builder after the calls
	 * @param pairText
	 *            the toString of the pair it builds
	 */
	record BuilderCase(String description,
		UnaryOperator<KeySetPair.KeySetPairBuilder<String, String>> calls, String builderText,
		String pairText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The builder methods value, values and clearValues, alone and after each other
	 *
	 * @return the cases
	 */
	static Stream<BuilderCase> builderCases()
	{
		return Stream.of(
			new BuilderCase("no value: an empty set", builder -> builder,
				"KeySetPair.KeySetPairBuilder(key=k, values=null)", "KeySetPair(key=k, values=[])"),
			new BuilderCase("one value: a set of it", builder -> builder.value("a"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[a])", "KeySetPair(key=k, values=[a])"),
			new BuilderCase("two values: both, in the order given",
				builder -> builder.value("b").value("a"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[b, a])",
				"KeySetPair(key=k, values=[b, a])"),
			new BuilderCase("three values: all, in the order given",
				builder -> builder.value("c").value("a").value("b"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[c, a, b])",
				"KeySetPair(key=k, values=[c, a, b])"),
			new BuilderCase("the same value twice: twice in the builder, once in the set",
				builder -> builder.value("a").value("a"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[a, a])",
				"KeySetPair(key=k, values=[a])"),
			new BuilderCase("a null value: kept", builder -> builder.value(null),
				"KeySetPair.KeySetPairBuilder(key=k, values=[null])",
				"KeySetPair(key=k, values=[null])"),
			new BuilderCase("values of a collection: all, in its order",
				builder -> builder.values(List.of("b", "a")),
				"KeySetPair.KeySetPairBuilder(key=k, values=[b, a])",
				"KeySetPair(key=k, values=[b, a])"),
			new BuilderCase("values of an empty collection: an empty set",
				builder -> builder.values(List.of()),
				"KeySetPair.KeySetPairBuilder(key=k, values=[])", "KeySetPair(key=k, values=[])"),
			new BuilderCase("values, then value: appended",
				builder -> builder.values(List.of("a", "b")).value("c"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[a, b, c])",
				"KeySetPair(key=k, values=[a, b, c])"),
			new BuilderCase("values twice: appended, not replaced",
				builder -> builder.values(List.of("a")).values(List.of("b", "a")),
				"KeySetPair.KeySetPairBuilder(key=k, values=[a, b, a])",
				"KeySetPair(key=k, values=[a, b])"),
			new BuilderCase("clearValues of a new builder: nothing to clear",
				builder -> builder.clearValues(),
				"KeySetPair.KeySetPairBuilder(key=k, values=null)", "KeySetPair(key=k, values=[])"),
			new BuilderCase("clearValues after values: an empty set",
				builder -> builder.value("a").value("b").clearValues(),
				"KeySetPair.KeySetPairBuilder(key=k, values=[])", "KeySetPair(key=k, values=[])"),
			new BuilderCase("clearValues, then value: the later value only",
				builder -> builder.value("a").clearValues().value("b"),
				"KeySetPair.KeySetPairBuilder(key=k, values=[b])",
				"KeySetPair(key=k, values=[b])"));
	}

	/**
	 * The builder collects the values in a list, duplicates and null included, and
	 * {@link KeySetPair.KeySetPairBuilder#build()} makes an unmodifiable set of them in their
	 * order; without a value the set is empty, not null
	 *
	 * @param testCase
	 *            the calls and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("builderCases")
	void builderCollectsTheValuesIntoAnUnmodifiableSet(final BuilderCase testCase)
	{
		final KeySetPair.KeySetPairBuilder<String, String> builder = testCase.calls()
			.apply(KeySetPair.<String, String> builder().key("k"));
		assertEquals(testCase.builderText(), builder.toString());
		final KeySetPair<String, String> pair = builder.build();
		assertEquals(testCase.pairText(), pair.toString());
		assertEquals("k", pair.getKey());
		assertThrows(UnsupportedOperationException.class, () -> pair.getValues().add("x"));
	}

	/**
	 * {@link KeySetPair.KeySetPairBuilder#values(java.util.Collection)} copies the given values and
	 * {@link KeySetPair.KeySetPairBuilder#build()} copies the values of the builder: a later change
	 * of the given collection or of the builder leaves a built pair as it is
	 */
	@Test
	void builderAndBuildCopyTheValues()
	{
		final List<String> given = new ArrayList<>(List.of("a"));
		final KeySetPair.KeySetPairBuilder<String, String> builder = KeySetPair
			.<String, String> builder().values(given);
		given.add("x");
		final KeySetPair<String, String> one = builder.build();
		builder.value("b");
		final KeySetPair<String, String> two = builder.build();
		builder.value("c").clearValues();
		assertEquals(Set.of("a"), one.getValues());
		assertEquals(Set.of("a", "b"), two.getValues());
		assertEquals(Set.of(), builder.build().getValues());
	}

	/**
	 * {@link KeySetPair.KeySetPairBuilder#values(java.util.Collection)} of null throws a
	 * {@link NullPointerException}; its Javadoc does not say so
	 */
	@Test
	void valuesOfNullThrowsANullPointerException()
	{
		final KeySetPair.KeySetPairBuilder<String, String> builder = KeySetPair.builder();
		assertThrows(NullPointerException.class, () -> builder.values(null));
	}

	/**
	 * The set of a pair to give to {@link KeySetPair#toBuilder()}, and what the copy is
	 *
	 * @param description
	 *            the name of the case
	 * @param values
	 *            makes the set of the pair in the test, may make null
	 * @param equal
	 *            true if the copy equals the pair
	 * @param copyText
	 *            the toString of the copy
	 */
	record ToBuilderCase(String description, Supplier<Set<String>> values, boolean equal,
		String copyText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs of a set, of an empty set and of no set
	 *
	 * @return the cases
	 */
	static Stream<ToBuilderCase> toBuilderCases()
	{
		return Stream.of(
			new ToBuilderCase("a set of one: an equal copy", () -> linkedSet("a"), true,
				"KeySetPair(key=k, values=[a])"),
			new ToBuilderCase("a set of two: an equal copy, in the order of the set",
				() -> linkedSet("b", "a"), true, "KeySetPair(key=k, values=[b, a])"),
			new ToBuilderCase("an empty set: an equal copy", () -> linkedSet(), true,
				"KeySetPair(key=k, values=[])"),
			new ToBuilderCase("no set (null): a copy of an empty set, not equal", () -> null, false,
				"KeySetPair(key=k, values=[])"));
	}

	/**
	 * {@link KeySetPair#toBuilder()} gives a builder of the key and of the values of the set; a
	 * pair of no set (null) gives a copy of an empty set, which is not equal to the pair
	 *
	 * @param testCase
	 *            the set and the copy
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("toBuilderCases")
	void toBuilderCopiesKeyAndValues(final ToBuilderCase testCase)
	{
		final KeySetPair<String, String> pair = new KeySetPair<>("k", testCase.values().get());
		final KeySetPair<String, String> copy = pair.toBuilder().build();
		assertEquals(testCase.equal(), pair.equals(copy));
		assertEquals(testCase.copyText(), copy.toString());
		assertNotSame(pair, copy);
	}

	/**
	 * {@link KeySetPair#toBuilder()} copies the values into the builder: adding or clearing values
	 * on the builder leaves the pair and its set as they are
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final KeySetPair<String, String> pair = new KeySetPair<>("k", linkedSet("a"));
		final KeySetPair<String, String> added = pair.toBuilder().key("x").value("b").build();
		final KeySetPair<String, String> cleared = pair.toBuilder().clearValues().build();
		assertEquals(new KeySetPair<>("x", Set.of("a", "b")), added);
		assertEquals(new KeySetPair<>("k", Set.of()), cleared);
		assertEquals(new KeySetPair<>("k", Set.of("a")), pair);
	}

	/**
	 * The no-argument constructor leaves key and set null, and the setters set them
	 */
	@Test
	void settersSetKeyAndSetOfAnEmptyPair()
	{
		final KeySetPair<String, String> pair = new KeySetPair<>();
		assertNull(pair.getKey());
		assertNull(pair.getValues());
		final Set<String> values = linkedSet("a");
		pair.setKey("k");
		pair.setValues(values);
		assertEquals("k", pair.getKey());
		assertSame(values, pair.getValues());
	}

	/**
	 * A {@link KeySetPair} is {@link java.io.Serializable}: a serialized and read pair of the set
	 * the builder makes equals the pair
	 *
	 * @throws IOException
	 *             if the pair cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read pair is not found
	 */
	@Test
	void aSerializedKeySetPairReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final KeySetPair<String, String> pair = KeySetPair.<String, String> builder().key("k")
			.value("a").value("b").build();
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
	 * Test method for {@link KeySetPair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeySetPair.class);
	}
}
