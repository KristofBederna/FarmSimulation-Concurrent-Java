import java.util.Random;

public class Sheep extends Thread {
    private int x;
    private int y;
    private String name;
    private Dog[] dogs;
    private Tile[][] farm;

    public Sheep(int x, int y, String name, Dog[] dogs, Tile[][] farm) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dogs = dogs;
        this.farm = farm;
    }

    @Override
    public void run() {
        while (!Main.simOver) {
            try {
                Thread.sleep(200);
                this.move(dogs, farm);
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
        return name;
    }

    public void move(Dog[] dogs, Tile[][] farm) {
        Random rand = new Random();
        int moveX = 0, moveY = 0;
        boolean dogAdjacent = false;
        int dogX = 0, dogY = 0;

        Tile currentTile = farm[this.getX()][this.getY()];
        Tile targetTile = null;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = this.getX() + dx;
                int neighborY = this.getY() + dy;

                if (neighborX < 0 || neighborX >= farm.length || neighborY < 0 || neighborY >= farm[0].length) {
                    continue;
                }

                Tile neighborTile = farm[neighborX][neighborY];
                if (neighborTile.tryLock()) {
                    try {
                        if (neighborTile.getOccupant() instanceof Dog) {
                            dogAdjacent = true;
                            dogX = neighborX;
                            dogY = neighborY;
                            break;
                        }
                    } finally {
                        neighborTile.unlock();
                    }
                }
            }
            if (dogAdjacent) break;
        }

        if (dogAdjacent) {
            if (dogX < this.getX() && dogY < this.getY()) {
                moveX = 1;
                moveY = 1;
            } else if (dogX < this.getX() && dogY == this.getY()) {
                moveX = 1;
                moveY = 0;
            } else if (dogX < this.getX() && dogY > this.getY()) {
                moveX = 1;
                moveY = -1;
            } else if (dogX == this.getX() && dogY < this.getY()) {
                moveX = 0;
                moveY = 1;
            } else if (dogX == this.getX() && dogY > this.getY()) {
                moveX = 0;
                moveY = -1;
            } else if (dogX > this.getX() && dogY < this.getY()) {
                moveX = -1;
                moveY = 1;
            } else if (dogX > this.getX() && dogY == this.getY()) {
                moveX = -1;
                moveY = 0;
            } else if (dogX > this.getX() && dogY > this.getY()) {
                moveX = -1;
                moveY = -1;
            }
        } else {
            do {
                moveX = rand.nextInt(3) - 1;
                moveY = rand.nextInt(3) - 1;
                int targetX = this.getX() + moveX;
                int targetY = this.getY() + moveY;

                if (isValidMove(this, moveX, moveY, farm)) {
                    targetTile = farm[targetX][targetY];
                    break;
                }
            } while (true);
        }

        int targetX = this.getX() + moveX;
        int targetY = this.getY() + moveY;
        targetTile = farm[targetX][targetY];

        Tile firstLock = (currentTile.hashCode() < targetTile.hashCode()) ? currentTile : targetTile;
        Tile secondLock = (firstLock == currentTile) ? targetTile : currentTile;

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                currentTile.setOccupant(null);
                this.updateLocation(targetX, targetY);
                targetTile.setOccupant(this);

                if (isAtGate(this, farm.length)) {
                    Main.simOver = true;
                    System.out.println(this.name + " has reached the gate and escaped!");
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    private boolean isValidMove(Sheep sheep, int moveX, int moveY, Tile[][] farm) {
        int newX = sheep.getX() + moveX;
        int newY = sheep.getY() + moveY;

        return !isTileOccupied(newX, newY, farm.length, farm);
    }

    private boolean isTileOccupied(int x, int y, int size, Tile[][] farm) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return true;
        }

        return farm[x][y].isOccupied();
    }

    private boolean isAtGate(Sheep sheep, int size) {
        int x = sheep.getX();
        int y = sheep.getY();

        if (x >= 1 && x < size - 1 && y >= 1 && y < size - 1) {
            return false;
        }

        return (x == 0 || x == size - 1 || y == 0 || y == size - 1);
    }
}
