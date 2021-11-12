package by.itacademy.javaenterprise.dao.impl;

import by.itacademy.javaenterprise.dao.ExerciseNameDAO;
import by.itacademy.javaenterprise.entity.ExerciseName;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Collections;
import java.util.List;

@Component
public class ExerciseNameDAOImpl implements ExerciseNameDAO<ExerciseName> {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseNameDAOImpl.class);

    private static final String ADD_QUERY = "INSERT Exercises_name(Name_Exercises_id,Name) VALUES(:id,:name)";

    private static final String UPDATE_QUERY = "UPDATE Exercises_name SET Name=:name WHERE Name_Exercises_id=:id";

    private static final String DELETE_QUERY = "DELETE FROM Exercises_name WHERE Name_Exercises_id=:id";

    public static final String SELECT_ALL = "SELECT Name_Exercises_id,Name From Exercises_name " +
            "LIMIT :limit OFFSET :offset";;

    private static final String FIND_EXERCISE_NAME_BY_ID_QUERY = "SELECT Name From Exercises_name " +
            "WHERE Name_Exercises_id=:id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MapSqlParameterSource mapSqlParameterSource;

    RowMapper<ExerciseName> EXERCISE_NAME_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new ExerciseName(resultSet.getInt(1), (resultSet.getString(2)));
    };

    RowMapper<ExerciseName> EXERCISE_MAPPER_ONLY_NAME = (ResultSet resultSet, int rowNum) -> {
        return ExerciseName.builder().exerciseName(resultSet.getString(1)).build();
    };

    public ExerciseNameDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               MapSqlParameterSource mapSqlParameterSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.mapSqlParameterSource = mapSqlParameterSource;
    }


    @Override
    public void addNameOfExercise(ExerciseName exerciseName) throws DAOException {
        mapSqlParameterSource.addValue("id", exerciseName.getExerciseNameId());
        mapSqlParameterSource.addValue("name", new SqlLobValue(exerciseName.getExerciseName(),
                new DefaultLobHandler()), Types.CLOB);
        try {
            namedParameterJdbcTemplate.update(ADD_QUERY, mapSqlParameterSource);
        } catch (Exception e) {
            throw new DAOException("Cant add exercise name:" + exerciseName + " " + e);
        }
    }

    @Override
    public void updateExerciseName(ExerciseName exerciseName, int exerciseNameId) throws DAOException {
        mapSqlParameterSource.addValue("id", exerciseNameId);
        mapSqlParameterSource.addValue("name", exerciseName.getExerciseName());
        try {
            namedParameterJdbcTemplate.update(UPDATE_QUERY, mapSqlParameterSource);
        } catch (Exception e) {
            throw new DAOException("Cant update exercise name with this id:" + exerciseNameId+ " " + e);
        }

    }

    @Override
    public void deleteExerciseName(int exerciseNameId) throws DAOException {
        try {
            namedParameterJdbcTemplate.update(DELETE_QUERY, Collections.singletonMap("id", exerciseNameId));
        } catch (DataAccessException e) {
            throw new DAOException("Cant delete exercise name with this id:" + exerciseNameId+ " " + e);
        }
    }

    @Override
    public List<ExerciseName> getAllExerciseNamesPagination(int limit, int offset) throws DAOException {
        mapSqlParameterSource.addValue("limit", limit);
        mapSqlParameterSource.addValue("offset", offset);

        List<ExerciseName> exerciseNames = namedParameterJdbcTemplate.query(SELECT_ALL,
                mapSqlParameterSource, EXERCISE_NAME_MAPPER);
        return exerciseNames;

    }

    public String getExerciseNameById(int exerciseNameId) throws DAOException {
        String exerciseNameById;
        mapSqlParameterSource.addValue("id", exerciseNameId);
        try {
            ExerciseName exerciseName = namedParameterJdbcTemplate.queryForObject(FIND_EXERCISE_NAME_BY_ID_QUERY
                    , mapSqlParameterSource, EXERCISE_MAPPER_ONLY_NAME);
            exerciseNameById = exerciseName.getExerciseName();
        } catch (Exception e) {
            throw new DAOException("Cant find exercise name by this id:" + e);
        }
        return exerciseNameById;
    }


}
