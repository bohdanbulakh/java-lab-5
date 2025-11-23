package org.example;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor {
    public String findLongestLineByWords(String filePath) throws IOException {
        String longestLine = "";
        int maxWords = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] words = currentLine.trim().split("\\s+");

                int currentWordCount = (int) Arrays.stream(words).filter(word -> !word.isEmpty()).count();

                if (currentWordCount > maxWords) {
                    maxWords = currentWordCount;
                    longestLine = currentLine;
                }
            }
        }

        if (maxWords == -1) {
            return "This file does not contain words";
        }

        return longestLine;
    }

    public void encryptDecrypt(String sourcePath, String destPath, String key, boolean encrypt) throws IOException {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }

        char keyChar = key.charAt(0);

        try {
            if (encrypt) {
                try (Reader fr = new FileReader(sourcePath);
                     Writer fw = new FileWriter(destPath);
                     CipherWriter writer = new CipherWriter(fw, keyChar)) {
                    int character;
                    while ((character = fr.read()) != -1) {
                        writer.write(character);
                    }
                    System.out.println("File successfully encrypted: " + destPath);
                }
            } else {
                try (Reader fr = new FileReader(sourcePath);
                     DecipherReader reader = new DecipherReader(fr, keyChar);
                     Writer fw = new FileWriter(destPath)) {
                    int character;
                    while ((character = reader.read()) != -1) {
                        fw.write(character);
                    }
                    System.out.println("File successfully decrypted: " + destPath);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error during encrypting/decrypting: " + e.getMessage(), e);
        }
    }

    public Map<String, Integer> countHtmlTags(String urlString) throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        Pattern tagPattern = Pattern.compile("<(\\w+)[\\s>/]");

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
        return tagCounts;
    }


    public void printSortedByTag(Map<String, Integer> tags) {
        Map<String, Integer> sorted = new TreeMap<>(tags);

        System.out.println("--- Sorting by tags ---");
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            System.out.printf("Tag: <%s>, Frequency: %d%n", entry.getKey(), entry.getValue());
        }
    }

    public void printSortedByFrequency(Map<String, Integer> tags) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tags.entrySet());

        list.sort(Comparator.comparingInt(Map.Entry::getValue));

        System.out.println("--- Sorting by frequency ---");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.printf("Tag: <%s>, Frequency: %d%n", entry.getKey(), entry.getValue());
        }
    }

    public static void createDummyFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Dummy input file created: " + filePath);
        }
    }
}