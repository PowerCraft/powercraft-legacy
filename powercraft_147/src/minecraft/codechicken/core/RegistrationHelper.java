package codechicken.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class RegistrationHelper
{
	@SuppressWarnings("unchecked")
	public static void registerHandledEntity(Class<? extends Entity> entityClass, String identifier)
	{
        EntityList.classToStringMapping.put(entityClass, identifier);
        EntityList.stringToClassMapping.put(identifier, entityClass);
	}
}
