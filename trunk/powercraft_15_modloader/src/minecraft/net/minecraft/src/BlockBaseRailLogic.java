package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class BlockBaseRailLogic
{
    private World field_94516_b;
    private int field_94517_c;
    private int field_94514_d;
    private int field_94515_e;
    private final boolean field_94512_f;
    private List field_94513_g;

    final BlockRailBase field_94518_a;

    public BlockBaseRailLogic(BlockRailBase par1, World par2, int par3, int par4, int par5)
    {
        this.field_94518_a = par1;
        this.field_94513_g = new ArrayList();
        this.field_94516_b = par2;
        this.field_94517_c = par3;
        this.field_94514_d = par4;
        this.field_94515_e = par5;
        int var6 = par2.getBlockId(par3, par4, par5);
        int var7 = par2.getBlockMetadata(par3, par4, par5);

        if (((BlockRailBase)Block.blocksList[var6]).isPowered)
        {
            this.field_94512_f = true;
            var7 &= -9;
        }
        else
        {
            this.field_94512_f = false;
        }

        this.func_94504_a(var7);
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
        for (int var1 = 0; var1 < this.field_94513_g.size(); ++var1)
        {
            BlockBaseRailLogic var2 = this.func_94501_a((ChunkPosition)this.field_94513_g.get(var1));

            if (var2 != null && var2.func_94508_a(this))
            {
                this.field_94513_g.set(var1, new ChunkPosition(var2.field_94517_c, var2.field_94514_d, var2.field_94515_e));
            }
            else
            {
                this.field_94513_g.remove(var1--);
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
        for (int var2 = 0; var2 < this.field_94513_g.size(); ++var2)
        {
            ChunkPosition var3 = (ChunkPosition)this.field_94513_g.get(var2);

            if (var3.x == par1BlockBaseRailLogic.field_94517_c && var3.z == par1BlockBaseRailLogic.field_94515_e)
            {
                return true;
            }
        }

        return false;
    }

    private boolean func_94510_b(int par1, int par2, int par3)
    {
        for (int var4 = 0; var4 < this.field_94513_g.size(); ++var4)
        {
            ChunkPosition var5 = (ChunkPosition)this.field_94513_g.get(var4);

            if (var5.x == par1 && var5.z == par3)
            {
                return true;
            }
        }

        return false;
    }

    protected int func_94505_a()
    {
        int var1 = 0;

        if (this.func_94502_a(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1))
        {
            ++var1;
        }

        if (this.func_94502_a(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1))
        {
            ++var1;
        }

        if (this.func_94502_a(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e))
        {
            ++var1;
        }

        if (this.func_94502_a(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e))
        {
            ++var1;
        }

        return var1;
    }

    private boolean func_94507_b(BlockBaseRailLogic par1BlockBaseRailLogic)
    {
        return this.func_94508_a(par1BlockBaseRailLogic) ? true : (this.field_94513_g.size() == 2 ? false : (this.field_94513_g.isEmpty() ? true : true));
    }

    private void func_94506_c(BlockBaseRailLogic par1BlockBaseRailLogic)
    {
        this.field_94513_g.add(new ChunkPosition(par1BlockBaseRailLogic.field_94517_c, par1BlockBaseRailLogic.field_94514_d, par1BlockBaseRailLogic.field_94515_e));
        boolean var2 = this.func_94510_b(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1);
        boolean var3 = this.func_94510_b(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1);
        boolean var4 = this.func_94510_b(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e);
        boolean var5 = this.func_94510_b(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e);
        byte var6 = -1;

        if (var2 || var3)
        {
            var6 = 0;
        }

        if (var4 || var5)
        {
            var6 = 1;
        }

        if (!this.field_94512_f)
        {
            if (var3 && var5 && !var2 && !var4)
            {
                var6 = 6;
            }

            if (var3 && var4 && !var2 && !var5)
            {
                var6 = 7;
            }

            if (var2 && var4 && !var3 && !var5)
            {
                var6 = 8;
            }

            if (var2 && var5 && !var3 && !var4)
            {
                var6 = 9;
            }
        }

        if (var6 == 0)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e - 1))
            {
                var6 = 4;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e + 1))
            {
                var6 = 5;
            }
        }

        if (var6 == 1)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c + 1, this.field_94514_d + 1, this.field_94515_e))
            {
                var6 = 2;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c - 1, this.field_94514_d + 1, this.field_94515_e))
            {
                var6 = 3;
            }
        }

        if (var6 < 0)
        {
            var6 = 0;
        }

        int var7 = var6;

        if (this.field_94512_f)
        {
            var7 = this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) & 8 | var6;
        }

        this.field_94516_b.setBlockMetadataWithNotify(this.field_94517_c, this.field_94514_d, this.field_94515_e, var7, 3);
    }

    private boolean func_94503_c(int par1, int par2, int par3)
    {
        BlockBaseRailLogic var4 = this.func_94501_a(new ChunkPosition(par1, par2, par3));

        if (var4 == null)
        {
            return false;
        }
        else
        {
            var4.func_94509_b();
            return var4.func_94507_b(this);
        }
    }

    public void func_94511_a(boolean par1, boolean par2)
    {
        boolean var3 = this.func_94503_c(this.field_94517_c, this.field_94514_d, this.field_94515_e - 1);
        boolean var4 = this.func_94503_c(this.field_94517_c, this.field_94514_d, this.field_94515_e + 1);
        boolean var5 = this.func_94503_c(this.field_94517_c - 1, this.field_94514_d, this.field_94515_e);
        boolean var6 = this.func_94503_c(this.field_94517_c + 1, this.field_94514_d, this.field_94515_e);
        byte var7 = -1;

        if ((var3 || var4) && !var5 && !var6)
        {
            var7 = 0;
        }

        if ((var5 || var6) && !var3 && !var4)
        {
            var7 = 1;
        }

        if (!this.field_94512_f)
        {
            if (var4 && var6 && !var3 && !var5)
            {
                var7 = 6;
            }

            if (var4 && var5 && !var3 && !var6)
            {
                var7 = 7;
            }

            if (var3 && var5 && !var4 && !var6)
            {
                var7 = 8;
            }

            if (var3 && var6 && !var4 && !var5)
            {
                var7 = 9;
            }
        }

        if (var7 == -1)
        {
            if (var3 || var4)
            {
                var7 = 0;
            }

            if (var5 || var6)
            {
                var7 = 1;
            }

            if (!this.field_94512_f)
            {
                if (par1)
                {
                    if (var4 && var6)
                    {
                        var7 = 6;
                    }

                    if (var5 && var4)
                    {
                        var7 = 7;
                    }

                    if (var6 && var3)
                    {
                        var7 = 9;
                    }

                    if (var3 && var5)
                    {
                        var7 = 8;
                    }
                }
                else
                {
                    if (var3 && var5)
                    {
                        var7 = 8;
                    }

                    if (var6 && var3)
                    {
                        var7 = 9;
                    }

                    if (var5 && var4)
                    {
                        var7 = 7;
                    }

                    if (var4 && var6)
                    {
                        var7 = 6;
                    }
                }
            }
        }

        if (var7 == 0)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e - 1))
            {
                var7 = 4;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c, this.field_94514_d + 1, this.field_94515_e + 1))
            {
                var7 = 5;
            }
        }

        if (var7 == 1)
        {
            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c + 1, this.field_94514_d + 1, this.field_94515_e))
            {
                var7 = 2;
            }

            if (BlockRailBase.isRailBlockAt(this.field_94516_b, this.field_94517_c - 1, this.field_94514_d + 1, this.field_94515_e))
            {
                var7 = 3;
            }
        }

        if (var7 < 0)
        {
            var7 = 0;
        }

        this.func_94504_a(var7);
        int var8 = var7;

        if (this.field_94512_f)
        {
            var8 = this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) & 8 | var7;
        }

        if (par2 || this.field_94516_b.getBlockMetadata(this.field_94517_c, this.field_94514_d, this.field_94515_e) != var8)
        {
            this.field_94516_b.setBlockMetadataWithNotify(this.field_94517_c, this.field_94514_d, this.field_94515_e, var8, 3);

            for (int var9 = 0; var9 < this.field_94513_g.size(); ++var9)
            {
                BlockBaseRailLogic var10 = this.func_94501_a((ChunkPosition)this.field_94513_g.get(var9));

                if (var10 != null)
                {
                    var10.func_94509_b();

                    if (var10.func_94507_b(this))
                    {
                        var10.func_94506_c(this);
                    }
                }
            }
        }
    }
}
