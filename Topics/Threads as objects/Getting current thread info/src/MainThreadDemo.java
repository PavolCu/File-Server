class Info {

    public static void printCurrentThreadInfo() {
        // get the thread and print its info
        Thread t = Thread.currentThread();
        String name = t.getName();
        int priority = t.getPriority();

        System.out.println("name: " + name);
        System.out.println("priority: " + priority);
    }
    class MainThreadDemo {
        public void execute() {
            Thread.currentThread().setName("my-thread");
            Info.printCurrentThreadInfo();
        }
    }
}