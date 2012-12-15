package powercraft.light;

import java.io.IOException;

import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_BeamTracer;
import powercraft.management.PC_Block;
import powercraft.management.PC_Color;
import powercraft.management.PC_IBeamHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCli_TileEntityLaser extends PC_TileEntity implements PC_IBeamHandler, PC_ITileEntityRenderer
{
	private static PCli_ModelLaser modelLaser = new PCli_ModelLaser();
    private boolean active = false;
    private ItemStack itemstack;
    private PC_BeamTracer laser;
    private boolean isKiller = false;
    private boolean powered = false;
    
    public boolean isKiller()
    {
        return isKiller;
    }
    
    public void setKiller(boolean b) {
    	if(isKiller != b){
	    	isKiller = b;
	    	PC_PacketHandler.setTileEntity(this, "isKiller", isKiller);
    	}
	}

    public void setPowerd(boolean powered) {
    	if(this.powered != powered){
    		this.powered = powered;
    		PC_PacketHandler.setTileEntity(this, "powered", powered);
    	}
	}
    
	public boolean isActive()
    {
		if(PCli_ItemLaserComposition.isSensor(itemstack))
			return active;
		return false;
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void updateEntity()
    {
    	if(!PCli_ItemLaserComposition.isSensor(itemstack) && !powered && !isRoasterBurning())
    		return;
    	if(laser==null){
	    	laser = new PC_BeamTracer(worldObj, this);
	    	laser.setStartCoord(getCoord());
	    	int metadata = GameInfo.getMD(worldObj, xCoord, yCoord, zCoord);
	    	laser.setStartMove(metadata == 4?1:metadata == 5?-1:0, 0, metadata == 2?1:metadata == 3?-1:0);
	    	laser.setColor(PCli_ItemLaserComposition.getColorForItemStack(itemstack));
	    	laser.setDetectEntities(true);
    	}
    	laser.setStartLength(PCli_ItemLaserComposition.getLengthLimit(itemstack, isRoasterBurning()));
    	boolean oldActive = active;
    	active = false;
    	laser.flash();
    	if(PCli_ItemLaserComposition.isSensor(itemstack)){
	    	if(oldActive != active){
	    		PC_PacketHandler.setTileEntity(this, "on", active);
	    		ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
	    	}
    	}
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        active = nbttagcompound.getBoolean("on");
        isKiller = nbttagcompound.getBoolean("isKiller");
        powered = nbttagcompound.getBoolean("powered");
        itemstack = null;
        if(nbttagcompound.hasKey("itemstack")){
        	itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("itemstack"));
        	laser = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("on", active);
        nbttagcompound.setBoolean("isKiller", isKiller);
        nbttagcompound.setBoolean("powered", powered);
        if(itemstack!=null){
	        NBTTagCompound nbttag = new NBTTagCompound();
	        itemstack.writeToNBT(nbttag);
	        nbttagcompound.setCompoundTag("itemstack", nbttag);
        }
    }

    private boolean isRoasterBurning(){
    	boolean isBurning = false;
		if(isKiller){
			Block b = GameInfo.getBlock(worldObj, xCoord, yCoord-1, zCoord);
			if(b instanceof PC_IMSG){
				Object o = ((PC_IMSG)b).msg(PC_Utils.MSG_STR_MSG, worldObj, getCoord().offset(0, -1, 0), "isBurning");
				if(o instanceof Boolean)
					isBurning = (Boolean)o;
			}
		}
		return isBurning;
    }
    
	@Override
	public boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_VecI coord) {
		return PCli_ItemLaserComposition.onBlockHit(beamTracer, block, coord, itemstack, isRoasterBurning());
	}

	@Override
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord) {
		active = true;
		return PCli_ItemLaserComposition.onEntityHit(beamTracer, entity, coord, itemstack, isRoasterBurning());
	}

	public ItemStack getItemStack() {
		return itemstack;
	}
	
	public void setItemStack(ItemStack itemstack) {
		this.itemstack = itemstack;
		laser = null;
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("nbt"))
            {
            	try {
					readFromNBT(CompressedStreamTools.decompress((byte[])o[p++]));
				} catch (IOException e) {
					e.printStackTrace();
				}
            }else if(var.equals("isKiller")){
            	isKiller = (Boolean)o[p++];
            }else if(var.equals("on")){
            	active = (Boolean)o[p++];
            }else if(var.equals("powered")){
            	powered = (Boolean)o[p++];
            }
   
        }
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public Object[] getData() {
		NBTTagCompound nbt = new NBTTagCompound("PCPacket");
		writeToNBT(nbt);
		try {
			return new Object[]{
				"nbt", CompressedStreamTools.compress(nbt),
				"isKiller", isKiller,
				"on", active,
				"powered", powered
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		modelLaser.laserParts[0].showModel = modelLaser.laserParts[1].showModel = modelLaser.laserParts[2].showModel = modelLaser.laserParts[3].showModel = 
				isKiller();

		modelLaser.laserParts[7].showModel = itemstack != null;

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y + 0.5F /* *f0 */, (float) z + 0.5F);

		int[] meta2angle = { 0, 0, 90, 270, 0, 180 };

		float f1 = meta2angle[getBlockMetadata()];

		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Light")) + "laser.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		PC_Renderer.glScalef(f, -f, -f);
		modelLaser.renderLaser();
		PC_Color color = PCli_ItemLaserComposition.getColorForItemStack(itemstack);
		PC_Renderer.glColor4f((float)color.x, (float)color.y, (float)color.z, 1.0F);
		modelLaser.renderLens();
		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();

		PC_Renderer.glPopMatrix();
	}
	
}

