package net.minecraft.src;


/**
 * Groups of items in the crafting tool.
 * 
 * @author MightyPork
 * @copy () 2012
 */
@SuppressWarnings("javadoc")
public enum PC_ItemGroup {
	PORTABLE, LOGIC, LOGIC_V, NETWORK, NETWORK_V, WIRELESS, WIRELESS_V, MACHINES, MACHINES_V, OPTICAL, OPTICAL_V,
	TRANSPORT, TRANSPORT_V, MOBILE, MOBILE_V, FIREWORKS, FIREWORKS_V, FUEL, FUEL_V,
	LIGHTS, LIGHTS_V, RECORDS, RECORDS_V, NON_FUNCTIONAL, NON_FUNCTIONAL_V, WOOD, WOOD_V,
	TOOLS, TOOLS_V, ARMOR, ARMOR_V, FARMING, FARMING_V, ALCHEMY_DROPS, ALCHEMY_DROPS_V,
	FOOD, FOOD_V, COLOURS, COLOURS_V, ORES, ORES_V, ROCKS_ETC, ROCKS_ETC_V;

	/**
	 * Index of the first element in group.
	 */
	public int index;

	private PC_ItemGroup() {
		this.index = mod_PCcore.G * (mod_PCcore.G++);
	}
}
