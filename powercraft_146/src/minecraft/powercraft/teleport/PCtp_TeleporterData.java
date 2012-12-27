package powercraft.teleport;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_INBT;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.SaveHandler;

public class PCtp_TeleporterData implements PC_INBT, Serializable {

	public static final int N=0, E=1, S=2, W=3;
	
	public String name;
	public PC_VecI pos;
	public int dimension;
	public boolean animals;
	public boolean monsters;
	public boolean items;
	public boolean players;
	public boolean sneakTrigger;
	public boolean playerChoose;
	public PC_VecI defaultTarget;
	public int defaultTargetDimension;
	public int direction;
	
	public PCtp_TeleporterData(){
		name="";
		animals = true;
		monsters = true;
		items = true;
		players = true;
		playerChoose = true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttag) {
		pos = new PC_VecI();
		name = nbttag.getString("name");
		SaveHandler.loadFromNBT(nbttag, "pos", pos);
		dimension = nbttag.getInteger("dimension");
		animals = nbttag.getBoolean("animals");
		monsters = nbttag.getBoolean("monsters");
		items = nbttag.getBoolean("items");
		players = nbttag.getBoolean("players");
		sneakTrigger = nbttag.getBoolean("sneakTrigger");
		playerChoose = nbttag.getBoolean("playerChoose");
		if(nbttag.hasKey("defaultTarget")){
			defaultTarget = new PC_VecI();
			SaveHandler.loadFromNBT(nbttag, "defaultTarget", defaultTarget);
		}else{
			defaultTarget = null;
		}
		defaultTargetDimension = nbttag.getInteger("defaultTargetDimension");
		direction = nbttag.getInteger("direction");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttag) {
		nbttag.setString("name", name);
		SaveHandler.saveToNBT(nbttag, "pos", pos);
		nbttag.setInteger("dimension", dimension);
		nbttag.setBoolean("animals", animals);
		nbttag.setBoolean("monsters", monsters);
		nbttag.setBoolean("items", items);
		nbttag.setBoolean("players", players);
		nbttag.setBoolean("sneakTrigger", sneakTrigger);
		nbttag.setBoolean("playerChoose", playerChoose);
		if(defaultTarget!=null)
			SaveHandler.saveToNBT(nbttag, "defaultTarget", defaultTarget);
		nbttag.setInteger("defaultTargetDimension", defaultTargetDimension);
		nbttag.setInteger("direction", direction);
	}

	public void setTo(PCtp_TeleporterData td) {
		name = td.name;
		animals = td.animals;
		monsters = td.monsters;
		items = td.items;
		players = td.players;
		sneakTrigger = td.sneakTrigger;
		defaultTarget = td.defaultTarget;
		playerChoose = td.playerChoose;
		defaultTargetDimension = td.defaultTargetDimension;
		direction = td.direction;
		PCtp_TileEntityTeleporter te = GameInfo.getTE(GameInfo.mcs().worldServerForDimension(dimension), pos);
		te.direction = direction;
		PC_PacketHandler.setTileEntity(te, "direction", direction);
		
	}

}
