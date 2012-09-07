package net.minecraft.src;

import java.util.List;
import java.util.ArrayList;

public class PC_GresTab extends PC_GresWidget {

	PC_GresLayoutH hGroup = new PC_GresLayoutH();
	PC_GresScrollArea scroll = new PC_GresScrollArea(size.x, 0, hGroup, PC_GresScrollArea.HSCROLL);
	PC_GresWidget wMouseOver;
	PC_GresWidget active;
	
	public PC_GresTab(){
		scroll.parent = this;
	}
	
	public PC_GresWidget makeTabVisible(PC_GresWidget widget){
		for(PC_GresWidget w:childs){
			w.visible = w==widget;
		}
		for(PC_GresWidget w:hGroup.childs){
			((PC_GresButton)w).isClicked = false;
		}
		active = widget;
		return this;
	}
	
	@Override
	public PC_GresWidget add(PC_GresWidget widget){
		super.add(widget);
		hGroup.add(new PC_GresButton(widget.getText()));
		makeTabVisible(widget);
		size.setTo(calcSize());
		if(parent!=null)
			parent.calcChildPositions();
		return this;
	}
	
	@Override
	public PC_CoordI calcSize() {
		PC_CoordI s = new PC_CoordI();
		PC_CoordI ws;
		for(PC_GresWidget w:childs){
			ws = w.calcSize();
			if(s.x<ws.x+2)
				s.x=ws.x+2;
			if(s.y<ws.y+43)
				s.y=ws.y+43;
		}
		if(s.x<minSize.x)
			s.x = minSize.x;
		if(s.y<minSize.y)
			s.y = minSize.y;
		return s;
	}

	@Override
	public void calcChildPositions() {
		scroll.setPosition(0, 0);
		scroll.size.x = size.x;
		scroll.size.y = 42;
		for(PC_GresWidget w:childs)
			w.setPosition(2, 42);
	}

	
	
	@Override
	protected PC_RectI render(PC_CoordI posOffset, PC_RectI scissorOld,
			double scale) {
		for(PC_GresWidget w : hGroup.childs)
			if(w.getText()==active.getText())
				((PC_GresButton)w).isClicked = true;
		scroll.updateRenderer(posOffset.copy().add(pos), scissorOld, scale);
		
		return null;
	}

	@Override
	public MouseOver mouseOver(PC_CoordI mousePos) {
		if(mousePos.y<42)
			wMouseOver = scroll.getWidgetUnderMouse(mousePos);
		else
			wMouseOver = null;
		return MouseOver.CHILD;
	}

	@Override
	public boolean mouseClick(PC_CoordI mousePos, int key) {
		if(wMouseOver!=null){
			if(wMouseOver.mouseClick(mousePos, key))
				for(PC_GresWidget w:childs)
					if(w.getText().equals(wMouseOver.getText())){
						makeTabVisible(w);
						break;
					}
		}
		return false;
	}

	@Override
	public void mouseMove(PC_CoordI mousePos) {
		if(wMouseOver!=null)
			wMouseOver.mouseMove(mousePos);
	}

	@Override
	public void mouseWheel(int i) {
		if(wMouseOver!=null)
			wMouseOver.mouseWheel(i);
	}

	@Override
	public boolean keyTyped(char c, int key) {
		return false;
	}

	@Override
	public void addedToWidget() {
	}

}
