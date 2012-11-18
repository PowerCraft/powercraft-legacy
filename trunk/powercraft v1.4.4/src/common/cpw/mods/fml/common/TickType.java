package cpw.mods.fml.common;

import java.util.EnumSet;

public enum TickType
{
    WORLD,

    RENDER,

    GUI,

    CLIENTGUI,

    WORLDLOAD,

    CLIENT,

    PLAYER,

    SERVER;

    public EnumSet<TickType> partnerTicks()
    {
        if (this == CLIENT)
        {
            return EnumSet.of(RENDER);
        }

        if (this == RENDER)
        {
            return EnumSet.of(CLIENT);
        }

        if (this == GUI)
        {
            return EnumSet.of(CLIENTGUI);
        }

        if (this == CLIENTGUI)
        {
            return EnumSet.of(GUI);
        }

        return EnumSet.noneOf(TickType.class);
    }
}