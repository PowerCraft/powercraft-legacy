package powercraft.api.renderer;

import net.minecraft.src.Icon;
import net.minecraft.src.ItemDye;
import net.minecraft.src.Tessellator;
import powercraft.api.multiblock.PC_BlockMultiblock;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Color;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_RectF;
import powercraft.api.utils.PC_VecF;
import powercraft.api.utils.PC_VecI;

public class PC_SpecialRenderer implements PC_ISpecialRenderer {

	public static class CableNode{
		public int cableMask;
		public float length;
		public boolean openEnd;
		public CableNode[] connectingNodes;
		
		public CableNode(int cableMask, float length, boolean openEnd){
			this.cableMask = cableMask;
			this.length = length;
			this.openEnd = openEnd;
			connectingNodes = new CableNode[3];
		}
		
		public void addCableNode(int dir, CableNode cableNode){
			connectingNodes[dir] = cableNode;
		}
		
	}
	
	public static class TexRec{
		public Icon icon;
		public float x;
		public float y;
		public float width;
		public float height;
		public boolean changeDir;
		
		public TexRec(Icon icon, float x, float y, float width, float height, boolean changeDir){
			this.icon = icon;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.changeDir = changeDir;
		}
		
		public PC_VecF interpolate(float x, float y) {
			return new PC_VecF(this.x+width*(changeDir?y:x), this.y+height*(changeDir?x:y));
		}
		
	}
	
	public void drawQuad(Icon icon, PC_VecF p1, PC_VecF c1, PC_VecF p2, PC_VecF c2, PC_VecF p3, PC_VecF c3, PC_VecF p4, PC_VecF c4){
		Tessellator tesselator = Tessellator.instance;
		float u1, u2, u3, u4, v1, v2, v3, v4;
		u1 = icon.getInterpolatedU(c1.x*16);
		u2 = icon.getInterpolatedU(c2.x*16);
		u3 = icon.getInterpolatedU(c3.x*16);
		u4 = icon.getInterpolatedU(c4.x*16);
		v1 = icon.getInterpolatedV(c1.y*16);
		v2 = icon.getInterpolatedV(c2.y*16);
		v3 = icon.getInterpolatedV(c3.y*16);
		v4 = icon.getInterpolatedV(c4.y*16);
		tesselator.addVertexWithUV(p1.x, p1.y, p1.z, u1, v1);
		tesselator.addVertexWithUV(p2.x, p2.y, p2.z, u2, v2);
		tesselator.addVertexWithUV(p3.x, p3.y, p3.z, u3, v3);
		tesselator.addVertexWithUV(p4.x, p4.y, p4.z, u4, v4);
	}
	
	private PC_VecI blockPos;
	private PC_Direction dir;
	private Icon[] icons;
	
	@Override
	public void drawBlock(PC_VecI blockPos, PC_Direction dir, Icon[] icons){
		this.blockPos = blockPos.copy();
		this.dir = dir;
		this.icons = icons;
	}
	
