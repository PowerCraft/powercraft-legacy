package powercraft.management;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModTextureStatic;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;

public abstract class PC_Item extends Item implements PC_IItemInfo, PC_IMSG
{
    private PC_IModule module;
    private boolean canSetTextureFile = true;
    private String currentTexture = "/gui/items.png";
    private Item replacedItem = null;
    
    protected PC_Item(int id)
    {
        this(id, true);
    }

    public PC_Item(int id, boolean canSetTextureFile)
    {
        this(id, 0, canSetTextureFile);
    }
    
    public PC_Item(int id, int iconIndex) {
		this(id, iconIndex, true);
	}

    public PC_Item(int id, int iconIndex, boolean canSetTextureFile) {
    	super(id-256);
        this.canSetTextureFile = canSetTextureFile;
        setIconIndex(iconIndex);
	}
    
	public void setItemID(int id){
		int oldID = itemID;
		if(oldID==id)
			return;
    	if(ValueWriting.setPrivateValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id)){
    		if(oldID!=-1){
    			Item.itemsList[oldID] = replacedItem;
    		}
    		if(id!=-1){
    			replacedItem = Item.itemsList[id];
    			Item.itemsList[id] = this;
    		}else{
    			replacedItem = null;
    		}
    	}
    }
    
    public PC_IModule getModule()
    {
        return module;
    }

    public void setModule(PC_IModule module)
    {
    	this.module = module;
    }

    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

    public void getSubItems(int index, CreativeTabs creativeTab, List list)
    {
        list.addAll(getItemStacks(new ArrayList<ItemStack>()));
    }

    public Item setTextureFile(String texture)
    {

    	/*if(canSetTextureFile){
			try {
				int iconIndex = ModLoader.getUniqueSpriteIndex("/gui/items.png");
				BufferedImage image = ModLoader.loadImage(PC_ClientUtils.mc().renderEngine, texture);
				BufferedImage image2 = new BufferedImage(16, 16, image.getType());
				int y = itemPosInTex/16;
				int x = itemPosInTex-y*16;
				for(int j=0; j<16; j++){
					for(int i=0; i<16; i++){
						image2.setRGB(i, j, image.getRGB(i + x*16, j + y*16));
					}
				}
				ModTextureStatic modTexture = new ModTextureStatic(iconIndex, 1, image2);
				PC_ClientUtils.mc().renderEngine.registerTextureFX(modTexture);
				super.setIconIndex(iconIndex);
				
				if(requiresMultipleRenderPasses()){
					iconIndex = ModLoader.getUniqueSpriteIndex("/gui/items.png");
					image = ModLoader.loadImage(PC_ClientUtils.mc().renderEngine, texture);
					image2 = new BufferedImage(16, 16, image.getType());
					y = itemPosInTexPass2/16;
					x = itemPosInTexPass2-y*16;
					for(int j=0; j<16; j++){
						for(int i=0; i<16; i++){
							image2.setRGB(i, j, image.getRGB(i + x*16, j + y*16));
						}
					}
					modTexture = new ModTextureStatic(iconIndex, 1, image2);
					PC_ClientUtils.mc().renderEngine.registerTextureFX(modTexture);
					iconIndexRenderPass2 = iconIndex;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}*/
    	
    	if(canSetTextureFile)
    		currentTexture = texture;
    	
        return this;
    }

    public String getTextureFile(){
    	return currentTexture;
    }
    
	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
	}

	public Object areItemsEqual(PC_ItemStack pc_ItemStack, int otherMeta, NBTTagCompound otherNbtTag) {
		return null;
	}
	
}
