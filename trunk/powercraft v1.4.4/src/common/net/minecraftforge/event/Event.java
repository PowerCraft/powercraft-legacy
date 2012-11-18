package net.minecraftforge.event;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public class Event
{
    @Retention(value = RUNTIME)
    @Target(value = TYPE)
    public @interface HasResult {}

    public enum Result
    {
        DENY,
        DEFAULT,
        ALLOW
    }

    private boolean isCanceled = false;
    private final boolean isCancelable;
    private Result result = Result.DEFAULT;
    private final boolean hasResult;
    private static ListenerList listeners = new ListenerList();

    public Event()
    {
        setup();
        isCancelable = hasAnnotation(Cancelable.class);
        hasResult = hasAnnotation(HasResult.class);
    }

    private boolean hasAnnotation(Class annotation)
    {
        Class cls = this.getClass();

        while (cls != Event.class)
        {
            if (cls.isAnnotationPresent(Cancelable.class))
            {
                return true;
            }

            cls = cls.getSuperclass();
        }

        return false;
    }

    public boolean isCancelable()
    {
        return isCancelable;
    }

    public boolean isCanceled()
    {
        return isCanceled;
    }

    public void setCanceled(boolean cancel)
    {
        if (!isCancelable())
        {
            throw new IllegalArgumentException("Attempted to cancel a uncancelable event");
        }

        isCanceled = cancel;
    }

    public boolean hasResult()
    {
        return hasResult;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result value)
    {
        result = value;
    }

    protected void setup()
    {
    }

    public ListenerList getListenerList()
    {
        return listeners;
    }
}
