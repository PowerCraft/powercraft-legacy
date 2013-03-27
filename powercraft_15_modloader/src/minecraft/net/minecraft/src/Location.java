package net.minecraft.src;

import argo.jdom.JsonNode;

public class Location extends ValueObject
{
    public String field_96396_a;
    public String field_96395_b;

    public static Location func_98167_a(JsonNode par0JsonNode)
    {
        Location var1 = new Location();
        var1.field_96396_a = par0JsonNode.getStringValue(new Object[] {"locationId"});
        var1.field_96395_b = par0JsonNode.getStringValue(new Object[] {"locationName"});
        return var1;
    }

    public static Location func_98168_a(JsonNode par0JsonNode, String par1Str)
    {
        Location var2 = new Location();
        var2.field_96396_a = par0JsonNode.getStringValue(new Object[] {par1Str, "locationId"});
        var2.field_96395_b = par0JsonNode.getStringValue(new Object[] {par1Str, "locationName"});
        return var2;
    }
}
