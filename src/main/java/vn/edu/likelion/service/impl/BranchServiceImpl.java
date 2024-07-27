package vn.edu.likelion.service.impl;

import vn.edu.likelion.dao.BranchDAO;
import vn.edu.likelion.entity.Branch;
import vn.edu.likelion.service.GeneralInterface;

import java.util.Scanner;

public class BranchServiceImpl implements GeneralInterface {
    private BranchDAO branchDAO = new BranchDAO();
    private Branch branch = null;
    private Scanner sc = new Scanner(System.in);

    @Override
    public void add() {
        branch = new Branch();
        System.out.println(">> ADD A WAREHOUSE BRANCH: ");
        System.out.print("Enter the name: ");
        String name = sc.nextLine();
    }

    @Override
    public void update() {

    }

    @Override
    public void listALl() {

    }

    @Override
    public void delete() {

    }
}
