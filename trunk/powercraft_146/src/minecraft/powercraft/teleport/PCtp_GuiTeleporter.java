package powercraft.teleport;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresRadioButton;
import powercraft.management.PC_GresRadioButton.PC_GresRadioGroup;
import powercraft.management.PC_GresScrollArea;
import powercraft.management.PC_GresTab;
import powercraft.management.PC_GresTextEdit;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_VecI;

public class PCtp_GuiTeleporter implements PC_IGresClient {

	private EntityPlayer player;
	private List<String> names;
	private String defaultTarget;
	private PCtp_TeleporterData td;
	private PC_GresButton ok;
	private PC_GresTextEdit name;
	
	private PC_GresRadioGroup rg;
	
	private PC_GresCheckBox animals;
	private PC_GresCheckBox monsters;
	private PC_GresCheckBox items;
	private PC_GresCheckBox players;
	private PC_GresCheckBox sneakTrigger;
	
	private PC_GresRadioGroup dir;
	
	public PCtp_GuiTeleporter(EntityPlayer player, Object[]o) {
		this.player = player;
		td = (PCtp_TeleporterData)o[0];
		names = (List<String>)o[1];
		defaultTarget = (String)o[2];
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = (new PC_GresWindow(Lang.tr("tile.PCtp_BlockTeleporter.name")));
		
		PC_GresTab t = new PC_GresTab();
		
		PC_GresWidget vg = new PC_GresLayoutV();
		PC_GresWidget hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel(Lang.tr("pc.gui.teleporter.name")));
		hg.add(name = new PC_GresTextEdit(td.name, 10));
		vg.add(hg);
		
		rg = new PC_GresRadioGroup();
		
		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel(Lang.tr("pc.gui.teleporter.target")));
		PC_GresWidget sa = new PC_GresLayoutV();
		for(String name:names){
			if(!name.equals(td.name)){
				PC_GresRadioButton rb = new PC_GresRadioButton(name, rg);
				if(name.equals(defaultTarget))
					rb.check(true);
				sa.add(rb);
			}
		}
		PC_GresRadioButton rb = new PC_GresRadioButton(Lang.tr("pc.gui.teleporter.nothing"), rg);
		if(defaultTarget==null||defaultTarget.equals(""))
			rb.check(true);
		sa.add(rb);
		hg.add(new PC_GresScrollArea(0, 100, sa, PC_GresScrollArea.VSCROLL));
		vg.add(hg);
		t.addTab(vg, new PC_GresLabel(Lang.tr("pc.gui.teleporter.page1")));
		
		vg = new PC_GresLayoutV();
		
		vg.add(animals = new PC_GresCheckBox(Lang.tr("pc.gui.teleporter.animals")));
		animals.check(td.animals);
		vg.add(monsters = new PC_GresCheckBox(Lang.tr("pc.gui.teleporter.monsters")));
		monsters.check(td.monsters);
		vg.add(items = new PC_GresCheckBox(Lang.tr("pc.gui.teleporter.items")));
		items.check(td.items);
		vg.add(players = new PC_GresCheckBox(Lang.tr("pc.gui.teleporter.players")));
		players.check(td.players);
		vg.add(sneakTrigger = new PC_GresCheckBox(Lang.tr("pc.gui.teleporter.sneakTrigger")));
		sneakTrigger.check(td.sneakTrigger);
		
		dir = new PC_GresRadioGroup();
		rb = new PC_GresRadioButton(Lang.tr("pc.gui.teleporter.north"), dir);
		rb.check(td.direction == PCtp_TeleporterData.N);
		rb.setId(PCtp_TeleporterData.N);
		vg.add(rb);
		rb = new PC_GresRadioButton(Lang.tr("pc.gui.teleporter.east"), dir);
		rb.check(td.direction == PCtp_TeleporterData.E);
		rb.setId(PCtp_TeleporterData.E);
		vg.add(rb);
		rb = new PC_GresRadioButton(Lang.tr("pc.gui.teleporter.south"), dir);
		rb.check(td.direction == PCtp_TeleporterData.S);
		rb.setId(PCtp_TeleporterData.S);
		vg.add(rb);
		rb = new PC_GresRadioButton(Lang.tr("pc.gui.teleporter.west"), dir);
		rb.check(td.direction == PCtp_TeleporterData.W);
		rb.setId(PCtp_TeleporterData.W);
		vg.add(rb);
		
		t.addTab(vg, new PC_GresLabel(Lang.tr("pc.gui.teleporter.page2")));
		
		w.add(t);
		w.add(ok = new PC_GresButton(Lang.tr("pc.gui.ok")));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==name){
			ok.enable(!names.contains(name.getText())||name.getText().equals(td.name));
		}else if(widget==ok){
			onReturnPressed(gui);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		if(ok.isEnabled()){
			PC_GresRadioButton rb = rg.getChecked();
			String target="";
			if(rb!=null)
				target = rb.getText();
			if(target.equals(Lang.tr("pc.gui.teleporter.nothing")))
				target = "";
			td.name = name.getText();
			td.animals = animals.isChecked();
			td.monsters = monsters.isChecked();
			td.items = items.isChecked();
			td.players = players.isChecked();
			td.sneakTrigger = sneakTrigger.isChecked();
			td.direction = this.dir.getChecked().getId();
			PC_PacketHandler.sendToPacketHandler(player.worldObj, "Teleporter", "set", td, target);
			gui.close();
		}
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

}
