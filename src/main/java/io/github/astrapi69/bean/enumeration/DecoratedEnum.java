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
package io.github.astrapi69.bean.enumeration;

import java.util.Objects;

/**
 * The class {@link DecoratedEnum} can decorate an enum with a new value that may be extended in a
 * future release.
 *
 * @param <E>
 *            the type of the decorated enum
 * @param <T>
 *            the type of the value
 */
public final class DecoratedEnum<E extends Enum<E>, T>
{

	/** The enumtype. */
	private final E enumtype;

	/** The value. */
	private final T value;

	/**
	 * Instantiates a new {@link DecoratedEnum}.
	 *
	 * @param enumtype
	 *            the type of the enum
	 * @param value
	 *            the value
	 * @throws NullPointerException
	 *             if both the enum constant and the value are {@code null}
	 */
	public DecoratedEnum(final E enumtype, final T value)
	{
		if (enumtype == null)
		{
			Objects.requireNonNull(value);
		}
		this.enumtype = enumtype;
		this.value = value;
	}

	/**
	 * Returns a new builder for a {@link DecoratedEnum}.
	 *
	 * @param <E>
	 *            the type of the decorated enum
	 * @param <T>
	 *            the type of the value
	 * @return a new builder, with neither an enum constant nor a value set
	 */
	public static <E extends Enum<E>, T> DecoratedEnumBuilder<E, T> builder()
	{
		return new DecoratedEnumBuilder<E, T>();
	}

	/**
	 * Returns the name of the decorated enum or if it is a new value it will return the result of
	 * the toString method of value
	 *
	 * @return the name of the decorated enum or if it is a new value it will return the result of
	 *         the toString method of value
	 */
	public String name()
	{
		if (enumtype != null)
		{
			return enumtype.name();
		}
		return value.toString();
	}

	/**
	 * Returns the enum constant this object decorates.
	 *
	 * @return the enum constant, or {@code null} if this object holds a value alone
	 */
	public E getEnumtype()
	{
		return this.enumtype;
	}

	/**
	 * Returns the value of this object.
	 *
	 * @return the value, or {@code null} if this object holds an enum constant alone
	 */
	public T getValue()
	{
		return this.value;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof DecoratedEnum))
			return false;
		final DecoratedEnum<?, ?> other = (DecoratedEnum<?, ?>)o;
		final Object this$enumtype = this.getEnumtype();
		final Object other$enumtype = other.getEnumtype();
		if (this$enumtype == null ? other$enumtype != null : !this$enumtype.equals(other$enumtype))
			return false;
		final Object this$value = this.getValue();
		final Object other$value = other.getValue();
		if (this$value == null ? other$value != null : !this$value.equals(other$value))
			return false;
		return true;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $enumtype = this.getEnumtype();
		result = result * PRIME + ($enumtype == null ? 43 : $enumtype.hashCode());
		final Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 43 : $value.hashCode());
		return result;
	}

	public String toString()
	{
		return "DecoratedEnum(enumtype=" + this.getEnumtype() + ", value=" + this.getValue() + ")";
	}

	/**
	 * Builds a {@link DecoratedEnum} from an enum constant, a value or both.
	 *
	 * @param <E>
	 *            the type of the decorated enum
	 * @param <T>
	 *            the type of the value
	 */
	public static class DecoratedEnumBuilder<E extends Enum<E>, T>
	{
		private E enumtype;
		private T value;

		DecoratedEnumBuilder()
		{
		}

		/**
		 * Sets the enum constant of the {@link DecoratedEnum} to build.
		 *
		 * @param enumtype
		 *            the enum constant
		 * @return this builder
		 */
		public DecoratedEnumBuilder<E, T> enumtype(E enumtype)
		{
			this.enumtype = enumtype;
			return this;
		}

		/**
		 * Sets the value of the {@link DecoratedEnum} to build.
		 *
		 * @param value
		 *            the value
		 * @return this builder
		 */
		public DecoratedEnumBuilder<E, T> value(T value)
		{
			this.value = value;
			return this;
		}

		/**
		 * Builds a new {@link DecoratedEnum} from the enum constant and the value set on this
		 * builder.
		 *
		 * @return the new {@link DecoratedEnum}
		 * @throws NullPointerException
		 *             if neither an enum constant nor a value was set
		 */
		public DecoratedEnum<E, T> build()
		{
			return new DecoratedEnum<>(enumtype, value);
		}

		public String toString()
		{
			return "DecoratedEnum.DecoratedEnumBuilder(enumtype=" + this.enumtype + ", value="
				+ this.value + ")";
		}
	}
}
