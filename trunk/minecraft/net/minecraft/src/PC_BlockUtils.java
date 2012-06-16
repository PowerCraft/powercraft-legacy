package net.minecraft.src;

/**
 * PowerCraft block type tests.<br>
 * Checks block flags at given position in world.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PC_BlockUtils {

	/**
	 * Check if block pointed by coordinate in world is conveyor.
	 * 
	 * @param pos block coordinate
	 * @param world the world
	 * @return flag
	 */
	public static boolean isConveyor(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isConveyor(world, pos);
		}

		return false;
	}

	/**
	 * Check if block pointed by coordinate in world is elevator.
	 * 
	 * @param pos block coordinate
	 * @param world the world
	 * @return flag
	 */
	public static boolean isElevator(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isElevator(world, pos);
		}

		return false;
	}

	/**
	 * Check if block pointed by coordinate in world is conveyor or elevator.
	 * 
	 * @param pos block coordinate
	 * @param world the world
	 * @return flag
	 */
	public static boolean isConveyorOrElevator(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isConveyor(world, pos) || type.isElevator(world, pos);
		}

		return false;
	}

	/**
	 * Check if Block at coordinate in world is ignored by Block Harvester.<br>
	 * <i>isBlockHarvesterDelimiter</i> can override this.
	 * 
	 * @param pos block coordinate
	 * @param world the world
	 * @return flag
	 */
	public static boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isHarvesterIgnored(world, pos);
		}

		return false;
	}

	/**
	 * Check if Block with given ID can't be placed by Block Builder.
	 * 
	 * @param id block id
	 * @return flag
	 */
	public static boolean isBuilderIgnored(int id) {
		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isBuilderIgnored();
		}
		return false;
	}

	/**
	 * Check if block pointed by coordinate in world is End-block for harvester.
	 * 
	 * @param world the world
	 * @param pos block coordinate
	 * @return flag
	 */
	public static boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isHarvesterDelimiter(world, pos);
		}

		return false;
	}

	/**
	 * Check if block pointed by coordinate in world is translucent for laser beam.
	 * 
	 * @param pos block coordinate
	 * @param world the world
	 * @return flag
	 */
	public static boolean isTranslucent(IBlockAccess world, PC_CoordI pos) {

		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			return type.isTranslucentForLaser(world, pos);
		}

		return false;
	}

}
