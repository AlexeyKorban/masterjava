package ru.javaops.masterjava.matrix;

public class MatrixContainer {
    int order;
    int[][] resultMatrix;
    int[][] rowsA;
    int[][] rowsB;

    public MatrixContainer(int order, int[][] rowsA, int[][] rowsB) {
        this.order = order;
        this.rowsA = rowsA;
        this.rowsB = rowsB;
    }

    public MatrixContainer(int order, int[][] resultMatrix) {
        this.order = order;
        this.resultMatrix = resultMatrix;
    }
}
