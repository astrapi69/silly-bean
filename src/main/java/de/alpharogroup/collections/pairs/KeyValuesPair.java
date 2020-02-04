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
import java.util.Collection;

/**
 * The class {@link KeyValuesPair} represents a key value pair where the value is a collection with
 * generic parameters for the key and value type.
 *
 *
 * @param <K>
 *            The type of the key.
 * @param <V>
 *            The type of the values in the collection.
 */
public final class KeyValuesPair<K, V> implements Serializable
{
	public static class KeyValuesPairBuilder<K, V>
	{

		private K key;

		private java.util.ArrayList<V> values;


		KeyValuesPairBuilder()
		{
		}

		public KeyValuesPair<K, V> build()
		{
			java.util.Collection<V> values;
			switch (this.values == null ? 0 : this.values.size())
			{
				case 0 :
					values = java.util.Collections.emptyList();
					break;
				case 1 :
					values = java.util.Collections.singletonList(this.values.get(0));
					break;
				default :
					values = java.util.Collections
						.unmodifiableList(new java.util.ArrayList<V>(this.values));
			}
			return new KeyValuesPair<K, V>(key, values);
		}

		public KeyValuesPairBuilder<K, V> clearValues()
		{
			if (this.values != null)
				this.values.clear();
			return this;
		}

		/**
		 * The key.
		 */
		public KeyValuesPairBuilder<K, V> key(final K key)
		{
			this.key = key;
			return this;
		}

		@Override
		public String toString()
		{
			return "KeyValuesPair.KeyValuesPairBuilder(key=" + this.key + ", values=" + this.values
				+ ")";
		}

		public KeyValuesPairBuilder<K, V> value(final V value)
		{
			if (this.values == null)
				this.values = new java.util.ArrayList<V>();
			this.values.add(value);
			return this;
		}

		public KeyValuesPairBuilder<K, V> values(final java.util.Collection<? extends V> values)
		{
			if (this.values == null)
				this.values = new java.util.ArrayList<V>();
			this.values.addAll(values);
			return this;
		}
	}

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public static <K, V> KeyValuesPairBuilder<K, V> builder()
	{
		return new KeyValuesPairBuilder<K, V>();
	}

	/**
	 * The key.
	 */
	private K key;

	/**
	 * The collection with the values.
	 */
	private Collection<V> values;

	public KeyValuesPair()
	{
	}

	public KeyValuesPair(final K key, final Collection<V> values)
	{
		this.key = key;
		this.values = values;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof KeyValuesPair))
			return false;
		final KeyValuesPair<?, ?> other = (KeyValuesPair<?, ?>)o;
		final Object this$key = this.getKey();
		final Object other$key = other.getKey();
		if (this$key == null ? other$key != null : !this$key.equals(other$key))
			return false;
		final Object this$values = this.getValues();
		final Object other$values = other.getValues();
		if (this$values == null ? other$values != null : !this$values.equals(other$values))
			return false;
		return true;
	}

	/**
	 * The key.
	 */
	public K getKey()
	{
		return this.key;
	}

	/**
	 * The collection with the values.
	 */
	public Collection<V> getValues()
	{
		return this.values;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $key = this.getKey();
		result = result * PRIME + ($key == null ? 43 : $key.hashCode());
		final Object $values = this.getValues();
		result = result * PRIME + ($values == null ? 43 : $values.hashCode());
		return result;
	}

	/**
	 * The key.
	 */
	public void setKey(final K key)
	{
		this.key = key;
	}

	/**
	 * The collection with the values.
	 */
	public void setValues(final Collection<V> values)
	{
		this.values = values;
	}

	public KeyValuesPairBuilder<K, V> toBuilder()
	{
		final KeyValuesPairBuilder<K, V> builder = new KeyValuesPairBuilder<K, V>().key(this.key);
		if (this.values != null)
			builder.values(this.values);
		return builder;
	}

	@Override
	public String toString()
	{
		return "KeyValuesPair(key=" + this.getKey() + ", values=" + this.getValues() + ")";
	}
}