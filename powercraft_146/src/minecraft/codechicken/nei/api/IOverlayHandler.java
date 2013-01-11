package codechicken.nei.api;

import java.util.List;

import codechicken.nei.PositionedStack;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public interface IOverlayHandler
{
    public void overlayRecipe(GuiContainer firstGui, List<PositionedStack> ingredients, boolean shift);
}
