public class QuantumSuperposition {

    public static void main(String[] args) {
        int[] qubits = {0, 1};
        double[] amplitudes = {0.707, 0.707};
        double[] probabilities = {0.5, 0.5};

        for (int i = 0; i < 100; i++) {
            applyHadamardGate(qubits, amplitudes, probabilities);
            applyPhaseShift(qubits, amplitudes, probabilities);
            measureQubits(qubits, amplitudes, probabilities);
        }
    }

    private static void applyHadamardGate(int[] qubits, double[] amplitudes, double[] probabilities) {
        for (int i = 0; i < qubits.length; i++) {
            int q = qubits[i];
            double amp = amplitudes[i];
            double prob = probabilities[i];

            double newAmp = amp / Math.sqrt(2);
            double newProb = (prob + newAmp * newAmp) / 2;

            amplitudes[i] = newAmp;
            probabilities[i] = newProb;
        }
    }

    private static void applyPhaseShift(int[] qubits, double[] amplitudes, double[] probabilities) {
        double phase = Math.PI / 4;
        for (int i = 0; i < qubits.length; i++) {
            int q = qubits[i];
            double amp = amplitudes[i];
            double prob = probabilities[i];

            double newAmp = amp * Math.cos(phase);
            double newProb = prob * Math.sin(phase);

            amplitudes[i] = newAmp;
            probabilities[i] = newProb;
        }
    }

    private static void measureQubits(int[] qubits, double[] amplitudes, double[] probabilities) {
        double prob0 = probabilities[0];
        double prob1 = probabilities[1];
        double random = Math.random();
        if (random < prob0) {
            System.out.println("Measured value: 0");
        } else {
            System.out.println("Measured value: 1");
        }
    }
}
