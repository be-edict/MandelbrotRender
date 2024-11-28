package de.ostfalia.reinhard.mandelbrotrender;

import java.util.LinkedList;

/**
 * @author Benedict Reinhard
 * @since 28.11.2024
 */
public class MandelbrotRenderer implements Renderer {

    private final static int coreCount = Runtime.getRuntime().availableProcessors();

    private final int[] img;
    private final int width;
    private final int height;

    private int xOffset;
    private int yOffset;
    private double xFactor;
    private double yFactor;

    private int maxIterations;


    public MandelbrotRenderer(final int[] img, int width, int height, int maxIterations) {
        this.img = img;
        this.width = width;
        this.height = height;

        this.maxIterations = maxIterations;

        xOffset = -width / 2;
        yOffset = -height / 2;
        xFactor = 5.0 / width;
        yFactor = 5.0 / height;
    }


    private void renderMandelbrotSet() {
        LinkedList<Thread> threadList = new LinkedList<>();
        Thread thread;
        int pxPerThread = img.length / coreCount;
        for (int i = 0; i < coreCount; i++) {
            int from = i * pxPerThread;
            int to = (i == coreCount - 1) ? img.length : from + pxPerThread;
            thread = new Thread(() -> calcImage(from, to));
            threadList.add(thread);
            thread.start();
        }
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void calcImage(int from, int to) {
        Complex z;
        Complex c;
        double xPos;
        double yPos;

        int iterations;

        for (int i = from; i < to; i++) {
            z = new Complex(0, 0);
            xPos = xFactor * ((i % width) + xOffset);
            yPos = yFactor * (i / width + yOffset);
            c = new Complex(xPos, yPos);
            iterations = 0;
            while (iterations++ < maxIterations && z.abs() < 2) {
                z = z.square().add(c);
            }
            if (z.abs() < 2) {
                img[i] = 0xff000000;
            } else {
                img[i] = (int) (0xff000000 + ((double) iterations / maxIterations) * 0xaa) + 0x55;
            }
        }
    }


    @Override
    public void addOffset(int x, int y) {
        xOffset += x;
        yOffset += y;
    }

    @Override
    public void setOffset(int x, int y) {
        xOffset = x;
        yOffset = y;
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }

    @Override
    public void multiplyScale(double x, double y) {
        xFactor *= x;
        yFactor *= y;
        maxIterations = (int) Math.max(50, 50 * Math.log(1 / Math.min(xFactor, yFactor)));
    }

    @Override
    public double getXScale() {
        return xFactor;
    }

    @Override
    public double getYScale() {
        return yFactor;
    }

    @Override
    public void render() {
        renderMandelbrotSet();
    }
}
