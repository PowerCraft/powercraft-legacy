package net.minecraftforge.common;

public class Property
{
    public enum Type
    {
        STRING,
        INTEGER,
        BOOLEAN
    }

    private String name;
    public String value;
    public String comment;
    private Type type;

    public Property() {}

    public Property(String name, String value, Type type)
    {
        setName(name);
        this.value = value;
        this.type = type;
    }

    public int getInt()
    {
        return getInt(-1);
    }

    public int getInt(int _default)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            return _default;
        }
    }

    public boolean isIntValue()
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public boolean getBoolean(boolean _default)
    {
        if (isBooleanValue())
        {
            return Boolean.parseBoolean(value);
        }
        else
        {
            return _default;
        }
    }

    public boolean isBooleanValue()
    {
        return ("true".equals(value.toLowerCase()) || "false".equals(value.toLowerCase()));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
