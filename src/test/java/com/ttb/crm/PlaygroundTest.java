package com.ttb.crm;

import org.junit.jupiter.api.Test;

public class PlaygroundTest {

    @Test
    void binarySearchTest() {
        int[] sortedArray = {0, 1, 2, 4, 6, 78, 1000000000};
        int target = 90000;

        int closestValue = findClosest(sortedArray, target, 0, sortedArray.length - 1);
        System.out.println("closestValue = " + closestValue);
    }

    private int findClosest(int[] numbers, int expectedNumber, int leftIndex, int rightIndex) {
        if (leftIndex > rightIndex) {
            int leftNumber = leftIndex < numbers.length ? numbers[leftIndex] : numbers[numbers.length - 1];
            int rightNumber = rightIndex >= 0 ? numbers[rightIndex] : numbers[0];

            int diffLeft = leftNumber > expectedNumber ? leftNumber - expectedNumber : expectedNumber - leftNumber;
            int diffRight = rightNumber > expectedNumber ? rightNumber - expectedNumber : expectedNumber - rightNumber;

            return diffLeft < diffRight ? leftNumber : rightNumber;
        }

        int midIndex = leftIndex + ((rightIndex - leftIndex) / 2);

        if (numbers[midIndex] == expectedNumber) return numbers[midIndex];

        if (numbers[midIndex] < expectedNumber)
            return findClosest(numbers, expectedNumber, midIndex + 1, rightIndex);
        else
            return findClosest(numbers, expectedNumber, leftIndex, midIndex - 1);
    }
}
