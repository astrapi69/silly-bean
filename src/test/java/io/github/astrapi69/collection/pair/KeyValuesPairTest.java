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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link KeyValuesPair}. The cases hold the keys, and lambdas
 * that make the collections or call the builder; every test builds its pairs itself, so that PIT
 * gives each case the code it runs
 */
public class KeyValuesPairTest
{

	/**
	 * The hash code the formula of {@link KeyValuesPair#hashCode()} takes for a null key or
	 * collection
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link KeyValuesPair#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given key or collection as {@link KeyValuesPair#hashCode()} takes it
	 *
	 * @param content
	 *            the key or collection, may be null
	 * @return the hash code of the key or collection, or 43 for null
	 */
	private static int hash(final Object content)
	{
		return content == null ? NULL_HASH : content.hashCode();
	}

	/**
	 * A modifiable {@link ArrayList} of the given values, in this order
	 *
	 * @param values
	 *            the values, null is allowed
	 * @return the list
	 */
	private static List<String> arrayList(final String... values)
	{
		return new ArrayList<>(Arrays.asList(values));
	}

	/**
	 * The keys and collections of two pairs, and whether the pairs are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair, may be null
	 * @param firstValues
	 *            makes the collection of the first pair in the test, may make null
	 * @param secondKey
	 *            the key of the second pair, may be null
	 * @param secondValues
	 *            makes the collection of the second pair in the test, may make null
	 * @param equal
	 *            true if the pairs are equal
	 */
	record EqualsCase(String description, String firstKey, Supplier<Collection<String>> firstValues,
		String secondKey, Supplier<Collection<String>> secondValues, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two pairs are equal if their keys are equal and their collections are equal by the equals of
	 * the collection, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same key and list: equal", "k", () -> List.of("a"), "k",
				() -> List.of("a"), true),
			new EqualsCase("key and collection null in both: equal", null, () -> null, null,
				() -> null, true),
			new EqualsCase("lists of the same values, of other classes: equal", "k",
				() -> arrayList("a", "b"), "k", () -> new LinkedList<>(List.of("a", "b")), true),
			new EqualsCase("lists of the same values in another order: not equal", "k",
				() -> arrayList("a", "b"), "k", () -> arrayList("b", "a"), false),
			new EqualsCase("a list and a set of the same value: not equal", "k", () -> List.of("a"),
				"k", () -> Set.of("a"), false),
			new EqualsCase("another key: not equal", "k", () -> List.of("a"), "x",
				() -> List.of("a"), false),
			new EqualsCase("another list: not equal", "k", () -> List.of("a"), "k",
				() -> List.of("b"), false),
			new EqualsCase("key null in the first only: not equal", null, () -> List.of("a"), "k",
				() -> List.of("a"), false),
			new EqualsCase("key null in the second only: not equal", "k", () -> List.of("a"), null,
				() -> List.of("a"), false),
			new EqualsCase("collection null in the first only: not equal", "k", () -> null, "k",
				() -> List.of("a"), false),
			new EqualsCase("collection null in the second only: not equal", "k", () -> List.of("a"),
				"k", () -> null, false),
			new EqualsCase("an empty list and a null collection: not equal", "k", () -> List.of(),
				"k", () -> null, false));
	}

	/**
	 * {@link KeyValuesPair#equals(Object)} compares the key and the collection, both ways round;
	 * equal pairs have the same {@link KeyValuesPair#hashCode()}, and for these keys and
	 * collections unequal pairs have different ones
	 *
	 * @param testCase
	 *            the keys, the collections and whether the pairs are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesKeyAndCollection(final EqualsCase testCase)
	{
		final KeyValuesPair<String, String> first = new KeyValuesPair<>(testCase.firstKey(),
			testCase.firstValues().get());
		final KeyValuesPair<String, String> second = new KeyValuesPair<>();
		second.setKey(testCase.secondKey());
		second.setValues(testCase.secondValues().get());
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An object to compare a pair of key "k" and the list [a] with, and whether it is equal to the
	 * pair
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param equal
	 *            true if the pair equals the object
	 */
	record OtherCase(String description, Function<KeyValuesPair<String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a pair of another key or collection; {@link KeyValuesPair} is final,
	 * there is no subclass
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the pair itself: equal", pair -> pair, true),
			new OtherCase("null: not equal", pair -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				pair -> pair.toString(), false),
			new OtherCase("a KeyValuePair of the same key and list: not equal",
				pair -> new KeyValuePair<>(pair.getKey(), pair.getValues()), false));
	}

	/**
	 * {@link KeyValuesPair#equals(Object)} of a pair and another object
	 *
	 * @param testCase
	 *            the object and whether the pair equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>("k", List.of("a"));
		assertEquals(testCase.equal(), pair.equals(testCase.other().apply(pair)));
	}

	/**
	 * The key and collection of a pair, and its {@link KeyValuesPair#toString()}
	 *
	 * @param description
	 *            the name of the case
	 * @param key
	 *            the key, may be null
	 * @param values
	 *            makes the collection in the test, may make null
	 * @param pairText
	 *            the toString of the pair
	 */
	record ContentCase(String description, String key, Supplier<Collection<String>> values,
		String pairText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs with and without key and collection
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("key and list", "k", () -> arrayList("a", "b"),
				"KeyValuesPair(key=k, values=[a, b])"),
			new ContentCase("the key null", null, () -> arrayList("a"),
				"KeyValuesPair(key=null, values=[a])"),
			new ContentCase("the collection null", "k", () -> null,
				"KeyValuesPair(key=k, values=null)"),
			new ContentCase("an empty list", "k", () -> arrayList(),
				"KeyValuesPair(key=k, values=[])"),
			new ContentCase("key and collection null", null, () -> null,
				"KeyValuesPair(key=null, values=null)"));
	}

	/**
	 * {@link KeyValuesPair#hashCode()} is the formula Lombok writes,
	 * {@code (59 + hash(key)) * 59 + hash(values)} with 43 for null; the contract of
	 * {@link Object#hashCode()} would allow any value that agrees with equals, the test pins this
	 * one so that a change shows
	 *
	 * @param testCase
	 *            the key and collection
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfKeyAndCollection(final ContentCase testCase)
	{
		final Collection<String> values = testCase.values().get();
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>(testCase.key(), values);
		assertEquals((PRIME + hash(testCase.key())) * PRIME + hash(values), pair.hashCode());
	}

	/**
	 * {@link KeyValuesPair#toString()} names the key and the collection; the constructor and the
	 * getters keep both, the collection itself
	 *
	 * @param testCase
	 *            the key, the collection and the text
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesKeyAndCollection(final ContentCase testCase)
	{
		final Collection<String> values = testCase.values().get();
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>(testCase.key(), values);
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
		UnaryOperator<KeyValuesPair.KeyValuesPairBuilder<String, String>> calls, String builderText,
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
			new BuilderCase("no value: an empty list", builder -> builder,
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=null)",
				"KeyValuesPair(key=k, values=[])"),
			new BuilderCase("one value: a list of it", builder -> builder.value("a"),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[a])",
				"KeyValuesPair(key=k, values=[a])"),
			new BuilderCase("two values: both, in the order given",
				builder -> builder.value("b").value("a"),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[b, a])",
				"KeyValuesPair(key=k, values=[b, a])"),
			new BuilderCase("the same value twice: twice in the list",
				builder -> builder.value("a").value("a"),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[a, a])",
				"KeyValuesPair(key=k, values=[a, a])"),
			new BuilderCase("a null value: kept", builder -> builder.value(null),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[null])",
				"KeyValuesPair(key=k, values=[null])"),
			new BuilderCase("values of a collection: all, in its order",
				builder -> builder.values(List.of("b", "a")),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[b, a])",
				"KeyValuesPair(key=k, values=[b, a])"),
			new BuilderCase("values of an empty collection: an empty list",
				builder -> builder.values(List.of()),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[])",
				"KeyValuesPair(key=k, values=[])"),
			new BuilderCase("values, then value: appended",
				builder -> builder.values(List.of("a", "b")).value("c"),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[a, b, c])",
				"KeyValuesPair(key=k, values=[a, b, c])"),
			new BuilderCase("values twice: appended, not replaced",
				builder -> builder.values(List.of("a")).values(List.of("b", "a")),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[a, b, a])",
				"KeyValuesPair(key=k, values=[a, b, a])"),
			new BuilderCase("clearValues of a new builder: nothing to clear",
				builder -> builder.clearValues(),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=null)",
				"KeyValuesPair(key=k, values=[])"),
			new BuilderCase("clearValues after values: an empty list",
				builder -> builder.value("a").value("b").clearValues(),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[])",
				"KeyValuesPair(key=k, values=[])"),
			new BuilderCase("clearValues, then value: the later value only",
				builder -> builder.value("a").clearValues().value("b"),
				"KeyValuesPair.KeyValuesPairBuilder(key=k, values=[b])",
				"KeyValuesPair(key=k, values=[b])"));
	}

	/**
	 * The builder collects the values in a list, duplicates and null included, and
	 * {@link KeyValuesPair.KeyValuesPairBuilder#build()} makes an unmodifiable list of them in
	 * their order; without a value the list is empty, not null
	 *
	 * @param testCase
	 *            the calls and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("builderCases")
	void builderCollectsTheValuesIntoAnUnmodifiableList(final BuilderCase testCase)
	{
		final KeyValuesPair.KeyValuesPairBuilder<String, String> builder = testCase.calls()
			.apply(KeyValuesPair.<String, String> builder().key("k"));
		assertEquals(testCase.builderText(), builder.toString());
		final KeyValuesPair<String, String> pair = builder.build();
		assertEquals(testCase.pairText(), pair.toString());
		assertEquals("k", pair.getKey());
		assertThrows(UnsupportedOperationException.class, () -> pair.getValues().add("x"));
	}

	/**
	 * {@link KeyValuesPair.KeyValuesPairBuilder#values(java.util.Collection)} copies the given
	 * values and {@link KeyValuesPair.KeyValuesPairBuilder#build()} copies the values of the
	 * builder: a later change of the given collection or of the builder leaves a built pair as it
	 * is
	 */
	@Test
	void builderAndBuildCopyTheValues()
	{
		final List<String> given = arrayList("a");
		final KeyValuesPair.KeyValuesPairBuilder<String, String> builder = KeyValuesPair
			.<String, String> builder().values(given);
		given.add("x");
		final KeyValuesPair<String, String> one = builder.build();
		builder.value("b");
		final KeyValuesPair<String, String> two = builder.build();
		builder.value("c").clearValues();
		assertEquals(List.of("a"), one.getValues());
		assertEquals(List.of("a", "b"), two.getValues());
		assertEquals(List.of(), builder.build().getValues());
	}

	/**
	 * {@link KeyValuesPair.KeyValuesPairBuilder#values(java.util.Collection)} of null throws a
	 * {@link NullPointerException}; its Javadoc does not say so
	 */
	@Test
	void valuesOfNullThrowsANullPointerException()
	{
		final KeyValuesPair.KeyValuesPairBuilder<String, String> builder = KeyValuesPair.builder();
		assertThrows(NullPointerException.class, () -> builder.values(null));
	}

	/**
	 * The collection of a pair to give to {@link KeyValuesPair#toBuilder()}, and what the copy is
	 *
	 * @param description
	 *            the name of the case
	 * @param values
	 *            makes the collection of the pair in the test, may make null
	 * @param equal
	 *            true if the copy equals the pair
	 * @param copyText
	 *            the toString of the copy
	 */
	record ToBuilderCase(String description, Supplier<Collection<String>> values, boolean equal,
		String copyText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs of a list, of an empty list, of a set and of no collection
	 *
	 * @return the cases
	 */
	static Stream<ToBuilderCase> toBuilderCases()
	{
		return Stream.of(
			new ToBuilderCase("a list of one: an equal copy", () -> arrayList("a"), true,
				"KeyValuesPair(key=k, values=[a])"),
			new ToBuilderCase("a list of two: an equal copy, in the order of the list",
				() -> arrayList("b", "a"), true, "KeyValuesPair(key=k, values=[b, a])"),
			new ToBuilderCase("an empty list: an equal copy", () -> arrayList(), true,
				"KeyValuesPair(key=k, values=[])"),
			new ToBuilderCase("a set: a copy of a list of the same text, not equal",
				() -> Set.of("a"), false, "KeyValuesPair(key=k, values=[a])"),
			new ToBuilderCase("no collection (null): a copy of an empty list, not equal",
				() -> null, false, "KeyValuesPair(key=k, values=[])"));
	}

	/**
	 * {@link KeyValuesPair#toBuilder()} gives a builder of the key and of the values of the
	 * collection, and the builder builds a list: a pair of a set gives a copy of a list, a pair of
	 * no collection (null) a copy of an empty list, and neither copy is equal to the pair
	 *
	 * @param testCase
	 *            the collection and the copy
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("toBuilderCases")
	void toBuilderCopiesKeyAndValuesIntoAList(final ToBuilderCase testCase)
	{
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>("k",
			testCase.values().get());
		final KeyValuesPair<String, String> copy = pair.toBuilder().build();
		assertEquals(testCase.equal(), pair.equals(copy));
		assertEquals(testCase.copyText(), copy.toString());
		assertNotSame(pair, copy);
	}

	/**
	 * {@link KeyValuesPair#toBuilder()} copies the values into the builder: adding or clearing
	 * values on the builder leaves the pair and its collection as they are
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>("k", arrayList("a"));
		final KeyValuesPair<String, String> added = pair.toBuilder().key("x").value("b").build();
		final KeyValuesPair<String, String> cleared = pair.toBuilder().clearValues().build();
		assertEquals(new KeyValuesPair<>("x", List.of("a", "b")), added);
		assertEquals(new KeyValuesPair<>("k", List.of()), cleared);
		assertEquals(new KeyValuesPair<>("k", List.of("a")), pair);
	}

	/**
	 * The no-argument constructor leaves key and collection null, and the setters set them
	 */
	@Test
	void settersSetKeyAndCollectionOfAnEmptyPair()
	{
		final KeyValuesPair<String, String> pair = new KeyValuesPair<>();
		assertNull(pair.getKey());
		assertNull(pair.getValues());
		final List<String> values = arrayList("a");
		pair.setKey("k");
		pair.setValues(values);
		assertEquals("k", pair.getKey());
		assertSame(values, pair.getValues());
	}

	/**
	 * A {@link KeyValuesPair} is {@link java.io.Serializable}: a serialized and read pair of the
	 * list the builder makes equals the pair
	 *
	 * @throws IOException
	 *             if the pair cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read pair is not found
	 */
	@Test
	void aSerializedKeyValuesPairReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final KeyValuesPair<String, String> pair = KeyValuesPair.<String, String> builder().key("k")
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

}
