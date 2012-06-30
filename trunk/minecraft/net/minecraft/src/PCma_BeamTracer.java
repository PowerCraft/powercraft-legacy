package net.minecraft.src;


import java.util.ArrayList;


/**
 * Laser beam tracing class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BeamTracer extends PC_BeamTracerBase {

	private boolean reflectMirror = false;
	private boolean reflectPrism = false;

	/**
	 * Laser beam raytracer subtype for machines module.<br>
	 * Works with mirrors and prisms
	 * 
	 * @param worldObj the world
	 * @param handler laser handler (interface)
	 */
	public PCma_BeamTracer(World worldObj, PC_IBeamHandler handler) {
		super(worldObj, handler);
	}

	// Setters

	/**
	 * Set if the beam should be reflected by mirror
	 * 
	 * @param state boolean value
	 * @return self
	 */
	public PC_BeamTracerBase setReflectedByMirror(boolean state) {
		reflectMirror = state;
		return this;
	}

	/**
	 * Set if the beam should be refracted by prism
	 * 
	 * @param state boolean value
	 * @return self
	 */
	public PC_BeamTracerBase setReflectedByPrism(boolean state) {
		reflectPrism = state;
		return this;
	}

	@Override
	public void flash() {
		usedPrisms.clear();
		super.flash();
	}

	/** mirror angle for meta */
	private static final float mirrorAngle[] = new float[16];
	static {
		for (int a = 0; a < 8; a++) {
			mirrorAngle[a] = a * 22.5F;
			mirrorAngle[a + 8] = a * 22.5F;
		}
	}

	/** angle rounded to 45 for vertical beam colliding with mirror */
	private static final int mirrorTo45[] = { 0, 0, 45, 90, 90, 90, 135, 180, 180, 180, 225, 270, 270, 270, 315, 0 };

	/** do not care */
	private static final int n = 0;

	/** prism redirection vector for side */
	private static final PC_CoordI[] prismMove = { new PC_CoordI(0, -1, 0), new PC_CoordI(0, 1, 0), new PC_CoordI(1, 0, 0), new PC_CoordI(1, 0, 1), new PC_CoordI(0, 0, 1), new PC_CoordI(-1, 0, 1), new PC_CoordI(-1, 0, 0), new PC_CoordI(-1, 0, -1),
			new PC_CoordI(0, 0, -1), new PC_CoordI(1, 0, -1) };

	/**
	 * get movement vector from prism's side
	 * 
	 * @param side the side number
	 * @return vector (coord)
	 */
	private PC_CoordI getPrismOutputMove(int side) {
		return prismMove[side];
	}

	/**
	 * Get index of prism side which faces the beam
	 * 
	 * @param move beam movement vector
	 * @return side number
	 */
	private int getPrismSideFacingMove(PC_CoordI move) {
		for (int i = 0; i < 10; i++) {
			if (prismMove[i].equals(move.getInverted())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Get horizontal angle from movement vector
	 * 
	 * @param move movement vector
	 * @return angle
	 */
	private static float getAngleFromMove(PC_CoordI move) {
		float beamAngle = 0;
		if (move.x == 0 && move.z == -1) {
			beamAngle = 0;
		}
		if (move.x == 1 && move.z == -1) {
			beamAngle = 45;
		}
		if (move.x == 1 && move.z == 0) {
			beamAngle = 90;
		}
		if (move.x == 1 && move.z == 1) {
			beamAngle = 135;
		}
		if (move.x == 0 && move.z == 1) {
			beamAngle = 180;
		}
		if (move.x == -1 && move.z == 1) {
			beamAngle = 225;
		}
		if (move.x == -1 && move.z == 0) {
			beamAngle = 270;
		}
		if (move.x == -1 && move.z == -1) {
			beamAngle = 315;
		}
		return beamAngle;
	}

	/**
	 * Get movement vector from angle
	 * 
	 * @param angle
	 * @return vector (coord)
	 */
	private static PC_CoordI getMoveFromAngle(float angle) {
		int angleint = Math.round(angle);
		switch (angleint) {
			case 0:
				return new PC_CoordI(0, n, -1);
			case 45:
				return new PC_CoordI(1, n, -1);
			case 90:
				return new PC_CoordI(1, n, 0);
			case 135:
				return new PC_CoordI(1, n, 1);
			case 180:
				return new PC_CoordI(0, n, 1);
			case 225:
				return new PC_CoordI(-1, n, 1);
			case 270:
				return new PC_CoordI(-1, n, 0);
			case 315:
				return new PC_CoordI(-1, n, -1);
		}
		return null;
	}

	/**
	 * Get real difference of two angles
	 * 
	 * @param firstAngle
	 * @param secondAngle
	 * @return result
	 */
	private static float angleDiff(float firstAngle, float secondAngle) {

		float difference = secondAngle - firstAngle;

		while (difference < -180) {
			difference += 360;
		}

		while (difference > 180) {
			difference -= 360;
		}

		return difference;

	}

	/**
	 * Convert invalid angle to 0-360
	 * 
	 * @param angle to convert
	 * @return converted
	 */
	private static float fixAngle(float angle) {

		while (angle > 360) {
			angle -= 360;
		}

		while (angle < 0) {
			angle += 360;
		}

		return angle;

	}

	/** prisms used in this laser run */
	private ArrayList<PC_CoordI> usedPrisms = new ArrayList<PC_CoordI>();

	@Override
	public result onBlockHit(PC_CoordI coord, PC_CoordI move, PC_CoordI moveOld, PC_Color color, int remainingLength) {

		int id = world.getBlockId(coord.x, coord.y, coord.z);
		int meta = world.getBlockMetadata(coord.x, coord.y, coord.z);

		if (Block.blocksList[id] == null) {
			return result.CONTINUE;
		}

		if (id == mod_PCmachines.optical.blockID) {

			if (PCma_BlockOptical.isMirror(world, coord.x, coord.y, coord.z) && reflectMirror) {

				int mirrorColor = PCma_BlockOptical.getMirrorColor(world, coord.x, coord.y, coord.z);

				// if can be reflected by this mirror
				if (mirrorColor == -1 || mirrorColor == color.getMeta()) {

					// vertical beam
					if (move.x == 0 && move.z == 0) {

						moveOld.setTo(move);
						int a = mirrorTo45[meta];
						PC_CoordI reflected = getMoveFromAngle(a).getInverted();

						move.x = reflected.x;
						move.z = reflected.z;

						return result.CONTINUE_DIR_CHANGE;

					} else {
						float beamAngle = getAngleFromMove(move);
						float mAngle = mirrorAngle[meta];

						float diff = angleDiff(beamAngle, mAngle);

						// the reflection
						float beamNew = beamAngle + diff * 2;

						beamNew = fixAngle(beamNew);

						moveOld.setTo(move);

						PC_CoordI reflected = getMoveFromAngle(beamNew).getInverted();

						move.x = reflected.x;
						move.z = reflected.z;

						return result.CONTINUE_DIR_CHANGE;
					}
				}

			} else if (PCma_BlockOptical.isPrism(world, coord.x, coord.y, coord.z) && reflectPrism) {
				// it's a prism!

				if (usedPrisms.contains(coord)) {
					return result.STOP;
				}

				usedPrisms.add(coord.copy());

				PCma_TileEntityOptical prism = PCma_BlockOptical.getTE(world, coord.x, coord.y, coord.z);

				int sideCount = 0;
				int[] side = new int[10];

				int thisPrismSide = getPrismSideFacingMove(move);

				for (int h = 0; h < 10; h++) {
					// include only non-this & not false sides.
					if (prism.getPrismSide(h) && h != thisPrismSide) {
						side[sideCount] = h;
						sideCount++;
					}
				}

				if (sideCount >= 1) {

					for (int h = 0; h < sideCount; h++) {
						PC_CoordI newMove = getPrismOutputMove(side[h]).copy();

						if (color.isVisible()) {
							spawnParticles(coord, newMove, 2, color);
						}

						forkBeam(coord, newMove, color, remainingLength / Math.round((sideCount * 0.75F)));
					}

				}

				if (sideCount > 0) {
					return result.STOP;
				}

				return result.CONTINUE;

			}

			return result.CONTINUE;
		}

		return result.FALLBACK;

	}

}
