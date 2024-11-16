import java.util.Random;

public class Farm {
    private int size;
    private Object[][] farm;
    private Sheep[] sheeps = new Sheep[1];
    private Dog[] dogs = new Dog[1];

    public Farm(int size) {
        this.size = (size * 3) + 2;
        farm = new Object[this.size][this.size];
        initFarm();
    }

    public Farm() {
        this.size = 14;
        farm = new Object[this.size][this.size];
        initFarm();
    }

    private void initFarm() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                farm[i][j] = " ";
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    farm[i][j] = "#";
                }
            }
        }
        initGates();
        initSheeps();
        initDogs();
    }

    private void initGates() {
        Random rand = new Random();
        farm[0][rand.nextInt(1, size - 2)] = " ";
        farm[size - 1][rand.nextInt(1, size - 2)] = " ";
        farm[rand.nextInt(1, size - 2)][0] = " ";
        farm[rand.nextInt(1, size - 2)][size - 1] = " ";
    }

    private void initSheeps() {
        Sheep sheep = new Sheep(6, 6, "A");
        sheeps[0] = sheep;
        farm[sheep.getX()][sheep.getY()] = sheep;
    }

    private void initDogs() {
        Dog dog = new Dog(1, 1, 1);
        dogs[0] = dog;
        farm[dog.getX()][dog.getY()] = dog;
    }

    public void moveSheeps() {
        Random rand = new Random();
        int x, y;
        Sheep sheep = sheeps[0];

        do {
            x = rand.nextInt(3) - 1;
            y = rand.nextInt(3) - 1;

            int dogX = dogs[0].getX();
            int dogY = dogs[0].getY();
            boolean dogAdjacent = false;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if ((dogX == sheep.getX() + dx) && (dogY == sheep.getY() + dy)) {
                        dogAdjacent = true;
                        break;
                    }
                }
                if (dogAdjacent) break;
            }

            if (dogAdjacent) {
                if (dogX < sheep.getX() && dogY < sheep.getY()) {
                    // Dog is top-left, move bottom-right
                    x = 1;
                    y = 1;
                } else if (dogX < sheep.getX() && dogY == sheep.getY()) {
                    // Dog is mid-left, move mid-right
                    x = 1;
                    y = 0;
                } else if (dogX < sheep.getX() && dogY > sheep.getY()) {
                    // Dog is bottom-left, move top-right
                    x = 1;
                    y = -1;
                } else if (dogX == sheep.getX() && dogY < sheep.getY()) {
                    // Dog is top-middle, move bottom-middle
                    x = 0;
                    y = 1;
                } else if (dogX == sheep.getX() && dogY > sheep.getY()) {
                    // Dog is bottom-middle, move top-middle
                    x = 0;
                    y = -1;
                } else if (dogX > sheep.getX() && dogY < sheep.getY()) {
                    // Dog is top-right, move bottom-left
                    x = -1;
                    y = 1;
                } else if (dogX > sheep.getX() && dogY == sheep.getY()) {
                    // Dog is mid-right, move mid-left
                    x = -1;
                    y = 0;
                } else if (dogX > sheep.getX() && dogY > sheep.getY()) {
                    // Dog is bottom-right, move top-left
                    x = -1;
                    y = -1;
                }
            }

        } while ((x == 0 && y == 0) || !isValidMove(sheep, x, y));

        farm[sheep.getX()][sheep.getY()] = " ";
        sheep.updateLocation(sheep.getX() + x, sheep.getY() + y);
        farm[sheep.getX()][sheep.getY()] = sheep;

        if (isAtGate(sheep)) {
            System.out.println("The sheep has reached the gate and escaped!");
            Main.simOver = true;
        }
    }




    public void moveDogs() {
        Random rand = new Random();
        int x, y;
        Dog dog = dogs[0];

        do {
            x = rand.nextInt(3) - 1;
            y = rand.nextInt(3) - 1;

        } while ((x == 0 && y == 0) || !isValidMove(dog, x, y));

        farm[dog.getX()][dog.getY()] = " ";
        dog.updateLocation(dog.getX() + x, dog.getY() + y);
        farm[dog.getX()][dog.getY()] = dog;
    }

    private boolean isValidMove(Sheep sheep, int moveX, int moveY) {
        int newX = sheep.getX() + moveX;
        int newY = sheep.getY() + moveY;

        return !isTileOccupied(newX, newY) && !isAdjacentToDog(newX, newY);
    }

    private boolean isValidMove(Dog dog, int moveX, int moveY) {
        int newX = dog.getX() + moveX;
        int newY = dog.getY() + moveY;
        return !isTileOccupied(newX, newY) && !isOutOfBounds(newX, newY);
    }

    private boolean isTileOccupied(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return true;
        }

        return farm[x][y].equals("#") || farm[x][y] instanceof Sheep || farm[x][y] instanceof Dog;
    }


    private boolean isOutOfBounds(int x, int y) {
        return x <= 0 || x >= size - 1 || y <= 0 || y >= size - 1;
    }

    private boolean isAdjacentToDog(int newX, int newY) {
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

    private boolean isAtGate(Sheep sheep) {
        int x = sheep.getX();
        int y = sheep.getY();

        if (x >= 1 && x < size - 1 && y >= 1 && y < size - 1) {
            return false;
        }

        if ((x == 0 || x == size - 1 || y == 0 || y == size - 1)) {
            return true;
        }

        return false;
    }


    public void printFarm() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(farm[i][j]);
                if (j == size - 1) {
                    System.out.println();
                }
            }
        }
    }
}