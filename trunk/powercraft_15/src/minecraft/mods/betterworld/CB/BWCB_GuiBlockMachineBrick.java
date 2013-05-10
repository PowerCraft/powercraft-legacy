package mods.betterworld.CB;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;


public class BWCB_GuiBlockMachineBrick extends GuiContainer {
	private BWCB_TileEntityBlockMachineBrick machineInv;
	private int test;

	public BWCB_GuiBlockMachineBrick(InventoryPlayer var1, BWCB_TileEntityBlockMachineBrick var2) {
		super(new BWCB_ContainerBrickMachine(var1, var2));
		this.machineInv = var2;
	}
    @Override
    public void initGui() {
            super.initGui();
            //make buttons
                                    //id, x, y, width, height, text
            this.buttonList.add(new GuiButton(1, 130, 60, 20, 40, "Stone"));
            this.buttonList.add(new GuiButton(2, 130, 90, 20, 40, "Glass"));
            this.buttonList.add(new GuiButton(3, 180, 60, 20, 40, "Normal"));
            this.buttonList.add(new GuiButton(4, 240, 60, 20, 40, "Resistant"));
            this.buttonList.add(new GuiButton(5, 140, 110, 20, 20, "<"));
            this.buttonList.add(new GuiButton(6, 260, 110, 20, 20, ">"));
    }

    protected void actionPerformed(GuiButton guibutton) {
            //id is the id you give your button
            switch(guibutton.id) {
            case 3:
                    test += 1;
                    break;
            case 4:
                    test -= 1;
            }
            //Packet code here
            //PacketDispatcher.sendPacketToServer(packet); //send packet
    }
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString("Brick Machine", 99, 6, 4210752);
		this.fontRenderer.drawString("Inventory", 117, 108, 4210752);
	}



	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mods/betterworld/CB/textures/Gui/BrickMachine.png");
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, 175, 200);
		int var7;

	}
}