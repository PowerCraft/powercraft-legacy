package net.minecraft.src;




/**
 * ItemBlock injected to itemsList to replace the standard item block.<br>
 * This kind of Locked Chest can hold additional information about the
 * inventory, and is used as a storage item for the Inventory Pickup function of
 * Activators.
 * 
 * @author MightyPork
 */
public class PCco_ItemBlockHackedLockedChest extends ItemBlock {

	/**
	 * @param par1 id
	 */
	public PCco_ItemBlockHackedLockedChest(int par1) {
		super(par1);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}


	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS !
	 */
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int face) {

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

		if (!entityplayer.canPlayerEdit(x, y, z)) {
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

		PC_CoordI pos = new PC_CoordI(x, y, z);
		
		if (world.canBlockBePlacedAt(placedID, x, y, z, false, face)) {
			Block block = Block.blocksList[placedID];

			if (pos.setBlock(world, placedID, placedMeta)) {
				if (pos.getId(world) == placedID) {
					Block.blocksList[placedID].onBlockPlaced(world, x, y, z, face);

					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

					if (itemstack.hasTagCompound()) {
						NBTTagCompound tag = itemstack.getTagCompound();

						TileEntity tec = pos.getTileEntity(world);
						if (tec != null) {
							int cx = tec.xCoord;
							int cy = tec.yCoord;
							int cz = tec.zCoord;

							tec.readFromNBT(tag);
							tec.xCoord = cx;
							tec.yCoord = cy;
							tec.zCoord = cz;
							itemstack.setTagCompound(null);
							world.setBlockTileEntity(cx,cy,cz, tec);
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

	/**
	 * Extract and remove chest at given position.
	 * 
	 * @param world the world
	 * @param pos position of the chest
	 * @return itemstack with contents of the harvested chest
	 */
	public static ItemStack extractAndRemoveChest(World world, PC_CoordI pos) {

		if (PC_BlockUtils.hasFlag(world, pos, "NO_PICKUP")) return null;

		TileEntity tec = pos.getTileEntity(world);

		if (tec == null) return null;

		ItemStack stack = new ItemStack(Block.lockedChest.blockID, 1,0);

		NBTTagCompound blocktag = new NBTTagCompound();
		pos.getTileEntity(world).writeToNBT(blocktag);

		String name = "Unknown";
		int dmg = pos.getId(world);

		stack.setItemDamage(dmg);

		blocktag.setString("BlockName", name);
		blocktag.setInteger("BlockMeta", pos.getMeta(world));

		stack.setTagCompound(blocktag);
		if (tec instanceof IInventory) {

			IInventory ic = (IInventory) tec;

			for (int i = 0; i < ic.getSizeInventory(); i++) {
				ic.setInventorySlotContents(i, null);
			}
		}

		tec.invalidate();
		world.removeBlockTileEntity(pos.x, pos.y, pos.z);

		pos.setBlock(world, 0, 0);


		return stack;

	}

	@Override
	public String getItemDisplayName(ItemStack stack) {

		if (stack.hasTagCompound()) {
			ItemStack tmpstack = new ItemStack(stack.getItemDamage(), 1, stack.getTagCompound().getInteger("BlockMeta"));
			String name = Item.itemsList[stack.getItemDamage()].getItemDisplayName(tmpstack);

			if (stack.getItemDamage() == Block.brewingStand.blockID) {
				name = Item.brewingStand.getItemDisplayName(new ItemStack(Item.brewingStand));
			}

			if (PC_BlockUtils.hasFlag(tmpstack, "GATE")) {
				name = PC_Lang.tr("tile.PCloLogicGate.name");
			}

			if (name.trim().equals("")) {
				name = PC_Lang.tr("pc.block.pickedUp.special");
			}

			return PC_Lang.tr("pc.block.pickedUp", new String[] { name });
		} else {
			return super.getItemDisplayName(stack);
		}
	}
}
