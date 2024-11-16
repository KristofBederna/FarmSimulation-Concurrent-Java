public class Dog {
    private int x;
    private int y;
    int name;

    public Dog(int x, int y, int name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void updateLocation(int newX, int newY) {
        this.x = 1;
        this.y = 1;
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
