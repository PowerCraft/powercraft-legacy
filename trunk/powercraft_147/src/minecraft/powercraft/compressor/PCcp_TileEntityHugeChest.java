package powercraft.compressor;

import net.minecraft.util.AxisAlignedBB;
import powercraft.management.PC_TileEntity;

public class PCcp_TileEntityHugeChest extends PC_TileEntity {


	@Override
	public boolean canUpdate() {
		// TODO Auto-generated method stub
		return super.canUpdate();
	}
	
	@Override
	public void updateEntity() {
		AxisAlignedBB bb=AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(1, 1, 1);
		
	}

	
	
}
