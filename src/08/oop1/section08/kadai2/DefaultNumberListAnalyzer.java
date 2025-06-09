package oop1.section08.kadai2;

import java.util.List;

public class DefaultNumberListAnalyzer implements NumberListAnalyzer {
    @Override
    public int findMaximumValue(List<Integer> numbers) throws InvalidCollectionDataException, EmptyCollectionException, NullItemInCollectionException {
        if (numbers == null) {
            throw new InvalidCollectionDataException("Input list cannot be null.");
        }
        
        if (numbers.isEmpty()) {
            throw new EmptyCollectionException("Input list cannot be empty.");
        }

        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < numbers.size(); i++) {
            Integer number = numbers.get(i);
            if (number == null) {
                throw new NullItemInCollectionException("List contains a null item.", i);
            }
            maxValue = Math.max(maxValue, number);
        }

        return maxValue;
    }
} 