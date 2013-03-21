package codechicken.core.render;

import java.awt.Dimension;

import org.lwjgl.opengl.GL11;

import codechicken.core.liquid.LiquidUtils;
import codechicken.core.vec.Cuboid6;
import codechicken.core.vec.Vector3;
import cpw.mods.fml.client.TextureFXManager;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.liquids.LiquidStack;

public class RenderUtils
{
    static Vector3[] vectors = new Vector3[8];
    
    static
    {
        for(int i = 0; i < vectors.length; i++)
            vectors[i] = new Vector3();
    }
    
    public static void addVecWithUV(Vector3 vec, double u, double v)
    {
        Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u, v);
    }
    
	public static Icon bindLiquidTexture(int liquidID, int liquidMeta)
	{
		if(liquidID < Block.blocksList.length && Block.blocksList[liquidID] != null)
		{
	    	Block liquidBlock = Block.blocksList[liquidID];
	    	return liquidBlock.getBlockTextureFromSideAndMetadata(0, liquidMeta);
		}
		else
		{
	    	Item liquidItem = Item.itemsList[liquidID];
	    	if(liquidItem == null) 
                return null;
	    	return liquidItem.getIconFromDamage(liquidMeta);
		}
	}
	
	public static void renderLiquidQuad(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, Icon icon, double res)
	{
    	double u1 = icon.getMinU();
    	double du = icon.getMaxU()-icon.getMinU();
    	double v2 = icon.getMinV();
    	double dv = icon.getMaxV()-icon.getMinV();
		
		Vector3 wide = vectors[0].set(point4).subtract(point1);
		Vector3 high = vectors[1].set(point1).subtract(point2);
		Tessellator t = Tessellator.instance;
		
		double wlen = wide.mag();
		double hlen = high.mag();
		
		double x = 0;
		while(x < wlen)
		{
			double rx = wlen - x;
    		if(rx > res)
    			rx = res;

    		double y = 0;
    		while(y < hlen)
    		{
    			double ry = hlen-y;
    			if(ry > res)
    				ry = res;

    			Vector3 dx1 = vectors[2].set(wide).multiply(x/wlen);
    			Vector3 dx2 = vectors[3].set(wide).multiply((x+rx)/wlen);    
    			Vector3 dy1 = vectors[4].set(high).multiply(y/hlen);    
    			Vector3 dy2 = vectors[5].set(high).multiply((y+ry)/hlen);

    			t.addVertexWithUV(point2.x+dx1.x+dy2.x, point2.y+dx1.y+dy2.y, point2.z+dx1.z+dy2.z, u1, v2-ry/res*dv);
    			t.addVertexWithUV(point2.x+dx1.x+dy1.x, point2.y+dx1.y+dy1.y, point2.z+dx1.z+dy1.z, u1, v2);
    			t.addVertexWithUV(point2.x+dx2.x+dy1.x, point2.y+dx2.y+dy1.y, point2.z+dx2.z+dy1.z, u1+rx/res*du, v2);
    			t.addVertexWithUV(point2.x+dx2.x+dy2.x, point2.y+dx2.y+dy2.y, point2.z+dx2.z+dy2.z, u1+rx/res*du, v2-ry/res*dv);
    			
    			y+=ry;
    		}
    		
    		x+=rx;
		}
	}
    
	public static void translateToWorldCoords(Entity entity, float frame)
    {       
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)frame;
        
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }
	
	public static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
    {
        Tessellator var2 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(3);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.draw();
        var2.startDrawing(1);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var2.draw();
    }
    
    public static void renderLiquidCuboid(Cuboid6 bound, Icon tex, double res)
    {
        renderLiquidQuad(//bottom
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z), 
                tex, res);
        renderLiquidQuad(//top
                new Vector3(bound.min.x, bound.max.y, bound.min.z),
                new Vector3(bound.min.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.min.z), 
                tex, res);
        renderLiquidQuad(//-x
                new Vector3(bound.min.x, bound.max.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z),
                new Vector3(bound.min.x, bound.max.y, bound.max.z), 
                tex, res);
        renderLiquidQuad(//+x
                new Vector3(bound.max.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.max.y, bound.min.z), 
                tex, res);
        renderLiquidQuad(//-z
                new Vector3(bound.max.x, bound.max.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.max.y, bound.min.z), 
                tex, res);
        renderLiquidQuad(//+z
                new Vector3(bound.min.x, bound.max.y, bound.max.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.max.z), 
                tex, res);
    }
    
    public static void renderBlockOverlaySide(int x, int y, int z, int side, double tx1, double tx2, double ty1, double ty2)
    {
        double[] points = new double[]{x - 0.009, x + 1.009, y - 0.009, y + 1.009, z - 0.009, z + 1.009};

        Tessellator tessellator = Tessellator.instance;
        switch(side)
        {
            case 0:
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
            break;
            case 1:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty2);
            break;
            case 2:
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx2, ty2);
            break;
            case 3:
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
            break;
            case 4:
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx2, ty2);
            break;
            case 5:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty2);
            break;
        }
    }

    public static boolean shouldRenderLiquid(LiquidStack liquid)
    {
        return liquid.amount > 0 && liquid.asItemStack().getItem() != null;
    }

    public static void renderLiquidCuboid(LiquidStack liquid, Cuboid6 bound, double res)
    {
        if(!shouldRenderLiquid(liquid))
            return;

        GL11.glDisable(GL11.GL_LIGHTING);
        if(liquid.isLiquidEqual(LiquidUtils.water))
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        
        CCRenderState.setColourOpaque(liquid.asItemStack().getItem().getColorFromItemStack(liquid.asItemStack(), 0));        
        TextureUtils.bindItemTexture(liquid.asItemStack());
        Icon tex = bindLiquidTexture(liquid.itemID, liquid.itemMeta);
        
        CCRenderState.startDrawing(7);
        renderLiquidCuboid(bound, tex, res);
        CCRenderState.draw();
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    /*public static void renderQuad3D(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, 
            int tx1, int ty1, int tx2, int ty2, int tw, int th, double depth)
    {
        double u1 = tx1/(double)tw;
        double v1 = ty1/(double)th;
        double u2 = tx2/(double)tw;
        double v2 = ty2/(double)th;
        int d_tw = tx2-tx1;
        int d_th = ty2-ty1;
        double d_u = u2-u1;
        double d_v = v2-v1;
        
        Vector3 width = point4.copy().subtract(point1);
        Vector3 height = point2.copy().subtract(point1);
        Vector3 normal = width.copy().crossProduct(height).normalize();
        Vector3 vd = normal.copy().multiply(-depth);
        Vector3 n_width = width.copy().normalize();
        Vector3 n_height = height.copy().normalize();
        
        Vector3 point1d = point1.copy().add(vd);
        Vector3 point2d = point2.copy().add(vd);
        Vector3 point3d = point3.copy().add(vd);
        Vector3 point4d = point4.copy().add(vd);
        
        Tessellator t = Tessellator.instance;
        //front
        t.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        t.addVertexWithUV(point1.x, point1.y, point1.z, u1, v1);
        t.addVertexWithUV(point2.x, point2.y, point2.z, u1, v2);
        t.addVertexWithUV(point3.x, point3.y, point3.z, u2, v2);
        t.addVertexWithUV(point4.x, point4.y, point4.z, u2, v1);
        //back
        t.setNormal((float)-normal.x, (float)-normal.y, (float)-normal.z);
        t.addVertexWithUV(point4d.x, point4d.y, point4d.z, u2, v1);
        t.addVertexWithUV(point3d.x, point3d.y, point3d.z, u2, v2);
        t.addVertexWithUV(point2d.x, point2d.y, point2d.z, u1, v2);
        t.addVertexWithUV(point1d.x, point1d.y, point1d.z, u1, v1);
        
        Vector3 dv = new Vector3();
        for(int i = 0; i <= d_tw; i++)
        {
            double d = i/(double)d_tw;
            double u = u1+d*d_u;
            dv.set(width).multiply(d);

            t.setNormal((float)n_width.x, (float)n_width.y, (float)n_width.z);
            t.addVertexWithUV(point1.x + dv.x, point1.y + dv.y, point1.z + dv.z, u, v1);
            t.addVertexWithUV(point2.x + dv.x, point2.y + dv.y, point2.z + dv.z, u, v2);
            t.addVertexWithUV(point2d.x + dv.x, point2d.y + dv.y, point2d.z + dv.z, u, v2);
            t.addVertexWithUV(point1d.x + dv.x, point1d.y + dv.y, point1d.z + dv.z, u, v1);

            t.setNormal((float)-n_width.x, (float)-n_width.y, (float)-n_width.z);
            t.addVertexWithUV(point1d.x + dv.x, point1d.y + dv.y, point1d.z + dv.z, u, v1);
            t.addVertexWithUV(point2d.x + dv.x, point2d.y + dv.y, point2d.z + dv.z, u, v2);
            t.addVertexWithUV(point2.x + dv.x, point2.y + dv.y, point2.z + dv.z, u, v2);
            t.addVertexWithUV(point1.x + dv.x, point1.y + dv.y, point1.z + dv.z, u, v1);
        }
        
        for(int i = 0; i <= d_th; i++)
        {
            double d = i/(double)d_th;
            double v = v1+d*d_v;
            dv.set(height).multiply(d);

            t.setNormal((float)n_height.x, (float)n_height.y, (float)n_height.z);
            t.addVertexWithUV(point1.x + dv.x, point1.y + dv.y, point1.z + dv.z, u1, v);
            t.addVertexWithUV(point4.x + dv.x, point4.y + dv.y, point4.z + dv.z, u2, v);
            t.addVertexWithUV(point4d.x + dv.x, point4d.y + dv.y, point4d.z + dv.z, u2, v);
            t.addVertexWithUV(point1d.x + dv.x, point1d.y + dv.y, point1d.z + dv.z, u1, v);

            t.setNormal((float)-n_height.x, (float)-n_height.y, (float)-n_height.z);
            t.addVertexWithUV(point1d.x + dv.x, point1d.y + dv.y, point1d.z + dv.z, u1, v);
            t.addVertexWithUV(point4d.x + dv.x, point4d.y + dv.y, point4d.z + dv.z, u2, v);
            t.addVertexWithUV(point4.x + dv.x, point4.y + dv.y, point4.z + dv.z, u2, v);
            t.addVertexWithUV(point1.x + dv.x, point1.y + dv.y, point1.z + dv.z, u1, v);
        }
    }
    
    public void render3DItem(int tex, int texIndex)
    {
        Dimension d = TextureFXManager.instance().getTextureDimensions(tex);
        int tx1 = (texIndex%16)*d.width/16;
        int ty1 = (texIndex/16)*d.width/16;
        renderQuad3D(new Vector3(0, 1, 0), new Vector3(0, 0, 0), new Vector3(1, 1, 0), new Vector3(1, 0, 0), 
                tx1, ty1, tx1+d.width/16, ty1+d.height/16, d.width, d.height, 0.0625);
    }*/
}
