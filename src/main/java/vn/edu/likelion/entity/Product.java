package vn.edu.likelion.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    private String name;
    private int amount;
    private long price;
    private Branch branch;
    private User user;
    private List<ProductAttribute> listProductAttributes = new ArrayList<>();
}
