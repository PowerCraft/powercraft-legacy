package net.minecraft.src;

public interface IWorldAccess
{
    void markBlockNeedsUpdate(int var1, int var2, int var3);

    void markBlockNeedsUpdate2(int var1, int var2, int var3);

    void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6);

    void playSound(String var1, double var2, double var4, double var6, float var8, float var9);

    void func_85102_a(EntityPlayer var1, String var2, double var3, double var5, double var7, float var9, float var10);

    void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12);

    void obtainEntitySkin(Entity var1);

    void releaseEntitySkin(Entity var1);

    void playRecord(String var1, int var2, int var3, int var4);

    void func_82746_a(int var1, int var2, int var3, int var4, int var5);

    void playAuxSFX(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6);

    void destroyBlockPartially(int var1, int var2, int var3, int var4, int var5);
}
