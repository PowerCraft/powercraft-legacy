package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_BeamTracer.BeamSettings;
import powercraft.management.PC_BeamTracer.result;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.registry.PC_MSGRegistry;

@PC_BlockInfo(tileEntity=PCli_TileEntityLaserSensor.class)
public class PCli_BlockLaserSensor extends PC_Block implements PC_IItemInfo {

	public PCli_BlockLaserSensor(int id) {
		super(id, 0, Material.ground, false);
		setStepSound(Block.soundMetalFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(0.7F);
        setResistance(10.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
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
    public TileEntity newTileEntity(World world, int metadata) {
        return new PCli_TileEntityLaserSensor();
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i, j, k, (double) i + 1, (double) j + 0.7F, (double) k + 1);
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
	public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
    	return ((PCli_TileEntityLaserSensor) GameInfo.getTE(world, x, y, z)).isActive();
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int s) {
		return isProvidingWeakPower(world, x, y, z, s);
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		ValueWriting.setBlockBounds(Block.ice, 0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		PC_Renderer.renderInvBox(renderer, Block.ice, 0);
		ValueWriting.setBlockBounds(Block.ice, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		ValueWriting.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		ValueWriting.setBlockBounds(Block.cobblestone, 0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		// reset
		ValueWriting.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		ValueWriting.setBlockBounds(Block.ice, 0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		PC_Renderer.renderStandardBlock(renderer, Block.ice, x, y, z);
		ValueWriting.setBlockBounds(Block.ice, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		ValueWriting.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderStandardBlock(renderer, Block.cobblestone, x, y, z);
		ValueWriting.setBlockBounds(Block.cobblestone, 0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderStandardBlock(renderer, Block.cobblestone, x, y, z);
		// reset
		ValueWriting.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public result onHitByBeamTracer(IBlockAccess world, BeamSettings bs) {
		PC_VecI pos = bs.getPos();
		PCli_TileEntityLaserSensor te = GameInfo.getTE(world, pos.x, pos.y, pos.z, blockID);
		if(te!=null && !te.getWorldObj().isRemote){
			te.hitByBeam();
		}
		return result.STOP;
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Laser Sensor";
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_ON_HIT_BY_BEAM_TRACER:
			return onHitByBeamTracer(world, (BeamSettings)obj[0]);
		default:
			return null;
		}
		return true;
	}
	
}
