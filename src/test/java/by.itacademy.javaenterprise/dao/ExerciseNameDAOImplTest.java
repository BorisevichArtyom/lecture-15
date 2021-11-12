package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.dao.impl.ExerciseNameDAOImpl;
import by.itacademy.javaenterprise.entity.ExerciseName;
import by.itacademy.javaenterprise.exception.DAOException;
import by.itacademy.javaenterprise.spring.SpringConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ExerciseNameDAOImplTest {

    private static DataSource ds;

    private static MySQLContainer<?> mysqlOldVersion = new MySQLContainer<>(DockerImageName.parse("mysql:5.7"))
            .withDatabaseName("demodb")
            .withUsername("xxx")
            .withPassword("xxx");
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);

    ExerciseName exerciseNamee = ExerciseName.builder().exerciseNameId(8).exerciseName("Chin-ups").build();
    ExerciseNameDAOImpl daoImpl = new ExerciseNameDAOImpl(namedParameterJdbcTemplate, mapSqlParameterSource);

    @BeforeAll
    public static void beforeClass() throws Exception {
        mysqlOldVersion.start();

        ds = new DriverManagerDataSource(mysqlOldVersion.getJdbcUrl(),
                mysqlOldVersion.getUsername(), mysqlOldVersion.getPassword());
        JdbcConnection jdbcConnection = new JdbcConnection(ds.getConnection());
        Database database = DatabaseFactory.getInstance().
                findCorrectDatabaseImplementation(jdbcConnection);

        Liquibase liquibase = new liquibase.Liquibase("liquibase/db.migration/changelog.xml"
                , new ClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts());
    }

    @Test
    public void testGetAllExerciseNamesPagination() throws DAOException {
        assertNotNull(daoImpl.getAllExerciseNamesPagination(1, 0));
        assertEquals(2, daoImpl.getAllExerciseNamesPagination(2, 0).size());
    }


    @Test
    public void testGetExerciseNameById() throws DAOException {
        assertNotNull(daoImpl.getExerciseNameById(1));
        assertThrows(DAOException.class, () -> daoImpl.getExerciseNameById(0));
    }

    @Test
    public void testAddNameOfExercise() throws DAOException {
        assertThrows(Exception.class, () -> daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.addNameOfExercise(exerciseNamee);
        assertEquals(exerciseNamee.getExerciseName(), daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.deleteExerciseName(exerciseNamee.getExerciseNameId());

    }

    @Test
    public void testAddNameOfExerciseNull() {
        assertThrows(Exception.class, () -> daoImpl.addNameOfExercise(new ExerciseName()));
    }

    @Test
    public void testAddNameConflict() throws DAOException {
        assertThrows(Exception.class, () -> daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.addNameOfExercise(exerciseNamee);
        assertThrows(Exception.class, () -> daoImpl.addNameOfExercise(exerciseNamee));
        daoImpl.deleteExerciseName(exerciseNamee.getExerciseNameId());
    }


    @Test
    public void testUpdateExerciseName() throws DAOException {
        daoImpl.addNameOfExercise(exerciseNamee);
        assertEquals(exerciseNamee.getExerciseName(), daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        exerciseNamee.setExerciseName("Plank");
        daoImpl.updateExerciseName(exerciseNamee, exerciseNamee.getExerciseNameId());
        assertEquals("Plank", daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.deleteExerciseName(exerciseNamee.getExerciseNameId());
    }

    @Test
    public void testUpdateExerciseNameInvalid() throws DAOException {
        assertNotNull(daoImpl.getExerciseNameById(1));
        assertThrows(Exception.class, () -> {
            daoImpl.updateExerciseName(new ExerciseName(), 1);
        });
    }


    @Test
    public void testDeleteExerciseName() throws DAOException {
        assertThrows(Exception.class, () -> daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.addNameOfExercise(exerciseNamee);
        assertEquals(exerciseNamee.getExerciseName(), daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
        daoImpl.deleteExerciseName(exerciseNamee.getExerciseNameId());
        assertThrows(Exception.class, () -> daoImpl.getExerciseNameById(exerciseNamee.getExerciseNameId()));
    }
}
