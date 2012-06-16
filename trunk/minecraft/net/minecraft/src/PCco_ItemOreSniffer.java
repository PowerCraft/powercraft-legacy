package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

/**
 * Ore radar device.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCco_ItemOreSniffer extends Item {

	/*
	 * @Override
	 * public void onUpdate(ItemStack itemstack, World world, Entity entity, int inv_index, boolean selected) {
	 * super.onUpdate(itemstack, world, entity, inv_index, selected);
	 * }
	 */

	/**
	 * @param i ID
	 */
	public PCco_ItemOreSniffer(int i) {
		super(i);
		setMaxStackSize(1);
		setMaxDamage(500);
	}

	private static String[] names = new String[4096];

	private String makeNameId(int id) {
		String f = "0000" + Integer.toString(id);
		return "block_" + f.substring(f.length() - 4);
	}

	/**
	 * Load block descriptors and colors from a config file.
	 */
	public void initNames() {

		PC_Logger.finer("Loading sniffer colors.");

		// @formatter:off
		PC_PropertyManager namecfg= new PC_PropertyManager(mod_PCcore.cfgdir+"/sniffer_colors.cfg", 
				"PowerCraft Mobile module\n"
				+ "*** Setup file for Ore Sniffer tool ***\n"
				+ "\n"
				+ "Syntax: block_NNNN = CL (C = color, L = one letter).\n"
				+ "\n Colors:\n"
				+ " 0 black\n"
				+ " 1 dark blue\n"
				+ " 2 dark green\n"
				+ " 3 dark aqua\n"
				+ " 4 dark red\n"
				+ " 5 purple\n"
				+ " 6 gold-orange\n"
				+ " 7 grey\n"
				+ " 8 dark grey\n"
				+ " 9 indigo\n"
				+ " a bright green\n"
				+ " b aqua\n"
				+ " c red\n"
				+ " d pink\n"
				+ " e yellow\n"
				+ " f white");
		// @formatter:on

		namecfg.cfgSeparateSections(false);

		names[0] = "f ";
		// add default colors and letters
		for (Integer q = 1; q < 4096; q++) {
			Block bl = Block.blocksList[q];
			if (bl != null && names[q] == null) {
				names[q] = "§f-";
				if (namecfg.getString(makeNameId(q)) != null && namecfg.getString(makeNameId(q)).length() == 1) {
					names[q] = namecfg.getString(makeNameId(q));
				} else {
					if (bl.hasTileEntity()) {
						names[q] = "d$";
					} else if (bl.blockMaterial == Material.leaves || bl.blockMaterial == Material.plants
							|| bl.blockMaterial == Material.vine) {
						names[q] = "a&";
					} else if (bl.blockMaterial == Material.ground) {
						names[q] = "8%";
					} else if (bl.blockMaterial == Material.wood) {
						names[q] = "6W";
					} else if (bl.blockMaterial == Material.iron) {
						names[q] = "4#";
					} else if (bl.blockMaterial == Material.cloth) {
						names[q] = "f%";
					} else if (bl.blockMaterial == Material.fire) {
						names[q] = "cf";
					} else if (bl.blockMaterial == Material.circuits || bl.blockMaterial == Material.redstoneLight) {
						names[q] = "4.";
					} else if (bl.blockMaterial == Material.glass || bl.blockMaterial == Material.ice) {
						names[q] = "b#";
					} else if (bl.blockMaterial == Material.snow) {
						names[q] = "f_";
					} else if (bl.blockMaterial == Material.craftedSnow) {
						names[q] = "f#";
					} else if (bl.blockMaterial == Material.cactus) {
						names[q] = "2#";
					} else if (bl.blockMaterial == Material.clay) {
						names[q] = "7C";
					} else if (bl.blockMaterial == Material.pumpkin) {
						names[q] = "6P";
					} else if (bl.blockMaterial == Material.portal) {
						names[q] = "5P";
					} else if (bl.blockMaterial == Material.web) {
						names[q] = "f.";
					} else if (bl.blockMaterial == Material.piston) {
						names[q] = "4=";
					} else if (bl.blockMaterial.isSolid()) {
						names[q] = "8#";
					} else if (bl.blockMaterial.isLiquid()) {
						names[q] = "9~";
					}
				}
				namecfg.putString(makeNameId(q), names[q]);
			}
		}

		// add special letters
		namecfg.putString(makeNameId(Block.stone.blockID), "8#");
		namecfg.putString(makeNameId(Block.waterMoving.blockID), "9~");
		namecfg.putString(makeNameId(Block.waterStill.blockID), "9~");
		namecfg.putString(makeNameId(Block.lavaMoving.blockID), "c*");
		namecfg.putString(makeNameId(Block.lavaStill.blockID), "c*");
		namecfg.putString(makeNameId(Block.bedrock.blockID), "0@");
		namecfg.putString(makeNameId(Block.oreCoal.blockID), "7C");
		namecfg.putString(makeNameId(Block.oreDiamond.blockID), "bD");
		namecfg.putString(makeNameId(Block.oreLapis.blockID), "1L");
		namecfg.putString(makeNameId(Block.oreIron.blockID), "7I");
		namecfg.putString(makeNameId(Block.oreGold.blockID), "6G");
		namecfg.putString(makeNameId(Block.oreRedstone.blockID), "cR");
		namecfg.putString(makeNameId(Block.oreRedstoneGlowing.blockID), "cR");
		namecfg.putString(makeNameId(Block.blockDiamond.blockID), "bD");
		namecfg.putString(makeNameId(Block.blockLapis.blockID), "1L");
		namecfg.putString(makeNameId(Block.blockSteel.blockID), "7I");
		namecfg.putString(makeNameId(Block.blockGold.blockID), "6G");
		namecfg.putString(makeNameId(Block.dirt.blockID), "4%");
		namecfg.putString(makeNameId(Block.grass.blockID), "2%");
		namecfg.putString(makeNameId(Block.gravel.blockID), "7%");
		namecfg.putString(makeNameId(Block.sand.blockID), "e%");
		namecfg.putString(makeNameId(Block.torchWood.blockID), "ft");
		namecfg.putString(makeNameId(Block.torchRedstoneIdle.blockID), "4t");
		namecfg.putString(makeNameId(Block.torchRedstoneActive.blockID), "ct");
		namecfg.putString(makeNameId(Block.redstoneWire.blockID), "cr");
		namecfg.putString(makeNameId(Block.cobblestone.blockID), "8#");
		namecfg.putString(makeNameId(Block.stoneBrick.blockID), "8#");
		namecfg.putString(makeNameId(Block.sandStone.blockID), "6#");
		namecfg.putString(makeNameId(Block.obsidian.blockID), "1O");
		namecfg.putString(makeNameId(Block.planks.blockID), "6W");
		namecfg.putString(makeNameId(Block.wood.blockID), "6W");
		namecfg.putString(makeNameId(Block.rail.blockID), "f_");
		namecfg.putString(makeNameId(Block.railPowered.blockID), "f_");
		namecfg.putString(makeNameId(Block.railDetector.blockID), "f_");
		namecfg.putString(makeNameId(Block.ladder.blockID), "6|");
		namecfg.putString(makeNameId(Block.fence.blockID), "6+");
		namecfg.putString(makeNameId(Block.pressurePlateStone.blockID), "7_");
		namecfg.putString(makeNameId(Block.pressurePlatePlanks.blockID), "6_");
		namecfg.putString(makeNameId(Block.mushroomBrown.blockID), "4m");
		namecfg.putString(makeNameId(Block.mushroomRed.blockID), "cm");
		namecfg.putString(makeNameId(Block.mushroomCapBrown.blockID), "4M");
		namecfg.putString(makeNameId(Block.mushroomCapRed.blockID), "cM");
		namecfg.putString(makeNameId(Block.glowStone.blockID), "e#");
		namecfg.putString(makeNameId(Block.redstoneLampIdle.blockID), "e#");
		namecfg.putString(makeNameId(Block.redstoneLampActive.blockID), "e#");
		namecfg.putString(makeNameId(Block.netherrack.blockID), "4#");
		namecfg.putString(makeNameId(Block.slowSand.blockID), "7%");
		namecfg.putString(makeNameId(Block.netherBrick.blockID), "4#");
		namecfg.putString(makeNameId(Block.netherStalk.blockID), "c&");
		namecfg.putString(makeNameId(Block.whiteStone.blockID), "f#");

		namecfg.putString(makeNameId(mod_PCcore.powerCrystal.blockID), "aX");
		namecfg.putString(makeNameId(mod_PCcore.powerCrystal.blockID), "aX");

		namecfg.apply();

		for (int i = 0; i < 4096; i++) {
			if (namecfg.getString(makeNameId(i)) != null) {
				names[i] = (namecfg.getString(makeNameId(i)));
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		int[] offsetX = { 0, 0, 0, 0, 1, -1 };
		int[] offsetZ = { 0, 0, 1, -1, 0, 0 };
		int[] offsetY = { 1, -1, 0, 0, 0, 0 };
		int MoveX = offsetX[l];
		int MoveY = offsetY[l];
		int MoveZ = offsetZ[l];

		ArrayList<Integer> blocks = new ArrayList<Integer>(35);

		int shift = 0;
		if (entityplayer.isSneaking()) {
			shift = 36;
		}

		for (int q = shift; q < shift + 35; q++) {
			blocks.add(Integer.valueOf(world.getBlockId(i + MoveX * q, j + MoveY * q, k + MoveZ * q)));
		}

		String msg = "";

		for (Object id : blocks) {
			msg += "§" + names[(Integer) id];
		}

		PC_Utils.chatMsg("§7" + PC_Lang.tr("pc.sniffer.sniffing") + (shift > 0 ? "  " + PC_Lang.tr("pc.sniffer.away") : ""), true);
		PC_Utils.chatMsg("§7>§f " + msg + " §7<", false);

		itemstack.damageItem(1, entityplayer);

		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemstack, List list) {
		list.add(PC_Lang.tr("pc.sniffer.desc"));
	}
}
