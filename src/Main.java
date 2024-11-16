public class Main {
    static boolean simOver = false;
    public static void main(String[] args) {
        Farm farm = new Farm();
        while (!simOver) {
            farm.printFarm();
            farm.moveSheeps();
            farm.moveDogs();
        }
        farm.printFarm();
        System.out.println("Sim over");
    }
}
