package powercraft.machines;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import powercraft.launcher.PC_Logger;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_Struct3;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.Inventory;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.registry.PC_SoundRegistry;

public class PCma_TreeHarvestingManager {

	/**
	 * Folder with the crops xml files
	 */
	private static final File folder = new File(GameInfo.getPowerCraftFile(), "/trees");

	private static boolean treesLoaded = false;

	/** WOOD(id, meta), LEAVES(id, meta), SAPLING(id, meta) */
	private static ArrayList<PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>>> trees = new ArrayList<PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>>>();

	/**
	 * API method for adding custom trees from PowerCraft modules. Set leaves or
	 * sapling numbers to -1 to disable them.
	 * 
	 * @param WOOD_ID
	 * @param WOOD_META
	 * @param LEAVES_ID
	 * @param LEAVES_META
	 * @param SAPLING_ID
	 * @param SAPLING_META
	 */
	public static void registerTree(int WOOD_ID, int WOOD_META, int LEAVES_ID, int LEAVES_META, int SAPLING_ID, int SAPLING_META) {
		PC_Logger.finest("Registering new tree into Tree Harvesting Manager.");
		trees.add(new PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>>(
				new PC_Struct2<Integer, Integer>(WOOD_ID, WOOD_META), new PC_Struct2<Integer, Integer>(LEAVES_ID, LEAVES_META),
				new PC_Struct2<Integer, Integer>(SAPLING_ID, SAPLING_META)));
	}

	/**
	 * Check if block is wood.
	 * 
	 * @param wood_id block id
	 * @param wood_meta block meta
	 * @return tree definition
	 */
	public static PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> getStructForTree(int wood_id,
			int wood_meta) {
		for (PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> tree : trees) {
			if (tree.a.a == wood_id && (tree.a.b == wood_meta || tree.a.b == -1)) {
				return tree;
			}
		}
		return null;
	}

