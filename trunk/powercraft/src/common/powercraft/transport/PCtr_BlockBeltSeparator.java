package powercraft.transport;

import net.minecraft.src.Entity;
import net.minecraft.src.World;

public class PCtr_BlockBeltSeparator extends PCtr_BlockBeltBase {

	public PCtr_BlockBeltSeparator(int id) {
		super(id, 7);
	}

	@Override
	public String getDefaultName() {
		return "Seperator Belt";
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {

	}

}
