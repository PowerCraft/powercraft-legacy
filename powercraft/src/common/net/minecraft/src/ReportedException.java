package net.minecraft.src;

public class ReportedException extends RuntimeException
{
    /** Instance of CrashReport. */
    private final CrashReport theReportedExceptionCrashReport;

    public ReportedException(CrashReport par1CrashReport)
    {
        this.theReportedExceptionCrashReport = par1CrashReport;
    }

    /**
     * Gets the CrashReport Instance.
     */
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
