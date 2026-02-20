import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class SimpleDriveAppTest {

    private static final String TEST_STORAGE = "DriveStorage";
    private static final String TEMP_DIR = "temp_test_dir";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEST_STORAGE));
        Files.createDirectories(Paths.get(TEMP_DIR));
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteDirectory(Paths.get(TEST_STORAGE));
        deleteDirectory(Paths.get(TEMP_DIR));
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (var stream = Files.walk(path)) {
                stream.sorted(java.util.Comparator.reverseOrder())
                      .map(Path::toFile)
                      .forEach(File::delete);
            }
        }
    }

    @Test
    void testUploadFile() throws IOException {
        Path sourceFile = Paths.get(TEMP_DIR, "test.txt");
        Files.writeString(sourceFile, "Hello World");

        String input = sourceFile.toAbsolutePath().toString() + "\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        SimpleDriveApp.uploadFile(sc);

        assertTrue(Files.exists(Paths.get(TEST_STORAGE, "test.txt")));
        assertEquals("Hello World", Files.readString(Paths.get(TEST_STORAGE, "test.txt")));
    }

    @Test
    void testListFiles() throws IOException {
        Files.writeString(Paths.get(TEST_STORAGE, "file1.txt"), "content1");
        Files.writeString(Paths.get(TEST_STORAGE, "file2.txt"), "content2");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SimpleDriveApp.listFiles();

        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains("file1.txt"));
        assertTrue(output.contains("file2.txt"));
    }

    @Test
    void testDeleteFile() throws IOException {
        Path fileToDelete = Paths.get(TEST_STORAGE, "delete_me.txt");
        Files.writeString(fileToDelete, "content");

        String input = "delete_me.txt\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        SimpleDriveApp.deleteFile(sc);

        assertFalse(Files.exists(fileToDelete));
    }

    @Test
    void testDownloadFile() throws IOException {
        Files.writeString(Paths.get(TEST_STORAGE, "download.txt"), "download content");

        String input = "download.txt\n" + TEMP_DIR + "\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        SimpleDriveApp.downloadFile(sc);

        Path downloadedFile = Paths.get(TEMP_DIR, "download.txt");
        assertTrue(Files.exists(downloadedFile));
        assertEquals("download content", Files.readString(downloadedFile));
    }

    @Test
    void testPathTraversalPrevention() throws IOException {
        Files.writeString(Paths.get(TEST_STORAGE, "secret.txt"), "secret");

        // Try to delete a file outside storage using ../
        String input = "../pom.xml\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        SimpleDriveApp.deleteFile(sc);

        assertTrue(Files.exists(Paths.get("pom.xml")), "pom.xml should not have been deleted");

        // Try to download a file outside storage using ../
        input = "../pom.xml\n" + TEMP_DIR + "\n";
        sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        SimpleDriveApp.downloadFile(sc);
        assertFalse(Files.exists(Paths.get(TEMP_DIR, "pom.xml")), "pom.xml should not have been downloaded from outside storage");
    }
}
