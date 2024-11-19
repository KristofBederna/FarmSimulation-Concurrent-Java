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
        int moveX, moveY;

        Tile currentTile = farm[this.getX()][this.getY()];
        Tile targetTile;

        while (!currentTile.tryLock()) {
        }

        try {
            do {
                moveX = rand.nextInt(3) - 1;
                moveY = rand.nextInt(3) - 1;

                boolean dogAdjacent = false;
                int dogX = 0;
                int dogY = 0;

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (farm[this.getX() + dx][this.getY() + dy].getOccupant() != null &&
                                farm[this.getX() + dx][this.getY() + dy].getOccupant() instanceof Dog) {
                            dogAdjacent = true;
                            dogX = this.getX() + dx;
                            dogY = this.getY() + dy;
                            break;
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

                    targetTile = farm[this.getX() + moveX][this.getY() + moveY];
                    while (!targetTile.tryLock()) {
                    }

                    try {
                        if (targetTile.isOccupied()) {
                            moveX = 0;
                            moveY = 0;
                            break;
                        }
                    } finally {
                        targetTile.unlock();
                    }
                }
            } while ((moveX == 0 && moveY == 0) || !isValidMove(this, moveX, moveY, farm));

            if (isAdjacentToDog(this.getX(), this.getY(), dogs)) {
                moveX = 0;
                moveY = 0;
            }
            currentTile.setOccupant(null);
            this.updateLocation(this.getX() + moveX, this.getY() + moveY);

            targetTile = farm[this.getX()][this.getY()];
            while (!targetTile.tryLock()) {
            }

            try {
                targetTile.setOccupant(this);
            } finally {
                targetTile.unlock();
            }

            if (isAtGate(this, farm.length)) {
                Main.simOver = true;
                System.out.println(this.name + " has reached the gate and escaped!");
            }
        } finally {
            currentTile.unlock();
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

    private boolean isAdjacentToDog(int newX, int newY, Dog[] dogs) {
        int dogX = dogs[0].getX();
        int dogY = dogs[0].getY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (newX + dx == dogX && newY + dy == dogY) {
                    return true;
                }
            }
        }
        return false;
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
