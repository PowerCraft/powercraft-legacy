package powercraft.teleport;

import java.io.Serializable;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Entry;
import powercraft.management.PC_INBT;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.SaveHandler;

public class PCtp_TeleporterData implements PC_INBT<PCtp_TeleporterData>, Serializable {

	public static final long serialVersionUID = 3200035481822302060L;
	
	public static final int N=0, E=1, S=2, W=3;
	
	public String name;
	public PC_VecI pos;
	public int dimension;
	public boolean animals;
	public boolean monsters;
	public boolean items;
	public boolean players;
	public boolean lasers;
	public boolean sneakTrigger;
	public boolean playerChoose;
	public boolean soundEnabled;
	public PC_VecI defaultTarget;
	public int defaultTargetDimension;
	public int direction;
	
	public PCtp_TeleporterData(){
		name="";
		animals = true;
		monsters = true;
		items = true;
		players = true;
		lasers = true;
		soundEnabled = true;
	}
	
	@Override
	public PCtp_TeleporterData readFromNBT(NBTTagCompound nbttag) {
		pos = new PC_VecI();
		name = nbttag.getString("name");
		SaveHandler.loadFromNBT(nbttag, "pos", pos);
		dimension = nbttag.getInteger("dimension");
		animals = nbttag.getBoolean("animals");
		monsters = nbttag.getBoolean("monsters");
		items = nbttag.getBoolean("items");
		players = nbttag.getBoolean("players");
		lasers = nbttag.getBoolean("lasers");
		sneakTrigger = nbttag.getBoolean("sneakTrigger");
		playerChoose = nbttag.getBoolean("playerChoose");
		soundEnabled = nbttag.getBoolean("soundEnabled");
		if(nbttag.hasKey("defaultTarget")){
			defaultTarget = new PC_VecI();
			SaveHandler.loadFromNBT(nbttag, "defaultTarget", defaultTarget);
		}else{
			defaultTarget = null;
		}
		defaultTargetDimension = nbttag.getInteger("defaultTargetDimension");
		direction = nbttag.getInteger("direction");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setString("name", name);
		SaveHandler.saveToNBT(nbttag, "pos", pos);
		nbttag.setInteger("dimension", dimension);
		nbttag.setBoolean("animals", animals);
		nbttag.setBoolean("monsters", monsters);
		nbttag.setBoolean("items", items);
		nbttag.setBoolean("players", players);
		nbttag.setBoolean("lasers", lasers);
		nbttag.setBoolean("sneakTrigger", sneakTrigger);
		nbttag.setBoolean("playerChoose", playerChoose);
		nbttag.setBoolean("soundEnabled", soundEnabled);
		if(defaultTarget!=null)
			SaveHandler.saveToNBT(nbttag, "defaultTarget", defaultTarget);
		nbttag.setInteger("defaultTargetDimension", defaultTargetDimension);
		nbttag.setInteger("direction", direction);
		return nbttag;
	}

	public void setTo(PCtp_TeleporterData td) {
		name = td.name;
		animals = td.animals;
		monsters = td.monsters;
		items = td.items;
		players = td.players;
		lasers = td.lasers;
		sneakTrigger = td.sneakTrigger;
		defaultTarget = td.defaultTarget;
		playerChoose = td.playerChoose;
		soundEnabled = td.soundEnabled;
		defaultTargetDimension = td.defaultTargetDimension;
		direction = td.direction;
		PCtp_TileEntityTeleporter te = GameInfo.getTE(GameInfo.mcs().worldServerForDimension(dimension), pos);
		te.direction = direction;
		te.soundEnabled = soundEnabled;
		te.defaultTarget = defaultTarget;
		PCtp_TeleporterData otherTPData=null;
		if(defaultTarget != null)
			otherTPData = PCtp_TeleporterManager.getTeleporterData(defaultTargetDimension, defaultTarget);
		if(otherTPData!=null){
			te.defaultTargetDirection = otherTPData.direction;
		}else{
			te.defaultTarget = null;
			te.defaultTargetDirection = 0;
		}
		
		PC_PacketHandler.setTileEntity(te, new PC_Entry("direction", direction), new PC_Entry("soundEnabled", soundEnabled), new PC_Entry("defaultTarget", defaultTarget), new PC_Entry("defaultTargetDirection", te.defaultTargetDirection));
		
	}

}
