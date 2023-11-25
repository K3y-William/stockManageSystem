package test;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class startTransaction {
    public static void main(String[] args) {
        // 创建一个阻塞队列用于在两个线程之间传递数据
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();

        // 启动输入线程
        Thread inputThread = new Thread(new NumberInputThread(sharedQueue));
        inputThread.start();

        // 启动输出线程
        Thread outputThread = new Thread(new NumberOutputThread(sharedQueue));
        outputThread.start();
    }
}

// 输入线程
class NumberInputThread implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;

    public NumberInputThread(BlockingQueue<Integer> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入数字，输入-1结束：");

        try {
            while (true) {
                int inputNumber = scanner.nextInt();
                if (inputNumber == -1) {
                    break;
                }
                sharedQueue.put(inputNumber); // 将输入的数字放入队列
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

// 输出线程
class NumberOutputThread implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;

    public NumberOutputThread(BlockingQueue<Integer> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int receivedNumber = sharedQueue.take(); // 从队列中取出数字
                System.out.println("接收到数字: " + receivedNumber);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
