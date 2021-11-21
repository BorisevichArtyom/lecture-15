package by.itacademy.javaenterprise.borisevich.command;


import by.itacademy.javaenterprise.borisevich.exception.DAOException;

public interface Command<T> {

    void execute(T smth) throws DAOException;

}
