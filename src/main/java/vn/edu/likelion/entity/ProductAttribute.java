package vn.edu.likelion.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    private Integer id;
    private String content;
    private Product product;

    public ProductAttribute(String content) {
        this.content = content;
    }
}
