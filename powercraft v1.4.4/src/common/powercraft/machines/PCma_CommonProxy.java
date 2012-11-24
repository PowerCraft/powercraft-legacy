package powercraft.machines;

import powercraft.core.PC_Proxy;

public class PCma_CommonProxy extends PC_Proxy
{
    @Override
    public Object[] registerGuis()
    {
        return new Object[]
                {
                    "AutomaticWorkbench", PCma_ContainerAutomaticWorkbench.class,
                    "Roaster", PCma_ContainerRoaster.class,
                    "Replacer", PCma_ContainerReplacer.class,
                    "Transmutabox", PCma_ContainerTransmutabox.class,
                    "BlockBuilder", PCma_ContainerBlockBuilder.class
                };
    }
}