	public void drawBox(PC_VecF middle, PC_VecF thick, TexRec sideXP, TexRec sideXN, TexRec sideYP, TexRec sideYN, TexRec sideZP, TexRec sideZN){
		PC_VecF p[] = new PC_VecF[8];
		thick = thick.copy().div(2);
		float xMin = middle.x-thick.x;
		float xMax = middle.x+thick.x;
		float yMin = middle.y-thick.y;
		float yMax = middle.y+thick.y;
		float zMin = middle.z-thick.z;
		float zMax = middle.z+thick.z;
		p[0] = new PC_VecF(xMin, yMax, zMin);
		p[1] = new PC_VecF(xMin, yMax, zMax);
		p[2] = new PC_VecF(xMax, yMax, zMax);
		p[3] = new PC_VecF(xMax, yMax, zMin);
		p[4] = new PC_VecF(xMin, yMin, zMin);
		p[5] = new PC_VecF(xMin, yMin, zMax);
		p[6] = new PC_VecF(xMax, yMin, zMax);
		p[7] = new PC_VecF(xMax, yMin, zMin);
		for(int i=0; i<8; i++){
			p[i] = reaclcForDirAndPos(p[i]);
		}
		if(sideYP!=null)
			drawQuad(sideYP.icon, p[0], sideYP.interpolate(xMin, zMin), 
					p[1], sideYP.interpolate(xMin, zMax), 
					p[2], sideYP.interpolate(xMax, zMax), 
					p[3], sideYP.interpolate(xMax, zMin));
		if(sideYN!=null)
			drawQuad(sideYN.icon, p[7], sideYN.interpolate(xMax, zMin), 
					p[6], sideYN.interpolate(xMax, zMax), 
					p[5], sideYN.interpolate(xMin, zMax), 
					p[4], sideYN.interpolate(xMin, zMin));
		if(sideXP!=null)
			drawQuad(sideXP.icon, p[3], sideXP.interpolate(yMax, zMin), 
					p[2], sideXP.interpolate(yMax, zMax), 
					p[6], sideXP.interpolate(yMin, zMax), 
					p[7], sideXP.interpolate(yMin, zMin));
		if(sideXN!=null)
			drawQuad(sideXN.icon, p[1], sideXN.interpolate(yMax, zMax), 
					p[0], sideXN.interpolate(yMax, zMin), 
					p[4], sideXN.interpolate(yMin, zMin), 
					p[5], sideXN.interpolate(yMin, zMax));
		if(sideZP!=null)
			drawQuad(sideZP.icon, p[2], sideZP.interpolate(xMax, yMax), 
					p[1], sideZP.interpolate(xMin, yMax), 
					p[5], sideZP.interpolate(xMin, yMin), 
					p[6], sideZP.interpolate(xMax, yMin));
		if(sideZN!=null)
			drawQuad(sideZN.icon, p[0], sideZN.interpolate(xMin, yMax), 
					p[3], sideZN.interpolate(xMax, yMax), 
					p[7], sideZN.interpolate(xMax, yMin), 
					p[4], sideZN.interpolate(xMin, yMin));
	}
	
