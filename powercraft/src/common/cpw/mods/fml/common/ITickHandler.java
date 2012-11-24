package cpw.mods.fml.common;

import java.util.EnumSet;

public interface ITickHandler
{
    public void tickStart(EnumSet<TickType> type, Object... tickData);

    public void tickEnd(EnumSet<TickType> type, Object... tickData);

    public EnumSet<TickType> ticks();

    public String getLabel();
}
