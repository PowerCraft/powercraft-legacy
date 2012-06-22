package net.minecraft.src;

import java.util.ArrayList;

import net.minecraft.src.PC_GresRadioButton.PC_GresRadioGroup;
import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class PCtr_GuiTeleporter implements PC_IGresBase {
	
	private PCtr_TileEntityTeleporter teleporter;

	private PC_GresCheckBox checkItems;
	private PC_GresCheckBox checkAnimals;
	private PC_GresCheckBox checkMobs;
	private PC_GresCheckBox checkPlayers;
	private PC_GresCheckBox checkSneak;


	private PC_GresRadioButton checkN;
	private PC_GresRadioButton checkS;
	private PC_GresRadioButton checkE;
	private PC_GresRadioButton checkW;

	private String field = "";
	private int type = 0;
	private static final int SENDER = 1, RECEIVER = 2;
	private String error = "";
	private boolean isnew = false;

	private PC_GresTextEdit edit;

	private PC_GresWidget buttonCancel;
	private PC_GresWidget buttonOK;

	private PC_GresWidget txError;


	public PCtr_GuiTeleporter(PCtr_TileEntityTeleporter te) {
		this(te, false);
	}

	public PCtr_GuiTeleporter(PCtr_TileEntityTeleporter te, boolean newt) {
		isnew = newt;
		teleporter = te;

		if (teleporter.isSender()) {

			type = SENDER;
			field = new String(teleporter.target);

		} else if (teleporter.isReceiver()) {

			type = RECEIVER;
			field = new String(teleporter.identifier);

		} else {

			PC_Logger.warning("openned gui for invalid teleporter device.");
		}
	}
	
	
	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		
		String aa = "";
		if (type == SENDER) {
			aa = PC_Lang.tr("pc.gui.teleporter.titleSender");
		}
		if (type == RECEIVER) {
			aa = PC_Lang.tr("pc.gui.teleporter.titleTarget");
		}
		
		PC_GresWidget w = new PC_GresWindow(aa);
		PC_GresWidget hg;
		PC_GresWidget vg,vg1;
		
		vg= new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(type == SENDER ? PC_Lang.tr("pc.gui.teleporter.linksTo") : PC_Lang.tr("pc.gui.teleporter.deviceId")));
		vg.add(edit = new PC_GresTextEdit(field, 20, PC_GresInputType.TEXT));
		w.add(vg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));
		w.add(hg);
		
		
		if(type==SENDER){
			
			
				
			
			vg1 = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
			vg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.teleporter.teleportGroup")));
			hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
			
			vg = new PC_GresLayoutV().setMinWidth(100).setAlignH(PC_GresAlign.LEFT);
			vg.add(checkItems = new PC_GresCheckBox(PC_Lang.tr("pc.gui.teleporter.items")).check(teleporter.items));
			vg.add(checkAnimals = new PC_GresCheckBox(PC_Lang.tr("pc.gui.teleporter.animals")).check(teleporter.animals));
			hg.add(vg);
			
			vg = new PC_GresLayoutV().setMinWidth(100).setAlignH(PC_GresAlign.LEFT);
			vg.add(checkMobs = new PC_GresCheckBox(PC_Lang.tr("pc.gui.teleporter.monsters")).check(teleporter.monsters));
			vg.add(checkPlayers = new PC_GresCheckBox(PC_Lang.tr("pc.gui.teleporter.players")).check(teleporter.players));
			hg.add(vg);
			
			vg1.add(hg);
			
			vg1.add(checkSneak = new PC_GresCheckBox(PC_Lang.tr("pc.gui.teleporter.sneak")).check(teleporter.sneakTrigger));
			w.add(vg1);
			
		}else if(type == RECEIVER){
			
			PC_GresRadioGroup group = new PC_GresRadioGroup();
			
			vg1 = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
			vg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.teleporter.outputDirection")));
			hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
			
			vg = new PC_GresLayoutV().setMinWidth(100).setAlignH(PC_GresAlign.LEFT);
			vg.add(checkN = new PC_GresRadioButton(PC_Lang.tr("pc.gui.teleporter.dir.north"), group).check(teleporter.direction.equals("N")));
			vg.add(checkS = new PC_GresRadioButton(PC_Lang.tr("pc.gui.teleporter.dir.south"), group).check(teleporter.direction.equals("S")));
			hg.add(vg);
			
			vg = new PC_GresLayoutV().setMinWidth(100).setAlignH(PC_GresAlign.LEFT);
			vg.add(checkE = new PC_GresRadioButton(PC_Lang.tr("pc.gui.teleporter.dir.east"), group).check(teleporter.direction.equals("E")));
			vg.add(checkW = new PC_GresRadioButton(PC_Lang.tr("pc.gui.teleporter.dir.west"), group).check(teleporter.direction.equals("W")));
			hg.add(vg);
			
			vg1.add(hg);
			w.add(vg1);			
			
		}
		
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		
		gui.add(w);
		gui.setCanShiftTransfer(false);
		
		actionPerformed(edit, gui);
		
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget == buttonCancel) {
			
			gui.close();
			
		} else if (widget == buttonOK) {

			if (!edit.getText().equals("")) {
				if (type == SENDER) {
					teleporter.target = new String(edit.getText());

					PC_Logger.finest("setting target to " + edit.getText());
				} else {

					if (isnew) {
						PCtr_TeleporterHelper.registerNewDevice(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord,
								edit.getText());
					} else {
						PCtr_TeleporterHelper.renameDevice(teleporter.identifier, edit.getText());
					}

					teleporter.identifier = new String(edit.getText());
					PC_Logger.finest("setting id to " + edit.getText());

				}

			} else {
				return;
			}

			if(type==SENDER){
				teleporter.items = checkItems.isChecked();
				teleporter.animals = checkAnimals.isChecked();
				teleporter.monsters = checkMobs.isChecked();
				teleporter.players = checkPlayers.isChecked();
				teleporter.sneakTrigger = checkSneak.isChecked();
			}else if(type==RECEIVER){	
				if (checkN.isChecked()) {
					teleporter.direction = "N";
				}
				if (checkS.isChecked()) {
					teleporter.direction = "S";
				}
				if (checkE.isChecked()) {
					teleporter.direction = "E";
				}
				if (checkW.isChecked()) {
					teleporter.direction = "W";
				}
			}

			teleporter.onInventoryChanged();

			teleporter.worldObj.markBlocksDirty(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord, teleporter.xCoord,
					teleporter.yCoord, teleporter.zCoord);
			teleporter.worldObj.markBlockNeedsUpdate(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord);

			gui.close();
			
		} else if(widget == edit){
			
			boolean valid = true;
			
			if (type == RECEIVER) {
				if (PCtr_TeleporterHelper.targetExistsExcept(edit.getText(), teleporter.getCoord())) {
					error = PC_Lang.tr("pc.gui.teleporter.errIdUsed");
					valid = false;
				} else if (edit.getText().equals("")) {
					error = PC_Lang.tr("pc.gui.teleporter.errIdRequired");
					valid = false;
				} else {
					error = "";
				}
			} else if (type == SENDER) {
				if (!PCtr_TeleporterHelper.targetExistsExcept(edit.getText(), teleporter.getCoord())) {
					error = PC_Lang.tr("pc.gui.teleporter.errIdNotFound");
				} else if (!PCtr_TeleporterHelper.isTargetInThisDimension(edit.getText())) {

					int dim = PCtr_TeleporterHelper.getTargetDimension(edit.getText());
					switch (dim) {
						case 1:
							error = PC_Lang.tr("pc.gui.teleporter.errIdDimEnd");
							break;

						case 0:
							error = PC_Lang.tr("pc.gui.teleporter.errIdDimWorld");
							break;

						case -1:
							error = PC_Lang.tr("pc.gui.teleporter.errIdDimnether");
							break;

						default:
							error = PC_Lang.tr("pc.gui.teleporter.errIdDim");
					}
					valid = false;

				} else if (edit.getText().equals("")) {
					error = PC_Lang.tr("pc.gui.teleporter.errTargetRequired");
					valid = false;
				} else {
					error = "";
				}
			}

			if (edit.getText().equals("")) {
				valid = false;
			}

			buttonOK.enable(valid);
			txError.setText(error);

		}
		
		
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		actionPerformed(buttonCancel, gui);
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

}
