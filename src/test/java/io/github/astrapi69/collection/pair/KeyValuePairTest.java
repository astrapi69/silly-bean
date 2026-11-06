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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link KeyValuePair}. The cases hold the keys and values only,
 * or lambdas that make the maps, properties and lists; every test builds its pairs itself, so that
 * PIT gives each case the code it runs
 */
public class KeyValuePairTest
{

	/**
	 * The hash code the formula of {@link KeyValuePair#hashCode()} takes for a null key or value
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link KeyValuePair#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given key or value as {@link KeyValuePair#hashCode()} takes it
	 *
	 * @param content
	 *            the key or value, may be null
	 * @return the hash code of the key or value, or 43 for null
	 */
	private static int hash(final Object content)
	{
		return content == null ? NULL_HASH : content.hashCode();
	}

	/**
	 * A {@link LinkedHashMap} of the given keys and values, in this order
	 *
	 * @param keysAndValues
	 *            a key, its value, the next key, its value and so on; null is allowed
	 * @return the map
	 */
	private static Map<String, String> linkedMap(final String... keysAndValues)
	{
		final Map<String, String> map = new LinkedHashMap<>();
		for (int index = 0; index < keysAndValues.length; index += 2)
		{
			map.put(keysAndValues[index], keysAndValues[index + 1]);
		}
		return map;
	}

	/**
	 * The keys and values of two pairs, and whether the pairs are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair, may be null
	 * @param firstValue
	 *            the value of the first pair, may be null
	 * @param secondKey
	 *            the key of the second pair, may be null
	 * @param secondValue
	 *            the value of the second pair, may be null
	 * @param equal
	 *            true if the pairs are equal
	 */
	record EqualsCase(String description, String firstKey, String firstValue, String secondKey,
		String secondValue, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two pairs are equal if their keys are equal and their values are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(new EqualsCase("the same key and value: equal", "a", "b", "a", "b", true),
			new EqualsCase("key and value null in both: equal", null, null, null, null, true),
			new EqualsCase("another key: not equal", "a", "b", "x", "b", false),
			new EqualsCase("another value: not equal", "a", "b", "a", "x", false),
			new EqualsCase("key null in the first only: not equal", null, "b", "a", "b", false),
			new EqualsCase("key null in the second only: not equal", "a", "b", null, "b", false),
			new EqualsCase("value null in the first only: not equal", "a", null, "a", "b", false),
			new EqualsCase("value null in the second only: not equal", "a", "b", "a", null, false),
			new EqualsCase("key and value swapped: not equal", "a", "b", "b", "a", false));
	}

