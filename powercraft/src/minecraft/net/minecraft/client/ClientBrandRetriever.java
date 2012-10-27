package net.minecraft.client;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        return "forge,fml";
    }
}
