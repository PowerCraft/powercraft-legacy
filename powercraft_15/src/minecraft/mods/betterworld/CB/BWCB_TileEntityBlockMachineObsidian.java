package mods.betterworld.CB;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;

import mods.betterworld.CB.core.BWCB_3DRecipe;
import mods.betterworld.CB.core.BWCB_I3DRecipeHandler;
import mods.betterworld.CB.core.BWCB_InventoryUtils;
import mods.betterworld.CB.core.BWCB_VecI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class BWCB_TileEntityBlockMachineObsidian extends TileEntity implements IInventory{
	private int counter =0;
	private int progressCounter =0;
	private boolean structComplete = false;
	private boolean isActive = false;
	private boolean updateSign = false;
	private int meta =0;
	private String[] signTextFurnace = new String[]{"","","",""};

	private int[] inChest = new int[2]; // 0 = x; 1 = Z
	private int[] outChest = new int[2];
	private int[] signPos = new int[3]; // x,y,z

	private static BWCB_3DRecipe recipe = new BWCB_3DRecipe(null,
			new String[]{
			"BBB",
			"BBB",
			"BBB",
			"BBB"},
			new String[]{
			"NNN",
			"G G",
			"GOG",
			"C C"},
			new String[]{
			"NNN",
			"G G",
			"GGG",
			"   "},
			new String[]{
			"NNN",
			"   ",
			"   ",
			"   "},
			new String[]{
			"NNN",
			"   ",
			"   ",
			"   "},
			new String[]{
			"NNN",
			"G G",
			"GGG",
			"   "},
			new String[]{
			"BBB",
			"BLB",
			"BBB",
			"   "},

			'B', Block.obsidian, 'C', Block.chest, 'N', Block.netherBrick, 'L', Block.lavaStill, 'G', Block.glass, 'O', BWCB.blockMachineObsidian);
	

	public boolean checkStructure()
	{
		meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (meta ==2 && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord-1) && Block.chest.blockID==worldObj.getBlockId(xCoord -1, yCoord, zCoord-1)) // South
		{   
				return structComplete =recipe.isStruct(null, worldObj , new BWCB_VecI(xCoord, yCoord, zCoord));				
		}
		if (meta ==3 && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord+1) && Block.chest.blockID==worldObj.getBlockId(xCoord -1, yCoord, zCoord+1)) // North
		{   
				return structComplete =recipe.isStruct(null, worldObj , new BWCB_VecI(xCoord, yCoord, zCoord));	
		}
		if (meta ==5 && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord -1) && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord +1)) // West
		{   
				return structComplete =recipe.isStruct(null, worldObj , new BWCB_VecI(xCoord, yCoord, zCoord));	
		}
		if (meta ==4 && Block.chest.blockID==worldObj.getBlockId(xCoord+1, yCoord, zCoord +1) && Block.chest.blockID==worldObj.getBlockId(xCoord+1, yCoord, zCoord-1)) // East
		{   
				return structComplete =recipe.isStruct(null, worldObj , new BWCB_VecI(xCoord, yCoord, zCoord));	
		}
		else{
		return structComplete= false;
		}
	}
	
	public void placeSign()
	{
			if (meta ==2 && worldObj.isAirBlock(xCoord +1, yCoord+1, zCoord-1)) // South
			{   
				
					worldObj.setBlock(xCoord +1,yCoord + 1, zCoord -1,Block.signWall.blockID, 2, 2);
					signPos[0] = xCoord +1;
					signPos[1] = yCoord +1;
					signPos[2] = zCoord -1;
					signTextFurnace[0] = "Obsidian";
					signTextFurnace[1] = "Generator";
					changeSignText2("-> IDLE <-");
					updateSign = true;
					//Set Chest cord.
					inChest[0] = xCoord +1;
					inChest[1] = zCoord -1;
					outChest[0] = xCoord -1;
					outChest[1] = zCoord -1;
			}
			if (meta ==3 && worldObj.isAirBlock(xCoord -1, yCoord+1, zCoord+1)) // North
			{   
					worldObj.setBlock(xCoord -1,yCoord + 1, zCoord +1,Block.signWall.blockID, 3, 2);
					signPos[0] = xCoord -1;
					signPos[1] = yCoord +1;
					signPos[2] = zCoord +1;
					signTextFurnace[0] = "Obsidian";
					signTextFurnace[1] = "Generator";
					changeSignText2("-> IDLE <-");
					updateSign = true;
					inChest[0] = xCoord -1;
					inChest[1] = zCoord +1;
					outChest[0] = xCoord +1;
					outChest[1] = zCoord +1;
			}
			if (meta ==5 && worldObj.isAirBlock(xCoord +1, yCoord +1, zCoord +1)) // West
			{   
					worldObj.setBlock(xCoord+1, yCoord + 1, zCoord +1,Block.signWall.blockID, 5, 2);
					signPos[0] = xCoord +1;
					signPos[1] = yCoord +1;
					signPos[2] = zCoord +1;
					signTextFurnace[0] = "Obsidian";
					signTextFurnace[1] = "Generator";
					changeSignText2("-> IDLE <-");
					updateSign = true;
					//Set Chest cord.
					inChest[0] = xCoord +1;
					inChest[1] = zCoord +1;
					outChest[0] = xCoord +1;
					outChest[1] = zCoord -1;
			}
			if (meta ==4 && worldObj.isAirBlock(xCoord-1, yCoord+1, zCoord-1)) // East
			{   
					worldObj.setBlock(xCoord-1, yCoord + 1, zCoord -1,Block.signWall.blockID, 4, 2);
					signPos[0] = xCoord -1;
					signPos[1] = yCoord +1;
					signPos[2] = zCoord -1;
					signTextFurnace[0] = "Obsidian";
					signTextFurnace[1] = "Generator";
					changeSignText2("-> IDLE <-");
					updateSign = true;
					//Set Chest cord.
					inChest[0] = xCoord -1;
					inChest[1] = zCoord -1;
					outChest[0] = xCoord -1;
					outChest[1] = zCoord +1;

			}
	}
	public void removeSign()
	{
			meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			if (meta ==2 && Block.signWall.blockID == worldObj.getBlockId(xCoord +1, yCoord + 1, zCoord -1)) // South
			{   
					worldObj.destroyBlock(signPos[0],signPos[1], signPos[2],false);
					progressCounter = 0;
					worldObj.destroyBlock(xCoord, yCoord, zCoord +1, false);
					isActive = false;
			}
			if (meta ==3 && Block.signWall.blockID == worldObj.getBlockId(xCoord - 1, yCoord + 1, zCoord +1)) // North
			{   
					worldObj.destroyBlock(signPos[0],signPos[1],signPos[2], false);
					progressCounter = 0;
					worldObj.destroyBlock(xCoord, yCoord, zCoord -1, false);
					isActive = false;
			}
			if (meta ==5 && Block.signWall.blockID == worldObj.getBlockId(xCoord +1, yCoord +1, zCoord+1)) // West
			{   
					worldObj.destroyBlock(signPos[0],signPos[1],signPos[2],false);
					progressCounter = 0;
					worldObj.destroyBlock(xCoord -1, yCoord, zCoord, false);
					isActive = false;
			}
			if (meta ==4 && Block.signWall.blockID == worldObj.getBlockId(xCoord -1, yCoord +1, zCoord -1)) // East
			{   
					worldObj.destroyBlock(signPos[0], signPos[1], signPos[2], false);
					progressCounter = 0;
					worldObj.destroyBlock(xCoord +1, yCoord, zCoord, false);
					isActive = false;
			}
	}
	
	public void changeSignText2(String text)
	{
				signTextFurnace[2] = text;
	            updateSign = true;

    }

	private void sendSignData() {
		

		if(!worldObj.isRemote){
			PacketDispatcher.sendPacketToAllInDimension(new Packet130UpdateSign(signPos[0], signPos[1], signPos[2], signTextFurnace), worldObj.provider.dimensionId);
		}
		updateSign = false;
	}
	
	public void generateObsidian()
	{
		progressCounter += 1;
		if(progressCounter == 5)
		{
			changeSignText2("-> ACTIVE <-");
			updateSign = true;
		}
		if (progressCounter == 10)
		{
			// Place Stone
			if (meta ==2 && Block.lavaStill.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord+1)) // South
			{   			
					worldObj.setBlock(xCoord, yCoord, zCoord +1, Block.stone.blockID, 0, 2);
			}
			if (meta ==3 && Block.lavaStill.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord -1)) // North
			{   
					worldObj.setBlock(xCoord, yCoord, zCoord -1, Block.stone.blockID, 0, 2);
			}
			if (meta ==5 &&Block.lavaStill.blockID == worldObj.getBlockId(xCoord -1, yCoord, zCoord)) // West
			{   
					worldObj.setBlock(xCoord -1, yCoord, zCoord, Block.stone.blockID, 0, 2);
			}
			if (meta ==4 && Block.lavaStill.blockID == worldObj.getBlockId(xCoord +1, yCoord, zCoord)) // East
			{   
					worldObj.setBlock(xCoord +1, yCoord, zCoord, Block.stone.blockID, 0, 2);
			}
			// remove Stone from inChest
			ItemStack itemS = new ItemStack(Block.stone);
			IInventory inv = BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]);
			int Slot;
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack slot = inv.getStackInSlot(i);
				if (slot !=null)
				{
					if (slot.isItemEqual(itemS))
					{
						inv.decrStackSize(i, 1);
						break;
					}
				}
			}
		}
		if(progressCounter == 70)
		{
			// replace Stone with Obsidian
			if (meta ==2 && Block.stone.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord+1)) // South
			{   				
					worldObj.setBlock(xCoord, yCoord, zCoord +1, Block.obsidian.blockID, 0, 2);
			}
			if (meta ==3 &&Block.stone.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord -1)) // North
			{   
					worldObj.setBlock(xCoord, yCoord, zCoord -1, Block.obsidian.blockID, 0, 2);
			}
			if (meta ==5 && Block.stone.blockID == worldObj.getBlockId(xCoord -1, yCoord, zCoord)) // West
			{   
					worldObj.setBlock(xCoord -1, yCoord, zCoord, Block.obsidian.blockID, 0, 2);
			}
			if (meta ==4 && Block.stone.blockID == worldObj.getBlockId(xCoord +1, yCoord, zCoord)) // East
			{   
					worldObj.setBlock(xCoord +1, yCoord, zCoord, Block.obsidian.blockID, 0, 2);
			}		
		}
		if(progressCounter >= 90)
		{
			//replace Obsidian with Lava moving
			if (meta ==2 && Block.obsidian.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord+1)) // South
			{   				
					worldObj.destroyBlock(xCoord, yCoord, zCoord +1, false);
			}
			if (meta ==3 &&Block.obsidian.blockID == worldObj.getBlockId(xCoord, yCoord, zCoord -1)) // North
			{   
					worldObj.destroyBlock(xCoord, yCoord, zCoord -1, false);
			}
			if (meta ==5 && Block.obsidian.blockID == worldObj.getBlockId(xCoord -1, yCoord, zCoord)) // West
			{   
					worldObj.destroyBlock(xCoord -1, yCoord, zCoord, false);
			}
			if (meta ==4 && Block.obsidian.blockID == worldObj.getBlockId(xCoord +1, yCoord, zCoord)) // East
			{   
					worldObj.destroyBlock(xCoord +1, yCoord, zCoord, false);
			}
			//add 1 Obsidian to outChest
			ItemStack itemS = new ItemStack(Block.obsidian);
			IInventory inv = BWCB_InventoryUtils.getBlockInventoryAt(worldObj, outChest[0], yCoord, outChest[1]);
			int slot = BWCB_InventoryUtils.getSlotWithPlaceFor(inv, itemS);
			BWCB_InventoryUtils.storeItemStackToSlot(inv, itemS , slot);

			progressCounter = 0;
			isActive = false;
			changeSignText2("-> IDLE <-");
			updateSign = true;
		}
		System.out.println("COUNTER             : " +progressCounter);
	}
	public boolean checkAcitve()
	{
		if (checkStoneInChest() && checkOutChestFree())
		{
			isActive = true;
		}

		return isActive;
	}
	
	public boolean checkStoneInChest()
	{
		ItemStack itemS = new ItemStack(Block.stone);
		if(BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS) > 0)
		{
			return true;
		}
		else return false;
	}
	public boolean checkOutChestFree()
	{
		ItemStack itemS = new ItemStack(Block.obsidian);
		
		if(BWCB_InventoryUtils.getInventorySpaceFor(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, outChest[0], yCoord, outChest[1]), itemS) >= 1)
		{
			return true;
		}
		
		else return false;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		meta = nbt.getInteger("Metadata");
		inChest = nbt.getIntArray("InChest");
		outChest = nbt.getIntArray("OutChest");
		signPos = nbt.getIntArray("SignPos");
		progressCounter = nbt.getInteger("ProgressCounter");
		structComplete = nbt.getBoolean("StructComplete");
		isActive = nbt.getBoolean("IsActive");
		updateSign = nbt.getBoolean("UpdateSign");
		
        for (int i = 0; i < 4; ++i)
        {
            this.signTextFurnace[i] = nbt.getString("SignText" + (i + 1));

            if (this.signTextFurnace[i].length() > 15)
            {
                this.signTextFurnace[i] = this.signTextFurnace[i].substring(0, 15);
            }
        }
		
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Metadata", meta);
		nbt.setIntArray("InChest", inChest);
		nbt.setIntArray("OutChest", outChest);
		nbt.setIntArray("SignPos", signPos);
		nbt.setInteger("ProgressCounter", progressCounter);
		nbt.setBoolean("StructComplete", structComplete);
		nbt.setBoolean("IsActive", isActive);
		nbt.setBoolean("UpdateSign", updateSign);
		nbt.setString("SignText1", this.signTextFurnace[0]);
		nbt.setString("SignText2", this.signTextFurnace[1]);
		nbt.setString("SignText3", this.signTextFurnace[2]);
		nbt.setString("SignText4", this.signTextFurnace[3]);
		
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Metadata", meta);
		nbt.setIntArray("InChest", inChest);
		nbt.setIntArray("OutChest", outChest);
		nbt.setIntArray("SignPos", signPos);
		nbt.setInteger("ProgressCounter", progressCounter);
		nbt.setBoolean("StructComplete", structComplete);
		nbt.setBoolean("IsActive", isActive);
		nbt.setBoolean("UpdateSign", updateSign);	
		nbt.setString("SignText1", this.signTextFurnace[0]);
		nbt.setString("SignText2", this.signTextFurnace[1]);
		nbt.setString("SignText3", this.signTextFurnace[2]);
		nbt.setString("SignText4", this.signTextFurnace[3]);

		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		meta = pkt.customParam1.getInteger("Metadata");
		inChest = pkt.customParam1.getIntArray("InChest");
		outChest = pkt.customParam1.getIntArray("OutChest");
		signPos = pkt.customParam1.getIntArray("SignPos");
		progressCounter = pkt.customParam1.getInteger("ProgressCounter");
		structComplete = pkt.customParam1.getBoolean("StructComplete");
		isActive = pkt.customParam1.getBoolean("IsActive");
		updateSign = pkt.customParam1.getBoolean("UpdateSign");
		this.signTextFurnace[0] = pkt.customParam1.getString("SignText1");
		this.signTextFurnace[1] = pkt.customParam1.getString("SignText2");
		this.signTextFurnace[2] = pkt.customParam1.getString("SignText3");
		this.signTextFurnace[3] = pkt.customParam1.getString("SignText4");
	}


	@Override
	public void updateEntity()
	{
		 counter +=1;
		if (counter >= 30){
			counter =0;
			checkStructure();
			if(structComplete == true)
			{
				placeSign();
				sendSignData();
				
				if (updateSign)
				{
					sendSignData();
				}
				
				if(checkAcitve())
				{
				generateObsidian();
				}
			}
			else
			{
				removeSign();
			}
		}
	}
	
	
	// implemts from IInventory
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}
}
