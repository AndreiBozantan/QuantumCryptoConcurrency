import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class QuantumCryptoCurrency {

    private static final int BLOCK_SIZE = 256;
    private static final double MINING_DIFFICULTY = 0.1;

    private static int blockNumber = 0;
    private static double difficulty = MINING_DIFFICULTY;

    public static void main(String[] args) throws Exception {
        byte[] key = generateKey(128);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(() -> mineBlock(key), 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> adjustDifficulty(), 0, 60, TimeUnit.SECONDS);
    }

    private static void mineBlock(byte[] key) {
        Stream<Double> bits = generateBitStream(key);
        bits.map(b -> b > 0.5 ? 1 : 0)
            .filter(b -> b == 1)
            .limit(BLOCK_SIZE)
            .reduce((a, b) -> a + b)
            .ifPresent(total -> {
                String hash = sha256(Double.toString(total));
                System.out.println("Mined block #" + blockNumber + " with hash: " + hash);
                blockNumber++;
            });
    }

    private static void adjustDifficulty() {
        difficulty = difficulty * (1 + 0.2 * Math.sin(System.currentTimeMillis() / 1000));
        System.out.println("Difficulty adjusted to: " + difficulty);
    }

    private static byte[] generateKey(int size) throws Exception {
        Random rand = new Random();
        byte[] seed = new byte[size / 8];
        rand.nextBytes(seed);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(seed);
        return md.digest();
    }

    private static Stream<Double> generateBitStream(byte[] key) {
        DoubleStream randStream = new Random().doubles(4096).parallel();
        Stream<Double> shaStream = IntStream.range(0, key.length)
                .parallel()
                .boxed()
                .flatMap(i -> DoubleStream.of(key[i] & 0xff))
                .map(d -> d / 256.0);
        return Stream.concat(randStream, shaStream);
    }

    private static String sha256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
