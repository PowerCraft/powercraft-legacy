package powercraft.management.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import powercraft.management.PC_Item;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemBlock;

public class PC_ItemRendererHack extends ItemRenderer {

	private Minecraft mc;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	
	public PC_ItemRendererHack(Minecraft par1Minecraft) {
		super(par1Minecraft);
		mc = par1Minecraft;
	}

	/**
     * Renders the item stack for being in an entity's hand Args: itemStack
     */
	@Override
    public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3)
    {
        GL11.glPushMatrix();
        Block var4 = null;

        if (par2ItemStack.itemID < Block.blocksList.length)
        {
            var4 = Block.blocksList[par2ItemStack.itemID];
        }

        if (var4 != null && RenderBlocks.renderItemIn3d(var4.getRenderType()))
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(PC_Hacks.getTextureFile(par2ItemStack, "/terrain.png")));
            this.renderBlocksInstance.renderBlockAsItem(var4, par2ItemStack.getItemDamage(), 1.0F);
        }
        else
        {
            if (var4 != null)
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(PC_Hacks.getTextureFile(par2ItemStack, "/terrain.png")));
            }
            else
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(PC_Hacks.getTextureFile(par2ItemStack, "/gui/items.png")));
            }

            Tessellator var5 = Tessellator.instance;
            int var6 = par1EntityLiving.getItemIcon(par2ItemStack, par3);
            float var7 = ((float)(var6 % 16 * 16) + 0.0F) / 256.0F;
            float var8 = ((float)(var6 % 16 * 16) + 15.99F) / 256.0F;
            float var9 = ((float)(var6 / 16 * 16) + 0.0F) / 256.0F;
            float var10 = ((float)(var6 / 16 * 16) + 15.99F) / 256.0F;
            float var11 = 0.0F;
            float var12 = 0.3F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(-var11, -var12, 0.0F);
            float var13 = 1.5F;
            GL11.glScalef(var13, var13, var13);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            renderItemIn2D(var5, var8, var9, var7, var10, 0.0625F);

            if (par2ItemStack != null && par2ItemStack.hasEffect() && par3 == 0)
            {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("%blur%/misc/glint.png"));
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                float var14 = 0.76F;
                GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float var15 = 0.125F;
                GL11.glScalef(var15, var15, var15);
                float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(var16, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(var15, var15, var15);
                var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-var16, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        GL11.glPopMatrix();
    }
	
}
