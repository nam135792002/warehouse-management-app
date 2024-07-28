package vn.edu.likelion.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;

    public User(String username) {
        this.username = username;
    }

    public User(int idUser, String role) {
        this.id = idUser;
        this.role = role;
    }

    public User(int idUser) {
        this.id = idUser;
    }

    public User(Integer id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
