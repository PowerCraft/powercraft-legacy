package net.minecraft.src;

public class PC_GresHGroup extends PC_GresWidget {
	
	public PC_GresHGroup(){
		super();
	}

	@Override
	public int[] calcSize() {
		calcChildPositions();
		return new int[]{width, height};
	}

	@Override
	public void calcChildPositions() {
		int xx=0, xSize=0;
		for(int i=0; i<childs.size(); i++){
			childs.get(i).calcChildPositions();
			int[] size = childs.get(i).calcSize();
			if(size[0] + xSize>width||size[1]>height){
				if(size[0] + xSize>width)
					width = size[0] + xSize;
				if(size[1]>height)
					height = size[1];
				if(parent!=null)
					parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			xSize += size[0] + widgetMargin;
			//childs.get(i).setPosition(xx, height/2 - childs.get(i).getSize()[1]/2);
			//xx += size[0] + widgetDistance;
		}
		xSize -= widgetMargin;
		for(int i=0; i<childs.size(); i++){
			int[] size = childs.get(i).getSize();
			int xPos=0;
			int yPos=0;
			switch(alignH){
				case LEFT:
					xPos = xx;
					break;
				case RIGHT:
					xPos = width - xSize + xx;
					break;
				case CENTER:
					xPos = width/2 - xSize/2 + xx;
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
					yPos = height - size[1];
					break;
				case CENTER:
					yPos = height/2 - size[1]/2;
					break;
				case STRETCH:
					yPos = 0;
					childs.get(i).setSize(childs.get(i).getSize()[0], height, false);
					break;
			}
			childs.get(i).setPosition(xPos, yPos);
			xx += size[0] + widgetMargin;
		}
	}

	@Override
	protected void render(int xOffset, int yOffset) {
	}

	@Override
	public boolean mouseOver(int x, int y) {
		return true;
	}

	@Override
	public boolean mouseClick(int x, int y, int key) {
		return false;
	}

	@Override
	public void keyTyped(char c, int key) {
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public int[] getMinSize() {
		return calcSize();
	}
	
	
	
}
