public class Sheep {
    private int x;
    private int y;
    String name;

    public Sheep(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
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
