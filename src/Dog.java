import java.util.Random;

public class Dog extends Thread {
    private int x;
    private int y;
    private int name;
    private Tile[][] farm;

    public Dog(int x, int y, int name, Tile[][] farm) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.farm = farm;
    }

    @Override
    public void run() {
        while (!Main.simOver) {
            try {
                Thread.sleep(200);
                this.move();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.interrupt();
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

    public void move() {
        Random rand = new Random();
        int deltaX, deltaY;

        Tile currentTile = farm[x][y];
        Tile targetTile;

        while (!currentTile.tryLock()) {
        }

        try {
            do {
                deltaX = rand.nextInt(3) - 1;
                deltaY = rand.nextInt(3) - 1;
            } while ((deltaX == 0 && deltaY == 0) ||
                    !isValidMove(deltaX, deltaY) ||
                    isInsideInnerThird(x + deltaX, y + deltaY));

            targetTile = farm[x + deltaX][y + deltaY];
            while (!targetTile.tryLock()) {
            }

            try {
                currentTile.setOccupant(null);
                updateLocation(x + deltaX, y + deltaY);
                targetTile.setOccupant(this);
            } finally {
                targetTile.unlock();
            }
        } finally {
            currentTile.unlock();
        }
    }

    private boolean isValidMove(int deltaX, int deltaY) {
        int newX = x + deltaX;
        int newY = y + deltaY;

        return !isOutOfBounds(newX, newY) && !farm[newX][newY].isOccupied();
    }

    private boolean isOutOfBounds(int newX, int newY) {
        return newX <= 0 || newX >= farm.length - 1 || newY <= 0 || newY >= farm[0].length - 1;
    }

    private boolean isInsideInnerThird(int newX, int newY) {
        int innerStart = (farm.length - 2) / 3 + 1;
        int innerEnd = innerStart + (farm.length - 2) / 3 - 1;

        return newX >= innerStart && newX <= innerEnd && newY >= innerStart && newY <= innerEnd;
    }
}
