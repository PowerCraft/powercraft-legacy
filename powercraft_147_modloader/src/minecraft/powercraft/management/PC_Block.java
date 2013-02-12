package powercraft.management;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

public abstract class PC_Block extends BlockContainer implements PC_IMSG
{
    private boolean canSetTextureFile = true;
    private PC_IModule module;
	private BlockInfo replaced = new BlockInfo();
	private BlockInfo thisBlock;
	private String textureFile = "/terrain.png";
	
    protected PC_Block(int id, Material material)
    {
        this(id, 0, material);
    }

    protected PC_Block(int id, int textureIndex, Material material)
    {
    	this(id, textureIndex, material, true);
    }

    public PC_Block(int id, int textureIndex, Material material, boolean canSetTextureFile)
    {
        super(id, textureIndex, material);
        this.canSetTextureFile = canSetTextureFile;
        disableStats();
        thisBlock = new BlockInfo(id);
    }
    
    public Object msg(int msg, Object...obj){
    	IBlockAccess world = null;
    	PC_VecI pos = null;
    	int i=0;
    	if(obj != null){
    		if(obj.length>=1){
    			if(obj[0] instanceof IBlockAccess){
    				world = (IBlockAccess)obj[0];
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

    public abstract Object msg(IBlockAccess world, PC_VecI pos, int msg, Object...obj);

    public void setModule(PC_IModule module){
    	this.module = module;
    }
    
    public PC_IModule getModule(){
    	return module;
    }
    
    @Override
    public int getRenderType()
    {
        return PC_Renderer.getRendererID(true);
    }

    public Block setTextureFile(String texture)
    {
        if (canSetTextureFile)
        {
        	textureFile = texture;
        }

        return this;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if (side == 1 && getRenderType() == PC_Renderer.getRendererID(true) && msg(PC_Utils.MSG_ROTATION, GameInfo.getMD(world, x, y, z))!=null)
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

    public ItemBlock getItemBlock() {
		return thisBlock.itemBlock;
	}
    
	public void setItemBlock(ItemBlock itemBlock) {
		thisBlock.itemBlock = itemBlock;
	}
    
	public void setBlockID(int id) {
		int oldID = blockID;
		if(oldID==id)
			return;
		if(ValueWriting.setPrivateValue(Block.class, this, PC_GlobalVariables.indexBlockID, id)){
	    	if(ValueWriting.setPrivateValue(Item.class, thisBlock.itemBlock, PC_GlobalVariables.indexItemSthiftedIndex, id)){
	    		if(ValueWriting.setPrivateValue(ItemBlock.class, thisBlock.itemBlock, 0, id)){
		    		if(oldID!=-1){
		    			replaced.storeToID(oldID);
		    		}
		    		if(id!=-1){
		    			replaced = new BlockInfo(id);
		    			thisBlock.storeToID(id);
		    		}else{
		    			new BlockInfo().storeToID(oldID);
		    			replaced = null;
		    		}
	    		}else{
	    			ValueWriting.setPrivateValue(Item.class, thisBlock.itemBlock, PC_GlobalVariables.indexItemSthiftedIndex, oldID);
	    			ValueWriting.setPrivateValue(Block.class, this, PC_GlobalVariables.indexBlockID, oldID);
	    		}
	    	}else{
	    		ValueWriting.setPrivateValue(Block.class, this, PC_GlobalVariables.indexBlockID, oldID);
	    	}
		}
	}

	public String getTextureFile() {
		return textureFile;
	}

	public TileEntity newTileEntity(World world, int metadata){
		return null;
	}
	
	public final TileEntity createNewTileEntity(World world) {
		return createNewTileEntity(world, 0);
	}
	
	public final TileEntity createNewTileEntity(World world, int metadata) {
		if(PC_GlobalVariables.tileEntity != null && !world.isRemote){
			PC_GlobalVariables.tileEntity.validate();
			return PC_GlobalVariables.tileEntity;
		}
		return newTileEntity(world, metadata);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int par5, EntityPlayer player) {
		removeBlockByPlayer(world, player, x, y, z);
	}
	
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        par2EntityPlayer.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer))
        {
            ItemStack var8 = this.createStackedBlock(par6);

            if (var8 != null)
            {
                this.dropBlockAsItem_do(par1World, par3, par4, par5, var8);
            }
        }
        else
        {
            int var7 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
            this.dropBlockAsItem(par1World, par3, par4, par5, par6, var7);
        }
    }
	
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return false;
	}
	
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
		PC_TileEntity te = GameInfo.getTE(world, x, y, z);
		PC_VecI pos = new PC_VecI(x, y, z);
		IInventory inv = PC_InvUtils.getCompositeInventoryAt(world, pos);
		if(PC_GlobalVariables.tileEntity==null){
			if(inv!=null)
				PC_InvUtils.dropInventoryContents(inv, world, pos);
	        super.breakBlock(world, x, y, z, par5, par6);
		}
    }
	
	 /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata)
    {
        return 0;
    }
    
    /**
     * Called when fire is updating, checks if a block face can catch fire.
     * 
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @return True if the face can be on fire, false otherwise.
     */
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata)
    {
        return getFlammability(world, x, y, z, metadata) > 0;
    }
	
	@Override
	protected Block setRequiresSelfNotify() {
		thisBlock.requiresSelfNotify = true;
		return super.setRequiresSelfNotify();
	}

	@Override
	protected Block setLightOpacity(int par1) {
		thisBlock.lightOpacity = par1;
		return super.setLightOpacity(par1);
	}

	@Override
	protected Block setLightValue(float par1) {
		thisBlock.lightValue = (int)(15.0F * par1);
		return super.setLightValue(par1);
	}

	public static class BlockInfo{
		public Block block = null;
		public boolean opaqueCubeLookup = false;
		public int lightOpacity = 0;
		public boolean canBlockGrass = false;
		public int lightValue = 0;
		public boolean requiresSelfNotify = false;
		public boolean useNeighborBrightness = false;
		public ItemBlock itemBlock = null;
		
		public BlockInfo(){}
		
		public BlockInfo(int id){
			block = Block.blocksList[id];
			opaqueCubeLookup = Block.opaqueCubeLookup[id];
			lightOpacity = Block.lightOpacity[id];
			canBlockGrass = Block.canBlockGrass[id];
			lightValue = Block.lightValue[id];
			requiresSelfNotify = Block.requiresSelfNotify[id];
			useNeighborBrightness = Block.useNeighborBrightness[id];
			itemBlock = (ItemBlock)Item.itemsList[id];
		}
		
		public void storeToID(int id){
			Block.blocksList[id] = block;
			Block.opaqueCubeLookup[id] = opaqueCubeLookup;
			Block.lightOpacity[id] = lightOpacity;
			Block.canBlockGrass[id] = canBlockGrass;
			Block.lightValue[id] = lightValue;
			Block.requiresSelfNotify[id] = requiresSelfNotify;
			Block.useNeighborBrightness[id] = useNeighborBrightness;
			Item.itemsList[id] = itemBlock;
		}
		
	}
	
}
