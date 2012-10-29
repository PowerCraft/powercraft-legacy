package net.minecraft.src;

import java.util.Comparator;

public class PlayerPositionComparator implements Comparator
{
    private final ChunkCoordinates field_82548_a;

    public PlayerPositionComparator(ChunkCoordinates par1ChunkCoordinates)
    {
        this.field_82548_a = par1ChunkCoordinates;
    }

    public int func_82547_a(EntityPlayerMP par1EntityPlayerMP, EntityPlayerMP par2EntityPlayerMP)
    {
        double var3 = par1EntityPlayerMP.getDistanceSq((double)this.field_82548_a.posX, (double)this.field_82548_a.posY, (double)this.field_82548_a.posZ);
        double var5 = par2EntityPlayerMP.getDistanceSq((double)this.field_82548_a.posX, (double)this.field_82548_a.posY, (double)this.field_82548_a.posZ);
        return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.func_82547_a((EntityPlayerMP)par1Obj, (EntityPlayerMP)par2Obj);
    }
}
