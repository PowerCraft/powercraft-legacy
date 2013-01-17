package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct4;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_VecI;
import powercraft.weasel.PCws_WeaselBitmapUtils.WeaselBitmapAdapter;
import powercraft.weasel.PCws_WeaselBitmapUtils.WeaselBitmapProvider;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginTouchscreen extends PCws_WeaselPlugin implements WeaselBitmapProvider {

	/** width in pixels */
	public static final int WIDTH = 14 * 4;
	/** width in pixels */
	public static final int HEIGHT = 12 * 4;
	
	/** screen array of rgb */
	public int screen[][] = new int[WIDTH][HEIGHT];
	
	private WeaselBitmapAdapter imageAdapter = new WeaselBitmapAdapter(this);
	
	public PCws_WeaselPluginTouchscreen(){
		for(int j=0; j<HEIGHT; j++){
			for(int i=0; i<WIDTH; i++){
				screen[i][j] = -1;
			}
		}
	}
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		for(int j=0; j<HEIGHT; j++){
			for(int i=0; i<WIDTH; i++){
				screen[i][j] = tag.getInteger("pic["+i+"]["+j+"]");
			}
		}
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		for(int j=0; j<HEIGHT; j++){
			for(int i=0; i<WIDTH; i++){
				tag.setInteger("pic["+i+"]["+j+"]", screen[i][j]);
			}
		}
		return tag;
	}

	@Override
	public void update() {}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {
		int n=0;
		List<Object> data = new ArrayList<Object>();
		for(int j=0; j<HEIGHT; j++){
			for(int i=0; i<WIDTH; i++){
				data.add("pic["+i+"]["+j+"]");
				data.add(screen[i][j]);
				tileEntityWeasel.setDataNoSend("pic["+i+"]["+j+"]", screen[i][j]);
				n++;
				if(n>200){
					PC_PacketHandler.setTileEntity(tileEntityWeasel, data.toArray());
					data.clear();
					n=0;
				}
			}
		}
		PC_PacketHandler.setTileEntity(tileEntityWeasel, data.toArray());
	}
	
	@Override
	public void getClientMsg(String msg, Object obj) {
		if(msg.equalsIgnoreCase("event")){
			PC_Struct4<String, PC_VecI, Integer, Integer> event = (PC_Struct4<String, PC_VecI, Integer, Integer>) obj;
			if(getNetwork()!=null){
				WeaselDouble mx = new WeaselDouble(event.b.x);
				WeaselDouble my = new WeaselDouble(event.b.y);
				WeaselDouble mk = new WeaselDouble(event.c);
				WeaselString k = new WeaselString("" + (char)(int)event.d);
				if(!getNetwork().callFunctionOnEngine("touchEvent."+getName()+"."+event.a, mx, my, mk, k)){
					WeaselString e = new WeaselString(event.a);
					if(!getNetwork().callFunctionOnEngine("touchEvent."+getName(), e, mx, my, mk, k)){
						WeaselString d = new WeaselString(getName());
						getNetwork().callFunctionOnEngine("touchEvent", d, e, mx, my, mk, k);
					}
				}
			}
		}else{
			super.getClientMsg(msg, obj);
		}
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		if(GameInfo.isPlacingReversed(player)){
			Gres.openGres("WeaselOnlyNet", player, getPos().x, getPos().y, getPos().z);
		}else{
			Gres.openGres("WeaselTouchscreen", player, getPos().x, getPos().y, getPos().z);
		}
	}

	@Override
	public void restart() {
		for(int j=0; j<HEIGHT; j++){
			for(int i=0; i<WIDTH; i++){
				screen[i][j] = -1;
			}
		}
	}

	@Override
	public PC_VecI getBitmapSize() {
		return new PC_VecI(WIDTH, HEIGHT);
	}

	@Override
	public int getBitmapPixel(int x, int y) {
		return screen[x][y];
	}

	@Override
	public void setBitmapPixel(int x, int y, int color) {
		needsSave();
		screen[x][y] = color;
		setData("pic["+x+"]["+y+"]", color);
	}

	@Override
	public void resize(int w, int h) {
		
	}

	@Override
	public void notifyChanges() {
		PCws_TileEntityWeasel te = getTE();
		if(te!=null){
			int n=0;
			List<Object> data = new ArrayList<Object>();
			for(int j=0; j<HEIGHT; j++){
				for(int i=0; i<WIDTH; i++){
					if((Integer)te.getData("pic["+i+"]["+j+"]") != screen[i][j]){
						data.add("pic["+i+"]["+j+"]");
						data.add(screen[i][j]);
						te.setDataNoSend("pic["+i+"]["+j+"]", screen[i][j]);
						n++;
						if(n>200){
							PC_PacketHandler.setTileEntity(te, data.toArray());
							data.clear();
							n=0;
						}
					}
				}
			}
			PC_PacketHandler.setTileEntity(te, data.toArray());
		}
	}
	
	@Override
	public WeaselBitmapProvider getImageForName(String name) {
		/*ItemStack disk = null;
		for (PCnt_WeaselPlugin member: network.iterator()) {
			if (member instanceof PCnt_WeaselPluginDiskDrive) {
				disk = ((PCnt_WeaselPluginDiskDrive) member).getImageDisk(name);
				if (disk != null) break;
			}
		}
		if(disk == null) return null;
		
		return PCnt_ItemWeaselDisk.getImageData(disk);*/
		return null;
	}

}
