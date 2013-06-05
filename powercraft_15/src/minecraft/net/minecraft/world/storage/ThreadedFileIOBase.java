package net.minecraft.world.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable
{
    /** Instance of ThreadedFileIOBase */
    public static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
    private List threadedIOQueue = Collections.synchronizedList(new ArrayList());
    private volatile long writeQueuedCounter = 0L;
    private volatile long savedIOCounter = 0L;
    private volatile boolean isThreadWaiting = false;

    private ThreadedFileIOBase()
    {
        Thread thread = new Thread(this, "File IO Thread");
        thread.setPriority(1);
        thread.start();
    }

    public void run()
    {
        while (true)
        {
            this.processQueue();
        }
    }

    /**
     * Process the items that are in the queue
     */
    private void processQueue()
    {
        for (int i = 0; i < this.threadedIOQueue.size(); ++i)
        {
            IThreadedFileIO ithreadedfileio = (IThreadedFileIO)this.threadedIOQueue.get(i);
            boolean flag = ithreadedfileio.writeNextIO();

            if (!flag)
            {
                this.threadedIOQueue.remove(i--);
                ++this.savedIOCounter;
            }

            try
            {
                Thread.sleep(this.isThreadWaiting ? 0L : 10L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }

        if (this.threadedIOQueue.isEmpty())
        {
            try
            {
                Thread.sleep(25L);
            }
            catch (InterruptedException interruptedexception1)
            {
                interruptedexception1.printStackTrace();
            }
        }
    }

    /**
     * threaded io
     */
    public void queueIO(IThreadedFileIO par1IThreadedFileIO)
    {
        if (!this.threadedIOQueue.contains(par1IThreadedFileIO))
        {
            ++this.writeQueuedCounter;
            this.threadedIOQueue.add(par1IThreadedFileIO);
        }
    }

    public void waitForFinish() throws InterruptedException
    {
        this.isThreadWaiting = true;

        while (this.writeQueuedCounter != this.savedIOCounter)
        {
            Thread.sleep(10L);
        }

        this.isThreadWaiting = false;
    }
}
