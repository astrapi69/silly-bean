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
 * The class {@link ImmutableBox} represents one value with a generic parameter for an immutable
 * value.
 *
 * @param <T>
 *            The type of the value.
 */
public class ImmutableBox<T> implements Serializable
{

	/**
	 * The class {@link ImmutableBoxBuilder}.
	 *
	 * @param <T>
	 *            the generic type
	 */
	public static class ImmutableBoxBuilder<T>
	{

		/** The value. */
		private T value;

		/**
		 * Instantiates a new immutable box builder.
		 */
		ImmutableBoxBuilder()
		{
		}

		/**
		 * Build it
		 *
		 * @return the immutable box
		 */
		public ImmutableBox<T> build()
		{
			return new ImmutableBox<>(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return "ImmutableBox.ImmutableBoxBuilder(value=" + this.value + ")";
		}

		/**
		 * The value.
		 *
		 * @param value
		 *            the value
		 * @return the immutable box builder
		 */
		public ImmutableBoxBuilder<T> value(final T value)
		{
			if (value == null)
			{
				throw new NullPointerException("value is marked non-null but is null");
			}
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
	 * @return the immutable box builder
	 */
	public static <T> ImmutableBoxBuilder<T> builder()
	{
		return new ImmutableBoxBuilder<>();
	}

	/**
	 * The value.
	 */
	private final T value;

	/**
	 * Instantiates a new immutable box.
	 *
	 * @param value
	 *            the value
	 */
	public ImmutableBox(final T value)
	{
		if (value == null)
		{
			throw new NullPointerException("value is marked non-null but is null");
		}
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
		return other instanceof ImmutableBox;
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
		if (!(o instanceof ImmutableBox))
		{
			return false;
		}
		final ImmutableBox<?> other = (ImmutableBox<?>)o;
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
	 * To builder.
	 *
	 * @return the immutable box builder
	 */
	public ImmutableBoxBuilder<T> toBuilder()
	{
		return new ImmutableBoxBuilder<T>().value(this.value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ImmutableBox(value=" + this.getValue() + ")";
	}
}