	@Override
	public void drawCables(int cableMask, CableNode cableNode1, CableNode cableNode2, CableNode cableNode3, CableNode cableNode4) {
		int drawMiddleType = 0;
		boolean rot = false;
		int on = 0;
		if(cableNode1!=null && cableNode1.cableMask==cableMask){
			on++;
		}
		if(cableNode2!=null && cableNode2.cableMask==cableMask){
			on++;
		}
		if(cableNode3!=null && cableNode3.cableMask==cableMask){
			on++;
		} 
		if(cableNode4!=null && cableNode4.cableMask==cableMask){
			on++;
		}
		if(on==0 || on==1){
			drawMiddleType=2;
			rot = cableNode2!=null && cableNode2.cableMask==cableMask || cableNode3!=null && cableNode3.cableMask==cableMask;
		}else if(on==2){
			if(cableNode1!=null && cableNode1.cableMask==cableMask){
				if(cableNode2!=null && cableNode2.cableMask==cableMask){
					drawMiddleType = (cableNode1.cableMask&cableMask)==(cableNode2.cableMask&cableMask)?1:0;
				}else if(cableNode3!=null && cableNode3.cableMask==cableMask){
					drawMiddleType = (cableNode1.cableMask&cableMask)==(cableNode3.cableMask&cableMask)?3:0;
				}else if(cableNode4!=null && cableNode4.cableMask==cableMask){
					drawMiddleType = (cableNode1.cableMask&cableMask)==(cableNode4.cableMask&cableMask)?2:0;
				}
			}else if(cableNode2!=null && cableNode2.cableMask==cableMask){
				if(cableNode3!=null && cableNode3.cableMask==cableMask){
					drawMiddleType = (cableNode2.cableMask&cableMask)==(cableNode3.cableMask&cableMask)?2:0;
					rot = true;
				}else if(cableNode4!=null && cableNode4.cableMask==cableMask){
					drawMiddleType = (cableNode2.cableMask&cableMask)==(cableNode4.cableMask&cableMask)?1:0;
				}
			}else if(cableNode3!=null && cableNode3.cableMask==cableMask){
				if(cableNode4!=null && cableNode4.cableMask==cableMask){
					drawMiddleType = (cableNode3.cableMask&cableMask)==(cableNode4.cableMask&cableMask)?1:0;
				}
			}
		}else{
			drawMiddleType = 0;
		}
		
		
		boolean mirror = dir == PC_Direction.TOP || dir == PC_Direction.FRONT || dir == PC_Direction.RIGHT;
		
		float halfThick = calcHalfThick(cableMask);
		float halfThickMiddle = halfThick;
		int iconIndex = coutCables(cableMask)==1?1:0;
		PC_Color color = new PC_Color(1.0f, 1.0f, 1.0f);
		if(iconIndex!=0){
			for(int i=0; i<16; i++){
				if((cableMask & 1<<i)!=0){
					color = PC_Color.fromHex(ItemDye.dyeColors[i]);
				}
			}
		}
		Tessellator.instance.setBrightness(PC_BlockMultiblock.instance.getMixedBrightnessForBlock(PC_ClientUtils.mc().theWorld, blockPos.x, blockPos.y, blockPos.z));
		Tessellator.instance.setColorOpaque_F(color.x, color.y, color.z);
		
		drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f), new PC_VecF(halfThick*2, halfThick, halfThick*2), 
				cableNode2==null || cableNode2.cableMask!=cableMask?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, !(cableNode1!=null||cableNode4!=null)):null, 
				cableNode3==null || cableNode3.cableMask!=cableMask?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, !(cableNode1!=null||cableNode4!=null)):null, 
				new TexRec(getIconSave(iconIndex), mirror && !rot?1:0, 0, mirror && !rot?-1:1, 1, rot), 
				new TexRec(getIconSave(iconIndex), mirror && !rot?1:0, 0, mirror && !rot?-1:1, 1, rot), 
				cableNode1==null || cableNode1.cableMask!=cableMask?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, cableNode2!=null||cableNode3!=null):null, 
				cableNode4==null || cableNode4.cableMask!=cableMask?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, cableNode2!=null||cableNode3!=null):null);
		
		int j=2;
		if(drawMiddleType==2 && iconIndex==0){
			for(int i=0; i<16; i++){
				if((cableMask & 1<<i)!=0){
					PC_Color cableColor = PC_Color.fromHex(ItemDye.dyeColors[i]);
					Tessellator.instance.setColorOpaque_F(cableColor.x, cableColor.y, cableColor.z);
					drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f), new PC_VecF(halfThick*2, halfThick, halfThick*2), 
							null, null, 
							new TexRec(getIconSave(j), mirror && !rot?1:0, 0, mirror && !rot?-1:1, 1, rot), 
							new TexRec(getIconSave(j), mirror && !rot?1:0, 0, mirror && !rot?-1:1, 1, rot), 
							null, null);
					j++;
				}
			}
		}
		
		if(cableNode1!=null){
			halfThick = calcHalfThick(cableMask & cableNode1.cableMask);
			iconIndex = coutCables(cableMask & cableNode1.cableMask)==1?1:0;
			color = new PC_Color(1.0f, 1.0f, 1.0f);
			if(iconIndex!=0){
				for(int i=0; i<16; i++){
					if((cableMask & cableNode1.cableMask & 1<<i)!=0){
						color = PC_Color.fromHex(ItemDye.dyeColors[i]);
					}
				}
			}
			float length = 0.5f-halfThickMiddle;
			Tessellator.instance.setColorOpaque_F(color.x, color.y, color.z);
			drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f+length/2+halfThickMiddle), new PC_VecF(halfThick*2, halfThick, length), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false), 
					new TexRec(getIconSave(iconIndex), mirror?1:0, 0, mirror?-1:1, 1, false), 
					new TexRec(getIconSave(iconIndex), mirror?1:0, 0, mirror?-1:1, 1, false), 
					cableNode1.openEnd?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false):null, null);
			
			if(iconIndex==0){
				j=2;
				for(int i=0; i<16; i++){
					if((cableMask & cableNode1.cableMask & 1<<i)!=0){
						PC_Color cableColor = PC_Color.fromHex(ItemDye.dyeColors[i]);
						Tessellator.instance.setColorOpaque_F(cableColor.x, cableColor.y, cableColor.z);
						drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f+length/2+halfThickMiddle), new PC_VecF(halfThick*2, halfThick, length), 
								null, null, 
								new TexRec(getIconSave(j), mirror?1:0, 0, mirror?-1:1, 1, false), 
								new TexRec(getIconSave(j), mirror?1:0, 0, mirror?-1:1, 1, false), 
								null, null);
						j++;
					}
				}
			}
			
		}
		
		if(cableNode2!=null){
			halfThick = calcHalfThick(cableMask & cableNode2.cableMask);
			iconIndex = coutCables(cableMask & cableNode2.cableMask)==1?1:0;
			color = new PC_Color(1.0f, 1.0f, 1.0f);
			if(iconIndex!=0){
				for(int i=0; i<16; i++){
					if((cableMask & cableNode2.cableMask & 1<<i)!=0){
						color = PC_Color.fromHex(ItemDye.dyeColors[i]);
					}
				}
			}
			float length = 0.5f-halfThickMiddle;
			Tessellator.instance.setColorOpaque_F(color.x, color.y, color.z);
			drawBox(new PC_VecF(0.5f+length/2+halfThickMiddle, halfThick/2, 0.5f), new PC_VecF(length, halfThick, halfThick*2), 
					cableNode2.openEnd?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false):null, null, 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true));
			
			if(iconIndex==0){
				j=2;
				for(int i=0; i<16; i++){
					if((cableMask & cableNode2.cableMask & 1<<i)!=0){
						PC_Color cableColor = PC_Color.fromHex(ItemDye.dyeColors[i]);
						Tessellator.instance.setColorOpaque_F(cableColor.x, cableColor.y, cableColor.z);
						drawBox(new PC_VecF(0.5f+length/2+halfThickMiddle, halfThick/2, 0.5f), new PC_VecF(length, halfThick, halfThick*2), 
								null, null, 
								new TexRec(getIconSave(j), 0, 0, 1, 1, true), 
								new TexRec(getIconSave(j), 0, 0, 1, 1, true), 
								null, null);
						j++;
					}
				}
			}
			
		}
		
		if(cableNode3!=null){
			halfThick = calcHalfThick(cableMask & cableNode3.cableMask);
			iconIndex = coutCables(cableMask & cableNode3.cableMask)==1?1:0;
			color = new PC_Color(1.0f, 1.0f, 1.0f);
			if(iconIndex!=0){
				for(int i=0; i<16; i++){
					if((cableMask & cableNode3.cableMask & 1<<i)!=0){
						color = PC_Color.fromHex(ItemDye.dyeColors[i]);
					}
				}
			}
			float length = 0.5f-halfThickMiddle;
			Tessellator.instance.setColorOpaque_F(color.x, color.y, color.z);
			drawBox(new PC_VecF(0.5f-length/2-halfThickMiddle, halfThick/2, 0.5f), new PC_VecF(length, halfThick, halfThick*2), 
					null, cableNode3.openEnd?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false):null, 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, true));
			
			if(iconIndex==0){
				j=2;
				for(int i=0; i<16; i++){
					if((cableMask & cableNode3.cableMask & 1<<i)!=0){
						PC_Color cableColor = PC_Color.fromHex(ItemDye.dyeColors[i]);
						Tessellator.instance.setColorOpaque_F(cableColor.x, cableColor.y, cableColor.z);
						drawBox(new PC_VecF(0.5f-length/2-halfThickMiddle, halfThick/2, 0.5f), new PC_VecF(length, halfThick, halfThick*2), 
								null, null, 
								new TexRec(getIconSave(j), 0, 0, 1, 1, true), 
								new TexRec(getIconSave(j), 0, 0, 1, 1, true), 
								null, null);
						j++;
					}
				}
			}
			
		}
		
		if(cableNode4!=null){
			halfThick = calcHalfThick(cableMask & cableNode4.cableMask);
			iconIndex = coutCables(cableMask & cableNode4.cableMask)==1?1:0;
			color = new PC_Color(1.0f, 1.0f, 1.0f);
			if(iconIndex!=0){
				for(int i=0; i<16; i++){
					if((cableMask & cableNode4.cableMask & 1<<i)!=0){
						color = PC_Color.fromHex(ItemDye.dyeColors[i]);
					}
				}
			}
			float length = 0.5f-halfThickMiddle;
			Tessellator.instance.setColorOpaque_F(color.x, color.y, color.z);
			drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f-length/2-halfThickMiddle), new PC_VecF(halfThick*2, halfThick, length), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false), 
					new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false), 
					new TexRec(getIconSave(iconIndex), mirror?1:0, 0, mirror?-1:1, 1, false), 
					new TexRec(getIconSave(iconIndex), mirror?1:0, 0, mirror?-1:1, 1, false), 
					null, cableNode4.openEnd?new TexRec(getIconSave(iconIndex), 0, 0, 1, 1, false):null);
			
			if(iconIndex==0){
				j=2;
				for(int i=0; i<16; i++){
					if((cableMask & cableNode4.cableMask & 1<<i)!=0){
						PC_Color cableColor = PC_Color.fromHex(ItemDye.dyeColors[i]);
						Tessellator.instance.setColorOpaque_F(cableColor.x, cableColor.y, cableColor.z);
						drawBox(new PC_VecF(0.5f, halfThick/2, 0.5f-length/2-halfThickMiddle), new PC_VecF(halfThick*2, halfThick, length), 
								null, null, 
								new TexRec(getIconSave(j), mirror?1:0, 0, mirror?-1:1, 1, false), 
								new TexRec(getIconSave(j), mirror?1:0, 0, mirror?-1:1, 1, false), 
								null, null);
						j++;
					}
				}
			}
			
		}
		
	}
	
	private int coutCables(int cableMask){
		int num = 0;
		for(int i=0; i<16; i++){
			num += (cableMask>>i)&1;
		}
		return num;
	}
	
	private float calcHalfThick(int cableMask){
		final int sizes[] = {0, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
		return sizes[coutCables(cableMask)]/16.0f;
	}
	
	private PC_VecF reaclcForDirAndPos(PC_VecF pos){
		PC_VecF f = null;
		if(dir == PC_Direction.BOTTOM){
			f = pos.copy();
		}else if(dir == PC_Direction.TOP){
			f = new PC_VecF(1-pos.x, 1-pos.y, pos.z);
		}else if(dir == PC_Direction.BACK){
			f = new PC_VecF(pos.z, pos.x, pos.y);
		}else if(dir == PC_Direction.FRONT){
			f = new PC_VecF(pos.z, 1-pos.x, 1-pos.y);
		}else if(dir == PC_Direction.LEFT){
			f = new PC_VecF(1-pos.y, pos.x, pos.z);
		}else if(dir == PC_Direction.RIGHT){
			f = new PC_VecF(pos.y, 1-pos.x, pos.z);
		}
		return f.add(blockPos);
	}
	
	private Icon getIconSave(int index){
		Icon icon = PC_ClientUtils.mc().renderEngine.getMissingIcon(0);
		if(icons!=null && icons.length>index){
			if(icons[index]!=null)
				icon = icons[index];
		}
		return icon;
	}
	
}
