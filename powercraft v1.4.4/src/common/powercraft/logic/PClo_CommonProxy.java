package powercraft.logic;

import powercraft.core.PC_Proxy;

public class PClo_CommonProxy extends PC_Proxy
{
    @Override
    public Object[] registerGuis()
    {
        return new Object[]
                {
                    "Special", PClo_ContainerSpecial.class
                };
    }
}
