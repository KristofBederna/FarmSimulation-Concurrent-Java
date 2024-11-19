public class Dog extends Thread {
    private int x;
    private int y;
    int name;

    public Dog(int x, int y, int name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public void run() {
        while (!Main.simOver) {
            try {
                Thread.sleep(200);
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
        return String.valueOf(name);
    }
}
