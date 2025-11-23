package org.example;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileProcessor processor = new FileProcessor();

        try (Scanner scanner = new Scanner(System.in)) {
            int choice = -1;

            System.out.println("\n--- Select Task ---");
            System.out.println("1. Run Task 1 (Find longest line by words)");
            System.out.println("2. Run Task 3 (Character Stream Cipher)");
            System.out.println("3. Run Task 4 (Count HTML Tags)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }

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
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.err.println("Invalid choice. Please select from the menu.");
            }
        }
    }

    private static void runTask1(Scanner scanner, FileProcessor processor) {
        System.out.println("\n--- Task 1: Find the line with maximum words ---");
        System.out.print("Enter path to the text file: ");
        String textFilePath = scanner.nextLine();

        try {
            String longestLine = processor.findLongestLineByWords(textFilePath);
            System.out.println("Result: " + longestLine);
        } catch (IOException e) {
            System.err.println("ERROR (Task 1): Failed to read file. " + e.getMessage());
        }
    }

    private static void runTask3(Scanner scanner, FileProcessor processor) {
        System.out.println("\n--- Task 3: Character Stream Encryption/Decryption ---");
        System.out.print("Enter key character for encryption (e.g., 'A'): ");
        String key = scanner.nextLine();

        String sourceEncrypt = "input.txt";
        String destEncrypt = "encrypted.txt";
        String destDecrypt = "decrypted.txt";

        try {
            FileProcessor.createDummyFile(sourceEncrypt, "This is the source text to be encrypted.");

            processor.encryptDecrypt(sourceEncrypt, destEncrypt, key, true);

            processor.encryptDecrypt(destEncrypt, destDecrypt, key, false);

        } catch (Exception e) {
            System.err.println("ERROR (Task 3): An error occurred during cipher process. " + e.getMessage());
        }
    }

    private static void runTask4(Scanner scanner, FileProcessor processor) {
        System.out.println("\n--- Task 4: Count HTML Tags Frequency ---");
        System.out.print("Enter URL to analyze: ");
        String urlString = scanner.nextLine();

        try {
            Map<String, Integer> tagCounts = processor.countHtmlTags(urlString);

            processor.printSortedByTag(tagCounts);
            System.out.println();
            processor.printSortedByFrequency(tagCounts);

        } catch (IOException e) {
            System.err.println("ERROR (Task 4): Failed to open or read the URL/network error. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR (Task 4): Check the correctness of the entered URL. " + e.getMessage());
        }
    }
}