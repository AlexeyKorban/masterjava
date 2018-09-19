package ru.javaops.masterjava.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixTest {
    private static final int THREAD_NUMBER = 10;
    private final static ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    public static void main(String[] args) throws Exception{
        final int matrixSize = 16;
        int[][] matrixA = MatrixUtil.create(matrixSize);
        int[][] matrixB = MatrixUtil.create(matrixSize);

        int[][] matrixBT = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixBT[j][i] = matrixB[i][j];
            }
        }

        int[][] result1 = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
        int[][] result2 = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);

        draw(result1);
        System.out.println();
        draw(result2);




    }

    public static void draw(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int multiplyRows(int[] rowA, int[] rowB) {
        int sum = 0;
        for (int i = 0; i < rowA.length; i++) {
            sum += rowA[i] * rowB[i];
        }
        return sum;
    }
}
