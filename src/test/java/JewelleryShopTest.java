import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class JewelleryShopTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testMain() {
        String input = "Gold Ring\n2\n500.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        JewelleryShop.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Item Name   : Gold Ring"));
        assertTrue(output.contains("Quantity    : 2"));
        assertTrue(output.contains("Price       : 500.0"));
        assertTrue(output.contains("Total Amount: 1000.0"));
    }
}
