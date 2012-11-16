package powercraft.light;

import java.util.Random;

import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.core.PC_Color;
import powercraft.core.PC_CoordD;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_InvUtils;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PCli_TileEntityLightningConductor extends PC_TileEntity  {
	
	/** Charge level in the lightning conductor */
	private int lightningCharge = 0;
	/** Charge needed to make lightning */
	public int lightningChargeRequired = 0;
	/** lowest allowed charge level for flash */
	private static final int FLASH_CHARGE_MIN = 8000;
	/** highest allowed charge level needed for flash */
	private static final int FLASH_CHARGE_MAX = 17000;
	private static final int FLASH_MIN_HEIGHT = 79;
	private static Random rand = new Random();
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(!worldObj.isRemote){
			lightningChargeRequired =  getLightningChargeRequired();
		}
	}

	@Override
	public void updateEntity() {
		if(tileEntityInvalid)
			return;
		if(worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord)){
			if(!worldObj.isRemote){
				updateFlashCharge();
				if(lightningCharge >= lightningChargeRequired){
					EntityLightningBolt bolt = null;
					for (int i = 0; i < 2; i++) {
						bolt = new EntityLightningBolt(worldObj, xCoord, yCoord+1, zCoord);
						worldObj.addWeatherEffect(bolt);
					}
					PC_Utils.givePowerToBlock(worldObj, xCoord, yCoord-2, zCoord, 1000.0f);
					lightningCharge = 0;
					lightningChargeRequired = getLightningChargeRequired();
				}
			}else{
				for (int i = 0; i < 2; i++) {
					PC_Utils.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_CoordD(getCoord())
					.offset(-0.1F + rand.nextFloat() * 1.2F, rand.nextFloat() * 0.8F - 1.0f, -0.1F + rand.nextFloat() * 1.2F), new PC_Color(0.6,
							0.6, 1), new PC_CoordD(), 0);
				}
		
				for (int i = 0; i < 2; i++) {
					PC_Utils.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_CoordD(getCoord())
					.offset(0.1F + rand.nextFloat() * 0.8F, rand.nextFloat() * 0.8F - 0.2f, 0.1F + rand.nextFloat() * 0.8F), new PC_Color(
					0.6, 0.6, 1), new PC_CoordD(), 0);
				}
		
				for (int i = 0; i < 2; i++) {
					PC_Utils.spawnParticle("PC_EntityLaserParticleFX", worldObj, new PC_CoordD(getCoord())
					.offset(0.2F + rand.nextFloat() * 0.6F, rand.nextFloat() * 0.9F - 0.4f, 0.2F + rand.nextFloat() * 0.6F), new PC_Color(
					0.6, 0.6, 1), new PC_CoordD(), 0);
				}
			}
		}
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return false
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	private void updateFlashCharge() {

		int increment = rand.nextInt(4);

		if (worldObj.isThundering()) {
			increment = 2 + rand.nextInt(15);
		} else if (worldObj.isRaining()) {
			increment = 1 + rand.nextInt(10);
		} else if (worldObj.isBlockHighHumidity(xCoord, yCoord, zCoord)) {
			increment = rand.nextInt(2);
		}

		lightningCharge += increment;

	}
	
	private int getLightningChargeRequired() {
		
		return FLASH_CHARGE_MIN + rand.nextInt(FLASH_CHARGE_MAX - FLASH_CHARGE_MIN);

	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		lightningCharge = tag.getInteger("LightningCharge");
		lightningChargeRequired = tag.getInteger("LightningChargeRequired");

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setInteger("LightningCharge", lightningCharge);
		tag.setInteger("LightningChargeRequired", lightningChargeRequired);

	}
	
}
