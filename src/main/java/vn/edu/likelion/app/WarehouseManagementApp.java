package vn.edu.likelion.app;

import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.impl.BranchServiceImpl;
import vn.edu.likelion.service.impl.UserServiceImpl;

public class WarehouseManagementApp {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
//        userService.update();
//        try {
//            userService.delete();
//        } catch (NotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        userService.listALl();

        BranchServiceImpl branchService = new BranchServiceImpl();
        branchService.add();
        try {
            branchService.update();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
//        branchService.listALl();
//        try {
//            branchService.delete();
//        } catch (NotFoundException e) {
//            System.out.println(e.getMessage());
//        }


    }
}
