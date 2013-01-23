package codechicken.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import codechicken.nei.forge.GuiContainerManager;

public class DropDownWidget extends Widget
{
	public DropDownWidget()
	{
		for(int i = 0; i < 7; i++)
		{
			final int savestate = i;
			stateButtons[i] = new SaveLoadButton("VIS")
			{
				@Override
				public void onTextChange()
				{					
					NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
		        	NEIClientConfig.saveCompound.setTag("vis", hashSave);
		        	
		        	NBTTagCompound statelist = hashSave.getCompoundTag("statename");
		        	hashSave.setTag("statename", statelist);
					
		        	statelist.setString(""+savestate, label.substring(5));
		        	NEIClientConfig.saveConfig();
				}
				
				@Override
				public boolean onButtonPress(boolean rightclick)
				{
					if(rightclick)
						return false;
					
		            if(ItemVisibilityHash.isStateSaved(savestate))
		            	NEIClientConfig.vishash.loadState(savestate);
		            else
		            	NEIClientConfig.vishash.saveState(savestate);
		            return true;
				}
			};
			deleteButtons[i] = new Button("x")
			{
				@Override
				public boolean onButtonPress(boolean rightclick)
				{
					if(!rightclick)
					{
						NEIClientConfig.vishash.clearState(savestate);	
						return true;
					}
					return false;
				}
			};
			
            stateButtons[i].height = 20;
            deleteButtons[i].width = 16;
            deleteButtons[i].height = 16;			
		}
	}
	
	public void draw(GuiContainerManager gui, int mousex, int mousey)
	{
        hoverItem = null;
		boolean mouseover = super.contains(mousex, mousey);
		
		texturedButtons = NEIClientConfig.getLayoutStyle() == 0;
		if(!texturedButtons)
        {
        	gui.drawRect(x, y, width, height, mouseover ? 0xee401008 : 0xee000000);
    		gui.drawTextCentered(x, y, width, height, "Item SubSets", 0xFFFFFFFF);
        }
        else
        {
            GL11.glDisable(2896 /*GL_LIGHTING*/);
			gui.bindTextureByName("/gui/gui.png");
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        int tex = mouseover ? 2 : 1;
	        LayoutManager.drawButtonBackground(gui, x-1, y, width+2, height, true, tex);
	        
	        if(mouseover)
	        {
	        	gui.drawTextCentered("Item SubSets", x + width / 2, y + (height - 8) / 2, 0xffffa0);
	        } else
	        {
	        	gui.drawTextCentered("Item SubSets", x + width / 2, y + (height - 8) / 2, 0xe0e0e0);
	        }
        }
		
		if(getDropDown() == 1)
		{
			if(mouseovernamestack.size() == 0)
			{
				setDropDown(0);
				return;
			}
			updateMouseOver(mousex, mousey);
			updatePosition(gui, mousex, mousey);
			file.draw(gui, mousex, mousey);
		}
		else if(getDropDown() == 2)
		{
			updateStatePosition(gui);
			drawStateButtons(gui, mousex, mousey);
		}
	}
	
	private void drawStateButtons(GuiContainerManager gui, int mousex, int mousey)
	{
		for(int i = 0; i < 7; i++)
		{
			stateButtons[i].draw(gui, mousex, mousey);
			deleteButtons[i].draw(gui, mousex, mousey);
		}
	}

	private void updateStatePosition(GuiContainerManager gui)
	{
		int maxWidth = 0;
        for(int i = 0; i < 7; i++)
        {
            deleteButtons[i].x = -1000;
            stateButtons[i].y = height+2+ 22 * i;
            
            NBTTagCompound hashSave = NEIClientConfig.saveCompound.getCompoundTag("vis");
        	NEIClientConfig.saveCompound.setTag("vis", hashSave);
        	
        	NBTTagCompound statelist = hashSave.getCompoundTag("statename");
        	hashSave.setTag("statename", statelist);
        	
        	String name = statelist.getString(""+i);
            if(statelist.getTag(""+i) == null)
            {
            	name = ""+(i+1);
            	statelist.setString(""+i, name);
            }
            stateButtons[i].label = (ItemVisibilityHash.isStateSaved(i) ? "Load " : "Save ") + name;
            
            int buttonw = gui.getStringWidth(stateButtons[i].label) + 26;
            if(buttonw + 22 > width)
            {
                buttonw = width - 22;
            }
            if(buttonw > maxWidth)
            {
                maxWidth = buttonw;
            }
        }
        
        int buttonx = x + (width - (maxWidth + 20)) / 2;
        
        for(int i = 0; i < 7; i++)
        {
            stateButtons[i].width = maxWidth;
            stateButtons[i].x = buttonx;
            if(ItemVisibilityHash.isStateSaved(i))
            {
                deleteButtons[i].x = stateButtons[i].x + maxWidth + 2;
                deleteButtons[i].y = stateButtons[i].y + 2;
            }
        }
	}

