package net.minecraft.src;

public class MapInfo
{
    /** Reference for EntityPlayer object in MapInfo */
    public final EntityPlayer entityplayerObj;
    public int[] field_76209_b;
    public int[] field_76210_c;
    private int field_76208_e;
    private int field_76205_f;
    private byte[] field_76206_g;

    /** reference in MapInfo to MapData object */
    final MapData mapDataObj;

    public MapInfo(MapData par1MapData, EntityPlayer par2EntityPlayer)
    {
        this.mapDataObj = par1MapData;
        this.field_76209_b = new int[128];
        this.field_76210_c = new int[128];
        this.field_76208_e = 0;
        this.field_76205_f = 0;
        this.entityplayerObj = par2EntityPlayer;

        for (int var3 = 0; var3 < this.field_76209_b.length; ++var3)
        {
            this.field_76209_b[var3] = 0;
            this.field_76210_c[var3] = 127;
        }
    }

    public byte[] func_76204_a(ItemStack par1ItemStack)
    {
        int var3;
        int var10;

        if (--this.field_76205_f < 0)
        {
            this.field_76205_f = 4;
            byte[] var2 = new byte[this.mapDataObj.playersVisibleOnMap.size() * 3 + 1];
            var2[0] = 1;

            for (var3 = 0; var3 < this.mapDataObj.playersVisibleOnMap.size(); ++var3)
            {
                MapCoord var4 = (MapCoord)this.mapDataObj.playersVisibleOnMap.get(var3);
                var2[var3 * 3 + 1] = (byte)(var4.field_76216_a + (var4.iconRotation & 15) * 16);
                var2[var3 * 3 + 2] = var4.centerX;
                var2[var3 * 3 + 3] = var4.centerZ;
            }

            boolean var9 = true;

            if (this.field_76206_g != null && this.field_76206_g.length == var2.length)
            {
                for (var10 = 0; var10 < var2.length; ++var10)
                {
                    if (var2[var10] != this.field_76206_g[var10])
                    {
                        var9 = false;
                        break;
                    }
                }
            }
            else
            {
                var9 = false;
            }

            if (!var9)
            {
                this.field_76206_g = var2;
                return var2;
            }
        }

        for (int var8 = 0; var8 < 10; ++var8)
        {
            var3 = this.field_76208_e * 11 % 128;
            ++this.field_76208_e;

            if (this.field_76209_b[var3] >= 0)
            {
                var10 = this.field_76210_c[var3] - this.field_76209_b[var3] + 1;
                int var5 = this.field_76209_b[var3];
                byte[] var6 = new byte[var10 + 3];
                var6[0] = 0;
                var6[1] = (byte)var3;
                var6[2] = (byte)var5;

                for (int var7 = 0; var7 < var6.length - 3; ++var7)
                {
                    var6[var7 + 3] = this.mapDataObj.colors[(var7 + var5) * 128 + var3];
                }

                this.field_76210_c[var3] = -1;
                this.field_76209_b[var3] = -1;
                return var6;
            }
        }

        return null;
    }
}
