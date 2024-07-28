package vn.edu.likelion.service.impl;

import vn.edu.likelion.dao.BranchDAO;
import vn.edu.likelion.dao.ProductDAO;
import vn.edu.likelion.dao.UserDAO;
import vn.edu.likelion.entity.Branch;
import vn.edu.likelion.entity.User;
import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.BranchInterface;
import vn.edu.likelion.service.GeneralInterface;

import java.util.List;
import java.util.Scanner;

public class BranchServiceImpl implements GeneralInterface, BranchInterface {
    private BranchDAO branchDAO = new BranchDAO();
    private UserDAO userDAO = new UserDAO();
    private Branch branch = null;
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();

    @Override
    public void add() {
        branch = new Branch();
        branch.setId(0);
        System.out.println(">> ADD A WAREHOUSE BRANCH: ");

        enterInfoBranch(branch);

        int choose;
        while (true){
            try{
                System.out.println(">> Press 1 to assign management branch for user");
                System.out.println(">> Press 0 to create branch without manager");
                System.out.print("Please, choose: ");
                choose = Integer.parseInt(sc.nextLine());
                break;
            }catch (NumberFormatException e){
                System.out.println(">> Input is invalid! Please enter again!");
            }
        }
        if(choose == 1){
            List<User> listUsers = userDAO.getAllUserWithoutAssign();
            int idUser = chooseIdUserInList(listUsers);
            User user = userDAO.checkExistOfManager(idUser);
            branch.setUser(user);
        } else if (choose == 0) {
            branch.setUser(null);
        }else{
            System.out.println(">> Input is invalid! Please enter again!");
        }


        if(branchDAO.insertOneBranch(branch)){
            System.out.println("Add a warehouse branch successfully!");
        }else{
            System.out.println("Add a warehouse branch failed!");
        }
    }

    @Override
    public void assign() {
        List<Branch> listBranches = branchDAO.getAllWithoutUser();
        List<User> listUsers = userDAO.getAllUserWithoutAssign();
        if(listBranches.isEmpty()){
            System.out.println(">> List of branch is empty, so let's add more branch before using this function!");
        } else if (listUsers.isEmpty()) {
            System.out.println(">> List of user is empty, so let's add more user before using this function!");
        }else {
            System.out.println(">> ASSIGN MANAGE WAREHOUSE BRANCH: ");
            int idBranch = chooseIdBranchInList(listBranches);

            System.out.println("---------------------------------------------");

            int idUser = chooseIdUserInList(listUsers);

            if (branchDAO.assignManageWarehouse(idUser, idBranch))
                System.out.println("Assign management for user successfully!");
            else System.out.println("Assign management for user failed!");
        }

    }

    public List<Branch> takeListAll(){
        return branchDAO.getAll();
    }

    public int chooseIdBranchInList(List<Branch> listBranches){
        boolean flag;
        boolean checkIdInList;
        int idBranch = 0;
        System.out.printf("%-5s %-40s %-30s\n", "ID", "NAME", "ADDRESS");
        System.out.println("------------------------------------------------------------------------------");
        listBranches.forEach(b -> System.out.printf("%-5d %-40s %-30s\n", b.getId(), b.getName(), b.getAddress()));
        do {
            flag = true;
            try {
                System.out.print("Enter the ID warehouse branch: ");
                idBranch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                flag = false;
            }
            int finalIdBranch = idBranch;
            checkIdInList = listBranches.stream().anyMatch(b -> b.getId() == finalIdBranch);
            if (!checkIdInList) System.out.println("ID of branch doesn't exist");
        } while (!checkIdInList || !flag);
        return idBranch;
    }

    private int chooseIdUserInList(List<User> listUsers){
        boolean flag;
        boolean checkIdInList;
        int idUser = 0;
        System.out.printf("%-5s %-20s\n", "ID", "USERNAME");
        System.out.println("---------------------------------------------");
        listUsers.forEach(u -> System.out.printf("%-5d %-20s\n", u.getId(), u.getUsername()));
        do {
            flag = true;
            try {
                System.out.print("Enter the ID user: ");
                idUser = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                flag = false;
            }
            int finalIdUser = idUser;
            checkIdInList = listUsers.stream().anyMatch(u -> u.getId() == finalIdUser);
            if (!checkIdInList) System.out.println("ID of user doesn't exist");
        } while (!checkIdInList || !flag);
        return idUser;
    }

