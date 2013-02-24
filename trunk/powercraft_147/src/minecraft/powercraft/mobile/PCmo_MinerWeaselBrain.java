package powercraft.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Inventory;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;
import powercraft.management.gres.PC_GresTextEditMultiline.Keyword;
import powercraft.management.registry.PC_RecipeRegistry;
import powercraft.mobile.PCmo_Command.ParseException;
import powercraft.mobile.PCmo_EntityMiner.Agree;
import powercraft.weasel.PCws_IWeaselEngine;
import powercraft.weasel.PCws_IWeaselNetworkDevice;
import powercraft.weasel.PCws_WeaselHighlightHelper;
import powercraft.weasel.PCws_WeaselManager;
import powercraft.weasel.PCws_WeaselNetwork;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.exception.WeaselRuntimeExceptionFunctionNotExist;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


public class PCmo_MinerWeaselBrain  implements PCmo_IMinerBrain, PCws_IWeaselNetworkDevice, PCws_IWeaselEngine {

	private static final String default_program = 
			"# *** Weasel powered Miner ***\n";
	
	private PCmo_EntityMiner miner;
	/** weasel engine */
	private MinerProvider functionProvider; 
	private WeaselEngine engine;
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private int sleep = 0;
	private String program = default_program;
	private String error;
	private boolean stop=false;
	private static Random rand = new Random();
	/** Displayed text. "\n" is a newline. */
	private String text = "";
	
	private int networkID = -1;
	private int id;
	
	
	/**  */
	private List<String> userInput = new ArrayList<String>();
	
	public PCmo_MinerWeaselBrain(PCmo_EntityMiner miner, boolean server) {
		if(server){
			id = PCws_WeaselManager.registerPlugin(this);
		}else{
			id=-1;
		}
		functionProvider = new MinerProvider();
		engine = new WeaselEngine(functionProvider);
		this.miner = miner;
		miner.setInfo("text", "");
		miner.setInfo("color", new PC_Color(1, 1, 1));
		miner.setInfo("deviceName", Calc.generateUniqueName());
		miner.setInfo("networkName", "");
	}

	@Override
	public String getScriptName(){
		return "Weasel";
	}
	
	@Override
	public void setProgram(String prog){
		program = prog;
	}
	
	@Override
	public String getProgram(){
		return program;
	}
	
	@Override
	public void restart() {
		error = null;
		engine.restartProgramClearGlobals();
	}

	@Override
	public void launch() throws ParseException {
		restart();
		try {
			List<Instruction> list = WeaselEngine.compileProgram(program);
			engine.insertNewProgram(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage());
		}
	}

	private void setError(String error){
		this.error = error;
	}
	
	@Override
	public boolean hasError() {
		return error != null;
	}

	@Override
	public String getError() {
		return error;
	}

	@Override
	public void run() {
		if (!hasError()) {
			engine.setStatementsToRun(500);
			try {

				if (sleep > 0) {
					sleep--;
					return;
				}
				while(engine.getStatementsToRun()>0  && !engine.isProgramFinished){
					if(!engine.run())
						break;
				}
			} catch (WeaselRuntimeException wre) {
				wre.printStackTrace();
				setError(wre.getMessage());
			}
		}
	}
	
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>();
		list.add("run");
		list.add("do");
		
		list.add("fw");
		list.add("forward");
		list.add("go");
		
		list.add("bw");
		list.add("back");
		list.add("backward");
		
		list.add("up");
		list.add("down");			
		
		list.add("left");
		list.add("right");
		
		list.add("turn");
		
		list.add("north");
		list.add("south");
		list.add("east");
		list.add("west");
		list.add("xplus");
		list.add("xminus");
		list.add("zplus");
		list.add("zminus");

		list.add("deminer.posit");
		list.add("depo");
		list.add("store");
		list.add("depoKeep");
		list.add("storeKeep");
		list.add("countStacks");
		list.add("stacks");
		list.add("countItems");
		list.add("items");
		list.add("countEmpty");
		list.add("full");
		list.add("isFull");
		list.add("empty");
		list.add("isEmpty");
		list.add("countFuel");
		list.add("fuel");
		
		list.add("destroyMiner");

		list.add("idMatch");
		list.add("ideq");
		list.add("getBlock");
		list.add("setBlock");
		list.add("place");
		list.add("getId");
		list.add("idAt");
		list.add("blockAt");
		list.add("canHarvest");

		list.add("cleanup");
		list.add("burn");
		list.add("destroy");
		list.add("burnItems");
		list.add("destroyItems");
		list.add("burnKeep");
		list.add("destroyKeep");

		list.add("bell");
		list.add("isDay");
		list.add("isNight");
		list.add("isRaining");

		list.add("sleep");

		list.add("global.get");
		list.add("global.set");
		list.add("global.has");

