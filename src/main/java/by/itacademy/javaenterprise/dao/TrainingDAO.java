package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.entity.Training;
import by.itacademy.javaenterprise.exception.DAOException;

import java.util.List;

public interface TrainingDAO<T> {


        void addTraining(Training training) throws DAOException;

        void updateTraining(Training training) throws DAOException;

        void deleteTraining(Training training) throws DAOException;

        List<T> getAllTrainingsPagination(int limit, int offset) throws DAOException;

}
