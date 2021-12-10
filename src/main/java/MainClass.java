import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static boolean winnerFlag = false;
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        CyclicBarrier barrierStart = new CyclicBarrier(CARS_COUNT, () -> {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        });
        CyclicBarrier barrierFinish = new CyclicBarrier(CARS_COUNT, () -> {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        });

        Lock lock = new ReentrantLock();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(barrierStart, race, 20 + (int) (Math.random() * 10), barrierFinish, lock);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();

        }

    }
}


