package powercraft.core.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_TileEntity;

@PC_BlockInfo(name="Roaster", blockid="roaster", defaultid=3003, tileEntity=PC_TileEntityRoaster.class)
public class PC_BlockRoaster extends PC_Block {

	private Icon topIcon;
	private Icon bottomIcon;
	
	public PC_BlockRoaster(int id) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {
		blockIcon = PC_ClientRegistry.registerIcon("Side");
		topIcon = PC_ClientRegistry.registerIcon("Top");
		bottomIcon = PC_ClientRegistry.registerIcon("Bottom");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		if(side==0)
			return bottomIcon;
		if(side==1)
			return topIcon;
		return blockIcon;
	}

	@Override
	public void registerRecipes() {
		// TODO Auto-generated method stub

	}
	
	@Override
    public boolean isOpaqueCube(){
        return false;
    }

	@Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		PC_TileEntityRoaster ter = PC_Utils.getTE(world, i, j, k);
        if (ter.isActive())
        {
            if (random.nextInt(24) == 0)
            {
                world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "fire.fire", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
            }

            for (int c = 0; c < 5; c++)
            {
                float y = j + 0.74F + (random.nextFloat() * 0.3F);
                float x = i + 0.2F + (random.nextFloat() * 0.6F);
                float z = k + 0.2F + (random.nextFloat() * 0.6F);
                world.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", x, y, z, 0.0D, 0.0D, 0.0D);
            }

            for (int c = 0; c < 5; c++)
            {
                float y = j + 1.3F;
                float x = i + 0.2F + (random.nextFloat() * 0.6F);
                float z = k + 0.2F + (random.nextFloat() * 0.6F);
                world.spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }

    }
	
}
