package powercraft.api.utils;

public class PC_RectF {
	public float x, y, width, height;
	
	public PC_RectF() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	public PC_RectF(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public PC_RectF averageQuantity(PC_RectF rect) {
		PC_RectF fin = new PC_RectF();
		float v1, v2;
		
		if (x > rect.x) {
			fin.x = x;
		} else {
			fin.x = rect.x;
		}
		
		if (y > rect.y) {
			fin.y = y;
		} else {
			fin.y = rect.y;
		}
		
		v1 = x + width;
		v2 = rect.x + rect.width;
		
		if (v1 > v2) {
			fin.width = v2 - fin.x;
		} else {
			fin.width = v1 - fin.x;
		}
		
		v1 = y + height;
		v2 = rect.y + rect.height;
		
		if (v1 > v2) {
			fin.height = v2 - fin.y;
		} else {
			fin.height = v1 - fin.y;
		}
		
		return fin;
	}
	
	public PC_RectF copy() {
		return new PC_RectF(x, y, width, height);
	}

	public PC_VecF interpolate(float x, float y) {
		return new PC_VecF(this.x+width*x, this.y+height*y);
	}
}
