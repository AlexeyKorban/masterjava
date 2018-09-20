package ru.javaops.masterjava.matrix;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 10, warmups = 10)
@State(Scope.Benchmark)
public class MyBenchmark {

    @Param("1000")
    public int matrixSize;

    @Param({"10", "100"})
    public int threadsNumber;
    public int[][] matrixA;
    public int[][] matrixB;

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }

    @Setup
    public void doSetup() {
        matrixA = MatrixUtil.create(matrixSize);
        matrixB = MatrixUtil.create(matrixSize);
    }

    @Benchmark
    public int[][] concurrentMultiplyStreamsBenchmark() throws ExecutionException, InterruptedException {
        return MatrixUtil.concurrentMultiplyStreams(matrixA, matrixB, threadsNumber);
    }

    @Benchmark
    public int[][] singleThreadMultiplyBenchmark() {
        return MatrixUtil.singleThreadMultiply(matrixA, matrixB);
    }

    @Benchmark
    public int[][] test() {
        return new int[10][10];
    }
}
