package net.minecraft.src;

public class PC_GresVGroup extends PC_GresWidget {
	
	public PC_GresVGroup(){
		super();
	}
	
	@Override
	public int[] calcSize() {
		calcChildPositions();
		return new int[]{width, height};
	}

	@Override
	public void calcChildPositions() {
		int yy=0, ySize=0;
		for(int i=0; i<childs.size(); i++){
			childs.get(i).calcChildPositions();
			int[] size = childs.get(i).calcSize();
			if(size[0]>width||ySize + size[1]>height){
				if(size[0]>width)
					width = size[0];
				if(ySize + size[1]>height)
					height = ySize + size[1];
				if(parent!=null)
					parent.calcChildPositions();
				calcChildPositions();
				return;
			}
			ySize += size[1] + widgetDistance;
		}
		ySize -= widgetDistance;
		for(int i=0; i<childs.size(); i++){
			int[] size = childs.get(i).getSize();
			int xPos=0;
			int yPos=0;
			switch(alignerHorizontal){
				case LEFT:
					xPos = 0;
					break;
				case RIGHT:
					xPos = width - size[0];
					break;
				case CENTER:
					xPos = width/2 - size[0]/2;
					break;
				case STRETCH:
					xPos = 0;
					childs.get(i).setSize(width, childs.get(i).getSize()[1], false);
					break;
			}
			switch(alignerVertical){
				case TOP:
					yPos = yy;
					break;
				case BOTTOM:
					yPos = height - ySize + yy;
					break;
				case CENTER:
					yPos = height/2 - ySize/2 + yy;
					break;
				case STRETCH:
					yPos = yy;
					break;
			}
			childs.get(i).setPosition(xPos, yPos);
			yy += size[1] + widgetDistance;
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
