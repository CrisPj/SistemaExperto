package determinista;

public class Resultado {
    private double input[][];
    private double output[][];
    private int hiddenSize;

    public Resultado(double[][] input, double[][] output, int hiddenSize) {
        this.input = input;
        this.output = output;
        this.hiddenSize = hiddenSize;
    }

    public double[][] getInput() {
        return input;
    }

    public void setInput(double[][] input) {
        this.input = input;
    }

    public double[][] getOutput() {
        return output;
    }

    public void setOutput(double[][] output) {
        this.output = output;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public void setHiddenSize(int hiddenSize) {
        this.hiddenSize = hiddenSize;
    }
}
