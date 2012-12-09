package powercraft.management;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public abstract class PC_Block extends BlockContainer implements PC_MSG
{
    private boolean canSetTextureFile = true;
    private String module;
	private ItemBlock itemBlock;
	private Block replacedBlock = null;
	private Item replacedItemBlock = null;
    
	
    protected PC_Block(Material material)
    {
        this(0, material);
    }

    protected PC_Block(int textureIndex, Material material)
    {
    	this(textureIndex, material, true);
    }

    public PC_Block(int textureIndex, Material material, boolean canSetTextureFile)
    {
        super(PC_Utils.getFreeBlockID(), textureIndex, material);
        this.canSetTextureFile = canSetTextureFile;
    }
    
    public Object msg(int msg, Object...obj){
    	World world = null;
    	PC_VecI pos = null;
    	int i=0;
    	if(obj != null){
    		if(obj.length>=1){
    			if(obj[0] instanceof World){
    				world = (World)obj[0];
    				i=1;
    				if(obj.length>=2){
    	    			if(obj[1] instanceof PC_VecI){
    	    				pos = (PC_VecI)obj[1];
    	    				i=2;
    	    			}
    	    		}
    			}
    		}
    	}
    	Object o[] = new Object[obj.length-i];
    	for(int j=0; j<o.length; j++){
    		o[j] = obj[j+i];
    	}
    	return msg(world, pos, msg, o);
    }

    public abstract Object msg(World world, PC_VecI pos, int msg, Object...obj);

    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    public void setModule(String module){
    	this.module = module;
    }
    
    public String getModule(){
    	return module;
    }
    
    @Override
    public int getRenderType()
    {
        return PC_Renderer.getRendererID(true);
    }

    @Override
    public Block setTextureFile(String texture)
    {
        if (canSetTextureFile)
        {
            super.setTextureFile(texture);
        }

        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if (side == 1 && getRenderType() == PC_Renderer.getRendererID(true) && this instanceof PC_IRotatedBox)
        {
            return false;
        }

        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    public static boolean canSilkHarvest(Block block)
    {
        return block.renderAsNormalBlock() && !block.hasTileEntity();
    }

    public static ItemStack createStackedBlock(Block block, int meta)
    {
        int var2 = 0;

        if (block.blockID >= 0 && block.blockID < Item.itemsList.length && Item.itemsList[block.blockID].getHasSubtypes())
        {
            var2 = meta;
        }

        return new ItemStack(block.blockID, 1, var2);
    }

	public void setItemBlock(ItemBlock itemBlock) {
		this.itemBlock = itemBlock;
	}
    
	public void setBlockID(int id) {
		int oldID = blockID;
		if(PC_Utils.setPrivateValue(Block.class, this, 170, id)){
	    	if(PC_Utils.setPrivateValue(Item.class, this, 160, id)){
	    		if(oldID!=-1){
	    			Block.blocksList[oldID] = replacedBlock;
	    			Item.itemsList[oldID] = replacedItemBlock;
	    		}
	    		if(id!=-1){
	    			replacedBlock = Block.blocksList[id];
	    			replacedItemBlock = Item.itemsList[id];
	    			Block.blocksList[id] = this;
	    			Item.itemsList[id] = itemBlock;
	    		}else{
	    			replacedBlock = null;
	    			replacedItemBlock = null;
	    		}
	    	}else{
	    		PC_Utils.setPrivateValue(Block.class, this, 170, oldID);
	    	}
		}
	}
    
}
