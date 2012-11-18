package cpw.mods.fml.common;

import java.util.Random;

import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface IDispenseHandler
{
    @Deprecated
    int dispense(double x, double y, double z, int xVelocity, int zVelocity, World world, ItemStack item, Random random, double entX, double entY, double entZ);
}
