package powercraft.light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Color;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresCheckBox;
import powercraft.management.gres.PC_GresColor;
import powercraft.management.gres.PC_GresColorPicker;
import powercraft.management.gres.PC_GresItemToggel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidgetTab;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;

public class PCli_GuiLight implements PC_IGresClient {

	private PCli_TileEntityLight light;
	
	private PC_GresCheckBox checkHuge, checkStable;
	private PC_GresColor colorWidget;
	private PC_GresColorPicker colorPicker;
	private PC_GresButton accept, cancel;
	
	public PCli_GuiLight(EntityPlayer player, PC_TileEntity te, Object[] o){
		light = (PCli_TileEntityLight)te;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("tile.PCli_BlockLight.name");
		
		PC_GresWidget v = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH);
		
		PC_GresWidget h = new PC_GresLayoutH().setAlignH(PC_GresAlign.JUSTIFIED);
		h.add(checkHuge = new PC_GresCheckBox("pc.gui.light.isHuge").check(light.isHuge()));
		h.add(checkStable = new PC_GresCheckBox("pc.gui.light.isStable").check(light.isStable()));
		v.add(h);
		
		h = new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH);
		h.add(colorWidget = new PC_GresColor(light.getColor()));
		h.add(colorPicker = new PC_GresColorPicker(light.getColor().getHex(), 100, 20));
		v.add(h);
		
		h = new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH);;
		h.add(accept = new PC_GresButton("pc.gui.ok"));
		h.add(cancel = new PC_GresButton("pc.gui.cancel"));
		v.add(h);
		w.add(v);
		gui.add(w);

	}


	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget == colorPicker){
			colorWidget.setColor(colorPicker.getColor());
		}else if(widget == accept){
			onReturnPressed(gui);
		}else if (widget == cancel) {
			gui.close();
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();

	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		light.setColor(PC_Color.fromHex(colorPicker.getColor()));
		light.setHuge(checkHuge.isChecked());
		light.setStable(checkStable.isChecked());
		gui.close();
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
	public void keyChange(String key, Object value) {
		if(key.equals("color")){
			colorPicker.setColor(((PC_Color)value).getHex());
			colorWidget.setColor(((PC_Color)value).copy());
		}else if(key.equals("isStable")){
			checkStable.check((Boolean)value);
		}else if(key.equals("isHuge")){
			checkHuge.check((Boolean)value);
		}
	}

}
