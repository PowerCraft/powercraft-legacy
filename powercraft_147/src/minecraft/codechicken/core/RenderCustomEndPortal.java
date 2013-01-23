package codechicken.core;
import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.GL11;

public class RenderCustomEndPortal
{
	FloatBuffer field_40448_a;

    public RenderCustomEndPortal()
    {
        field_40448_a = GLAllocation.createDirectFloatBuffer(16);
    }

    public void render(double posX, double posY, double posZ, float frame, double playerX, double playerY, double playerZ, RenderEngine r)
    {
    	if(r == null)return;
        GL11.glDisable(GL11.GL_LIGHTING);
        Random random = new Random(31100L);
        float f4 = 0.75F;
        for(int i = 0; i < 16; i++)
        {
            GL11.glPushMatrix();
            float f5 = 16 - i;
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if(i == 0)
            {
                r.bindTexture(r.getTexture("/misc/tunnel.png"));
                f7 = 0.1F;
                f5 = 65F;
                f6 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(770, 771);
            }
            if(i == 1)
            {
                r.bindTexture(r.getTexture("/misc/particlefield.png"));
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }
            float f8 = (float)(-(posY + (double)f4));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 = (float)(posY + (double)f4) + f11;
            GL11.glTranslated(playerX, f11, playerZ);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, func_40447_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, func_40447_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, func_40447_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, func_40447_a(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 0xaae60L) / 700000F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslated(-playerX, -playerZ, -playerY);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslated((ActiveRenderInfo.objectX * f5) / f9, (ActiveRenderInfo.objectZ * f5) / f9, -playerY + 20);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if(i == 0)
            {
                f11 = f12 = f13 = 1.0F;
            }
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            drawSurface(tessellator, posX, posY, posZ, frame, playerX, playerY, playerZ);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW0_ARB);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    public void drawSurface(Tessellator tessellator, double posX, double posY, double posZ, float frame, double playerX, double playerY, double playerZ)
    {
    	float offsetY = 0.75F;
        tessellator.addVertex(posX, posY + (double)offsetY, posZ);
        tessellator.addVertex(posX, posY + (double)offsetY, posZ + 1.0D);
        tessellator.addVertex(posX + 1.0D, posY + (double)offsetY, posZ + 1.0D);
        tessellator.addVertex(posX + 1.0D, posY + (double)offsetY, posZ);
    }

    private FloatBuffer func_40447_a(float f, float f1, float f2, float f3)
    {
        field_40448_a.clear();
        field_40448_a.put(f).put(f1).put(f2).put(f3);
        field_40448_a.flip();
        return field_40448_a;
    }
}