	private void updateMouseOver(int mousex, int mousey)
	{
		String newmouseover = file.updateMouseOver(mousex, mousey, mouseovernamestack.get(0));
		if(!mouseoverTickRecorded)
		{
			if(canChangeMouseOver)
			{
				String prevmouseover = mouseovernamestack.get(mouseovernamestack.size() - 1);
				String currentmouseover = mouseovernamestack.get(0);
				if(!newmouseover.equals(prevmouseover))
				{
					for(int i = 0; i < mouseovernamestack.size(); i++)
					{
						mouseovernamestack.set(i, currentmouseover);
					}
				}
				mouseovernamestack.add(newmouseover);
			}
			else
			{
				mouseovernamestack.add(mouseovernamestack.get(mouseovernamestack.size() - 1));
			}
			mouseoverTickRecorded = true;
		}
		if(mouseovernamestack.get(0).equals("") && !LayoutManager.dropDown.contains(mousex, mousey))
		{
			setDropDown(0);
		}
	}

	private void updatePosition(GuiContainerManager gui, int mousex, int mousey)
	{
		rehashMaxHeight(gui);
		while(true)
		{
			droppedwidth = 0;
			file.position(x + relx, y + height);
			
			if(droppedwidth > width)
			{
				int levelwidth = file.getWidthAtLevel(hiddenlevel);
				
				if(mousex - levelwidth < x)//would move mouse out of bounds
				{
					levelwidth = droppedwidth - width;
				}
				
				moveMouse(gui.window.mc, -levelwidth, 0);
				mousex -= levelwidth;

				relx -= levelwidth;
				hiddenstack.addLast(levelwidth);
				hiddenlevel++;
			}
			else if(relx < 0 && width - droppedwidth > hiddenstack.getLast())
			{
				int levelwidth = hiddenstack.getLast();
				
				if(!mouseovernamestack.get(0).equals(""))//we are over a tag and need adjusting
				{
					moveMouse(gui.window.mc, levelwidth, 0);
					mousex += levelwidth;
				}

				relx += levelwidth;
				hiddenstack.removeLast();
				hiddenlevel--;
			}
			else
			{
				break;
			}
		}
	}
	
	public static void moveMouse(Minecraft mc, int relx, int rely)
	{
		ScaledResolution sres = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        Mouse.setCursorPosition(Mouse.getX() + (relx * mc.displayWidth / sres.getScaledWidth()), Mouse.getY() + (rely * mc.displayHeight / sres.getScaledHeight()));
	}
	
	private void rehashMaxHeight(GuiContainerManager gui)
	{
		maxheight = gui.window.height - height - y - 25;//25 for the search box
		maxheight = (maxheight / DropDownFile.slotheight) * DropDownFile.slotheight;
	}

	public boolean handleClick(int mousex, int mousey, int button)
	{
		if(super.contains(mousex, mousey))//click on this
		{
			if(button == 0)
			{
				if(System.currentTimeMillis() - lastclicktime < 300)
				{
					file.showAllItems();
					file.updateState();
					ItemList.updateSearch();
					NEIClientConfig.vishash.save();
				}
				setDropDown(1);
				lastclicktime = System.currentTimeMillis();
			}
			else if(button == 1)
			{
				if(getDropDown() == 2)
				{
					setDropDown(0);
				}
				else
				{
					setDropDown(2);
				}
			}
			return true;
		}
		else if(getDropDown() == 1)
		{
			return file.click(mousex, mousey, button);
		}
		else if(getDropDown() == 2)
		{
			return processStateClick(mousex, mousey, button);
		}
	
		return false;
	}
	
