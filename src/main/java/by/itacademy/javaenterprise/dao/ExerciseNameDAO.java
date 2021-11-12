package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.entity.ExerciseName;
import by.itacademy.javaenterprise.exception.DAOException;

import java.util.List;

public interface ExerciseNameDAO<T> {
    void addNameOfExercise(ExerciseName exerciseName) throws DAOException;

    void updateExerciseName(ExerciseName exerciseName, int exerciseNameId) throws DAOException;

    void deleteExerciseName(int exerciseNameId) throws DAOException;

    List<T> getAllExerciseNamesPagination(int limit, int offset) throws DAOException;
}
