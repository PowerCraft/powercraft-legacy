package net.minecraft.src;

import java.lang.annotation.Annotation;

public @interface MLProp
{
    public abstract String name();

    public abstract String info();

    public abstract double min();

    public abstract double max();
}
