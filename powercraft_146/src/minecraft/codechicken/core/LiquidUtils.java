package codechicken.core;

import net.minecraft.block.Block;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidUtils
{
    public static int B = LiquidContainerRegistry.BUCKET_VOLUME;
    public static LiquidStack water = new LiquidStack(Block.waterStill.blockID, 1000);
    public static LiquidStack lava = new LiquidStack(Block.lavaStill.blockID, 1000);
}
