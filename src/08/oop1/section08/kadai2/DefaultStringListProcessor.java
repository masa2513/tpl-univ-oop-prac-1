package oop1.section08.kadai2;

import java.util.List;

public class DefaultStringListProcessor implements StringListProcessor {
    @Override
    public String concatenateAndUppercase(List<String> texts) throws InvalidCollectionDataException, EmptyCollectionException, NullItemInCollectionException {
        if (texts == null) {
            throw new InvalidCollectionDataException("Input list of strings cannot be null.");
        }

        if (texts.isEmpty()) {
            throw new EmptyCollectionException("Input list of strings cannot be empty.");
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            if (text == null) {
                throw new NullItemInCollectionException("List of strings contains a null item.", i);
            }
            result.append(text);
        }

        return result.toString().toUpperCase();
    }
} 