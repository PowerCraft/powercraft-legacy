package net.minecraft.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class BlockBaseRailLogic
{
    private World field_94516_b;
    private int field_94517_c;
    private int field_94514_d;
    private int field_94515_e;
    private final boolean field_94512_f;
    private List field_94513_g;

    private final boolean canMakeSlopes;

    final BlockRailBase field_94518_a;

    public BlockBaseRailLogic(BlockRailBase par1, World par2, int par3, int par4, int par5)
    {
        this.field_94518_a = par1;
        this.field_94513_g = new ArrayList();
        this.field_94516_b = par2;
        this.field_94517_c = par3;
        this.field_94514_d = par4;
        this.field_94515_e = par5;
        int l = par2.getBlockId(par3, par4, par5);

        BlockRailBase target = (BlockRailBase)Block.blocksList[l];
        int i1 = target.getBasicRailMetadata(par2, null, par3, par4, par5);
        field_94512_f = !target.isFlexibleRail(par2, par3, par4, par5);
        canMakeSlopes = target.canMakeSlopes(par2, par3, par4, par5);

        this.func_94504_a(i1);
    }

    private void func_94504_a(int par1)
    {
        this.field_94513_g.clear();

        if (par1 == 0)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1));
        }
        else if (par1 == 1)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e));
        }
        else if (par1 == 2)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c + 1, this.field_94514_d + 1, this.field_94515_e));
        }
        else if (par1 == 3)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c - 1, this.field_94514_d + 1, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e));
        }
        else if (par1 == 4)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d + 1, this.field_94515_e - 1));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1));
        }
        else if (par1 == 5)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d + 1, this.field_94515_e + 1));
        }
        else if (par1 == 6)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1));
        }
        else if (par1 == 7)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1));
        }
        else if (par1 == 8)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1));
        }
        else if (par1 == 9)
        {
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e));
            this.field_94513_g.add(new ChunkPosition(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1));
        }
    }

    private void func_94509_b()
    {
        for (int i = 0; i < this.field_94513_g.size(); ++i)
        {
            BlockBaseRailLogic blockbaseraillogic = this.func_94501_a((ChunkPosition)this.field_94513_g.get(i));

            if (blockbaseraillogic != null && blockbaseraillogic.func_94508_a(this))
            {
                this.field_94513_g.set(i, new ChunkPosition(blockbaseraillogic.field_94517_c, blockbaseraillogic.field_94514_d, blockbaseraillogic.field_94515_e));
            }
            else
            {
                this.field_94513_g.remove(i--);
            }
        }
    }

    private boolean func_94502_a(int par1, int par2, int par3)
    {
        return BlockRailBase.isRailBlockAt(this.field_94516_b, par1, par2, par3) ? true : (BlockRailBase.isRailBlockAt(this.field_94516_b, par1, par2 + 1, par3) ? true : BlockRailBase.isRailBlockAt(this.field_94516_b, par1, par2 - 1, par3));
    }

    private BlockBaseRailLogic func_94501_a(ChunkPosition par1ChunkPosition)
    {
        return BlockRailBase.isRailBlockAt(this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y, par1ChunkPosition.z) ? new BlockBaseRailLogic(this.field_94518_a, this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y, par1ChunkPosition.z) : (BlockRailBase.isRailBlockAt(this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y + 1, par1ChunkPosition.z) ? new BlockBaseRailLogic(this.field_94518_a, this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y + 1, par1ChunkPosition.z) : (BlockRailBase.isRailBlockAt(this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y - 1, par1ChunkPosition.z) ? new BlockBaseRailLogic(this.field_94518_a, this.field_94516_b, par1ChunkPosition.x, par1ChunkPosition.y - 1, par1ChunkPosition.z) : null));
    }

    private boolean func_94508_a(BlockBaseRailLogic par1BlockBaseRailLogic)
    {
        for (int i = 0; i < this.field_94513_g.size(); ++i)
        {
            ChunkPosition chunkposition = (ChunkPosition)this.field_94513_g.get(i);

            if (chunkposition.x == par1BlockBaseRailLogic.field_94517_c && chunkposition.z == par1BlockBaseRailLogic.field_94515_e)
            {
                return true;
            }
        }

        return false;
    }

    private boolean func_94510_b(int par1, int par2, int par3)
    {
        for (int l = 0; l < this.field_94513_g.size(); ++l)
        {
            ChunkPosition chunkposition = (ChunkPosition)this.field_94513_g.get(l);

            if (chunkposition.x == par1 && chunkposition.z == par3)
            {
                return true;
            }
        }

        return false;
    }

    public int func_94505_a()
    {
        int i = 0;

        if (this.func_94502_a(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1))
        {
            ++i;
        }

        if (this.func_94502_a(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1))
        {
            ++i;
        }

        if (this.func_94502_a(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e))
        {
            ++i;
        }

        if (this.func_94502_a(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e))
        {
            ++i;
        }

        return i;
    }

    private boolean func_94507_b(BlockBaseRailLogic par1BlockBaseRailLogic)
    {
        return this.func_94508_a(par1BlockBaseRailLogic) ? true : (this.field_94513_g.size() == 2 ? false : (this.field_94513_g.isEmpty() ? true : true));
    }

    private void func_94506_c(BlockBaseRailLogic par1BlockBaseRailLogic)
    {
        this.field_94513_g.add(new ChunkPosition(par1BlockBaseRailLogic.field_94517_c, par1BlockBaseRailLogic.field_94514_d, par1BlockBaseRailLogic.field_94515_e));
        boolean flag = this.func_94510_b(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1);
        boolean flag1 = this.func_94510_b(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1);
        boolean flag2 = this.func_94510_b(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e);
        boolean flag3 = this.func_94510_b(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e);
        byte b0 = -1;

        if (flag || flag1)
        {
            b0 = 0;
        }

        if (flag2 || flag3)
        {
            b0 = 1;
        }

        if (!this.field_94512_f)
        {
            if (flag1 && flag3 && !flag && !flag2)
            {
                b0 = 6;
            }

            if (flag1 && flag2 && !flag && !flag3)
            {
                b0 = 7;
            }

            if (flag && flag2 && !flag1 && !flag3)
            {
                b0 = 8;
            }

            if (flag && flag3 && !flag1 && !flag2)
            {
                b0 = 9;
            }
        }

        if (b0 == 0 && canMakeSlopes)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e - 1))
            {
                b0 = 4;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e + 1))
            {
                b0 = 5;
            }
        }

        if (b0 == 1 && canMakeSlopes)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c + 1, this.field_94514_d + 1, this.field_94515_e))
            {
                b0 = 2;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c - 1, this.field_94514_d + 1, this.field_94515_e))
            {
                b0 = 3;
            }
        }

        if (b0 < 0)
        {
            b0 = 0;
        }

        int i = b0;

        if (this.field_94512_f)
        {
            i = this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) & 8 | b0;
        }

        this.field_94516_b.setBlockMetadataWithNotify(this.field_94517_c, this.field_94514_d, this.field_94515_e, i, 3);
    }

    private boolean func_94503_c(int par1, int par2, int par3)
    {
        BlockBaseRailLogic blockbaseraillogic = this.func_94501_a(new ChunkPosition(par1, par2, par3));

        if (blockbaseraillogic == null)
        {
            return false;
        }
        else
        {
            blockbaseraillogic.func_94509_b();
            return blockbaseraillogic.func_94507_b(this);
        }
    }

    public void func_94511_a(boolean par1, boolean par2)
    {
        boolean flag2 = this.func_94503_c(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1);
        boolean flag3 = this.func_94503_c(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1);
        boolean flag4 = this.func_94503_c(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e);
        boolean flag5 = this.func_94503_c(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e);
        byte b0 = -1;

        if ((flag2 || flag3) && !flag4 && !flag5)
        {
            b0 = 0;
        }

        if ((flag4 || flag5) && !flag2 && !flag3)
        {
            b0 = 1;
        }

        if (!this.field_94512_f)
        {
            if (flag3 && flag5 && !flag2 && !flag4)
            {
                b0 = 6;
            }

            if (flag3 && flag4 && !flag2 && !flag5)
            {
                b0 = 7;
            }

            if (flag2 && flag4 && !flag3 && !flag5)
            {
                b0 = 8;
            }

            if (flag2 && flag5 && !flag3 && !flag4)
            {
                b0 = 9;
            }
        }

        if (b0 == -1)
        {
            if (flag2 || flag3)
            {
                b0 = 0;
            }

            if (flag4 || flag5)
            {
                b0 = 1;
            }

            if (!this.field_94512_f)
            {
                if (par1)
                {
                    if (flag3 && flag5)
                    {
                        b0 = 6;
                    }

                    if (flag4 && flag3)
                    {
                        b0 = 7;
                    }

                    if (flag5 && flag2)
                    {
                        b0 = 9;
                    }

                    if (flag2 && flag4)
                    {
                        b0 = 8;
                    }
                }
                else
                {
                    if (flag2 && flag4)
                    {
                        b0 = 8;
                    }

                    if (flag5 && flag2)
                    {
                        b0 = 9;
                    }

                    if (flag4 && flag3)
                    {
                        b0 = 7;
                    }

                    if (flag3 && flag5)
                    {
                        b0 = 6;
                    }
                }
            }
        }

        if (b0 == 0 && canMakeSlopes)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e - 1))
            {
                b0 = 4;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e + 1))
            {
                b0 = 5;
            }
        }

        if (b0 == 1 && canMakeSlopes)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c + 1, this.field_94514_d + 1, this.field_94515_e))
            {
                b0 = 2;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c - 1, this.field_94514_d + 1, this.field_94515_e))
            {
                b0 = 3;
            }
        }

        if (b0 < 0)
        {
            b0 = 0;
        }

        this.func_94504_a(b0);
        int i = b0;

        if (this.field_94512_f)
        {
            i = this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) & 8 | b0;
        }

        if (par2 || this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) != i)
        {
            this.field_94516_b.setBlockMetadataWithNotify(this.field_94517_c, this.field_94514_d, this.field_94515_e, i, 3);

            for (int j = 0; j < this.field_94513_g.size(); ++j)
            {
                BlockBaseRailLogic blockbaseraillogic = this.func_94501_a((ChunkPosition)this.field_94513_g.get(j));

                if (blockbaseraillogic != null)
                {
                    blockbaseraillogic.func_94509_b();

                    if (blockbaseraillogic.func_94507_b(this))
                    {
                        blockbaseraillogic.func_94506_c(this);
                    }
                }
            }
        }
    }
}
