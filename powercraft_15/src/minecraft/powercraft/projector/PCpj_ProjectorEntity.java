package powercraft.projector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class PCpj_ProjectorEntity extends EntityLiving {

	public PCpj_ProjectorEntity(World par1World) {
		super(par1World);
	}

	@Override
	public int getMaxHealth() {
		return 0;
	}

}
