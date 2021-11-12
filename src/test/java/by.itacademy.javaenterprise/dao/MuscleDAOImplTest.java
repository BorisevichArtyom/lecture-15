package by.itacademy.javaenterprise.dao;

import by.itacademy.javaenterprise.dao.impl.MuscleDAOImpl;
import by.itacademy.javaenterprise.entity.Muscle;
import by.itacademy.javaenterprise.exception.DAOException;
import by.itacademy.javaenterprise.spring.SpringConfig;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class MuscleDAOImplTest {
    private  MuscleDAOImpl muscleDAO;
    private  JdbcTemplate jdbcMock;
    private static Muscle muscle = Muscle.builder().muscleName("XXX").muscleId(2).build();
    private static List<Muscle> list = new ArrayList<>();

    static {
        list.add(muscle);
    }

    @BeforeEach
    public void setup()  {
        jdbcMock = Mockito.mock(JdbcTemplate.class);
        muscleDAO = new MuscleDAOImpl(jdbcMock);
        when(jdbcMock.queryForObject(anyString(), Mockito.<BeanPropertyRowMapper<Muscle>>any(),
                anyInt())).thenReturn(muscle);
        when(jdbcMock.query(anyString(), Mockito.<RowMapper<Muscle>>any(),
                anyInt(), anyInt())).thenReturn(list);

    }

    @Test
    public void testGetAllMusclesPagination() throws DAOException {
        assertNotNull(muscleDAO.getAllMusclesPagination(1, 0));
        assertEquals(1, muscleDAO.getAllMusclesPagination(1, 0).size());
    }


    @Test
    public void testGetMuscleById() throws DAOException {
        assertNotNull(muscleDAO.getMuscleById(1));
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        when(muscleDAO.getMuscleById(muscle.getMuscleId())).thenReturn(muscle);
        assertEquals(muscle.getMuscleName(), muscleDAO.getMuscleById(2).getMuscleName());
        doThrow(new NullPointerException()).when(muscleDAO).getMuscleById(0);
        assertThrows(Exception.class, () -> muscleDAO.getMuscleById(0));

    }


    @Test
    public void testUpdateMuscle() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);

        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);
            assertEquals(muscle, arg0);
            assertEquals(muscle.getMuscleId(), arg1);
            return null;
        }).when(muscleDAO).updateMuscle(any(Muscle.class), any(Integer.class));

        Mockito.doThrow(new NullPointerException()).when(jdbcMock).update(anyString(), anyInt(), anyString());
        assertThrows(Exception.class,
                () -> {
                    jdbcMock.update("update...", 2, "Bycep");
                });

        Mockito.doThrow(new NullPointerException()).when(muscleDAO).updateMuscle(any(), anyInt());
        assertThrows(Exception.class,
                () -> {
                    muscleDAO.updateMuscle(muscle, 2);
                });
    }


    @Test
    public void testUpdateMuscleInvalid() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        doThrow(new NullPointerException()).when(muscleDAO).updateMuscle(isNull(), anyInt());
        assertThrows(Exception.class,
                () -> {
                    muscleDAO.updateMuscle(null, 2);
                });

    }


    @Test
    public void testAddMuscle() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        Mockito.doNothing().when(muscleDAO).addMuscle(any());
        muscleDAO.addMuscle(muscle);
        Mockito.verify(muscleDAO, times(1)).addMuscle(muscle);

    }


    @Test
    public void testAddMuscleNull() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        doThrow(new NullPointerException()).when(muscleDAO).addMuscle(isNull());
        assertThrows(Exception.class, () -> muscleDAO.addMuscle(null));
    }

    @Test
    public void testAddMuscleConflict() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        muscleDAO.addMuscle(muscle);
        muscleDAO.addMuscle(muscle);
        Mockito.verify(muscleDAO, times(2)).addMuscle(muscle);

    }


    @Test
    public void testDeleteMuscle() throws DAOException {
        muscleDAO = Mockito.mock(MuscleDAOImpl.class);
        doThrow(new NullPointerException()).when(muscleDAO).deleteMuscle(muscle.getMuscleName());
        assertThrows(Exception.class, () -> muscleDAO.deleteMuscle(muscle.getMuscleName()));
    }
}
