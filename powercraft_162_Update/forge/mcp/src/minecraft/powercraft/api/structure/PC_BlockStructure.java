package powercraft.api.structure;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Structure", tileEntity=PC_TileEntityStructure.class)
public class PC_BlockStructure extends PC_Block {
	
	public static PC_BlockStructure structure;
	
	private Icon[] icons;
	private int colorMultiply=-1;
	
	public PC_BlockStructure(int id) {
		super(id, Material.rock, makeList());
		structure = this;
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	private static String[] makeList(){
		String[] list = new String[18];
		list[0] = "blank";
		list[1] = "white";
		for(int i=0; i<12; i++){
			list[i+2] = "Cable"+i;
		}
		return list;
	}
	
	@Override
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		return true;
	}
	
	@Override
	public Icon getIcon(PC_Direction dir,
			int metadata) {
		if(icons==null)
			return sideIcons[0];
		Icon icon = icons[dir.getMCDir()];
		if(icon==null)
			icon = sideIcons[0];
		return icon;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		ItemStack structure = tileEntitystructure.getStructure();
		float p=1.0f/16.0f;
		icons = new Icon[6];
		if(structure!=null){
			boolean[] canConnectTo = new boolean[6];
			boolean anyTrue = false;
			boolean allTrue = true;
			for(int i=0; i<6; i++){
				anyTrue |= canConnectTo[i] = canThisStructureConnectTo(world, x, y, z, PC_Direction.getFromMCDir(i));
				allTrue &= canConnectTo[i];
			}
			Tessellator.instance.draw();
			GL11.glDisable(GL11.GL_CULL_FACE);
			Tessellator.instance.startDrawingQuads();
			if(!allTrue){
				for(int i=0; i<6; i++){
					if(!canConnectTo[i]){
						icons[i] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.getFromMCDir(i));
					}
				}
				setBlockBounds(p*4, p*4, p*4, p*12, p*12, p*12);
				PC_Renderer.renderBlockAllFaces(renderer, this, x, y, z);
			}
			if(anyTrue){
				for(int i=0; i<6; i++){
					if(canConnectTo[i]){
						PC_VecI offset = PC_Direction.getFromMCDir(i).getOffset();
						if(offset.x==0){
							icons[PC_Direction.RIGHT.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.RIGHT);
							icons[PC_Direction.LEFT.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.LEFT);
						}else{
							icons[PC_Direction.RIGHT.getMCDir()] = null;
							icons[PC_Direction.LEFT.getMCDir()] = null;
						}
						if(offset.y==0){
							icons[PC_Direction.TOP.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.TOP);
							icons[PC_Direction.BOTTOM.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.BOTTOM);
						}else{
							icons[PC_Direction.TOP.getMCDir()] = null;
							icons[PC_Direction.BOTTOM.getMCDir()] = null;
						}
						if(offset.z==0){
							icons[PC_Direction.FRONT.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.FRONT);
							icons[PC_Direction.BACK.getMCDir()] = ((PC_ItemStructure)structure.getItem()).getIconFromSide(structure, PC_Direction.BACK);
						}else{
							icons[PC_Direction.FRONT.getMCDir()] = null;
							icons[PC_Direction.BACK.getMCDir()] = null;
						}
						setBlockBounds(clamp0To1(p*4+offset.x*8*p), clamp0To1(p*4+offset.y*8*p), clamp0To1(p*4+offset.z*8*p), 
								clamp0To1(p*12+offset.x*8*p), clamp0To1(p*12+offset.y*8*p), clamp0To1(p*12+offset.z*8*p));
						PC_Renderer.renderBlockAllFaces(renderer, this, x, y, z);
					}
				}
			}
			Tessellator.instance.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			Tessellator.instance.startDrawingQuads();
		}
		for(int i=0; i<6; i++){
			PC_Direction dir = PC_Direction.getFromMCDir(i);
			int cable=tileEntitystructure.getCable(dir);
			if(cable>0){
				PC_VecI offset = dir.getOffset();
				int on = -1;
				int onCount=0;
				for(int j=0; j<16; j++){
					if((cable & 1<<j)!=0){
						onCount++;
						if(onCount==1){
							on = j;
						}
					}
				}
				if(onCount==1){
					for(int k=0; k<6; k++){
						icons[k] = sideIcons[1];
					}
					colorMultiply = PC_ItemCable.getColor(on);
					setBlockBounds(clamp(p*7+offset.x*6*p, p, p*15), clamp(p*7+offset.y*6*p, p, p*15), clamp(p*7+offset.z*6*p, p, p*15), clamp(p*9+offset.x*6*p, p, p*15), clamp(p*9+offset.y*6*p, p, p*15), clamp(p*9+offset.z*6*p, p, p*15));
					PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
					for(int j=0; j<6; j++){
						PC_Direction dir2 = PC_Direction.getFromMCDir(j);
						int[] cables = cableConnectTo(world, x, y, z, dir, dir2, true);
						if(cables!=null&&((cables[0]|cables[1]|cables[2]|cables[3]|cables[4])&cable)!=0){
							PC_VecI offset2 = dir2.getOffset();
							float l = (cables[2]&cable)!=0?1:(cables[1]&cable)!=0||(cables[3]&cable)!=0?1+p*3:p*15;
							setBlockBounds(clamp(p*7+offset.x*6*p+(offset2.x<0?-p*10:offset2.x>0?p*2:0), 1-l, l), 
									clamp(p*7+offset.y*6*p+(offset2.y<0?-p*10:offset2.y>0?p*2:0), 1-l, l), 
									clamp(p*7+offset.z*6*p+(offset2.z<0?-p*10:offset2.z>0?p*2:0), 1-l, l), 
									clamp(p*9+offset.x*6*p+(offset2.x>0?p*10:offset2.x<0?-p*2:0), 1-l, l), 
									clamp(p*9+offset.y*6*p+(offset2.y>0?p*10:offset2.y<0?-p*2:0), 1-l, l), 
									clamp(p*9+offset.z*6*p+(offset2.z>0?p*10:offset2.z<0?-p*2:0), 1-l, l));
							PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
						}
					}
				}else if(onCount>1){
					float size = 1+onCount/16.0f;
					setBlockBounds(clamp(p*(8-size)+offset.x*6*p, p*(2-size), p*(14+size)), clamp(p*(8-size)+offset.y*6*p, p*(2-size), p*(14+size)), clamp(p*(8-size)+offset.z*6*p, p*(2-size), p*(14+size)), 
							clamp(p*(8+size)+offset.x*6*p, p*(2-size), p*(14+size)), clamp(p*(8+size)+offset.y*6*p, p*(2-size), p*(14+size)), clamp(p*(8+size)+offset.z*6*p, p*(2-size), p*(14+size)));
					
					int c=0;
					for(int s=0; s<6; s++){
						for(int t=0; t<4; t++){
							for(int k=0; k<6; k++){
								if(k==s){
									icons[k] = sideIcons[10+t];
								}else{
									icons[k] = null;
								}
							}
							while((cable & 1<<c)==0){
								c++;
								if(c>=16)
									c=0;
							}
							colorMultiply = PC_ItemCable.getColor(c);
							PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
							c++;
							if(c>=16)
								c=0;
						}
					}
					
					for(int j=0; j<6; j++){
						PC_Direction dir2 = PC_Direction.getFromMCDir(j);
						int[] cables = cableConnectTo(world, x, y, z, dir, dir2, false);
						if(cables!=null){
							int cableToDir = (cables[0]|cables[1]|cables[2]|cables[3]|cables[4])&cable;
							on = -1;
							onCount=0;
							for(int k=0; k<16; k++){
								if((cableToDir & 1<<k)!=0){
									onCount++;
									if(onCount==1){
										on = k;
									}
								}
							}
							if(onCount==1){
								for(int k=0; k<6; k++){
									icons[k] = sideIcons[1];
								}
								colorMultiply = PC_ItemCable.getColor(on);
								PC_VecI offset2 = dir2.getOffset();
								float l = (cables[2]&cable)!=0?1:(cables[1]&cable)!=0||(cables[3]&cable)!=0?1+p*3:p*15;
								setBlockBounds(clamp(p*7+offset.x*6*p+(offset2.x<0?-p*10:0), 1-l, l), clamp(p*7+offset.y*6*p+(offset2.y<0?-p*10:0), 1-l, l), clamp(p*7+offset.z*6*p+(offset2.z<0?-p*10:0), 1-l, l), 
										clamp(p*9+offset.x*6*p+(offset2.x>0?p*10:0), 1-l, l), clamp(p*9+offset.y*6*p+(offset2.y>0?p*10:0), 1-l, l), clamp(p*9+offset.z*6*p+(offset2.z>0?p*10:0), 1-l, l));
								PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
							}else if(onCount>1){
								size = 1+onCount/16.0f;
								PC_VecI offset2 = dir2.getOffset();
								float l = (cables[2]&cable)!=0?1:(cables[1]&cable)!=0||(cables[3]&cable)!=0?1+p*(2+size):p*(14+size);
								setBlockBounds(clamp(p*(8-size)+offset.x*6*p+(offset2.x<0?-p*10:offset2.x>0?p*(size*2):0), 1-l, l), 
										clamp(p*(8-size)+offset.y*6*p+(offset2.y<0?-p*10:offset2.y>0?p*(size*2):0), 1-l, l), 
										clamp(p*(8-size)+offset.z*6*p+(offset2.z<0?-p*10:offset2.z>0?p*(size*2):0), 1-l, l), 
										clamp(p*(8+size)+offset.x*6*p+(offset2.x>0?p*10:offset2.x<0?-p*(size*2):0), 1-l, l), 
										clamp(p*(8+size)+offset.y*6*p+(offset2.y>0?p*10:offset2.y<0?-p*(size*2):0), 1-l, l), 
										clamp(p*(8+size)+offset.z*6*p+(offset2.z>0?p*10:offset2.z<0?-p*(size*2):0), 1-l, l));
								
								c=0;
								for(int s=0; s<6; s++){
									if(s==dir2.getMCDir()||s==dir2.mirror().getMCDir())
										continue;
									for(int t=0; t<4; t++){
										for(int k=0; k<6; k++){
											if(k==s){
												boolean rot = false;
												if(k==0||k==1){
													rot = dir2==PC_Direction.FRONT||dir2==PC_Direction.BACK;
												}else {
													rot = dir2==PC_Direction.TOP||dir2==PC_Direction.BOTTOM;
												}
												icons[k] = sideIcons[2+t+(rot?4:0)];
											}else{
												icons[k] = null;
											}
										}
										while((cableToDir & 1<<c)==0){
											c++;
											if(c>=16)
												c=0;
										}
										colorMultiply = PC_ItemCable.getColor(c);
										PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
										c++;
										if(c>=16)
											c=0;
									}
								}
								
							}
						}
					}
				}
			}
		}
		colorMultiply = -1;
		icons = null;
		setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}
	
	public static boolean isStructure(IBlockAccess world, int x, int y, int z){
		if(PC_Utils.getBlock(world, x, y, z)!=structure)
			return false;
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		ItemStack structure = tileEntitystructure.getStructure();
		return structure!=null;
	}
	
	private float clamp0To1(float f){
		return clamp(f, 0, 1);
	}
	
	public int[] cableConnectTo(IBlockAccess world, int x, int y, int z, PC_Direction dir, PC_Direction dir2, boolean redstone){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(dir==dir2||dir==dir2.mirror())
			return null;
		int[] cables = new int[5];
		cables[0] = tileEntitystructure.getCable(dir2);
		PC_VecI offset = dir2.getOffset();
		int x1=x+offset.x, y1=y+offset.y, z1=z+offset.z;
		PC_TileEntityStructure tileEntitystructureOther;
		if(PC_Utils.getBlock(world, x1, y1, z1)==this){
			tileEntitystructureOther = PC_Utils.getTE(world, x1, y1, z1);
			cables[1] = tileEntitystructureOther.getCable(dir2.mirror());
			cables[2] = tileEntitystructureOther.getCable(dir);
		}else if(redstone){
			if(dir2.mirror()==PC_Direction.BOTTOM){
				cables[1] = BlockRedstoneWire.isPowerProviderOrWire(world, x1, y1, z1, dir.getMCDir())?-1:0;
			}else if(dir==PC_Direction.BOTTOM){
				cables[2] = BlockRedstoneWire.isPowerProviderOrWire(world, x1, y1, z1, dir2.mirror().getMCDir())?-1:0;
			}
		}
		offset = dir.getOffset();
		x += offset.x;
		y += offset.y;
		z += offset.z;
		if(PC_Utils.getBlock(world, x1, y1, z1)==this||PC_Utils.getBlock(world, x1, y1, z1)==null){
			x1 += offset.x;
			y1 += offset.y;
			z1 += offset.z;
			if(PC_Utils.getBlock(world, x1, y1, z1)==this){
				tileEntitystructureOther = PC_Utils.getTE(world, x1, y1, z1);
				cables[3] = tileEntitystructureOther.getCable(dir2.mirror());
			}
		}
		if(PC_Utils.getBlock(world, x, y, z)==this){
			tileEntitystructureOther = PC_Utils.getTE(world, x, y, z);
			cables[4] = tileEntitystructureOther.getCable(dir2);
		}
		return cables;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
		return colorMultiply;
	}

	private float clamp(float f, float min, float max){
		if(f>max)
			return max;
		if(f<min)
			return min;
		return f;
	}
	
	public boolean canThisStructureConnectTo(IBlockAccess world, int x, int y, int z, PC_Direction dir){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		ItemStack structure = tileEntitystructure.getStructure();
		if(structure==null)
			return false;
		if(tileEntitystructure.getCable(dir)>0)
			return false;
		return ((PC_ItemStructure)structure.getItem()).canStructureConnectTo(world, x, y, z, structure, dir);
	}
	
	@Override
	public boolean canStructureConnectTo(IBlockAccess world, int x, int y, int z, ItemStack structure, PC_Direction dir){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getCable(dir)>0)
			return false;
		ItemStack thisstructure = tileEntitystructure.getStructure();
		return PC_ItemStructure.areStructuresCompatible(structure, thisstructure);
	}

	public boolean setStructure(World world, int x, int y, int z, ItemStack structure) {
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getStructure()==null){
			tileEntitystructure.setStructure(structure);
			return true;
		}
		return false;
	}
	
	public boolean setCable(World world, int x, int y, int z, PC_Direction dir, int cableID) {
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		int cables = tileEntitystructure.getCable(dir);
		if((cables&1<<cableID)==0){
			if(tileEntitystructure.getStructure()==null){
				//dir = dir.mirror();
				PC_VecI offset = dir.getOffset();
				Block block = PC_Utils.getBlock(world, x+offset.x, y+offset.y, z+offset.z);
				if(block==null || !block.isOpaqueCube()){
					update(world, x, y, z);
					return false;
				}
			}
			tileEntitystructure.setCable(dir, cableID, true);
			return true;
		}
		update(world, x, y, z);
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		update(world, x, y, z);
		super.onNeighborBlockChange(world, x, y, z, id);
	}
	
	public void update(World world, int x, int y, int z){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getStructure()==null){
			boolean anyCables = false;
			for(int i=0; i<6; i++){
				PC_Direction dir = PC_Direction.getFromMCDir(i);
				PC_VecI offset = dir.getOffset();
				Block block = PC_Utils.getBlock(world, x+offset.x, y+offset.y, z+offset.z);
				int cable = tileEntitystructure.getCable(dir);
				if(block==null || !block.isOpaqueCube()){
					for(int j=0; j<16; j++){
						if((cable&1<<j)!=0){
							PC_Utils.dropItemStack(world, x, y, z, new ItemStack(PC_ItemCable.cable, 1, j));
							tileEntitystructure.setCable(dir, j, false);
						}
					}
					cable = 0;
				}
				anyCables |= cable!=0;
			}
			if(!anyCables){
				PC_Utils.setBID(world, x, y, z, 0);
			}
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void onIconLoading() {
		for(int i=0; i<Item.itemsList.length; i++){
			Item item = Item.itemsList[i];
			if(item instanceof PC_ItemStructure){
				((PC_ItemStructure)item).onIconLoading();
			}
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		float f=1.0f/16.0f;
		if(te instanceof PC_TileEntityStructure){
			if(((PC_TileEntityStructure) te).getStructure()!=null){
				float minX = canThisStructureConnectTo(world, x, y, z, PC_Direction.RIGHT) ? 0 : f*4;
				float minY = canThisStructureConnectTo(world, x, y, z, PC_Direction.BOTTOM) ? 0 : f*4;
				float minZ = canThisStructureConnectTo(world, x, y, z, PC_Direction.BACK) ? 0 : f*4;
				float maxX = canThisStructureConnectTo(world, x, y, z, PC_Direction.LEFT) ? 1 : f*12;
				float maxY = canThisStructureConnectTo(world, x, y, z, PC_Direction.TOP) ? 1 : f*12;
				float maxZ = canThisStructureConnectTo(world, x, y, z, PC_Direction.FRONT) ? 1 : f*12;
				return AxisAlignedBB.getAABBPool().getAABB(x+minX, y+minY, z+minZ, x+maxX, y+maxY, z+maxZ);
			}
			return null;
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if(te instanceof PC_TileEntityStructure){
			PC_TileEntityStructure structure = (PC_TileEntityStructure)te;
			float f=1.0f/16.0f;
			if(structure.getStructure()==null){
				PC_Direction dir[] = {PC_Direction.BOTTOM, PC_Direction.TOP, PC_Direction.FRONT, PC_Direction.BACK, PC_Direction.RIGHT, PC_Direction.LEFT};
				for(int i=0; i<6; i++){
					if(structure.getCable(dir[i])!=0){
						PC_VecI offset = dir[i].getOffset();
						setBlockBounds(clamp0To1(offset.x*f*12), clamp0To1(offset.y*f*12), clamp0To1(offset.z*f*12), clamp0To1(offset.x*f*12+1), clamp0To1(offset.y*f*12+1), clamp0To1(offset.z*f*12+1));
						break;
					}
				}
			}else{
				float minX = canThisStructureConnectTo(world, x, y, z, PC_Direction.RIGHT) ? 0 : f*4;
				float minY = canThisStructureConnectTo(world, x, y, z, PC_Direction.BOTTOM) ? 0 : f*4;
				float minZ = canThisStructureConnectTo(world, x, y, z, PC_Direction.BACK) ? 0 : f*4;
				float maxX = canThisStructureConnectTo(world, x, y, z, PC_Direction.LEFT) ? 1 : f*12;
				float maxY = canThisStructureConnectTo(world, x, y, z, PC_Direction.TOP) ? 1 : f*12;
				float maxZ = canThisStructureConnectTo(world, x, y, z, PC_Direction.FRONT) ? 1 : f*12;
				setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
			}
		}else{
			setBlockBounds(0, 0, 0, 1, 1, 1);
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side){
		if(side==-1)
			return false;
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		int cable = tileEntitystructure.getCable(PC_Direction.BOTTOM);
		int onCount=0;
		for(int j=0; j<16; j++){
			if((cable & 1<<j)!=0){
				onCount++;
			}
		}
		return onCount==1;
	}

	public void setBlockBloundsForStructure(IBlockAccess world, int x, int y, int z) {
		float f=1.0f/16.0f;
		setBlockBounds(f*4, f*4, f*4, f*12, f*12, f*12);
	}
	
	public void setBlockBloundsForCable(IBlockAccess world, int x, int y, int z) {
		setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int dir, float xHit, float yHit, float zHit) {
		if(entityPlayer.getCurrentEquippedItem()!=null && entityPlayer.getCurrentEquippedItem().getItem()==PC_ItemCable.cable)
			return false;
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		int cable = tileEntitystructure.getCable(pcDir);
		if(cable!=0){
			for(int j=0; j<16; j++){
				if((cable&1<<j)!=0){
					if(!PC_Utils.isCreative(entityPlayer)){
						PC_Utils.dropItemStack(world, x, y, z, new ItemStack(PC_ItemCable.cable, 1, j));
					}
					tileEntitystructure.setCable(pcDir, j, false);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer entityPlayer) {
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getStructure()!=null){
			if(!PC_Utils.isCreative(entityPlayer)){
				PC_Utils.dropItemStack(world, x, y, z, tileEntitystructure.getStructure());
			}
			tileEntitystructure.setStructure(null);
		}
		for(int i=0; i<6; i++){
			PC_Direction dir = PC_Direction.getFromMCDir(i);
			int cable = tileEntitystructure.getCable(dir);
			if(cable!=0){
				for(int j=0; j<16; j++){
					if((cable&1<<j)!=0){
						if(!PC_Utils.isCreative(entityPlayer)){
							PC_Utils.dropItemStack(world, x, y, z, new ItemStack(PC_ItemCable.cable, 1, j));
						}
						tileEntitystructure.setCable(dir, j, false);
					}
				}
			}
		}
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return 0;
	}
	
	@Override
	public int idPicked(World world, int x, int y, int z){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getStructure()!=null){
			return tileEntitystructure.getStructure().itemID;
		}
		return 0;
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z){
		PC_TileEntityStructure tileEntitystructure = PC_Utils.getTE(world, x, y, z);
		if(tileEntitystructure.getStructure()!=null){
			return tileEntitystructure.getStructure().getItemDamage();
		}
		return 0;
	}
	
}
