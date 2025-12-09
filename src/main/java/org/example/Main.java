package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static ResourceBundle bundle = ResourceBundle.getBundle("location.messages", Locale.US);

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("Failed to set UTF-8 console output", e);
        }

        FileProcessor processor = new FileProcessor();


        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            int choice = -1;
            while (choice != 0) {
                printMenu();

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    System.err.println(bundle.getString("error.invalid_int"));
                    logger.warn("User entered non-integer input.");
                    scanner.nextLine();
                    continue;
                }

                logger.info("User selected option: {}", choice);

                switch (choice) {
                    case 1:
                        runTask1(scanner, processor);
                        break;
                    case 2:
                        runTask3(scanner, processor);
                        break;
                    case 3:
                        runTask4(scanner, processor);
                        break;
                    case 4:
                        changeLanguage(scanner);
                        break;
                    case 0:
                        System.out.println(bundle.getString("msg.exit"));
                        logger.info("Application exit requested.");
                        break;
                    default:
                        System.err.println(bundle.getString("error.invalid_choice"));
                        logger.warn("Invalid menu selection: {}", choice);
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println(bundle.getString("menu.title"));
        System.out.println(bundle.getString("menu.task1"));
        System.out.println(bundle.getString("menu.task3"));
        System.out.println(bundle.getString("menu.task4"));
        System.out.println(bundle.getString("menu.lang"));
        System.out.println(bundle.getString("menu.exit"));
        System.out.print(bundle.getString("prompt.choice"));
    }

    private static void changeLanguage(Scanner scanner) {
        System.out.print(bundle.getString("lang.prompt"));
        String langCode = scanner.nextLine().trim().toLowerCase();

        if (langCode.equals("uk")) {
            bundle = ResourceBundle.getBundle("location.messages", new Locale("uk"));
        } else {
            bundle = ResourceBundle.getBundle("location.messages", Locale.US);
        }
        System.out.println(bundle.getString("lang.success"));
        logger.info("Language changed to: {}", langCode);
    }

    private static void runTask1(Scanner scanner, FileProcessor processor) {
        System.out.print(bundle.getString("task1.prompt"));
        String textFilePath = scanner.nextLine();
        try {
            logger.debug("Starting Task 1 with file: {}", textFilePath);
            String longestLine = processor.findLongestLineByWords(textFilePath);
            System.out.println(bundle.getString("task1.result") + longestLine);
            logger.info("Task 1 completed successfully.");
        } catch (IOException e) {
            logger.error("Error in Task 1", e);
        }
    }

    private static void runTask3(Scanner scanner, FileProcessor processor) {
        System.out.print(bundle.getString("task3.prompt"));
        String key = scanner.nextLine();

        String sourceFile = "input.txt";
        String encryptedFile = "encrypted.txt";
        String decryptedFile = "decrypted.txt";

        try {
            FileProcessor.createDummyFile(sourceFile, "Hello Log4j World!");

            logger.debug("Starting encryption process...");
            processor.encryptDecrypt(sourceFile, encryptedFile, key, true);

            logger.debug("Starting decryption process...");
            processor.encryptDecrypt(encryptedFile, decryptedFile, key, false);

            System.out.println(bundle.getString("task3.done"));
            logger.info("Task 3 completed. Check files.");
        } catch (Exception e) {
            logger.error("Error in Task 3", e);
        }
    }

    private static void runTask4(Scanner scanner, FileProcessor processor) {
        System.out.print(bundle.getString("task4.prompt"));
        String urlString = scanner.nextLine();

        try {
            logger.debug("Starting Task 4 for URL: {}", urlString);
            var tagCounts = processor.countHtmlTags(urlString);
            processor.printSortedByTag(tagCounts);
            processor.printSortedByFrequency(tagCounts);
            logger.info("Task 4 completed successfully.");
        } catch (IOException e) {
            logger.error("Network/IO Error in Task 4", e);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid URL in Task 4", e);
        }
    }
}