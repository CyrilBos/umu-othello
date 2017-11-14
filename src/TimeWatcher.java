/**
 * This class implements the Runnable interface to run a time watch in a separate thread.
 *
 * @author cbos502 ens17cbs
 */
//TODO: DELETE
public class TimeWatcher implements Runnable {
    private final long timeLimitMilis;
    private boolean isTimeUp;

    public TimeWatcher(long timeLimitMilis) {
        //To avoid going over the limit, we substract 100 miliseconds from it
        this.timeLimitMilis = timeLimitMilis - 100;
        this.isTimeUp = false;
    }

    synchronized public boolean isTimeUp() {
        return isTimeUp;
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (System.currentTimeMillis() - startTime < this.timeLimitMilis);

        this.isTimeUp = true;
    }

}
