import java.util.Random;

public class Farm {
    private int size;
    private Object[][] farm;
    private Sheep[] sheeps = new Sheep[10];
    private Dog[] dogs = new Dog[5];

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
        Random random = new Random();
        char sheepName = 'A';

        for (int i = 0; i < 10; i++) {
            int x, y;

            do {
                x = random.nextInt(1, size - 1);
                y = random.nextInt(1, size - 1);
            } while (isTileOccupied(x, y) || !isInsideInnerThird(x, y));

            Sheep sheep = new Sheep(x, y, String.valueOf(sheepName));
            sheeps[i] = sheep;
            farm[x][y] = sheep;

            sheepName++;
        }
    }

    private void initDogs() {
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int x, y;

            do {
                x = random.nextInt(1, size - 1);
                y = random.nextInt(1, size - 1);
            } while (isTileOccupied(x, y) || isInsideInnerThird(x, y));

            Dog dog = new Dog(x, y, i + 1);
            dogs[i] = dog;
            farm[x][y] = dog;
        }
    }

    public synchronized void moveSheeps() {
        Random rand = new Random();

        for (Sheep sheep : sheeps) {
            if (sheep == null) continue;
            System.out.println("moving: " + sheep.name);
            int x, y;

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
                        x = 1;
                        y = 1;
                    } else if (dogX < sheep.getX() && dogY == sheep.getY()) {
                        x = 1;
                        y = 0;
                    } else if (dogX < sheep.getX() && dogY > sheep.getY()) {
                        x = 1;
                        y = -1;
                    } else if (dogX == sheep.getX() && dogY < sheep.getY()) {
                        x = 0;
                        y = 1;
                    } else if (dogX == sheep.getX() && dogY > sheep.getY()) {
                        x = 0;
                        y = -1;
                    } else if (dogX > sheep.getX() && dogY < sheep.getY()) {
                        x = -1;
                        y = 1;
                    } else if (dogX > sheep.getX() && dogY == sheep.getY()) {
                        x = -1;
                        y = 0;
                    } else if (dogX > sheep.getX() && dogY > sheep.getY()) {
                        x = -1;
                        y = -1;
                    }
                    if (farm[sheep.getX()+x][sheep.getY()+y] != " ") {
                        x = 0;
                        y = 0;
                        break;
                    }
                }
            } while ((x == 0 && y == 0) || !isValidMove(sheep, x, y));

            if (isAdjacentToDog(sheep.getX(), sheep.getY())) {
                continue;
            }


            farm[sheep.getX()][sheep.getY()] = " ";
            sheep.updateLocation(sheep.getX() + x, sheep.getY() + y);
            farm[sheep.getX()][sheep.getY()] = sheep;

            if (isAtGate(sheep)) {
                System.out.println(sheep.name + " has reached the gate and escaped!");
                Main.simOver = true;
            }
        }
    }

    public synchronized void moveDogs() {
        Random rand = new Random();

        for (Dog dog : dogs) {
            if (dog == null) continue;

            int x, y;

            do {
                x = rand.nextInt(3) - 1;
                y = rand.nextInt(3) - 1;
            } while ((x == 0 && y == 0) || !isValidMove(dog, x, y) || isInsideInnerThird(dog.getX() + x, dog.getY() + y));

            farm[dog.getX()][dog.getY()] = " ";
            dog.updateLocation(dog.getX() + x, dog.getY() + y);
            farm[dog.getX()][dog.getY()] = dog;
        }
    }

    private boolean isValidMove(Sheep sheep, int moveX, int moveY) {
        int newX = sheep.getX() + moveX;
        int newY = sheep.getY() + moveY;

        return !isTileOccupied(newX, newY);
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

    private boolean isInsideInnerThird(int x, int y) {
        int innerStart = (size - 2) / 3 + 1;
        int innerEnd = innerStart + (size - 2) / 3 - 1;

        return x >= innerStart && x <= innerEnd && y >= innerStart && y <= innerEnd;
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
