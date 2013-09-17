package powercraft.tutorial;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockRotated;
import powercraft.api.registries.PC_TextureRegistry;
				//IngameName	internalName		defaultid		reference to tileentity class
@PC_BlockInfo(name="Tutorial", blockid="tutorial", defaultid=2000, tileEntity=PC_TileEntityTutorial.class)
public class PC_BlockTutorial extends PC_BlockRotated {

	
	public PC_BlockTutorial(int id) {
		super(id, Material.ice);
		setCreativeTab(CreativeTabs.tabBlock);
		
	}

	@Override
	public void loadIcons() {
		blockIcon = PC_TextureRegistry.registerIcon("Icon");
	}

	@Override
	public void registerRecipes() {
		
	}
	@Override
	public void onEntityCollidedWithBlock(World world, int x,
			int y, int z, Entity entity) {
		// TODO Auto-generated method stub
				
	}
	
	@Override
	public void onEntityWalking(World par1World, int par2,
			int par3, int par4, Entity par5Entity) {
		onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int side, float xHit,
			float yHit, float zHit) {		
		
		return true;
	}
	@Override
	public void onBlockClicked(World par1World, int par2,
			int par3, int par4, EntityPlayer par5EntityPlayer) {
		
	}
}
