package net.minecraft.src;

public class ReportedException extends RuntimeException
{
    private final CrashReport theReportedExceptionCrashReport;

    public ReportedException(CrashReport par1CrashReport)
    {
        this.theReportedExceptionCrashReport = par1CrashReport;
    }

    public CrashReport getTheReportedExceptionCrashReport()
    {
        return this.theReportedExceptionCrashReport;
    }

    public Throwable getCause()
    {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }

    public String getMessage()
    {
        return this.theReportedExceptionCrashReport.getDescription();
    }
}