	/**
	 * Check if block is a wood of some tree
	 * 
	 * @param id block id
	 * @param meta block meta
	 * @return is part of tree
	 */
	public static boolean isBlockTreeWood(int id, int meta) {
		for (PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> tree : trees) {
			if (tree.a.a == id && (tree.a.b == meta || tree.a.b == -1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if block is a sapling of some tree
	 * 
	 * @param id block id
	 * @param meta block meta
	 * @return is part of tree
	 */
	public static boolean isBlockTreeSapling(int id, int meta) {
		for (PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> tree : trees) {
			if (tree.c != null && tree.c.a == id && (tree.c.b == meta || tree.c.b == -1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * API method - harvest tree at position, return list of stacks obtained.
	 * 
	 * @param world
	 * @param treeStart
	 * @return output array of stacks
	 */
	public static ItemStack[] harvestTreeAt(World world, PC_VecI treeStart) {

		PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> treeData = getStructForTree(
				GameInfo.getBID(world, treeStart), GameInfo.getMD(world, treeStart));
		ArrayList<ItemStack> harvestedStacks = new ArrayList<ItemStack>();

		if (treeData != null) {
			cnt = 0;
			chopTree(world, treeStart, treeStart, harvestedStacks, treeData);
			if (harvestedStacks.size() > 0) {
				return Inventory.groupStacks(Inventory.stacksToArray(harvestedStacks));
			}
		}

		return null;

	}

	/** Limit for the tree harvesting call (recursive) */
	public static int MAXLOGS = 1000;


	/** Counting already harvested blocks in this run */
	private static int cnt = 0;

	/**
	 * Recursively chop tree
	 * 
	 * @param world the world
	 * @param treeRootPos position of the tree's roots
	 * @param pos current block position (used for recursion, set to treeRootPos
	 *            at start)
	 * @param harvestedStacks list of stacks to eject
	 * @param treeData structure describing the current tree
	 */
	public static void chopTree(World world, PC_VecI treeRootPos, PC_VecI pos, ArrayList<ItemStack> harvestedStacks,
			PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> treeData) {

		if (cnt >= MAXLOGS || pos.distanceTo(treeRootPos.x, pos.y, treeRootPos.z) > 10) {
			return;
		}
		int id = GameInfo.getBID(world, pos);
		int meta = GameInfo.getMD(world, pos);

		int wood_id = treeData.a.a;
		int wood_meta = treeData.a.b;

		int leaves_id = treeData.b.a;
		int leaves_meta = treeData.b.b;

		int sapling_id = treeData.c.a;
		int sapling_meta = treeData.c.b;

		if(id == 127){
			harvestedStacks.add(new ItemStack(351, meta>=2?3:1, 3));
			ValueWriting.setBID(world, pos, 0, 0);
			return;
		}
		
		if ((id == wood_id && (meta == wood_meta || wood_meta == -1)) || ((meta & 3) == leaves_meta || leaves_meta == -1)) {

			int dropI = Block.blocksList[id].idDropped(meta, world.rand, 0);
			int dropM = Block.blocksList[id].damageDropped(meta);
			int dropQ = Block.blocksList[id].quantityDropped(world.rand);

			ItemStack dropped = new ItemStack(dropI, dropQ, dropM);

			if (dropQ > 0 && dropI > 0 && Item.itemsList[dropI] != null) {
				harvestedStacks.add(dropped);
			}

			ValueWriting.setBIDNoNotify(world, pos, 0, 0);

			if (world.rand.nextInt(10 - ((id == wood_id && (meta == wood_meta || wood_meta == -1)) ? 8 : 0)) == 0) {
				if (PC_SoundRegistry.isSoundEnabled()) {
					world.playAuxSFX(2001, pos.x, pos.y, pos.z, id + (meta << 12));
				}
			}

			if (cnt == 0) {
				// if not in tree, simply stop.
				if (GameInfo.getBID(world, pos.offset(0, 1, 0)) != wood_id || (GameInfo.getMD(world, pos.offset(0, 1, 0)) != wood_meta && wood_meta != -1)) {
					ValueWriting.notifyNeighbour(world, pos);
					return;
				}
				if (GameInfo.getBID(world, pos.offset(0, 2, 0)) != wood_id || (GameInfo.getMD(world, pos.offset(0, 2, 0)) != wood_meta && wood_meta != -1)) {
					ValueWriting.notifyNeighbour(world, pos);
					return;
				}
				if (GameInfo.getBID(world, pos.offset(0, 3, 0)) != wood_id || (GameInfo.getMD(world, pos.offset(0, 3, 0)) != wood_meta && wood_meta != -1)) {
					ValueWriting.notifyNeighbour(world, pos);
					return;
				}
			}


			cnt++;
			if (cnt >= MAXLOGS) {
				ValueWriting.notifyNeighbour(world, pos);
				return;
			}


			for (int x = pos.x - 1; x <= pos.x + 1; x++) {
				for (int y = pos.y - 1; y <= pos.y + 1; y++) {
					for (int z = pos.z - 1; z <= pos.z + 1; z++) {
						PC_VecI here = new PC_VecI(x, y, z);
						int here_id = GameInfo.getBID(world, here);
						int here_meta = GameInfo.getMD(world, here);
						if ((here_id == wood_id && ((here_meta&3) == wood_meta || wood_meta == -1))
								|| (here_id == leaves_id && ((here_meta & 3) == leaves_meta || leaves_meta == -1))
								|| (here_id == 127/*Cacao*/)) {
							chopTree(world, treeRootPos, here, harvestedStacks, treeData);
						}

					}
				}
			}
			
			ValueWriting.notifyNeighbour(world, pos);
			
			// replant sapling.
			if (GameInfo.getBID(world, pos.offset(0, -1, 0)) == Block.dirt.blockID) {
				if (sapling_id > 0 && sapling_meta >= 0) {
					ValueWriting.setBID(world, pos, sapling_id, sapling_meta);
				}
			}

		}
		return;
	}

	/**
	 * Load trees data from file.
	 */
	public static void loadTrees() {
		if (treesLoaded) {
			return;
		}

		PC_Logger.finer("Loading XML configuration for trees.");

		if (!folder.exists()) {
			folder.mkdir();
		}

		if (!(new File(folder + "/" + "default.xml")).exists()) {

			try {
				PC_Logger.finest("Generating default trees configuration file in " + folder + "/trees.xml");

				FileWriter out;

				out = new FileWriter(new File(folder + "/" + "default.xml"));

				//@formatter:off
				// write the default crops
				try {
					out.write("<?xml version='1.1' encoding='UTF-8' ?>\n" + "<!-- \n"
							+ " This file defines trees harvestable automatically (eg. by harvester machine)\n"
							+ " The purpose of this system is to make PowerCraft compatible with new trees from mods.\n"
							+ " All files in 'trees' directory will be parsed, so please DO NOT EDIT THIS FILE but make your own.\n"
							+ "-->\n\n"
							+ "<trees>\n"
							+ "\n"
							+ "\t<tree name='Oak'>\n"
							+ "\t\t<wood id='17' meta='0' />\n"
							+ "\t\t<leaves id='18' meta='0' />\n"
							+ "\t\t<sapling id='6' meta='0' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "\t<tree name='Pine'>\n"
							+ "\t\t<wood id='17' meta='1' />\n"
							+ "\t\t<leaves id='18' meta='1' />\n"
							+ "\t\t<sapling id='6' meta='1' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "\t<tree name='Birch'>\n"
							+ "\t\t<wood id='17' meta='2' />\n"
							+ "\t\t<leaves id='18' meta='2' />\n"
							+ "\t\t<sapling id='6' meta='2' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "\t<tree name='Jungle'>\n"
							+ "\t\t<wood id='17' meta='3' />\n"
							+ "\t\t<leaves id='18' meta='3' />\n"
							+ "\t\t<sapling id='6' meta='3' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "\t<tree name='Huge Brown Mushroom'>\n"
							+ "\t\t<wood id='99' meta='-1' />\n"
							+ "\t\t<sapling id='39' meta='0' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "\t<tree name='Huge Red Mushroom'>\n"
							+ "\t\t<wood id='100' meta='-1' />\n"
							+ "\t\t<sapling id='40' meta='0' />\n"
							+ "\t</tree>\n"
							+ "\n"
							+ "</trees>");
				} catch (IOException e) {
					e.printStackTrace();
				}
				//@formatter:on

				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String[] files = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.matches("[^.]+[.]xml");
			}
		});

		for (String filename : files) {

			PC_Logger.finest("* loading file " + filename + "...");
			File file = new File(folder + "/" + filename);
			parseFile(file);

		}

		PC_Logger.finer("Trees configuration loaded.");

		treesLoaded = true;

	}

	/**
	 * Load and parse XML file with tree specs
	 * 
	 * @param file the file to load
	 */
	private static void parseFile(File file) {

		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			doc.getDocumentElement().normalize();

			NodeList treesList = doc.getElementsByTagName("tree");

			treeloop:
			for (int i = 0; i < treesList.getLength(); i++) {

				Node treeNode = treesList.item(i);
				if (treeNode.getNodeType() == Node.ELEMENT_NODE) {

					// process one crop entry

					Element tree = (Element) treeNode;


					// <wood>
					NodeList woodlist = tree.getElementsByTagName("wood");
					if (woodlist.getLength() != 1) {
						PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - invalid no. of <wood> tags in <tree>");
						continue treeloop;
					}

					Element wood = (Element) woodlist.item(0);


					// <leaves>
					NodeList leaveslist = tree.getElementsByTagName("leaves");
					Element leaves = null;
					if (leaveslist.getLength() == 1) {
						leaves = (Element) leaveslist.item(0);
					}


					// <sapling>
					NodeList saplinglist = tree.getElementsByTagName("sapling");
					Element sapling = null;
					if (saplinglist.getLength() == 1) {
						sapling = (Element) saplinglist.item(0);
					}


					// parse wood.

					PC_Struct2<Integer, Integer> woodStruct;
					PC_Struct2<Integer, Integer> leavesStruct = new PC_Struct2<Integer, Integer>(-1, -1);
					PC_Struct2<Integer, Integer> saplingStruct = new PC_Struct2<Integer, Integer>(-1, -1);

					String woodId_s = wood.getAttribute("id");

					if (woodId_s.equals("") || !woodId_s.matches("[0-9]+")) {
						PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad wood ID");
						continue treeloop;
					}

					int wood_id = Integer.parseInt(woodId_s);

					String woodMeta_s = wood.getAttribute("meta");

					if (woodMeta_s.equals("") || !woodMeta_s.matches("-?[0-9]+")) {
						PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad wood meta");
						continue treeloop;
					}

					int wood_meta = Integer.parseInt(woodMeta_s);

					woodStruct = new PC_Struct2<Integer, Integer>(wood_id, wood_meta);


					if (leaves != null) {

						String leavesId_s = leaves.getAttribute("id");

						if (leavesId_s.equals("") || !leavesId_s.matches("[0-9]+")) {
							PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad leaves ID");
							continue treeloop;
						}

						int leaves_id = Integer.parseInt(leavesId_s);

						String leavesMeta_s = leaves.getAttribute("meta");

						if (leavesMeta_s.equals("") || !leavesMeta_s.matches("-?[0-9]+")) {
							PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad leaves meta");
							continue treeloop;
						}

						int leaves_meta = Integer.parseInt(leavesMeta_s);

						leavesStruct = new PC_Struct2<Integer, Integer>(leaves_id, leaves_meta);

					}

					if (sapling != null) {

						String saplingId_s = sapling.getAttribute("id");

						if (saplingId_s.equals("") || !saplingId_s.matches("[0-9]+")) {
							PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad sapling ID");
							continue treeloop;
						}

						int sapling_id = Integer.parseInt(saplingId_s);

						String saplingMeta_s = sapling.getAttribute("meta");

						if (saplingMeta_s.equals("") || !saplingMeta_s.matches("[0-9]+")) {
							PC_Logger.warning("Tree manager - parseFile - Error while parsing " + file + " - bad sapling meta");
							continue treeloop;
						}

						int sapling_meta = Integer.parseInt(saplingMeta_s);

						saplingStruct = new PC_Struct2<Integer, Integer>(sapling_id, sapling_meta);

					}

					PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>> struct = new PC_Struct3<PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>, PC_Struct2<Integer, Integer>>(
							woodStruct, leavesStruct, saplingStruct);

					trees.add(struct);

					PC_Logger.finest("   - Tree \"" + tree.getAttribute("name") + "\" loaded. -> " + struct);

				}

			}

		} catch (SAXParseException err) {
			PC_Logger.severe("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			PC_Logger.severe(" " + err.getMessage());
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
}
