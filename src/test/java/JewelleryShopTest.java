import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class JewelleryShopTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restore() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testJewelleryShopMain() {
        String input = "Gold Ring\n5\n1000.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        JewelleryShop.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("Item Name   : Gold Ring"));
        assertTrue(output.contains("Quantity    : 5"));
        assertTrue(output.contains("Price       : 1000.0"));
        assertTrue(output.contains("Total Amount: 5000.0"));
    }
}
