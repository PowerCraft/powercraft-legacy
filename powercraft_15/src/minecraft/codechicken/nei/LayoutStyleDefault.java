package codechicken.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.nei.api.LayoutStyle;
import codechicken.nei.forge.GuiContainerManager;

import static codechicken.nei.LayoutManager.*;

public abstract class LayoutStyleDefault extends LayoutStyle
{	
	@Override
	public void layout(GuiContainer gui, VisiblityData visiblity)
	{
		int windowWidth = gui.width;
		int windowHeight = gui.height;
		int containerWidth = gui.xSize;
		//int containerHeight = gui.ySize;
		int containerLeft = gui.guiLeft;
		//int containerTop = gui.guiTop;
        
        reset();
		
		prev.y = 2;
		prev.height = 16;
        prev.width = containerLeft/3;
    	prev.x = (containerWidth + windowWidth) / 2 + 2;
        next.x = windowWidth-prev.width-2;
        
        next.y = prev.y;
        next.width = prev.width;
        next.height = prev.height;
        pageLabel.x = containerLeft*3/2+containerWidth+1;
        pageLabel.y = prev.y+5;
        if(itemPanel.getNumPages() == 0)
        	pageLabel.text = "(0/0)";
        else
        	pageLabel.text = "("+(itemPanel.getPage()+1)+"/"+itemPanel.getNumPages()+")";
        
        itemPanel.y = prev.height+prev.y;
    	itemPanel.x = (containerWidth + windowWidth) / 2 + 3;
        itemPanel.width = windowWidth - 3 - itemPanel.x;
        itemPanel.height = windowHeight - 15 - itemPanel.y;
        if(!NEIClientConfig.isActionPermissable(InterActionMap.ITEM))
        	itemPanel.height+=15;
        itemPanel.resize();
        
        more.width = more.height = less.width = less.height = 16;
        less.x = prev.x;
        more.x = windowWidth-less.width-2;
        more.y = less.y = windowHeight-more.height-2;
        
        quantity.x = less.x+less.width+2;
        quantity.y = less.y;
        quantity.width = more.x-quantity.x-2;
        quantity.height = less.height;
        		
        options.x = NEIClientConfig.isEnabled() ? 0 : 6;
        options.y = NEIClientConfig.isEnabled() ? windowHeight - 22 : windowHeight - 28;
        options.width = 80;
        options.height = 22;

        delete.state = 0x4;
        if(NEIController.deleteMode)
        	delete.state |= 1;
        else if(!visiblity.enableDeleteMode)
        	delete.state |= 2;
        
        rain.state = 0x4;
        if(NEIClientUtils.isRaining())
        	rain.state |= 1;
        else if(NEIClientConfig.isPropertyDisabled("rain"))
        	rain.state |= 2;
        
        creative.state = 0x4;
        if(NEIClientUtils.getCreativeMode() != 0)
        	creative.state |= 0x1;
        if(NEIClientUtils.getCreativeMode() == 2)
        	creative.state |= 0x8;
        
        magnet.state = 0x4 | (NEIClientConfig.getMagnetMode() ? 1 : 0);
        
		if(NEIClientConfig.isActionPermissable(InterActionMap.DELETE))
			layoutButton(delete, gui.manager);
		if(NEIClientConfig.isActionPermissable(InterActionMap.RAIN))
			layoutButton(rain, gui.manager);
		if(NEIClientConfig.isActionPermissable(InterActionMap.CREATIVE))
			layoutButton(creative, gui.manager);
		if(NEIClientConfig.isActionPermissable(InterActionMap.MAGNET))
			layoutButton(magnet, gui.manager);
		if(NEIClientConfig.isActionPermissable(InterActionMap.TIME))
		{
	        for(int i = 0; i < 4; i++)
	        {
	        	timeButtons[i].state = NEIClientConfig.isPropertyDisabled(timeZones[i]) ? 2 : 0;
		        layoutButton(timeButtons[i], gui.manager);
	        }
		}
		if(NEIClientConfig.isActionPermissable(InterActionMap.HEAL))
			layoutButton(heal,gui.manager);
		
        searchField.y = windowHeight - searchField.height - 2;
                
        dropDown.height = 20;
    	dropDown.x = NEIClientConfig.getLayoutStyle() == 1 ? 93 : 90;
    	dropDown.width = prev.x - dropDown.x - 3;
        searchField.height = 20;
        searchField.width = 150;
    	searchField.x = (windowWidth - searchField.width) / 2;
		
		if(!visiblity.showItemSection)
        {
        	dropDown.setDropDown(0);
        	searchField.setFocus(false);
        }
        
        int maxWidth = 0;
        for(int i = 0; i < 7; i++)
        {
            deleteButtons[i].width = 16;
            deleteButtons[i].height = 16;
            
            NBTTagCompound statelist = NEIClientConfig.saveCompound.getCompoundTag("statename");
        	NEIClientConfig.saveCompound.setTag("statename", statelist);
        	String name = statelist.getString(""+i);
            if(statelist.getTag(""+i) == null)
            {
            	name = ""+(i+1);
            	statelist.setString(""+i, name);
            }
            stateButtons[i].label = (NEIClientConfig.isStateSaved(i) ? "Load " : "Save ") + name;
            
            int width = gui.manager.getStringWidth(stateButtons[i].label) + 26;
            if(width + 22 > containerLeft)
            {
                width = containerLeft - 22;
            }
            if(width > maxWidth)
            {
                maxWidth = width;
            }
        }

        for(int i = 0; i < 7; i++)
        {
        	stateButtons[i].x = 0;
            stateButtons[i].y = 58 + i * 22;
            stateButtons[i].height = 20;
        	
    		stateButtons[i].x = 0;
            stateButtons[i].width = maxWidth;
            deleteButtons[i].x = stateButtons[i].width + 3;
            deleteButtons[i].y = stateButtons[i].y + 2;
        }
	}

	public abstract void layoutButton(Button button, GuiContainerManager gui);
}
