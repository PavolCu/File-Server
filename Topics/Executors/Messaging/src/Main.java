import java.util.concurrent.*;

/* Do not change this class */
class Message {
    final String text;
    final String from;
    final String to;

    Message(String from, String to, String text) {
        this.text = text;
        this.from = from;
        this.to = to;
    }
}

/* Do not change this interface */
interface AsyncMessageSender {
    void sendMessages(Message[] messages);
    void stop();
}

class AsyncMessageSenderImpl implements AsyncMessageSender {
    private final ExecutorService executor; // TODO initialize the executor
    private final int repeatFactor;

    public AsyncMessageSenderImpl(int repeatFactor) {

        this.repeatFactor = repeatFactor;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void sendMessages(Message[] messages) {
        for (Message msg : messages) {
            // TODO repeat messages
            executor.submit(() -> {
                for (int i = 0; i < repeatFactor; i++) {
                    System.out.printf("(%s>%s): %s\n", msg.from, msg.to, msg.text); // do not change it
                }
            });
        }
    }

    @Override
    public void stop() {
        // TODO stop the executor and wait for it
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.out.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}