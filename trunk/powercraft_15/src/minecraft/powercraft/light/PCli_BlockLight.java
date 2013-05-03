package powercraft.light;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Color;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="Light", tileEntity=PCli_TileEntityLight.class)
public class PCli_BlockLight extends PC_Block implements PC_IItemInfo
{
    @ON
    public static PCli_BlockLight on;
    @OFF
    public static PCli_BlockLight off;

    public PCli_BlockLight(int id, boolean on)
    {
        super(id, Material.glass, "lightsmall", "lighthuge");
        setHardness(0.3F);
        setResistance(20F);
        setStepSound(Block.soundStoneFootstep);

        if (on)
        {
            setCreativeTab(CreativeTabs.tabDecorations);
        }
    }
    
    @Override
	public void initConfig(PC_Property config) {
		super.initConfig(config);
		on.setLightValue(config.getInt("brightness", 15) * 0.0625F);
	}

	@Override
    public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, PC_Direction par5){
        PCli_TileEntityLight te = PC_Utils.getTE(par1iBlockAccess, par2, par3, par4);

        if (te == null) return
                    super.getBlockTexture(par1iBlockAccess, par2, par3, par4, par5);

        if (!te.isHuge()) 
        	return sideIcons[0];

        return sideIcons[1];
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
        return true;
    }
    
    @Override
	public int onBlockPlaced(World world, int x, int y, int z, int l, float par6, float par7, float par8, int par9) {
    	int metadata = 0;
		if (l == 2) {
			metadata = 1;
		} else if (l == 3) {
			metadata = 2;
		} else if (l == 4) {
			metadata = 3;
		} else if (l == 5) {
			metadata = 4;
		}else if (l == 0) {
			metadata = 5;
		}
		return metadata;
    }
    
	@Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving player, ItemStack itemStack)
    {
        PCli_TileEntityLight tileentity = PC_Utils.getTE(world, i, j, k);

        if (tileentity != null && tileentity.isStable())
        {
            return;
        }

        //onPoweredBlockChange(world, i, j, k, world.isBlockIndirectlyGettingPowered(i, j, k));
        onNeighborBlockChange(world, i, j, k, 0);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return true;
    }

    private int[] meta2side = { 1, 2, 3, 4, 5, 0 };

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = entityplayer.getCurrentEquippedItem();

        PCli_TileEntityLight te = PC_Utils.<PCli_TileEntityLight>getTE(world, i, j, k);
        
        if (ihold != null)
        {
            if (ihold.getItem().itemID == Item.dyePowder.itemID)
            {
                if(te!=null){
                	Integer hex = PC_Color.getHexColorForName(ItemDye.dyeColorNames[ihold.getItemDamage()]);
                	if(hex!=null)
                		te.setColor(new PC_Color(hex));
                	return true;
                }
            }
        }

        PC_GresRegistry.openGres("Light", entityplayer, te);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        PCli_TileEntityLight tileentity = PC_Utils.getTE(world, x, y, z);

        if (tileentity == null || tileentity.isStable())
        {
            return;
        }

        boolean powered = PC_Utils.getBlockRedstonePowereValue(world, x, y, z)>0;

        if (tileentity.isActive() != powered)
        {
            world.scheduleBlockUpdate(x, y, z, blockID, 1);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        PCli_TileEntityLight tileentity = PC_Utils.getTE(world, x, y, z);

        if (tileentity == null || tileentity.isStable())
        {
            return;
        }

        boolean powered = PC_Utils.getBlockRedstonePowereValue(world, x, y, z)>0;

        if (tileentity.isActive() != powered)
        {
            onPoweredBlockChange(world, x, y, z, powered);
        }
    }

    public static void onPoweredBlockChange(World world, int x, int y, int z, boolean rs_state)
    {
        PCli_TileEntityLight tileentity = PC_Utils.getTE(world, x, y, z);

        if ((tileentity == null || tileentity.isStable()) && rs_state == false)
        {
            return;
        }

        PC_Utils.setBlockState(world, x, y, z, rs_state);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	TileEntity te = PC_Utils.getTE(par1World, par2, par3, par4);
    	if(te instanceof PCli_TileEntityLight){
    		setBlockBoundsBasedOnState(par1World, par2, par3, par4);
    	}
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        PCli_TileEntityLight te = PC_Utils.getTE(iblockaccess, i, j, k);

        if (te == null)
        {
            return;
        }

        float sidehalf = te.isHuge() ? 0.5F : 0.1875F;
        float height = te.isHuge() ? 0.0625F * 3 : 0.0625F * 2;

        if (i1 == 0)
        {
            setBlockBounds((float) 0.5 - sidehalf, 0, (float) 0.5 - sidehalf, (float) 0.5 + sidehalf, height, (float) 0.5 + sidehalf);
        }
        else if (i1 == 1)
        {
            setBlockBounds((float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 1 - height, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf, 1);
        }
        else if (i1 == 2)
        {
            setBlockBounds((float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 0, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf, height);
        }
        else if (i1 == 3)
        {
            setBlockBounds(1 - height, (float) 0.5 - sidehalf, (float) 0.5 - sidehalf, 1, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf);
        }
        else if (i1 == 4)
        {
            setBlockBounds(0, (float) 0.5 - sidehalf, (float) 0.5 - sidehalf, height, (float) 0.5 + sidehalf, (float) 0.5 + sidehalf);
        }

        if (i1 == 5)
        {
            setBlockBounds((float) 0.5 - sidehalf, 1 - height, (float) 0.5 - sidehalf, (float) 0.5 + sidehalf, 1, (float) 0.5 + sidehalf);
        }
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
    }

    private PC_Color getColor(IBlockAccess w, int i, int j, int k)
    {
        PCli_TileEntityLight tei = PC_Utils.getTE(w, i, j, k);

        if (tei == null)
        {
            return null;
        }

        return tei.getColor();
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return -1;
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        PCli_TileEntityLight tei = PC_Utils.getTE(world, i, j, k);

        if (!tei.isActive())
        {
            return;
        }

        try
        {
            if (tei.isHuge())
            {
                return;
            }

            if (tei.isStable() && world.rand.nextInt(3) != 0)
            {
                return;
            }
        }
        catch (NullPointerException e) {}

        int l = world.getBlockMetadata(i, j, k);
        PC_Color color = getColor(world, i, j, k);

        if (color == null)
        {
            return;
        }

        double ii = i + 0.5D;
        double jj = j + 0.5D;
        double kk = k + 0.5D;
        double h = 0.22D;
        double r = color.x, g = color.y, b = color.z;
        r = (r == 0) ? 0.001D : r;
        g = (g == 0) ? 0.001D : g;
        b = (b == 0) ? 0.001D : b;

        if (l == 0)
        {
            world.spawnParticle("reddust", ii, j + h, kk, r, g, b);
        }
        else if (l == 1)
        {
            world.spawnParticle("reddust", ii, jj, k + 1 - h, r, g, b);
        }
        else if (l == 2)
        {
            world.spawnParticle("reddust", ii, jj, k + h, r, g, b);
        }
        else if (l == 3)
        {
            world.spawnParticle("reddust", i + 1 - h, jj, kk, r, g, b);
        }
        else if (l == 4)
        {
            world.spawnParticle("reddust", i + h, jj, kk, r, g, b);
        }

        if (l == 5)
        {
            world.spawnParticle("reddust", i, jj + 1 - h, kk, r, g, b);
        }
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

    public boolean renderInventoryBlock(int metadata, Object renderer) {
        float sidehalf = 0.1875F;
        float height = 0.15F;
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
        setBlockBounds(0.5F - sidehalf, 0.5F - sidehalf, 0.5F - height / 2F, 0.5F + sidehalf, 0.5F + sidehalf, 0.5F + height / 2F);
        PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[0]);
        return true;
    }
    
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}
    
}
