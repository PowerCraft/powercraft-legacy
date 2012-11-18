package cpw.mods.fml.common;

import java.util.Random;

import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface IDispenserHandler
{
    int dispense(int x, int y, int z, int xVelocity, int zVelocity, World world, ItemStack item, Random random, double entX, double entY, double entZ);
}
