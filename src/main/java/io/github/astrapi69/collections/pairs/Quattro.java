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
package io.github.astrapi69.collections.pairs;

import java.io.Serializable;

/**
 * The class {@link Quattro} represents four generic values.
 *
 * @param <TL>
 *            the generic type of the top left content.
 * @param <TR>
 *            the generic type of the top right content.
 * @param <BL>
 *            the generic type of the bottom left content.
 * @param <BR>
 *            the generic type of the bottom right content.
 */
public class Quattro<TL, TR, BL, BR> implements Serializable
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The bottom left value.
	 */
	private BL bottomLeft;
	/**
	 * The bottom right value.
	 */
	private BR bottomRight;
	/**
	 * The top left value.
	 */
	private TL topLeft;
	/**
	 * The top right value.
	 */
	private TR topRight;

	/**
	 * Instantiates a new quattro.
	 */
	public Quattro()
	{
	}

	/**
	 * Instantiates a new quattro.
	 *
	 * @param bottomLeft
	 *            the bottom left
	 * @param bottomRight
	 *            the bottom right
	 * @param topLeft
	 *            the top left
	 * @param topRight
	 *            the top right
	 */
	public Quattro(final BL bottomLeft, final BR bottomRight, final TL topLeft, final TR topRight)
	{
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
		this.topLeft = topLeft;
		this.topRight = topRight;
	}

	/**
	 * Builder.
	 *
	 * @param <TL>
	 *            the generic type
	 * @param <TR>
	 *            the generic type
	 * @param <BL>
	 *            the generic type
	 * @param <BR>
	 *            the generic type
	 * @return the quattro builder
	 */
	public static <TL, TR, BL, BR> QuattroBuilder<TL, TR, BL, BR> builder()
	{
		return new QuattroBuilder<>();
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
		return other instanceof Quattro;
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
		if (!(o instanceof Quattro))
		{
			return false;
		}
		final Quattro<?, ?, ?, ?> other = (Quattro<?, ?, ?, ?>)o;
		if (!other.canEqual(this))
		{
			return false;
		}
		final Object this$bottomLeft = this.getBottomLeft();
		final Object other$bottomLeft = other.getBottomLeft();
		if (this$bottomLeft == null
			? other$bottomLeft != null
			: !this$bottomLeft.equals(other$bottomLeft))
		{
			return false;
		}
		final Object this$bottomRight = this.getBottomRight();
		final Object other$bottomRight = other.getBottomRight();
		if (this$bottomRight == null
			? other$bottomRight != null
			: !this$bottomRight.equals(other$bottomRight))
		{
			return false;
		}
		final Object this$topLeft = this.getTopLeft();
		final Object other$topLeft = other.getTopLeft();
		if (this$topLeft == null ? other$topLeft != null : !this$topLeft.equals(other$topLeft))
		{
			return false;
		}
		final Object this$topRight = this.getTopRight();
		final Object other$topRight = other.getTopRight();
		if (this$topRight == null ? other$topRight != null : !this$topRight.equals(other$topRight))
		{
			return false;
		}
		return true;
	}

	/**
	 * The bottom left value.
	 *
	 * @return the bottom left
	 */
	public BL getBottomLeft()
	{
		return this.bottomLeft;
	}

	/**
	 * The bottom left value.
	 *
	 * @param bottomLeft
	 *            the new bottom left
	 */
	public void setBottomLeft(final BL bottomLeft)
	{
		this.bottomLeft = bottomLeft;
	}

	/**
	 * The bottom right value.
	 *
	 * @return the bottom right
	 */
	public BR getBottomRight()
	{
		return this.bottomRight;
	}

	/**
	 * The bottom right value.
	 *
	 * @param bottomRight
	 *            the new bottom right
	 */
	public void setBottomRight(final BR bottomRight)
	{
		this.bottomRight = bottomRight;
	}

	/**
	 * The top left value.
	 *
	 * @return the top left
	 */
	public TL getTopLeft()
	{
		return this.topLeft;
	}

	/**
	 * The top left value.
	 *
	 * @param topLeft
	 *            the new top left
	 */
	public void setTopLeft(final TL topLeft)
	{
		this.topLeft = topLeft;
	}

	/**
	 * The top right value.
	 *
	 * @return the top right
	 */
	public TR getTopRight()
	{
		return this.topRight;
	}

	/**
	 * The top right value.
	 *
	 * @param topRight
	 *            the new top right
	 */
	public void setTopRight(final TR topRight)
	{
		this.topRight = topRight;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $bottomLeft = this.getBottomLeft();
		result = result * PRIME + ($bottomLeft == null ? 43 : $bottomLeft.hashCode());
		final Object $bottomRight = this.getBottomRight();
		result = result * PRIME + ($bottomRight == null ? 43 : $bottomRight.hashCode());
		final Object $topLeft = this.getTopLeft();
		result = result * PRIME + ($topLeft == null ? 43 : $topLeft.hashCode());
		final Object $topRight = this.getTopRight();
		result = result * PRIME + ($topRight == null ? 43 : $topRight.hashCode());
		return result;
	}

	/**
	 * To builder.
	 *
	 * @return the quattro builder
	 */
	public QuattroBuilder<TL, TR, BL, BR> toBuilder()
	{
		return new QuattroBuilder<TL, TR, BL, BR>().bottomLeft(this.bottomLeft)
			.bottomRight(this.bottomRight).topLeft(this.topLeft).topRight(this.topRight);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Quattro(bottomLeft=" + this.getBottomLeft() + ", bottomRight="
			+ this.getBottomRight() + ", topLeft=" + this.getTopLeft() + ", topRight="
			+ this.getTopRight() + ")";
	}

	/**
	 * The class {@link QuattroBuilder}.
	 *
	 * @param <TL>
	 *            the generic type
	 * @param <TR>
	 *            the generic type
	 * @param <BL>
	 *            the generic type
	 * @param <BR>
	 *            the generic type
	 */
	public static class QuattroBuilder<TL, TR, BL, BR>
	{

		/** The bottom left. */
		private BL bottomLeft;

		/** The bottom right. */
		private BR bottomRight;

		/** The top left. */
		private TL topLeft;

		/** The top right. */
		private TR topRight;

		/**
		 * Instantiates a new quattro builder.
		 */
		QuattroBuilder()
		{
		}

		/**
		 * The bottom left value.
		 *
		 * @param bottomLeft
		 *            the bottom left
		 * @return the quattro builder
		 */
		public QuattroBuilder<TL, TR, BL, BR> bottomLeft(final BL bottomLeft)
		{
			this.bottomLeft = bottomLeft;
			return this;
		}

		/**
		 * The bottom right value.
		 *
		 * @param bottomRight
		 *            the bottom right
		 * @return the quattro builder
		 */
		public QuattroBuilder<TL, TR, BL, BR> bottomRight(final BR bottomRight)
		{
			this.bottomRight = bottomRight;
			return this;
		}

		/**
		 * Build it
		 *
		 * @return the quattro
		 */
		public Quattro<TL, TR, BL, BR> build()
		{
			return new Quattro<>(bottomLeft, bottomRight, topLeft, topRight);
		}

		/**
		 * The top left value.
		 *
		 * @param topLeft
		 *            the top left
		 * @return the quattro builder
		 */
		public QuattroBuilder<TL, TR, BL, BR> topLeft(final TL topLeft)
		{
			this.topLeft = topLeft;
			return this;
		}

		/**
		 * The top right value.
		 *
		 * @param topRight
		 *            the top right
		 * @return the quattro builder
		 */
		public QuattroBuilder<TL, TR, BL, BR> topRight(final TR topRight)
		{
			this.topRight = topRight;
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return "Quattro.QuattroBuilder(bottomLeft=" + this.bottomLeft + ", bottomRight="
				+ this.bottomRight + ", topLeft=" + this.topLeft + ", topRight=" + this.topRight
				+ ")";
		}
	}
}
