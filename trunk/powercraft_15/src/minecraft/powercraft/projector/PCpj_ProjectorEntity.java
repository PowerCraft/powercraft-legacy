package powercraft.projector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class PCpj_ProjectorEntity extends EntityLiving {

	public PCpj_ProjectorEntity(PCpj_TileEntityProjector projector) {
		super(projector.getWorld());
		setLocationAndAngles(projector.xCoord+0.5, projector.yCoord+0.5, projector.zCoord+0.5, 0, 0);
	}

	@Override
	public int getMaxHealth() {
		return 0;
	}

}
