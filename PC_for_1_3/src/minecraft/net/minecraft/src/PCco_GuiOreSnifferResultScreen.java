package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Crafting Tool GRES GUI
 * 
 * @author MightyPork & XOR19
 * @copy (c) 2012
 */
public class PCco_GuiOreSnifferResultScreen implements PC_IGresBase {

	private PC_GresProgressBar slider;
	private EntityPlayer player;
	private PC_GresInventory inv;
	private PC_CoordI vector;
	private PC_CoordI[][] startpos;
	private World world;
	private PC_CoordI start;
	private List<Slot> lSlot = new ArrayList<Slot>();
	
	private static final int range = 16;

	private void rotateRight() {
		PC_CoordI swap = startpos[0][0];
		startpos[0][0] = startpos[0][1];
		startpos[0][1] = startpos[0][2];
		startpos[0][2] = startpos[1][2];
		startpos[1][2] = startpos[2][2];
		startpos[2][2] = startpos[2][1];
		startpos[2][1] = startpos[2][0];
		startpos[2][0] = startpos[1][0];
		startpos[1][0] = swap;
	}

	/**
	 * @param player the player
	 * @param world the world
	 * @param start starting block (start of sniffing)
	 * @param vector (sniffing direction)
	 */
	public PCco_GuiOreSnifferResultScreen(EntityPlayer player, TileEntity te) {
		this.player = player;
		this.startpos = new PC_CoordI[3][3];
		this.vector = new PC_CoordI(1, 0, 0);
		this.world = te.worldObj;
		this.start = new PC_CoordI(te.xCoord, te.yCoord, te.zCoord);
		
		int l = MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (vector.equals(new PC_CoordI(0, -1, 0))) {

			this.startpos[0][0] = start.offset(-1, 0, -1);
			this.startpos[1][0] = start.offset(0, 0, -1);
			this.startpos[2][0] = start.offset(1, 0, -1);
			this.startpos[0][1] = start.offset(-1, 0, 0);
			this.startpos[1][1] = start;
			this.startpos[2][1] = start.offset(1, 0, 0);
			this.startpos[0][2] = start.offset(-1, 0, 1);
			this.startpos[1][2] = start.offset(0, 0, 1);
			this.startpos[2][2] = start.offset(1, 0, 1);

			l = 3 - l;
			l += 3;
			for (int i = 0; i < l; i++) {
				rotateRight();
				rotateRight();
			}


		} else if (vector.equals(new PC_CoordI(1, 0, 0))) {
			this.startpos[0][0] = start.offset(0, 1, -1);
			this.startpos[1][0] = start.offset(0, 1, 0);
			this.startpos[2][0] = start.offset(0, 1, 1);
			this.startpos[0][1] = start.offset(0, 0, -1);
			this.startpos[1][1] = start;
			this.startpos[2][1] = start.offset(0, 0, 1);
			this.startpos[0][2] = start.offset(0, -1, -1);
			this.startpos[1][2] = start.offset(0, -1, 0);
			this.startpos[2][2] = start.offset(0, -1, 1);
		} else if (vector.equals(new PC_CoordI(0, 0, -1))) {
			this.startpos[0][0] = start.offset(-1, 1, 0);
			this.startpos[1][0] = start.offset(0, 1, 0);
			this.startpos[2][0] = start.offset(1, 1, 0);
			this.startpos[0][1] = start.offset(-1, 0, 0);
			this.startpos[1][1] = start;
			this.startpos[2][1] = start.offset(1, 0, 0);
			this.startpos[0][2] = start.offset(-1, -1, 0);
			this.startpos[1][2] = start.offset(0, -1, 0);
			this.startpos[2][2] = start.offset(1, -1, 0);


		} else if (vector.equals(new PC_CoordI(0, 1, 0))) {
			this.startpos[0][2] = start.offset(-1, 0, -1);
			this.startpos[1][2] = start.offset(0, 0, -1);
			this.startpos[2][2] = start.offset(1, 0, -1);
			this.startpos[0][1] = start.offset(-1, 0, 0);
			this.startpos[1][1] = start;
			this.startpos[2][1] = start.offset(1, 0, 0);
			this.startpos[0][0] = start.offset(-1, 0, 1);
			this.startpos[1][0] = start.offset(0, 0, 1);
			this.startpos[2][0] = start.offset(1, 0, 1);

			l += 2;
			for (int i = 0; i < l; i++) {
				rotateRight();
				rotateRight();
			}

		} else if (vector.equals(new PC_CoordI(-1, 0, 0))) {
			this.startpos[2][0] = start.offset(0, 1, -1);
			this.startpos[1][0] = start.offset(0, 1, 0);
			this.startpos[0][0] = start.offset(0, 1, 1);
			this.startpos[2][1] = start.offset(0, 0, -1);
			this.startpos[1][1] = start;
			this.startpos[0][1] = start.offset(0, 0, 1);
			this.startpos[2][2] = start.offset(0, -1, -1);
			this.startpos[1][2] = start.offset(0, -1, 0);
			this.startpos[0][2] = start.offset(0, -1, 1);
		} else if (vector.equals(new PC_CoordI(0, 0, 1))) {
			this.startpos[2][0] = start.offset(-1, 1, 0);
			this.startpos[1][0] = start.offset(0, 1, 0);
			this.startpos[0][0] = start.offset(1, 1, 0);
			this.startpos[2][1] = start.offset(-1, 0, 0);
			this.startpos[1][1] = start;
			this.startpos[0][1] = start.offset(1, 0, 0);
			this.startpos[2][2] = start.offset(-1, -1, 0);
			this.startpos[1][2] = start.offset(0, -1, 0);
			this.startpos[0][2] = start.offset(1, -1, 0);
		}
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("item.PCcoOreSnifferItem.name"));
		w.padding.setTo(10, 10);
		w.gapUnderTitle = 15;

		w.setAlignH(PC_GresAlign.CENTER);
		w.setAlignV(PC_GresAlign.CENTER);

		PC_GresWidget vg;

		vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);

		vg.add(new PC_GresLabel(PC_Lang.tr("pc.sniffer.distance")));

		vg.add(slider = new PC_GresProgressBar(0x9900ff, 150));

		slider.configureLabel("", "" + (range - 1), range - 1);
		slider.setLabelOffset(1);
		slider.setFraction(0);
		slider.setEditable(true);
		w.add(vg);

		w.add(inv = new PC_GresInventory(3, 3));
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				inv.setSlot(lSlot.get(x+y*3), x, y);
			}
		}
		//w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);

		loadBlocksForDistance(0);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		player.inventory.closeChest();
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == slider) {
			int distance = slider.getNumber() - 1;
			loadBlocksForDistance(distance);
		}
	}

	private void loadBlocksForDistance(int distance) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				ItemStack stack = null;

				PC_CoordI pos = startpos[x][y].offset(vector.multiply(distance));

				int id = pos.getId(world);
				int meta = pos.getMeta(world);

				if (id != 0 && Block.blocksList[id] != null) {
					stack = new ItemStack(id, 1, meta);
				}

				((PC_SlotNoPickup) inv.getSlot(x, y)).setBackgroundStack(stack);
			}
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {

		if (start.distanceTo(new PC_CoordI(Math.round(player.posX), Math.round(player.posY), Math.round(player.posZ))) > 8) gui.close();

	}

	@Override
	public List<Slot> getAllSlots(Container c) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				lSlot.add(new PC_SlotNoPickup());
			}
		}
		return lSlot;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}

}
