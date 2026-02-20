import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JewelleryShopTest {

    @Test
    void testMain() {
        String input = "Gold Ring\n2\n500.0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        JewelleryShop.main(new String[]{});

        String output = out.toString();

        assertTrue(output.contains("===== JEWELLERY SHOP BILL ====="));
        assertTrue(output.contains("Item Name   : Gold Ring"));
        assertTrue(output.contains("Quantity    : 2"));
        assertTrue(output.contains("Price       : 500.0"));
        assertTrue(output.contains("Total Amount: 1000.0"));
    }
}
