package by.itacademy.javaenterprise.command;


import by.itacademy.javaenterprise.exception.DAOException;

public interface Command<T> {

    void execute(T smth) throws DAOException;

}
