import java.util.Random;

public class Farm {
    private int size;
    private Tile[][] farm;
    private Sheep[] sheeps = new Sheep[10];
    private Dog[] dogs = new Dog[5];

    public Farm(int size) {
        this.size = (size * 3) + 2;
        farm = new Tile[this.size][this.size];
        initFarm();
    }

    public Farm() {
        this.size = 14;
        farm = new Tile[this.size][this.size];
        initFarm();
    }

    private void initFarm() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                farm[i][j] = new Tile();
                farm[i][j].setOccupant(null);
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    farm[i][j].setOccupant("#");
                }
            }
        }
        initGates();
        initDogs();
        initSheeps();
    }

    private void initGates() {
        Random rand = new Random();
        farm[0][rand.nextInt(1, size - 2)].setOccupant(null);
        farm[size - 1][rand.nextInt(1, size - 2)].setOccupant(null);
        farm[rand.nextInt(1, size - 2)][0].setOccupant(null);
        farm[rand.nextInt(1, size - 2)][size - 1].setOccupant(null);
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

            Sheep sheep = new Sheep(x, y, String.valueOf(sheepName), this.dogs, this.farm);
            sheeps[i] = sheep;
            farm[x][y].setOccupant(sheep);

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

            Dog dog = new Dog(x, y, i + 1, this.farm);
            dogs[i] = dog;
            farm[x][y].setOccupant(dog);
        }
    }

    public void moveSheeps() {
        for (Sheep sheep : sheeps) {
            if (sheep == null) continue;
            sheep.start();
        }
    }

    public void moveDogs() {
        for (Dog dog : dogs) {
            if (dog == null) continue;
            dog.start();
        }
    }

    private boolean isTileOccupied(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return true;
        }

        return farm[x][y].isOccupied();
    }

    private boolean isInsideInnerThird(int x, int y) {
        int innerStart = (size - 2) / 3 + 1;
        int innerEnd = innerStart + (size - 2) / 3 - 1;

        return x >= innerStart && x <= innerEnd && y >= innerStart && y <= innerEnd;
    }

    public void printFarm() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(farm[i][j]);
            }
            System.out.println();
        }
    }
}
