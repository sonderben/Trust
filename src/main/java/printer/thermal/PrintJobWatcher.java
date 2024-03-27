package printer.thermal;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.*;

public class PrintJobWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintJobWatcher.class.getSimpleName());

    private final AtomicBoolean done = new AtomicBoolean();

    public PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            @Override
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done.set(true);
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    /**
     * Wait for print job to be done.
     */
    public synchronized void waitForDone() {
        try {
            LOGGER.info("Checking printer status...");
            while (!done.get()) {
                wait();
            }
            LOGGER.info("Print Job Completed!");
        } catch (InterruptedException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
    }
}
