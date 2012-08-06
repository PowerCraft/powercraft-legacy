package net.minecraft.src;

public class ReportedException extends RuntimeException
{
    private final CrashReport field_71576_a;

    public ReportedException(CrashReport par1CrashReport)
    {
        this.field_71576_a = par1CrashReport;
    }

    public CrashReport func_71575_a()
    {
        return this.field_71576_a;
    }

    public Throwable getCause()
    {
        return this.field_71576_a.func_71505_b();
    }

    public String getMessage()
    {
        return this.field_71576_a.getDescription();
    }
}
