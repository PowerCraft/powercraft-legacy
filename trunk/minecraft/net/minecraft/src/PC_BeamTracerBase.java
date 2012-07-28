package net.minecraft.src;


import java.util.List;
import java.util.Random;


/**
 * Laser beam tracing class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PC_BeamTracerBase {

	private Random rand = new Random();

	private PC_CoordI startCoord, startMove;

	/**
	 * The beam color.<br>
	 * This object also contains information about beam visibility (particles
	 * enabled) and power crystal metadata.
	 */
	private PC_Color origColor;

	private PC_IBeamHandler handler;

	/** The current world */
	protected World world;

	private boolean canChangeColor = false;
	private boolean particlesBidi = false;
	private boolean canHitEntity = false;

	private int particle_count = 2; // in one block each tick
	private static final int particles_range = 20; // visibility range
	private int maxTotalLength = 8000;
	private int crystal_add = 40;
	private int start_limit = 40;
	private int maximum_current_limit = 80;


	/**
	 * Laser beam raytracer
	 * 
	 * @param worldObj the world
	 * @param handler laser handler (interface)
	 */
	public PC_BeamTracerBase(World worldObj, PC_IBeamHandler handler) {
		this.handler = handler;
		this.world = worldObj;
	}


	/**
	 * Set if the beam can change color on PowerCrystals
	 * 
	 * @param state boolean value
	 * @return self
	 */
	public PC_BeamTracerBase setCanChangeColor(boolean state) {
		canChangeColor = state;
		return this;
	}


	/**
	 * Set starting range
	 * 
	 * @param length range in blocks
	 * @return self
	 */
	public PC_BeamTracerBase setStartLength(int length) {
		start_limit = length;
		return this;
	}

	/**
	 * Set maximum current limit. When crystal adds some length, it is checked
	 * against this limit and shortened if needed.<br>
	 * If set to small number, crystals won't add more than this number.
	 * 
	 * @param length range in blocks
	 * @return self
	 */
	public PC_BeamTracerBase setMaxLengthAfterCrystal(int length) {
		maximum_current_limit = length;
		return this;
	}

	/**
	 * Set how many blocks a crystal adds.
	 * 
	 * @param length range in blocks
	 * @return self
	 */
	public PC_BeamTracerBase setCrystalAddedLength(int length) {
		crystal_add = length;
		return this;
	}

	/**
	 * Set the highest possible length (all forked beams together)
	 * 
	 * @param length range in blocks
	 * @return self
	 */
	public PC_BeamTracerBase setTotalLengthLimit(int length) {
		maxTotalLength = length;
		return this;
	}

	/**
	 * Set particles count in each block each flash
	 * 
	 * @param particles number of particles
	 * @return self
	 */
	public PC_BeamTracerBase setParticleCount(int particles) {
		particle_count = particles;
		return this;
	}


	/**
	 * Set starting coordinates of the beam (the device)
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return self
	 */
	public PC_BeamTracerBase setStartCoord(int x, int y, int z) {
		startCoord = new PC_CoordI(x, y, z);
		return this;
	}


	/**
	 * Set starting coordinates of the beam (the device)
	 * 
	 * @param coord
	 * @return self
	 */
	public PC_BeamTracerBase setStartCoord(PC_CoordI coord) {
		startCoord = coord.copy();
		return this;
	}


	/**
	 * Set starting movement vector of the beam
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return self
	 */
	public PC_BeamTracerBase setStartMove(int x, int y, int z) {
		startMove = new PC_CoordI(x, y, z);
		return this;
	}


	/**
	 * Set starting movement vector of the beam
	 * 
	 * @param coord
	 * @return self
	 */
	public PC_BeamTracerBase setStartMove(PC_CoordI coord) {
		startMove = coord.copy();
		return this;
	}


	/**
	 * Set starting beam color (can be changed by power crystals)
	 * 
	 * @param color color object representing the color;<br>
	 *            Color must also contain information about beam metadata, and
	 *            visibility flag.
	 * @return self
	 */
	public PC_BeamTracerBase setColor(PC_Color color) {
		origColor = color.copy();
		return this;
	}


	/**
	 * Set initial beam color
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param meta metadata of corresponding power crystal -1 <br>
	 *            (crystals meta starts at 1, which equals 0 here)
	 * @return self
	 */
	public PC_BeamTracerBase setColor(double r, double g, double b, int meta) {
		origColor = new PC_Color(r, g, b);
		origColor.setMeta(meta);
		return this;
	}


	/**
	 * Set that the particles will "move" both forward and backward to simulate
	 * reflected beam for sensors.
	 * 
	 * @param bidi
	 * @return self
	 */
	public PC_BeamTracerBase setParticlesBidirectional(boolean bidi) {
		particlesBidi = bidi;
		return this;
	}


	/**
	 * Set whether the beam can detect entities
	 * 
	 * @param state
	 * @return self
	 */
	public PC_BeamTracerBase setDetectEntities(boolean state) {
		canHitEntity = state;
		return this;
	}


	/**
	 * Total beam length in this flash. <br>
	 * Used to prevent infinite loops and stack overflow.
	 */
	private int totalLength = 0;


	/**
	 * Send one light quantum and spawn particles on the way.
	 */
	public void flash() {
		totalLength = 0;

		forkBeam(startCoord, startMove, origColor, start_limit);
	}


	/**
	 * Fork current beam. To be called only by subclasses.
	 * 
	 * @param par_cnt starting coordinate
	 * @param par_move starting movement
	 * @param par_color starting color object
	 * @param limit length limit for this fork
	 */
	protected void forkBeam(PC_CoordI par_cnt, PC_CoordI par_move, PC_Color par_color, int limit) {
		// copy parameters to prevent interference
		PC_CoordI cnt = par_cnt.copy();
		PC_CoordI move = par_move.copy();
		PC_Color color = par_color.copy();

		// changed dir on this block - for particle renderer
		boolean chdir = false;

		// old direction
		PC_CoordI moveOld = move.copy();


		mainloop:
		for (int c = 0; c < limit; c++) {

			if (++totalLength > maxTotalLength) {
				break mainloop;
			}

			chdir = false;

			cnt.add(move);

			int id = cnt.getId(world);
			int meta = cnt.getMeta(world);



			if (id == mod_PCcore.powerCrystal.blockID) {

				if (canChangeColor) {
					color.setTo(PC_Color.crystal_colors[meta]);
					color.setMeta(meta);
				}

				limit += crystal_add;
				if (limit > maximum_current_limit) {
					limit = maximum_current_limit;
				}

			} else {

				result res = onBlockHit(cnt, move, moveOld, color, limit - c);

				if (res == result.FALLBACK) {

					boolean stop = handler.onBlockHit(world, cnt, startCoord);

					if (stop) {
						return;
					}

				} else if (res == result.CONTINUE) {

					// just continue

				} else if (res == result.CONTINUE_DIR_CHANGE) {

					// direction changed
					chdir = true;

				} else if (res == result.STOP) {

					// break loop
					return;

				}

			}



			// make particles
			if (color.isVisible() && shouldSpawnParticle(cnt)) {
				for (int f = 0; f < particle_count; f++) {

					// not changed direction
					if (!chdir) {

						// two at once
						spawnParticles(cnt, move, 0 /* both halves */, color);

					} else {

						// old half
						spawnParticles(cnt, moveOld, 1 /* 1st half */, color);

						// new half
						spawnParticles(cnt, move, 2 /* 2nd half */, color);

					}
				}
			}

			if (canHitEntity) {

				// check for entities in this block.
				List<Entity> hitList = world.getEntitiesWithinAABB(net.minecraft.src.Entity.class,
						AxisAlignedBB.getBoundingBoxFromPool(cnt.x, cnt.y, cnt.z, cnt.x + 1, cnt.y + 1, cnt.z + 1));

				if (hitList.size() > 0) {
					if (handler.onEntityHit(world, hitList.toArray(new Entity[hitList.size()]), startCoord)) {
						return;
					}
				}
			}
		}

	}


	/**
	 * Is player close enough to render particles?
	 * 
	 * @param coord current coord
	 * @return make particles
	 */
	private boolean shouldSpawnParticle(PC_CoordI coord) {
		return world.getClosestPlayer(coord.x + 0.5D, coord.y + 0.5D, coord.z + 0.5D, particles_range) != null;
	}


	/**
	 * Spawn laser particles
	 * 
	 * @param cnt coordinate
	 * @param move movement
	 * @param half 0=both, 1=start, 2=end
	 * @param color the beam color;
	 */
	protected void spawnParticles(PC_CoordI cnt, PC_CoordI move, int half, PC_Color color) {
		if (!color.isVisible()) {
			return;
		}

		double shift = 0;
		if (half == 0) {
			shift = -0.5D + rand.nextFloat() * 1.0D;
		}
		if (half == 1) {
			shift = -rand.nextFloat() * 0.5D;
		}
		if (half == 2) {
			shift = rand.nextFloat() * 0.5D;
		}

		PC_CoordD particle = cnt.offset(0.5, 0.5, 0.5);

		if (move.x > 0) {
			particle.x += shift;
		}
		if (move.y > 0) {
			particle.y += shift;
		}
		if (move.z > 0) {
			particle.z += shift;
		}

		if (move.x < 0) {
			particle.x -= shift;
		}
		if (move.y < 0) {
			particle.y -= shift;
		}
		if (move.z < 0) {
			particle.z -= shift;
		}

		if (particlesBidi && rand.nextBoolean()) {
			ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(world, particle, color, move.getInverted(), half));
		} else {
			ModLoader.getMinecraftInstance().effectRenderer.addEffect(new PC_EntityLaserParticleFX(world, particle, color, move, half));
		}
	}


	/**
	 * Called when block is hit.
	 * 
	 * @param blockCoord coordinate of the hit block
	 * @param move current movement vector
	 * @param moveOld old movement vector (for changing direction)
	 * @param color current color object, with visiblity flag and crystal
	 *            metadata
	 * @param remainingLength laser beam remaining length
	 * @return result enum state
	 */
	public abstract result onBlockHit(PC_CoordI blockCoord, PC_CoordI move, PC_CoordI moveOld, PC_Color color, int remainingLength);

	/**
	 * Result state enum for extending class's block hit method.
	 */
	protected static enum result {
		/** Fall back to handler */
		FALLBACK,
		/** Continue to next block */
		CONTINUE,
		/** Continue to next block; This block changed direction. */
		CONTINUE_DIR_CHANGE,
		/** Stop the beam propagation */
		STOP;
	}



}
