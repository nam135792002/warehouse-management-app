package vn.edu.likelion.service.impl;

import vn.edu.likelion.dao.BranchDAO;
import vn.edu.likelion.dao.ProductDAO;
import vn.edu.likelion.dao.UserDAO;
import vn.edu.likelion.entity.Branch;
import vn.edu.likelion.entity.User;
import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.GeneralInterface;
import vn.edu.likelion.service.UserInterface;

import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements GeneralInterface, UserInterface {

    private Scanner sc = new Scanner(System.in);
    private UserDAO userDAO = new UserDAO();
    private BranchDAO branchDAO = new BranchDAO();
    private User user = null;
    private ProductDAO productDAO = new ProductDAO();
    private BranchServiceImpl branchService = new BranchServiceImpl();

    @Override
    public void add() {
        user = new User();
        user.setId(0);
        System.out.println(">> ADD A MANAGER: ");

        enterInfoOfUser(user);

        if(userDAO.insertOneUser(user)){
            System.out.println(">> Add manager successfully!");
        }else{
            System.out.println(">> Add manager failed!");
        }
    }

    @Override
    public void update() throws NotFoundException {
        listALl();
        System.out.println(">> UPDATE A MANAGER: ");
        int idManager = enterIdOfUser();

        user = userDAO.checkExistOfManager(idManager);
        if(user != null){
            enterInfoOfUser(user);
            if(userDAO.updateUser(user)) System.out.println(">> Update manager successfully!");
            else System.out.println(">> Update manager failed!");
        }else{
            throw new NotFoundException("ID " + idManager + " not found in DB");
        }
    }

    @Override
    public void listALl() {
        List<User> listUsers = userDAO.getAll();
        if(listUsers.isEmpty()){
            System.out.println(">> List of users is empty!");
        }else{
            System.out.println(">> LIST OF MANAGER: ");
            System.out.printf("%-5s %-20s %-20s\n", "ID", "USERNAME", "ROLE");
            System.out.println("---------------------------------------------");
            listUsers.forEach(u -> System.out.printf("%-5d %-20s %-20s\n", u.getId(), u.getUsername(), u.getRole()));
        }
    }

    @Override
    public void delete() throws NotFoundException {
        listALl();
        System.out.println(">> DELETE A MANAGER: ");
        int idManager = enterIdOfUser();

        user = userDAO.checkExistOfManager(idManager);
        if(user != null){
            int idBranch = userDAO.checkUserManageWarehouse(idManager);
            if(idBranch > 0){
                if(productDAO.checkProductInWarehouse(idBranch)){
                    System.out.println("This warehouse has contain product. Please move all of product to " +
                            "another warehouse before deleting!");
                    List<Branch> listBranches = branchDAO.listBranchWithoutBranchChoice(idBranch);
                    int idToBranch = branchService.chooseIdBranchInList(listBranches);
                    productDAO.moveProductToAnotherWarehouse(idBranch,idToBranch,2);
                }
                branchDAO.deleteBranchByUserId(idManager);
            }

            if(userDAO.delete(idManager)){
                System.out.println(">> Delete manager successfully!");
            }else System.out.println(">> Delete manager failed!");
        }else{
            throw new NotFoundException("ID " + idManager + " not found in DB");
        }
    }

    private void enterInfoOfUser(User user){
        String username;

        do {
            System.out.print("Enter the username: ");
            username = sc.nextLine();
            user.setUsername(username);
            if(userDAO.checkDuplicateUsername(username, user.getId())) System.out.println(">> Username: " + username + " is existed in DB. Please enter again!");
        }while (userDAO.checkDuplicateUsername(username, user.getId()));

        System.out.print("Enter the password: ");
        String password = sc.nextLine();
        user.setPassword(password);
    }

    private int enterIdOfUser(){
        int idManager;

        while (true){
            try {
                System.out.print("Enter the ID of manager: ");
                idManager = Integer.parseInt(sc.nextLine());
                break;
            }catch (NumberFormatException e){
                System.out.println(">> ID is invalid. Please enter again!");
            }
        }
        return idManager;
    }

    @Override
    public User login() {
        System.out.println(">> Please login before using app");
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        return userDAO.checkExistUsernameAndPasswordInDB(username, password);
    }
}
