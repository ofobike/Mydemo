package common;

public class Test {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        IdWorker idWorker = new IdWorker(1, 1);
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
        System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
    }

}
