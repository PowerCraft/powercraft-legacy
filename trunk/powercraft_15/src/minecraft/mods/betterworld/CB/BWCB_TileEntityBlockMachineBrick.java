package mods.betterworld.CB;

import cpw.mods.fml.common.network.PacketDispatcher;
import mods.betterworld.CB.core.BWCB_3DRecipe;
import mods.betterworld.CB.core.BWCB_I3DRecipeHandler;
import mods.betterworld.CB.core.BWCB_InventoryUtils;
import mods.betterworld.CB.core.BWCB_VecI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;

public class BWCB_TileEntityBlockMachineBrick extends TileEntity {
	private int counter = 0;
	private boolean structComplete = false;
	private boolean isActive = false;
	private boolean updateSign = false;
	private boolean isBurning = false;
	private int meta = 0;
	private String[] signTextFurnace = new String[] { "", "", "", "" };
	private int[] inChest = new int[2]; // 0 = x; 1 = Z
	private int[] outChest = new int[2];
	private int[] signPos = new int[3]; // x,y,z
	private int progressCounter;
	private int inBlockID, outBlockID, outBlockDamVal, coalUse;
	private int burnTime = 0;

	
	private static BWCB_3DRecipe recipe = new BWCB_3DRecipe(null,
			new String[] { 
			"BBBBBBBBBBB", 
			"CBBBBNBBBBC", 
			"BBBBBOBBBBB" },
			new String[] { 
			" HBGBGBGBH ", 
			"           ", 
			" HBGBGBGBH " },
			new String[] { 
			"   HHHHH   ", 
			"   HH HH   ", 
			"   HHHHH   " },

			'B', Block.brick, 'C', Block.chest, 'N', Block.netherrack, 'H',
			Block.stoneSingleSlab, 4, 'G', Block.glass, 'O',
			BWCB.blockMachineBrick);
	
