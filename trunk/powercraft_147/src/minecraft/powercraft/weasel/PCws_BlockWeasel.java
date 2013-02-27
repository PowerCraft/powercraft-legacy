package powercraft.weasel;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.block.PC_Block;
import powercraft.management.registry.PC_ItemRegistry;
import powercraft.management.registry.PC_KeyRegistry;
import powercraft.management.registry.PC_LangRegistry;
import powercraft.management.registry.PC_MSGRegistry;

@PC_BlockInfo(itemBlock=PCws_ItemBlockWeasel.class, tileEntity=PCws_TileEntityWeasel.class)
public class PCws_BlockWeasel extends PC_Block {

	public PCws_BlockWeasel(int id) {
		super(id, 6, Material.ground);
		setHardness(0.5F);
		setLightValue(0);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setRequiresSelfNotify();
		setResistance(60.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCws_TileEntityWeasel();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		PCws_TileEntityWeasel te = GameInfo.getTE(world, x, y, z);
		if(te!=null){
			float[] bounds = te.getPluginInfo().getBounds();
			setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int l) {

		if(!world.isRemote){
			getPlugin(world, x, y, z).refreshInport();
		}
		
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int dir) {
		PCws_WeaselPlugin weaselPlugin = GameInfo.<PCws_TileEntityWeasel>getTE(world, x, y, z).getPlugin();
		if(weaselPlugin!=null){
			
			int meta = GameInfo.getMD(world, x, y, z);
			int rotation = getRotation(meta);
			if(dir==0){
				return weaselPlugin.getOutport(4);
			}else if(dir==1){
				return weaselPlugin.getOutport(5);
			}else{
				if(rotation==0){
					if(dir==2){
						return weaselPlugin.getOutport(0);
					}else if(dir==3){
						return weaselPlugin.getOutport(3);
					}else if(dir==4){
						return weaselPlugin.getOutport(2);
					}else{
						return weaselPlugin.getOutport(1);
					}
				}else if(rotation==1){
					if(dir==2){
						return weaselPlugin.getOutport(2);
					}else if(dir==3){
						return weaselPlugin.getOutport(1);
					}else if(dir==4){
						return weaselPlugin.getOutport(3);
					}else{
						return weaselPlugin.getOutport(0);
					}
				}else if(rotation==2){
					if(dir==2){
						return weaselPlugin.getOutport(3);
					}else if(dir==3){
						return weaselPlugin.getOutport(0);
					}else if(dir==4){
						return weaselPlugin.getOutport(1);
					}else{
						return weaselPlugin.getOutport(2);
					}
				}else{
					if(dir==2){
						return weaselPlugin.getOutport(1);
					}else if(dir==3){
						return weaselPlugin.getOutport(2);
					}else if(dir==4){
						return weaselPlugin.getOutport(0);
					}else{
						return weaselPlugin.getOutport(3);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int dir) {
		return isProvidingWeakPower(world, x, y, z, dir);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();

		PCws_WeaselPlugin weaselPlugin = getPlugin(world, x, y, z);
		if (weaselPlugin == null) return false;
		
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().itemID];
				return false;
			} else if (ihold.getItem().itemID == PC_ItemRegistry.getPCItemIDByName("PCco_ItemActivator")) {

				if (weaselPlugin.getNetwork()==null||PC_KeyRegistry.isPlacingReversed(player)){
					if (ihold.hasTagCompound()) {
						String network = ihold.getTagCompound().getString("WeaselNetwork");
						weaselPlugin.connectToNetwork(PCws_WeaselManager.getNetwork(network));
						world.scheduleBlockUpdate(x, y, z, blockID, 1);

						Communication.chatMsg(PC_LangRegistry.tr("pc.weasel.activatorSetNetwork", new String[] { network }), true);
						world.playSoundEffect(x, y, z, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F, 0.5F);
					}
				}else{
					String network = weaselPlugin.getNetwork().getName();
					if (!ihold.hasTagCompound()) {
						ihold.setTagCompound(new NBTTagCompound());
					}
					ihold.getTagCompound().setString("WeaselNetwork", network);
					Communication.chatMsg(PC_LangRegistry.tr("pc.weasel.activatorGetNetwork", new String[] { network }), true);
				}
				return true;
			}
		}
		
        if (ihold != null)
        {
            if (ihold.getItem() instanceof ItemBlock)
            {
                if (ihold.itemID == blockID)
                {
                    return false;
                }
            }
        }

        weaselPlugin.openGui(player);
        
        return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player) {
		int l = PC_MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 2.5D) & 3;

        if (player instanceof EntityPlayer && PC_KeyRegistry.isPlacingReversed(((EntityPlayer)player)))
        {
            l = ValueWriting.reverseSide(l);
        }
        
        ValueWriting.setMD(world, x, y, z, l);
        
		super.onBlockPlacedBy(world, x, y, z, player);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		if(!world.isRemote){
			PCws_WeaselPlugin weaselPlugin = getPlugin(world, x, y, z);
			if(weaselPlugin!=null)
				PCws_WeaselManager.removePlugin(weaselPlugin);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
    public int idDropped(int i, Random random, int j)
    {
        return -1;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
	
	@Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        int type = GameInfo.<PCws_TileEntityWeasel>getTE(world, x, y, z).getType();
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !GameInfo.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PCws_App.weasel, 1, type));
        }

        return remove;
    }
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		PCws_TileEntityWeasel te = GameInfo.getTE(world, x, y, z);

		if (te != null) {
			String b = (String)te.getData("error");
			if (b!=null) {
				double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
				double d1 = (y + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
				double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

				world.spawnParticle("largesmoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			PCws_WeaselManager.getPluginInfo((Integer)obj[1]).renderInventoryBlock((Block)obj[0], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			break;
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			return list;
		}
		default:
			return null;
		}
		return true;
	}

	public static PCws_WeaselPlugin getPlugin(World world, int x, int y, int z){
		if(world.isRemote)
			return null;
		PCws_WeaselPlugin wp = null;
		if(world.blockExists(x, y, z))
			wp = GameInfo.<PCws_TileEntityWeasel>getTE(world, x, y, z).getPlugin();
		if(wp==null)
			wp = PCws_WeaselManager.getPlugin(world, x, y, z);
		return wp;
	}
	
	public static PCws_WeaselPlugin getPlugin(World world, PC_VecI pos){
		return getPlugin(world, pos.x, pos.y, pos.z);
	}
	
	/**
	 * Get redstone states on direct inputs. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 5
	 * TOP, 4 BOTTOM
	 * 
	 * @param world
	 * @param pos the block position
	 * @return array of booleans
	 */
	public static boolean[] getWeaselInputStates(World world, PC_VecI pos) {

		if(!world.blockExists(pos.x, pos.y, pos.z))
			return null;
		
		return new boolean[]{
				powered_from_input(world, pos, 0),
				powered_from_input(world, pos, 1),
				powered_from_input(world, pos, 2),
				powered_from_input(world, pos, 3),
				powered_from_input(world, pos, 4),
				powered_from_input(world, pos, 5)
			};
		
	}

	public static boolean gettingPowerFrom(World world, int x, int y, int z, int rot){
		return world.isBlockIndirectlyProvidingPowerTo(x, y, z, rot) || 
				(GameInfo.getBID(world, x, y, z) == Block.redstoneWire.blockID && 
				GameInfo.getMD(world, x, y, z)>0);
	}
	
	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param pos the block position
	 * @param inp the input number
	 * @return is powered
	 */
	public static boolean powered_from_input(World world, PC_VecI pos, int inp) {
		if (world == null) return false;
		int x = pos.x, y = pos.y, z = pos.z;
		
		if (inp == 4) {
			return gettingPowerFrom(world, x, y - 1, z, 0);
		}
		if (inp == 5) {
			return gettingPowerFrom(world, x, y + 1, z, 1);
		}

		int rotation = getRotation(world.getBlockMetadata(x, y, z));
		int N0 = 0, N1 = 1, N2 = 2, N3 = 3;
		if (inp == 0) {
			N0 = 0;
			N1 = 1;
			N2 = 2;
			N3 = 3;
		}else if (inp == 1) {
			N0 = 3;
			N1 = 0;
			N2 = 1;
			N3 = 2;
		} else if (inp == 2) {
			N0 = 1;
			N1 = 2;
			N2 = 3;
			N3 = 0;
		} else if (inp == 3) {
			N0 = 2;
			N1 = 3;
			N2 = 0;
			N3 = 1;
		}

		if (rotation == N0) {
			return gettingPowerFrom(world, x, y, z + 1, 3);
		}
		if (rotation == N1) {
			return gettingPowerFrom(world, x - 1, y, z, 4);
		}
		if (rotation == N2) {
			return gettingPowerFrom(world, x, y, z - 1, 2);
		}
		if (rotation == N3) {
			return gettingPowerFrom(world, x + 1, y, z, 5);
		}
		return false;
	}
	
	/**
	 * Get gate rotation, same as getRotation, but available statically
	 * 
	 * @param meta block meta
	 * @return rotation 0,1,2,3
	 */
	public static int getRotation(int meta) {
		return meta & 3;
	}
	
}
