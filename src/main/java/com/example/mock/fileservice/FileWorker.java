package com.example.mock.fileservice;

import com.example.mock.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileWorker {

    @Value("${file.data.path}")
    private String dataPath;

    private final ObjectMapper objectMapper;

    public FileWorker(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeUserToFile(User user) throws IOException {
        try {
            Path path = Paths.get(dataPath + "/" + user.getLogin() + ".json");
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            objectMapper.writeValue(new File(dataPath + "/" + user.getLogin() + ".json"), user);
        } catch (IOException e) {
            throw new IOException("Error while writing user to file: " + e.getMessage(), e);
        }
    }


    public String readRandomLineFromFile() throws IOException {
        try {
            Path path = Paths.get(dataPath + "/random_users.txt");
            if (!Files.exists(path)) {
                createRandomUsersFile(10);
            }

            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return "File is empty";
            }

            Random random = new Random();
            int randomIndex = random.nextInt(lines.size());
            return lines.get(randomIndex);

        } catch (IOException e) {
            throw new IOException("Error while reading file: " + e.getMessage(), e);
        }
    }


    public void createRandomUsersFile(int numberOfLines) throws IOException {
        try {
            Path path = Paths.get(dataPath + "/random_users.txt");
            Files.createDirectories(path.getParent());
            Files.createFile(path);

            List<String> randomStrings = getRandomUsers(numberOfLines);
            Files.write(path, randomStrings, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Error while generating file");
        }
    }


    private List<String> getRandomUsers(int numberOfLines) {
        return Stream.generate(() -> {
                    try {
                        User user = new User(
                                "random-user" + new Random().nextInt(100),
                                "password" + new Random().nextInt(100),
                                "random-user" + new Random().nextInt(100) + "@example.com",
                                new Date(System.currentTimeMillis())
                        );
                        return objectMapper.writeValueAsString(user);
                    } catch (Exception e) {
                        return "{}";
                    }
                })
                .limit(numberOfLines)
                .collect(Collectors.toList());
    }

}
