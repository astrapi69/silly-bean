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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link KeyMapPair}. The cases hold the keys, and lambdas that
 * make the maps; every test builds its pairs itself, so that PIT gives each case the code it runs
 */
public class KeyMapPairTest
{

	/**
	 * The hash code the formula of {@link KeyMapPair#hashCode()} takes for a null key or map
	 */
	private static final int NULL_HASH = 43;

	/**
	 * The prime the formula of {@link KeyMapPair#hashCode()} multiplies with
	 */
	private static final int PRIME = 59;

	/**
	 * The hash code of the given key or map as {@link KeyMapPair#hashCode()} takes it
	 *
	 * @param content
	 *            the key or map, may be null
	 * @return the hash code of the key or map, or 43 for null
	 */
	private static int hash(final Object content)
	{
		return content == null ? NULL_HASH : content.hashCode();
	}

	/**
	 * A {@link HashMap} of one entry
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return the map
	 */
	private static Map<String, String> hashMap(final String key, final String value)
	{
		final Map<String, String> map = new HashMap<>();
		map.put(key, value);
		return map;
	}

	/**
	 * The keys and maps of two pairs, and whether the pairs are equal
	 *
	 * @param description
	 *            the name of the case
	 * @param firstKey
	 *            the key of the first pair, may be null
	 * @param firstValues
	 *            makes the map of the first pair in the test, may make null
	 * @param secondKey
	 *            the key of the second pair, may be null
	 * @param secondValues
	 *            makes the map of the second pair in the test, may make null
	 * @param equal
	 *            true if the pairs are equal
	 */
	record EqualsCase(String description, String firstKey,
		Supplier<Map<String, String>> firstValues, String secondKey,
		Supplier<Map<String, String>> secondValues, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Two pairs are equal if their keys are equal and their maps are equal, null equal to null
	 *
	 * @return the cases
	 */
	static Stream<EqualsCase> equalsCases()
	{
		return Stream.of(
			new EqualsCase("the same key and map: equal", "k", () -> Map.of("a", "1"), "k",
				() -> Map.of("a", "1"), true),
			new EqualsCase("key and map null in both: equal", null, () -> null, null, () -> null,
				true),
			new EqualsCase("maps of the same entries, of other classes: equal", "k",
				() -> hashMap("a", "1"), "k", () -> new TreeMap<>(Map.of("a", "1")), true),
			new EqualsCase("another key: not equal", "k", () -> Map.of("a", "1"), "x",
				() -> Map.of("a", "1"), false),
			new EqualsCase("another map: not equal", "k", () -> Map.of("a", "1"), "k",
				() -> Map.of("a", "2"), false),
			new EqualsCase("key null in the first only: not equal", null, () -> Map.of("a", "1"),
				"k", () -> Map.of("a", "1"), false),
			new EqualsCase("key null in the second only: not equal", "k", () -> Map.of("a", "1"),
				null, () -> Map.of("a", "1"), false),
			new EqualsCase("map null in the first only: not equal", "k", () -> null, "k",
				() -> Map.of("a", "1"), false),
			new EqualsCase("map null in the second only: not equal", "k", () -> Map.of("a", "1"),
				"k", () -> null, false),
			new EqualsCase("an empty map and a null map: not equal", "k", () -> Map.of(), "k",
				() -> null, false));
	}

	/**
	 * {@link KeyMapPair#equals(Object)} compares the key and the map, both ways round; equal pairs
	 * have the same {@link KeyMapPair#hashCode()}, and for these keys and maps unequal pairs have
	 * different ones
	 *
	 * @param testCase
	 *            the keys, the maps and whether the pairs are equal
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalsCases")
	void equalsComparesKeyAndMap(final EqualsCase testCase)
	{
		final KeyMapPair<String, String, String> first = new KeyMapPair<>(testCase.firstKey(),
			testCase.firstValues().get());
		final KeyMapPair<String, String, String> second = KeyMapPair
			.<String, String, String> builder().key(testCase.secondKey())
			.values(testCase.secondValues().get()).build();
		assertEquals(testCase.equal(), first.equals(second));
		assertEquals(testCase.equal(), second.equals(first));
		assertEquals(testCase.equal(), first.hashCode() == second.hashCode());
	}

	/**
	 * An object to compare a pair of key "k" and the map {a=1} with, and whether it is equal to the
	 * pair
	 *
	 * @param description
	 *            the name of the case
	 * @param other
	 *            the object, made from the pair in the test
	 * @param equal
	 *            true if the pair equals the object
	 */
	record OtherCase(String description, Function<KeyMapPair<String, String, String>, Object> other,
		boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The objects other than a pair of another key or map; {@link KeyMapPair} is final, there is no
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
			new OtherCase("a KeyValuePair of the same key and map: not equal",
				pair -> new KeyValuePair<>(pair.getKey(), pair.getValues()), false));
	}

