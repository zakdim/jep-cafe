package com.example.jepcafe18;

import jdk.incubator.vector.IntVector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * To run this junit test add VM option in command line: --add-modules jdk.incubator.vector
 */
class VectorAPITest {


//    @Benchmark
    @Test
    void sum_arrays() {
        int[] v1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] v2 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] result = new int[v1.length];

        for (int index = 0; index < v1.length; index++) {
            result[index] = v1[index] + v2[index];
        }

        Arrays.stream(result).forEach(System.out::println);
        assertThat(result).containsExactly(11, 11, 11, 11, 11, 11, 11, 11, 11, 11);
    }

    @Test
    void sum_vectors() {
        int[] v1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] v2 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] result = new int[v1.length];
        var species = IntVector.SPECIES_PREFERRED;

        var V1 = IntVector.fromArray(species, v1, 0);
        var V2 = IntVector.fromArray(species, v2, 0);
        var RESULT = V1.add(V2);
        RESULT.intoArray(result, 0);

        System.out.println(Arrays.asList(result));
//        Arrays.stream(result).forEach(System.out::println);
//        assertThat(result).containsExactly(6, 6, 6, 6, 6);
    }
}