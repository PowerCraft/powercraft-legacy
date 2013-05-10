package mods.betterworld.DeCB.client;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.DeCB.BWDeCB;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnRound;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;


public class BWDeCB_RendererColumnRound extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
    private ModelBlockColumnSmallR aModelSmallR = new ModelBlockColumnSmallR();   // 0
    private ModelBlockColumnBottomR aModelBottomR = new ModelBlockColumnBottomR();   // 1
    private ModelBlockColumnCenterR aModelCenterR = new ModelBlockColumnCenterR();   // 2
    private ModelBlockColumnTopR aModelTopR = new ModelBlockColumnTopR();    //3
	


    private int blockMeta;
    private int blockMetaID;
    private int renderBlockModelType;
    private boolean bottomBlockColumn;
    private boolean topBlockColumn;
    private boolean topBlockAir;
    private boolean bottomBlockAir;
    private int bottomBlockID;
    private int thisBlockID;
    private int topBlockID;
    private boolean leftCol, rightCol, backCol,frontCol;
    
    public void renderAModel(BWDeCB_TileEntityBlockColumnRound var1, double var2, double var4, double var6, float var8)
    {
        int var9;

        
        if(var1.worldObj != null) // to tell the world that your object is placed.
        {
        blockMeta =(var1.worldObj.getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord)); // to tell the game it needs to pick up metadata from your block
        blockMetaID = (var1.getBlockDamageID());
        topBlockAir =(var1.worldObj.isAirBlock(var1.xCoord, var1.yCoord + 1, var1.zCoord));
        bottomBlockAir =(var1.worldObj.isAirBlock(var1.xCoord, var1.yCoord - 1, var1.zCoord));
        bottomBlockID =(var1.worldObj.getBlockId(var1.xCoord, var1.yCoord - 1, var1.zCoord));
        thisBlockID =(var1.worldObj.getBlockId(var1.xCoord, var1.yCoord, var1.zCoord));
        topBlockID =(var1.worldObj.getBlockId(var1.xCoord, var1.yCoord + 1, var1.zCoord));
          
        }
        if (thisBlockID == bottomBlockID || (var1.worldObj.getBlockId(var1.xCoord, var1.yCoord - 1, var1.zCoord)) == BWDeCB.blockColumnSquareID)
        {
        	bottomBlockColumn = true;
        }
        else
        {
        	bottomBlockColumn = false;
        }
        
        if (thisBlockID == topBlockID|| (var1.worldObj.getBlockId(var1.xCoord, var1.yCoord + 1, var1.zCoord)) == BWDeCB.blockColumnSquareID)
        {
        	topBlockColumn = true;
        }
        else
        {
        	topBlockColumn = false;
        }

 
        if (bottomBlockAir == false && bottomBlockColumn == false  && topBlockAir == false && topBlockColumn == false)
        {
        	renderBlockModelType = 0; // ModelBlockColumnSmall
        }
        if (bottomBlockAir == false && bottomBlockColumn == false  && topBlockAir == false && topBlockColumn == true)
        {
        	renderBlockModelType = 1; // ModelBlockColumnBottom
        }
        if (bottomBlockAir == false && bottomBlockColumn == false  && topBlockAir == true && topBlockColumn == false)
        {
        	renderBlockModelType = 1; // ModelBlockColumnBottom
        }
        if (bottomBlockAir == true && topBlockAir == false && topBlockColumn == true)
        {
        	renderBlockModelType = 2; // ModelBlockColumnCenter
        }
        if (bottomBlockColumn == true  && topBlockColumn == true)
        {
        	renderBlockModelType = 2; // ModelBlockColumnCenter
        }
        if (bottomBlockAir == true  && topBlockAir == true)
        {
        	renderBlockModelType = 2; // ModelBlockColumnCenter
        }
        if (bottomBlockColumn == true && topBlockAir == true)
        {
        	renderBlockModelType = 2; // ModelBlockColumnCenter
        }

        if (bottomBlockColumn ==true  && topBlockAir == false && topBlockColumn == false)
       	{
    	   	renderBlockModelType = 3; // ModelBlockColumnTop
       	}
        if (bottomBlockAir == true  && topBlockAir == false && topBlockColumn == false)
       	{
    	   	renderBlockModelType = 4; // ModelBlockColumnTop
       	}
       
        
        
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 1.5F, (float)var6 + 0.5F);
        short var11 = 0;

        GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);


		       this.bindTextureByName("/mods/betterworld/CB/textures/blocks/"+ String.valueOf(BWCB_BlockList.blockStoneNormalTexture.get(blockMetaID))+ "_" + BWCB.textureRes+".png");

        GL11.glPushMatrix();

        	if (renderBlockModelType == 0)
        	{
        		this.aModelSmallR.renderModel(0.0625F);
        	}
        	if (renderBlockModelType == 1)
        	{
        		this.aModelBottomR.renderModel(0.0625F);
        	}
        	if (renderBlockModelType == 2)
        	{
        		this.aModelCenterR.renderModel(0.0625F);
        	}
        	if (renderBlockModelType == 3)
        	{
 		
        		this.aModelTopR.renderModel(0.0625F);
        	}
        	
        	if(renderBlockModelType == 4)
        	{
     			this.aModelTopR.renderModel(0.0625F);

        	}
        	
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        this.renderAModel((BWDeCB_TileEntityBlockColumnRound)var1, var2, var4, var6, var8);
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
		if (block == BWDeCB.blockColumnRound)
		{
			 GL11.glPushMatrix();
		        GL11.glTranslatef((float) 0.5F, (float) 1.5F, (float) 0.5F);
		        short var11 = 0;
	  
		        GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
		        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		    
	 	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/mods/betterworld/CB/textures/blocks/"+ String.valueOf(BWCB_BlockList.blockStoneNormalTexture.get(metadata))+ "_" + BWCB.textureRes+".png"));

		        GL11.glPushMatrix();

		        		this.aModelBottomR.renderModel(0.0625F);

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
		return BWDeCB.renderColumnRoundID;
	}

}