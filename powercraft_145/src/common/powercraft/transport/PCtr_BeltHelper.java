package powercraft.transport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;
import powercraft.management.PC_IInventoryWrapper;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Utils;

public class PCtr_BeltHelper
{
    public static final float HEIGHT = 0.0625F;

    public static final float HEIGHT_SELECTED = HEIGHT;

    public static final float HEIGHT_COLLISION = HEIGHT;

    public static final double MAX_HORIZONTAL_SPEED = 0.5F;

    public static final double HORIZONTAL_BOOST = 0.14D;

    public static final double BORDERS = 0.35D;

    public static final double BORDER_BOOST = 0.063D;

    public static final float STORAGE_BORDER = 0.5F;

    public static final float STORAGE_BORDER_LONG = 0.8F;

    public static final float STORAGE_BORDER_V = 0.6F;

    public static int getRotation(int meta)
    {
        switch (meta)
        {
            case 0:
            case 6:
                return 0;

            case 1:
            case 7:
                return 1;

            case 8:
            case 14:
                return 2;

            case 9:
            case 15:
                return 3;
        }

        return 0;
    }

    public static boolean isEntityIgnored(Entity entity)
    {
        if (entity == null)
        {
            return true;
        }

        if (!entity.isEntityAlive())
        {
            return true;
        }

        if (entity.ridingEntity != null)
        {
            return true;
        }

        if (entity instanceof EntityPlayer)
        {
            if (((EntityPlayer) entity).isSneaking())
            {
                return true;
            }

            if (((EntityPlayer) entity).inventory.armorItemInSlot(0) != null)
            {
                if (((EntityPlayer) entity).inventory.armorItemInSlot(0).itemID == PCtr_App.slimeboots.shiftedIndex)
                {
                    return true;
                }
            }
        }

        if (PC_Utils.isEntityFX(entity))
        {
            return true;
        }

        return false;
    }

