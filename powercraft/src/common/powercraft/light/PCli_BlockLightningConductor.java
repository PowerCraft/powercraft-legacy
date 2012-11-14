package powercraft.light;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_Utils;
import powercraft.logic.mod_PowerCraftLogic;

public class PCli_BlockLightningConductor extends PC_Block {

	protected PCli_BlockLightningConductor(int id) {
		super(id, 22, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getDefaultName() {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if(metadata==1)
			return new PCli_TileEntityLightningConductor();
		return null;
	}

	@Override
	public boolean canBeHarvest() {
		return false;
	}
	
}
