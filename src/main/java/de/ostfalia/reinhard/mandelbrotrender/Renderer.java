package de.ostfalia.reinhard.mandelbrotrender;

/**
 * @author Benedict Reinhard
 * @since 28.11.2024
 */
public interface Renderer {
    void render();

    void addOffset(int x, int y);

    void setOffset(int x, int y);

    int getXOffset();

    int getYOffset();

    void multiplyScale(double x, double y);

    double getXScale();

    double getYScale();
}
