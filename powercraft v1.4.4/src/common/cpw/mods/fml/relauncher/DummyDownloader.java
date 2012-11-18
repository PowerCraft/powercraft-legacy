package cpw.mods.fml.relauncher;

public class DummyDownloader implements IDownloadDisplay
{
    @Override
    public void resetProgress(int sizeGuess)
    {
    }

    @Override
    public void setPokeThread(Thread currentThread)
    {
    }

    @Override
    public void updateProgress(int fullLength)
    {
    }

    @Override
    public boolean shouldStopIt()
    {
        return false;
    }

    @Override
    public void updateProgressString(String string, Object... data)
    {
    }

    @Override
    public Object makeDialog()
    {
        return null;
    }

    @Override
    public void makeHeadless()
    {
    }
}
