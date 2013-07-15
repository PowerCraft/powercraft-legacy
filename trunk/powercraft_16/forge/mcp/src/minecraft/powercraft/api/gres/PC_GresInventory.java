package powercraft.api.gres;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;

public class PC_GresInventory extends PC_GresComponent {

	protected static final String textureName = "Slot";
	
	protected static final RenderItem itemRenderer = new RenderItem();
	
	protected Slot slots[][];
	
	protected int slotWidth = 0;
	
	protected int slotHeight = 0;
	
	public PC_GresInventory(int width, int height) {

		slotWidth = 18;
		slotHeight = 18;

		slots = new Slot[width][height];
	}
	
	public PC_GresInventory(int width, int height, int slotWidth, int slotHeight) {

		this.slotWidth = slotWidth;
		this.slotHeight = slotHeight;
		
		slots = new Slot[width][height];
	}

	public PC_GresInventory setSlot(int x, int y, Slot slot) {
		if (x >= 0 && x < this.slots.length && y >= 0 && y < this.slots[x].length) {
			this.slots[x][y] = slot;
		}
		return this;
	}

	public Slot getSlot(int x, int y) {
		if (x >= 0 && x < this.slots.length && y >= 0 && y < this.slots[x].length) {
			return this.slots[x][y];
		}
		return null;
	}
	
	@Override
	protected PC_Vec2I calculateMinSize() {
		return calculatePrefSize();
	}

	@Override
	protected PC_Vec2I calculateMaxSize() {
		return calculatePrefSize();
	}

	@Override
	protected PC_Vec2I calculatePrefSize() {
		return new PC_Vec2I(slots.length * slotWidth, slots[0].length * slotHeight);
	}

	@Override
	protected void paint(PC_RectI scissor, float timeStamp) {
		for (int x = 0; x < slots.length; x++) {
			for (int y = 0; y < slots[x].length; y++) {
				drawTexture(textureName, x*slotWidth, y*slotHeight, slotWidth, slotHeight);
			}
		}
		
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		int k = 240;
		int i1 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, i1 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		PC_Vec2I realLocation = getRealLocation();
		
		for (int x = 0, xp = 1 + (slotWidth-18)/2; x < slots.length; x++, xp += slotWidth) {
			for (int y = 0, yp = 1 + (slotHeight-18)/2; y < slots[x].length; y++, yp += slotHeight) {
				if (slots[x][y] != null) {
					Slot slot = slots[x][y];
					slot.xDisplayPosition = xp + realLocation.x;
					slot.yDisplayPosition = yp + realLocation.y;
				}
			}
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	protected Slot getSlotAtPosition(PC_Vec2I position) {
		int x = position.x / slotWidth;
		int y = position.y / slotHeight;
		if(x>=0 && y>=0 && x<slots.length && y<slots[x].length){
			return slots[x][y];
		}
		return null;
	}
	
	public List<String> getTooltip(PC_Vec2I position) {
		Slot slot = getSlotAtPosition(position);
		if(slot!=null){
			ItemStack itemstack = null;

			if (slot.getHasStack()) 
				itemstack = slot.getStack();

			//if (slot instanceof PC_Slot && ((PC_Slot) slot).getBackgroundStack() != null && ((PC_Slot) slot).renderTooltipWhenEmpty())
				//itemstack = ((PC_Slot) slot).getBackgroundStack();

			if (itemstack != null) {
				List<String> l = itemstack.getTooltip(PC_ClientUtils.mc().thePlayer, PC_ClientUtils.mc().gameSettings.advancedItemTooltips);
				l.set(0, (new StringBuilder()).append("\247").append(Integer.toHexString(itemstack.getRarity().rarityColor)).append(l.get(0)).append("\2477").toString());
				return l;
			}
		}
		return super.getTooltip(position);
	}
	
}
