package de.osfalia.reinhard.pixeldrawtest;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    private final int stageWidth = 800;
    private final int stageHeight = 700;
    private final int[] img = new int[stageWidth * stageHeight];

    private PixelWriter pixelWriter;
    private int maxIterations = 600;

    Renderer r;

    private double dragStartX = -1;
    private double dragStartY = -1;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(getClass().getName());
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        stage.setResizable(false);

        Canvas canvas = new Canvas(stageWidth, stageHeight);
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        root.setOnMousePressed(e -> {
            dragStartX = e.getX();
            dragStartY = e.getY();
        });
        root.setOnMouseDragged(e -> {
            if (dragStartY == -1 || dragStartX == -1) {
                return;
            }
            r.addOffset((int) -(e.getX() - dragStartX), (int) -(e.getY() - dragStartY));
            dragStartX = e.getX();
            dragStartY = e.getY();
            drawCanvas();
        });
        root.setOnScroll(e -> {
            double oldXScale = r.getXScale();
            double oldYScale = r.getYScale();
            double factor = e.getDeltaY() < 0 ? 4.0 / 3 : 3.0 / 4;
            double mouseX = (e.getX() + r.getXOffset()) * oldXScale;
            double mouseY = (e.getY() + r.getYOffset()) * oldYScale;

            r.multiplyScale(factor, factor);

            double newXScale = r.getXScale();
            double newYScale = r.getYScale();
            int newXOffset = (int) (mouseX / newXScale - e.getX());
            int newYOffset = (int) (mouseY / newYScale - e.getY());

            r.setOffset(newXOffset, newYOffset);

            drawCanvas();
        });


        r = new MandelbrotRenderer(img, stageWidth, stageHeight, maxIterations);
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        drawCanvas();
        stage.show();
    }

    private void drawCanvas() {
        r.render();
        pixelWriter.setPixels(0, 0, stageWidth, stageHeight, PixelFormat.getIntArgbInstance(), img, 0, stageWidth);
    }

    private int[] noiseImg() {
        Random r = new Random();
        int[] img = new int[stageWidth * stageHeight];
        for (int i = 0; i < img.length; i++) {
            img[i] = 0xff000000 + r.nextInt(0x00ffffff);
        }
        return img;
    }
}
