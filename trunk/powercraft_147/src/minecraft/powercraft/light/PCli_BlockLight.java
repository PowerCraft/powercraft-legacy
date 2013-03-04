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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.launcher.PC_Property;
import powercraft.management.PC_Color;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.annotation.PC_Shining.OFF;
import powercraft.management.annotation.PC_Shining.ON;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_IItemInfo;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.renderer.PC_Renderer;

@PC_Shining
@PC_BlockInfo(tileEntity=PCli_TileEntityLight.class)
public class PCli_BlockLight extends PC_Block implements PC_IItemInfo
{
    @ON
    public static PCli_BlockLight on;
    @OFF
    public static PCli_BlockLight off;

    public PCli_BlockLight(int id, boolean on)
    {
        super(id, 66, Material.glass);
        setHardness(0.3F);
        setResistance(20F);
        setStepSound(Block.soundStoneFootstep);
        setRequiresSelfNotify();

        if (on)
        {
            setCreativeTab(CreativeTabs.tabDecorations);
        }
    }

    @Override
	public boolean showInCraftingTool() {
    	if(this==on)
			return true;
		return false;
	}
    
    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCli_TileEntityLight();
    }

    @Override
    public int getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
    {
        PCli_TileEntityLight te = GameInfo.getTE(par1iBlockAccess, par2, par3, par4);

        if (te == null) return
                    super.getBlockTexture(par1iBlockAccess, par2, par3, par4, par5);

        if (!te.isHuge()) return
                    66;

        return 117;
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
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving player)
    {
        PCli_TileEntityLight tileentity = GameInfo.getTE(world, i, j, k);

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

        PCli_TileEntityLight te = GameInfo.<PCli_TileEntityLight>getTE(world, i, j, k, blockID);
        
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
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        PCli_TileEntityLight tileentity = GameInfo.getTE(world, i, j, k, blockID);

        if (tileentity == null || tileentity.isStable())
        {
            return;
        }

        boolean powered = world.isBlockIndirectlyGettingPowered(i, j, k)||world.isBlockGettingPowered(i, j, k) ;

        if (tileentity.isActive() != powered)
        {
            world.scheduleBlockUpdate(i, j, k, blockID, 1);
        }
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        PCli_TileEntityLight tileentity = GameInfo.getTE(world, i, j, k);

        if (tileentity == null || tileentity.isStable())
        {
            return;
        }

        boolean powered = world.isBlockGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j, k);

        if (tileentity.isActive() != powered)
        {
            onPoweredBlockChange(world, i, j, k, powered);
        }
    }

    private boolean isAttachmentBlockPowered(World world, int x, int y, int z, int side)
    {
        if (side == 0)
        {
            return world.isBlockGettingPowered(x, y - 1, z) && world.getBlockId(x, y - 1, x) != 0;
        }

        if (side == 1)
        {
            return world.isBlockGettingPowered(x, y, z + 1) && world.getBlockId(x, y, x + 1) != 0;
        }

        if (side == 2)
        {
            return world.isBlockGettingPowered(x, y, z - 1) && world.getBlockId(x, y, x - 1) != 0;
        }

        if (side == 3)
        {
            return world.isBlockGettingPowered(x + 1, y, z) && world.getBlockId(x + 1, y, x) != 0;
        }

        if (side == 4)
        {
            return world.isBlockGettingPowered(x - 1, y, z) && world.getBlockId(x - 1, y, x) != 0;
        }

        if (side == 5)
        {
            return world.isBlockGettingPowered(x, y + 1, z) && world.getBlockId(x, y + 1, x) != 0;
        }

        return false;
    }

    public static void onPoweredBlockChange(World world, int x, int y, int z, boolean rs_state)
    {
        PCli_TileEntityLight tileentity = GameInfo.getTE(world, x, y, z);

        if ((tileentity == null || tileentity.isStable()) && rs_state == false)
        {
            return;
        }

        ValueWriting.setBlockState(world, x, y, z, rs_state);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        PCli_TileEntityLight te = GameInfo.getTE(iblockaccess, i, j, k);

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
        PCli_TileEntityLight tei = GameInfo.getTE(w, i, j, k);

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
        PCli_TileEntityLight tei = GameInfo.getTE(world, i, j, k);

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

    public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer)
    {
        PC_Renderer.swapTerrain(block);
        float sidehalf = 0.1875F;
        float height = 0.15F;
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
        ValueWriting.setBlockBounds(block, 0.5F - sidehalf, 0.5F - sidehalf, 0.5F - height / 2F, 0.5F + sidehalf, 0.5F + sidehalf, 0.5F + height / 2F);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 66);
        PC_Renderer.resetTerrain(true);
    }

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_LOAD_FROM_CONFIG:
			on.setLightValue(((PC_Property)obj[0]).getInt("brightness", 15) * 0.0625F);
			break;
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			break;
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Light";
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
			list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		default:
			return null;
		}
		return true;
	}
    
}
