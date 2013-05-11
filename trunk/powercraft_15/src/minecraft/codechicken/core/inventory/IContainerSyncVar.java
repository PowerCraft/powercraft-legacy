package codechicken.core.inventory;

import codechicken.core.packet.PacketCustom;

public interface IContainerSyncVar
{
    public boolean changed();
    
    public void reset();
    
    public void writeChange(PacketCustom packet);
    
    public void readChange(PacketCustom packet);
}
