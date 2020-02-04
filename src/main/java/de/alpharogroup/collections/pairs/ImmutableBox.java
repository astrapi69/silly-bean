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
public class ImmutableBox<T> implements Serializable {
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The value.
	 */
	private final T value;

	public static class ImmutableBoxBuilder<T> {
		private T value;

		ImmutableBoxBuilder() {
		}

		/**
		 * The value.
		 */
		public ImmutableBoxBuilder<T> value(final T value) {
			if (value == null) {
				throw new NullPointerException("value is marked non-null but is null");
			}
			this.value = value;
			return this;
		}

		public ImmutableBox<T> build() {
			return new ImmutableBox<T>(value);
		}

		@Override
		public String toString() {
			return "ImmutableBox.ImmutableBoxBuilder(value=" + this.value + ")";
		}
	}

	public static <T> ImmutableBoxBuilder<T> builder() {
		return new ImmutableBoxBuilder<T>();
	}

	public ImmutableBoxBuilder<T> toBuilder() {
		return new ImmutableBoxBuilder<T>().value(this.value);
	}

	/**
	 * The value.
	 */
	public T getValue() {
		return this.value;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof ImmutableBox)) return false;
		final ImmutableBox<?> other = (ImmutableBox<?>) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$value = this.getValue();
		final Object other$value = other.getValue();
		if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof ImmutableBox;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $value = this.getValue();
		result = result * PRIME + ($value == null ? 43 : $value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ImmutableBox(value=" + this.getValue() + ")";
	}

	public ImmutableBox(final T value) {
		if (value == null) {
			throw new NullPointerException("value is marked non-null but is null");
		}
		this.value = value;
	}
}
