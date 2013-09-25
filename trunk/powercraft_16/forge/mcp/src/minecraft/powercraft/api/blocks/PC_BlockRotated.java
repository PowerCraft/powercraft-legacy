package powercraft.api.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;

public abstract class PC_BlockRotated extends PC_Block {

	public PC_BlockRotated(int id, Material material) {
		super(id, material);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.onBlockActivated(player, rotate(world, x, y, z, PC_Direction.getOrientation(side)), xHit, yHit, zHit);
	}


	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getRedstonePowerValue(rotate(world, x, y, z, PC_Direction.getOrientation(side)));
	}


	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getRedstonePowerValue(rotate(world, x, y, z, PC_Direction.getOrientation(side)));
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.canConnectRedstone(rotate(world, x, y, z, PC_Direction.getOrientation(side)));
	}
	
	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.rotateBlock(rotate(world, x, y, z, PC_Direction.getDirection(axis)));
	}


	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y, int z) {

		PC_Direction rotation = PC_Direction.DOWN;
		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		PC_Direction[] rotations =  tileEntity.getValidRotations();
		if(rotations==null)
			return null;
		ForgeDirection[] forgeRotations = new ForgeDirection[rotations.length];
		for(int i=0; i<forgeRotations.length; i++){
			forgeRotations[i] = rotations[i].getRotation(rotation).getForgeDirection();
		}
		return forgeRotations;
	}


	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.recolourBlock(rotate(world, x, y, z, PC_Direction.getDirection(side)), colour);
	}
	
	
	
	@Override
	public int modifyMetadataPostPlace(World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ, int metadata,
			ItemStack stack, EntityPlayer entity) {
		int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		return (PC_Direction.PLAYER2MD[l]&3)|(metadata&~3);
	}

	public PC_Direction rotate(IBlockAccess world, int x, int y, int z, PC_Direction side){
		return side.rotateMD(PC_Utils.getRotation(world, x, y, z));
	}

	@Override
	public final Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		return getBlockTexture(world, x, y, z, rotate(world, x, y, z, PC_Direction.getOrientation(side)));
	}
	
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		return super.getBlockTexture(world, x, y, z, side.ordinal());
	}

	@Override
	public final Icon getIcon(int side, int metadata) {
		return getIcon(PC_Direction.getOrientation(side).rotateMD((metadata & 3)+2), metadata);
	}
	
	public Icon getIcon(PC_Direction side, int metadata) {
		return super.getIcon(side.ordinal(), metadata);
	}
	
}
