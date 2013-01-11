package codechicken.nei;

import codechicken.nei.forge.GuiContainerManager;

public class Label extends Widget
{
	boolean centered;
	int colour;
	String text;
	
	public Label(String s, boolean center, int color)
	{
		text = s;
		centered = center;
		colour = color;
	}
	
	public Label(String s, boolean center)
	{
		this(s, center, 0xFFFFFFFF);
	}
	
	@Override
	public void draw(GuiContainerManager gui, int mousex, int mousey)
	{
		if(centered)
			gui.drawTextCentered(text, x, y, colour);
		else
			gui.drawText(x, y, text, colour);
	}
}
