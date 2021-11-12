package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.entity.User;
import by.itacademy.javaenterprise.exception.DAOException;

import java.util.List;


public interface UserDAO<T> {

    void addUser(User user) throws DAOException;

    void updateUser(User user) throws DAOException;

    void deleteUser(User user) throws DAOException;

    List<T> getAllUsersPagination(int limit, int offset) throws DAOException;
}
