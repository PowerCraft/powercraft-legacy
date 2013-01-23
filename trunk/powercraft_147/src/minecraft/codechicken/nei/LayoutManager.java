package codechicken.nei;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.lwjgl.opengl.GL11;

import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.api.LayoutStyle;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerDrawHandler;
import codechicken.nei.forge.IContainerInputHandler;
import codechicken.nei.forge.IContainerObjectHandler;
import codechicken.nei.forge.IContainerTooltipHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.Slot;

public class LayoutManager implements IContainerInputHandler, IContainerTooltipHandler, IContainerDrawHandler, IContainerObjectHandler
{
	private static LayoutManager instance;
	
	private static Widget inputFocused;	
	/**
	 * Sorted bottom first
	 */
	private static TreeSet<Widget> drawWidgets;
	/**
	 * Sorted top first
	 */
	private static TreeSet<Widget> controlWidgets;
	
	public static ItemPanel itemPanel;
	public static DropDownWidget dropDown;
	public static TextField searchField;

	public static Button options;
	
	public static Button prev;
	public static Button next;
	public static Label pageLabel;
	public static Button more;
	public static Button less;
	public static ItemQuantityField quantity;
	
	public static SaveLoadButton[] stateButtons;
	public static Button[] deleteButtons;
	
	public static Button delete;
	public static Button2ActiveState creative;
	public static Button rain;
	public static Button magnet;
	public static Button[] timeButtons = new Button[4];
	public static final String[] timeZones = new String[]{"dawn", "noon", "dusk", "midnight"};
	public static Button heal;
	
    public static IRecipeOverlayRenderer overlayRenderer;

	public static HashMap<Integer, LayoutStyle> layoutStyles = new HashMap<Integer, LayoutStyle>();
	
	public static void load()
	{
		API.addLayoutStyle(0, new LayoutStyleMinecraft());
		API.addLayoutStyle(1, new LayoutStyleTMIOld());
		
		instance = new LayoutManager();
		GuiContainerManager.addInputHandler(instance);
		GuiContainerManager.addTooltipHandler(instance);
		GuiContainerManager.addDrawHandler(instance);
		GuiContainerManager.addObjectHandler(instance);
		init();
	}	
	
	@Override
	public void onPreDraw(GuiContainer gui)
	{
		if(!NEIClientConfig.isHidden() && NEIClientConfig.isEnabled() && gui instanceof InventoryEffectRenderer)//Reset the gui to the center of the screen, for potion effect offsets etc
    	{
	        gui.guiLeft = (gui.width - gui.xSize) / 2;
	        gui.guiTop = (gui.height - gui.ySize) / 2;
	        
	        if(gui instanceof GuiContainerCreative && gui.controlList.size() > 0)
	        {
	        	GuiButton button1 = (GuiButton)gui.controlList.get(0);
	        	GuiButton button2 = (GuiButton)gui.controlList.get(1);
	        	button1.xPosition = gui.guiLeft;
	        	button2.xPosition = gui.guiLeft + gui.xSize - 20;
	        }
    	}
	}
	
