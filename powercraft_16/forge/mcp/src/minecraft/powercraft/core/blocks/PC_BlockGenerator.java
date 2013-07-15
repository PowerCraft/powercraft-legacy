package powercraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_TileEntity;

@PC_BlockInfo(name="Generator", blockid="generator", defaultid=3001, tileEntity = PC_TileEntityGenerator.class)
public class PC_BlockGenerator extends PC_Block {

	private Icon frontOn;
	private Icon frontOff;
	
	public PC_BlockGenerator(int id) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {
		blockIcon = PC_ClientRegistry.registerIcon("DefaultMaschineTexture");
		frontOn = PC_ClientRegistry.registerIcon("Front_On");
		frontOff = PC_ClientRegistry.registerIcon("Front_Off");
	}

	@Override
	public void registerRecipes() {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		if(side==2){
			PC_TileEntityGenerator generator = PC_Utils.getTE(world, x, y, z);
			if(generator!=null && generator.isActive()){
				return frontOn;
			}else{
				return frontOff;
			}
		}else{
			return blockIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		if(side==2)
			return frontOff;
		return blockIcon;
	}
	
}
