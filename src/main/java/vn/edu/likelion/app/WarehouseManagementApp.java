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
                                throw new RuntimeException(e);
                            }
                        }else{
                            try {
                                menuOfManager();
                            } catch (NotFoundException e) {
                                throw new RuntimeException(e);
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
                System.out.println(">> Manage user: ");
                System.out.println("\t\t1. Create a manager");
                System.out.println("\t\t2. List of managers");
                System.out.println("\t\t3. Update a manager");
                System.out.println("\t\t4. Delete a manager");
                System.out.println(">> Manage warehouse branch: ");
                System.out.println("\t\t5. Create a warehouse");
                System.out.println("\t\t6. List of warehouse");
                System.out.println("\t\t7. Update a warehouse");
                System.out.println("\t\t8. Delete a warehouse");
                System.out.println("\t\t9. Assign manger for a warehouse");
                System.out.println(">> Manage product: ");
                System.out.println("\t\t10. Import file excel into system");
                System.out.println("\t\t11. List of product by branch id");
                System.out.println("\t\t12. Export report file excel");
                System.out.println("\t\t Press 0 to exit");

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
                    branchService.delete();
                } else if (select == 10) {
                    productService.addProduct(user);
                } else if (select == 11) {
                    productService.listAll(user);
                } else if (select == 12) {
                    System.out.println("Updating...!");
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
                    System.out.println("Updating...!");
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
}
