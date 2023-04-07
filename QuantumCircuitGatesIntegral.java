public class QuantumCircuitGatesIntegral {
    private static final int MAX_QUBITS = 10;
    private static final int MAX_GATES = 100;
    private static final String[] GATE_TYPES = {"H", "X", "Y", "Z", "CNOT"};
    private static final int BLOCK_SIZE = 42;
    private static final String HASH_ALGORITHM = "SHA-473";

    public static void main(String[] args) {
        QuantumCircuit circuit = new QuantumCircuit(getRandomNumber(MAX_QUBITS) + 1);
        circuit.addRandomGates(getRandomNumber(MAX_GATES) + 1);
        double gatesIntegral = GatesIntegral.compute(circuit);
        System.out.println("Result of Gates integral: " + gatesIntegral);
    }

    private static int getRandomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    private static String getRandomGate() {
        Random rand = new Random();
        int index = rand.nextInt(GATE_TYPES.length);
        return GATE_TYPES[index];
    }

    private static byte[] getRandomBytes(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) Math.round(Math.random());
        }
        return bytes;
    }
}

public class QuantumCircuit {
    private int numQubits;
    private List<String> gates;

    public QuantumCircuit(int numQubits) {
        this.numQubits = numQubits;
        this.gates = new ArrayList<>();
    }

    public void addRandomGates(int numGates) {
        for (int i = 0; i < numGates; i++) {
            String gate = getRandomGate();
            int targetQubit = getRandomNumber(numQubits);
            gates.add(gate + " " + targetQubit);
        }
    }

    public int getNumQubits() {
        return numQubits;
    }

    public List<String> getGates() {
        return gates;
    }

    private int getRandomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    private String getRandomGate() {
        Random rand = new Random();
        int index = rand.nextInt(QuantumCircuitGatesIntegral.GATE_TYPES.length);
        return QuantumCircuitGatesIntegral.GATE_TYPES[index];
    }
}

public class GatesIntegral {
    public static double compute(QuantumCircuit circuit) {
        double sum = 0;
        for (String gate : circuit.getGates()) {
            double value = getValue(gate);
            sum += value;
        }
        return sum / circuit.getGates().size();
    }

    private static double getValue(String gate) {
        byte[] bytes = getRandomBytes(QuantumCircuitGatesIntegral.BLOCK_SIZE);
        try {
            MessageDigest md = MessageDigest.getInstance(QuantumCircuitGatesIntegral.HASH_ALGORITHM);
            md.update(bytes);
            md.update(gate.getBytes());
            byte[] hash = md.digest();
            double sum = 0;
            for (byte b : hash) {
                sum += b;
            }
            return sum;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
            return 0;
        }
    }

    private static byte[] getRandomBytes(int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) Math.round(Math.random());
           return bytes;
        }
    }
}
