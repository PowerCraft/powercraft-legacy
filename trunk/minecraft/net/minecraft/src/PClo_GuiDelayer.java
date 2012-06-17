package net.minecraft.src;

import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Gui for delayer and repeater gates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiDelayer implements PC_IGresBase {

	private PClo_TileEntityGate gateTE;

	private int ticks;
	private boolean error = false;
	private String errMsg = ""; 

	private boolean delayer_type;
	private static final boolean FIFO = true, HOLD = false;
	
	private PC_GresButton buttonOK, buttonCancel;
	private PC_GresTextEdit edit;
	
	/**
	 * @param tep Gate tile entity
	 * @param fifo is the delayer of type FIFO (buffered)?
	 */
	public PClo_GuiDelayer(PClo_TileEntityGate tep, boolean fifo) {
		gateTE = tep;
		ticks = fifo ? gateTE.getDelayBufferLength() : gateTE.repeaterGetHoldTime();
		delayer_type = fifo;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		String title="";
		if(delayer_type == FIFO) title = PC_Lang.tr("tile.PCloLogicGate.buffer.name");
		if(delayer_type == HOLD)  title = PC_Lang.tr("tile.PCloLogicGate.slowRepeater.name");
		
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresLayoutH hg;
		
		//hg = new PC_GresLayoutH();
		//hg.setAlignH(PC_GresAlign.CENTER);
		
		PC_GresLayoutV vg = (PC_GresLayoutV) new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT).setAlignV(PC_GresAlign.TOP);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.gate.delay")));
		vg.add(edit = new PC_GresTextEdit(PC_Utils.floatToString(ticks * 0.05F), 8, PC_GresInputType.SIGNED_FLOAT));
		//hg.add(vg);
		w.add(vg);
		
		
		
		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = (PC_GresButton) new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = (PC_GresButton) new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		
		gui.add(w);
		
		gui.setPausesGame(true);
		
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		
		if(widget.getId() == 0){
			
			if (delayer_type == FIFO) {
				gateTE.bufferResize(ticks);
			} else if (delayer_type == HOLD) {
				gateTE.setRepeaterHoldTime(ticks);
			}
			
			gui.close();
			
		}else if(widget.getId() == 1){
			try {
				Desktop.getDesktop().browse(
						URI.create("http://www.minecraftforum.net/topic/842589-125-power-craft-factory-mod/#entry10831808"));
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		
		if(widget == edit){
			try {
				double time = Double.parseDouble(fieldLength.getText());

				ticks = PC_Utils.secsToTicks(time);

				error = (ticks < 2) || (ticks > 37000);
				errMsg = "pc.gui.gate.delayer.errRange";

				buttonOK.enabled = !error;

			} catch (NumberFormatException nfe) {

				buttonOK.enabled = false;
				error = true;
				errMsg = "pc.gui.gate.delayer.errNumFormat";

			} catch (NullPointerException npe) {

				buttonOK.enabled = false;
				error = true;

				errMsg = "pc.gui.gate.delayer.errNumFormat";
			}
			
			if(!error) errMsg = "";
		}
		
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}
	
	
	

	
	

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		fieldLength.mouseClicked(i, j, k);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiRadioBackgroundLayer(f);

		GL11.glPushMatrix();
		GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String title = ;

		fontRenderer.drawString(title, width / 2 - (fontRenderer.getStringWidth(title) / 2), (height / 2 - 50) + 20, 0x000000);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.gate.delay"), width / 2 - 50, (height / 2 - 50) + 35, 0x404040);

		if (!error) {
			fontRenderer.drawString("= " + ticks + " t.", width / 2 + 60, height / 2 + 3, 0x606060);
			int secs = PC_Utils.ticksToSecsInt(ticks);
			if (secs >= 60) {
				fontRenderer.drawString("= " + PC_Utils.formatTimeSecs(secs), width / 2 + 60, height / 2 + 3 + 16, 0x606060);
			}

		} else {
			fontRenderer.drawString(PC_Lang.tr("pc.gui.gate.invalid"), width / 2 + 60, height / 2 + 3, 0x990000);
		}

		fieldLength.drawTextBox();

		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiRadioBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-small.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - 100) / 2;
		int k = (height - 50) / 2;
		drawTexturedModalRect(j - 100 + 30, k - 50 + 30 + 5, 0, 0, 240, 100);
	}

	private GuiTextField fieldLength;
}