	/**
	 * {@link KeyValuePair#equals(Object)} compares the key and the value, both ways round; equal
	 * pairs have the same {@link KeyValuePair#hashCode()}, and for these keys and values unequal
	 * pairs have different ones
	 *
	 * @param testCase
	 *            the keys, the values and whether the pairs are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesKeyAndValue(final EqualsCase testCase)
	{
		final KeyValuePair<String, String> first = new KeyValuePair<>(testCase.firstKey(),
			testCase.firstValue());
		final KeyValuePair<String, String> second = KeyValuePair.<String, String> builder()
			.key(testCase.secondKey()).value(testCase.secondValue()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An object to compare a pair of key "a" and value "b" with, and whether it is equal to the
	 * pair
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param equal
	 *            true if the pair equals the object
	 */
	record OtherCase(String description, Function<KeyValuePair<String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a pair of another key or value; {@link KeyValuePair} is final, there
	 * is no subclass
	 *
	 * @return the cases
	 */
	static Stream<OtherCase> otherCases()
	{
		return Stream.of(new OtherCase("the pair itself: equal", pair -> pair, true),
			new OtherCase("null: not equal", pair -> null, false),
			new OtherCase("a String of the same text as its toString: not equal",
				pair -> pair.toString(), false),
			new OtherCase("a Pair of the same contents: not equal",
				pair -> new Pair<>(pair.getKey(), pair.getValue()), false),
			new OtherCase("a map entry of the same key and value: not equal",
				pair -> Map.entry(pair.getKey(), pair.getValue()), false));
	}

	/**
	 * {@link KeyValuePair#equals(Object)} of a pair and another object
	 *
	 * @param testCase
	 *            the object and whether the pair equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final KeyValuePair<String, String> pair = new KeyValuePair<>("a", "b");
		assertEquals(testCase.equal(), pair.equals(testCase.other().apply(pair)));
	}

	/**
	 * The key and value of a pair, its {@link KeyValuePair#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param key
	 *            the key, may be null
	 * @param value
	 *            the value, may be null
	 * @param pairText
	 *            the toString of the pair
	 * @param builderText
	 *            the toString of a builder of this key and value
	 */
	record ContentCase(String description, String key, String value, String pairText,
		String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs with and without key and value
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(
			new ContentCase("key and value", "a", "b", "KeyValuePair(key=a, value=b)",
				"KeyValuePair.KeyValuePairBuilder(key=a, value=b)"),
			new ContentCase("the key null", null, "b", "KeyValuePair(key=null, value=b)",
				"KeyValuePair.KeyValuePairBuilder(key=null, value=b)"),
			new ContentCase("the value null", "a", null, "KeyValuePair(key=a, value=null)",
				"KeyValuePair.KeyValuePairBuilder(key=a, value=null)"),
			new ContentCase("key and value null", null, null, "KeyValuePair(key=null, value=null)",
				"KeyValuePair.KeyValuePairBuilder(key=null, value=null)"));
	}

	/**
	 * {@link KeyValuePair#hashCode()} is the formula Lombok writes,
	 * {@code (59 + hash(key)) * 59 + hash(value)} with 43 for null; the contract of
	 * {@link Object#hashCode()} would allow any value that agrees with equals, the test pins this
	 * one so that a change shows
	 *
	 * @param testCase
	 *            the key and value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfKeyAndValue(final ContentCase testCase)
	{
		final KeyValuePair<String, String> pair = new KeyValuePair<>(testCase.key(),
			testCase.value());
		assertEquals((PRIME + hash(testCase.key())) * PRIME + hash(testCase.value()),
			pair.hashCode());
	}

	/**
	 * {@link KeyValuePair#toString()} and {@link KeyValuePair.KeyValuePairBuilder#toString()} name
	 * the key and the value
	 *
	 * @param testCase
	 *            the key, the value and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesKeyAndValue(final ContentCase testCase)
	{
		final KeyValuePair.KeyValuePairBuilder<String, String> builder = KeyValuePair
			.<String, String> builder().key(testCase.key()).value(testCase.value());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.pairText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link KeyValuePair#toBuilder()} give the same key and
	 * value, and the getters return them
	 *
	 * @param testCase
	 *            the key and value
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepKeyAndValue(final ContentCase testCase)
	{
		final KeyValuePair<String, String> built = KeyValuePair.<String, String> builder()
			.key(testCase.key()).value(testCase.value()).build();
		final KeyValuePair<String, String> constructed = new KeyValuePair<>(testCase.key(),
			testCase.value());
		final KeyValuePair<String, String> copy = constructed.toBuilder().build();
		assertEquals(testCase.key(), built.getKey());
		assertEquals(testCase.value(), built.getValue());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link KeyValuePair#toBuilder()} starts from the key and value of the pair and leaves the
	 * pair as it is
	 */
	@Test
	void toBuilderChangesTheCopyOnly()
	{
		final KeyValuePair<String, String> pair = new KeyValuePair<>("a", "b");
		final KeyValuePair<String, String> changed = pair.toBuilder().value("c").build();
		assertEquals(new KeyValuePair<>("a", "c"), changed);
		assertEquals(new KeyValuePair<>("a", "b"), pair);
	}

	/**
	 * The no-argument constructor leaves key and value null, and the setters set them
	 */
	@Test
	void settersSetKeyAndValueOfAnEmptyPair()
	{
		final KeyValuePair<String, String> pair = new KeyValuePair<>();
		assertNull(pair.getKey());
		assertNull(pair.getValue());
		pair.setKey("a");
		pair.setValue("b");
		assertEquals("a", pair.getKey());
		assertEquals("b", pair.getValue());
	}

	/**
	 * A map to give to {@link KeyValuePair#toKeyValuePairs(Map)}, and the text of the list it
	 * returns
	 *
	 * @param description
	 *            the name of the case
	 * @param map
	 *            makes the map in the test
	 * @param listText
	 *            the toString of the returned list
	 */
	record MapCase(String description, Supplier<Map<String, String>> map, String listText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Maps with no, one and several entries
	 *
	 * @return the cases
	 */
	static Stream<MapCase> mapCases()
	{
		return Stream.of(new MapCase("an empty map: an empty list", () -> linkedMap(), "[]"),
			new MapCase("one entry: one pair", () -> linkedMap("a", "1"),
				"[KeyValuePair(key=a, value=1)]"),
			new MapCase("three entries: one pair each, in the order of the map",
				() -> linkedMap("c", "3", "a", "1", "b", "2"),
				"[KeyValuePair(key=c, value=3), KeyValuePair(key=a, value=1), "
					+ "KeyValuePair(key=b, value=2)]"),
			new MapCase("a null key and a null value: kept", () -> linkedMap(null, "1", "a", null),
				"[KeyValuePair(key=null, value=1), KeyValuePair(key=a, value=null)]"));
	}

	/**
	 * {@link KeyValuePair#toKeyValuePairs(Map)} gives one pair for each entry, in the order of the
	 * entry set of the map
	 *
	 * @param testCase
	 *            the map and the text of the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("mapCases")
	void toKeyValuePairsOfAMapGivesOnePairForEachEntry(final MapCase testCase)
	{
		final List<KeyValuePair<String, String>> pairs = KeyValuePair
			.toKeyValuePairs(testCase.map().get());
		assertEquals(testCase.listText(), pairs.toString());
	}

	/**
	 * Properties to give to {@link KeyValuePair#toKeyValuePairs(Properties)}, and the texts of the
	 * pairs it returns, sorted
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            makes the properties in the test
	 * @param sortedText
	 *            the toStrings of the returned pairs, sorted, as a list
	 */
	record PropertiesCase(String description, Supplier<Properties> properties, String sortedText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Properties of the given keys and values
	 *
	 * @param defaults
	 *            the defaults of the properties, may be null
	 * @param keysAndValues
	 *            a key, its value, the next key, its value and so on
	 * @return the properties
	 */
	private static Properties properties(final Properties defaults, final String... keysAndValues)
	{
		final Properties properties = new Properties(defaults);
		for (int index = 0; index < keysAndValues.length; index += 2)
		{
			properties.setProperty(keysAndValues[index], keysAndValues[index + 1]);
		}
		return properties;
	}

	/**
	 * Properties with no, one and several entries, and with defaults
	 *
	 * @return the cases
	 */
	static Stream<PropertiesCase> propertiesCases()
	{
		return Stream.of(
			new PropertiesCase("no properties: an empty list", () -> properties(null), "[]"),
			new PropertiesCase("one property: one pair", () -> properties(null, "foo", "bar"),
				"[KeyValuePair(key=foo, value=bar)]"),
			new PropertiesCase("two properties: one pair each",
				() -> properties(null, "foo", "bar", "bla", "fasel"),
				"[KeyValuePair(key=bla, value=fasel), KeyValuePair(key=foo, value=bar)]"),
			new PropertiesCase("only defaults: an empty list, the defaults are left out",
				() -> properties(properties(null, "foo", "bar")), "[]"),
			new PropertiesCase("a property and a default: the property only",
				() -> properties(properties(null, "foo", "bar"), "bla", "fasel"),
				"[KeyValuePair(key=bla, value=fasel)]"));
	}

	/**
	 * {@link KeyValuePair#toKeyValuePairs(Properties)} gives one pair for each entry of the
	 * properties themselves; it reads the entry set, so the defaults of the properties are left
	 * out, although {@link Properties#getProperty(String)} would return them
	 *
	 * @param testCase
	 *            the properties and the texts of the pairs
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("propertiesCases")
	void toKeyValuePairsOfPropertiesGivesOnePairForEachOwnEntry(final PropertiesCase testCase)
	{
		final List<KeyValuePair<String, String>> pairs = KeyValuePair
			.toKeyValuePairs(testCase.properties().get());
		assertEquals(testCase.sortedText(), pairs.stream().map(KeyValuePair::toString).sorted()
			.collect(Collectors.joining(", ", "[", "]")));
	}

	/**
	 * Pairs to give to {@link KeyValuePair#toMap(Collection)}, and the map it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param pairs
	 *            makes the pairs in the test
	 * @param map
	 *            makes the expected map in the test
	 */
	record ToMapCase(String description, Supplier<Collection<KeyValuePair<String, String>>> pairs,
		Supplier<Map<String, String>> map) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * No, one and several pairs, also of the same key
	 *
	 * @return the cases
	 */
	static Stream<ToMapCase> toMapCases()
	{
		return Stream.of(
			new ToMapCase("no pairs: an empty map", () -> new ArrayList<>(), () -> linkedMap()),
			new ToMapCase("one pair: one entry", () -> List.of(new KeyValuePair<>("a", "1")),
				() -> linkedMap("a", "1")),
			new ToMapCase("two pairs of different keys: two entries",
				() -> List.of(new KeyValuePair<>("a", "1"), new KeyValuePair<>("b", "2")),
				() -> linkedMap("a", "1", "b", "2")),
			new ToMapCase("two pairs of the same key: one entry, the later value wins",
				() -> List.of(new KeyValuePair<>("a", "1"), new KeyValuePair<>("a", "2")),
				() -> linkedMap("a", "2")),
			new ToMapCase("a null key and a null value: kept",
				() -> List.of(new KeyValuePair<>(null, "1"), new KeyValuePair<>("a", null)),
				() -> linkedMap(null, "1", "a", null)));
	}

	/**
	 * {@link KeyValuePair#toMap(Collection)} puts the key and value of each pair into a new
	 * {@link HashMap}, in the order of the collection, so that of two pairs of the same key the
	 * later wins
	 *
	 * @param testCase
	 *            the pairs and the expected map
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("toMapCases")
	void toMapPutsEachPairIntoAHashMap(final ToMapCase testCase)
	{
		final Map<String, String> map = KeyValuePair.toMap(testCase.pairs().get());
		assertEquals(testCase.map().get(), map);
		assertEquals(HashMap.class, map.getClass());
	}

	/**
	 * A call of a static method of {@link KeyValuePair} that throws, and the exception
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call
	 * @param exception
	 *            the class of the exception it throws
	 */
	record ThrowsCase(String description, Executable call,
		Class<? extends RuntimeException> exception) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Null arguments and properties of keys or values that are not Strings
	 *
	 * @return the cases
	 */
	static Stream<ThrowsCase> throwsCases()
	{
		return Stream.of(
			new ThrowsCase("toKeyValuePairs of a null map: NullPointerException",
				() -> KeyValuePair.toKeyValuePairs((Map<String, String>)null),
				NullPointerException.class),
			new ThrowsCase("toKeyValuePairs of null properties: NullPointerException",
				() -> KeyValuePair.toKeyValuePairs((Properties)null), NullPointerException.class),
			new ThrowsCase("toMap of null: NullPointerException", () -> KeyValuePair.toMap(null),
				NullPointerException.class),
			new ThrowsCase("toMap of a null pair: NullPointerException",
				() -> KeyValuePair.toMap(Arrays.asList(new KeyValuePair<>("a", "1"), null)),
				NullPointerException.class),
			new ThrowsCase("toKeyValuePairs of properties with a key that is no String: "
				+ "ClassCastException", () -> {
					final Properties properties = new Properties();
					properties.put(1, "one");
					KeyValuePair.toKeyValuePairs(properties);
				}, ClassCastException.class),
			new ThrowsCase("toKeyValuePairs of properties with a value that is no String: "
				+ "ClassCastException", () -> {
					final Properties properties = new Properties();
					properties.put("one", 1);
					KeyValuePair.toKeyValuePairs(properties);
				}, ClassCastException.class));
	}

	/**
	 * The static methods do not check their arguments: a null argument or a null pair throws a
	 * {@link NullPointerException}, and properties of a key or value that is no String throw a
	 * {@link ClassCastException}; the Javadoc names neither
	 *
	 * @param testCase
	 *            the call and the exception
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("throwsCases")
	void staticMethodsThrowForArgumentsTheyCannotRead(final ThrowsCase testCase)
	{
		assertThrows(testCase.exception(), testCase.call());
	}

	/**
	 * {@link KeyValuePair#toKeyValuePairs(Map)} and {@link KeyValuePair#toMap(Collection)} return
	 * new, modifiable collections, and toMap of the pairs of a map gives the map back
	 */
	@Test
	void toKeyValuePairsAndToMapReturnNewModifiableCollections()
	{
		final Map<String, String> map = linkedMap("a", "1", "b", "2");
		final List<KeyValuePair<String, String>> pairs = KeyValuePair.toKeyValuePairs(map);
		final Map<String, String> back = KeyValuePair.toMap(pairs);
		assertEquals(map, back);
		assertNotSame(map, back);
		pairs.add(new KeyValuePair<>("c", "3"));
		back.put("c", "3");
		assertEquals(3, pairs.size());
		assertEquals(3, back.size());
	}

	/**
	 * A {@link KeyValuePair} is {@link java.io.Serializable}: a serialized and read pair equals the
	 * pair
	 *
	 * @throws IOException
	 *             if the pair cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read pair is not found
	 */
	@Test
	void aSerializedKeyValuePairReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final KeyValuePair<String, String> pair = new KeyValuePair<>("a", "b");
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
