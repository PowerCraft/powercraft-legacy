package net.minecraft.entity;

import java.util.concurrent.Callable;

class CallableEntityName implements Callable
{
    final Entity field_96564_a;

    CallableEntityName(Entity par1Entity)
    {
        this.field_96564_a = par1Entity;
    }

    public String func_96563_a()
    {
        return this.field_96564_a.getEntityName();
    }

    public Object call()
    {
        return this.func_96563_a();
    }
}