	private boolean processStateClick(int mousex, int mousey, int button)
	{
		for(int i = 0; i < 7; i++)
		{
			if((stateButtons[i].contains(mousex, mousey) && stateButtons[i].handleClick(mousex, mousey, button)) || 
					(deleteButtons[i].contains(mousex, mousey) && deleteButtons[i].handleClick(mousex, mousey, button)))
			{
				return true;
			}
		}
		
		setDropDown(0);
		return false;
	}
	
	public boolean handleKeyPress(int keyID, char keyChar)
	{
		for(int i = 0; i < 7; i++)
		{
			if(stateButtons[i].handleKeyPress(keyID, keyChar))
			{
				return true;
			}
		}
		return false;
	}
	
	public void onGuiClick(int mousex, int mousey)
	{
		if(getDropDown() == 2)
		{
			for(int i = 0; i < 7; i++)//renameable buttons naming
			{
				stateButtons[i].onGuiClick(mousex, mousey);
			}
			if(!contains(mousex, mousey))
			{
				setDropDown(0);
			}
		}
	}

	public void mouseUp(int mousex, int mousey, int button)
	{
		if(getDropDown() == 1)file.mouseUp(mousex, mousey, button);
	}
	
	public boolean contains(int mousex, int mousey)
	{		
		return super.contains(mousex, mousey) || 
				(getDropDown() == 1 && file.contains(mousex, mousey) ||
				getDropDown() == 2 && statesContain(mousex, mousey));
	}

	private boolean statesContain(int mousex, int mousey)
	{
		for(int i = 0; i < 7; i++)
		{
			if(stateButtons[i].contains(mousex, mousey) || deleteButtons[i].contains(mousex, mousey))
			{
				return true;
			}
		}
		return false;
	}

	public boolean onMouseWheel(int i, int mousex, int mousey)
	{
		if(getDropDown() == 1)
		{
			file.onMouseWheel(-i);
			return true;
		}
		return false;
	}
	
	public void setHoverItem(ItemStack item)
	{
		hoverItem = item;
	}
	
	public void update(GuiContainerManager gui)
	{
		if(getDropDown() == 1)
		{
			if(mouseovernamestack.size() == 0)
			{
				setDropDown(0);
				return;
			}
			mouseovernamestack.remove(0);
			if(!mouseoverTickRecorded)
			{
				mouseovernamestack.add(mouseovernamestack.get(mouseovernamestack.size()-1));
			}
			mouseoverTickRecorded = false;
		}
		else if(getDropDown() == 2)
		{
			for(int i = 0; i < 7; i++)
			{
				stateButtons[i].update(gui);
			}
		}
	}
	
	public void setDropDown(int drop)
	{
		if(drop == 1)
		{
			mouseoverTickRecorded = false;
			mouseovernamestack.clear();
			for(int i = 0; i < stacklatency; i++)
			{
				mouseovernamestack.add("");
			}
		}
		dropDowna = drop;
	}
	
	public int getDropDown()
	{
		return dropDowna;
	}
	
	@Override
	public ItemStack getStackMouseOver(int mousex, int mousey)
	{
		return hoverItem;
	}
	
	@Override
	public List<String> handleTooltip(int mx, int my, List<String> tooltip)
	{
		return tooltip;
	}
	
	public DropDownFile file = DropDownFile.dropDownInstance;
	private int dropDowna;
	private long lastclicktime;
	private ArrayList<String> mouseovernamestack = new ArrayList<String>();
	private boolean mouseoverTickRecorded;
	public boolean canChangeMouseOver = true;
	public ItemStack hoverItem;

	public SaveLoadButton[] stateButtons = new SaveLoadButton[7];
	public Button[] deleteButtons = new Button[7];
    
	public int maxheight;
	public int droppedwidth;
	private int relx;
	private int hiddenlevel;
	private LinkedList<Integer> hiddenstack = new LinkedList<Integer>();
	
	public static boolean texturedButtons;
	private static final int stacklatency = 4;
}
