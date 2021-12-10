import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier cyclicBarrierStart;
    private CyclicBarrier cyclicBarrierFinish;
    private Lock lock;


    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }


    public Car(CyclicBarrier cyclicBarrierStart, Race race, int speed, CyclicBarrier cyclicBarrierFinish, Lock lock) {
        this.cyclicBarrierStart = cyclicBarrierStart;
        this.cyclicBarrierFinish = cyclicBarrierFinish;
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.lock = lock;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cyclicBarrierStart.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);

        }
        lock.lock();
        if(!MainClass.winnerFlag) {
            System.out.println(name + " ПОБЕДИТЕЛЬ! Он финишировал первым!");
            MainClass.winnerFlag = true;
        }
        lock.unlock();
        //будет ли корректно вместо lock использовать AtomicBoolean?
        try {
            cyclicBarrierFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
