package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge extends Item
{
    public ItemFireworkCharge(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public int getIconFromDamageForRenderPass(int par1, int par2)
    {
        return par2 == 1 ? this.iconIndex + 1 : super.getIconFromDamageForRenderPass(par1, par2);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        if (par2 != 1)
        {
            return super.getColorFromItemStack(par1ItemStack, par2);
        }
        else
        {
            NBTBase var3 = func_92108_a(par1ItemStack, "Colors");

            if (var3 == null)
            {
                return 9079434;
            }
            else
            {
                NBTTagIntArray var4 = (NBTTagIntArray)var3;

                if (var4.intArray.length == 1)
                {
                    return var4.intArray[0];
                }
                else
                {
                    int var5 = 0;
                    int var6 = 0;
                    int var7 = 0;
                    int[] var8 = var4.intArray;
                    int var9 = var8.length;

                    for (int var10 = 0; var10 < var9; ++var10)
                    {
                        int var11 = var8[var10];
                        var5 += (var11 & 16711680) >> 16;
                        var6 += (var11 & 65280) >> 8;
                        var7 += (var11 & 255) >> 0;
                    }

                    var5 /= var4.intArray.length;
                    var6 /= var4.intArray.length;
                    var7 /= var4.intArray.length;
                    return var5 << 16 | var6 << 8 | var7;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static NBTBase func_92108_a(ItemStack par0ItemStack, String par1Str)
    {
        if (par0ItemStack.hasTagCompound())
        {
            NBTTagCompound var2 = par0ItemStack.getTagCompound().getCompoundTag("Explosion");

            if (var2 != null)
            {
                return var2.getTag(par1Str);
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.hasTagCompound())
        {
            NBTTagCompound var5 = par1ItemStack.getTagCompound().getCompoundTag("Explosion");

            if (var5 != null)
            {
                func_92107_a(var5, par3List);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void func_92107_a(NBTTagCompound par0NBTTagCompound, List par1List)
    {
        byte var2 = par0NBTTagCompound.getByte("Type");

        if (var2 >= 0 && var2 <= 4)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.type." + var2).trim());
        }
        else
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }

        int[] var3 = par0NBTTagCompound.getIntArray("Colors");
        int var8;
        int var9;

        if (var3.length > 0)
        {
            boolean var4 = true;
            String var5 = "";
            int[] var6 = var3;
            int var7 = var3.length;

            for (var8 = 0; var8 < var7; ++var8)
            {
                var9 = var6[var8];

                if (!var4)
                {
                    var5 = var5 + ", ";
                }

                var4 = false;
                boolean var10 = false;

                for (int var11 = 0; var11 < 16; ++var11)
                {
                    if (var9 == ItemDye.dyeColors[var11])
                    {
                        var10 = true;
                        var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[var11]);
                        break;
                    }
                }

                if (!var10)
                {
                    var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            par1List.add(var5);
        }

        int[] var13 = par0NBTTagCompound.getIntArray("FadeColors");
        boolean var16;

        if (var13.length > 0)
        {
            var16 = true;
            String var14 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            int[] var15 = var13;
            var8 = var13.length;

            for (var9 = 0; var9 < var8; ++var9)
            {
                int var18 = var15[var9];

                if (!var16)
                {
                    var14 = var14 + ", ";
                }

                var16 = false;
                boolean var19 = false;

                for (int var12 = 0; var12 < 16; ++var12)
                {
                    if (var18 == ItemDye.dyeColors[var12])
                    {
                        var19 = true;
                        var14 = var14 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.dyeColorNames[var12]);
                        break;
                    }
                }

                if (!var19)
                {
                    var14 = var14 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            par1List.add(var14);
        }

        var16 = par0NBTTagCompound.getBoolean("Trail");

        if (var16)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }

        boolean var17 = par0NBTTagCompound.getBoolean("Flicker");

        if (var17)
        {
            par1List.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }
}
