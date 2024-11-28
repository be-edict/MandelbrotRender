package de.ostfalia.reinhard.mandelbrotrender;

/**
 * @author Benedict Reinhard
 * @since 28.11.2024
 */
public class Complex {
    private final double real;
    private final double complex;

    public Complex(double real, double complex) {
        this.real = real;
        this.complex = complex;
    }

    public Complex add(Complex summand) {
        return new Complex(real + summand.real, complex + summand.complex);
    }

    public Complex subtract(Complex subtrahend) {
        return new Complex(real - subtrahend.real, complex - subtrahend.complex);
    }

    public Complex square() {
        return new Complex(real * real - complex * complex, 2 * real * complex);
    }

    public double abs() {
        return Math.sqrt(real * real + complex * complex);
    }
}
