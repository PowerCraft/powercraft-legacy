package powercraft.deco;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.utils.PC_Utils;

public class PCde_ItemBlockPlatform extends PC_ItemBlock {

	public PCde_ItemBlockPlatform(int id) {
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
		
		int bID = PC_Utils.getBID(world, i, j - 1, k);
		
		if (bID == PCde_App.stairs.blockID || bID == PCde_App.platform.blockID) {

			int dir = ((MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

			int meta = world.getBlockMetadata(i, j - 1, k);

			i -= Direction.offsetX[dir];
			k -= Direction.offsetZ[dir];

			if (bID == PCde_App.stairs.blockID) {

				if (meta == dir) {

					if (!PC_KeyRegistry.isPlacingReversed(entityplayer)) {
						j ++;
					}

				}

			} 
			
			j--;
			
		}


		if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return false;
		}

		if (world.canPlaceEntityOnSide(PCde_App.platform.blockID, i, j, k, false, l, entityplayer, itemstack)) {
			Block block = PCde_App.platform;
			if(PC_Utils.setBID(world, i, j, k, block.blockID, 0)){
				// set tile entity
				PCde_TileEntityPlatform ted = (PCde_TileEntityPlatform) world.getBlockTileEntity(i, j, k);
				if (ted == null) {
					ted = (PCde_TileEntityPlatform) ((BlockContainer) block).createNewTileEntity(world);
				}
				world.setBlockTileEntity(i, j, k, ted);
				//block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer, itemstack);
				
				world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
				world.markBlockForUpdate(i, j, k);

				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);

				itemstack.stackSize--;
			}
		}
		return true;
	}

}
