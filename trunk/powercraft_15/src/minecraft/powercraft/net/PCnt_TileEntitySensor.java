package powercraft.net;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;

public class PCnt_TileEntitySensor extends PC_TileEntity implements PC_ITileEntityRenderer {
	
	/** Flag that the sensor is active - giving power */
	@PC_ClientServerSync(clientChangeAble=false)
	private boolean active = false;
	/** Current range in blocks. */
	@PC_ClientServerSync
	private int range = 3;
	@PC_ClientServerSync(clientChangeAble=false)
	private int type;
	
	private static PCnt_ModelSensor model = new PCnt_ModelSensor();
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		type = stack.getItemDamage();
	}

	public void setRange(int range) {
		if(this.range != range){
			this.range = range;
			notifyChanges("range");
		}
	}
	
	/**
	 * show range using
	 */
	public void printRange() {
		String msg = "";

		if (getRange() == 1) {
			msg = PC_LangRegistry.tr("pc.sensor.range.1", new String[] { getRange() + "" });
		}
		if (getRange() > 1 && getRange() < 5) {
			msg = PC_LangRegistry.tr("pc.sensor.range.2-4", new String[] { getRange() + "" });
		}
		if (getRange() >= 5) {
			msg = PC_LangRegistry.tr("pc.sensor.range.5+", new String[] { getRange() + "" });
		}

		PC_Utils.chatMsg(msg);
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
		if(worldObj.isRemote)
			return;
		int count = 0;
		AxisAlignedBB bb=AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(getRange(), getRange(), getRange());
		if (getGroup() == 0) {
			
			count += worldObj.getEntitiesWithinAABB(
					EntityItem.class, bb).size();
			
			count += worldObj.getEntitiesWithinAABB(
					EntityXPOrb.class, bb).size();
			
			count += worldObj.getEntitiesWithinAABB(
					EntityArrow.class, bb).size();
			
		} else if (getGroup() == 1) {
			count += worldObj.getEntitiesWithinAABB(
					EntityAnimal.class, bb).size();
			count += worldObj.getEntitiesWithinAABB(
					EntityCreature.class, bb).size();
			count += worldObj.getEntitiesWithinAABB(
					EntitySlime.class, bb).size();
		} else if (getGroup() == 2) {
			count += worldObj.getEntitiesWithinAABB(
					EntityPlayer.class, bb).size();
		}
		if (count > 0) {
			if (!isActive()) {
				active = true;
				notifyChanges("active");
				if(getBlockType()!=null){
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
					worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}

			}
		} else {
			if (isActive()) {
				active = false;
				notifyChanges("active");
				if(getBlockType()!=null){
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
					worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}

			}
		}
	}

	@Override
	public int getProvidingStrongRedstonePowerValue(PC_Direction dir) {
		return isActive()?15:0;
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

	public boolean isActive() {
		return active;
	}
	
	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef(0, -0.5F, 0);

		PC_Renderer.bindTexture(PC_TextureRegistry.getPowerCraftImageDir()+PC_TextureRegistry.getTextureName(PCnt_App.instance, "block_sensor.png"));

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);
		model.setType(getGroup(), isActive());
		model.render();
		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
	}
}
