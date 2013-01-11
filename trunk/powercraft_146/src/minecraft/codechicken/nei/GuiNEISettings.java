package codechicken.nei;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiNEISettings extends GuiNEIOptions
{
	public static abstract class NEIOption
	{
		private String tagName;
		private boolean globalOnly = false;
		
		public NEIOption(String n)
		{
			tagName = n;
		}
		
		public NEIOption setGlobalOnly()
		{
			globalOnly = true;
			return this;
		}
		
		public void onClick()
		{
			NEIClientConfig.toggleBooleanSetting(getIdent());
		}
		
		public String getIdent()
		{
			return tagName;
		}
		
		public abstract String updateText();
		
		/**
		 * A utility function to be used inside subclasses for updateText
		 * WARNING: Do not call on non boolean settings.
		 */
		public final boolean enabled()
		{
			return NEIClientConfig.getBooleanSetting(getIdent());
		}
		
		/**
		 * A utility function to be used inside subclasses for updateText
		 * WARNING: Do not call on non int settings.
		 */
		public int intValue()
		{
			return NEIClientConfig.getIntSetting(getIdent());
		}
	}
	
	public GuiNEISettings(GuiContainer parentContainer)
	{
		super(parentContainer);
		options = getOptionList();
	}
	
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		for(int i = 0; i < options.size(); i++)
		{
			if(options.get(i).globalOnly && !global)
				continue;
			
			controlList.add(new GuiOptionButton(i, (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), ""));
		}
	    controlList.add(new GuiButton(202, width / 2 - 50, height / 6 - 30, 100, 20, "NEI "+(global ? "Global" : "World")+" Options"));
	    controlList.add(new GuiButton(201, width / 2 - 100, height / 6 + 145, "Controls"));
	    super.initGui();
	}
	
	public void updateButtonNames()
	{
		for(int i = 0; i < controlList.size(); i++)
		{
			GuiButton button = (GuiButton)controlList.get(i);
			if(button.id >= options.size())
				continue;
			
			NEIOption option = options.get(button.id);
			
	    	NEIClientConfig.global = global;
			button.displayString = option.updateText();
	    	NEIClientConfig.global = false;
			if(!global)
			{
				button.enabled = NEIClientConfig.isWorldSpecific(option.getIdent());
			}
		}
	}

	@Override
	public String getBackButtonName()
	{
		return "Controls";
	}
	
	@Override
	public void onBackButtonClick()
	{
        mc.displayGuiScreen(new GuiNEIControls(parentScreen));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 202)
	    {
	    	global = !global;
	        try
			{
				mc.displayGuiScreen(getClass().getConstructor(GuiContainer.class).newInstance(parentScreen));
			}
			catch(Exception e)
			{
				NEIClientUtils.reportException(e);
			}
	    }
	    else if(guibutton.id < options.size())
	    {
	    	NEIOption option = options.get(guibutton.id);
	    	if(mousebutton == 1 && guibutton.enabled && !global)
	    	{
	    		NEIClientConfig.removeWorldSetting(option.getIdent());
	    	}
	    	else if(mousebutton == 0 && !guibutton.enabled && !global)
	    	{
	    		NEIClientConfig.copyWorldSetting(option.getIdent());
	    	}
	    	else if(mousebutton == 0)
	    	{
		    	NEIClientConfig.global = global;
		    	option.onClick();
		    	NEIClientConfig.global = false;
	    	}
	    	updateButtonNames();
	    }
	    else
	    {
	    	super.actionPerformed(guibutton);
	    }
	}
	
	public List<NEIOption> getOptionList()
	{
		Class<?> classz = getClass();
		while(GuiNEISettings.class.isAssignableFrom(classz))
		{
			LinkedList<NEIOption> list = optionMap.get(getClass());
			if(list != null)
				return list;
			classz = classz.getSuperclass();
		}
		
		return new LinkedList<NEIOption>();
	}

    protected void mouseClicked(int i, int j, int k)
    {
        mousebutton = k;
        super.mouseClicked(i, j, 0);
    }
	
	int mousebutton = 0;
	public static boolean global = true;
	public List<NEIOption> options;
	
	public static HashMap<Class<? extends GuiNEISettings>, LinkedList<NEIOption>> optionMap = new HashMap<Class<? extends GuiNEISettings>, LinkedList<NEIOption>>();
}
