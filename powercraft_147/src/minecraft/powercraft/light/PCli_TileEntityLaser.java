package powercraft.light;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.management.PC_BeamTracer;
import powercraft.management.PC_Color;
import powercraft.management.PC_IBeamHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCli_TileEntityLaser extends PC_TileEntity implements PC_IBeamHandler, PC_ITileEntityRenderer{
	
	public static final String ACTIVE = "active", ITEMSTACK = "itemstack", ISKILLER = "isKiller", POWERED = "powered";
	
	private static PCli_ModelLaser modelLaser = new PCli_ModelLaser();
    //private boolean active = false;
    //private ItemStack itemstack;
    private PC_BeamTracer laser;
    //private boolean isKiller = false;
   // private boolean powered = false;
    private boolean active = false;
    
    public PCli_TileEntityLaser(){
    	setData(ACTIVE, false);
    	setData(ITEMSTACK, null);
    	setData(ISKILLER, false);
    	setData(POWERED, false);
    }
    
    public boolean isKiller() {
    	return (Boolean)getData(ISKILLER);
    }
    
    public void setKiller(boolean b) {
    	if(isKiller() != b){
    		setData(ISKILLER, b);
    	}
	}

    public boolean isPowered() {
    	return (Boolean)getData(POWERED);
    }
    
    public void setPowered(boolean powered) {
    	if(isPowered() != powered){
    		setData(POWERED, powered);
    	}
	}
    
	public boolean isActive()
    {
		if(PCli_ItemLaserComposition.isSensor(getItemStack()))
			return (Boolean)getData(ACTIVE);
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
    	if(!PCli_ItemLaserComposition.isSensor(getItemStack()) && !isPowered() && !isRoasterBurning())
    		return;
    	if(laser==null){
	    	laser = new PC_BeamTracer(worldObj, this);
	    	laser.setStartCoord(getCoord());
	    	int metadata = GameInfo.getMD(worldObj, xCoord, yCoord, zCoord);
	    	laser.setStartMove(metadata == 4?1:metadata == 5?-1:0, 0, metadata == 2?1:metadata == 3?-1:0);
	    	laser.setColor(PCli_ItemLaserComposition.getColorForItemStack(getItemStack()));
	    	laser.setDetectEntities(true);
	    	laser.setCanChangeColor(true);
    	}
    	laser.setStartLength(PCli_ItemLaserComposition.getLengthLimit(getItemStack(), isRoasterBurning()));
    	boolean oldActive = isActive();
    	active = false;
    	laser.flash();
    	if(PCli_ItemLaserComposition.isSensor(getItemStack())){
	    	if(oldActive != active){
	    		setData(ACTIVE, active);
	    		ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
	    	}
    	}
    }

    private boolean isRoasterBurning(){
    	boolean isBurning = false;
		if(isKiller()){
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
		return PCli_ItemLaserComposition.onBlockHit(beamTracer, block, coord, getItemStack(), isRoasterBurning());
	}

	@Override
	public boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord) {
		active = true;
		return PCli_ItemLaserComposition.onEntityHit(beamTracer, entity, coord, getItemStack(), isRoasterBurning());
	}

	public PC_ItemStack getItemStack() {
		return (PC_ItemStack)getData(ITEMSTACK);
	}
	
	public void setItemStack(PC_ItemStack itemstack) {
		setData(ITEMSTACK, itemstack);
		laser = null;
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		modelLaser.laserParts[0].showModel = modelLaser.laserParts[1].showModel = modelLaser.laserParts[2].showModel = modelLaser.laserParts[3].showModel = 
				isKiller();

		modelLaser.laserParts[7].showModel = getItemStack() != null;

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
		PC_Color color = PCli_ItemLaserComposition.getColorForItemStack(getItemStack());
		PC_Renderer.glColor4f((float)color.x, (float)color.y, (float)color.z, 1.0F);
		modelLaser.renderLens();
		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();

		PC_Renderer.glPopMatrix();
	}
	
}

