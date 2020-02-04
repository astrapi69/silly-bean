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
 * The class {@link Triple} represents a triple of values with generic parameters for the values.
 *
 * @param <L>
 *            The generic type of the left content of this Triple.
 * @param <M>
 *            The generic type of the middle content of this Triple.
 * @param <R>
 *            The generic type of the right content of this Triple.
 */
public class Triple<L, M, R> implements Serializable
{
	public static class TripleBuilder<L, M, R>
	{

		private L left;

		private M middle;

		private R right;

		TripleBuilder()
		{
		}

		public Triple<L, M, R> build()
		{
			return new Triple<L, M, R>(left, middle, right);
		}

		/**
		 * The left value.
		 */
		public TripleBuilder<L, M, R> left(final L left)
		{
			this.left = left;
			return this;
		}

		/**
		 * The middle value.
		 */
		public TripleBuilder<L, M, R> middle(final M middle)
		{
			this.middle = middle;
			return this;
		}

		/**
		 * The right value.
		 */
		public TripleBuilder<L, M, R> right(final R right)
		{
			this.right = right;
			return this;
		}

		@Override
		public String toString()
		{
			return "Triple.TripleBuilder(left=" + this.left + ", middle=" + this.middle + ", right="
				+ this.right + ")";
		}
	}
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	public static <L, M, R> TripleBuilder<L, M, R> builder()
	{
		return new TripleBuilder<L, M, R>();
	}
	/**
	 * The left value.
	 */
	private L left;

	/**
	 * The middle value.
	 */
	private M middle;

	/**
	 * The right value.
	 */
	private R right;

	public Triple()
	{
	}

	public Triple(final L left, final M middle, final R right)
	{
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof Triple;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof Triple))
			return false;
		final Triple<?, ?, ?> other = (Triple<?, ?, ?>)o;
		if (!other.canEqual(this))
			return false;
		final Object this$left = this.getLeft();
		final Object other$left = other.getLeft();
		if (this$left == null ? other$left != null : !this$left.equals(other$left))
			return false;
		final Object this$middle = this.getMiddle();
		final Object other$middle = other.getMiddle();
		if (this$middle == null ? other$middle != null : !this$middle.equals(other$middle))
			return false;
		final Object this$right = this.getRight();
		final Object other$right = other.getRight();
		if (this$right == null ? other$right != null : !this$right.equals(other$right))
			return false;
		return true;
	}

	/**
	 * The left value.
	 */
	public L getLeft()
	{
		return this.left;
	}

	/**
	 * The middle value.
	 */
	public M getMiddle()
	{
		return this.middle;
	}

	/**
	 * The right value.
	 */
	public R getRight()
	{
		return this.right;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $left = this.getLeft();
		result = result * PRIME + ($left == null ? 43 : $left.hashCode());
		final Object $middle = this.getMiddle();
		result = result * PRIME + ($middle == null ? 43 : $middle.hashCode());
		final Object $right = this.getRight();
		result = result * PRIME + ($right == null ? 43 : $right.hashCode());
		return result;
	}

	/**
	 * The left value.
	 */
	public void setLeft(final L left)
	{
		this.left = left;
	}

	/**
	 * The middle value.
	 */
	public void setMiddle(final M middle)
	{
		this.middle = middle;
	}

	/**
	 * The right value.
	 */
	public void setRight(final R right)
	{
		this.right = right;
	}

	public TripleBuilder<L, M, R> toBuilder()
	{
		return new TripleBuilder<L, M, R>().left(this.left).middle(this.middle).right(this.right);
	}

	@Override
	public String toString()
	{
		return "Triple(left=" + this.getLeft() + ", middle=" + this.getMiddle() + ", right="
			+ this.getRight() + ")";
	}
}