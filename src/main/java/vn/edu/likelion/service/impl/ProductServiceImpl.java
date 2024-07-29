package vn.edu.likelion.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.time.LocalDate;
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

    @Override
    public void exportFileExcel(User user) {
        System.out.println(">> EXPORT FILE REPORT WAREHOUSE MANAGEMENT: ");
        System.out.println("Please, waiting some seconds...");
        XSSFWorkbook workbook = new XSSFWorkbook();
        Cell cell = null;
        Row row = null;
        Sheet sheet = workbook.createSheet("Sheet1");

        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        String[] headers = {"ID", "Name", "Amount", "Price"};
        for (int i = 0; i < headers.length; i++) {
            Cell cellFormat = headerRow.createCell(i);
            cellFormat.setCellValue(headers[i]);
            cellFormat.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        if (user.getRole().equals("Admin")) {
            List<Branch> listBranches = branchService.takeListAll();
            for (Branch branch : listBranches) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(0);
                cell.setCellValue(branch.getName() + " - " + branch.getAddress());
                System.out.println(branch.getName() + " - " + branch.getAddress());
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
                rowIndex++;

                List<Product> listProducts = productDAO.listAll(branch.getId());
                if (!listProducts.isEmpty()) {
                    for (Product p : listProducts) {
                        row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(p.getId());
                        row.createCell(1).setCellValue(p.getName());
                        row.createCell(2).setCellValue(p.getAmount());
                        row.createCell(3).setCellValue(p.getPrice());
                    }
                    row = sheet.createRow(rowIndex++);
                    row.createCell(1).setCellValue("Total Amount: ");
                    int totalAmount = productDAO.sumAmountProductByBranchId(branch.getId());
                    row.createCell(2).setCellValue(totalAmount);
                } else {
                    row = sheet.createRow(rowIndex++);
                    cell = row.createCell(0);
                    cell.setCellValue(">> This warehouse has no products.");
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));
                }
            }
            createFileExcel(workbook, "ReportAdmin");
        } else {
            int idBranch = userDAO.getIdBranch(user.getId());
            if (idBranch > 0) {
                rowIndex = exportProduct(sheet, rowIndex, idBranch);
                createFileExcel(workbook, "ReportManager");
            } else {
                System.out.println(">> You are not assigned warehouse branch management.");
            }
        }
    }

    private int exportProduct(Sheet sheet, int rowIndex, Integer branchId) {
        List<Product> listProducts = productDAO.listAll(branchId);
        if (!listProducts.isEmpty()) {
            for (Product p : listProducts) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getAmount());
                row.createCell(3).setCellValue(p.getPrice());
            }
            Row row = sheet.createRow(rowIndex++);
            row.createCell(1).setCellValue("Total Amount: ");
            int totalAmount = productDAO.sumAmountProductByBranchId(branchId);
            row.createCell(2).setCellValue(totalAmount);
        } else {
            Row row = sheet.createRow(rowIndex++);
            Cell cell = row.createCell(0);
            cell.setCellValue(">> This warehouse does not contain products.");
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, 3));
        }
        return rowIndex;
    }

    private void createFileExcel(Workbook workbook, String fileName) {
        try {
            LocalDate localDate = LocalDate.now();
            String dateString = localDate.toString();
            FileOutputStream stream = new FileOutputStream(fileName + dateString + ".xlsx");
            workbook.write(stream);
            System.out.println("Export file excel successfully!");
            workbook.close();
            stream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
