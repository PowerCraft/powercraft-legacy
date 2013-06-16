package powercraft.projector;

import net.minecraft.src.EntityLiving;
import powercraft.api.utils.PC_Utils;

public class PCpj_ProjectorEntity extends EntityLiving {

	public PCpj_ProjectorEntity(PCpj_TileEntityProjector projector) {
		super(projector.getWorld());
		int md = PC_Utils.getMD(projector.getWorld(), projector.getCoord());
		setLocationAndAngles(projector.xCoord+0.5, projector.yCoord+0.5, projector.zCoord+0.5, md*90+180, 0);
		prevRotationYaw = rotationYaw;
	    prevRotationPitch = rotationPitch;
	}

	@Override
	public int getMaxHealth() {
		return 0;
	}

}
