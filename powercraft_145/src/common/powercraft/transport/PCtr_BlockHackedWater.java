package powercraft.transport;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.management.PC_Logger;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCtr_BlockHackedWater extends BlockStationary
{
    private static PCtr_BlockHackedWater instance = null;
    private static Random rand = new Random();

    public static PCtr_BlockHackedWater hackWater()
    {
        if (instance == null)
        {
            Block.blocksList[Block.waterStill.blockID] = null;
            instance = new PCtr_BlockHackedWater();
            PC_Utils.setPrivateValue(Block.class, Block.class, 32, instance);
            PC_Logger.info("Water Hacked");
        }

        return instance;
    }

    private PCtr_BlockHackedWater()
    {
        super(Block.waterStill.blockID, Material.water);
        setHardness(100F);
        setLightOpacity(3);
        setBlockName("water");
        disableStats();
        setRequiresSelfNotify();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        super.onEntityCollidedWithBlock(world, i, j, k, entity);

        if (entity.stepHeight < 0.25F)
        {
            entity.stepHeight = 0.25F;
        }

        if (entity instanceof EntityItem)
        {
            if (rand.nextFloat() < 0.8F && ((EntityItem) entity).age % 20 == 0 && world.getBlockMetadata(i, j, k) != 0)
            {
                PCtr_BeltHelper.storeAllSides(world, new PC_VecI(i, j, k), (EntityItem) entity);
                PCtr_BeltHelper.packItems(world, new PC_VecI(i, j, k));
            }

            if (entity.motionY < 0)
            {
                entity.motionY *= 0.6F;
            }
        }
    }
}
