package powercraft.logic;

import java.util.Random;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.core.PC_Block;

public class PClo_BlockGate extends PC_Block {

	public static final int getMaxGates = 1;
	public static final int AND2 = 0;
	
	public PClo_BlockGate(int id){
		super(id, Material.ground);
	}
	
	@Override
	public String getDefaultName() {
		return null;
	}

	public static boolean getGateOutput(int gate, boolean i1, boolean i2, boolean i3){
		switch(gate){
		case AND2:
			return i1 && i2;
		default:
			return false;
		}
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		// TODO Auto-generated method stub
		super.updateTick(par1World, par2, par3, par4, par5Random);
	}

	@Override
	public boolean isPoweringTo(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		// TODO Auto-generated method stub
		return super.isPoweringTo(par1iBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World par1World, int par2, int par3, int par4, int par5) {
		// TODO Auto-generated method stub
		return super.isIndirectlyPoweringTo(par1World, par2, par3, par4, par5);
	}
	
	
	
}
