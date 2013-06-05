package codechicken.core.internal;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler
{
    public static int renderTime;
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if(type.contains(TickType.CLIENT))
            renderTime++;
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel()
    {
        return "CodeChicken Core internals";
    }

}
