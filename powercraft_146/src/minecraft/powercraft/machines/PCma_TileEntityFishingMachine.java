package powercraft.machines;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import powercraft.management.PC_Block;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCma_TileEntityFishingMachine extends PC_TileEntity implements PC_ITileEntityRenderer {

	private static PCma_ModelFishingMachine model = new PCma_ModelFishingMachine();
	private static Random rand = new Random();
	
	private long lastTime = System.currentTimeMillis();
	private int fishTimer;
	private int burningFuel;
	public boolean running;
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		fishTimer = nbtTagCompound.getInteger("fishTimer");
		burningFuel = nbtTagCompound.getInteger("burningFuel");
		running = burningFuel > 11;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("fishTimer", fishTimer);
		nbtTagCompound.setInteger("burningFuel", burningFuel);
	}

	private IInventory getChestInventory() {
		TileEntity te = GameInfo.getTE(worldObj, getCoord().offset(0, 1, 0));

		if (te instanceof IInventory) {
			return (IInventory) te;
		}
		return null;
	}
	
	private boolean checkFuel() {

		if (burningFuel > 11) {
			burningFuel -= 3;
			return true;
		} else {

			IInventory inv = getChestInventory();
			if (inv == null) {
				turnIntoBlocks();
				return false;
			}

			for (int s = 0; s < inv.getSizeInventory(); s++) {
				ItemStack stack = inv.getStackInSlot(s);
				int cost = GameInfo.getFuelValue(stack, 1.0D);
				if (cost > 0) {
					burningFuel += cost;
					if (stack.getItem().hasContainerItem()) {
						inv.setInventorySlotContents(s, new ItemStack(stack.getItem().getContainerItem(), 1, 0));
					} else {
						inv.decrStackSize(s, 1);
					}

					return true;
				}
			}

			return false;
		}
	}
	
	private void turnIntoBlocks(){
		PC_VecI pos = getCoord();
		int meta = GameInfo.getMD(worldObj, pos);
		ValueWriting.setBID(worldObj, pos, Block.planks.blockID, meta);
	}
	
	/**
	 * Catch and eject a fish to nearby covneyor or air block.
	 */
	private void catchFish() {

		PC_VecI[] outputs = {
				new PC_VecI(1, 1, 0),
				new PC_VecI(0, 1, 1),
				new PC_VecI(-1, 1, 0),
				new PC_VecI(0, 1, -1),
				new PC_VecI(2, 0, 0),
				new PC_VecI(0, 0, 2),
				new PC_VecI(-2, 0, 0),
				new PC_VecI(0, 0, -2)	
		};


		for (int i = 0; i < outputs.length; i++) {
			Block b = GameInfo.getBlock(worldObj, getCoord().offset(outputs[i]));
			if (b instanceof PC_Block && ((PC_Block)b).getModule().getName().equals("Transport")) {
				ejectFish_do(getCoord().offset(outputs[i]), false);
				return;
			}
		}

		for (int i = 0; i < outputs.length; i++) {
			if(GameInfo.getBID(worldObj, getCoord().offset(outputs[i])) == 0) {
				ejectFish_do(getCoord().offset(outputs[i]), true);
				return;
			}
		}

	}
	
	/**
	 * Create and eject fish or ink item
	 * 
	 * @param out pos of the output block
	 * @param fast set false if there's a conveyor.
	 */
	private void ejectFish_do(PC_VecI out, boolean fast) {
		if(worldObj.isRemote)
			return;
		ItemStack caught = new ItemStack(rand.nextInt(6) == 0 ? Item.dyePowder : Item.fishRaw, 1, 0);
		EntityItem entityitem = new EntityItem(worldObj, out.x + 0.5D, out.y + 0.5D, out.z + 0.5D, caught);

		if (!fast) {
			entityitem.motionX = 0;
			entityitem.motionY = 0;
			entityitem.motionZ = 0;
		}

		entityitem.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityitem);
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote)
			return;
		if(!PCma_BlockFishingMachine.isStructOK(worldObj, getCoord())){
			turnIntoBlocks();
		}
		boolean oldRunning = running;
		if(running = checkFuel()){
			if (--fishTimer <= 0) {
				fishTimer = 250 + rand.nextInt(350);
				catchFish();
			}
		}
		if(oldRunning != running){
			PC_PacketHandler.setTileEntity(this, "running", running);
		}
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	
	
	@Override
	public void setData(Object[] o) {
		int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("running"))
            {
            	running = (Boolean)o[p++];
            }
            
        }
	}
	
	@Override
	public Object[] getData() {
		return new Object[]{
				"running", running
		};
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		long currentTime = System.currentTimeMillis();
		int rotation = (int)((currentTime-lastTime)/1000.0f*360);
		PC_Renderer.glPushMatrix();
		PC_Renderer.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
		float f4 = 0.75F;
		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Machines")) + "fisher.png");
		int rota = GameInfo.getMD(worldObj, getCoord().offset(0, 1, 0));
		if (rota == 2){
			rota = 270;
        }else if (rota == 3){
        	rota = 90;
        }else if (rota == 4){
        	rota = 0;
        }else if (rota == 5){
        	rota = 180;
        }
		PC_Renderer.glRotatef(rota, 0.0F, 1.0F, 0.0F);
		PC_Renderer.glScalef(-1F, -1F, 1.0F);
		model.renderModel();

		PC_Renderer.glRotatef(180F - rotation, 0.0F, 1.0F, 0.0F);
		PC_Renderer.glScalef(-1F, -1F, 1.0F);
		model.renderScrew();

		PC_Renderer.glPopMatrix();

		PC_Renderer.glPushMatrix();
		PC_Renderer.glDisable(3553 /* GL_TEXTURE_2D */);
		PC_Renderer.glDisable(2896 /* GL_LIGHTING */);
		PC_Renderer.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);

		double diameter = 0.6D;

		for (int q = 0; q < 24; q++) {
			PC_Renderer.tessellatorStartDrawing(2);
			PC_Renderer.tessellatorSetColorOpaque_I(0x000022);

			for (double k = 0; k <= 3.1415D * 2; k += (3.1415D * 2) / 20D) {
				PC_Renderer.tessellatorAddVertex(Math.sin(k) * diameter, -q * 0.2D, Math.cos(k) * diameter);
			}

			PC_Renderer.tessellatorDraw();
		}

		PC_Renderer.tessellatorStartDrawing(1);
		for (double k = 0; k <= 3.1415 * 2; k += (3.1415D * 2) / 20D) {

			PC_Renderer.tessellatorSetColorOpaque_I(0x000022);
			PC_Renderer.tessellatorAddVertex(Math.sin(k) * diameter, 0D, Math.cos(k) * diameter);
			PC_Renderer.tessellatorAddVertex(Math.sin(k) * diameter, -23 * 0.2D, Math.cos(k) * diameter);

		}
		PC_Renderer.tessellatorDraw();

		PC_Renderer.glEnable(2896 /* GL_LIGHTING */);
		PC_Renderer.glEnable(3553 /* GL_TEXTURE_2D */);
		PC_Renderer.glPopMatrix();
	}
	
}
