package powercraft.core;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class PCco_ItemBlockSaver extends PC_Item {

	public PCco_ItemBlockSaver(int id) {
		super(id);
		setIconIndex(3);
	}

	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int face, float par8, float par9, float par10)
    {
		int i = world.getBlockId(x, y, z);

		if (i == Block.snow.blockID) {
			face = 1;
		} else if (i != Block.vine.blockID && i != Block.tallGrass.blockID && i != Block.deadBush.blockID) {
			if (face == 0) {
				y--;
			}

			if (face == 1) {
				y++;
			}

			if (face == 2) {
				z--;
			}

			if (face == 3) {
				z++;
			}

			if (face == 4) {
				x--;
			}

			if (face == 5) {
				x++;
			}
		}

		if (itemstack.stackSize == 0) {
			return false;
		}

		if (!entityplayer.canPlayerEdit(x, y, z, face, itemstack)) {
			return false;
		}

		int placedID = getBlockID(itemstack);
		int placedMeta = getBlockMetadata(itemstack);
		if (Block.blocksList[placedID] == null) {
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			return true;
		}

		if (y == 255 && Block.blocksList[placedID].blockMaterial.isSolid()) {
			return false;
		}

		PC_VecI pos = new PC_VecI(x, y, z);

		if (world.canPlaceEntityOnSide(placedID, x, y, z, false, face, entityplayer)) {
			Block block = Block.blocksList[placedID];

			if (PC_Utils.setBID(world, pos, placedID, placedMeta)) {
				if (PC_Utils.getBID(world, pos) == placedID) {
					/** @todo onBlockPlacedBy*/
					Block.blocksList[placedID].onBlockPlacedBy(world, x, y, z, entityplayer);

					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
							block.stepSound.getPitch() * 0.8F);

					if (itemstack.hasTagCompound()) {
						NBTTagCompound tag = itemstack.getTagCompound();

						TileEntity tec = PC_Utils.getTE(world, pos);
						if (tec != null) {
							int cx = tec.xCoord;
							int cy = tec.yCoord;
							int cz = tec.zCoord;

							tec.readFromNBT(tag);
							tec.xCoord = cx;
							tec.yCoord = cy;
							tec.zCoord = cz;
							itemstack.setTagCompound(null);
							world.setBlockTileEntity(cx, cy, cz, tec);
						}
					}

					Block.blocksList[placedID].onBlockPlacedBy(world, x, y, z, entityplayer);

				}


				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			}

			return true;
		} else {
			return false;
		}
    }

	private int getBlockMetadata(ItemStack itemstack) {
		if (!itemstack.hasTagCompound()) {
			return 0;
		} else {
			return itemstack.getTagCompound().getInteger("BlockMeta");
		}
	}


	private int getBlockID(ItemStack itemstack) {
		if (!itemstack.hasTagCompound()) {
			return 0;
		} else {
			return itemstack.getItemDamage();
		}
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return par1ItemStack.hasTagCompound();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		Item i = Item.itemsList[itemStack.getItemDamage()];
		ItemStack is = new ItemStack(i);
		list.add(i.getItemDisplayName(is));
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Block Saver", null));
            return names;
		case PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			return true;
		}
		return null;
	}

}
