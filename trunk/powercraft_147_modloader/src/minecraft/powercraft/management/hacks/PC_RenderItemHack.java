package powercraft.management.hacks;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import powercraft.management.PC_Item;
import powercraft.management.PC_ItemArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Tessellator;

public class PC_RenderItemHack extends RenderItem {
	
	private RenderBlocks renderBlocks = new RenderBlocks();
	private Random random = new Random();
	
	/**
     * Renders the item
     */
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9)
    {
        this.random.setSeed(187L);
        ItemStack var10 = par1EntityItem.func_92059_d();

        if (var10.getItem() != null)
        {
            GL11.glPushMatrix();
            float var11 = MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
            float var12 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
            byte var13 = 1;

            if (par1EntityItem.func_92059_d().stackSize > 1)
            {
                var13 = 2;
            }

            if (par1EntityItem.func_92059_d().stackSize > 5)
            {
                var13 = 3;
            }

            if (par1EntityItem.func_92059_d().stackSize > 20)
            {
                var13 = 4;
            }

            if (par1EntityItem.func_92059_d().stackSize > 40)
            {
                var13 = 5;
            }

            GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            Block var14 = null;

            if (var10.itemID < Block.blocksList.length)
            {
                var14 = Block.blocksList[var10.itemID];
            }

            int var15;
            float var17;
            float var16;
            float var18;

            if (var14 != null && RenderBlocks.renderItemIn3d(var14.getRenderType()))
            {
                GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

                if (field_82407_g)
                {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                this.loadTexture(PC_Hacks.getTextureFile(var10, "/terrain.png"));
                float var24 = 0.25F;
                var15 = var14.getRenderType();

                if (var15 == 1 || var15 == 19 || var15 == 12 || var15 == 2)
                {
                    var24 = 0.5F;
                }

                GL11.glScalef(var24, var24, var24);

                for (int var23 = 0; var23 < var13; ++var23)
                {
                    GL11.glPushMatrix();

                    if (var23 > 0)
                    {
                        var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        var16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        GL11.glTranslatef(var18, var16, var17);
                    }

                    var18 = 1.0F;
                    renderBlocks.renderBlockAsItem(var14, var10.getItemDamage(), var18);
                    GL11.glPopMatrix();
                }
            }
            else
            {
                int var19;
                float var20;

                if (var10.getItem().requiresMultipleRenderPasses())
                {
                    if (field_82407_g)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    this.loadTexture(PC_Hacks.getTextureFile(var10, "/gui/items.png"));

                    for (var19 = 0; var19 <= 1; ++var19)
                    {
                        this.random.setSeed(187L);
                        var15 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var19);
                        var20 = 1.0F;

                        if (this.field_77024_a)
                        {
                            int var21 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var19);
                            var16 = (float)(var21 >> 16 & 255) / 255.0F;
                            var17 = (float)(var21 >> 8 & 255) / 255.0F;
                            float var22 = (float)(var21 & 255) / 255.0F;
                            GL11.glColor4f(var16 * var20, var17 * var20, var22 * var20, 1.0F);
                            this.func_77020_a(par1EntityItem, var15, var13, par9, var16 * var20, var17 * var20, var22 * var20);
                        }
                        else
                        {
                            this.func_77020_a(par1EntityItem, var15, var13, par9, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }
                else
                {
                    if (field_82407_g)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    var19 = var10.getIconIndex();

                    if (var14 != null)
                    {
                        this.loadTexture(PC_Hacks.getTextureFile(var10, "/terrain.png"));
                    }
                    else
                    {
                        this.loadTexture(PC_Hacks.getTextureFile(var10, "/gui/items.png"));
                    }

                    if (this.field_77024_a)
                    {
                        var15 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
                        var20 = (float)(var15 >> 16 & 255) / 255.0F;
                        var18 = (float)(var15 >> 8 & 255) / 255.0F;
                        var16 = (float)(var15 & 255) / 255.0F;
                        var17 = 1.0F;
                        this.func_77020_a(par1EntityItem, var19, var13, par9, var20 * var17, var18 * var17, var16 * var17);
                    }
                    else
                    {
                        this.func_77020_a(par1EntityItem, var19, var13, par9, 1.0F, 1.0F, 1.0F);
                    }
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    private void func_77020_a(EntityItem par1EntityItem, int par2, int par3, float par4, float par5, float par6, float par7)
    {
        Tessellator var8 = Tessellator.instance;
        float var9 = (float)(par2 % 16 * 16 + 0) / 256.0F;
        float var10 = (float)(par2 % 16 * 16 + 16) / 256.0F;
        float var11 = (float)(par2 / 16 * 16 + 0) / 256.0F;
        float var12 = (float)(par2 / 16 * 16 + 16) / 256.0F;
        float var13 = 1.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;
        float var16;

        if (this.renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (field_82407_g)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((((float)par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float var17 = 0.0625F;
            var16 = 0.021875F;
            ItemStack var18 = par1EntityItem.func_92059_d();
            int var19 = var18.stackSize;
            byte var20;

            if (var19 < 2)
            {
                var20 = 1;
            }
            else if (var19 < 16)
            {
                var20 = 2;
            }
            else if (var19 < 32)
            {
                var20 = 3;
            }
            else
            {
                var20 = 4;
            }

            GL11.glTranslatef(-var14, -var15, -((var17 + var16) * (float)var20 / 2.0F));

            for (int var21 = 0; var21 < var20; ++var21)
            {
                GL11.glTranslatef(0.0F, 0.0F, var17 + var16);

                if (var18.itemID < Block.blocksList.length && Block.blocksList[var18.itemID] != null)
                {
                    this.loadTexture(PC_Hacks.getTextureFile(var18, "/terrain.png"));
                }
                else
                {
                    this.loadTexture(PC_Hacks.getTextureFile(var18, "/gui/items.png"));
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, var17);

                if (var18 != null && var18.hasEffect())
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture("%blur%/misc/glint.png"));
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float var22 = 0.76F;
                    GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float var23 = 0.125F;
                    GL11.glScalef(var23, var23, var23);
                    float var24 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(var24, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, var17);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(var23, var23, var23);
                    var24 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-var24, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int var25 = 0; var25 < par3; ++var25)
            {
                GL11.glPushMatrix();

                if (var25 > 0)
                {
                    var16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var27 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var16, var27, var26);
                }

                if (!field_82407_g)
                {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                var8.startDrawingQuads();
                var8.setNormal(0.0F, 1.0F, 0.0F);
                var8.addVertexWithUV((double)(0.0F - var14), (double)(0.0F - var15), 0.0D, (double)var9, (double)var12);
                var8.addVertexWithUV((double)(var13 - var14), (double)(0.0F - var15), 0.0D, (double)var10, (double)var12);
                var8.addVertexWithUV((double)(var13 - var14), (double)(1.0F - var15), 0.0D, (double)var10, (double)var11);
                var8.addVertexWithUV((double)(0.0F - var14), (double)(1.0F - var15), 0.0D, (double)var9, (double)var11);
                var8.draw();
                GL11.glPopMatrix();
            }
        }
    }
	
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
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture(PC_Hacks.getTextureFile(par3ItemStack, "/terrain.png")));
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
                par2RenderEngine.bindTexture(par2RenderEngine.getTexture(PC_Hacks.getTextureFile(par3ItemStack, "/gui/items.png")));

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
                    par2RenderEngine.bindTexture(par2RenderEngine.getTexture(PC_Hacks.getTextureFile(par3ItemStack, "/terrain.png")));
                }
                else
                {
                    par2RenderEngine.bindTexture(par2RenderEngine.getTexture(PC_Hacks.getTextureFile(par3ItemStack, "/gui/items.png")));
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
