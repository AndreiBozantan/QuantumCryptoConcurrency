import java.util.Random;
import java.util.stream.IntStream;

public class QuantumCoinFlip {
    public static void main(String[] args) {
        Random rand = new Random();
        int numQubits = 4;
        int[] state = IntStream.generate(() -> rand.nextInt(2)).limit(numQubits).toArray();
        System.out.println("Initial state: " + getStateString(state));
        int index = rand.nextInt(numQubits);
        state[index] = 1;
        System.out.println("Final state: " + getStateString(state));
        
        BanachSpace space = new BanachSpace(numQubits);
        GatesIntegral integral = new GatesIntegral(space);
        System.out.println("Gates integral of initial state: " + integral.compute(state));
    }

    private static String getStateString(int[] state) {
        return IntStream.of(state).mapToObj(Integer::toString).reduce("", (s1, s2) -> s1 + s2);
    }
}

class BanachSpace {
    private final int dimension;
    
    public BanachSpace(int dimension) {
        this.dimension = dimension;
    }
    
    public int getDimension() {
        return dimension;
    }
    
    public double innerProduct(int[] state1, int[] state2) {
        return IntStream.range(0, dimension).parallel()
                .mapToDouble(i -> state1[i] * state2[i])
                .sum();
    }
}

class GatesIntegral {
    private final BanachSpace space;
    
    public GatesIntegral(BanachSpace space) {
        this.space = space;
    }
    
    public double compute(int[] state) {
        double sum = IntStream.range(0, space.getDimension()).parallel()
                .mapToDouble(i -> Math.pow(space.innerProduct(state, new int[]{1, 0, 0, 0}), 2) +
                        Math.pow(space.innerProduct(state, new int[]{0, 1, 0, 0}), 2) +
                        Math.pow(space.innerProduct(state, new int[]{0, 0, 1, 0}), 2) +
                        Math.pow(space.innerProduct(state, new int[]{0, 0, 0, 1}), 2))
                .sum();
        return Math.sqrt(sum);
    }
}
