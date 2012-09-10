package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.PCnt_NetManager.NetworkMember;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;


/**
 * TouchScreen with graphics display
 * 
 * @author MightyPork
 *
 */
public class PCnt_WeaselPluginTouchscreen extends PCnt_WeaselPlugin implements WeaselBitmapProvider {

	/** width in pixels */
	public static final int WIDTH = 14 * 4;
	/** width in pixels */
	public static final int HEIGHT = 12 * 4;

	/** screen array of rgb */
	public int screen[][] = new int[WIDTH][HEIGHT];
	
	/** Rotation, like sign, 0-15 */
	public int rotation;

	private boolean isChanged;
	private WeaselBitmapAdapter imageAdapter;
	
	@Override
	protected void onNameChanged() {
		imageAdapter.setObjName(getName());
	}

	/**
	 * TS plugin
	 * @param tew tile entity
	 */
	public PCnt_WeaselPluginTouchscreen(PCnt_TileEntityWeasel tew) {
		super(tew);
		this.imageAdapter = new PC_BitmapUtils.WeaselBitmapAdapter(this, getName());
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}



	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		isChanged = true;
		if(functionName.equals(getName()+".toDisk")) {
			ItemStack disk = null;
			for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member instanceof PCnt_WeaselPluginDiskDrive) {
					disk = ((PCnt_WeaselPluginDiskDrive) member).getImageDisk(Calc.toString(args[0]));					
					if (disk != null) break;
				}
			}
			if(disk == null) {
				throw new WeaselRuntimeException(getName()+".toDisk(): disk "+args[0]+" not found.");
			}
			
			if(args.length == 1) {
				PCnt_ItemWeaselDisk.setImageData(disk, screen);
				return null;
			}else {
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		if(functionName.equals(getName()+".fromDisk")) {
			ItemStack disk = null;
			for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member instanceof PCnt_WeaselPluginDiskDrive) {
					disk = ((PCnt_WeaselPluginDiskDrive) member).getImageDisk(Calc.toString(args[0]));					
					if (disk != null) break;
				}
			}
			if(disk == null) {
				throw new WeaselRuntimeException(getName()+".fromDisk(): disk "+args[0]+" not found.");
			}
			
			if(args.length == 1) {
				PC_BitmapUtils.rect(screen, 0, 0, WIDTH, HEIGHT, -1);
				PC_BitmapUtils.image(screen, PCnt_ItemWeaselDisk.getImageData(disk), 0, 0);
				return null;
			}else {
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		if(functionName.equals(getName()+".resize")) {
			throw new WeaselRuntimeException("Can't resize touchscreen!");
		}
		return imageAdapter.callProvidedFunction(engine, functionName, args);
	}

	@Override
	public WeaselObject getVariable(String name) {
		return imageAdapter.getVariable(name);
	}

	@Override
	public void setVariable(String name, Object object) {
		imageAdapter.setVariable(name, object);
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		ArrayList<String> list = new ArrayList<String>(1);
		imageAdapter.setObjName(getName());
		list.addAll(imageAdapter.getProvidedFunctionNames());
		list.add(getName()+".toDisk");
		list.add(getName()+".fromDisk");
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(10);
		list.addAll(imageAdapter.getProvidedVariableNames());
		return list;
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.TOUCHSCREEN;
	}

	@Override
	protected boolean updateTick() {
		if (isChanged) {
			isChanged = false;
			return true;
		}
		return false;
	}

	@Override
	public void onRedstoneSignalChanged() {}

	@Override
	public void restartDevice() {
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				screen[i][j] = -1;
			}
		}
	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	protected void onDeviceDestroyed() {}

	@Override
	public void callFunctionOnEngine(String function, Object... args) {}

	@Override
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				screen[i][j] = tag.getInteger("p" + i + "_" + j);
			}
		}
		rotation = tag.getInteger("rotation");
		imageAdapter.setObjName(getName());
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				tag.setInteger("p" + i + "_" + j, screen[i][j]);
			}
		}
		tag.setInteger("rotation", rotation);
		return tag;
	}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {
		rotation = MathHelper.floor_double(((entityliving.rotationYaw + 180F) * 16F) / 360F + 0.5D) & 0xf;
		restartDevice();
	}

	@Override
	public void onRandomDisplayTick(Random random) {}

	@Override
	public boolean onClick(EntityPlayer player) {
		if (player.isSneaking()) {
			PC_Utils.openGres(player, new PCnt_GuiWeaselTouchscreen(this));
		} else {
			PC_Utils.openGres(player, new PCnt_GuiWeaselTouchscreenTouch(this));
		}

		return true;
	}

	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 1, 1 };
	}

	@Override
	public int[][] getImageForName(String name) {
		ItemStack disk = null;
		for (NetworkMember member : getNetwork().getMembers().values()) {
			if (member instanceof PCnt_WeaselPluginDiskDrive) {
				disk = ((PCnt_WeaselPluginDiskDrive) member).getImageDisk(name);
				if (disk != null) break;
			}
		}
		if(disk == null) return null;
		
		return PCnt_ItemWeaselDisk.getImageData(disk);
	}

	@Override
	public int[][] getScreen() {
		return screen;
	}

	@Override
	public void screenChanged(int[][] newScreen) {
		// do nothing.
	}

	@Override
	public void setScreen(int[][] newdata) {
		// no resize allowed here.
	}


}
