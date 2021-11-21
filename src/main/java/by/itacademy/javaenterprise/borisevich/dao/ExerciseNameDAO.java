package by.itacademy.javaenterprise.borisevich.dao;

import by.itacademy.javaenterprise.borisevich.entity.ExerciseName;
import by.itacademy.javaenterprise.borisevich.exception.DAOException;

import java.util.List;

public interface ExerciseNameDAO<T> {
    void addNameOfExercise(ExerciseName exerciseName) throws DAOException;

    void updateExerciseName(ExerciseName exerciseName, int exerciseNameId) throws DAOException;

    void deleteExerciseName(int exerciseNameId) throws DAOException;

    List<T> getAllExerciseNamesPagination(int limit, int offset) throws DAOException;
}
