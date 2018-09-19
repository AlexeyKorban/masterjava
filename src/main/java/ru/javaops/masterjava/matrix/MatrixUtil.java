package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[][] matrixBT = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixBT[j][i] = matrixB[i][j];
            }
        }

        final int divider = 10;
        final int size = matrixSize / divider;

        List<MatrixContainer> containers = new ArrayList<>();
        int matrixCounter = 0;
        for (int i = 0; i < divider; i++) {
            for (int j = 0; j < divider; j++) {
                int[][] rowsA = copyRows(matrixA, i * size, size);
                int[][] rowsB = copyRows(matrixBT, j * size, size);
                containers.add(new MatrixContainer(matrixCounter, rowsA, rowsB));
                matrixCounter++;
            }
        }

        List<Future<MatrixContainer>> futures = new ArrayList<>();
        for (MatrixContainer matrixContainer : containers) {
            futures.add(executor.submit(new Callable<MatrixContainer>() {
                @Override
                public MatrixContainer call() throws Exception {
                    int[][] resultMatrix = new int[size][size];
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            resultMatrix[i][j] = multiplyRows(matrixContainer.rowsA[i], matrixContainer.rowsB[j]);
                        }
                    }
                    return new MatrixContainer(matrixContainer.order, resultMatrix);
                }

                private int multiplyRows(int[] rowA, int[] rowB) {
                    int sum = 0;
                    for (int i = 0; i < rowA.length; i++) {
                        sum += rowA[i] * rowB[i];
                    }
                    return sum;
                }
            }));
        }

        List<MatrixContainer> results = new ArrayList<>();

        for (Future<MatrixContainer> future : futures) {
            MatrixContainer container = future.get();
            results.add(new MatrixContainer(container.order, container.resultMatrix));
        }

        matrixCounter = 0;
        for (int i = 0; i < divider; i++) {
            for (int j = 0; j < divider; j++) {
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        matrixC[i * size + x][j * size + y] = results.get(matrixCounter).resultMatrix[x][y];
                    }
                }
                matrixCounter++;
            }
        }
        return matrixC;
    }

    public static int[][] copyRows(int[][] matrixA, int startI, int size) {
        int[][] resultRows = new int[size][matrixA.length];
        for (int i = 0, j = startI; i < size; i++, j++) {
            resultRows[i] = matrixA[j];
        }
        return resultRows;
    }

    public static int[][] copy(int[][] matrixA, int startI, int startJ, int size) {
        int[][] resultMatrix = new int[size][size];
        for (int i = 0, i2 = startI; i < size; i++, i2++) {
            for (int j = 0, j2 = startJ; j < size; j++, j2++) {
                resultMatrix[i][j] = matrixA[i2][j2];
            }
        }
        return resultMatrix;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int thatColumn[] = new int[matrixSize];

        try {
            for (int j = 0; ; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < matrixSize; i++) {
                    int thisRow[] = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return matrixC;
    }

    public static int[][] singleThreadMultiplyOld(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
