class Invoker {

    public static void invokeMethods(Thread t1, Thread t2, Thread t3)
            throws InterruptedException {
        // Start t3 and wait for it to finish
        t3.start();
        t3.join();
        // Start t2 and wait for it to finish
        t2.start();
        t2.join();
        // Start t1 and wait for it to finish
        t1.start();
        t1.join();
    }
}