	/**
	 * {@link KeyMapPair#equals(Object)} of a pair and another object
	 *
	 * @param testCase
	 *            the object and whether the pair equals it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherCases")
	void equalsWithAnotherObject(final OtherCase testCase)
	{
		final KeyMapPair<String, String, String> pair = new KeyMapPair<>("k", Map.of("a", "1"));
		assertEquals(testCase.equal(), pair.equals(testCase.other().apply(pair)));
	}

	/**
	 * The key and map of a pair, its {@link KeyMapPair#toString()} and the one of its builder
	 *
	 * @param description
	 *            the name of the case
	 * @param key
	 *            the key, may be null
	 * @param values
	 *            makes the map in the test, may make null
	 * @param pairText
	 *            the toString of the pair
	 * @param builderText
	 *            the toString of a builder of this key and map
	 */
	record ContentCase(String description, String key, Supplier<Map<String, String>> values,
		String pairText, String builderText) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Pairs with and without key and map
	 *
	 * @return the cases
	 */
	static Stream<ContentCase> contentCases()
	{
		return Stream.of(new ContentCase("key and map", "k", () -> hashMap("a", "1"),
			"KeyMapPair(key=k, values={a=1})", "KeyMapPair.KeyMapPairBuilder(key=k, values={a=1})"),
			new ContentCase("the key null", null, () -> hashMap("a", "1"),
				"KeyMapPair(key=null, values={a=1})",
				"KeyMapPair.KeyMapPairBuilder(key=null, values={a=1})"),
			new ContentCase("the map null", "k", () -> null, "KeyMapPair(key=k, values=null)",
				"KeyMapPair.KeyMapPairBuilder(key=k, values=null)"),
			new ContentCase("an empty map", "k", () -> new HashMap<>(),
				"KeyMapPair(key=k, values={})", "KeyMapPair.KeyMapPairBuilder(key=k, values={})"),
			new ContentCase("key and map null", null, () -> null,
				"KeyMapPair(key=null, values=null)",
				"KeyMapPair.KeyMapPairBuilder(key=null, values=null)"));
	}

	/**
	 * {@link KeyMapPair#hashCode()} is the formula Lombok writes,
	 * {@code (59 + hash(key)) * 59 + hash(values)} with 43 for null; the contract of
	 * {@link Object#hashCode()} would allow any value that agrees with equals, the test pins this
	 * one so that a change shows
	 *
	 * @param testCase
	 *            the key and map
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void hashCodeIsTheFormulaOfKeyAndMap(final ContentCase testCase)
	{
		final Map<String, String> values = testCase.values().get();
		final KeyMapPair<String, String, String> pair = new KeyMapPair<>(testCase.key(), values);
		assertEquals((PRIME + hash(testCase.key())) * PRIME + hash(values), pair.hashCode());
	}

	/**
	 * {@link KeyMapPair#toString()} and {@link KeyMapPair.KeyMapPairBuilder#toString()} name the
	 * key and the map
	 *
	 * @param testCase
	 *            the key, the map and the texts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void toStringNamesKeyAndMap(final ContentCase testCase)
	{
		final KeyMapPair.KeyMapPairBuilder<String, String, String> builder = KeyMapPair
			.<String, String, String> builder().key(testCase.key()).values(testCase.values().get());
		assertEquals(testCase.builderText(), builder.toString());
		assertEquals(testCase.pairText(), builder.build().toString());
	}

	/**
	 * The builder, the constructor and {@link KeyMapPair#toBuilder()} give the same key and map,
	 * and the getters return them
	 *
	 * @param testCase
	 *            the key and map
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("contentCases")
	void builderConstructorAndToBuilderKeepKeyAndMap(final ContentCase testCase)
	{
		final Map<String, String> values = testCase.values().get();
		final KeyMapPair<String, String, String> built = KeyMapPair
			.<String, String, String> builder().key(testCase.key()).values(values).build();
		final KeyMapPair<String, String, String> constructed = new KeyMapPair<>(testCase.key(),
			values);
		final KeyMapPair<String, String, String> copy = constructed.toBuilder().build();
		assertEquals(testCase.key(), built.getKey());
		assertSame(values, built.getValues());
		assertEquals(built, constructed);
		assertEquals(constructed, copy);
		assertNotSame(constructed, copy);
	}

	/**
	 * {@link KeyMapPair#toBuilder()} starts from the key and map of the pair; a change on the
	 * builder leaves the pair as it is, but the copy has the same map, no copy of it, so a change
	 * of the map shows in both
	 */
	@Test
	void toBuilderChangesTheCopyOnlyAndSharesTheMap()
	{
		final KeyMapPair<String, String, String> pair = new KeyMapPair<>("k", hashMap("a", "1"));
		final KeyMapPair<String, String, String> changed = pair.toBuilder().key("x").build();
		final KeyMapPair<String, String, String> otherMap = pair.toBuilder()
			.values(hashMap("b", "2")).build();
		assertEquals(new KeyMapPair<>("x", Map.of("a", "1")), changed);
		assertEquals(new KeyMapPair<>("k", Map.of("b", "2")), otherMap);
		assertEquals(new KeyMapPair<>("k", Map.of("a", "1")), pair);
		assertSame(pair.getValues(), changed.getValues());
		changed.getValues().put("c", "3");
		assertEquals(Map.of("a", "1", "c", "3"), pair.getValues());
	}

	/**
	 * The no-argument constructor leaves key and map null, and the setters set them
	 */
	@Test
	void settersSetKeyAndMapOfAnEmptyPair()
	{
		final KeyMapPair<String, String, String> pair = new KeyMapPair<>();
		assertNull(pair.getKey());
		assertNull(pair.getValues());
		final Map<String, String> values = hashMap("a", "1");
		pair.setKey("k");
		pair.setValues(values);
		assertEquals("k", pair.getKey());
		assertSame(values, pair.getValues());
	}

	/**
	 * A {@link KeyMapPair} is {@link java.io.Serializable}: a serialized and read pair of a
	 * serializable map equals the pair
	 *
	 * @throws IOException
	 *             if the pair cannot be written or read
	 * @throws ClassNotFoundException
	 *             if the class of the read pair is not found
	 */
	@Test
	void aSerializedKeyMapPairReadsBackEqual() throws IOException, ClassNotFoundException
	{
		final KeyMapPair<String, String, String> pair = new KeyMapPair<>("k", hashMap("a", "1"));
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
	 * Test method for {@link KeyMapPair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeyMapPair.class);
	}

}
