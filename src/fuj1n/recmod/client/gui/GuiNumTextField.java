package fuj1n.recmod.client.gui;

import net.minecraft.client.gui.*;

public class GuiNumTextField extends Gui
{

	private FontRenderer fn;
	private int x, y, width, height;
	public int min, max;
	public String value = "";
	public boolean hasFocus, canFocus = true, visible = true;

	public GuiNumTextField(FontRenderer fn, int x, int y, int width, int height, int min, int max)
	{
		this.fn = fn;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.min = min;
		this.max = max;
	}

	public GuiNumTextField(FontRenderer fn, int x, int y, int width, int height, int min, int max, int value)
	{
		this.fn = fn;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.min = min;
		this.max = max;
		this.value = Integer.toString(value);
	}

	public void draw (String label)
	{
		if (visible)
		{
			if (label != null && !label.equals(""))
			{
				fn.drawString(label, x - 5 - fn.getStringWidth(label), y + height / 2 - fn.FONT_HEIGHT / 2, 0x000000);
			}

			drawRect(x - 1, y - 1, x + width + 1, y + height + 1, 0xFFFFFFFF);
			drawRect(x, y, x + width, y + height, 0xFF000000);
			fn.drawString(value + (hasFocus ? "_" : ""), x + 5, y + height / 2 - fn.FONT_HEIGHT / 2, 0xFFFFFF);
		}
	}

	public void keyTyped (char c, int i)
	{
		if (!canFocus)
			hasFocus = false;

		if (!hasFocus)
			return;

		if ((value.length() == 0 && c == '-' && min < 0) || Character.isDigit(c))
		{
			if (value(value + c) >= min && value(value + c) <= max)
			{
				value += c;
			}
		}
		else if (i == 14)
		{
			if (value.length() > 0)
			{
				value = value.substring(0, value.length() - 1);
			}
		}
	}

	public void mouseClicked (int x, int y)
	{
		if (canFocus)
		{
			if (x > this.x && y > this.y && x < this.x + width && y < this.y + height)
			{
				hasFocus = true;
			}
			else
			{
				hasFocus = false;
			}
		}
		else
		{
			hasFocus = false;
		}
	}

	public int value ()
	{
		return value(value);
	}

	public int value (String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
		}

		return 0;
	}
}
