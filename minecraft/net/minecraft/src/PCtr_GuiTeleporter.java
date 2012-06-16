package net.minecraft.src;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class PCtr_GuiTeleporter extends GuiScreen {
	private PCtr_TileEntityTeleporter teleporter;

	private PC_GuiCheckBox checkItems;
	private PC_GuiCheckBox checkAnimals;
	private PC_GuiCheckBox checkMobs;
	private PC_GuiCheckBox checkPlayers;
	private PC_GuiCheckBox checkSneak;


	private PC_GuiCheckBox checkN;
	private PC_GuiCheckBox checkS;
	private PC_GuiCheckBox checkE;
	private PC_GuiCheckBox checkW;

	private String field = "";
	private int type = 0;
	private static final int SENDER = 1, RECEIVER = 2;
	private String error = "";
	private boolean isnew = false;
	private int halfX;
	private int halfY;

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

			PC_Logger.warning("openned gui for invalid device.");
		}
	}

	@Override
	public void updateScreen() {
		inputField.updateCursorCounter();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {

		halfX = width / 2;
		halfY = height / 2;

		Keyboard.enableRepeatEvents(true);
		controlList.clear();

		PC_GuiButtonAligner.alignSingleToRight(controlList, 0, "pc.gui.ok", 60, halfY + 75 - 30, halfX + 110);
		PC_GuiButtonAligner.alignSingleToRight(controlList, 1, "pc.gui.cancel", 60, halfY + 75 - 30 - 23, halfX + 110);


		inputField = new GuiTextField(fontRenderer, halfX - 100, halfY - 75 + 36, 190, 20);
		inputField.setText(field);
		inputField.setFocused(true);
		inputField.setMaxStringLength(20);

		((GuiButton) controlList.get(0)).enabled = (field.length() > 0);

		checkItems = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3, halfY - 75 + 2 + 85, teleporter.items,
				PC_Lang.tr("pc.gui.teleporter.items"));
		checkAnimals = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3, halfY - 75 + 85 + 12, teleporter.animals,
				PC_Lang.tr("pc.gui.teleporter.animals"));
		checkMobs = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3 + 70, halfY - 75 + 2 + 85, teleporter.monsters,
				PC_Lang.tr("pc.gui.teleporter.monsters"));
		checkPlayers = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3 + 70, halfY - 75 + 2 + 85 + 12, teleporter.players,
				PC_Lang.tr("pc.gui.teleporter.players"));

		checkSneak = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3, halfY - 75 + 2 + 85 + 12 + 12 + 5, teleporter.sneakTrigger,
				PC_Lang.tr("pc.gui.teleporter.sneak"));

		checkN = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3, halfY - 75 + 2 + 85, teleporter.direction.equals("N"),
				PC_Lang.tr("pc.gui.teleporter.dir.north"));
		checkS = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3, halfY - 75 + 2 + 85 + 12, teleporter.direction.equals("S"),
				PC_Lang.tr("pc.gui.teleporter.dir.south"));
		checkE = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3 + 70, halfY - 75 + 2 + 85, teleporter.direction.equals("E"),
				PC_Lang.tr("pc.gui.teleporter.dir.east"));
		checkW = new PC_GuiCheckBox(this, fontRenderer, halfX - 100 + 3 + 70, halfY - 75 + 2 + 85 + 12, teleporter.direction.equals("W"),
				PC_Lang.tr("pc.gui.teleporter.dir.west"));

		ArrayList<PC_GuiCheckBox> foo = new ArrayList<PC_GuiCheckBox>();
		foo.add(checkN);
		foo.add(checkS);
		foo.add(checkE);
		foo.add(checkW);

		checkN.radios = checkS.radios = checkE.radios = checkW.radios = foo;

	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }
		if (guibutton.id == 1) {
			// remove the gui
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else if (guibutton.id == 0) {

			if (!inputField.getText().equals("")) {
				if (type == SENDER) {
					teleporter.target = new String(inputField.getText());

					PC_Logger.finest("setting target to " + inputField.getText());
				} else {

					if (isnew) {
						PCtr_TeleporterHelper.registerNewDevice(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord,
								inputField.getText());
					} else {
						PCtr_TeleporterHelper.renameDevice(teleporter.identifier, inputField.getText());
					}

					teleporter.identifier = new String(inputField.getText());
					PC_Logger.finest("setting id to " + inputField.getText());

				}

			} else {
				return;
			}

			teleporter.items = checkItems.isChecked();
			teleporter.animals = checkAnimals.isChecked();
			teleporter.monsters = checkMobs.isChecked();
			teleporter.players = checkPlayers.isChecked();
			teleporter.sneakTrigger = checkSneak.isChecked();

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

			teleporter.onInventoryChanged();

			teleporter.worldObj.markBlocksDirty(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord, teleporter.xCoord,
					teleporter.yCoord, teleporter.zCoord);
			teleporter.worldObj.markBlockNeedsUpdate(teleporter.xCoord, teleporter.yCoord, teleporter.zCoord);

			mc.displayGuiScreen(null);
			mc.setIngameFocus();

		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (PC_KeyUtils.filterKeyFilename(c, i)) {
			inputField.textboxKeyTyped(c, i);
		}

		boolean valid = true;
		if (type == RECEIVER) {
			if (PCtr_TeleporterHelper.targetExistsExcept(inputField.getText(), teleporter.getCoord())) {
				error = PC_Lang.tr("pc.gui.teleporter.errIdUsed");
				valid = false;
			} else if (inputField.getText().equals("")) {
				error = PC_Lang.tr("pc.gui.teleporter.errIdRequired");
				valid = false;
			} else {
				error = "";
			}
		} else if (type == SENDER) {
			if (!PCtr_TeleporterHelper.targetExistsExcept(inputField.getText(), teleporter.getCoord())) {
				error = PC_Lang.tr("pc.gui.teleporter.errIdNotFound");
			} else if (!PCtr_TeleporterHelper.isTargetInThisDimension(inputField.getText())) {

				int dim = PCtr_TeleporterHelper.getTargetDimension(inputField.getText());
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

			} else if (inputField.getText().equals("")) {
				error = PC_Lang.tr("pc.gui.teleporter.errTargetRequired");
				valid = false;
			} else {
				error = "";
			}
		}

		if (inputField.getText().equals("")) {
			valid = false;
		}

		((GuiButton) controlList.get(0)).enabled = valid;

		if (c == '\r') {
			actionPerformed((GuiButton) controlList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		inputField.mouseClicked(i, j, k);

		if (teleporter.isSender()) {
			checkItems.mouseClicked(i, j, k);
			checkAnimals.mouseClicked(i, j, k);
			checkMobs.mouseClicked(i, j, k);
			checkPlayers.mouseClicked(i, j, k);
			checkSneak.mouseClicked(i, j, k);
		}

		if (teleporter.isReceiver()) {
			checkN.mouseClicked(i, j, k);
			checkS.mouseClicked(i, j, k);
			checkE.mouseClicked(i, j, k);
			checkW.mouseClicked(i, j, k);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiBackgroundLayer(f);

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String aa = "";
		if (type == SENDER) {
			aa = PC_Lang.tr("pc.gui.teleporter.titleSender");
		}
		if (type == RECEIVER) {
			aa = PC_Lang.tr("pc.gui.teleporter.titleTarget");
		}

		fontRenderer.drawString(aa, halfX - (fontRenderer.getStringWidth(aa) / 2), halfY - 75 + 10, 0x000000);

		fontRenderer.drawString(type == SENDER ? PC_Lang.tr("pc.gui.teleporter.linksTo") : PC_Lang.tr("pc.gui.teleporter.deviceId"),
				halfX - 100, halfY - 75 + 24, 0x404040);

		if (!error.equals("")) {
			fontRenderer.drawString(error, halfX - 100, halfY - 75 + 61, 0x990000);
		}

		inputField.drawTextBox();

		if (teleporter.isSender()) {
			fontRenderer.drawString(PC_Lang.tr("pc.gui.teleporter.teleportGroup"), halfX - 100, halfY - 75 + 75, 0x404040);
			checkItems.drawCheckBox();
			checkAnimals.drawCheckBox();
			checkMobs.drawCheckBox();
			checkPlayers.drawCheckBox();
			checkSneak.drawCheckBox();
		}

		if (teleporter.isReceiver()) {
			fontRenderer.drawString(PC_Lang.tr("pc.gui.teleporter.outputDirection"), halfX - 100, halfY - 75 + 75, 0x404040);
			checkN.drawCheckBox();
			checkS.drawCheckBox();
			checkE.drawCheckBox();
			checkW.drawCheckBox();
		}

		GL11.glPopMatrix();

		super.drawScreen(i, j, f);

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-medium.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width) / 2;
		int k = (height) / 2;
		drawTexturedModalRect(j - 120, k - 75, 0, 0, 240, 150);
	}

	private GuiTextField inputField;
}
