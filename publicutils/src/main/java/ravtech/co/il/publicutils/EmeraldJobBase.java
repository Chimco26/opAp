package ravtech.co.il.publicutils;

/**
 * Created by david
 */

public abstract class EmeraldJobBase extends JobBase{

    @Override
    protected abstract void executeJob(OnJobFinishedListener onJobFinishedListener);

}
