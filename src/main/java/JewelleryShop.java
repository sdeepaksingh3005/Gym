import java.util.Scanner;

public class JewelleryShop {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String itemName;
        int quantity;
        double price, total;

        System.out.println("===== JEWELLERY SHOP BILL =====");

        System.out.print("Enter Jewellery Name: ");
        itemName = sc.nextLine();

        System.out.print("Enter Quantity: ");
        quantity = sc.nextInt();

        System.out.print("Enter Price per Item: ");
        price = sc.nextDouble();

        total = quantity * price;

        System.out.println("\n----- BILL DETAILS -----");
        System.out.println("Item Name   : " + itemName);
        System.out.println("Quantity    : " + quantity);
        System.out.println("Price       : " + price);
        System.out.println("Total Amount: " + total);

        sc.close();
    }
}
