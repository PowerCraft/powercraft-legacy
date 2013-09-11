package powercraft.transport.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Hashtable;

import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3;


public class PC_EntityDictionary
{
	protected static Hashtable<Integer, PC_Vec3> entityDictionary;
	public PC_EntityDictionary()
	{
		entityDictionary = new Hashtable<Integer, PC_Vec3>();
	}
	
	public static int GetSize()
	{
		return entityDictionary.size();
	}
	
	/**
	 * Checks dictionary for EntityID
	 * @param eid Entity ID to check
	 * @return true if ID exists
	 */
	public static boolean HasEntityID(int eid)
	{
		if ((Integer)eid == null)
		{
			return false;			
		}
		return entityDictionary.containsKey(eid);
	}
	/**
	 * Returns PC_Vec object 
	 * @param eid ID of object to return
	 * @return PC_Vec object, null if ID does not exist in dictionary.
	 */
	public static PC_Vec3 GetMotionForID(int eid)
	{
		if (HasEntityID(eid))
		{
			return entityDictionary.get(eid);
		}
		else
		{
			return null;
		}
	}
	/**
	 * Add item into dictionary
	 * @param eid New EntityID to add
	 * @param evalues New Values to add
	 */
	public static void AddEntityValues(int eid, PC_Vec3 evalues)
	{
		if (!HasEntityID(eid))
		{
			entityDictionary.put(eid, evalues);
		}
	}
	/**
	 * Updates dictionary values with new motion data
	 * @param eid Entity ID to update
	 * @param evalues New motion values to use
	 */
	public static void UpdateEntityValues(int eid, PC_Vec3 evalues, boolean ignorey)
	{
		if (HasEntityID(eid))
		{
			entityDictionary.get(eid).x = evalues.x;
			if (!ignorey) entityDictionary.get(eid).y = evalues.y;
			entityDictionary.get(eid).z = evalues.z;
		}		
	}
	/**
	 * Returns true if motion in direction is greater than motion in dictionary.
	 * @param eid EntityID to check
	 * @param evalues New PC_Vec3 values 
	 * @param edirection PC_Direction to check in
	 * @return Boolean True if specified direction is greater than stored value.
	 */
	public static boolean NewMotionGreater(int eid, PC_Vec3 evalues, PC_Direction edirection)
	{		
		if (HasEntityID(eid))
		{
			PC_Vec3 vec = entityDictionary.get(eid);
			// check rounded values			
//			vec.y = BigDecimal.valueOf(vec.y).setScale(5, RoundingMode.HALF_UP).doubleValue();				
//			evalues.y = BigDecimal.valueOf(evalues.y).setScale(5, RoundingMode.HALF_UP).doubleValue();			
			
			switch (edirection)
			{
				case NORTH:
					return BigDecimal.valueOf(evalues.z).setScale(5, RoundingMode.HALF_UP).doubleValue() < BigDecimal.valueOf(vec.z).setScale(5, RoundingMode.HALF_UP).doubleValue();					
				case EAST:
					return BigDecimal.valueOf(evalues.x).setScale(5, RoundingMode.HALF_UP).doubleValue() > BigDecimal.valueOf(vec.x).setScale(5, RoundingMode.HALF_UP).doubleValue();
				case SOUTH:
					return BigDecimal.valueOf(evalues.z).setScale(5, RoundingMode.HALF_UP).doubleValue() > BigDecimal.valueOf(vec.z).setScale(5, RoundingMode.HALF_UP).doubleValue();
				case WEST:
					return BigDecimal.valueOf(evalues.x).setScale(5, RoundingMode.HALF_UP).doubleValue() < BigDecimal.valueOf(vec.x).setScale(5, RoundingMode.HALF_UP).doubleValue();
				case UP:
					return evalues.y > vec.y;
				case DOWN:
					return evalues.y < vec.y;
				case UNKNOWN:
					return false;					
			}
		}
		return false;
	}
}