		list.add("cap");
		list.add("opt");
		list.add("miner.getFlag(PCmo_EntityMiner");
		list.add("can");
		list.add("hasCap");
		list.add("hasOpt");
		list.add("clearOpt");
		list.add("clearCap");
		list.add("clearminer.getFlag(PCmo_EntityMiner");
		list.add("resetOpt");
		list.add("resetCap");
		list.add("resetminer.getFlag(PCmo_EntityMiner");
		list.add("capOn");
		list.add("optOn");
		list.add("miner.getFlag(PCmo_EntityMinerOn");
		list.add("capOff");
		list.add("optOff");
		list.add("miner.getFlag(PCmo_EntityMinerOff");
		
		list.add("term.clear");
		list.add("term.cls");
		list.add("term.print");
		list.add("term.out");
		list.add("term.hasInput");
		list.add("term.getInput");
		list.add("term.in");
		list.add("term");
		
		return list;
	}
	
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		try {
			if (functionName.equals("bell")) {
				miner.worldObj.playSoundEffect(miner.posX + 1D, miner.posY + 2D, miner.posZ + 1D, "random.orb", 0.8F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
				miner.worldObj.spawnParticle("note", miner.posX, miner.posY + 1.5D, miner.posZ, (functionName.length() * (3 + args.length)) / 24D, 0.0D, 0.0D);
				return null;
			}

			if (functionName.equals("clearCap") || functionName.equals("clearminer.getFlag(PCmo_EntityMiner") || functionName.equals("clearOpt") || functionName.equals("resetCap")
					|| functionName.equals("resetminer.getFlag(PCmo_EntityMiner") || functionName.equals("resetOpt")) {
				miner.setFlag(PCmo_EntityMiner.airFillingEnabled, false);
				miner.setFlag(PCmo_EntityMiner.bridgeEnabled, false);
				miner.setFlag(PCmo_EntityMiner.compressBlocks, false);
				miner.setFlag(PCmo_EntityMiner.keepAllFuel, false);
				miner.setFlag(PCmo_EntityMiner.lavaFillingEnabled, false);
				miner.setFlag(PCmo_EntityMiner.miningEnabled, false);
				miner.setFlag(PCmo_EntityMiner.torches, false);
				miner.setFlag(PCmo_EntityMiner.torchesOnlyOnFloor, false);
				miner.setFlag(PCmo_EntityMiner.waterFillingEnabled, false);
				miner.setFlag(PCmo_EntityMiner.cobbleMake, false);
				return null;
			}

			if (functionName.equals("sleep")) {
				sleep += Calc.toInteger(args[0]);
				engine.requestPause();
				return null;
			}

			if (functionName.equals("run")) functionName = "do";



			if (functionName.equals("forward")) functionName = "fw";
			if (functionName.equals("go")) functionName = "fw";

			if (functionName.equals("fw")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}

				// spaces are for safety - when there are two numbers next to each other.
				miner.appendCode(" " + num + " ");
				engine.requestPause();
				return null;
			}

			if (functionName.equals("backward")) functionName = "bw";
			if (functionName.equals("back")) functionName = "bw";

			if (functionName.equals("bw")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}
				num = -num;
				// spaces are for safety - when there are two numbers next to each other.
				miner.appendCode(" " + num + " ");
				engine.requestPause();
				return null;
			}else if (functionName.equals("left")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}
				boolean R = num < 0;
				if (R) num = -num;
				for (int i = 0; i < num; i++) {
					miner.appendCode(R ? "R" : "L");
				}
				engine.requestPause();
				return null;
			}else if (functionName.equals("up")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}

				for (int i = 0; i < num; i++) {
					miner.appendCode("U");
				}
				engine.requestPause();
				return null;
			}else if (functionName.equals("down")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}

				for (int i = 0; i < num; i++) {
					miner.appendCode("D");
				}
				engine.requestPause();
				return null;
			}else if (functionName.equals("right")) {
				int num = 1;
				if (args.length == 1) {
					num = Calc.toInteger(args[0]);
				}
				boolean L = num < 0;
				if (L) num = -num;
				for (int i = 0; i < num; i++) {
					miner.appendCode(L ? "L" : "R");
				}
				engine.requestPause();
				return null;
			}

			if (functionName.equals("turn")) {
				do {
					if (args.length > 0 && args[0] instanceof WeaselString) {
						functionName = "do";
						break; //redir to do
					}
					int num = 2;
					if (args.length == 1) {
						num = Calc.toInteger(args[0]);
					}
					boolean L = num < 0;
					if (L) num = -num;
					for (int i = 0; i < num; i++) {
						miner.appendCode(L ? "L" : "R");
					}
					engine.requestPause();
					return null;
				} while (false);
			}


			if (functionName.equals("do")) {
				int num = 1;
				if (args.length == 2) {
					num = Calc.toInteger(args[1]);
				}
				for (int i = 0; i < num; i++) {
					miner.appendCode(Calc.toString(args[0]));
				}
				engine.requestPause();
				return null;
			}


			if (functionName.equals("xplus")) functionName = "east";
			if (functionName.equals("xminus")) functionName = "west";
			if (functionName.equals("zplus")) functionName = "south";
			if (functionName.equals("zminus")) functionName = "north";

			if (functionName.equals("north")) {
				miner.appendCode("N");
				engine.requestPause();
				return null;
			}else if (functionName.equals("south")) {
				miner.appendCode("S");
				engine.requestPause();
				return null;
			}else if (functionName.equals("east")) {
				miner.appendCode("E");
				engine.requestPause();
				return null;
			}else if (functionName.equals("west")) {
				miner.appendCode("W");
				engine.requestPause();
				return null;
			}else if (functionName.equals("capOff") || functionName.equals("miner.getFlag(PCmo_EntityMinerOff") || functionName.equals("optOff") || functionName.equals("capOn") || functionName.equals("miner.getFlag(PCmo_EntityMinerOn")
					|| functionName.equals("optOn") || functionName.equals("miner.getFlag(PCmo_EntityMiner") || functionName.equals("opt") || functionName.equals("cap")) {

				int state = -1;
				if (functionName.equals("capOff") || functionName.equals("miner.getFlag(PCmo_EntityMinerOff") || functionName.equals("optOff")) state = 0;
				if (functionName.equals("capOn") || functionName.equals("miner.getFlag(PCmo_EntityMinerOn") || functionName.equals("optOn")) state = 1;

				for (int i = 0; i < (state == -1 ? 1 : args.length); i++) {

					if (args[i] instanceof WeaselDouble) {
						int cap = Calc.toInteger(args[i]);
						if (cap == Block.cobblestone.blockID) {
							args[i] = new WeaselString("COBBLE");
						} else if (cap == i) {
							args[i] = new WeaselString("TUNNEL");
						} else if (cap == i) {
							args[i] = new WeaselString("TUNNEL");
						} else if (cap == Block.waterMoving.blockID) {
							args[i] = new WeaselString("WATER");
						} else if (cap == Block.lavaMoving.blockID) {
							args[i] = new WeaselString("LAVA");
						}
					}

					String capname = Calc.toString(args[i]);
					int argl = args.length;
					
					boolean flag = false;
					if(state == -1) {
						if(argl == 1) {
							flag = false;
						}else {
							flag = Calc.toBoolean(args[1]);
						}
					}else {
						flag = state > 0;
					}

					if (capname.equals("KEEP_FUEL")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.keepAllFuel));
						miner.setFlag(PCmo_EntityMiner.keepAllFuel, flag);
						continue;
					}
					if (capname.equals("COBBLE")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.cobbleMake));
						miner.setFlag(PCmo_EntityMiner.cobbleMake, flag);
						continue;
					}
					if (capname.equals("TORCHES")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.torches));
						miner.setFlag(PCmo_EntityMiner.torches, flag);
						continue;
					}
					if (capname.equals("TORCH_FLOOR")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.torchesOnlyOnFloor));
						miner.setFlag(PCmo_EntityMiner.torchesOnlyOnFloor, flag);
						continue;
					}
					if (capname.equals("COMPRESS")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.compressBlocks));
						miner.setFlag(PCmo_EntityMiner.compressBlocks, flag);
						continue;
					}
					if (capname.equals("MINING")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.miningEnabled));
						miner.setFlag(PCmo_EntityMiner.miningEnabled, flag);
						continue;
					}
					if (capname.equals("BRIDGE")) {						
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.bridgeEnabled));
						miner.setFlag(PCmo_EntityMiner.bridgeEnabled, flag);
						continue;
					}
					if (capname.equals("LAVA")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.lavaFillingEnabled));
						miner.setFlag(PCmo_EntityMiner.lavaFillingEnabled, flag);
						continue;
					}
					if (capname.equals("WATER")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.waterFillingEnabled));
						miner.setFlag(PCmo_EntityMiner.waterFillingEnabled, flag);
						continue;
					}
					if (capname.equals("TUNNEL")) {
						if (argl == 1&&state == -1) return new WeaselBoolean(miner.getFlag(PCmo_EntityMiner.airFillingEnabled));
						miner.setFlag(PCmo_EntityMiner.airFillingEnabled, flag);
						continue;
					}
					throw new WeaselRuntimeException(functionName + "(): Unknown option " + capname);
				}

				//what else?
				return null;
			}else if (functionName.equals("can") || functionName.equalsIgnoreCase("hasOpt") || functionName.equalsIgnoreCase("hasCap")) {
				String capname = Calc.toString(args[0]);

				if (capname.equals("KEEP_FUEL")) {
					return new WeaselBoolean(true);
				}
				if (capname.equals("TORCHES")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LTORCH);
				}
				if (capname.equals("TORCH_FLOOR")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LTORCH);
				}
				if (capname.equals("COMPRESS")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LCOMPRESS);
				}
				if (capname.equals("MINING")) {
					return new WeaselBoolean(true);
				}
				if (capname.equals("BRIDGE")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LBRIDGE);
				}
				if (capname.equals("LAVA")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LLAVA);
				}
				if (capname.equals("WATER")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LWATER);
				}
				if (capname.equals("COBBLE")) {
					return new WeaselBoolean(miner.st.level >= PCmo_EntityMiner.LCOBBLE);
				}
				throw new WeaselRuntimeException(functionName + "(): Unknown option " + capname);
			}else if (functionName.equals("destroyItems") || functionName.equals("burnItems") || functionName.equals("destroy") || functionName.equals("burn") || functionName.equals("depo")
					|| functionName.equals("deminer.posit") || functionName.equals("store")) {

				boolean kill = functionName.equals("destroyItems") || functionName.equals("burnItems") || functionName.equals("destroy") || functionName.equals("burn");

				if (args.length == 0) {
					miner.cargo.depositToNearbyChest(kill, null);
				} else {
					int num = 0;
					if (args[0] instanceof WeaselDouble) {
						num = Calc.toInteger(args[0]);

						if (args.length == 1) {
							final int id = num;
							// if args length == 1, then this is type, not amount
							miner.cargo.depositToNearbyChest(kill, new Agree() {
								@Override
								public boolean agree(ItemStack stack) {
									return stack.itemID == id;
								}
							});
							return null;
						}

					} else {
						if (args.length == 1) {

							// if args length == 1, then this is type, not amount
							miner.cargo.depositToNearbyChest(kill, new Agree() {
								private WeaselObject obj;
								
								public Agree set(WeaselObject obj) {
									this.obj = obj;
									return this;
								}
								
								@Override
								public boolean agree(ItemStack stack) {
									return miner.matchStackIdentifier(obj, stack);
								}
							}.set(args[0]));

							return null;
						}

						num = -1;
					}

					// num = count, others are types.
					miner.cargo.depositToNearbyChest(kill, new Agree() {
						
						private WeaselObject args[];
						private int n = 0;
						
						public Agree set(int n, WeaselObject args[]) {
							this.n = n;
							this.args = args;
							return this;
						}
						@Override
						public boolean agree(ItemStack stack) {
							if (agree_do(stack)) {
								if (n > 0) n--;
								return true;
							} else
								return false;
						}

						private boolean agree_do(ItemStack stack) {
							if (n > 0 || n == -1) {
								for (int i = 1; i < args.length; i++) {
									WeaselObject arg = args[i];
									if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
								}
							}
							return false;
						}

					}.set(num, args));
				}
				engine.requestPause();
				return null;
			}else if (functionName.equals("destroyKeep") || functionName.equals("burnKeep") || functionName.equals("storeKeep") || functionName.equals("depoKeep")) {
				final boolean kill = functionName.equals("destroyKeep") || functionName.equals("burnKeep");
				if (args.length == 0) {
					throw new WeaselRuntimeException("depoKeep needs at least 1 argument, 0 given.");
				} else {
					int num = 0;
					if (args[0] instanceof WeaselDouble) {
						num = Calc.toInteger(args[0]);
						if (args.length == 1) {
							final int id = num;
							// if args length == 1, then this is type, not amount
							miner.cargo.depositToNearbyChest(kill, new Agree() {
								@Override
								public boolean agree(ItemStack stack) {
									return stack.itemID != id;
								}
							});
							return null;
						}
					} else {

						if (args.length == 1) {

							// if args length == 1, then this is type, not amount
							miner.cargo.depositToNearbyChest(kill, new Agree() {
								private WeaselObject obj;
								
								public Agree set(WeaselObject obj) {
									this.obj = obj;
									return this;
								}
								
								@Override
								public boolean agree(ItemStack stack) {
									return !agree_do(stack);
								}

								private boolean agree_do(ItemStack stack) {
									return PCmo_EntityMiner.matchStackIdentifier(obj, stack);
								}
							}.set(args[0]));

							return null;
						}// end of "len 1 string"

						num = -1;
					}

					// num = count, others are types.
					miner.cargo.depositToNearbyChest(kill, new Agree() {
						
						private WeaselObject args[];
						private int n = 0;
						
						public Agree set(int n, WeaselObject args[]) {
							this.n = n;
							this.args = args;
							return this;
						}

						@Override
						public boolean agree(ItemStack stack) {
							if (agree_do(stack)) {
								if (n == -1 || n > 0) {
									if (n != -1) n--;
									return false;
								} else {
									return true;
								}
							} else
								return true;
						}

						private boolean agree_do(ItemStack stack) {
							if (n > 0 || n == -1) {
								for (int i = 1; i < args.length; i++) {
									WeaselObject arg = args[i];
									if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
								}
							}
							return false;
						}

					}.set(num, args));
				}
				engine.requestPause();
				return null;
			}else if (functionName.equals("items") || functionName.equals("countItems")) {
				int cnt = 0;
				oo:
				for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
					ItemStack stack = miner.cargo.getStackInSlot(i);
					for (int j = 0; j < args.length; j++) {
						WeaselObject arg = args[j];
						if (stack == null) continue oo;
						if (arg instanceof WeaselDouble) {
							if (stack.itemID == Calc.toInteger(arg)) {
								cnt += stack.stackSize;
								continue oo;
							}
						} else {
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) {
								cnt += stack.stackSize;
								continue oo;
							}
						}
					}
				}

				return new WeaselDouble(cnt);
			}else if (functionName.equals("stacks") || functionName.equals("countStacks")) {
				int cnt = 0;
				oo:
				for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
					ItemStack stack = miner.cargo.getStackInSlot(i);
					for (int j = 0; j < args.length; j++) {
						WeaselObject arg = args[j];
						if (stack == null) continue oo;
						if (arg instanceof WeaselDouble) {
							if (stack.itemID == Calc.toInteger(arg)) {
								cnt++;
								continue oo;
							}
						} else {
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) {
								cnt++;
								continue oo;
							}
						}
					}
				}

				return new WeaselDouble(cnt);
			}else if (functionName.equals("idMatch") || functionName.equals("ideq")) {

				int id1 = Calc.toInteger(args[0]);

				WeaselObject arg = args[1];

				ItemStack stack = new ItemStack(id1, 1, 0);

				if (stack.itemID == 0) return new WeaselBoolean(arg instanceof WeaselDouble && Calc.toInteger(arg) == 0);

				if (stack.getItem() == null) throw new WeaselRuntimeException(args[0] + " is not a valid block/item ID.");

				return new WeaselBoolean((PCmo_EntityMiner.matchStackIdentifier(arg, stack)));
			}else if (functionName.equals("countEmpty")) {
				int cnt = 0;
				for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
					ItemStack stack = miner.cargo.getStackInSlot(i);
					if (stack == null) cnt++;
				}

				return new WeaselDouble(cnt);
			}else if (functionName.equals("full") || functionName.equals("isFull")) {
				boolean str = args.length == 1 && Calc.toBoolean(args[0]);
				if (str) return new WeaselBoolean(Inventory.isInventoryFull(miner.cargo));
				return new WeaselBoolean(Inventory.hasInventoryNoFreeSlots(miner.cargo));
			}else if (functionName.equals("empty") || functionName.equals("isEmpty")) {
				return new WeaselBoolean(Inventory.isInventoryEmpty(miner.cargo));
			}else if (functionName.equals("destroyMiner")) {
				miner.turnIntoBlocks();
				return null;
			}else if (functionName.equals("isDay")) {
				return new WeaselBoolean(miner.worldObj.isDaytime());
			}else if (functionName.equals("idNight")) {
				return new WeaselBoolean(!miner.worldObj.isDaytime());
			}else if (functionName.equals("isRaining")) {
				return new WeaselBoolean(miner.worldObj.isRaining());
			}else if (functionName.equals("global.set")) {
				PCws_WeaselManager.setGlobalVariable(Calc.toString(args[0]), args[1]);
				return null;
			}else if (functionName.equals("global.has")) {
				return new WeaselBoolean(PCws_WeaselManager.hasGlobalVariable(Calc.toString(args[0])));

			}else if (functionName.equals("global.get")) {
				return PCws_WeaselManager.getGlobalVariable(Calc.toString(args[0]));
			}else if (functionName.equals("countFuel") || functionName.equals("fuel")) {
				int cnt = 0;
				for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
					ItemStack stack = miner.cargo.getStackInSlot(i);
					if (stack == null) continue;
					if (stack.itemID != Item.bucketLava.itemID || !miner.getFlag(PCmo_EntityMiner.cobbleMake)) {
						cnt += PC_RecipeRegistry.getFuelValue(stack) * PCmo_EntityMiner.FUEL_STRENGTH * stack.stackSize;
					}
				}

				return new WeaselDouble(cnt + miner.st.fuelBuffer);
			}else if (functionName.equals("canHarvest")) {
				String side = Calc.toString(args[0]);
				char sid = side.charAt(0);
				String num = side.substring(1);

				PC_VecI pos = miner.getCoordOnSide(sid, Integer.valueOf(num));
				return new WeaselDouble(miner.canHarvestBlockWithCurrentLevel(pos, GameInfo.getBID(miner.worldObj, pos)));
			}else if (functionName.equals("getBlock") || functionName.equals("getId") || functionName.equals("idAt") || functionName.equals("blockAt")) {
				String side = Calc.toString(args[0]);
				char sid = side.charAt(0);
				String num = side.substring(1);

				return new WeaselDouble(GameInfo.getBID(miner.worldObj, miner.getCoordOnSide(sid, Integer.valueOf(num))));
			}else if (functionName.equals("cleanup")) {
				miner.cargo.order();
				return null;
			}else if (functionName.equals("place") || functionName.equals("setBlock")) {
				String side = Calc.toString(args[0]);
				char sid = side.charAt(0);
				String num = side.substring(1);

				Object id = args[1];
				String str = "";

				int numid = -1;

				if (id instanceof WeaselDouble) {
					numid = Calc.toInteger(id);
				}

				if (id instanceof WeaselString) {
					numid = -2;
					str = Calc.toString(id);
				}

				if (numid == -1) throw new WeaselRuntimeException(id + " is not a valid block id or group.");

				PC_VecI pos = miner.getCoordOnSide(sid, Integer.valueOf(num));
				if (pos == null) {
					throw new WeaselRuntimeException(functionName + "(): " + side + " is not a valid side [FBLRUD][1234] or [ud][12].");
				}

				if (str.equals("BUILDING_BLOCK") || str.equals("BLOCK")) {
					ItemStack placed = miner.cargo.getBlockForBuilding();
					if (placed == null) {
						return new WeaselBoolean(false);
					} else {
						if (!placed.tryPlaceItemIntoWorld(miner.fakePlayer, miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f)) {
							Inventory.addItemStackToInventory(miner.cargo, placed);
						} else {
							return new WeaselBoolean(true);
						}
					}
				}
				
				if (numid == -2) {
					
				}

				if (numid != -2) {
					for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
						ItemStack stack = miner.cargo.getStackInSlot(i);
						if (stack == null) continue;

						if (stack.itemID == numid) {
							ItemStack placed = miner.cargo.decrStackSize(i, 1);
							if (!placed.tryPlaceItemIntoWorld(miner.fakePlayer, miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f)) {
								Inventory.addItemStackToInventory(miner.cargo, placed);
							} else {
								return new WeaselBoolean(true);
							}
						}
					}
					
					if(numid == Block.cobblestone.blockID && miner.canMakeCobble()) {
						return new WeaselBoolean((new ItemStack(Block.cobblestone)).tryPlaceItemIntoWorld(miner.fakePlayer, 
								miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f));		
					}
						
				}

				return new WeaselBoolean(false);
			}else if(functionName.equals("term.clear") || functionName.equals("term.cls")){
				text = "";
				userInput.clear();
				miner.setInfo("text", text);
				return new WeaselNull();
			}else if(functionName.equals("term.print") || functionName.equals("term.out")){
				addText(Calc.toString(args[0]) + "\n");
				return new WeaselNull();
			}else if(functionName.equals("term.hasInput")){
				return new WeaselBoolean(userInput.size() > 0);
			}else if(functionName.equals("term.getInput") || functionName.equals("term.in")){
				if (userInput.size() == 0) return  new WeaselString();
				String s = userInput.get(0);
				userInput.remove(0);
				return new WeaselString(s);
			}else if(functionName.equals("term")){
				if(args.length>0){
					addText(Calc.toString(args[0]) + "\n");
					return new WeaselNull();
				}else{
					if (userInput.size() == 0) return  new WeaselString();
					String s = userInput.get(0);
					userInput.remove(0);
					return new WeaselString(s);
				}
			}
				
		} catch (ParseException e) {
			e.printStackTrace();
			throw new WeaselRuntimeException(e.getMessage());
		}

		throw new WeaselRuntimeException(functionName + " not implemented or not ended properly.");
	}

	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(0);
		list.add("miner.pos.x");
		list.add("miner.pos.y");
		list.add("miner.pos.z");
		list.add("dir");
		list.add("dir.axis");
		list.add("dir.compass");
		list.add("dir.angle");
		list.add("axis");
		list.add("angle");
		list.add("compass");
		list.add("level");
		list.add("term.text");
		list.add("term.txt");
		return list;
	}
	
	public WeaselObject getVariable(String name) {
		if (name.equals("miner.pos.x")) {
			return new WeaselDouble(Math.round(miner.posX));
		}else if (name.equals("level")) {
			miner.updateLevel();
			return new WeaselDouble(miner.st.level);
		}else if (name.equals("miner.pos.y")) {
			return new WeaselDouble(Math.round(miner.posY) + (miner.isOnHalfStep() ? 1 : 0));
		}else if (name.equals("miner.pos.z")) {
			return new WeaselDouble(Math.round(miner.posZ));
		}else if (name.equals("angle") || name.equals("dir.angle")) {
			int rot = miner.getRotationRounded();
			return new WeaselString(rot);
		}else if (name.equals("dir") || name.equals("dir.axis") || name.equals("axis")) {
			int rot = miner.getRotationRounded();
			return new WeaselString(rot == 0 ? "x-" : rot == 90 ? "z-" : rot == 180 ? "x+" : "z+");
		}else if (name.equals("dir.compass") || name.equals("compass")) {
			int rot = miner.getRotationRounded();
			return new WeaselString(rot == 0 ? "W" : rot == 90 ? "N" : rot == 180 ? "E" : "S");
		}else if (name.equals("term.text") || name.equals("term.txt")) {
			return new WeaselString(text);
		}

		return null;
	}

	public void setVariable(String name, WeaselObject object) {
		if (name.equals("term.text") || name.equals("term.txt")) {
			text = "";
			addText(text);
		}
	}

	@Override
	public List<Keyword> getKeywords() {
		return PCws_WeaselHighlightHelper.weasel(functionProvider, engine);
	}
	
	@Override
	public PCmo_IMinerBrain readFromNBT(NBTTagCompound nbttag) {
		if(id!=-1){
			PCws_WeaselManager.removePlugin(this);
			id = nbttag.getInteger("id");
			PCws_WeaselManager.registerPlugin(this, id);
		}
		networkID = nbttag.getInteger("networkID");
		miner.setInfo("deviceName", nbttag.getString("name"));
		SaveHandler.loadFromNBT(nbttag, "engine", engine);
		sleep = nbttag.getInteger("sleep");
		program = nbttag.getString("program");
		if(nbttag.hasKey("error"))
			error = nbttag.getString("error");
		if(getNetwork()!=null){
			getNetwork().registerMember(this);
		}
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("id", id);
		nbttag.setInteger("networkID", networkID);
		nbttag.setString("name", (String)miner.getInfo("deviceName"));
		SaveHandler.saveToNBT(nbttag, "engine", engine);
		nbttag.setInteger("sleep", sleep);
		nbttag.setString("program", program);
		if(error!=null)
			nbttag.setString("error", error);
		return nbttag;
	}

	@Override
	public void compileProgram(String text) throws Exception {
		WeaselEngine.compileProgram(text);
	}

	public boolean doesProvideFunctionOnEngine(String functionName) {
		return engine.instructionList.hasFunctionForExternalCall(functionName);
	}
	
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {
		if(hasError()||stop)
			return;
		if(sleep<=0){
			try{
				int state = engine.callFunctionExternal(functionName, (Object[])args);
				if(state == -1 || state == 1) return;	
			} catch (WeaselRuntimeException wre) {
				setError(wre.getMessage());
				return;	
			} catch (Exception e) {
				e.printStackTrace();
				setError(e.getMessage());
				return;	
			}
		}
		externalCallsWaiting.add(new PC_Struct2<String, Object[]>(functionName, args));
	}
	
	/**
	 * Add text to the terminal, if too long remove oldest.
	 * @param text
	 */
	public void addText(String text) {
		this.text += text.replace("\\n", "\n");
		if (countIn(this.text, '\n') > 60) {
			while (countIn(this.text, '\n') > 60) {
				this.text = this.text.substring(this.text.indexOf('\n') + 1);
			}
		}
		miner.setInfo("text", this.text);
	}
	
	private int countIn(String str, char c) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				counter++;
			}
		}
		return counter;
	}
	
	public void removeFromNetwork(){
		PCws_WeaselNetwork oldNetwork = getNetwork();
		if(oldNetwork!=null){
			oldNetwork.removeMember(this);
			miner.setInfo("networkName", "");
		}
	}
	
	public void connectToNetwork(PCws_WeaselNetwork network){
		removeFromNetwork();
		network.registerMember(this);
		miner.setInfo("networkName", network.getName());
		miner.setInfo("color", network.getColor());
	}
	
	@Override
	public void msg(Object[] obj) {
		if("input".equals(obj[0])){
			String input = (String)obj[1];
			input = input.trim();
			if (input.length() > 0) {
				userInput.add(input);
				addText("> " + input + "\n");
				if(doesProvideFunctionOnEngine("termIn")){
					callFunctionOnEngine("termIn", new WeaselString(getName()), new WeaselString(input));
				}
			}
			if (userInput.size() > 16) {
				userInput.remove(0);
			}
		}else if(obj[0].equals("deviceRename")){
			if(getNetwork()!=null){
				getNetwork().renameMember(this, (String)obj[1]);
			}
			miner.setInfo("deviceName", (String)obj[1]);
		}else if(obj[0].equals("networkJoin")){
			if(((String) obj[1]).equals("")){
				removeFromNetwork();
			}else{
				connectToNetwork(PCws_WeaselManager.getNetwork((String) obj[1]));
			}
		}else if(obj[0].equals("networkRename")){
			if(getNetwork()==null){
				connectToNetwork(new PCws_WeaselNetwork());
			}
			getNetwork().setName((String) obj[1]);
		}else if(obj[0].equals("networkNew")){
			connectToNetwork(new PCws_WeaselNetwork());
			getNetwork().setName((String) obj[1]);
		}else if(obj[0].equals("networkColor")){
			if(getNetwork()!=null){
				getNetwork().setColor((PC_Color)obj[1]);
			}
		}
	}

	@Override
	public WeaselEngine getEngine() {
		return engine;
	}

	@Override
	public void setNetwork(int i) {
		networkID = i;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public WeaselFunctionManager makePluginProvider() {
		return new MinerProvider2();
	}

	@Override
	public void setNetworkName(String name) {
		miner.setInfo("networkName", name);
	}

	@Override
	public void setNetworkColor(PC_Color color) {
		miner.setInfo("color", color);
	}

	@Override
	public PCws_WeaselNetwork getNetwork() {
		if(networkID==-1)
			return null;
		return PCws_WeaselManager.getNetwork(networkID);
	}
	
	public class MinerProvider extends WeaselFunctionManager{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject... args) throws WeaselRuntimeException {
			if(var){
				if(PCmo_MinerWeaselBrain.this.getProvidedVariableNames().contains(name)){
					if(args.length==0){
						return PCmo_MinerWeaselBrain.this.getVariable(name);
					}else{
						PCmo_MinerWeaselBrain.this.setVariable(name, args[0]);
						return new WeaselNull();
					}
				}
			}else{
				if(PCmo_MinerWeaselBrain.this.doesProvideFunction(name)){
					return PCmo_MinerWeaselBrain.this.callProvidedFunction(engine, name, args);
				}
			}
			try{
				return PCws_WeaselManager.getGlobalFunctionManager().call(engine, name, var, args);
			}catch(WeaselRuntimeExceptionFunctionNotExist e1){
				if(getNetwork()==null){
					throw e1;
				}else{
					return getNetwork().getFunctionHandler().call(engine, name, var, args);
				}
			}
		}

		@Override
		public boolean doesProvideFunction(String name) {
			if(PCmo_MinerWeaselBrain.this.doesProvideFunction(name))
				return true;
			if(PCws_WeaselManager.getGlobalFunctionManager().doesProvideFunction(name))
				return true;
			if(getNetwork()==null)
				return false;
			return getNetwork().getFunctionHandler().doesProvideFunction(name);
		}
		
		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = PCmo_MinerWeaselBrain.this.getProvidedFunctionNames();
			list.addAll(PCws_WeaselManager.getGlobalFunctionManager().getProvidedFunctionNames());
			if(getNetwork()!=null){
				list.addAll(getNetwork().getFunctionHandler().getProvidedFunctionNames());
			}
			return list;
		}
		
		@Override
		public List<String> getProvidedVariableNames() {
			List<String> list = PCmo_MinerWeaselBrain.this.getProvidedVariableNames();
			if(getNetwork()!=null){
				list.addAll(getNetwork().getFunctionHandler().getProvidedVariableNames());
			}
			return list;
		}
		
	}

	public class MinerProvider2 extends WeaselFunctionManager{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject... args) throws WeaselRuntimeException {
			if(var){
				if(PCmo_MinerWeaselBrain.this.getProvidedVariableNames().contains(name)){
					if(args.length==0){
						return PCmo_MinerWeaselBrain.this.getVariable(name);
					}
				}else{
					throw new WeaselRuntimeExceptionFunctionNotExist(name);
				}
			}else{
				if(PCmo_MinerWeaselBrain.this.doesProvideFunction(name)){
					return PCmo_MinerWeaselBrain.this.callProvidedFunction(engine, name, args);
				}
			}
			return PCws_WeaselManager.getGlobalFunctionManager().call(engine, name, var, args);
		}

		@Override
		public boolean doesProvideFunction(String name) {
			if(PCmo_MinerWeaselBrain.this.doesProvideFunction(name))
				return true;
			return PCws_WeaselManager.getGlobalFunctionManager().doesProvideFunction(name);
		}
		
		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = PCmo_MinerWeaselBrain.this.getProvidedFunctionNames();
			list.addAll(PCws_WeaselManager.getGlobalFunctionManager().getProvidedFunctionNames());
			return list;
		}
		
		@Override
		public List<String> getProvidedVariableNames() {
			return PCmo_MinerWeaselBrain.this.getProvidedVariableNames();
		}
		
	}
	
	@Override
	public String getName() {
		return (String)miner.getInfo("deviceName");
	}

	@Override
	public void onOpenGui() {
		miner.setInfo("deviceNames", PCws_WeaselManager.getAllPluginNames());
		miner.setInfo("networkNames", PCws_WeaselManager.getAllNetworkNames());
		miner.setInfo("text", text);
		if(getNetwork()!=null){
			miner.setInfo("networkName", getNetwork().getName());
		}
	}
	
}
