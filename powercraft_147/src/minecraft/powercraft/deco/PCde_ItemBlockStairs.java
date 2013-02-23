package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;

public class PCde_ItemBlockStairs extends PC_ItemBlock {

	public PCde_ItemBlockStairs(int id) {
		super(id);
		setMaxDamage(0);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		int id = world.getBlockId(i, j, k);

		if (id == Block.snow.blockID) {
			l = 1;
		} else if (id != Block.vine.blockID && id != Block.tallGrass.blockID && id != Block.deadBush.blockID) {
			if (l == 0) {
				j--;
			}

			if (l == 1) {
				j++;
			}

			if (l == 2) {
				k--;
			}

			if (l == 3) {
				k++;
			}

			if (l == 4) {
				i--;
			}

			if (l == 5) {
				i++;
			}
		}

		if (itemstack.stackSize == 0) {
			return false;
		}else if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }



		// special placing rules for Ledge
		int bID = GameInfo.getBID(world, i, j - 1, k);
		
		if (bID == PCde_App.stairs.blockID || bID == PCde_App.platform.blockID) {


			int dir = ((MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

			int meta = world.getBlockMetadata(i, j - 1, k);

			i -= Direction.offsetX[dir];
			k -= Direction.offsetZ[dir];

			if (bID == PCde_App.stairs.blockID) {

				if (meta == dir) {

					if (!GameInfo.isPlacingReversed(entityplayer)) {
						j += 1;
					}

				} else if (GameInfo.isPlacingReversed(entityplayer)) {
					j--;
				}

			} else if (bID == PCde_App.platform.blockID) {
				if (GameInfo.isPlacingReversed(entityplayer)) {
					j--;
				}
			}

			j--;
		}


		if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return false;
		}

		if (world.canPlaceEntityOnSide(PCde_App.stairs.blockID, i, j, k, false, l, entityplayer)) {
			Block block = PCde_App.stairs;
			if (world.setBlock(i, j, k, block.blockID)) {
				// set tile entity
				PCde_TileEntityStairs ted = (PCde_TileEntityStairs) world.getBlockTileEntity(i, j, k);
				if (ted == null) {
					ted = (PCde_TileEntityStairs) ((BlockContainer) block).createNewTileEntity(world);
				}
				world.setBlockTileEntity(i, j, k, ted);
				//block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer);

				world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
				world.markBlockForUpdate(i, j, k);

				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);

				itemstack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Stairs"));
            return names;
		}
		return null;
	}

}
