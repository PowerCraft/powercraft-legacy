package powercraft.core;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityMobSpawner;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresLayoutV;
import powercraft.api.gres.PC_GresRadioButton;
import powercraft.api.gres.PC_GresRadioButton.PC_GresRadioGroup;
import powercraft.api.gres.PC_GresScrollArea;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWidget.PC_GresAlign;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.network.PC_PacketHandler;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.tileentity.PC_TileEntity;

public class PCco_GuiSpawnerEditor implements PC_IGresClient {

	private TileEntityMobSpawner tems;
	private List<String> entityIds = new ArrayList<String>();
	private EntityPlayer thePlayer;
	
	public PCco_GuiSpawnerEditor(EntityPlayer player, PC_TileEntity te, Object[] o){
		tems = (TileEntityMobSpawner)player.worldObj.getBlockTileEntity((Integer)o[0], (Integer)o[1], (Integer)o[2]);
		thePlayer = player;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(230, 100, "tile.mobSpawner.name").setAlignH(PC_GresAlign.STRETCH);
		
		PC_GresScrollArea sa = new PC_GresScrollArea(PC_GresScrollArea.VSCROLL);
		sa.setSize(0, 100);
		
		PC_GresLayoutV lv = new PC_GresLayoutV();
		lv.setAlignH(PC_GresAlign.LEFT);
		
		PC_GresRadioGroup rg = new PC_GresRadioGroup();
		
		Map<Class, String> c2s = PC_ReflectHelper.getValue(EntityList.class, EntityList.class, 1, Map.class);
		Entry<Class, String>[] se = c2s.entrySet().toArray(new Entry[0]);
		Arrays.sort(se,  new Comparator<Entry<Class, String>>(){

			@Override
			public int compare(Entry<Class, String> arg0, Entry<Class, String> arg1) {
				return PC_LangRegistry.tr(arg0.getValue()).compareToIgnoreCase(PC_LangRegistry.tr(arg1.getValue()));
			}
			
		});
		
		for(Entry<Class, String> e: se){
			Class mob = e.getKey();
			if((mob.getModifiers() & Modifier.ABSTRACT)==0 && EntityLiving.class.isAssignableFrom(mob) && !EntityPlayer.class.isAssignableFrom(mob)){
				PC_GresRadioButton rb = new PC_GresRadioButton(e.getValue(), rg);
				entityIds.add(e.getValue());
				rb.setId(entityIds.size());
				if(e.getValue().equalsIgnoreCase(tems.func_98049_a().getEntityNameToSpawn()))
					rb.check(true);
				lv.add(rb);
			}
		}
		
		sa.setChild(lv);
		
		w.add(sa);
		
		gui.add(w);
	}
	
	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
		int id = widget.getId()-1;
		if(id>=0){
			PC_PacketHandler.sendToPacketHandler(thePlayer.worldObj, "MobSpawner", tems.xCoord, tems.yCoord, tems.zCoord, entityIds.get(id));
			new PCco_ClientMobSpawnerSetter().handleIncomingPacket(thePlayer, new Object[]{tems.xCoord, tems.yCoord, tems.zCoord, entityIds.get(id)});
		}
		
	}

	@Override
	public void onKeyPressed(PC_IGresGui gui, char c, int i) {
		if(i==Keyboard.KEY_RETURN || i==Keyboard.KEY_ESCAPE || i==Keyboard.KEY_E){
			gui.close();
		}
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
