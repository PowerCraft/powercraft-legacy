package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockMushroomCap extends Block
{
    private static final String[] field_94429_a = new String[] {"mushroom_skin_brown", "mushroom_skin_red"};

    /** The mushroom type. 0 for brown, 1 for red. */
    private final int mushroomType;
    @SideOnly(Side.CLIENT)
    private Icon[] field_94428_c;
    @SideOnly(Side.CLIENT)
    private Icon field_94426_cO;
    @SideOnly(Side.CLIENT)
    private Icon field_94427_cP;

    public BlockMushroomCap(int par1, Material par2Material, int par3)
    {
        super(par1, par2Material);
        this.mushroomType = par3;
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par2 == 10 && par1 > 1 ? this.field_94426_cO : (par2 >= 1 && par2 <= 9 && par1 == 1 ? this.field_94428_c[this.mushroomType] : (par2 >= 1 && par2 <= 3 && par1 == 2 ? this.field_94428_c[this.mushroomType] : (par2 >= 7 && par2 <= 9 && par1 == 3 ? this.field_94428_c[this.mushroomType] : ((par2 == 1 || par2 == 4 || par2 == 7) && par1 == 4 ? this.field_94428_c[this.mushroomType] : ((par2 == 3 || par2 == 6 || par2 == 9) && par1 == 5 ? this.field_94428_c[this.mushroomType] : (par2 == 14 ? this.field_94428_c[this.mushroomType] : (par2 == 15 ? this.field_94426_cO : this.field_94427_cP)))))));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        int i = par1Random.nextInt(10) - 7;

        if (i < 0)
        {
            i = 0;
        }

        return i;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.mushroomBrown.blockID + this.mushroomType;
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return Block.mushroomBrown.blockID + this.mushroomType;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.field_94428_c = new Icon[field_94429_a.length];

        for (int i = 0; i < this.field_94428_c.length; ++i)
        {
            this.field_94428_c[i] = par1IconRegister.func_94245_a(field_94429_a[i]);
        }

        this.field_94427_cP = par1IconRegister.func_94245_a("mushroom_inside");
        this.field_94426_cO = par1IconRegister.func_94245_a("mushroom_skin_stem");
    }
}
