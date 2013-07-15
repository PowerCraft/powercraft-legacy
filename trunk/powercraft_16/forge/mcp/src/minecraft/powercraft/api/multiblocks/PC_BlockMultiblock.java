package powercraft.api.multiblocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Registry;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;

@PC_BlockInfo(name="Multiblock", blockid="multiblock", defaultid = 3000, tileEntity=PC_TileEntityMultiblock.class)
public class PC_BlockMultiblock extends PC_Block {

	public static PC_BlockMultiblock block;
	@SideOnly(Side.CLIENT)
	private static Icon[] icons;
	
	public PC_BlockMultiblock(int id) {
		super(id, Material.ground);
		block = this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {
		PC_MultiblockRegistry.loadIcons();
	}

	@Override
	public void registerRecipes() {
		
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, 0)!=null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		if(icons==null)
			return null;
		if(side>=icons.length)
			side = icons.length-1;
		return icons[side];
	}

	@SideOnly(Side.CLIENT)
	public static void setIcons(Icon...newIcons){
		icons = newIcons;
	}
	
}
