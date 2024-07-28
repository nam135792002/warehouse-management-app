package vn.edu.likelion.service.impl;

import org.apache.poi.ss.usermodel.*;
import vn.edu.likelion.dao.BranchDAO;
import vn.edu.likelion.dao.ProductDAO;
import vn.edu.likelion.dao.UserDAO;
import vn.edu.likelion.entity.Branch;
import vn.edu.likelion.entity.Product;
import vn.edu.likelion.entity.ProductAttribute;
import vn.edu.likelion.entity.User;
import vn.edu.likelion.exception.NotFoundException;
import vn.edu.likelion.service.ProductInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductServiceImpl implements ProductInterface {
    private Product product;
    private List<Product> listProducts;
    private ProductAttribute productAttribute;
    private List<ProductAttribute> listProductAttributes;
    private BranchServiceImpl branchService = new BranchServiceImpl();
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private UserDAO userDAO = new UserDAO();
    private BranchDAO branchDAO = new BranchDAO();

    public List<Product> readFileExcel(String fileName) throws NotFoundException {
        listProducts = new ArrayList<>();
        File file = new File(fileName);
        FileInputStream stream = null;
        Workbook workbook = null;

        try {
            stream = new FileInputStream(file);
            workbook = WorkbookFactory.create(stream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet){
                if(row.getRowNum() < 5) continue;
                if(row.getCell(0).getCellType() == CellType.BLANK) continue;
                product = new Product();
                product.setName(row.getCell(1).getStringCellValue());
                product.setPrice((long) row.getCell(4).getNumericCellValue());
                product.setAmount((int) row.getCell(3).getNumericCellValue());

                String[] splitValue = row.getCell(2).getStringCellValue().split("\n");
                listProductAttributes = new ArrayList<>();
                for (String s : splitValue) {
                    productAttribute = new ProductAttribute(s);
                    listProductAttributes.add(productAttribute);
                }
                product.setListProductAttributes(listProductAttributes);
                listProducts.add(product);
            }
        } catch (IOException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return listProducts;
    }


    @Override
    public void addProduct(User user) {
        System.out.println(">> IMPORT PRODUCT INTO WAREHOUSE: ");
        int idBranch;
        if(user.getRole().equals("Admin")){
            List<Branch> listBranches = branchService.takeListAll();
            idBranch = branchService.chooseIdBranchInList(listBranches);
            if (branchDAO.checkManagementOfWarehouse(idBranch)){
                idBranch = 0;
                System.out.println("Nobody manage this warehouse, so you can not add product.");
            }
        }else{
            idBranch = userDAO.getIdBranch(user.getId());
            if(idBranch == 0) System.out.println(">> You are assigned warehouse branch management.");
        }

        if(idBranch > 0){
            System.out.print("Enter the name of excel: ");
            String fileName = sc.nextLine();
            try {
                List<Product> listProducts = readFileExcel(fileName);
                if(productDAO.insertInfoProduct(listProducts, idBranch, user.getId())) System.out.println("Create list of product in DB successfully");
                else System.out.println("Create list of product in DB failed");
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void listAll(User user) {
        System.out.println(">> LIST ALL OF PRODUCT");
        int idBranch;
        if (user.getRole().equals("Admin")){
            List<Branch> listBranches = branchService.takeListAll();
            idBranch = branchService.chooseIdBranchInList(listBranches);
            printAll(idBranch);
        }else{
            idBranch = userDAO.getIdBranch(user.getId());
            if(idBranch > 0) printAll(idBranch);
            else System.out.println(">> You are assigned warehouse branch management.");
        }
    }

    private void printAll(Integer idBranch){
        List<Product> listProducts = productDAO.listAll(idBranch);
        if (listProducts.isEmpty()) System.out.println(">> This warehouse has not contain product.");
        else{
            System.out.printf("%-5s | %-30s | %-6s | %-10s | %s\n", "ID", "NAME", "AMOUNT", "PRICE", "ATTRIBUTES");
            System.out.println("----------------------------------------------------------------------------------------------");

            // Iterate over the list of products and print each product's details
            for (Product p : listProducts) {
                // Print product details
                System.out.printf("%-5d | %-30s | %-6d | %-10d |", p.getId(), p.getName(), p.getAmount(), p.getPrice());

                // Print first attribute (if exists) in the same line as product details
                if (p.getListProductAttributes() != null && !p.getListProductAttributes().isEmpty()) {
                    System.out.printf(" %-30s\n", p.getListProductAttributes().get(0).getContent());

                    // Print remaining attributes (if any)
                    for (int i = 1; i < p.getListProductAttributes().size(); i++) {
                        System.out.printf("%-5s | %-30s | %-6s | %-10s | %-30s\n", "", "", "", "", p.getListProductAttributes().get(i).getContent());
                    }
                } else {
                    // If no attributes, just move to the next line
                    System.out.println();
                }

                // Add a separator between products
                System.out.println("----------------------------------------------------------------------------------------------");
            }
        }
    }
}
