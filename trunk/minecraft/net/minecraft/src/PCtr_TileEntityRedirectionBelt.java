package net.minecraft.src;


/**
 * Redstone powered redirection belt.
 * 
 * @author MightyPork
 */
public class PCtr_TileEntityRedirectionBelt extends PCtr_TileEntityRedirectionBeltBase {

	/**
	 * 
	 */
	public PCtr_TileEntityRedirectionBelt() {}

	@Override
	protected int calculateItemDirection(Entity entity) {

		PCtr_BlockBeltRedirector block = ((PCtr_BlockBeltRedirector) mod_PCtransport.redirectionBelt);

		PC_CoordI pos = getCoord();
		int meta = block.getRotation(pos.getMeta(worldObj));

		// get redir
		int redir = 0;
		if (block.isPowered(worldObj, pos)) {
			switch (meta) {
				case 0: // '\0' Z--
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(1, 0, 0))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(-1, 0, 0))) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, 1))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, -1))) {
						redir = 1;
					}
					break;

				// 6,7
				case 2: // '\0' Z++
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(-1, 0, 0))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(1, 0, 0))) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, -1))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, 1))) {
						redir = 1;
					}
					break;
			}
		}

		if (redir == 0) { // not powered
			switch (meta) {
				case 0: // '\0' Z--
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(1, 0, 0)) && PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(-1, 0, 0)) && !PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, -1))) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, 1)) && PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, -1)) && !PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(1, 0, 0))) {
						redir = 1;
					}
					break;

				case 2: // '\0' Z++
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(-1, 0, 0)) && PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(1, 0, 0)) && !PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, 1))) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, -1)) && PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(0, 0, 1)) && !PCtr_BeltBase.isTransporterAt(worldObj, pos.offset(-1, 0, 0))) {
						redir = 1;
					}
					break;
			}
		}

		redir = -redir;

		setItemDirection(entity, Integer.valueOf(redir));

		return redir;

	}

}
