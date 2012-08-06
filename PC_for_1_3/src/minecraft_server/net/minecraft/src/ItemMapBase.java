package net.minecraft.src;

public class ItemMapBase extends Item
{
    protected ItemMapBase(int par1)
    {
        super(par1);
    }

    public boolean func_77643_m_()
    {
        return true;
    }

    public Packet getUpdatePacket(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return null;
    }
}
