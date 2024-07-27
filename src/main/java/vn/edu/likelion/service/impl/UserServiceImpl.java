package vn.edu.likelion.service.impl;

import vn.edu.likelion.dao.UserDAO;
import vn.edu.likelion.entity.User;
import vn.edu.likelion.service.UserInterface;

import java.util.Scanner;

public class UserServiceImpl implements UserInterface {

    private Scanner sc = new Scanner(System.in);
    private UserDAO userDAO = new UserDAO();
    private User user = null;

    @Override
    public void add() {
        user = new User();
        System.out.println(">> ADD A USER");
        System.out.print("Enter the username: ");
        String username = sc.nextLine();
        user.setUsername(username);

        System.out.print("Enter the password: ");
        String password = sc.nextLine();
        user.setPassword(password);

        if(userDAO.insertOneUser(user)){
            System.out.println("Add user successfully!");
        }else{
            System.out.println("Add user failed!");
        }
    }

    @Override
    public void update() {

    }
}
