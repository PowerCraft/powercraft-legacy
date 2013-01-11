package codechicken.nei;

import java.util.List;

import codechicken.nei.forge.GuiContainerManager;

import net.minecraft.item.ItemStack;

public abstract class Widget
{
    public Widget()
    {
    }

    public abstract void draw(GuiContainerManager gui, int mousex, int mousey);

    public void postDraw(GuiContainerManager gui, int mousex, int mousey)
    {
    	
    }
    
    public boolean handleClick(int mousex, int mousey, int button)
    {
        return true;
    }
    
	public void onGuiClick(int mousex, int mousey)
	{
	}
    
	public void mouseUp(int mousex, int mousey, int button)
	{
        return;
	}

	public boolean handleKeyPress(int keyID, char keyChar)
	{
		return false;
	}
	
	public boolean onMouseWheel(int i, int mousex, int mousey)
	{
		return false;
	}
	
	public void update(GuiContainerManager gui)
	{
		
	}
	
    public boolean contains(int posx, int posy)
    {
        return posx >= x && posx < x + width && posy >= y && posy < y + height;
    }

    public void resize()
    {
    }
    
	public ItemStack getStackMouseOver(int mousex, int mousey)
	{
		return null;
	}
	
	public List<String> handleTooltip(int mx, int my, List<String> tooltip)
	{
		return tooltip;
	}
	
	public void loseFocus()
	{
		
	}
	
	public void gainFocus()
	{
		
	}

    public int x;
    public int y;
    public int z;
    public int width;
    public int height;
}
