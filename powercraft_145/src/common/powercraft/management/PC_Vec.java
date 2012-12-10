package powercraft.management;

public interface PC_Vec<t extends Number, ret extends PC_Vec> {

	public t getX();
	public t getY();
	public t getZ();
	
	public ret setX(t x);
	public ret setY(t y);
	public ret setZ(t z);
	
	public ret setTo(PC_Vec vec);
	public ret setTo(t x, t y, t z);
	
	public ret add(PC_Vec vec);
	public ret add(t n);
	public ret add(t x, t y, t z);
	
	public ret offset(PC_Vec vec);
	public ret offset(t n);
	public ret offset(t x, t y, t z);
	
	public ret sub(PC_Vec vec);
	public ret sub(t n);
	public ret sub(t x, t y, t z);
	
	public ret mul(PC_Vec vec);
	public ret mul(t n);
	public ret mul(t x, t y, t z);

	public ret div(PC_Vec vec);
	public ret div(t n);
	public ret div(t x, t y, t z);
	
	public double length();
	
	public ret normalize();
	
	public ret clamp(PC_Vec min, PC_Vec max);
	public ret clamp(PC_Vec min, t max);
	public ret clamp(t min, PC_Vec max);
	public ret clamp(t min, t max);
	
	public ret max(PC_Vec max);
	public ret max(t max);
	
	public ret min(PC_Vec min);
	public ret min(t min);
	
	public ret copy();
	
}
