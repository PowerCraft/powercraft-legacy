package net.minecraft.src;


/**
 * Groups of items in the crafting tool.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public enum PC_CraftingToolGroup {
	/**
	 * Portable devices
	 */
	HANDHELD(1000),
	/**
	 * Logic gates, pulsar, integrated circuits, control lights
	 */
	LOGIC(2000),
	/**
	 * Radios, sensors
	 */
	WIRELESS(3000),
	/**
	 * Machines except laser and optical stuff
	 */
	MACHINES(4000),
	/**
	 * Optical things, lasers
	 */
	OPTICAL(5000),
	/**
	 * Transportation
	 */
	TRANSPORT(6000),
	/**
	 * Non-functinal blocks.
	 */
	DECORATIVE(7000),
	/**
	 * Mobile vehicles
	 */
	MOBILE(8000),
	/**
	 * Non-PowerCraft, but redstone related or used by powercraft
	 */
	VANILLA_RELATED(100000),
	/**
	 * Non-PowerCraft, tools (axe, hoe..)
	 */
	VANILLA_TOOLS(150000),
	/**
	 * Non-PowerCraft, armours
	 */
	VANILLA_ARMOUR(200000),
	/**
	 * Non-PowerCraft, unrelated
	 */
	VANILLA_OTHER(250000);

	/**
	 * Index of the first element in group.
	 */
	public int index;

	private PC_CraftingToolGroup(int index) {
		this.index = index;
	}
}
