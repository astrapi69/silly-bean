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
package de.alpharogroup.collections.pairs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * The class {@link KeyValuePair} represents a key value pair with generic parameters for the key
 * and value type.
 *
 *
 * @param <K>
 *            The generic type of the key
 * @param <V>
 *            The generic type of the value
 */
public final class KeyValuePair<K, V> implements Serializable
{

	/**
	 * The class {@link KeyValuePairBuilder}.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 */
	public static class KeyValuePairBuilder<K, V>
	{

		/** The key. */
		private K key;

		/** The value. */
		private V value;


		/**
		 * Instantiates a new key value pair builder.
		 */
		KeyValuePairBuilder()
		{
		}

		/**
		 * Build it
		 *
		 * @return the key value pair
		 */
		public KeyValuePair<K, V> build()
		{
			return new KeyValuePair<>(key, value);
		}

		/**
		 * The key.
		 *
		 * @param key
		 *            the key
		 * @return the key value pair builder
		 */
		public KeyValuePairBuilder<K, V> key(final K key)
		{
			this.key = key;
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return "KeyValuePair.KeyValuePairBuilder(key=" + this.key + ", value=" + this.value
				+ ")";
		}

		/**
		 * The value.
		 *
		 * @param value
		 *            the value
		 * @return the key value pair builder
		 */
		public KeyValuePairBuilder<K, V> value(final V value)
		{
			this.value = value;
			return this;
		}
	}

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Builder.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @return the key value pair builder
	 */
	public static <K, V> KeyValuePairBuilder<K, V> builder()
	{
		return new KeyValuePairBuilder<>();
	}

	/**
	 * Transforms the given {@link Map} to a list of {@link KeyValuePair}'s.
	 *
	 * @param <K>
	 *            The generic type of the key
	 * @param <V>
	 *            The generic type of the value
	 * @param map
	 *            the map
	 * @return the new list with the {@link KeyValuePair}'s.
	 */
	public static <K, V> List<KeyValuePair<K, V>> toKeyValuePairs(final Map<K, V> map)
	{
		final List<KeyValuePair<K, V>> list = new ArrayList<>();
		for (final Entry<K, V> entry : map.entrySet())
		{
			list.add(
				KeyValuePair.<K, V> builder().key(entry.getKey()).value(entry.getValue()).build());
		}
		return list;
	}

	/**
	 * Transforms the given {@link Properties} to a list of {@link KeyValuePair}'s.
	 *
	 * @param properties
	 *            the properties
	 * @return the new list with the {@link KeyValuePair}'s.
	 */
	public static List<KeyValuePair<String, String>> toKeyValuePairs(final Properties properties)
	{
		final List<KeyValuePair<String, String>> list = new ArrayList<>();
		for (final Entry<Object, Object> entry : properties.entrySet())
		{
			list.add(KeyValuePair.<String, String> builder().key((String)entry.getKey())
				.value((String)entry.getValue()).build());
		}
		return list;
	}

	/**
	 * Transforms the given {@link List} of {@link KeyValuePair}'s to a {@link Map}.
	 *
	 * @param <K>
	 *            The generic type of the key
	 * @param <V>
	 *            The generic type of the value
	 * @param list
	 *            the list
	 * @return the new map.
	 */
	public static <K, V> Map<K, V> toMap(final Collection<KeyValuePair<K, V>> list)
	{
		final Map<K, V> map = new HashMap<>();
		for (KeyValuePair<K, V> keyValuePair : list)
		{
			map.put(keyValuePair.getKey(), keyValuePair.getValue());
		}
		return map;
	}

	/**
	 * The key.
	 */
	private K key;

	/**
	 * The value.
	 */
	private V value;

	/**
	 * Instantiates a new key value pair.
	 */
	public KeyValuePair()
	{
	}

	/**
	 * Instantiates a new key value pair.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public KeyValuePair(final K key, final V value)
	{
		this.key = key;
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof KeyValuePair))
		{
			return false;
		}
		final KeyValuePair<?, ?> other = (KeyValuePair<?, ?>)o;
		final Object this$key = this.getKey();
		final Object other$key = other.getKey();
		if (this$key == null ? other$key != null : !this$key.equals(other$key))
		{
			return false;
		}
		final Object this$value = this.getValue();
		final Object other$value = other.getValue();
		if (this$value == null ? other$value != null : !this$value.equals(other$value))
		{
			return false;
		}
		return true;
	}

	/**
	 * The key.
	 *
	 * @return the key
	 */
	public K getKey()
	{
		return this.key;
	}

	/**
	 * The value.
	 *
	 * @return the value
	 */
	public V getValue()
	{
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $key = this.getKey();
		result = result * PRIME + ($key == null ? 43 : $key.hashCode());
		final Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 43 : $value.hashCode());
		return result;
	}

	/**
	 * The key.
	 *
	 * @param key
	 *            the new key
	 */
	public void setKey(final K key)
	{
		this.key = key;
	}

	/**
	 * The value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(final V value)
	{
		this.value = value;
	}

	/**
	 * To builder.
	 *
	 * @return the key value pair builder
	 */
	public KeyValuePairBuilder<K, V> toBuilder()
	{
		return new KeyValuePairBuilder<K, V>().key(this.key).value(this.value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "KeyValuePair(key=" + this.getKey() + ", value=" + this.getValue() + ")";
	}
}