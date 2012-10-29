package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SideOnly(Side.SERVER)
class GuiStatsListener implements ActionListener
{
    final GuiStatsComponent statsComponent;

    GuiStatsListener(GuiStatsComponent par1GuiStatsComponent)
    {
        this.statsComponent = par1GuiStatsComponent;
    }

    public void actionPerformed(ActionEvent par1ActionEvent)
    {
        GuiStatsComponent.update(this.statsComponent);
    }
}
