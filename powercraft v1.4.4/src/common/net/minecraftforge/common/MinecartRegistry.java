package net.minecraftforge.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.src.EntityMinecart;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class MinecartRegistry
{
    private static Map<MinecartKey, ItemStack> itemForMinecart = new HashMap<MinecartKey, ItemStack>();
    private static Map<ItemStack, MinecartKey> minecartForItem = new HashMap<ItemStack, MinecartKey>();

    public static void registerMinecart(Class <? extends EntityMinecart > cart, ItemStack item)
    {
        registerMinecart(cart, 0, item);
    }

    public static void registerMinecart(Class <? extends EntityMinecart > minecart, int type, ItemStack item)
    {
        MinecartKey key = new MinecartKey(minecart, type);
        itemForMinecart.put(key, item);
        minecartForItem.put(item, key);
    }

    public static void removeMinecart(Class <? extends EntityMinecart > minecart, int type)
    {
        MinecartKey key = new MinecartKey(minecart, type);
        ItemStack item = itemForMinecart.remove(key);

        if (item != null)
        {
            minecartForItem.remove(item);
        }
    }

    public static ItemStack getItemForCart(Class <? extends EntityMinecart > minecart)
    {
        return getItemForCart(minecart, 0);
    }

    public static ItemStack getItemForCart(Class <? extends EntityMinecart > minecart, int type)
    {
        ItemStack item = itemForMinecart.get(new MinecartKey(minecart, type));

        if (item == null)
        {
            return null;
        }

        return item.copy();
    }

    public static ItemStack getItemForCart(EntityMinecart cart)
    {
        return getItemForCart(cart.getClass(), cart.getMinecartType());
    }

    public static Class <? extends EntityMinecart > getCartClassForItem(ItemStack item)
    {
        MinecartKey key = null;

        for (Map.Entry<ItemStack, MinecartKey> entry : minecartForItem.entrySet())
        {
            if (entry.getKey().isItemEqual(item))
            {
                key = entry.getValue();
                break;
            }
        }

        if (key != null)
        {
            return key.minecart;
        }

        return null;
    }

    public static int getCartTypeForItem(ItemStack item)
    {
        MinecartKey key = null;

        for (Map.Entry<ItemStack, MinecartKey> entry : minecartForItem.entrySet())
        {
            if (entry.getKey().isItemEqual(item))
            {
                key = entry.getValue();
                break;
            }
        }

        if (key != null)
        {
            return key.type;
        }

        return -1;
    }

    public static Set<ItemStack> getAllCartItems()
    {
        Set<ItemStack> ret = new HashSet<ItemStack>();

        for (ItemStack item : minecartForItem.keySet())
        {
            ret.add(item.copy());
        }

        return ret;
    }

    static
    {
        registerMinecart(EntityMinecart.class, 0, new ItemStack(Item.minecartEmpty));
        registerMinecart(EntityMinecart.class, 1, new ItemStack(Item.minecartCrate));
        registerMinecart(EntityMinecart.class, 2, new ItemStack(Item.minecartPowered));
    }

    public static class MinecartKey
    {
        public final Class <? extends EntityMinecart > minecart;
        public final int type;

        public MinecartKey(Class <? extends EntityMinecart > cls, int typtID)
        {
            minecart = cls;
            type = typtID;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }

            if (getClass() != obj.getClass())
            {
                return false;
            }

            final MinecartKey other = (MinecartKey)obj;

            if (this.minecart != other.minecart && (this.minecart == null || !this.minecart.equals(other.minecart)))
            {
                return false;
            }

            return (this.type == other.type);
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 59 * hash + (this.minecart != null ? this.minecart.hashCode() : 0);
            hash = 59 * hash + this.type;
            return hash;
        }
    }
}