    public static void packItems(World world, PC_CoordI pos)
    {
        List<EntityItem> items = world.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class,
                AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1));

        if (items.size() < 5)
        {
            return;
        }

        nextItem:

        for (EntityItem item1 : items)
        {
            if (item1 == null || item1.isDead || item1.item == null)
            {
                continue nextItem;
            }

            if (item1.item.stackSize < 1)
            {
                item1.setDead();
                continue nextItem;
            }

            if (item1.item.isItemStackDamageable())
            {
                continue nextItem;
            }

            if (item1.item.isItemEnchanted())
            {
                continue nextItem;
            }

            if (!item1.item.isStackable())
            {
                continue nextItem;
            }

            ItemStack stackTarget = item1.item;

            if (stackTarget.stackSize == stackTarget.getMaxStackSize())
            {
                continue nextItem;
            }

            for (EntityItem item2 : items)
            {
                if (item2.isDead)
                {
                    continue nextItem;
                }

                ItemStack stackAdded = item2.item;

                if (item2 == item1)
                {
                    continue;
                }

                if (stackTarget.isItemEqual(stackAdded))
                {
                    if (stackTarget.stackSize < stackTarget.getMaxStackSize())
                    {
                        int sizeRemain = stackTarget.getMaxStackSize() - stackTarget.stackSize;

                        if (sizeRemain >= stackAdded.stackSize)
                        {
                            stackTarget.stackSize += stackAdded.stackSize;
                            item2.setDead();
                        }
                        else
                        {
                            stackTarget.stackSize = stackTarget.getMaxStackSize();
                            stackAdded.stackSize -= sizeRemain;
                            continue nextItem;
                        }
                    }
                }
            }
        }
    }

    public static void doSpecialItemAction(World world, PC_CoordI beltPos, EntityItem entity)
    {
        if (entity == null || entity.item == null)
        {
            return;
        }

        boolean flag = false;
        flag |= entity.item.itemID == Item.bucketWater.shiftedIndex;
        flag |= entity.item.itemID == Item.bucketEmpty.shiftedIndex;
        flag |= entity.item.itemID == Item.glassBottle.shiftedIndex;

        if (!flag)
        {
            return;
        }

        do
        {
            if (doSpecialItemAction_do(world, beltPos.offset(0, 0, 1), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(0, 0, -1), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(1, 0, 0), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(-1, 0, 0), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(0, -1, 1), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(0, -1, -1), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(1, -1, 0), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(-1, -1, 0), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(0, 1, 0), entity))
            {
                break;
            }

            if (doSpecialItemAction_do(world, beltPos.offset(0, -1, 0), entity))
            {
                break;
            }
        }
        while (false);
    }

    private static boolean doSpecialItemAction_do(World world, PC_CoordI pos, EntityItem entity)
    {
        if (entity.item.itemID == Item.bucketWater.shiftedIndex)
        {
            if (pos.getId(world) == Block.cauldron.blockID && pos.getMeta(world) < 3)
            {
                pos.setMeta(world, 3);
                entity.item.itemID = Item.bucketEmpty.shiftedIndex;
                return true;
            }
        }

        if (entity.item.itemID == Item.bucketEmpty.shiftedIndex)
        {
            if (pos.getId(world) == Block.waterStill.blockID || pos.getId(world) == Block.waterMoving.blockID && pos.getMeta(world) == 0)
            {
                pos.setBlock(world, 0, 0);
                entity.item.itemID = Item.bucketWater.shiftedIndex;
                return true;
            }
        }

        if (entity.item.itemID == Item.glassBottle.shiftedIndex)
        {
            if (pos.getId(world) == Block.cauldron.blockID && pos.getId(world) > 0)
            {
                int meta = pos.getMeta(world);
                pos.setMeta(world, meta - 1);
                EntityItem entity2 = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Item.potion.shiftedIndex, 1, 0));
                entity2.motionX = entity.motionX;
                entity2.motionY = entity.motionY;
                entity2.motionZ = entity.motionZ;
                entity2.delayBeforeCanPickup = 7;
                world.spawnEntityInWorld(entity2);
                entity.item.stackSize--;

                if (entity.item.stackSize <= 0)
                {
                    entity.item.stackSize = 0;
                    entity.setDead();
                }

                return true;
            }
        }

        return false;
    }

    public static boolean storeNearby(World world, PC_CoordI pos, EntityItem entity, boolean ignoreStorageBorder)
    {
        if (storeItemIntoMinecart(world, pos, entity))
        {
            return true;
        }

        if (!ignoreStorageBorder && entity.posY > pos.y + 1 - STORAGE_BORDER_V)
        {
            return false;
        }

        int rot = getRotation(pos.getMeta(world));

        if (isBeyondStorageBorder(world, rot, pos, entity, STORAGE_BORDER) || ignoreStorageBorder)
        {
            if (rot == 0 && storeEntityItemAt(world, pos.offset(0, 0, -1), entity))
            {
                return true;
            }

            if (rot == 1 && storeEntityItemAt(world, pos.offset(1, 0, 0), entity))
            {
                return true;
            }

            if (rot == 2 && storeEntityItemAt(world, pos.offset(0, 0, 1), entity))
            {
                return true;
            }

            if (rot == 3 && storeEntityItemAt(world, pos.offset(-1, 0, 0), entity))
            {
                return true;
            }

            if (rot != 0 && rot != 2 && storeEntityItemAt(world, pos.offset(0, 0, -1), entity))
            {
                return true;
            }

            if (rot != 1 && rot != 3 && storeEntityItemAt(world, pos.offset(1, 0, 0), entity))
            {
                return true;
            }

            if (rot != 2 && rot != 0 && storeEntityItemAt(world, pos.offset(0, 0, 1), entity))
            {
                return true;
            }

            if (rot != 3 && rot != 1 && storeEntityItemAt(world, pos.offset(-1, 0, 0), entity))
            {
                return true;
            }

            if (storeEntityItemAt(world, pos.offset(0, 1, 0), entity))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean storeItemIntoMinecart(World world, PC_CoordI beltPos, EntityItem entity)
    {
        List<EntityMinecart> hitList = world.getEntitiesWithinAABB(
                EntityMinecart.class,
                AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(1.0D, 1.0D,
                        1.0D));

        if (hitList.size() > 0)
        {
            for (EntityMinecart cart : hitList)
            {
                if (cart == null || cart.minecartType != 1)
                {
                    continue;
                }

                IInventory inventory = cart;

                if (entity != null && entity.isEntityAlive())
                {
                    ItemStack stackToStore = entity.item;

                    if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore))
                    {
                        soundEffectChest(world, beltPos);

                        if (stackToStore.stackSize <= 0)
                        {
                            entity.setDead();
                            stackToStore.stackSize = 0;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean storeEntityItemAt(World world, PC_CoordI inventoryPos, EntityItem entity)
    {
        IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);

        if (inventory != null && entity != null && entity.isEntityAlive())
        {
            ItemStack stackToStore = entity.item;

            if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore))
            {
                soundEffectChest(world, inventoryPos);

                if (stackToStore.stackSize <= 0)
                {
                    entity.setDead();
                    stackToStore.stackSize = 0;
                    return true;
                }
            }
        }

        return false;
    }

    public static void soundEffectChest(World world, PC_CoordI pos)
    {
        PC_Utils.playSound(pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, "random.pop", (world.rand.nextFloat() + 0.7F) / 5.0F,
                0.5F + world.rand.nextFloat() * 0.3F);
    }

    public static boolean isBlocked(World world, PC_CoordI blockPos)
    {
        boolean isWall = !world.isAirBlock(blockPos.x, blockPos.y, blockPos.z) && !isTransporterAt(world, blockPos);

        if (isWall)
        {
            Block block = Block.blocksList[blockPos.getId(world)];

            if (block != null)
            {
                if (!block.blockMaterial.blocksMovement())
                {
                    isWall = false;
                }
            }
        }

        return isWall;
    }

    public static boolean isConveyorAt(World world, PC_CoordI pos)
    {
        int id = pos.getId(world);

        if (id > 0)
        {
            if (Block.blocksList[id] instanceof PCtr_BlockBeltBase)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isTransporterAt(World world, PC_CoordI pos)
    {
        int id = pos.getId(world);

        if (id > 0)
        {
            if (Block.blocksList[id] instanceof PCtr_BlockBeltBase)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isBeyondStorageBorder(World world, int rotation, PC_CoordI beltPos, Entity entity, float border)
    {
        switch (rotation)
        {
            case 0:
                if (entity.posZ > beltPos.z + 1 - border)
                {
                    return false;
                }

                break;

            case 1:
                if (entity.posX < beltPos.x + border)
                {
                    return false;
                }

                break;

            case 2:
                if (entity.posZ < beltPos.z + border)
                {
                    return false;
                }

                break;

            case 3:
                if (entity.posX > beltPos.x + 1 - border)
                {
                    return false;
                }

                break;
        }

        return true;
    }

    public static void entityPreventDespawning(World world, PC_CoordI pos, boolean preventPickup, Entity entity)
    {
        if (entity instanceof EntityItem)
        {
            if (preventPickup)
            {
                ((EntityItem) entity).delayBeforeCanPickup = 7;
            }

            if (((EntityItem) entity).age >= 5000)
            {
                if (world.getEntitiesWithinAABBExcludingEntity(null,
                        AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1)).size() < 40)
                {
                    ((EntityItem) entity).age = 4000;
                }
            }
        }

        if (entity instanceof EntityXPOrb)
        {
            if (((EntityXPOrb) entity).xpOrbAge >= 5000)
            {
                if (world.getEntitiesWithinAABBExcludingEntity(null,
                        AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1)).size() < 40)
                {
                    ((EntityXPOrb) entity).xpOrbAge = 4000;
                }
            }
        }
    }

    public static void moveEntityOnBelt(World world, PC_CoordI pos, Entity entity, boolean bordersEnabled, boolean motionEnabled, int moveDirection,
            double max_horizontal_speed, double horizontal_boost)
    {
        if (motionEnabled && world.rand.nextInt(35) == 0)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(entity,
                    AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1));

            if (world.rand.nextInt(list.size() + 1) == 0)
            {
                soundEffectBelt(world, pos);
            }
        }

        if (entity instanceof EntityItem)
        {
            if (entity.motionY > 0.2F)
            {
                entity.motionY /= 3F;
            }
        }

        if (entity instanceof EntityItem || entity instanceof EntityXPOrb)
        {
            if (entity.motionY > 0.2)
            {
                entity.motionY -= 0.1;
            }
        }

        if (entity.stepHeight <= 0.15F)
        {
            entity.stepHeight = 0.25F;
        }

        float motionX, motionZ;
        motionZ = PC_MathHelper.clamp_float((float) entity.motionZ, (float) - max_horizontal_speed, (float) max_horizontal_speed);
        motionX = PC_MathHelper.clamp_float((float) entity.motionX, (float) - max_horizontal_speed, (float) max_horizontal_speed);

        switch (moveDirection)
        {
            case 0:
                if (motionZ >= -max_horizontal_speed && motionEnabled)
                {
                    entity.addVelocity(0, 0, -horizontal_boost);
                }

                if (bordersEnabled)
                {
                    if (entity.posX > pos.x + (1D - BORDERS))
                    {
                        entity.addVelocity(-BORDER_BOOST, 0, 0);
                    }

                    if (entity.posX < pos.x + BORDERS)
                    {
                        entity.addVelocity(BORDER_BOOST, 0, 0);
                    }
                }

                break;

            case 1:
                if (motionX <= max_horizontal_speed && motionEnabled)
                {
                    entity.addVelocity(horizontal_boost, 0, 0);
                }

                if (bordersEnabled)
                {
                    if (entity.posZ > pos.z + BORDERS)
                    {
                        entity.addVelocity(0, 0, -BORDER_BOOST);
                    }

                    if (entity.posZ < pos.z + (1D - BORDERS))
                    {
                        entity.addVelocity(0, 0, BORDER_BOOST);
                    }
                }

                break;

            case 2:
                if (motionZ <= max_horizontal_speed && motionEnabled)
                {
                    entity.addVelocity(0, 0, horizontal_boost);
                }

                if (bordersEnabled)
                {
                    if (entity.posX > pos.x + (1D - BORDERS))
                    {
                        entity.addVelocity(-BORDER_BOOST, 0, 0);
                    }

                    if (entity.posX < pos.x + BORDERS)
                    {
                        entity.addVelocity(BORDER_BOOST, 0, 0);
                    }
                }

                break;

            case 3:
                if (motionX >= -max_horizontal_speed && motionEnabled)
                {
                    entity.addVelocity(-horizontal_boost, 0, 0);
                }

                if (bordersEnabled)
                {
                    if (entity.posZ > pos.z + BORDERS)
                    {
                        entity.addVelocity(0, 0, -BORDER_BOOST);
                    }

                    if (entity.posZ < pos.z + (1D - BORDERS))
                    {
                        entity.addVelocity(0, 0, BORDER_BOOST);
                    }
                }

                break;
        }

        if (entity.riddenByEntity != null)
        {
            entity.updateRiderPosition();
        }
    }

    public static void soundEffectBelt(World world, PC_CoordI pos)
    {
        PC_Utils.playSound(pos.x + 0.5D, pos.y + 0.625D, pos.z + 0.5D, "random.wood click", (world.rand.nextFloat() + 0.2F) / 10.0F,
                1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.6F);
    }

    public static int getPlacedMeta(EntityLiving player)
    {
        int l = PC_MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 2.5D) & 3;

        if (player instanceof EntityPlayer && PC_Utils.isPlacingReversed(((EntityPlayer)player)))
        {
            l = PC_Utils.reverseSide(l);
        }

        if (l == 2)
        {
            l = 8;
        }

        if (l == 3)
        {
            l = 9;
        }

        return l;
    }

    public static boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        ItemStack stack = entityplayer.getCurrentEquippedItem();

        if (stack == null)
        {
            return false;
        }

        Item equip_item = stack.getItem();

        if (equip_item instanceof ItemMinecart)
        {
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityMinecart(world, i + 0.5F, j + 0.5F, k + 0.5F, ((ItemMinecart) equip_item).minecartType));
            }

            if (!PC_Utils.isCreative(entityplayer))
            {
                entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
            }

            return true;
        }

        return false;
    }

    public static boolean isActive(int meta)
    {
        return meta == getActiveMeta(meta);
    }

    public static int getActiveMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return 6;

            case 1:
                return 7;

            case 8:
                return 14;

            case 9:
                return 15;
        }

        return meta;
    }

    public static int getPassiveMeta(int meta)
    {
        switch (meta)
        {
            case 6:
                return 0;

            case 7:
                return 1;

            case 14:
                return 8;

            case 15:
                return 9;
        }

        return meta;
    }

    public static int getMeta(int meta, boolean on)
    {
        if (on)
        {
            return getActiveMeta(meta);
        }

        return getPassiveMeta(meta);
    }

    public static boolean storeAllSides(World world, PC_CoordI pos, EntityItem entity)
    {
        if (storeItemIntoMinecart(world, pos, entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(0, 0, -1), entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(0, 0, 1), entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(-1, 0, 0), entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(1, 0, 0), entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(0, 1, 0), entity))
        {
            return true;
        }

        if (storeEntityItemAt(world, pos.offset(0, -1, 0), entity))
        {
            return true;
        }

        return false;
    }

    public static boolean dispenseFromInventoryAt(World world, PC_CoordI inventoryPos, PC_CoordI beltPos)
    {
        IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);

        if (inventory == null)
        {
            return false;
        }

        return dispenseItemOntoBelt(world, inventoryPos, inventory, beltPos);
    }

    public static void tryToDispenseItem(World world, PC_CoordI beltPos)
    {
        int rot = getRotation(beltPos.getMeta(world));

        if (rot == 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos))
        {
            return;
        }

        if (rot == 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos))
        {
            return;
        }

        if (rot == 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos))
        {
            return;
        }

        if (rot == 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos))
        {
            return;
        }

        if (rot != 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos))
        {
            return;
        }

        if (rot != 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos))
        {
            return;
        }

        if (rot != 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos))
        {
            return;
        }

        if (rot != 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos))
        {
            return;
        }
    }

    public static boolean dispenseItemOntoBelt(World world, PC_CoordI invPos, IInventory inventory, PC_CoordI beltPos)
    {
        ItemStack[] stacks = dispenseStuffFromInventory(world, beltPos, inventory);

        if (stacks != null)
        {
            stacks = PC_InvUtils.groupStacks(stacks);

            for (ItemStack stack : stacks)
            {
                createEntityItemOnBelt(world, invPos, beltPos, stack);
            }

            return true;
        }

        return false;
    }

    public static boolean dispenseStackFromNearbyMinecart(World world, PC_CoordI beltPos)
    {
        List<Entity> hitList = world.getEntitiesWithinAABB(
                IInventory.class,
                AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(0.6D, 0.6D,
                        0.6D));

        if (hitList.size() > 0)
        {
            for (Entity entityWithInventory : hitList)
            {
                if (dispenseItemOntoBelt(world, new PC_CoordD(entityWithInventory.posX, entityWithInventory.posY, entityWithInventory.posZ).round(),
                        (IInventory) entityWithInventory, beltPos))
                {
                    return true;
                }
            }
        }

        List<Entity> hitList2 = world.getEntitiesWithinAABB(
                PC_IInventoryWrapper.class,
                AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(0.6D, 0.6D,
                        0.6D));

        if (hitList2.size() > 0)
        {
            for (Entity entityWithInventory : hitList2)
            {
                if (((PC_IInventoryWrapper)entityWithInventory).getInventory() != null)
                {
                    if (dispenseItemOntoBelt(world, new PC_CoordD(entityWithInventory.posX, entityWithInventory.posY, entityWithInventory.posZ).round(),
                            ((PC_IInventoryWrapper) entityWithInventory).getInventory(), beltPos))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    static boolean isSpecialContainer(IInventory inventory)
    {
        boolean flag = false;
        flag |= inventory instanceof TileEntityFurnace;
        flag |= inventory instanceof TileEntityBrewingStand;
        return flag;
    }

    static ItemStack dispenseFromSpecialContainer(IInventory inventory)
    {
        if (inventory instanceof TileEntityFurnace)
        {
            ItemStack stack = inventory.getStackInSlot(2);

            if (stack != null && stack.stackSize > 0)
            {
                inventory.setInventorySlotContents(2, null);
                return stack;
            }

            return null;
        }

        if (inventory instanceof TileEntityBrewingStand)
        {
            if (((TileEntityBrewingStand) inventory).getBrewTime() != 0)
            {
                return null;
            }

            for (int i = 0; i < 4; i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);

                if ((i < 3 && (stack != null && stack.stackSize > 0 && stack.itemID == Item.potion.shiftedIndex && stack.getItemDamage() != 0))
                        || (i == 3 && (stack != null)))
                {
                    inventory.setInventorySlotContents(i, null);
                    return stack;
                }
            }

            return null;
        }

        return null;
    }

    public static ItemStack[] dispenseStuffFromInventory(World world, PC_CoordI beltPos, IInventory inventory)
    {
        if (isSpecialContainer(inventory))
        {
            return new ItemStack[] {dispenseFromSpecialContainer(inventory) };
        }

        PCtr_TileEntityEjectionBelt teb = (PCtr_TileEntityEjectionBelt) beltPos.getTileEntity(world);
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        boolean modeStacks = teb.actionType == 0;
        boolean modeItems = teb.actionType == 1;
        boolean modeAll = teb.actionType == 2;

        if (modeAll)
        {
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                if (inventory instanceof PC_ISpecialAccessInventory)
                {
                    if (!((PC_ISpecialAccessInventory) inventory).canDispenseStackFrom(i))
                    {
                        continue;
                    }
                }

                ItemStack inSlot = inventory.getStackInSlot(i);

                if (inSlot != null)
                {
                    stacks.add(inSlot);
                    inventory.setInventorySlotContents(i, null);
                }
            }

            return PC_InvUtils.stacksToArray(stacks);
        }

        boolean random = teb.itemSelectMode == 2;
        boolean first = teb.itemSelectMode == 0;
        boolean last = teb.itemSelectMode == 1;
        int numStacks = teb.numStacksEjected;
        int numItems = teb.numItemsEjected;

        if (modeStacks && numStacks == 0) return new ItemStack[] {};

        if (modeItems && numItems == 0) return new ItemStack[] {};

        int i = 0;

        if (first)
        {
            i = 0;
        }

        if (last)
        {
            i = inventory.getSizeInventory() - 1;
        }

        if (random)
        {
            i = teb.rand.nextInt(inventory.getSizeInventory());
        }

        int randomTries = inventory.getSizeInventory() * 2;

        while (true)
        {
            boolean accessDenied = false;

            if (inventory instanceof PC_ISpecialAccessInventory)
            {
                if (!((PC_ISpecialAccessInventory) inventory).canDispenseStackFrom(i))
                {
                    accessDenied = true;
                }
            }

            ItemStack stack = inventory.getStackInSlot(i);

            if (stack != null && stack.stackSize > 0 && !accessDenied)
            {
                if (modeStacks)
                {
                    if (numStacks > 0)
                    {
                        inventory.setInventorySlotContents(i, null);
                        stacks.add(stack);
                        numStacks--;

                        if (numStacks <= 0)
                        {
                            break;
                        }
                    }
                }
                else if (modeItems)
                {
                    if (numItems > 0)
                    {
                        stack = inventory.decrStackSize(i, numItems);
                        numItems -= stack.stackSize;
                        stacks.add(stack);

                        if (numItems <= 0)
                        {
                            break;
                        }
                    }
                }
            }

            if (first)
            {
                i++;

                if (i >= inventory.getSizeInventory())
                {
                    break;
                }
            }
            else if (last)
            {
                i--;

                if (i < 0)
                {
                    break;
                }
            }
            else if (random)
            {
                i = teb.rand.nextInt(inventory.getSizeInventory());
                randomTries--;

                if (randomTries == 0)
                {
                    break;
                }
            }
        }

        return PC_InvUtils.stacksToArray(stacks);
    }

    public static void createEntityItemOnBelt(World world, PC_CoordI invPos, PC_CoordI beltPos, ItemStack stack)
    {
        EntityItem item = new EntityItem(world, beltPos.x + 0.5D, beltPos.y + 0.3D, beltPos.z + 0.5D, stack);
        item.motionX = 0.0D;
        item.motionY = 0.0D;
        item.motionZ = 0.0D;
        PC_CoordD vector = PC_CoordI.getVector(beltPos, invPos);
        item.posX += 0.43D * vector.x;
        item.posZ += 0.43D * vector.z;
        item.delayBeforeCanPickup = 7;
        world.spawnEntityInWorld(item);
    }
}
