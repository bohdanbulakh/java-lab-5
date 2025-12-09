package org.example;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor {
    private static final Logger logger = LogManager.getLogger(FileProcessor.class);

    public String findLongestLineByWords(String filePath) throws IOException {
        String longestLine = "";
        int maxWords = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int lineCount = 0;
            while ((currentLine = reader.readLine()) != null) {
                lineCount++;
                String[] words = currentLine.trim().split("\\s+");
                int currentWordCount = (int) Arrays.stream(words).filter(w -> !w.isEmpty()).count();

                logger.debug("Line {} has {} words.", lineCount, currentWordCount);

                if (currentWordCount > maxWords) {
                    maxWords = currentWordCount;
                    longestLine = currentLine;
                }
            }
        }
        if (maxWords == -1) {
            logger.warn("File {} was empty or contained no words.", filePath);
            return "File is empty or contains no words";
        }
        return longestLine;
    }

    public void encryptDecrypt(String sourcePath, String destPath, String key, boolean encrypt) throws IOException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
        char keyChar = key.charAt(0);
        logger.debug("Cipher operation: {} with key: {}", (encrypt ? "ENCRYPT" : "DECRYPT"), keyChar);

        if (encrypt) {
            try (Reader fr = new FileReader(sourcePath);
                 Writer fw = new FileWriter(destPath);
                 CipherWriter writer = new CipherWriter(fw, keyChar)) {
                int c;
                long charsProcessed = 0;
                while ((c = fr.read()) != -1) {
                    writer.write(c);
                    charsProcessed++;
                }
                logger.debug("Encrypted {} characters to {}", charsProcessed, destPath);
            }
        } else {
            try (Reader fr = new FileReader(sourcePath);
                 DecipherReader reader = new DecipherReader(fr, keyChar);
                 Writer fw = new FileWriter(destPath)) {
                int c;
                long charsProcessed = 0;
                while ((c = reader.read()) != -1) {
                    fw.write(c);
                    charsProcessed++;
                }
                logger.debug("Decrypted {} characters to {}", charsProcessed, destPath);
            }
        }
    }

    public Map<String, Integer> countHtmlTags(String urlString) throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        Pattern tagPattern = Pattern.compile("<(\\w+)[\\s>].*?");

        logger.debug("Connecting to {}", urlString);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new URL(urlString).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = tagPattern.matcher(line);
                while (matcher.find()) {
                    String tagName = matcher.group(1).toLowerCase();
                    tagCounts.put(tagName, tagCounts.getOrDefault(tagName, 0) + 1);
                }
            }
        }
        logger.debug("Found {} unique tags.", tagCounts.size());
        return tagCounts;
    }

    public void printSortedByTag(Map<String, Integer> tags) {
        Map<String, Integer> sorted = new TreeMap<>(tags);
        System.out.println("--- Tags (Lexicographical Order) ---");
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            System.out.printf("Tag: <%s> - %d%n", entry.getKey(), entry.getValue());
        }
    }

    public void printSortedByFrequency(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());
        list.sort(Map.Entry.comparingByValue());

        System.out.println("--- Tags (Frequency Order) ---");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.printf("Tag: <%s> - %d%n", entry.getKey(), entry.getValue());
        }
    }

    public static void createDummyFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            logger.info("Created dummy file: {}", filePath);
        }
    }
}