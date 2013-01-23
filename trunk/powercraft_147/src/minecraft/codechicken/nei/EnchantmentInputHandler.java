package codechicken.nei;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.InterActionMap;
import codechicken.nei.forge.IContainerInputHandler;


public class EnchantmentInputHandler implements IContainerInputHandler
{
	@Override
	public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode)
	{
		return false;
	}

	@Override
	public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button)
	{
		return false;
	}

	@Override
	public void onKeyTyped(GuiContainer gui, char keyChar, int keyID)
	{
	}

	@Override
	public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID)
	{
		if(keyID == NEIClientConfig.getKeyBinding("enchant") && NEIClientConfig.isActionPermissable(InterActionMap.ENCHANT))
	    {
    		ClientPacketHandler.sendOpenEnchantmentWindow();
	    	return true;
	    }
		return false;
	}

	@Override
	public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button)
	{
	}

	@Override
	public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button)
	{
	}

	@Override
	public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
	{
		return false;
	}

	@Override
	public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
	{
	}
}
