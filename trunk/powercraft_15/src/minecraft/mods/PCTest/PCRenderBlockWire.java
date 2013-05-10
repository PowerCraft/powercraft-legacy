package mods.PCTest;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;


public class PCRenderBlockWire extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
 
    private PCModelBlockWire modelW = new PCModelBlockWire();   // 0

    private int blockMeta;
    private boolean topBlockAir;
    private int bottomBlockID;
    private int thisBlockID;
    private int topBlockID;


    
    public void renderAModel(PCTileEntityBlockWire tile, double var2, double var4, double var6, float var8)
    {
        int var9;

        
        if(tile.worldObj != null) // to tell the world that your object is placed.
        {

 
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
        short var11 = 0;

        GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);


		       this.bindTextureByName("/mods/PCTest/textures/custom/ModelCable1.png");

        GL11.glPushMatrix();


        		this.modelW.renderCenter(0.0625F);
        		if (getFront(tile))
        		{
        			this.modelW.renderFront(0.0625F);
        			this.modelW.renderFront1(0.0625F);
        			
        		}

        		if (getBack(tile))
        		{
        			this.modelW.renderBack(0.0625F);
        			this.modelW.renderBack1(0.0625F);
        		}
        		if (getLeft(tile))
        		{
        			this.modelW.renderLeft(0.0625F);
        			this.modelW.renderLeft1(0.0625F);
        		}
        		
        		if (getRight(tile))
        		{
        			this.modelW.renderRight(0.0625F);
        			this.modelW.renderRight1(0.0625F);
        		}
        		if ((getFront(tile) && getBack(tile) && getLeft(tile)) || (getFront(tile) && getBack(tile) && getRight(tile)) || (getLeft(tile) && getRight(tile) && getFront(tile)) || (getLeft(tile) && getRight(tile) && getBack(tile)))
        		{
        			this.modelW.renderConnect(0.0625F);
        		}



        	}
        	
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        this.renderAModel((PCTileEntityBlockWire)var1, var2, var4, var6, var8);
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
		if (block == PCTest.blockWire)
		{
			 GL11.glPushMatrix();
		        GL11.glTranslatef((float) 0.5F, (float) 1.5F, (float) 0.5F);
		        short var11 = 0;
	  
		        GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
		        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		    
	 	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/mods/PCTest/textures/custom/ModelCable1.png"));

		        GL11.glPushMatrix();

        		this.modelW.renderCenter(0.0625F);
        		this.modelW.renderFront(0.0625F);
        		this.modelW.renderBack(0.0625F);
        		this.modelW.renderLeft(0.0625F);
    //    		this.modelW.renderRight(0.0625F);
        		this.modelW.renderConnect(0.0625F);

		        GL11.glPopMatrix();
		        GL11.glPopMatrix();
	}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return PCTest.renderWireID;
	}
	
	public boolean getFront(PCTileEntityBlockWire tile)
	{
        int i1 = tile.worldObj.getBlockId(tile.xCoord, tile.yCoord, tile.zCoord -1);

        if (i1 == PCTest.blockWire.blockID)
        {
            return true;
        }
        else if (i1 == 0)
        {
            return false;
        }
        else if (Block.blocksList[i1] != null && Block.blocksList[i1].canConnectRedstone(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord -1, 0))
        {
            return true;
        }
		else return false;
	}
	public boolean getBack(PCTileEntityBlockWire tile)
	{
        int i1 = tile.worldObj.getBlockId(tile.xCoord, tile.yCoord, tile.zCoord +1);

        if (i1 == PCTest.blockWire.blockID)
        {
            return true;
        }
        else if (i1 == 0)
        {
            return false;
        }
        else if (Block.blocksList[i1] != null && Block.blocksList[i1].canConnectRedstone(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord +1, 2))
        {
            return true;
        }
		else return false;
	}
	public boolean getLeft(PCTileEntityBlockWire tile)
	{
        int i1 = tile.worldObj.getBlockId(tile.xCoord -1, tile.yCoord, tile.zCoord);

        if (i1 == PCTest.blockWire.blockID)
        {
            return true;
        }
        else if (i1 == 0)
        {
            return false;
        }
        else if (Block.blocksList[i1] != null && Block.blocksList[i1].canConnectRedstone(tile.worldObj, tile.xCoord -1, tile.yCoord, tile.zCoord, 1))
        {
            return true;
        }
		else return false;
	}
	public boolean getRight(PCTileEntityBlockWire tile)
	{
        int i1 = tile.worldObj.getBlockId(tile.xCoord +1, tile.yCoord, tile.zCoord);

        if (i1 == PCTest.blockWire.blockID)
        {
            return true;
        }
        else if (i1 == 0)
        {
            return false;
        }
        else if (Block.blocksList[i1] != null && Block.blocksList[i1].canConnectRedstone(tile.worldObj, tile.xCoord +1, tile.yCoord, tile.zCoord, 3))
        {
            return true;
        }
		else return false;

	}

}