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

/**
 * The class {@link ValueBox} represents one value with a generic parameter for the value.
 *
 * @param <T>
 *            The type of the value.
 */
public class ValueBox<T> implements Serializable
{

	/**
	 * The class {@link ValueBoxBuilder}.
	 *
	 * @param <T>
	 *            the generic type
	 */
	public static class ValueBoxBuilder<T>
	{

		/** The value. */
		private T value;

		/**
		 * Instantiates a new value box builder.
		 */
		ValueBoxBuilder()
		{
		}

		/**
		 * Build it
		 *
		 * @return the value box
		 */
		public ValueBox<T> build()
		{
			return new ValueBox<>(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return "ValueBox.ValueBoxBuilder(value=" + this.value + ")";
		}

		/**
		 * The value.
		 *
		 * @param value
		 *            the value
		 * @return the value box builder
		 */
		public ValueBoxBuilder<T> value(final T value)
		{
			this.value = value;
			return this;
		}
	}

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Builder.
	 *
	 * @param <T>
	 *            the generic type
	 * @return the value box builder
	 */
	public static <T> ValueBoxBuilder<T> builder()
	{
		return new ValueBoxBuilder<>();
	}

	/**
	 * The value.
	 */
	private T value;

	/**
	 * Instantiates a new value box.
	 */
	public ValueBox()
	{
	}

	/**
	 * Instantiates a new value box.
	 *
	 * @param value
	 *            the value
	 */
	public ValueBox(final T value)
	{
		this.value = value;
	}

	/**
	 * Can equal.
	 *
	 * @param other
	 *            the other
	 * @return true, if successful
	 */
	protected boolean canEqual(final Object other)
	{
		return other instanceof ValueBox;
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
		if (!(o instanceof ValueBox))
		{
			return false;
		}
		final ValueBox<?> other = (ValueBox<?>)o;
		if (!other.canEqual(this))
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
	 * The value.
	 *
	 * @return the value
	 */
	public T getValue()
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
		final Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 43 : $value.hashCode());
		return result;
	}

	/**
	 * The value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(final T value)
	{
		this.value = value;
	}

	/**
	 * To builder.
	 *
	 * @return the value box builder
	 */
	public ValueBoxBuilder<T> toBuilder()
	{
		return new ValueBoxBuilder<T>().value(this.value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ValueBox(value=" + this.getValue() + ")";
	}
}