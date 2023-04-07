import java.util.Random;

public class QCDSimulation {
    public static void main(String[] args) {
        Random rand = new Random();
        int numQubits = 4;
        int[] state = new int[numQubits];
        for (int i = 0; i < numQubits; i++) {
            state[i] = rand.nextInt(2);
        }
        System.out.println("Initial state: " + getStateString(state));
        int index = rand.nextInt(numQubits);
        state[index] = 1;
        System.out.println("Final state: " + getStateString(state));
        
        Complex[] superposition = new Complex[]{new Complex(1/Math.sqrt(2), 0), new Complex(0, 1/Math.sqrt(2))};
        QuantumState qstate = new QuantumState(numQubits);
        qstate.applyGate(new HadamardGate(), 0);
        qstate.applyGate(new CNOTGate(), 0, 1);
        System.out.println("Resulting state: " + qstate.getStateString());
    }

    private static String getStateString(int[] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++) {
            sb.append(state[i]);
        }
        return sb.toString();
    }
}

class Complex {
    private final double real;
    private final double imag;
    
    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    
    public double getReal() {
        return real;
    }
    
    public double getImag() {
        return imag;
    }
}

class QuantumState {
    private final int numQubits;
    private final Complex[] amplitudes;
    
    public QuantumState(int numQubits) {
        this.numQubits = numQubits;
        this.amplitudes = new Complex[1 << numQubits];
        for (int i = 0; i < amplitudes.length; i++) {
            amplitudes[i] = new Complex(0, 0);
        }
        amplitudes[0] = new Complex(1, 0);
    }
    
    public void applyGate(QuantumGate gate, int... targets) {
        Complex[][] matrix = gate.getMatrix();
        int[] bits = new int[numQubits];
        for (int i = 0; i < targets.length; i++) {
            bits[targets[i]] = 1;
        }
        int mask = (1 << numQubits) - 1;
        for (int i = 0; i < amplitudes.length; i++) {
            if ((i & mask) == (mask & applyMask(bits, i))) {
                Complex sum = new Complex(0, 0);
                for (int j = 0; j < matrix.length; j++) {
                    sum = addComplex(sum, multiplyComplex(matrix[j][(i >> (targets.length * j)) & ((1 << targets.length) - 1)],
                            amplitudes[i & mask]));
                }
                amplitudes[i & mask] = sum;
            }
        }
    }
    
    public String getStateString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amplitudes.length; i++) {
          sb.append(String.format("%.3f", amplitudes[i].getReal()));
          if (amplitudes[i].getImag() >= 0) {
            sb.append("+");
          }
            sb.append(String.format("%.3fi", amplitudes[i].getImag()));
          if (i < amplitudes.length - 1) {
            sb.append(", ");
          }
        }
        return sb.toString();
    }

private static int applyMask(int[] bits, int value) {
    int result = 0;
    for (int i = 0; i < bits.length; i++) {
        if (bits[i] == 1) {
            result |= (1 << i);
        } else {
            result |= ((value >> i) & 1) << i;
        }
    }
    return result;
}

private static Complex addComplex(Complex c1, Complex c2) {
    return new Complex(c1.getReal() + c2.getReal(), c1.getImag() + c2.getImag());
}

private static Complex multiplyComplex(Complex c1, Complex c2) {
    return new Complex(c1.getReal() * c2.getReal() - c1.getImag() * c2.getImag(),
            c1.getReal() * c2.getImag() + c1.getImag() * c2.getReal());
}
  
interface QuantumGate {
  Complex[][] getMatrix();
}

class HadamardGate implements QuantumGate {
  public Complex[][] getMatrix() {
    Complex[][] matrix = new Complex[2][2];
    matrix[0][0] = new Complex(1/Math.sqrt(2), 0);
    matrix[0][1] = new Complex(1/Math.sqrt(2), 0);
    matrix[1][0] = new Complex(1/Math.sqrt(2), 0);
    matrix[1][1] = new Complex(-1/Math.sqrt(2), 0);
    return matrix;
  }
}

class CNOTGate implements QuantumGate {
  public Complex[][] getMatrix() {
    Complex[][] matrix = new Complex[4][4];
    matrix[0][0] = new Complex(1, 0);
    matrix[0][1] = new Complex(0, 0);
    matrix[0][2] = new Complex(0, 0);
    matrix[0][3] = new Complex(0, 0);
    matrix[1][0] = new Complex(0, 0);
    matrix[1][1] = new Complex(1, 0);
    matrix[1][2] = new Complex(0, 0);
    matrix[1][3] = new Complex(0, 0);
    matrix[2][0] = new Complex(0, 0);
    matrix[2][1] = new Complex(0, 0);
    matrix[2][2] = new Complex(0, 0);
    matrix[2][3] = new Complex(1, 0);
    matrix[3][0] = new Complex(0, 0);
    matrix[3][1] = new Complex(0, 0);
    matrix[3][2] = new Complex(1, 0);
    matrix[3][3] = new Complex(0, 0);
    return matrix;
  }
}
