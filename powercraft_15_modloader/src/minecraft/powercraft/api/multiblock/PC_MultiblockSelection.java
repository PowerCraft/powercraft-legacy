package powercraft.api.multiblock;

import net.minecraft.src.AxisAlignedBB;
import powercraft.api.utils.PC_VecI;

public class PC_MultiblockSelection {

	public PC_VecI selectPos;
	public PC_FractionSide side;
	
	public PC_MultiblockSelection(PC_VecI selectPos, PC_FractionSide side) {
		this.selectPos = selectPos;
		this.side = side;
	}
	
}
