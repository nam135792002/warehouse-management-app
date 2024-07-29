package vn.edu.likelion.app;

import vn.edu.likelion.entity.User;
import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.impl.BranchServiceImpl;
import vn.edu.likelion.service.impl.ProductServiceImpl;
import vn.edu.likelion.service.impl.UserServiceImpl;

import java.util.Scanner;

public class WarehouseManagementApp {
    public static User user = null;
    public static UserServiceImpl userService = new UserServiceImpl();
    public static BranchServiceImpl branchService = new BranchServiceImpl();
    public static ProductServiceImpl productService = new ProductServiceImpl();
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        while (true){
            try {
                System.out.println("\t\t>>> WAREHOUSE MANAGEMENT SYSTEM <<<");
                System.out.println("\t\t\t\t1. Login");
                System.out.println("\t\t\t\t2. Exit");
                System.out.print("\t--> Enter your choice: ");
                String input = sc.nextLine();
                int select = Integer.parseInt(input);

                if(select == 1){
                    user = userService.login();
                    if(user != null){
                        if (user.getRole().equals("Admin")){
                            try {
                                menuOfAdmin();
                            } catch (NotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }else{
                            try {
                                menuOfManager();
                            } catch (NotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    else System.out.println("--> Username or password is invalid!");
                } else if (select == 2) {
                    break;
                }else {
                    System.out.println("--> Invalid your choice. Please, choice again!");
                }
            }catch (NumberFormatException ex){
                System.out.println("--> Invalid your choice. Please, choice again!");
            }
        }
    }

    public static void menuOfAdmin() throws NotFoundException {
        while (true){
            try {
                // Define menu items
                String[] userMenu = {
                        "1. Create a manager",
                        "2. List of managers",
                        "3. Update a manager",
                        "4. Delete a manager"
                };

                String[] branchMenu = {
                        "5. Create a warehouse",
                        "6. List of warehouse",
                        "7. Update a warehouse",
                        "8. Delete a warehouse",
                        "9. Assign manager for a warehouse"
                };

                String[] productMenu = {
                        "10. Import file excel into system",
                        "11. List of product by branch id",
                        "12. Export report file excel",
                        "Press 0 to exit"
                };

                // Calculate maximum length of each column
                int maxUserMenuLength = getMaxStringLength(userMenu);
                int maxBranchMenuLength = getMaxStringLength(branchMenu);
                int maxProductMenuLength = getMaxStringLength(productMenu);
                System.out.println("============================================================================================");
                // Print headers
                System.out.printf("%-" + maxUserMenuLength + "s   %-" + maxBranchMenuLength + "s   %-" + maxProductMenuLength + "s\n", "Manage user:", "Manage warehouse branch:", "Manage product:");
                System.out.println("=".repeat(maxUserMenuLength) + "   " + "=".repeat(maxBranchMenuLength) + "   " + "=".repeat(maxProductMenuLength));

                // Print menu items
                for (int i = 0; i < branchMenu.length; i++) {
                    String userItem = i < userMenu.length ? userMenu[i] : "";
                    String branchItem = branchMenu[i];
                    String productItem = i < productMenu.length ? productMenu[i] : "";

                    System.out.printf("%-" + maxUserMenuLength + "s   %-" + maxBranchMenuLength + "s   %-" + maxProductMenuLength + "s\n", userItem, branchItem, productItem);
                }
                System.out.println("============================================================================================");

                System.out.print("\t--> Enter your choice: ");
                String input = sc.nextLine();
                int select = Integer.parseInt(input);

                if(select == 1){
                    userService.add();
                } else if (select == 2) {
                    userService.listALl();
                } else if (select == 3) {
                    userService.update();
                } else if (select == 4) {
                    userService.delete();
                } else if (select == 5) {
                    branchService.add();
                } else if (select == 6) {
                    branchService.listALl();
                } else if (select == 7) {
                    branchService.update();
                } else if (select == 8) {
                    branchService.delete();
                } else if (select == 9) {
                    branchService.assign();
                } else if (select == 10) {
                    productService.addProduct(user);
                } else if (select == 11) {
                    productService.listAll(user);
                } else if (select == 12) {
                    productService.exportFileExcel(user);
                } else if (select == 0) {
                    break;
                }else {
                    System.out.println("--> Invalid your choice. Please, choice again!");
                }
            }catch (NumberFormatException ex){
                System.out.println("--> Invalid your choice. Please, choice again!");
            }
        }
    }

    public static void menuOfManager() throws NotFoundException {
        while (true) {
            try {
                System.out.println(">> Manage product: ");
                System.out.println("\t\t1. Import file excel into system");
                System.out.println("\t\t2. List of product by branch id");
                System.out.println("\t\t3. Export report file excel");
                System.out.println("\t\t Press 0 to exit");

                System.out.print("\t--> Enter your choice: ");
                String input = sc.nextLine();
                int select = Integer.parseInt(input);

                if (select == 1) {
                    productService.addProduct(user);
                } else if (select == 2) {
                    productService.listAll(user);
                } else if (select == 3) {
                    productService.exportFileExcel(user);
                } else if (select == 0) {
                    break;
                } else {
                    System.out.println("--> Invalid your choice. Please, choice again!");
                }
            } catch (NumberFormatException ex) {
                System.out.println("--> Invalid your choice. Please, choice again!");
            }
        }
    }

    private static int getMaxStringLength(String[] array) {
        int maxLength = 0;
        for (String s : array) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }
}
