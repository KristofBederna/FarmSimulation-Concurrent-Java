public class Sheep extends Thread {
    private int x;
    private int y;
    String name;

    public Sheep(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public void run() {
        while (!Main.simOver) {
            try {
                Thread.sleep(100);  // Simulate movement
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLocation(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return name;
    }
}
