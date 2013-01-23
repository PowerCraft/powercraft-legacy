package codechicken.nei.recipe;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Objects;

import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IStackPositioner;

public class RecipeInfo
{
    private static class OverlayValue
    {
        public OverlayValue(IOverlayHandler handler, int mode)
        {
            this.handler = handler;
            this.mode = mode;
        }
        
        IOverlayHandler handler;
        int mode;
    }
    
    private static class OverlayKey
    {
        String ident;
        Class<? extends GuiContainer> guiClass;
        
        public OverlayKey(Class<? extends GuiContainer> classz, String ident)
        {
            this.guiClass = classz;
            this.ident = ident;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(!(obj instanceof OverlayKey))
                return false;
            OverlayKey key = (OverlayKey)obj;
            return ident.equals(key.ident) && guiClass == key.guiClass;
        }
        
        @Override
        public int hashCode()
        {
            return Objects.hashCode(ident, guiClass);
        }
    }
    
    static HashMap<OverlayKey, IOverlayHandler> overlayMap = new HashMap<OverlayKey, IOverlayHandler>();
    static HashMap<OverlayKey, IStackPositioner> positionerMap = new HashMap<OverlayKey, IStackPositioner>();
    
    public static void registerOverlayHandler(Class<? extends GuiContainer> classz, IOverlayHandler handler, String ident)
    {
        overlayMap.put(new OverlayKey(classz, ident), handler);
    }
        
    public static void registerGuiOverlay(Class<? extends GuiContainer> classz, String ident, IStackPositioner positioner)
    {
        positionerMap.put(new OverlayKey(classz, ident), positioner);
    }

    public static boolean hasDefaultOverlay(GuiContainer gui, String ident)
    {
        return positionerMap.containsKey(new OverlayKey(gui.getClass(), ident));
    }

    public static boolean hasOverlayHandler(GuiContainer gui, String ident)
    {
        return overlayMap.containsKey(new OverlayKey(gui.getClass(), ident));
    }

    public static IOverlayHandler getOverlayHandler(GuiContainer gui, String ident)
    {
        return overlayMap.get(new OverlayKey(gui.getClass(), ident));
    }

    public static IStackPositioner getStackPositioner(GuiContainer gui, String ident)
    {
        return positionerMap.get(new OverlayKey(gui.getClass(), ident));
    }

    public static void load()
    {
        API.registerRecipeHandler(new ShapedRecipeHandler());
        API.registerUsageHandler(new ShapedRecipeHandler());
        API.registerRecipeHandler(new ShapelessRecipeHandler());
        API.registerUsageHandler(new ShapelessRecipeHandler());
        API.registerRecipeHandler(new FireworkRecipeHandler());
        API.registerUsageHandler(new FireworkRecipeHandler());
        API.registerRecipeHandler(new FurnaceRecipeHandler());
        API.registerUsageHandler(new FurnaceRecipeHandler());
        API.registerRecipeHandler(new BrewingRecipeHandler());
        API.registerUsageHandler(new BrewingRecipeHandler());
        API.registerRecipeHandler(new FuelRecipeHandler());
        API.registerUsageHandler(new FuelRecipeHandler());

        API.registerGuiOverlay(GuiCrafting.class, "crafting");
        API.registerGuiOverlay(GuiInventory.class, "crafting2x2", 63, 20);
        API.registerGuiOverlay(GuiFurnace.class, "smelting");
        API.registerGuiOverlay(GuiFurnace.class, "fuel");
        API.registerGuiOverlay(GuiBrewingStand.class, "brewing");

        API.registerGuiOverlayHandler(GuiCrafting.class, new DefaultOverlayHandler(), "crafting");
        API.registerGuiOverlayHandler(GuiBrewingStand.class, new BrewingOverlayHandler(), "brewing");
    }
}
