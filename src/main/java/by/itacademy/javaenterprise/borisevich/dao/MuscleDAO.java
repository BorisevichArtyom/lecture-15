package by.itacademy.javaenterprise.borisevich.dao;

import by.itacademy.javaenterprise.borisevich.entity.Muscle;
import by.itacademy.javaenterprise.borisevich.exception.DAOException;

import java.util.List;

public interface MuscleDAO<T> {
    void addMuscle(Muscle muscle) throws DAOException;

    void updateMuscle(Muscle muscle, int muscleId) throws DAOException;

    void deleteMuscle(String muscleName) throws DAOException;

    List<T> getAllMusclesPagination(int limit, int offset) throws DAOException;
}
