package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        FileProcessor processor = new FileProcessor();
        try (Scanner scanner = new Scanner(System.in)) {
            int choice = -1;
            while (choice != 0) {
                System.out.println("\n--- Lab 5 + Log4j 2 ---");
                System.out.println("1. Task 1: Find line with max words");
                System.out.println("2. Task 3: Cipher/Decipher file");
                System.out.println("3. Task 4: HTML Tag Frequency");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } else {
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
                    case 0:
                        logger.info("Application exit requested.");
                        break;
                    default:
                        logger.warn("Invalid menu selection: {}", choice);
                }
            }
        }
    }

    private static void runTask1(Scanner scanner, FileProcessor processor) {
        System.out.print("Enter path to the text file: ");
        String textFilePath = scanner.nextLine();
        try {
            logger.debug("Starting Task 1 with file: {}", textFilePath);
            String longestLine = processor.findLongestLineByWords(textFilePath);
            System.out.println("Result line: " + longestLine);
            logger.info("Task 1 completed successfully.");
        } catch (IOException e) {
            logger.error("Error in Task 1", e);
        }
    }

    private static void runTask3(Scanner scanner, FileProcessor processor) {
        System.out.print("Enter key character for encryption: ");
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

            logger.info("Task 3 completed. Check files.");
        } catch (Exception e) {
            logger.error("Error in Task 3", e);
        }
    }

    private static void runTask4(Scanner scanner, FileProcessor processor) {
        System.out.print("Enter URL to analyze: ");
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