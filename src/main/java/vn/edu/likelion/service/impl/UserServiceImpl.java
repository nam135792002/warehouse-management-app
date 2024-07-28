package vn.edu.likelion.service.impl;

import vn.edu.likelion.dao.BranchDAO;
import vn.edu.likelion.dao.UserDAO;
import vn.edu.likelion.entity.User;
import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.GeneralInterface;

import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements GeneralInterface {

    private Scanner sc = new Scanner(System.in);
    private UserDAO userDAO = new UserDAO();
    private BranchDAO branchDAO = new BranchDAO();
    private User user = null;

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
            if(branchDAO.deleteBranchByUserId(idManager) || userDAO.delete(idManager)) System.out.println(">> Delete manager successfully!");
            else System.out.println(">> Delete manager failed!");
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
}
