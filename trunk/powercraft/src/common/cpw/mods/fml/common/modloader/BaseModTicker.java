package cpw.mods.fml.common.modloader;

import java.util.EnumSet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class BaseModTicker implements ITickHandler
{
    private BaseModProxy mod;
    private EnumSet<TickType> ticks;
    private boolean clockTickTrigger;
    private boolean sendGuiTicks;

    BaseModTicker(BaseModProxy mod, boolean guiTicker)
    {
        this.mod = mod;
        this.ticks = EnumSet.of(TickType.WORLDLOAD);
        this.sendGuiTicks = guiTicker;
    }

    BaseModTicker(EnumSet<TickType> ticks, boolean guiTicker)
    {
        this.ticks = ticks;
        this.sendGuiTicks = guiTicker;
    }

    @Override
    public void tickStart(EnumSet<TickType> types, Object... tickData)
    {
        tickBaseMod(types, false, tickData);
    }

    @Override
    public void tickEnd(EnumSet<TickType> types, Object... tickData)
    {
        tickBaseMod(types, true, tickData);
    }

    private void tickBaseMod(EnumSet<TickType> types, boolean end, Object... tickData)
    {
        if (FMLCommonHandler.instance().getSide().isClient() && (ticks.contains(TickType.CLIENT) || ticks.contains(TickType.WORLDLOAD)))
        {
            EnumSet cTypes = EnumSet.copyOf(types);

            if ((end && types.contains(TickType.CLIENT)) || types.contains(TickType.WORLDLOAD))
            {
                clockTickTrigger =  true;
                cTypes.remove(TickType.CLIENT);
                cTypes.remove(TickType.WORLDLOAD);
            }

            if (end && clockTickTrigger && types.contains(TickType.RENDER))
            {
                clockTickTrigger = false;
                cTypes.remove(TickType.RENDER);
                cTypes.add(TickType.CLIENT);
            }

            sendTick(cTypes, end, tickData);
        }
        else
        {
            sendTick(types, end, tickData);
        }
    }

    private void sendTick(EnumSet<TickType> types, boolean end, Object... tickData)
    {
        for (TickType type : types)
        {
            if (!ticks.contains(type))
            {
                continue;
            }

            boolean keepTicking = true;

            if (sendGuiTicks)
            {
                keepTicking = mod.doTickInGUI(type, end, tickData);
            }
            else
            {
                keepTicking = mod.doTickInGame(type, end, tickData);
            }

            if (!keepTicking)
            {
                ticks.remove(type);
                ticks.removeAll(type.partnerTicks());
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return (clockTickTrigger ? EnumSet.of(TickType.RENDER) : ticks);
    }

    @Override
    public String getLabel()
    {
        return mod.getClass().getSimpleName();
    }

    public void setMod(BaseModProxy mod)
    {
        this.mod = mod;
    }
}
