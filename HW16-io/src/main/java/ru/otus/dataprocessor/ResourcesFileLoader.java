package ru.otus.dataprocessor;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        List<Measurement> measurementList;
        try {
            measurementList = createMeasurementList(fileName);
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
        return measurementList;
    }

    private List<Measurement> createMeasurementList(String fileName) throws IOException {
        List<Measurement> measurementList;
        try (InputStream inputStream = this.getClass().getResourceAsStream("/" + fileName)) {
            if (isNull(inputStream)) {
                throw new IOException("Can not read file");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            measurementList = objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
        return measurementList;
    }
}
