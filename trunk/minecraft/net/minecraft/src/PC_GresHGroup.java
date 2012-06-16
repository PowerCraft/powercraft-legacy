package net.minecraft.src;

public class PC_GresHGroup extends PC_GresWidget {
	
	public PC_GresHGroup(){
		super();
	}

	@Override
	public PC_CoordI calcSize() {
		calcChildPositions();
		return size.copy();
	}

	@Override
	public void calcChildPositions() {
		int xx=0, xSize=0;
		for(int i=0; i<childs.size(); i++){
			childs.get(i).calcChildPositions();
			PC_CoordI csize = childs.get(i).calcSize();
			if(csize.x + xSize>size.x||csize.y>size.y){
				if(csize.x + xSize>size.x)
					size.x = csize.x + xSize;
				if(csize.y>size.y)
					size.y = csize.y;
				if(parent!=null)
					parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			xSize += csize.x + widgetMargin;
			//childs.get(i).setPosition(xx, height/2 - childs.get(i).getSize().y/2);
			//xx += size.x + widgetDistance;
		}
		xSize -= widgetMargin;
		for(int i=0; i<childs.size(); i++){
			PC_CoordI csize = childs.get(i).getSize();
			int xPos=0;
			int yPos=0;
			switch(alignH){
				case LEFT:
					xPos = xx;
					break;
				case RIGHT:
					xPos = size.x - xSize + xx;
					break;
				case CENTER:
					xPos = size.x/2 - xSize/2 + xx;
					break;
				case STRETCH:
					xPos = 0;
					xPos = xx;
					break;
			}
			switch(alignV){
				case TOP:
					yPos = 0;
					break;
				case BOTTOM:
					yPos = size.y - csize.y;
					break;
				case CENTER:
					yPos = size.y/2 - csize.y/2;
					break;
				case STRETCH:
					yPos = 0;
					childs.get(i).setSize(childs.get(i).getSize().x, size.y, false);
					break;
			}
			childs.get(i).setPosition(xPos, yPos);
			xx += csize.x + widgetMargin;
		}
	}

	@Override
	protected void render(PC_CoordI mpos) {
	}

	@Override
	public boolean mouseOver(PC_CoordI mpos) {
		return true;
	}

	@Override
	public boolean mouseClick(PC_CoordI mpos, int key) {
		return false;
	}

	@Override
	public void keyTyped(char c, int key) {
	}

	@Override
	public void mouseMove(PC_CoordI mpos) {
	}

	@Override
	public PC_CoordI getMinSize() {
		return calcSize();
	}
	
	
	
}
