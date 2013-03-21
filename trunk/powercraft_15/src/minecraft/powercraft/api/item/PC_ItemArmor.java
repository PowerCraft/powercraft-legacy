package powercraft.api.item;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.IArmorTextureProvider;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_IIDChangeAble;
import powercraft.api.PC_IMSG;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_TextureRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_ItemArmor extends ItemArmor implements PC_IItemInfo, PC_IMSG, PC_IIDChangeAble, IArmorTextureProvider
{
    public static final int HEAD = 0, TORSO = 1, LEGS = 2, FEET = 3;

    private PC_ModuleObject module;
    private boolean canSetTextureFile = true;
    private String armorTexture;
    private Item replacedItem = null;
    private ItemData replacedItemData = null;
    protected Icon[] icons;
    private String[] textureNames;
    
    protected PC_ItemArmor(int id, EnumArmorMaterial material, int type, String textureName, String...textureNames){
    	super(id-256, material, 2, type);
    	this.textureNames = new String[1+textureNames.length];
    	icons = new Icon[1+textureNames.length];
    	this.textureNames[0] = textureName;
    	for(int i=0; i<textureNames.length; i++){
    		this.textureNames[i+1] = textureNames[i];
    	}
    }
    
    public void setID(int id){
    	int oldID = itemID;
		Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
		ItemData thisItemData = map.get(oldID);
		if(PC_ReflectHelper.setValue(Item.class, this, PC_GlobalVariables.indexItemSthiftedIndex, id, int.class)){
    		if(oldID!=-1){
    			if(replacedItemData==null){
    				map.remove(oldID);
    			}else{
    				PC_ReflectHelper.setValue(ItemData.class, replacedItemData, 3, oldID, int.class);
    				map.put(oldID, replacedItemData);
    			}
    			Item.itemsList[oldID] = replacedItem;
    		}
    		if(id!=-1){
    			replacedItemData = map.get(id);
    			replacedItem = Item.itemsList[id];
    			if(thisItemData==null){
    				map.remove(id);
    			}else{
    				PC_ReflectHelper.setValue(ItemData.class, thisItemData, 3, id, int.class);
    				map.put(id, thisItemData);
    			}
    			Item.itemsList[id] = this;
    		}else{
    			replacedItemData = null;
    			replacedItem = null;
    		}
    	}
    }
    
    public PC_ModuleObject getModule()
    {
        return module;
    }

    public void setModule(PC_ModuleObject module)
    {
        this.module = module;
    }

    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }
    
    @Override
	public boolean showInCraftingTool() {
		return true;
	}
    
    public void setArmorTextureFile(String armorTexture) {
		this.armorTexture = armorTexture;
	}
    
    @Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return PC_TextureRegistry.getPowerCraftImageDir()+PC_TextureRegistry.getTextureName(module, armorTexture);
	}
    
    @Override
	public Item setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(GameInfo.getCreativeTab(_default));
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister par1IconRegister){
		for(int i=0; i<textureNames.length; i++){
			icons[i] = par1IconRegister.registerIcon(PC_TextureRegistry.getTextureName(module, textureNames[i]));
		}
	}

	public Icon getIconFromDamage(int par1){
		if(par1>=icons.length){
			par1 = icons.length-1;
		}
        return icons[par1];
    }
    
}
