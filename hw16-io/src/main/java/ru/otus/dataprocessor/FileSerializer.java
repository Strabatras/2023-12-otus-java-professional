package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try {
            writeSerializeData(data);
        } catch (Exception ex) {
            throw new FileProcessException(ex);
        }
    }

    private void writeSerializeData(Map<String, Double> data) throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName))) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, data);
        }
    }
}
