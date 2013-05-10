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
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_ItemRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Weasel", itemBlock=PCws_ItemBlockWeasel.class, tileEntity=PCws_TileEntityWeasel.class)
public class PCws_BlockWeasel extends PC_Block {

	public PCws_BlockWeasel(int id) {
		super(id, Material.ground);
		setHardness(0.5F);
		setLightValue(0);
		setStepSound(Block.soundWoodFootstep);
		disableStats();
		setResistance(60.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public void onIconLoading() {
		for(PCws_WeaselPluginInfo pluginInfo:PCws_WeaselManager.getPluginInfoMap().values()){
			pluginInfo.onIconLoading();
		}
	}

	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, PC_Direction par5) {
		return PC_Utils.<PCws_TileEntityWeasel>getTE(par1iBlockAccess, par2, par3, par4).getTexture(par5);
	}
	
	@Override
	public Icon getIcon(PC_Direction par1, int par2) {
		return PCws_WeaselManager.getPluginInfo(0).getTexture(PC_Direction.BOTTOM);
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
		PCws_TileEntityWeasel te = PC_Utils.getTE(world, x, y, z);
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
	
	public final void setWeaselBlockBounds(float par1, float par2, float par3, float par4, float par5, float par6){
		setBlockBounds(par1, par2, par3, par4, par5, par6);
	}
	
	
	@Override
	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction pcDir) {
		PCws_WeaselPlugin weaselPlugin = PC_Utils.<PCws_TileEntityWeasel>getTE(world, x, y, z).getPlugin();
		int dir = pcDir.getMCDir();
		if(weaselPlugin!=null){
			if(dir==0){
				return weaselPlugin.getOutport(4);
			}else if(dir==1){
				return weaselPlugin.getOutport(5);
			}else if(dir==2){
				return weaselPlugin.getOutport(0);
			}else if(dir==3){
				return weaselPlugin.getOutport(3);
			}else if(dir==4){
				return weaselPlugin.getOutport(2);
			}else{
				return weaselPlugin.getOutport(1);
			}
		}
		return 0;
	}

	@Override
	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return getProvidingWeakRedstonePowerValue(world, x, y, z, dir);
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

						PC_Utils.chatMsg(PC_LangRegistry.tr("pc.weasel.activatorSetNetwork", new String[] { network }));
						world.playSoundEffect(x, y, z, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F, 0.5F);
					}
				}else{
					String network = weaselPlugin.getNetwork().getName();
					if (!ihold.hasTagCompound()) {
						ihold.setTagCompound(new NBTTagCompound());
					}
					ihold.getTagCompound().setString("WeaselNetwork", network);
					PC_Utils.chatMsg(PC_LangRegistry.tr("pc.weasel.activatorGetNetwork", new String[] { network }));
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
        int type = PC_Utils.<PCws_TileEntityWeasel>getTE(world, x, y, z).getType();
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !PC_Utils.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PCws_App.weasel, 1, type));
        }

        return remove;
    }
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		PCws_TileEntityWeasel te = PC_Utils.getTE(world, x, y, z);

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
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}

	@Override
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		PCws_WeaselManager.getPluginInfo(metadata).renderInventoryBlock(this, renderer);
		return true;
	}

	public static PCws_WeaselPlugin getPlugin(World world, int x, int y, int z){
		if(world.isRemote)
			return null;
		PCws_WeaselPlugin wp = null;
		if(world.blockExists(x, y, z))
			wp = PC_Utils.<PCws_TileEntityWeasel>getTE(world, x, y, z).getPlugin();
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
	public int[] getWeaselInputStates(World world, PC_VecI pos) {

		if(!world.blockExists(pos.x, pos.y, pos.z))
			return null;
		
		return new int[]{
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.BACK),
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.LEFT),
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.RIGHT),
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.FRONT),
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.TOP),
				getRedstonePowereValueFromInput(world, pos.x, pos.y, pos.z, PC_Direction.BOTTOM)
			};
		
	}

	public static int gettingPowerFrom(World world, int x, int y, int z, int rot){
		if(world.getIndirectPowerLevelTo(x, y, z, rot)>0){
			return world.getIndirectPowerLevelTo(x, y, z, rot);
		}
		if(PC_Utils.getBID(world, x, y, z) == Block.redstoneWire.blockID){
			return PC_Utils.getMD(world, x, y, z);
		}
		return 0;
	}
	
}
