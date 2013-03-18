package codechicken.core.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import codechicken.core.packet.PacketCustom;

public abstract class ContainerSynchronised extends ContainerExtended
{
    private ArrayList<IContainerSyncVar> syncVars = new ArrayList<IContainerSyncVar>();
    
    public abstract PacketCustom createSyncPacket();
    
    @Override
    public final void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        for(int i = 0; i < syncVars.size(); i++)
        {
            IContainerSyncVar var = syncVars.get(i);
            if(var.changed())
            {
                PacketCustom packet = createSyncPacket();
                packet.writeByte(i);
                var.writeChange(packet);
                sendContainerPacket(packet);
                var.reset();
            }
        }
    }
    
    public void addSyncVar(IContainerSyncVar var)
    {
        syncVars.add(var);
    }
    
    @Override
    public final void handleOutputPacket(PacketCustom packet)
    {
        syncVars.get(packet.readUnsignedByte()).readChange(packet);
    }
    
    public List<IContainerSyncVar> getSyncedVars()
    {
        return Collections.unmodifiableList(syncVars);
    }
}
