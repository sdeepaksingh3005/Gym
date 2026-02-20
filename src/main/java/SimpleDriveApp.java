import java.io.IOException;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SimpleDriveApp {

    static final String STORAGE = "DriveStorage";

    public static void main(String[] args) {
        try {
            // Create storage folder if not exists
            Files.createDirectories(Paths.get(STORAGE));
        } catch (IOException e) {
            System.err.println("Fatal Error: Could not create storage directory: " + e.getMessage());
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== Simple Drive App =====");
                System.out.println("1. Upload File");
                System.out.println("2. List Files");
                System.out.println("3. Download File");
                System.out.println("4. Delete File");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");

                int choice;
                try {
                    choice = sc.nextInt();
                    sc.nextLine(); // clear buffer
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number.");
                    sc.nextLine(); // clear buffer
                    continue;
                }

                switch (choice) {
                    case 1 -> uploadFile(sc);
                    case 2 -> listFiles();
                    case 3 -> downloadFile(sc);
                    case 4 -> deleteFile(sc);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    static void uploadFile(Scanner sc) {
        System.out.print("Enter full path of file to upload: ");
        String pathInput = sc.nextLine();

        Path source = Paths.get(pathInput);
        if (!Files.exists(source) || Files.isDirectory(source)) {
            System.out.println("Error: Source file does not exist or is a directory.");
            return;
        }

        // Use getFileName() to prevent path traversal
        Path fileName = source.getFileName();
        if (fileName == null) {
            System.out.println("Error: Invalid file name.");
            return;
        }
        Path target = Paths.get(STORAGE, fileName.toString());

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File uploaded successfully!");
        } catch (IOException e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }

    static void listFiles() {
        System.out.println("\nStored Files:");
        try (var stream = Files.list(Paths.get(STORAGE))) {
            stream.forEach(p -> System.out.println(p.getFileName()));
        } catch (IOException e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }

    static void downloadFile(Scanner sc) {
        System.out.print("Enter file name to download: ");
        String nameInput = sc.nextLine();

        // Sanitize input to prevent path traversal
        Path namePath = Paths.get(nameInput).getFileName();
        if (namePath == null) {
            System.out.println("Error: Invalid file name.");
            return;
        }
        String name = namePath.toString();

        Path source = Paths.get(STORAGE, name);
        if (!Files.exists(source)) {
            System.out.println("Error: File not found in storage.");
            return;
        }

        System.out.print("Enter destination folder path: ");
        String dest = sc.nextLine();
        Path destDir = Paths.get(dest);

        if (!Files.exists(destDir) || !Files.isDirectory(destDir)) {
            System.out.println("Error: Destination folder does not exist or is not a directory.");
            return;
        }

        Path target = destDir.resolve(name);

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            System.out.println("Error downloading file: " + e.getMessage());
        }
    }

    static void deleteFile(Scanner sc) {
        System.out.print("Enter file name to delete: ");
        String nameInput = sc.nextLine();

        // Sanitize input to prevent path traversal
        Path namePath = Paths.get(nameInput).getFileName();
        if (namePath == null) {
            System.out.println("Error: Invalid file name.");
            return;
        }
        String name = namePath.toString();

        Path path = Paths.get(STORAGE, name);
        try {
            if (Files.deleteIfExists(path)) {
                System.out.println("File deleted successfully!");
            } else {
                System.out.println("Error: File not found.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }
}
