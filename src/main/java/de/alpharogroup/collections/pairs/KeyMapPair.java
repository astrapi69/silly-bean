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
import java.util.Map;

/**
 * The class {@link KeyMapPair} represents a key value pair with generic parameters for the key and
 * map value type.
 *
 * @param <K>
 *            The type of the key.
 * @param <MK>
 *            the generic type of the map key.
 * @param <MV>
 *            the generic type of the map value.
 */
public final class KeyMapPair<K, MK, MV> implements Serializable
{

	public static class KeyMapPairBuilder<K, MK, MV>
	{
		private K key;
		private Map<MK, MV> values;

		KeyMapPairBuilder()
		{
		}

		public KeyMapPair<K, MK, MV> build()
		{
			return new KeyMapPair<K, MK, MV>(key, values);
		}

		/**
		 * The key.
		 */
		public KeyMapPairBuilder<K, MK, MV> key(final K key)
		{
			this.key = key;
			return this;
		}

		@Override
		public String toString()
		{
			return "KeyMapPair.KeyMapPairBuilder(key=" + this.key + ", values=" + this.values + ")";
		}

		/**
		 * The map with the values.
		 */
		public KeyMapPairBuilder<K, MK, MV> values(final Map<MK, MV> values)
		{
			this.values = values;
			return this;
		}
	}

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public static <K, MK, MV> KeyMapPairBuilder<K, MK, MV> builder()
	{
		return new KeyMapPairBuilder<K, MK, MV>();
	}

	/**
	 * The key.
	 */
	private K key;

	/**
	 * The map with the values.
	 */
	private Map<MK, MV> values;

	public KeyMapPair()
	{
	}

	public KeyMapPair(final K key, final Map<MK, MV> values)
	{
		this.key = key;
		this.values = values;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof KeyMapPair))
			return false;
		final KeyMapPair<?, ?, ?> other = (KeyMapPair<?, ?, ?>)o;
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
	 * The map with the values.
	 */
	public Map<MK, MV> getValues()
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
	 * The map with the values.
	 */
	public void setValues(final Map<MK, MV> values)
	{
		this.values = values;
	}

	public KeyMapPairBuilder<K, MK, MV> toBuilder()
	{
		return new KeyMapPairBuilder<K, MK, MV>().key(this.key).values(this.values);
	}

	@Override
	public String toString()
	{
		return "KeyMapPair(key=" + this.getKey() + ", values=" + this.getValues() + ")";
	}
}