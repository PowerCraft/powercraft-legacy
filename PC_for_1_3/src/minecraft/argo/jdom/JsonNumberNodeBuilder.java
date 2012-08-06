package argo.jdom;

final class JsonNumberNodeBuilder implements JsonNodeBuilder
{
    private final JsonNode field_74602_a;

    JsonNumberNodeBuilder(String par1Str)
    {
        this.field_74602_a = JsonNodeFactories.aJsonNumber(par1Str);
    }

    public JsonNode buildNode()
    {
        return this.field_74602_a;
    }
}
