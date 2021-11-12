package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.entity.Muscle;
import by.itacademy.javaenterprise.exception.DAOException;

import java.util.List;

public interface MuscleDAO<T> {
    void addMuscle(Muscle muscle) throws DAOException;

    void updateMuscle(Muscle muscle, int muscleId) throws DAOException;

    void deleteMuscle(String muscleName) throws DAOException;

    List<T> getAllMusclesPagination(int limit, int offset) throws DAOException;
}
