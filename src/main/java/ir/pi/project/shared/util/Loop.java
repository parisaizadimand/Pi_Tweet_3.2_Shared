package ir.pi.project.shared.util;

public class Loop {
    private final double fps;
    private volatile boolean running = false;
    protected Thread thread;
    private final Runnable updatable;

    public Loop(double fps, Runnable updatable) {
        this.fps = fps;
        this.updatable = updatable;
        thread = new Thread(this::run);
    }

    public void update() {
        if (updatable != null)
            updatable.run();
    }

    private void run() {
        long lastTime = System.nanoTime();
        double ns_per_update = 1000000000 / fps;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (Math.abs(now - lastTime)) * 1.0 / ns_per_update;
            lastTime = now;
            if (delta < 1) {
                sleep((long) (ns_per_update * (1 - delta)));
            }
            while (running && delta >= 1) {
                try {
                    update();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                delta--;
            }
        }
    }

    public void sleep(long time) {

        int milliseconds = (int) (time) / 1000000;
        int nanoseconds = (int) (time) % 1000000;
        try {
            Thread.sleep(milliseconds, nanoseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void restart() {
        thread = new Thread(this::run);
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        if (Thread.currentThread().equals(thread))
            return;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}