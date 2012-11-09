package powercraft.transport;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.World;
import powercraft.core.PC_CoordI;

public class PCtr_BlockBeltNormal extends PCtr_BlockBeltBase {
	
	public PCtr_BlockBeltNormal(int id) {
		super(id, 0);
	}

	@Override
	public String getDefaultName() {
		return "normal belt";
	}
	
	// -------------MOVEMENT------------------
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (PCtr_BeltHelper.isEntityIgnored(entity)) {
			return;
		}

		if (entity instanceof EntityItem) {
			PCtr_BeltHelper.packItems(world, pos);
		}


		if (entity instanceof EntityItem) {
			PCtr_BeltHelper.doSpecialItemAction(world, pos, (EntityItem) entity);
			if (PCtr_BeltHelper.storeNearby(world, pos, (EntityItem) entity, false)) {
				return;
			}
		}


		int direction = getRotation(pos.getMeta(world));

		PC_CoordI pos_leading_to = pos.copy();
		switch (direction) {
			case 0: // Z--
				pos_leading_to.z--;
				break;

			case 1: // X++
				pos_leading_to.x++;
				break;

			// 6,7
			case 2: // Z++
				pos_leading_to.z++;
				break;

			case 3: // X--
				pos_leading_to.x--;
				break;
		}

		boolean leadsToNowhere = PCtr_BeltHelper.isBlocked(world, pos_leading_to);
		leadsToNowhere = leadsToNowhere && PCtr_BeltHelper.isBeyondStorageBorder(world, direction, pos, entity, PCtr_BeltHelper.STORAGE_BORDER_LONG);

		// longlife!
		if (!leadsToNowhere) {
			PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
		}



		double speed_max = PCtr_BeltHelper.MAX_HORIZONTAL_SPEED;

		double boost = PCtr_BeltHelper.HORIZONTAL_BOOST;

		PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, direction, speed_max, boost);

	}
	
}
