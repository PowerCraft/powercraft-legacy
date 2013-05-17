package powercraft.modloader.testmodule;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;

@PC_BlockInfo(name = "Test Rotate", canPlacedRotated = true)
public class PCmltm_BlockTestRotate extends PC_Block {
	
	private boolean on;
	
	public PCmltm_BlockTestRotate(int id) {
		super(id, Material.ground);
	}
	
	@Override
	public void onIconLoading() {
		sideIcons = new Icon[6];
		for (int i = 0; i < 6; i++) {
			sideIcons[i] = Block.blocksList[i + 1].getIcon(i, 0);
		}
	}
	
	@Override
	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		if (dir == PC_Direction.RIGHT)
			return on?15:0;
		return 0;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		System.out.println(getRotation(PC_Utils.getMD(world, x, y, z)));
		for (int i = 0; i < 6; i++) {
			System.out.println(PC_Direction.getFormMCSide(i) + ":" + getRedstonePowerValueFromInput(world, x, y, z, PC_Direction.getFormMCSide(i)));
		}
		boolean newOn = getRedstonePowerValueFromInput(world, x, y, z, PC_Direction.FRONT)>0;
		if(newOn!=on){
			on=newOn;
			PC_Utils.hugeUpdate(world, x, y, z);
		}
	}
	
}
