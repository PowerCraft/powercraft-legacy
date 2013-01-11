package codechicken.nei;

import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import codechicken.nei.forge.GuiContainerManager;

public abstract class TextField extends Widget
{
	public TextField(String ident)
	{
		identifier = ident;
	}
	
	public int getTextColour()
	{
		return focused() ? 0xFFE0E0E0 : 0xFF909090;
	}
	
	public void drawBox(GuiContainerManager gui)
	{
		gui.drawRect(x, y, width, height, 0xffA0A0A0);
		gui.drawRect(x + 1, y + 1, width - 2, height - 2, 0xFF000000);
	}
	
	public void draw(GuiContainerManager gui, int mousex, int mousey)
	{
		drawBox(gui);
		
		String drawtext = text;
		
		if(text.length() > getMaxTextLength())
		{
			int startOffset = drawtext.length() - getMaxTextLength();
			if(startOffset < 0 || startOffset > drawtext.length())
				startOffset = 0;
			drawtext = drawtext.substring(startOffset);
		}
		
		if(focused())
		{
			if((cursorCounter / 6) % 2 == 0)
			{
				drawtext = drawtext + '_';
			}
		}
		
		int textWidth = gui.getStringWidth(text);
		int textx = centered ? x+(width-textWidth)/2 : x + 4;
		int texty = y + (height + 1) / 2 - 3;
		
		gui.drawText(textx, texty, drawtext, getTextColour());
	}
	
	public void onGuiClick(int mousex, int mousey)
	{
		if(!contains(mousex, mousey))
		{
			setFocus(false);
		}
	}
	
	public boolean handleClick(int mousex, int mousey, int button)
	{
		if(button == 1)
		{
			text = "";
			onTextChange();
		}
		setFocus(true);
		return true;
	}
	
	public boolean handleKeyPress(int keyID, char keyChar)
	{
		if(LayoutManager.getInputFocused() != this)
			return false;
		
		if(keyID == Keyboard.KEY_BACK)
		{
			if(text.length() > 0)
			{
				text = text.substring(0, text.length() - 1);
				onTextChange();
				backdowntime = System.currentTimeMillis();
			}
		}
		else if(keyID == Keyboard.KEY_RETURN || keyID == Keyboard.KEY_ESCAPE)
		{
			setFocus(false);
		}	
		else if(keyChar == 22)//paste
		{
			String pastestring = GuiScreen.getClipboardString();
			if(pastestring == null) 
			{
				pastestring = "";
			}
			
			if(isValid(text + pastestring))
			{
				text = text + pastestring;
				onTextChange();
			}			
		}
		else if(isValid(text+keyChar)) 
		{
			text = text+keyChar;
			onTextChange();
		}
		return true;
	}
	
	public abstract void onTextChange();

	public boolean isValid(String string)
	{
		return ChatAllowedCharacters.allowedCharacters.indexOf(string.charAt(string.length()-1)) >= 0;
	}

	public void update(GuiContainerManager gui)
	{
		cursorCounter++;
		if(backdowntime > 0)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && text.length() > 0)
			{
				if(System.currentTimeMillis() - backdowntime > 200 / (1+backs * 0.3F))
				{
					text = text.substring(0, text.length() - 1);
					onTextChange();
					backdowntime = System.currentTimeMillis();
					backs++;
				}
			}
			else
			{
				backdowntime = 0;
				backs = 0;
			}
		}
	}
	
	public void setText(String s)
	{
		text = s;
		onTextChange();
	}
	
	private int getMaxTextLength()
	{
		return width / 6 - 2;
	}
	
	public void setFocus(boolean focus)
	{
		if(focus)
		{
			LayoutManager.setInputFocused(this);
		}
		else if(focused())
		{
			LayoutManager.setInputFocused(null);
		}
	}
	
	public boolean focused()
	{
		return LayoutManager.getInputFocused() == this;
	}

	public boolean centered;
	public long backdowntime;
	public int backs;
	public String text = "";
	public String identifier;
	public int cursorCounter;
}
