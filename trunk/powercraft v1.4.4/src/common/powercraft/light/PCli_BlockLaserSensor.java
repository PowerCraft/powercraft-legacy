package powercraft.light;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_BeamTracer;
import powercraft.core.PC_Block;
import powercraft.core.PC_Color;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_IBeamSpecialHandling;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;
import powercraft.core.PC_BeamTracer.result;

public class PCli_BlockLaserSensor extends PC_Block implements PC_ICraftingToolDisplayer, PC_IBlockRenderer, PC_IBeamSpecialHandling {

	public PCli_BlockLaserSensor(int id) {
		super(id, 0, Material.ground, false);
		setStepSound(Block.soundMetalFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(0.7F);
        setResistance(10.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getDefaultName() {
		return "Laser Sensor";
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
    public TileEntity createNewTileEntity(World world)
    {
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
    public boolean isPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return ((PCli_TileEntityLaserSensor) PC_Utils.getTE(world, i, j, k)).isActive();
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int i, int j, int k, int l)
    {
        return isPoweringTo(world, i, j, k, l);
    }

	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftLight.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		Block.ice.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		PC_Renderer.renderInvBox(renderer, Block.ice, 0);
		Block.ice.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		Block.cobblestone.setBlockBounds(0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		// reset
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		Block.ice.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		PC_Renderer.renderStandardBlock(renderer, Block.ice, x, y, z);
		Block.ice.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderStandardBlock(renderer, Block.cobblestone, x, y, z);
		Block.cobblestone.setBlockBounds(0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderStandardBlock(renderer, Block.cobblestone, x, y, z);
		// reset
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public result onHitByBeamTracer(PC_BeamTracer beamTracer, PC_CoordI cnt, PC_CoordI move, PC_Color color, float strength, int distanceToMove) {
		World world = beamTracer.getWorld();
		PCli_TileEntityLaserSensor te = PC_Utils.getTE(world, cnt.x, cnt.y, cnt.z, blockID);
		if(te!=null){
			te.hitByBeam();
		}
		return result.STOP;
	}
	
}
