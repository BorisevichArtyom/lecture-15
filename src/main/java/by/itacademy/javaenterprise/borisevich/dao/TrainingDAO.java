package by.itacademy.javaenterprise.borisevich.dao;

import by.itacademy.javaenterprise.borisevich.exception.DAOException;
import by.itacademy.javaenterprise.borisevich.entity.Training;

import java.util.List;

public interface TrainingDAO<T> {


        void addTraining(Training training) throws DAOException;

        void updateTraining(Training training) throws DAOException;

        void deleteTraining(Training training) throws DAOException;

        List<T> getAllTrainingsPagination(int limit, int offset) throws DAOException;

}
