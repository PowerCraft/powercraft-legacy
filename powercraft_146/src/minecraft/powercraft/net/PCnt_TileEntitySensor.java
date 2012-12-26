package powercraft.net;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCnt_TileEntitySensor extends PC_TileEntity implements PC_ITileEntityRenderer {
	/** Flag that the sensor is active - giving power */
	public boolean active = false;
	/** Current range in blocks. */
	public int range = 3;
	
	public int type;
	
	private static PCnt_ModelSensor model = new PCnt_ModelSensor();
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		type = stack.getItemDamage();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		active = nbttagcompound.getBoolean("on");
		range = nbttagcompound.getInteger("range");
		if (range < 1) {
			range = 3;
		}
		type = nbttagcompound.getInteger("type");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("on", active);
		nbttagcompound.setInteger("range", range);
		nbttagcompound.setInteger("type", type);
	}


	/**
	 * show range using
	 */
	public void printRange() {
		String msg = "";

		if (range == 1) {
			msg = Lang.tr("pc.sensor.range.1", new String[] { range + "" });
		}
		if (range > 1 && range < 5) {
			msg = Lang.tr("pc.sensor.range.2-4", new String[] { range + "" });
		}
		if (range >= 5) {
			msg = Lang.tr("pc.sensor.range.5+", new String[] { range + "" });
		}

		Communication.chatMsg(msg, true);
	}

	/**
	 * Forge method - can update?
	 * 
	 * @return can update
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		int count = 0;
		if (getGroup() == 0) {
			
			count += worldObj.getEntitiesWithinAABB(
					EntityItem.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
			
			count += worldObj.getEntitiesWithinAABB(
					EntityXPOrb.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
			
			count += worldObj.getEntitiesWithinAABB(
					EntityArrow.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
			
		} else if (getGroup() == 1) {
			count += worldObj.getEntitiesWithinAABB(
					EntityAnimal.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
			count += worldObj.getEntitiesWithinAABB(
					EntityCreature.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
			count += worldObj.getEntitiesWithinAABB(
					EntitySlime.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
		} else if (getGroup() == 2) {
			count += worldObj.getEntitiesWithinAABB(
					EntityPlayer.class,
					AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(),
							getRange())).size();
		}
		if (count > 0) {
			if (!active) {
				active = true;
				if(getBlockType()!=null){
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
					worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}

			}
		} else {
			if (active) {
				active = false;
				if(getBlockType()!=null){
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
					worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}

			}
		}
	}

	/**
	 * Get detected entity group (item, mob, player)
	 * 
	 * @return group number
	 */
	public int getGroup() {
		return type;
	}

	/**
	 * Get surrent range distance.
	 * 
	 * @return range
	 */
	public int getRange() {
		return range;
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("active"))
				active = (Boolean)o[p++];
			else if(var.equals("range"))
				range = (Integer)o[p++];
			else if(var.equals("type"))
				type = (Integer)o[p++];
		}
		
	}

	@Override
	public Object[] getData() {
		return new Object[] {
				"active", active,
				"range", range,
				"type", type
		};
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Net"))+"block_sensor.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);
		model.setType(getGroup(), active);
		model.render();
		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
	}
}
