/**
 * Timer thread used for keeping track of how long it
 * takes the AI to make a move
 */
public class Timer implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
