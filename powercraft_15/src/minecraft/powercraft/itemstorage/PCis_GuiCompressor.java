package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresCheckBox;
import powercraft.api.gres.PC_GresInventory;
import powercraft.api.gres.PC_GresInventoryPlayer;
import powercraft.api.gres.PC_GresLabel;
import powercraft.api.gres.PC_GresTextEdit;
import powercraft.api.gres.PC_GresTextEdit.PC_GresInputType;
import powercraft.api.gres.PC_GresWidget;
import powercraft.api.gres.PC_GresWidgetTab;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGui;
import powercraft.api.tileentity.PC_TileEntity;

public class PCis_GuiCompressor extends PCis_ContainerCompressor implements PC_IGresClient {

	public PC_GresTextEdit name;
	public PC_GresCheckBox takeStacks;
	public PC_GresTextEdit putStacks;
	
	public PCis_GuiCompressor(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PCis_App.compressor.getUnlocalizedName(thePlayer.getCurrentEquippedItem())+".name");
		
		PC_GresWidgetTab wt = new PC_GresWidgetTab(0xBBBBBB, "/gui/items.png", Item.paper.getBlockTextureFromSideAndMetadataFromDamage(0));
		
		wt.add(new PC_GresLabel("pc.gui.compressor.name"));
		wt.add(name = new PC_GresTextEdit(PCis_ItemCompressor.getName(getItem()), 10));
		
		w.add(wt);
		
		wt = new PC_GresWidgetTab(0x684E1E, "/gui/items.png", Item.fishingRod.getBlockTextureFromSideAndMetadataFromDamage(0));
		
		wt.add(takeStacks = new PC_GresCheckBox("pc.gui.compressor.takeStacks"));
		takeStacks.check(PCis_ItemCompressor.isTakeStacks(getItem()));
		wt.add(new PC_GresLabel("pc.gui.compressor.putStacks"));
		wt.add(putStacks = new PC_GresTextEdit(""+PCis_ItemCompressor.getPutStacks(getItem()), 3, PC_GresInputType.UNSIGNED_INT));
		
		w.add(wt);
		
		PC_GresInventory inventory = new PC_GresInventory(inv.getSize().x, inv.getSize().y);
		int i=0;
		for(int y=0; y<inv.getSize().y; y++){
			for(int x=0; x<inv.getSize().x; x++){
				inventory.setSlot(x, y, invSlots[i]);
				i++;
			}
		}
		w.add(inventory);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==name){
			PCis_ItemCompressor.setName(thePlayer, name.getText());
		}else if(widget==takeStacks){
			PCis_ItemCompressor.setTakeStacks(thePlayer, takeStacks.isChecked());
		}else if(widget==putStacks){
			if(putStacks.getText().equals("")){
				PCis_ItemCompressor.setPutStacks(thePlayer, 0);
			}else{
				PCis_ItemCompressor.setPutStacks(thePlayer, Integer.parseInt(putStacks.getText()));
			}
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
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
