package org.example;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MultidimensionalArray {
    private final int[] dimensions;
    private final int[][] intervals;
    private final int[] B;
    private final int[][] iliffeVectors;

    public MultidimensionalArray(int[] dimensions, int[][] intervals) {
        this.dimensions = dimensions;
        this.intervals = intervals;
        int size = IntStream.of(dimensions).reduce(1, (a, b) -> a * b);
        this.B = new int[size];
        this.iliffeVectors = getIliffeVectors();
    }

    public int directAccess(int... indexes) {
        int goalIndex = indexes[indexes.length - 1];

        for (int i = indexes.length - 2; i >= 0; i--) {
            int currentProduct = indexes[i];
            for (int j = i + 1; j < indexes.length; j++) {
                currentProduct *= dimensions[j];
            }
            goalIndex += currentProduct;
        }
        return B[goalIndex];
        /*int goalIndex = indexes[indexes.length - 1];
        goalIndex += IntStream.range(0, indexes.length - 1)
                .map(i -> indexes[i] * Arrays.stream(
                                dimensions, i + 1, indexes.length
                        ).reduce(1, (a, b) -> a * b)
                )
                .sum();
        return B[goalIndex];*/
    }

    public int getByIliffe(int... indexes) {
        int goalIndex = 0;
        for (int i = 0; i < iliffeVectors.length; i++) {
            goalIndex += iliffeVectors[i][indexes[i]];
        }
        return goalIndex;
        /*return IntStream.range(0, iliffeVectors.length)
                .map(i -> iliffeVectors[i][indexes[i]])
                .sum();*/
    }

    private int[][] getIliffeVectors() {
        int[][] vectors = new int[dimensions.length][];
        /*int prevInterval;
        int interval = B.length;
        int times;
        for (int i = 0; i < vectors.length; i++) {
            times = B.length / interval;
            prevInterval = interval;
            interval /= dimensions[i];
            for (int j = 0; j < dimensions[i]; j++) {
                for (int k = 0; k < times; k++) {
                    Arrays.fill(vectors[i], k * prevInterval + j * interval,
                            k * prevInterval + j * interval + interval, j);
                }
            }
        }*/
        /*int size = B.length;
        for (int i = vectors.length - 1; i >= 0; i--) {
            vectors[i] = new int[size];
            size /= dimensions[i];
        }
        int interval = B.length;
        int times;
        for (int i = 0; i < vectors.length; i++) {
            times = B.length / interval;
            interval /= dimensions[i];
            for (int j = 0; j < dimensions[i]; j++) {
                for (int k = 0; k < times; k++) {
                    vectors[i][k * dimensions[i] + j] = j;
                }
            }

        }*/
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new int[dimensions[i]];
        }
        int interval = B.length;
        for (int i = 0; i < vectors.length; i++) {
            interval /= dimensions[i];
            for (int j = 0; j < dimensions[i]; j++) {
                vectors[i][j] = j * interval;
            }
        }
        return vectors;
    }

    public static void main(String[] args) {
        int[] dimensions = new int[]{10, 20, 30};  //{10, 20, 30}// размерность массива
        int[][] intervals = new int[][]{{0, 9}, {0, 19}, {0, 29}};  //{{0, 9}, {0, 19}, {0, 29}}// интервалы изменения индексов
        MultidimensionalArray array = new MultidimensionalArray(dimensions, intervals);

        // заполнение массива
        for (int i = 0; i < array.B.length; i++) {
            array.B[i] = i;
        }

        // прямой доступ
        long start = System.nanoTime();
        int val1 = array.directAccess(4, 5, 6); // 4, 5, 6
        long end = System.nanoTime();
        System.out.println("Прямой доступ: " + val1 + ", время: " + (end - start) + " нс");
        int memoryUsage = Integer.BYTES;
        System.out.println("Memory usage: " + memoryUsage + " bytes");

        // доступ по векторам Айлиффа
        start = System.nanoTime();
        int val2 = array.getByIliffe(4, 5, 6); // 4, 5, 6
        end = System.nanoTime();
        System.out.println("Доступ по векторам Айлиффа: " + val2 + ", время: " + (end - start) + " нс");
        memoryUsage = Arrays.stream(dimensions).sum() * Integer.BYTES;
        System.out.println("Memory usage: " + memoryUsage + " bytes");

        // определяющие векторы
//        start = System.nanoTime();
//        int val3 = array.fftAccess(4, 5, 6);
//        end = System.nanoTime();
//        System.out.println("Доступ с помощью определяющих векторов: " + val3 + ", время: " + (end - start) + " нс");
    }
}
