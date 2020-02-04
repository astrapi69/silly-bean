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
 * The class {@link Pair} represents a pair of values with generic parameters for the values.
 *
 *
 * @param <K>
 *            The generic type of the left content of this Pair.
 * @param <V>
 *            The generic type of the right content of this Pair.
 */
public class Pair<K, V> implements Serializable {
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The left.
	 */
	private K leftContent;
	/**
	 * The right.
	 */
	private V rightContent;

	public static class PairBuilder<K, V> {
		
		private K leftContent;
		
		private V rightContent;

		PairBuilder() {
		}

		/**
		 * The left.
		 */
		public PairBuilder<K, V> leftContent(final K leftContent) {
			this.leftContent = leftContent;
			return this;
		}

		/**
		 * The right.
		 */
		public PairBuilder<K, V> rightContent(final V rightContent) {
			this.rightContent = rightContent;
			return this;
		}

		
		public Pair<K, V> build() {
			return new Pair<K, V>(leftContent, rightContent);
		}

		@Override
		public String toString() {
			return "Pair.PairBuilder(leftContent=" + this.leftContent + ", rightContent=" + this.rightContent + ")";
		}
	}
	
	public static <K, V> PairBuilder<K, V> builder() {
		return new PairBuilder<K, V>();
	}
	
	public PairBuilder<K, V> toBuilder() {
		return new PairBuilder<K, V>().leftContent(this.leftContent).rightContent(this.rightContent);
	}

	/**
	 * The left.
	 */
	public K getLeftContent() {
		return this.leftContent;
	}

	/**
	 * The right.
	 */
	public V getRightContent() {
		return this.rightContent;
	}

	/**
	 * The left.
	 */
	public void setLeftContent(final K leftContent) {
		this.leftContent = leftContent;
	}

	/**
	 * The right.
	 */
	public void setRightContent(final V rightContent) {
		this.rightContent = rightContent;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Pair)) return false;
		final Pair<?, ?> other = (Pair<?, ?>) o;
		if (!other.canEqual((Object) this)) return false;
		final Object this$leftContent = this.getLeftContent();
		final Object other$leftContent = other.getLeftContent();
		if (this$leftContent == null ? other$leftContent != null : !this$leftContent.equals(other$leftContent)) return false;
		final Object this$rightContent = this.getRightContent();
		final Object other$rightContent = other.getRightContent();
		if (this$rightContent == null ? other$rightContent != null : !this$rightContent.equals(other$rightContent)) return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof Pair;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $leftContent = this.getLeftContent();
		result = result * PRIME + ($leftContent == null ? 43 : $leftContent.hashCode());
		final Object $rightContent = this.getRightContent();
		result = result * PRIME + ($rightContent == null ? 43 : $rightContent.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Pair(leftContent=" + this.getLeftContent() + ", rightContent=" + this.getRightContent() + ")";
	}
	
	public Pair() {
	}
	
	public Pair(final K leftContent, final V rightContent) {
		this.leftContent = leftContent;
		this.rightContent = rightContent;
	}
}