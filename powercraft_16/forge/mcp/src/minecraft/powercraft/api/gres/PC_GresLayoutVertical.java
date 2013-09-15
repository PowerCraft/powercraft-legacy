package powercraft.api.gres;


import powercraft.api.PC_RectI;
import powercraft.api.PC_Vec2I;


public class PC_GresLayoutVertical implements PC_IGresLayout {

	@Override
	public PC_Vec2I getPreferredLayoutSize(PC_GresContainer container) {

		PC_Vec2I preferredSize = new PC_Vec2I(0, 0);
		for (PC_GresComponent component : container.layoutChildOrder) {
			if (component.prefSize.y == -1) {
				preferredSize.y += component.minSize.y + component.padding.y + component.padding.height;
			} else {
				preferredSize.y += component.prefSize.y + component.padding.y + component.padding.height;
			}
			int width;
			if (component.prefSize.x == -1) {
				width = component.minSize.x + component.padding.x + component.padding.width;
			} else {
				width = component.prefSize.x + component.padding.x + component.padding.width;
			}
			if (width > preferredSize.x) {
				preferredSize.x = width;
			}
		}
		return preferredSize;
	}


	@Override
	public PC_Vec2I getMinimumLayoutSize(PC_GresContainer container) {

		PC_Vec2I minimumSize = new PC_Vec2I(0, 0);
		for (PC_GresComponent component : container.layoutChildOrder) {
			int width = component.minSize.x + component.padding.x + component.padding.width;
			if (width > minimumSize.x) {
				minimumSize.x = width;
			}
			minimumSize.y += component.minSize.y + component.padding.y + component.padding.height;
		}
		return minimumSize;
	}


	@Override
	public void updateLayout(PC_GresContainer container) {

		PC_Vec2I minimumSize = getMinimumLayoutSize(container);
		PC_RectI childRect = container.getChildRect();
		int y = childRect.height / 2 - minimumSize.y / 2;
		for (PC_GresComponent component : container.layoutChildOrder) {
			int height = component.rect.height + component.padding.y + component.padding.height;
			component.putInRect(0, y, childRect.width, height);
			y += height;
		}
	}

}
