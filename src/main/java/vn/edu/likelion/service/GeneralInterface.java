package vn.edu.likelion.service;

import vn.edu.likelion.exception.NotFoundException;

public interface GeneralInterface {
    void add();
    void update() throws NotFoundException;
    void listALl();
    void delete() throws NotFoundException;
}
