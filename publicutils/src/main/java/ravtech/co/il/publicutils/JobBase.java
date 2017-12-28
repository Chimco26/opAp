//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ravtech.co.il.publicutils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class JobBase implements Runnable {
    private ScheduledFuture<?> mScheduledFuture;
    private long mIntervalDelay;
    private TimeUnit mTimeUnit;
    private AtomicBoolean isJobActive = new AtomicBoolean(false);
    private final JobBase.OnJobFinishedListener mOnJobFinishedListener = new JobBase.OnJobFinishedListener() {
        public void onJobFinished() {
            JobBase.this.attachToPolling(JobBase.this.mIntervalDelay);
        }
    };
    private static final JobBase.PollingManager sPollingManager = new JobBase.PollingManager();

    public JobBase() {
    }

    public void startJob(long startDelay, long intervalDelay, TimeUnit timeUnit) {
        if(!this.isJobActive.getAndSet(true)) {
            this.mIntervalDelay = intervalDelay;
            this.mTimeUnit = timeUnit;
            this.attachToPolling(startDelay);
        }

    }

    public void stopJob() {
        if(this.isJobActive.getAndSet(false)) {
            this.cancel();
        }

    }

    public final void run() {
        this.executeJob(this.mOnJobFinishedListener);
    }

    protected abstract void executeJob(JobBase.OnJobFinishedListener var1);

    private void attachToPolling(long initDelay) {
        if(this.isJobActive.get()) {
            this.mScheduledFuture = sPollingManager.register(this, initDelay, this.mTimeUnit);
        }

    }

    private void cancel() {
        if(this.mScheduledFuture != null) {
            this.mScheduledFuture.cancel(false);
        }

    }

    private static final class PollingManager {
        private static final int CORE_POOL_SIZE = 4;
        private static JobBase.PollingManager msInstance;
        private final ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

        private PollingManager() {
            this.mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
        }

        private ScheduledFuture<?> register(Runnable job, long initDelay, TimeUnit timeUnit) {
            return this.mScheduledThreadPoolExecutor.schedule(job, initDelay, timeUnit);
        }
    }

    public interface OnJobFinishedListener {
        void onJobFinished();
    }
}
