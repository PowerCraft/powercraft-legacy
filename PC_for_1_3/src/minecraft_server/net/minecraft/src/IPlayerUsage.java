package net.minecraft.src;

public interface IPlayerUsage
{
    void func_70000_a(PlayerUsageSnooper var1);

    void func_70001_b(PlayerUsageSnooper var1);

    /**
     * Returns whether snooping is enabled or not.
     */
    boolean isSnooperEnabled();
}
