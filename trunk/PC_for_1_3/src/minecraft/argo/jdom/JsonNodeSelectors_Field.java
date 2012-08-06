package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Field extends LeafFunctor
{
    final JsonStringNode field_74643_a;

    JsonNodeSelectors_Field(JsonStringNode par1JsonStringNode)
    {
        this.field_74643_a = par1JsonStringNode;
    }

    public boolean func_74641_a(Map par1Map)
    {
        return par1Map.containsKey(this.field_74643_a);
    }

    public String shortForm()
    {
        return "\"" + this.field_74643_a.getText() + "\"";
    }

    public JsonNode func_74642_b(Map par1Map)
    {
        return (JsonNode)par1Map.get(this.field_74643_a);
    }

    public String toString()
    {
        return "a field called [\"" + this.field_74643_a.getText() + "\"]";
    }

    public Object typeSafeApplyTo(Object par1Obj)
    {
        return this.func_74642_b((Map)par1Obj);
    }

    public boolean matchesNode(Object par1Obj)
    {
        return this.func_74641_a((Map)par1Obj);
    }
}