	public boolean checkStructure() {
		meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (meta == 2 && Block.chest.blockID==worldObj.getBlockId(xCoord +5, yCoord, zCoord +1) && Block.chest.blockID==worldObj.getBlockId(xCoord -5, yCoord, zCoord +1)) 
			{
			return structComplete = recipe.isStruct(null, worldObj,new BWCB_VecI(xCoord, yCoord, zCoord));
		}
		if (meta == 3 && Block.chest.blockID==worldObj.getBlockId(xCoord -5, yCoord, zCoord -1) && Block.chest.blockID==worldObj.getBlockId(xCoord +5, yCoord, zCoord -1)) {
			return structComplete = recipe.isStruct(null, worldObj,
					new BWCB_VecI(xCoord, yCoord, zCoord));
		}
		if (meta == 5 && Block.chest.blockID==worldObj.getBlockId(xCoord -1, yCoord, zCoord +5) && Block.chest.blockID==worldObj.getBlockId(xCoord -1, yCoord, zCoord -5)) {
			return structComplete = recipe.isStruct(null, worldObj,
					new BWCB_VecI(xCoord, yCoord, zCoord));
		}
		if (meta == 4 && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord -5) && Block.chest.blockID==worldObj.getBlockId(xCoord +1, yCoord, zCoord -5)) {
			return structComplete = recipe.isStruct(null, worldObj,
					new BWCB_VecI(xCoord, yCoord, zCoord));
		}
		return false;

	}

	public void placeSign() {
		
		meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		if (meta == 2
				&& worldObj.isAirBlock(xCoord + 1, yCoord + 1, zCoord - 1)) // South
		{
			worldObj.setBlock(xCoord + 1, yCoord + 1, zCoord - 1,
					Block.signWall.blockID, 2, 2);
			signPos[0] = xCoord + 1;
			signPos[1] = yCoord + 1;
			signPos[2] = zCoord - 1;
			signTextFurnace[0] = "Brick Furnace";
			changeSignText(1, "-> IDLE <-");
			// Set Chest cord.
			inChest[0] = xCoord + 5;
			inChest[1] = zCoord + 1;
			outChest[0] = xCoord - 5;
			outChest[1] = zCoord + 1;

		}
		if (meta == 3
				&& worldObj.isAirBlock(xCoord - 1, yCoord + 1, zCoord + 1)) // North
		{
			worldObj.setBlock(xCoord - 1, yCoord + 1, zCoord + 1,
					Block.signWall.blockID, 3, 2);
			signPos[0] = xCoord - 1;
			signPos[1] = yCoord + 1;
			signPos[2] = zCoord + 1;
			signTextFurnace[0] = "Brick Furnace";
			changeSignText(1, "-> IDLE <-");
			// Set Chest cord.
			inChest[0] = xCoord - 5;
			inChest[1] = zCoord - 1;
			outChest[0] = xCoord + 5;
			outChest[1] = zCoord - 1;

		}
		if (meta == 5
				&& worldObj.isAirBlock(xCoord + 1, yCoord + 1, zCoord + 1)) // West
		{
			worldObj.setBlock(xCoord + 1, yCoord + 1, zCoord + 1,
					Block.signWall.blockID, 5, 2);
			signPos[0] = xCoord + 1;
			signPos[1] = yCoord + 1;
			signPos[2] = zCoord + 1;
			signTextFurnace[0] = "Brick Furnace";
			changeSignText(1, "-> IDLE <-");
			// Set Chest cord.
			inChest[0] = xCoord - 1;
			inChest[1] = zCoord + 5;
			outChest[0] = xCoord - 1;
			outChest[1] = zCoord - 5;
		}
		if (meta == 4
				&& worldObj.isAirBlock(xCoord - 1, yCoord + 1, zCoord - 1)) // East
		{
			worldObj.setBlock(xCoord - 1, yCoord + 1, zCoord - 1,
					Block.signWall.blockID, 4, 2);
			signPos[0] = xCoord - 1;
			signPos[1] = yCoord + 1;
			signPos[2] = zCoord - 1;
			signTextFurnace[0] = "Brick Furnace";
			changeSignText(1, "-> IDLE <-");
			// Set Chest cord.
			inChest[0] = xCoord + 1;
			inChest[1] = zCoord - 5;
			outChest[0] = xCoord + 1;
			outChest[1] = zCoord + 5;
		}
	}

	public void removeSign()
	{
			meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			if (meta ==2 && Block.signWall.blockID == worldObj.getBlockId(xCoord +1, yCoord + 1, zCoord -1)) // South
			{   
					worldObj.destroyBlock(signPos[0],signPos[1], signPos[2],false);
					progressCounter = 0;
					coalUse = 0;
					isActive = false;
			}
			if (meta ==3 && Block.signWall.blockID == worldObj.getBlockId(xCoord - 1, yCoord + 1, zCoord +1)) // North
			{   
					worldObj.destroyBlock(signPos[0],signPos[1],signPos[2], false);
					progressCounter = 0;
					coalUse = 0;
					isActive = false;
			}
			if (meta ==5 && Block.signWall.blockID == worldObj.getBlockId(xCoord +1, yCoord +1, zCoord+1)) // West
			{   
					worldObj.destroyBlock(signPos[0],signPos[1],signPos[2],false);
					progressCounter = 0;
					coalUse = 0;
					isActive = false;
			}
			if (meta ==4 && Block.signWall.blockID == worldObj.getBlockId(xCoord -1, yCoord +1, zCoord -1)) // East
			{   
					worldObj.destroyBlock(signPos[0], signPos[1], signPos[2], false);
					progressCounter = 0;
					coalUse = 0;
					isActive = false;
			}
	}

	public void changeSignText(int i,String text) {
		signTextFurnace[i] = text;
	}

	
	 public void sendSignData() {
		 
			if(!worldObj.isRemote){
				PacketDispatcher.sendPacketToAllInDimension(new Packet130UpdateSign(signPos[0], signPos[1], signPos[2], signTextFurnace), worldObj.provider.dimensionId);
			}
			updateSign = false;
	 }


	private void progress() {
		if (checkActive() || isActive == true)
		{
		progressCounter += 1;
		}
		if(progressCounter == 1)
		{
			ItemStack coal = new ItemStack(Item.coal);
			ItemStack charcoal = new ItemStack(Item.coal, 1, 1);
			ItemStack stone = new ItemStack(Item.lightStoneDust);
			ItemStack dust = new ItemStack(Item.lightStoneDust);
			changeSignText(1, "-> ACTIVE <-");
			updateSign = true;
			isActive = true;
			if (coalUse <= 0)
			{
				if (BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), coal) > 0)
				{
					takeOutChest(coal);					
				}
				if (BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), charcoal) > 0)
				{
					takeOutChest(charcoal);					
				}
				burnTime =0;
				coalUse = 5;
				isBurning();
			}
			// block list 1.normal, 2. normal-light, 3. resistant, 4. resistant-light
			switch(inBlockID){
				
			case 1: stone = new ItemStack(Block.cobblestone);
				takeOutChest(stone);
				break;
			case 2: stone = new ItemStack(Block.cobblestone);
				takeOutChest(stone);
				takeOutChest(dust);
				break;
			case 3: stone = new ItemStack(Block.obsidian);
				takeOutChest(stone);
				break;
			case 4: stone = new ItemStack(Block.obsidian);
				takeOutChest(stone);
				takeOutChest(dust);
				break;
			}			

		}

		if(progressCounter >= 10)
		{
			//add 1 block to outChest
			ItemStack itemS = null;
			switch (outBlockID)
			{
			case 1: itemS = new ItemStack(BWCB.blockStone, 1,outBlockDamVal);
			break;
			case 2: itemS = new ItemStack(BWCB.blockStoneR, 1, outBlockDamVal);
			break;
			}
			
			IInventory inv = BWCB_InventoryUtils.getBlockInventoryAt(worldObj, outChest[0], yCoord, outChest[1]);
			int slot = BWCB_InventoryUtils.getSlotWithPlaceFor(inv, itemS);
			BWCB_InventoryUtils.storeItemStackToSlot(inv, itemS , slot);
			burnTime =0;
			progressCounter = 0;
			isActive = false;
			changeSignText(1, "-> IDLE <-");
			updateSign = true;
			coalUse -= 1;
			isBurning();
		}
		System.out.println("progress : "+progressCounter);
	}	
	
	public void isBurning()
	{
		if (coalUse >0 && isBurning == false)
		{
			switch (meta){
			case 2: worldObj.setBlock(xCoord, yCoord + 1, zCoord + 1, Block.fire.blockID, 0, 2);
				break;
			case 3: worldObj.setBlock(xCoord, yCoord + 1, zCoord - 1, Block.fire.blockID, 0, 2);
				break;
			case 5: worldObj.setBlock(xCoord -1, yCoord + 1, zCoord, Block.fire.blockID, 0, 2);
				break;
			case 4: worldObj.setBlock(xCoord +1, yCoord + 1, zCoord, Block.fire.blockID, 0, 2);
				break;
			}
			isBurning = true;		
		}
		if (coalUse <= 0 && isBurning == true)
		{
			switch (meta){
			case 2: worldObj.setBlock(xCoord, yCoord + 1, zCoord + 1, 0, 0, 2);
				break;
			case 3: worldObj.setBlock(xCoord, yCoord + 1, zCoord - 1, 0, 0, 2);
				break;
			case 5: worldObj.setBlock(xCoord -1, yCoord + 1, zCoord, 0, 0, 2);
				break;
			case 4: worldObj.setBlock(xCoord +1, yCoord + 1, zCoord, 0, 0, 2);
				break;
			}
			isBurning = false;
		}
		if (isActive == false && coalUse > 0)
		{
			burnTime += 1;
			if (burnTime ==10)
			{
				coalUse -= 1;
				burnTime = 0;
			}
		System.out.println("Coal usage: "+ coalUse);
		System.out.println("burning time: "+ burnTime);
		System.out.println("IS ACTIVE : "+ isActive);
		}
		System.out.println("IS ACTIVE : "+ isActive);
		System.out.println("IS B U R N I N G: : "+ isBurning);
	}

	public boolean checkMaterialInChest()
	{
		// block list 1.normal, 2. normal-light, 3. resistant, 4. resistant-light
		ItemStack itemS = null;
		ItemStack itemG = new ItemStack(Item.lightStoneDust);
		if(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1])==null)
			return false;
		switch(inBlockID){
			
		case 1: itemS = new ItemStack(Block.cobblestone);
			if(BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS) > 0 
					&& checkFuel())
			{
				return true;
			}
			break;
		case 2: itemS = new ItemStack(Block.cobblestone);
			if(BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS) > 0 && 
					BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemG) > 0 && 
					checkFuel())
			{
				return true;
			}
			break;
		case 3: itemS = new ItemStack(Block.obsidian);
			if(BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS) > 0 && 
					checkFuel())
			{
				return true;
			}
			break;
		case 4: itemS = new ItemStack(Block.obsidian);
			if(BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS) > 0 && 
					BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemG) > 0 && 
					checkFuel())
			{
				return true;
			}
		break;
		}
		
		return false;
	}
	
	public boolean checkOutChestFree()
	{
		// block list: 1.normal, 2.resistant
		ItemStack itemS =null;

		switch (outBlockID)
		{
		case 1: itemS = new ItemStack(BWCB.blockStone, outBlockDamVal);
		break;
		case 2: itemS = new ItemStack(BWCB.blockStoneR, outBlockDamVal);
		break;
		}

		if(BWCB_InventoryUtils.getInventorySpaceFor(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, outChest[0], yCoord, outChest[1]), itemS) >= 1)
		{
			return true;
		}
		
		else return false;
	}
	
	public void takeOutChest(ItemStack itemstack)
	{
		IInventory inv = BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]);
		int Slot;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			if (slot !=null)
			{
				if (slot.isItemEqual(itemstack))
				{
					inv.decrStackSize(i, 1);
					break;
				}
			}
		}
	}
	
	public boolean checkFuel()
	{
		ItemStack itemS1 = new ItemStack(Item.coal);
		ItemStack itemS2 = new ItemStack(Item.coal, 1, 1);
		
		if (BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS1) > 0 || BWCB_InventoryUtils.getInventoryCountOf(BWCB_InventoryUtils.getBlockInventoryAt(worldObj, inChest[0], yCoord, inChest[1]), itemS2) > 0)
		{
			return true;
		}
		if (coalUse > 0)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkActive()
	{
		
		if (checkMaterialInChest() && checkOutChestFree() && checkFuel())
		{
			return true;
		}
		return false;
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
		isBurning = nbt.getBoolean("IsBurning");
		inBlockID = nbt.getInteger("InBlockID");
		outBlockID = nbt.getInteger("OutBlockID");
		outBlockDamVal = nbt.getInteger("OutBlockDamVal");
		coalUse = nbt.getInteger("CoalUse");
		burnTime = nbt.getInteger("BurnTime");
		for (int i = 0; i < 4; ++i) {
			this.signTextFurnace[i] = nbt.getString("SignText" + (i + 1));

			if (this.signTextFurnace[i].length() > 15) {
				this.signTextFurnace[i] = this.signTextFurnace[i].substring(0,
						15);
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
		nbt.setBoolean("IsBurning", isBurning);
		nbt.setInteger("InBlockID", inBlockID);
		nbt.setInteger("OutBlockID", outBlockID);
		nbt.setInteger("OutBlockDamVal", outBlockDamVal);
		nbt.setInteger("CoalUse", coalUse);
		nbt.setInteger("BurnTime", burnTime);
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
		nbt.setBoolean("IsBurning", isBurning);
		nbt.setInteger("InBlockID", inBlockID);
		nbt.setInteger("OutBlockID", outBlockID);
		nbt.setInteger("OutBlockDamVal", outBlockDamVal);
		nbt.setInteger("CoalUse", coalUse);
		nbt.setInteger("BurnTime", burnTime);
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
		isBurning = pkt.customParam1.getBoolean("IsBurning");
		inBlockID = pkt.customParam1.getInteger("InBlockID");
		outBlockID = pkt.customParam1.getInteger("OutBlockID");
		outBlockDamVal = pkt.customParam1.getInteger("OutBlockDamVal");
		coalUse = pkt.customParam1.getInteger("CoalUse");
		burnTime = pkt.customParam1.getInteger("BurnTime");
		this.signTextFurnace[0] = pkt.customParam1.getString("SignText1");
		this.signTextFurnace[1] = pkt.customParam1.getString("SignText2");
		this.signTextFurnace[2] = pkt.customParam1.getString("SignText3");
		this.signTextFurnace[3] = pkt.customParam1.getString("SignText4");
	}

	@Override
	public void updateEntity() {
		counter += 1;
		if (counter >= 30) 
		{
			// Test variablen bitte löschen wenn GUI fertig;
			inBlockID = 1;
			outBlockID = 1;
			outBlockDamVal = 0;
			
			counter = 0;
			;
			if (checkStructure()) {
				placeSign();
				isBurning();
				sendSignData();
				if (updateSign)
				{
					sendSignData();
				}
				progress();
				
			} else {
				removeSign();
				isBurning();
			}
		}
	}


}