    @Override
    public void update() throws NotFoundException {
        listALl();
        System.out.println(">> UPDATE A BRANCH: ");
        int idBranch = enterIdBranch();
        branch = branchDAO.checkExistedBranchById(idBranch);
        if(branch != null){
            enterInfoBranch(branch);

            while (true){
                System.out.println(">> Press 1 to replace new manager.");
                System.out.println(">> Press 0 to skip.");
                System.out.print("Enter your choice: ");
                int choose = 0;
                try{
                    choose = Integer.parseInt(sc.nextLine());
                }catch (NumberFormatException e){
                    System.out.println("Your choice is invalid! Please choose again.");
                }
                if(choose == 1){
                    List<User> listUsers = userDAO.getAllUserWithoutAssign();
                    if (listUsers.isEmpty()) {
                        System.out.println(">> List of user is empty, so let's add more user before using this function!");
                    }else {
                        System.out.println("---------------------------------------------");

                        int idUser = chooseIdUserInList(listUsers);
                        branch.setUser(new User(idUser));
                    }
                }else if(choose == 0) break;
                else System.out.println("Not found this function. Please choose again!");
            }

            if(branchDAO.updateBranch(branch)) System.out.println("Update branch successfully!");
            else System.out.println("Update branch failed!");
        }else throw new NotFoundException("ID: " + idBranch + " not found in DB");

    }

    @Override
    public void listALl() {
        List<Branch> listBranches = branchDAO.getAll();
        if(listBranches.isEmpty()){
            System.out.println(">> List of branch is empty!");
        }else{
            System.out.println(">> LIST OF branch: ");
            System.out.printf("%-5s %-35s %-20s %-15s%n", "ID", "NAME", "ADDRESS", "USERNAME");
            System.out.println("-------------------------------------------------------------");
            listBranches.forEach(branch -> {
                System.out.printf("%-5d %-35s %-20s %-15s%n",
                        branch.getId(),
                        branch.getName(),
                        branch.getAddress(),
                        branch.getUser().getUsername() != null ? branch.getUser().getUsername() : "No Manager");
            });
        }
    }

    @Override
    public void delete() throws NotFoundException {
        listALl();
        System.out.println("DELETE A BRANCH: ");
        int idBranch = enterIdBranch();

        if(branchDAO.checkExistedBranchById(idBranch) != null){
            if(productDAO.checkProductInWarehouse(idBranch)){
                System.out.println("This warehouse has contain product. Please move all of product to " +
                        "another warehouse before deleting!");
                List<Branch> listBranches = branchDAO.listBranchWithoutBranchChoice(idBranch);
                int idToBranch = chooseIdBranchInList(listBranches);
                productDAO.moveProductToAnotherWarehouse(idBranch,idToBranch,2);
            }
            if(branchDAO.deleteBranchById(idBranch)){
                System.out.println("Delete branch successfully!");
            }else System.out.println("Delete branch failed!");
        }else throw new NotFoundException(">> ID " + idBranch + " not found in DB");
    }

    private int enterIdBranch(){
        int idBranch = 0;
        while (true){
            try{
                System.out.print("Enter the ID of branch: ");
                idBranch = Integer.parseInt(sc.nextLine());
                break;
            }catch (NumberFormatException e){
                System.out.println(">> ID is invalid!");
            }
        }
        return idBranch;
    }

    private void enterInfoBranch(Branch branch){
        String name;
        do {
            System.out.print("Enter the name: ");
            name = sc.nextLine();
            if (branchDAO.checkDuplicateNameBranch(name, branch.getId())) System.out.println(">> Name of branch is existed in DB. Please enter again!");
        }while (branchDAO.checkDuplicateNameBranch(name, branch.getId()));

        System.out.print("Enter the address: ");
        String address = sc.nextLine();

        branch.setName(name);
        branch.setAddress(address);
    }
}
