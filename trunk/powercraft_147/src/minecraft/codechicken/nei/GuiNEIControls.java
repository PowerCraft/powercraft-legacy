package codechicken.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiSmallButton;

public class GuiNEIControls extends GuiNEIOptions
{
	public static class NEIKeyBind
	{
		String ident;
		String desc;
		
		public NEIKeyBind(String s, String s1)
		{
			ident = s;
			desc = s1;
		}
	}
	
	public GuiNEIControls(GuiContainer parentContainer)
	{
		super(parentContainer);
		keyBinds = getOptionList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		for(int i = 0; i < keyBinds.size(); i++)
		{
			controlList.add(new GuiSmallButton(i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), 70, 20, ""));
		}
		super.initGui();
	}
	
	public String getBackButtonName()
	{
		return "Settings";
	}
	
	public void onBackButtonClick()
	{
        mc.displayGuiScreen(new GuiNEISettings(parentScreen));
	}

	private boolean doesButtonClash(String ident)
	{
		int buttonID = NEIClientConfig.getKeyBinding(ident);
		if(buttonID == mc.gameSettings.keyBindInventory.keyCode)return true;
		for(NEIKeyBind key : keyBinds)
		{
			if(!ident.equals(key.ident) && buttonID == NEIClientConfig.getKeyBinding(key.ident))return true;
		}
		return false;
	}
	
	public void updateButtonNames()
	{
		for(int i = 0; i < keyBinds.size(); i++)
		{
			GuiButton button = (GuiButton)controlList.get(i);
			String name = keyBinds.get(i).ident;
			int keyCode = NEIClientConfig.getKeyBinding(name);
			
			if(focusedButton == i)
			{
				button.displayString = "\247f> \247e??? \247f<";
			}
			else if(doesButtonClash(name))
			{
				button.displayString = "\247c" + Keyboard.getKeyName(keyCode);
			}
			else
			{
				button.displayString = Keyboard.getKeyName(keyCode);
			}
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id < keyBinds.size())
	    {
	    	focusedButton = guibutton.id;
	    	updateButtonNames();
	    }
	    else
	    {
	    	super.actionPerformed(guibutton);
	    }
	}
	
	protected void keyTyped(char keyChar, int keyID)
	{
		if(focusedButton >= 0 && keyID != 1)
		{
			NEIClientConfig.setKeyBinding(keyBinds.get(focusedButton).ident, keyID);
			focusedButton = -1;
		}
		super.keyTyped(keyChar, keyID);
	}
	
	protected void mouseClicked(int i, int j, int k)
    {
        if(focusedButton >= 0)focusedButton = -1;
        super.mouseClicked(i, j, 0);
    }
	
	public void drawScreen(int i, int j, float f)
	{
	    super.drawScreen(i, j, f);
	    
	    drawCenteredString(fontRenderer, "NEI Controls", width / 2, height / 6 - 24, 0xffffff);
    	for(int b = 0; b < keyBinds.size(); b++)
    	{
	        drawString(fontRenderer, keyBinds.get(b).desc, width / 2 - 155 + (b % 2) * 160 + 70 + 6, height / 6 + 24 * (b >> 1) + 7, 0xFFFFFFFF);
    	}
	}
	
	public List<NEIKeyBind> getOptionList()
	{
		Class<?> classz = getClass();
		while(GuiNEIControls.class.isAssignableFrom(classz))
		{
			LinkedList<NEIKeyBind> list = keyBindMap.get(getClass());
			if(list != null)
				return list;
			classz = classz.getSuperclass();
		}
		
		return null;
	}
	
	int focusedButton = -1;
	public List<NEIKeyBind> keyBinds = new ArrayList<NEIKeyBind>();
	
	public static HashMap<Class<? extends GuiNEIControls>, LinkedList<NEIKeyBind>> keyBindMap = new HashMap<Class<? extends GuiNEIControls>, LinkedList<NEIKeyBind>>();
}
