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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The class {@link KeySetPair} represents a key value pair where the value is a set with generic
 * parameters.
 *
 *
 * @param <K>
 *            The type of the key.
 * @param <V>
 *            The type of the values in the set.
 */
public final class KeySetPair<K, V> implements Serializable
{

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The key.
	 */
	private K key;
	/**
	 * The collection with the values.
	 */
	private Set<V> values;

	/**
	 * Instantiates a new key set pair.
	 */
	public KeySetPair()
	{
	}

	/**
	 * Instantiates a new key set pair.
	 *
	 * @param key
	 *            the key
	 * @param values
	 *            the values
	 */
	public KeySetPair(final K key, final Set<V> values)
	{
		this.key = key;
		this.values = values;
	}

	/**
	 * Builder.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @return the key set pair builder
	 */
	public static <K, V> KeySetPairBuilder<K, V> builder()
	{
		return new KeySetPairBuilder<>();
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
		if (!(o instanceof KeySetPair))
		{
			return false;
		}
		final KeySetPair<?, ?> other = (KeySetPair<?, ?>)o;
		final Object this$key = this.getKey();
		final Object other$key = other.getKey();
		if (this$key == null ? other$key != null : !this$key.equals(other$key))
		{
			return false;
		}
		final Object this$values = this.getValues();
		final Object other$values = other.getValues();
		if (this$values == null ? other$values != null : !this$values.equals(other$values))
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
	 * The collection with the values.
	 *
	 * @return the values
	 */
	public Set<V> getValues()
	{
		return this.values;
	}

	/**
	 * The collection with the values.
	 *
	 * @param values
	 *            the new values
	 */
	public void setValues(final Set<V> values)
	{
		this.values = values;
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
		final Object $values = this.getValues();
		result = result * PRIME + ($values == null ? 43 : $values.hashCode());
		return result;
	}

	/**
	 * To builder.
	 *
	 * @return the key set pair builder
	 */
	public KeySetPairBuilder<K, V> toBuilder()
	{
		final KeySetPairBuilder<K, V> builder = new KeySetPairBuilder<K, V>().key(this.key);
		if (this.values != null)
		{
			builder.values(this.values);
		}
		return builder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "KeySetPair(key=" + this.getKey() + ", values=" + this.getValues() + ")";
	}

	/**
	 * The class {@link KeySetPairBuilder}.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 */
	public static class KeySetPairBuilder<K, V>
	{

		/** The key. */
		private K key;

		/** The values. */
		private ArrayList<V> values;

		/**
		 * Instantiates a new key set pair builder.
		 */
		KeySetPairBuilder()
		{
		}

		/**
		 * Build it
		 *
		 * @return the key set pair
		 */
		public KeySetPair<K, V> build()
		{
			Set<V> values;
			switch (this.values == null ? 0 : this.values.size())
			{
				case 0 :
					values = Collections.emptySet();
					break;
				case 1 :
					values = Collections.singleton(this.values.get(0));
					break;
				default :
					values = new LinkedHashSet<>(this.values.size() < 1073741824
						? 1 + this.values.size() + (this.values.size() - 3) / 3
						: Integer.MAX_VALUE);
					values.addAll(this.values);
					values = Collections.unmodifiableSet(values);
			}
			return new KeySetPair<>(key, values);
		}

		/**
		 * Clear values.
		 *
		 * @return the key set pair builder
		 */
		public KeySetPairBuilder<K, V> clearValues()
		{
			if (this.values != null)
			{
				this.values.clear();
			}
			return this;
		}

		/**
		 * The key.
		 *
		 * @param key
		 *            the key
		 * @return the key set pair builder
		 */
		public KeySetPairBuilder<K, V> key(final K key)
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
			return "KeySetPair.KeySetPairBuilder(key=" + this.key + ", values=" + this.values + ")";
		}

		/**
		 * Value.
		 *
		 * @param value
		 *            the value
		 * @return the key set pair builder
		 */
		public KeySetPairBuilder<K, V> value(final V value)
		{
			if (this.values == null)
			{
				this.values = new ArrayList<>();
			}
			this.values.add(value);
			return this;
		}

		/**
		 * Values.
		 *
		 * @param values
		 *            the values
		 * @return the key set pair builder
		 */
		public KeySetPairBuilder<K, V> values(final Collection<? extends V> values)
		{
			if (this.values == null)
			{
				this.values = new ArrayList<>();
			}
			this.values.addAll(values);
			return this;
		}
	}
}
