package powercraft.api.renderer;

import net.minecraft.src.Icon;
import powercraft.api.renderer.PC_SpecialRenderer.CableNode;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_VecI;

public interface PC_ISpecialRenderer {

	public void drawBlock(PC_VecI pos, PC_Direction dir, Icon[] icons);
	public void drawCables(int cableMask, CableNode cableNode1, CableNode cableNode2, CableNode cableNode3, CableNode cableNode4);
	
}
