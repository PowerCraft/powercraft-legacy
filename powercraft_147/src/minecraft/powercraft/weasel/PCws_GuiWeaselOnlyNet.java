package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresTab;
import powercraft.management.PC_IGresGui;

public class PCws_GuiWeaselOnlyNet extends PCws_GuiWeasel {

	public PCws_GuiWeaselOnlyNet(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	protected void addTabs(PC_GresTab tab) {}

}
