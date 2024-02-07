package ru.otus.dataprocessor;

import com.google.gson.Gson;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String filename;

    public ResourcesFileLoader(String fileName) {
        this.filename = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        Measurement[] measurements;
        Gson gson = new Gson();
        try (InputStream inputStream =
                ResourcesFileLoader.class.getClassLoader().getResourceAsStream(filename)) {
            String jsonString = new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
            measurements = gson.fromJson(jsonString, Measurement[].class);
        }
        return Arrays.asList(measurements);
    }
}
