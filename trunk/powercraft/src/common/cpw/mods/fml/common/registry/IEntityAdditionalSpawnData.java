package cpw.mods.fml.common.registry;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface IEntityAdditionalSpawnData
{
    public void writeSpawnData(ByteArrayDataOutput data);

    public void readSpawnData(ByteArrayDataInput data);
}
