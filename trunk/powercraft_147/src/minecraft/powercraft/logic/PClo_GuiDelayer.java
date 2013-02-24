package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Converter;
import powercraft.management.gres.PC_GresButton;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresTextEdit;
import powercraft.management.gres.PC_GresTextEdit.PC_GresInputType;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;

public class PClo_GuiDelayer implements PC_IGresClient {

	private PClo_TileEntityDelayer delayer;
	
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget editDelay;
	private PC_GresWidget txError;
	
	public PClo_GuiDelayer(EntityPlayer player, PC_TileEntity te, Object[] o){
		delayer = (PClo_TileEntityDelayer)te;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("tile.PClo_BlockDelayer.delayer"+delayer.getType()+".name");
		
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresWidget hg, vg;


		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel("pc.gui.delayer.delay"));
		vg.add(editDelay = new PC_GresTextEdit(Converter.doubleToString(Converter.ticksToSecs(delayer.getDelay())), 8, PC_GresInputType.UNSIGNED_FLOAT));
		w.add(vg);


		w.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000).setColor(PC_GresWidget.textColorHover, 0x990000));

		/*
		 * w.add(new PC_GresGap(0,3));
		 * w.add(new PC_GresImage(mod_PClogic.getImgDir()+"pulsar_hint.png", 0, 0, 131, 20));
		 * w.add(new PC_GresGap(0,3));
		 */

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.setAlignH(PC_GresAlign.JUSTIFIED);
		hg.add(buttonCancel = new PC_GresButton("pc.gui.cancel").setId(1));
		hg.add(buttonOK = new PC_GresButton("pc.gui.ok").setId(0));
		w.add(hg);
		
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==buttonOK){
			onReturnPressed(gui);
		}else if(widget==buttonCancel){
			onEscapePressed(gui);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		String sDelay = editDelay.getText();
		if(sDelay!=null && !sDelay.equals("")){
			int delay = Converter.secsToTicks(Double.valueOf(sDelay));
			if(delay>0){
				delayer.setDelay(delay);
				gui.close();
			}else{
				txError.setText("pc.gui.pulsar.errintputzero");
			}
		}else{
			txError.setText("pc.gui.delayer.errnoinput");
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
	public void keyChange(String key, Object value) {
		if(key.equals("stateBuffer")){
			editDelay.setText(""+((boolean[])value).length);
		}
	}

}