	@Override
	public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) 
	{		
		for(Widget widget : controlWidgets)
        {
        	widget.onGuiClick(mousex, mousey);
        }
	}
	
	@Override
	public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button)
    {
    	if(NEIClientConfig.isHidden())
    		return false;
    	
    	if(!NEIClientConfig.isEnabled())
    		return options.contains(mousex, mousey) && options.handleClick(mousex, mousey, button);
    	
        for(Widget widget : controlWidgets)
        {
        	widget.onGuiClick(mousex, mousey);
        	if(widget.contains(mousex, mousey) && widget.handleClick(mousex, mousey, button))
        		return true;
        }
        
        return false;
    }
	
	@Override
	public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey)
	{
		if(!NEIClientConfig.isEnabled() || NEIClientConfig.isHidden())
            return false;

        for(Widget widget : controlWidgets)
        	if(widget.contains(mousex, mousey))
        		return true;
        
        return false;
	}
	
	public boolean keyTyped(GuiContainer gui, char keyChar, int keyID)
    {
    	if(NEIClientConfig.isEnabled() && !NEIClientConfig.isHidden())
        {
    		if(inputFocused != null)
        		return inputFocused.handleKeyPress(keyID, keyChar);
            
            for(Widget widget : controlWidgets)
            	if(widget.handleKeyPress(keyID, keyChar))
            		return true;
        }
        
        return false;
    }
	
	@Override
	public void onKeyTyped(GuiContainer gui, char keyChar, int keyID)
	{
	}
	
	@Override
	public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID)
	{        
        if(keyID == NEIClientConfig.getKeyBinding("hide"))
        {
        	NEIClientConfig.toggleBooleanSetting("options.hidden");
        	return true;
        }
        return false;
	}
	
	public void onMouseUp(GuiContainer gui, int mx, int my, int button)
    {
    	try
        {
        	if(!NEIClientConfig.isEnabled() || NEIClientConfig.isHidden())
            {
                return;
            }
            for(Widget widget : controlWidgets)
            {
            	widget.mouseUp(mx, my, button);
            }
        }
        catch(Exception exception)
        {
            NEIClientUtils.reportException(exception);
            NEIClientConfig.setEnabled(false);
        }
    }

	@Override
	public ItemStack getStackUnderMouse(GuiContainer gui, int mousex, int mousey)
	{
		if(!NEIClientConfig.isEnabled() || NEIClientConfig.isHidden())
        {
            return null;
        }
    	
        for(Widget widget : controlWidgets)
        {
        	ItemStack stack = widget.getStackMouseOver(mousex, mousey);
        	if(stack != null)
        		return stack;
        }
        return null;
	}
	
	public void renderObjects(GuiContainer gui, int mousex, int mousey)
	{
        if(!NEIClientConfig.isHidden())
        {
    		layout(gui);
        	if(NEIClientConfig.isEnabled())
            {
        		getLayoutStyle().drawBackground(gui.manager);
        		for(Widget widget : drawWidgets)
                {
                	widget.draw(gui.manager, mousex, mousey);
                }
            }
            else
            {
            	options.draw(gui.manager, mousex, mousey);
            }
    		
    		GL11.glEnable(GL11.GL_LIGHTING);
    		GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
	}
	
	@Override
	public void postRenderObjects(GuiContainer gui, int mousex, int mousey)
	{
		if(!NEIClientConfig.isHidden() && NEIClientConfig.isEnabled())
        {
    		for(Widget widget : drawWidgets)
            {
            	widget.postDraw(gui.manager, mousex, mousey);
            }
		
    		/*GL11.glEnable(GL11.GL_LIGHTING);
    		GL11.glEnable(GL11.GL_DEPTH_TEST);*/
        }
	}
	
	@Override
	public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip)
	{
		if(!NEIClientConfig.isHidden() && NEIClientConfig.isEnabled() && gui.manager.shouldShowTooltip())
    	{
			for(Widget widget : controlWidgets)
	        {
				currenttip = widget.handleTooltip(mousex, mousey, currenttip);
	        }
    	}
		return currenttip;
	}
	
	@Override
	public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip)
	{
		String overridename = ItemInfo.getOverrideName(itemstack.itemID, itemstack.getItemDamage());
		if(overridename != null)
			currenttip.set(0, overridename);
		
		String mainname = currenttip.get(0);
        if(NEIClientConfig.showIDs() && itemstack != null)
        {
            mainname = (new StringBuilder()).append(mainname).append(" ").append(itemstack.itemID).toString();
            if(itemstack.getItemDamage() != 0)
            {
                mainname = (new StringBuilder()).append(mainname).append(":").append(itemstack.getItemDamage()).toString();
            }
            currenttip.set(0, mainname);
        }
        
        return currenttip;
	}
	
	public static void layout(GuiContainer gui)
	{
		VisiblityData visiblity = new VisiblityData();
		if(NEIClientConfig.isHidden())
			visiblity.showNEI = false;
		if(gui.height - gui.ySize <= 40)
			visiblity.showSearchSection = false;
		if(gui.guiLeft - 4 < 76)
			visiblity.showWidgets = false;
		
		for(INEIGuiHandler handler : GuiInfo.guiHandlers)
			handler.modifyVisiblity(gui, visiblity);
		
		visiblity.translateDependancies();
		
		getLayoutStyle().layout(gui, visiblity);
		
		updateWidgetVisiblities(gui, visiblity);
	}
	
	private static void init()
	{
		itemPanel = new ItemPanel();
		dropDown = new DropDownWidget();
		searchField = new SearchField("search");
		
		options = new Button("Options")
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
					NEIClientUtils.overlayScreen(new GuiNEISettings(NEIClientUtils.getGuiContainer()));
					return true;
				}
				return false;
			}
		};
		prev = new Button("Prev")
		{
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
					LayoutManager.itemPanel.scroll(-1);
					return true;
				}
				return false;
			}
		};
		next = new Button("Next")
		{
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
					LayoutManager.itemPanel.scroll(1);
					return true;
				}
				return false;
			}
		};
		pageLabel = new Label("(0/0)", true);
		more = new Button("+")
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(rightclick)
					return false;
				
				int modifier = NEIClientUtils.controlKey() ? 64 : NEIClientUtils.shiftKey() ? 10 : 1;
	    		
	    		int quantity = NEIClientConfig.getItemQuantity() + modifier;
	    		if(quantity < 0)
	    			quantity = 0;
	    		
	    		NEIClientUtils.setItemQuantity(quantity);
	    		return true;
			}
		};
		less = new Button("-")
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(rightclick)
					return false;
				
				int modifier = NEIClientUtils.controlKey() ? -64 : NEIClientUtils.shiftKey() ? -10 : -1;
	    		
	    		int quantity = NEIClientConfig.getItemQuantity() + modifier;
	    		if(quantity < 0)
	    			quantity = 0;
	    		
	    		NEIClientUtils.setItemQuantity(quantity);
	    		return true;
			}
		};
		quantity = new ItemQuantityField("quantity");

		stateButtons = new SaveLoadButton[7];
		deleteButtons = new Button[7];
		
		for(int i = 0; i < 7; i++)
		{
			final int savestate = i;
			stateButtons[i] = new SaveLoadButton("")
			{
				@Override
				public boolean onButtonPress(boolean rightclick)
				{
					if(NEIClientConfig.isStateSaved(savestate))
						NEIClientConfig.loadState(savestate);
					else
						NEIClientConfig.saveState(savestate);
					return true;
				}
				
				@Override
				public void onTextChange()
				{
					NBTTagCompound statelist = NEIClientConfig.saveCompound.getCompoundTag("statename");
		        	NEIClientConfig.saveCompound.setTag("statename", statelist);
		        	
		        	statelist.setString(""+savestate, label.substring(5));
		        	NEIClientConfig.saveConfig();
				}
			};
			deleteButtons[i] = new Button("x")
			{
				@Override
				public boolean onButtonPress(boolean rightclick)
				{
					if(!rightclick)
					{
						NEIClientConfig.clearState(savestate);
						return true;
					}
					return false;
				}
			};
		}
		
		delete = new Button()
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if((state & 0x3) == 2)
					return false;
				
				ItemStack held = NEIClientUtils.getHeldItem();
	            if(held != null)
	            {
	                if(NEIClientUtils.shiftKey())
	                {
	                    NEIClientUtils.deleteHeldItem();
	                    NEIClientUtils.deleteItemsOfType(held);
	                } 
	                else if(rightclick)
	                	NEIClientUtils.decreaseSlotStack(-999);
	                else
	                    NEIClientUtils.deleteHeldItem();
	            } 
				else if(NEIClientUtils.shiftKey())
	        		NEIClientUtils.deleteEverything();
	            else
	                NEIController.deleteMode = !NEIController.deleteMode;
	            
	            return true;
			}
			
			public String getButtonTip() 
			{
				if((state & 0x3) == 2)
	            	return null;
	        	
	            ItemStack itemstack = NEIClientUtils.getHeldItem();
	            if(itemstack == null)
	            {
	                if(NEIClientUtils.shiftKey())
	                	return "DELETE ALL ITEMS from current inventory screen";
	                else
	        			return getStateTip("Delete Mode", state);
	            } 
	            else if(NEIClientUtils.shiftKey())
	            	return "DELETE ALL " + GuiContainerManager.itemDisplayNameShort(itemstack);
	            else
	            	return "DELETE " + GuiContainerManager.itemDisplayNameShort(itemstack);
			};
		};
		creative = new Button2ActiveState()
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
					NEIClientUtils.cycleCreativeMode();
					return true;
				}
				return false;
			}
			
			public String getButtonTip() 
			{
				return getStateTip("Creative Mode", state);
			}
		};
		rain = new Button()
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(handleDisabledButtonPress("rain", rightclick))
					return true;
				
				if(!rightclick)
				{
		            NEIClientUtils.toggleRaining();
		            return true;
				}
				return false;
			}
			
			public String getButtonTip() 
			{
				return getStateTip("Rain", state);
			}
		};
		magnet = new Button()
		{
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
		            NEIClientUtils.toggleMagnetMode();
					return true;
				}
	            return false;
			}
			
			public String getButtonTip() 
			{
				return getStateTip("Magnet Mode", state);
			}
		};
		for(int i = 0; i < 4; i++)
		{
			final int zone = i;
			timeButtons[i] = new Button()
			{				
				@Override
				public boolean onButtonPress(boolean rightclick)
				{
					if(handleDisabledButtonPress(timeZones[zone], rightclick))
						return true;
					
					if(!rightclick)
					{
						NEIClientUtils.setHourForward(zone*6);
						return true;
					}
					return false;
				}
				
				@Override
				public String getButtonTip()
				{
		            return getTimeTip(timeZones[zone], timeZones[(zone+1)%4], state);
				}
				
			};
		}
		heal = new Button()
		{			
			@Override
			public boolean onButtonPress(boolean rightclick)
			{
				if(!rightclick)
				{
		            NEIClientUtils.healPlayer();
		            return true;
				}
				return false;
			}
			
			@Override
			public String getButtonTip()
			{
	            return "Heal the player";
			}
		};

		delete.state |= 0x4;
		creative.state |= 0x4;
		rain.state |= 0x4;
		magnet.state |= 0x4;
	}

    private static String getStateTip(String name, int state)
    {
    	if((state & 0x3) == 2)
    		return "Enable "+name;
    	else
    		return "Turn "+name+((state & 0x3) == 1 ? " OFF" : " ON");
    }
	
    private static String getTimeTip(String zone, String next, int state)
    {
    	return (state & 0x3) == 2 ? "Enable "+zone+"-"+next : "Set time to "+zone;
    }

    private static boolean handleDisabledButtonPress(String ident, boolean rightclick)
	{
    	if(!AllowedPropertyMap.nameSet.contains(ident))
    		return false;
		if(rightclick && !NEIClientConfig.isPropertyDisabled(ident))
			return setPropertyDisabled(ident, true);
		if(!rightclick && NEIClientConfig.isPropertyDisabled(ident))
			return setPropertyDisabled(ident, false);
		return false;
	}
    
	private static boolean setPropertyDisabled(String ident, boolean disable)
	{
		if(disable && AllowedPropertyMap.nameToIDMap.get(ident)<4)
		{
			int count = 0;
			for(int i = 0; i < 4; i++)
			{
				if(NEIClientConfig.isPropertyDisabled(AllowedPropertyMap.idToNameMap.get(i)))
					count++;
			}
			if(count == 3)
				return false;			
		}
		NEIClientConfig.setPropertyDisabled(ident, disable);
		return true;
	}
    
	@Override
	public void load(GuiContainer gui)
	{
		if(NEIClientConfig.isEnabled())
		{
			setInputFocused(null);
			
			ItemList.loadItems();
			overlayRenderer = null;
						
			getLayoutStyle().init();
			layout(gui);
		}
		
		NEIController.load(gui);
		
		if(checkCreativeInv(gui))
    	{
			if(gui.mc.currentScreen instanceof GuiContainerCreative)//makes cycle transititions seemless. Only closes if we are opening from fresh
				gui.mc.displayGuiScreen(null);
    		return;
    	}
	}
	
	@Override
	public void refresh(GuiContainer gui)
	{
	}
	
	public boolean checkCreativeInv(GuiContainer gui)
	{
		if(gui instanceof GuiContainerCreative && NEIClientConfig.invCreativeMode())
    	{
			ClientPacketHandler.sendCreativeInv(true);
    		return true;
    	}
    	else if(gui instanceof GuiExtendedCreativeInv && !NEIClientConfig.invCreativeMode())
    	{
    		ClientPacketHandler.sendCreativeInv(false);
    		return true;
    	}
		return false;
	}
	
	public static void updateWidgetVisiblities(GuiContainer gui, VisiblityData visiblity)
	{		
		drawWidgets = new TreeSet<Widget>(new WidgetZOrder(false));
		controlWidgets = new TreeSet<Widget>(new WidgetZOrder(true));

		if(!visiblity.showNEI)
			return;
		
		addWidget(options);
		if(visiblity.showItemPanel)
		{
			addWidget(itemPanel);
			addWidget(prev);
			addWidget(next);
			addWidget(pageLabel);
			if(NEIClientConfig.isActionPermissable(InterActionMap.ITEM))
			{
				addWidget(more);
				addWidget(less);
				addWidget(quantity);
			}
		}
		
		if(visiblity.showSearchSection)
		{
			addWidget(dropDown);
			addWidget(searchField);
		}
		
		if(NEIClientConfig.isActionPermissable(InterActionMap.ITEM) && visiblity.showStateButtons)
        {
        	for(int i = 0; i < 7; i++)
        	{
        		addWidget(stateButtons[i]);
                if(NEIClientConfig.isStateSaved(i))
                	addWidget(deleteButtons[i]);
        	}
        }
		if(visiblity.showUtilityButtons)
		{
			if(NEIClientConfig.isActionPermissable(InterActionMap.TIME))
			{
				for(int i = 0; i < 4; i++)
					addWidget(timeButtons[i]);
			}
			if(NEIClientConfig.isActionPermissable(InterActionMap.RAIN))
				addWidget(rain);
			if(NEIClientConfig.isActionPermissable(InterActionMap.HEAL))
				addWidget(heal);
			if(NEIClientConfig.isActionPermissable(InterActionMap.MAGNET))
				addWidget(magnet);
			if(NEIClientConfig.isActionPermissable(InterActionMap.CREATIVE))
				addWidget(creative);
			if(NEIClientConfig.isActionPermissable(InterActionMap.DELETE))
				addWidget(delete);
		}
	}
	
	public static LayoutStyle getLayoutStyle()
	{
		LayoutStyle style = layoutStyles.get(NEIClientConfig.getLayoutStyle());
		if(style == null)
			style = layoutStyles.get(0);
		return style;
	}
	
	private static void addWidget(Widget widget)
	{
		drawWidgets.add(widget);
		controlWidgets.add(widget);
	}
	
	public void guiTick(GuiContainer gui)
    {     	
    	if(checkCreativeInv(gui))
    		return;
    	
    	if(!NEIClientConfig.isEnabled())
            return;
    	
        for(Widget widget : controlWidgets)
        	widget.update(gui.manager);
    }
    
	public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
	{
    	if(NEIClientConfig.isHidden() || !NEIClientConfig.isEnabled())
            return false;
    	
        for(Widget widget : controlWidgets)
        	if(widget.onMouseWheel(scrolled, mousex, mousey))
        		return true;
        
        return NEIController.mouseScrolled(scrolled);
	}
	
	@Override
	public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
	{
	}
	
	@Override
	public boolean shouldShowTooltip(GuiContainer gui)
	{
		return itemPanel.draggedStack == null;
	}
	
	public static Widget getInputFocused()
	{
		return inputFocused;
	}
	
	public static void setInputFocused(Widget widget)
	{
		if(inputFocused != null)
			inputFocused.loseFocus();
		
		inputFocused = widget;
		if(inputFocused != null)
			inputFocused.gainFocus();
	}
		
	@Override
	public void renderSlotUnderlay(GuiContainer gui, Slot slot)
	{
		if(overlayRenderer != null)
			overlayRenderer.renderOverlay(gui.manager, slot);		
	}
	
	@Override
	public void renderSlotOverlay(GuiContainer window, Slot slot)
	{
		ItemStack item = slot.getStack();
		if(NEIClientConfig.getBooleanSetting("options.searchinventories") && (item == null ? !NEIClientConfig.getSearchExpression().equals("") : !ItemList.itemMatchesSearch(item)))
    	{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0, 0, 150);
            window.manager.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, 0x80000000);
			GL11.glTranslatef(0, 0, -150);
			GL11.glEnable(GL11.GL_LIGHTING);
    	}
	}
		
	public static void drawIcon(GuiContainer window, int x, int y, Image image)
    {
        window.manager.bindTextureByName("/codechicken/nei/nei_sprites.png");
    	GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        window.drawTexturedModalRect(x, y, image.x, image.y, image.width, image.height);
        GL11.glDisable(GL11.GL_BLEND);
    }
	
    public static void drawButtonBackground(GuiContainerManager manager, int x, int y, int w, int h, boolean edges, int type)
    {
    	int wtiles = 0;
    	int ew = w;//end width
    	if(w / 2 > 100)
    	{
    		wtiles = (w - 200) / 50 + 1;
    		ew = 200;
    	}
    	
    	int w1 = ew / 2;
    	int h1 = h / 2;
    	int w2 = (ew + 1) / 2;
    	int h2 = (h + 1) / 2;
    	
    	int x2 = x + w - w2;
    	int y2 = y + h - h2;
    	
    	int ty = 46 + type * 20;
    	int te = (edges ? 0 : 1);//tex edges
    	
    	int ty1 = ty + te;
    	int tx1 = te;
    	int tx3 = 75;
    	//halfway the 1 is for odd number adjustment
    	int ty2 = ty + 20 - h2 - te;
    	int tx2 = 200 - w2 - te;

    	manager.bindTextureByName("/gui/gui.png");	
    	manager.drawTexturedModalRect(x , y , tx1, ty1, w1, h1);//top left
    	manager.drawTexturedModalRect(x , y2, tx1, ty2, w1, h2);//bottom left
        
        for(int tile = 0; tile < wtiles; tile++)
        {
        	int tilex = x + w1 + 50*tile;
        	manager.drawTexturedModalRect(tilex, y , tx3, ty1, 50, h1);//top
        	manager.drawTexturedModalRect(tilex, y2, tx3, ty2, 50, h2);//bottom
        }
        
        manager.drawTexturedModalRect(x2, y , tx2, ty1, w2, h1);//top right
        manager.drawTexturedModalRect(x2, y2, tx2, ty2, w2, h2);//bottom right
    }

	public static LayoutManager instance()
	{
		return instance;
	}
}
