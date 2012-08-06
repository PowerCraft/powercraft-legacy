package net.minecraft.src;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Radio Tile Entity (both TX and RX)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCfix_TileEntityRadioPlaceholder extends PC_TileEntity {

	/** Device channel */
	public String channel = mod_PClogic.default_radio_channel;
	/** Device type, 0=TX, 1=RX */
	public int type = 0; // 0=tx, 1=rx

	/** Dimension of the device (nether, world, end) */
	public int dim = 0;

	private boolean loaded = false;

	@Override
	public void updateEntity() {
		if (!loaded) {

			try {
				Properties props = new Properties();
				props.load(new FileInputStream((((SaveHandler) worldObj.saveHandler).getSaveDirectory()) + "/radio/" + xCoord + "_" + yCoord + "_"
						+ zCoord));

				String type_s = (String) props.get("type");

				if (type_s == null) {
					throw new NullPointerException();
				}

				String channel_s = (String) props.get("channel");

				if (channel_s == null) {
					throw new NullPointerException();
				}

				type = Integer.parseInt(type_s);

				channel = channel_s;

				PC_Logger.fine("Loaded old radio entry for block at [" + xCoord + "," + yCoord + "," + zCoord + "], channel=" + channel + ", type="
						+ type);


				Block block = mod_PClogic.radio;

				int i = xCoord, j = yCoord, k = zCoord;

				if (worldObj.setBlockWithNotify(i, j, k, block.blockID)) {
					/** @todo block.onBlockPlaced(worldObj, i, j, k, 0);*/
					worldObj.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(),
							(block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

					// set tile entity
					PClo_TileEntityRadio ter = (PClo_TileEntityRadio) worldObj.getBlockTileEntity(i, j, k);
					if (ter == null) {
						/** @todo ter = (PClo_TileEntityRadio) ((BlockContainer) block).createNewTileEntity(world);*/
					}

					ter.channel = channel;

					ter.setType(type);
					ter.dim = PC_Utils.mc().thePlayer.dimension;

					worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, ter);

					if (type == 1) {
						ter.active = mod_PClogic.RADIO.getChannelState(channel);
						if (ter.active) {
							worldObj.setBlockMetadataWithNotify(i, j, k, 1);
						}
					}

					worldObj.scheduleBlockUpdate(i, j, k, block.blockID, 1);
				}

				PC_Logger.fine("Radio device replaced with new version.");

			} catch (FileNotFoundException e) {
				PC_Logger.throwing(getClass().getName(), "updateEntity()", e);
				e.printStackTrace();
			} catch (IOException e) {
				PC_Logger.throwing(getClass().getName(), "updateEntity()", e);
				e.printStackTrace();
			} catch (NumberFormatException e) {
				PC_Logger.throwing(getClass().getName(), "updateEntity()", e);
				e.printStackTrace();
			} catch (Throwable t) {
				PC_Logger.throwing(getClass().getName(), "updateEntity()", t);
				t.printStackTrace();
			}

			loaded = true;
		}
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return true
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}
}
