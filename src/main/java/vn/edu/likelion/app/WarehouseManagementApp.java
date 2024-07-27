package vn.edu.likelion.app;

import vn.edu.likelion.service.impl.UserServiceImpl;

public class WarehouseManagementApp {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.add();
    }
}
