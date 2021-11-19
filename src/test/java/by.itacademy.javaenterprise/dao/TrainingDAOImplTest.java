package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.dao.impl.TrainingDAOImpl;
import by.itacademy.javaenterprise.entity.Training;
import by.itacademy.javaenterprise.exception.DAOException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class TrainingDAOImplTest {

    private TrainingDAOImpl trainingDAO;

    private  DataSource mockDataSource;
    private  Connection mockConn;
    private  PreparedStatement mockPreparedStmnt;
    private  ResultSet mockResultSet;
    private  Training training = Training.builder().date(LocalDateTime.now()).id(2).userID(1).build();

    @BeforeEach
    public void setUp() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        mockPreparedStmnt = Mockito.mock(PreparedStatement.class);
        mockConn = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        trainingDAO = new TrainingDAOImpl(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConn);
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockPreparedStmnt.execute()).thenReturn(Boolean.TRUE);
        when(mockPreparedStmnt.executeUpdate()).thenReturn(100);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE);
    }

    @Test
    public void testAddTraining() throws DAOException, SQLException {
        trainingDAO.addTraining(training);
        mockPreparedStmnt.setString(1, String.valueOf(training.getDate()));
        mockPreparedStmnt.setInt(2, training.getUserID());
        mockPreparedStmnt.setInt(3, training.getId());
        assertEquals(100, mockPreparedStmnt.executeUpdate());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).executeUpdate();
    }

    @Test
    public void testUpdateTraining() throws DAOException, SQLException {
        training.setId(10);
        trainingDAO.updateTraining(training);
        mockPreparedStmnt.setString(1, String.valueOf(training.getDate()));
        mockPreparedStmnt.setInt(2, training.getUserID());
        mockPreparedStmnt.setInt(3, training.getId());
        assertEquals(100, mockPreparedStmnt.executeUpdate());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).executeUpdate();
    }

    @Test
    public void testDeleteTraining() throws DAOException, SQLException {
        trainingDAO.deleteTraining(training);
        mockPreparedStmnt.setInt(1, training.getId());
        assertEquals(100, mockPreparedStmnt.executeUpdate());
        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).executeUpdate();
    }

    @Test
    public void testGetAllTrainingsPagination() throws DAOException, SQLException {
        assertThrows(Exception.class, () -> trainingDAO.getAllTrainingsPagination(1, 0));

        verify(mockConn, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();

    }

    @Test
    public void testUpdateTrainingInvalid() throws DAOException {
        trainingDAO = Mockito.mock(TrainingDAOImpl.class);
        doThrow(new NullPointerException()).when(trainingDAO).updateTraining(isNull());
        assertThrows(Exception.class,
                () -> {
                    trainingDAO.updateTraining(null);
                });

    }

    @Test
    public void testAddTrainingNull() throws DAOException {
        trainingDAO = Mockito.mock(TrainingDAOImpl.class);
        doThrow(new NullPointerException()).when(trainingDAO).addTraining(isNull());
        assertThrows(Exception.class, () -> trainingDAO.addTraining(null));
    }


}
