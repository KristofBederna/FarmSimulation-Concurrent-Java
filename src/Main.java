public class Main {
    static boolean simOver = false;

    public static void main(String[] args) {
        Farm farm = new Farm();
        farm.moveSheeps();
        farm.moveDogs();
        while (!simOver) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            farm.printFarm();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        farm.printFarm();
        System.out.println("Sim over");
    }
}
