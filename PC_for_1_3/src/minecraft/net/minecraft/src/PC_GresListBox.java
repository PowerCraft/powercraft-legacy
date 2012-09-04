package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PC_GresListBox extends PC_GresWidget {

	private List<PC_GresWidget> rows = new ArrayList<PC_GresWidget>();
	
	public PC_GresListBox(int width, int height){
		super(width, height);
		canAddWidget = false;
	}
	
	public PC_GresListBox addRow(PC_GresWidget widget){
		rows.add(widget);
		return this;
	}
	
	public PC_GresListBox addAll(Collection<PC_GresWidget> widget){
		rows.addAll(widget);
		return this;
	}

	public PC_GresListBox setRow(int row, PC_GresWidget widget){
		rows.set(row, widget);
		return this;
	}
	
	public PC_GresListBox setRow(PC_GresWidget widgetOld, PC_GresWidget widget){
		for(int i=0; i<rows.size(); i++)
			if(rows.get(i)==widgetOld)
				rows.set(i, widget);
		return this;
	}
	
	public PC_GresListBox removeRow(PC_GresWidget widget){
		rows.remove(widget);
		return this;
	}
	
	public PC_GresListBox removeAll(Collection<PC_GresWidget> widget){
		rows.removeAll(widget);
		return this;
	}
	
	@Override
	public PC_CoordI calcSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void calcChildPositions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void render(PC_CoordI posOffset) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean mouseOver(PC_CoordI mousePos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheel(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyTyped(char c, int key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addedToWidget() {
		// TODO Auto-generated method stub

	}

}
