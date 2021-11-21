package by.itacademy.javaenterprise.borisevich.dao.impl;

import by.itacademy.javaenterprise.borisevich.dao.TrainingDAO;
import by.itacademy.javaenterprise.borisevich.entity.Training;
import by.itacademy.javaenterprise.borisevich.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingDAOImpl implements TrainingDAO<Training> {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDAOImpl.class);

    private static final String ADD_QUERY = "INSERT Training(TRAINING_ID,TRAINING_DATE,USER_ID) VALUES(?,?,?)";
    private static final int TRAINING_ID_ADD_QUERY_INDEX = 1;
    private static final int DATE_ADD_QUERY_INDEX = 2;
    private static final int USER_ID_QUERY_INDEX = 3;

    private static final String UPDATE_QUERY = "UPDATE Training SET TRAINING_DATE=?,USER_ID=? WHERE TRAINING_ID=?";
    private static final int UPDATE_DATE_QUERY_INDEX = 1;
    private static final int UPDATE_USER_ID_QUERY_INDEX = 2;
    private static final int UPDATE_TRAINING_ID_QUERY_INDEX = 3;

    private static final String DELETE_QUERY = "DELETE FROM Training WHERE TRAINING_ID=?";
    private static final int TRAINING_ID_DELETE_QUERY_INDEX = 1;

    public static final String SELECT_ALL = "SELECT TRAINING_ID,TRAINING_DATE,USER_ID FROM Training LIMIT ? OFFSET ?";
    private static final int SELECT_LIMIT_QUERY_INDEX = 1;
    private static final int SELECT_OFFSET_QUERY_INDEX = 2;

    private static final int SELECT_ALL_TRAINING_QUERY_INDEX = 1;
    private static final int SELECT_ALL_TRAINING_DATE_QUERY_INDEX = 2;
    private static final int SELECT_ALL_USER_ID_QUERY_INDEX = 3;

    private final DataSource connect;

    public TrainingDAOImpl(DataSource connect) {
        this.connect = connect;
    }

    @Override
    public void addTraining(Training training) throws DAOException {
        try (Connection con = connect.getConnection();
             PreparedStatement pst = con.prepareStatement(ADD_QUERY)) {
            prepareAddStatement(pst, training);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while adding a training" + training.toString(), e);
        }

    }

    @Override
    public void updateTraining(Training training) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            prepareUpdateStatement(statement, training);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while updating training" + training.toString(), e);

        }
    }

    @Override
    public void deleteTraining(Training training) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            prepareDeleteStatement(statement, training);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while deleting training" + training.toString(), e);
        }
    }

    @Override
    public List<Training> getAllTrainingsPagination(int limit, int offset) throws DAOException {
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {

            statement.setInt(SELECT_LIMIT_QUERY_INDEX, limit);
            statement.setInt(SELECT_OFFSET_QUERY_INDEX, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                trainings.add(Training.builder().id(rs.getInt(SELECT_ALL_TRAINING_QUERY_INDEX)).
                        date(LocalDateTime.parse(rs.getString(SELECT_ALL_TRAINING_DATE_QUERY_INDEX).replaceAll(" ", "T")))
                        .userID(rs.getInt(SELECT_ALL_USER_ID_QUERY_INDEX)).build());
            }
        } catch (SQLException e) {
            logger.error("Error with selecting trainings"+ " ", e);
        }
        return trainings;
    }


    private void prepareUpdateStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setString(UPDATE_DATE_QUERY_INDEX, String.valueOf(training.getDate()));
        statement.setInt(UPDATE_USER_ID_QUERY_INDEX, training.getUserID());
        statement.setInt(UPDATE_TRAINING_ID_QUERY_INDEX, training.getId());
    }

    private void prepareAddStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setInt(TRAINING_ID_ADD_QUERY_INDEX, training.getId());
        statement.setString(DATE_ADD_QUERY_INDEX, String.valueOf(training.getDate()));
        statement.setInt(USER_ID_QUERY_INDEX, training.getUserID());
    }

    private void prepareDeleteStatement(PreparedStatement statement, Training training) throws SQLException {
        statement.setInt(TRAINING_ID_DELETE_QUERY_INDEX, training.getId());
    }


}
