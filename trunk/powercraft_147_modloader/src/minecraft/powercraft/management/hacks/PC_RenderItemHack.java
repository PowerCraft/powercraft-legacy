package powercraft.management.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;

public class PC_RenderItemHack extends RenderItem {
	
	private RenderBlocks renderBlocks = new RenderBlocks();
	
	@Override
	public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
		if(par3ItemStack.getItem()==null)
			return;
        int var6 = par3ItemStack.itemID;
        int var7 = par3ItemStack.getItemDamage();
        int var8 = par3ItemStack.getIconIndex();
        int var9;
        float var10;
        float var11;
        float var12;

        if (var6 < Block.blocksList.length && Block.blocksList[var6]!=null && RenderBlocks.renderItemIn3d(Block.blocksList[var6].getRenderType()))
        {
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/terrain.png"));
            Block var16 = Block.blocksList[var6];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            var9 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
            var12 = (float)(var9 >> 16 & 255) / 255.0F;
            var10 = (float)(var9 >> 8 & 255) / 255.0F;
            var11 = (float)(var9 & 255) / 255.0F;

            if (this.field_77024_a)
            {
                GL11.glColor4f(var12, var10, var11, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.useInventoryTint = this.field_77024_a;
            this.renderBlocks.renderBlockAsItem(var16, var7, 1.0F);
            this.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        }
        else
        {
            int var13;

            if (Item.itemsList[var6].requiresMultipleRenderPasses())
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));

                for (var13 = 0; var13 <= 1; ++var13)
                {
                    var9 = Item.itemsList[var6].getIconFromDamageForRenderPass(var7, var13);
                    int var14 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, var13);
                    var10 = (float)(var14 >> 16 & 255) / 255.0F;
                    var11 = (float)(var14 >> 8 & 255) / 255.0F;
                    float var15 = (float)(var14 & 255) / 255.0F;

                    if (this.field_77024_a)
                    {
                        GL11.glColor4f(var10, var11, var15, 1.0F);
                    }

                    this.renderTexturedQuad(par4, par5, var9 % 16 * 16, var9 / 16 * 16, 16, 16);
                }

                GL11.glEnable(GL11.GL_LIGHTING);
            }
            else if (var8 >= 0)
            {
                GL11.glDisable(GL11.GL_LIGHTING);

                if (var6 < 256)
                {
                    par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/terrain.png"));
                }
                else
                {
                    par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));
                }

                var13 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
                float var17 = (float)(var13 >> 16 & 255) / 255.0F;
                var12 = (float)(var13 >> 8 & 255) / 255.0F;
                var10 = (float)(var13 & 255) / 255.0F;

                if (this.field_77024_a)
                {
                    GL11.glColor4f(var17, var12, var10, 1.0F);
                }

                this.renderTexturedQuad(par4, par5, var8 % 16 * 16, var8 / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
