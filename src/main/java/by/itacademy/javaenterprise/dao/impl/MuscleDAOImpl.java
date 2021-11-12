package by.itacademy.javaenterprise.dao.impl;

import by.itacademy.javaenterprise.dao.MuscleDAO;
import by.itacademy.javaenterprise.entity.Muscle;
import by.itacademy.javaenterprise.exception.DAOException;
import by.itacademy.javaenterprise.spring.SpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component
public class MuscleDAOImpl implements MuscleDAO<Muscle> {

    private static final Logger logger = LoggerFactory.getLogger(MuscleDAOImpl.class);

    private static final String ADD_QUERY = "INSERT Muscles(MUSCLE_ID,MUSCLE_NAME) VALUES(?,?)";

    private static final String UPDATE_QUERY = "UPDATE Muscles SET MUSCLE_NAME=? WHERE MUSCLE_ID=?";

    private static final String DELETE_QUERY = "DELETE FROM Muscles WHERE MUSCLE_NAME=?";

    public static final String SELECT_ALL = "SELECT MUSCLE_ID,MUSCLE_NAME FROM Muscles LIMIT ? OFFSET ?";

    private static final String FIND_MUSCLE_BY_ID_QUERY = "SELECT MUSCLE_ID,MUSCLE_NAME FROM Muscles WHERE MUSCLE_ID=?";

    private final JdbcTemplate jdbcTemplate;


    RowMapper<Muscle> MUSCLE_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new Muscle(resultSet.getInt(1), (resultSet.getString(2)));
    };

    public MuscleDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addMuscle(Muscle muscle) throws DAOException {
        try {
            jdbcTemplate.update(ADD_QUERY, muscle.getMuscleId(), muscle.getMuscleName());
        } catch (Exception e) {
            throw new DAOException("Cant add muscle:" + muscle+ " " + e);
        }
    }


    @Override
    public void updateMuscle(Muscle muscle, int muscleId) throws DAOException {
        try {
            String muscleName = muscle.getMuscleName();
            jdbcTemplate.update(UPDATE_QUERY, muscleName, muscleId);
        } catch (Exception e) {
            throw new DAOException("Cant update muscle:" + muscle + " muscle id:" + muscleId + " " + e);
        }
    }

    @Override
    public void deleteMuscle(String muscleName) throws DAOException {
        try {
            jdbcTemplate.update(DELETE_QUERY, muscleName);
        } catch (DataAccessException e) {
            throw new DAOException("Cant delete muscle:" + muscleName+ " " + e);
        }

    }


    @Override
    public List<Muscle> getAllMusclesPagination(int limit, int offset) throws DAOException {
        List<Muscle> muscleList = jdbcTemplate.query(SELECT_ALL, MUSCLE_MAPPER, limit, offset);
        return muscleList;
    }


    public Muscle getMuscleById(int muscleId) throws DAOException {
        Muscle muscle = null;
        try {
            muscle = jdbcTemplate.queryForObject(FIND_MUSCLE_BY_ID_QUERY,
                    new BeanPropertyRowMapper<>(Muscle.class), muscleId);
        } catch (Exception e) {
            throw new DAOException("Cant find muscle by this id:" + muscleId+ " " + e);
        }
        return muscle;
    }

}
