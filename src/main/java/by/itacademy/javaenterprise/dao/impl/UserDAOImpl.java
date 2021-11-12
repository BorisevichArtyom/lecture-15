package by.itacademy.javaenterprise.dao.impl;

import by.itacademy.javaenterprise.dao.UserDAO;
import by.itacademy.javaenterprise.entity.Role;
import by.itacademy.javaenterprise.entity.User;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO<User> {

    private static final String ADD_QUERY = "INSERT Diary_user(email,user_password,first_name,last_name," +
            "balance_amount,user_role_id)" +
            "VALUES (?,?,?,?,?,?)";
    private static final int EMAIL_ADD_QUERY_INDEX = 1;
    private static final int PASSWORD_ADD_QUERY_INDEX = 2;
    private static final int FIRST_NAME_ADD_QUERY_INDEX = 3;
    private static final int LAST_NAME_ADD_QUERY_INDEX = 4;
    private static final int BALANCE_AMOUNT_ADD_QUERY_INDEX = 5;
    private static final int ROLE_INDEX_ADD_QUERY = 6;

    private static final String UPDATE_QUERY = "UPDATE Diary_user " +
            "SET EMAIL=?,USER_PASSWORD=?,FIRST_NAME=?,LAST_NAME=?,BALANCE_AMOUNT=?,USER_ROLE_ID=? WHERE USER_ID=?";
    private static final int UPDATE_QUERY_EMAIL_INDEX = 1;
    private static final int UPDATE_QUERY_PASSWORD_INDEX = 2;
    private static final int UPDATE_QUERY_FIRST_NAME_INDEX = 3;
    private static final int UPDATE_QUERY_LAST_NAME_INDEX = 4;
    private static final int UPDATE_QUERY_BALANCE_AMOUNT_INDEX = 5;
    private static final int UPDATE_QUERY_ROLE_INDEX = 6;
    private static final int UPDATE_QUERY_CONDITION_USER_ID = 7;

    private static final String SELECT_ID_BY_EMAIL = "SELECT USER_ID FROM Diary_user WHERE EMAIL =?";
    private static final int SELECT_ID_USER_ID_QUERY_INDEX = 1;
    private static final int SELECT_ID_EMAIL_QUERY_INDEX = 1;

    public static final String SELECT_ALL =
            "SELECT USER_ID,EMAIL,USER_PASSWORD,FIRST_NAME,LAST_NAME,BALANCE_AMOUNT,USER_ROLE_ID" +
                    " FROM Diary_user LIMIT ? OFFSET ?";

    public static final String FIND_USER_BY_ID =
            "SELECT EMAIL,USER_PASSWORD,FIRST_NAME,LAST_NAME,BALANCE_AMOUNT,USER_ROLE_ID FROM Diary_user WHERE USER_ID=?";

    private static final String DELETE_QUERY = "DELETE FROM Diary_user WHERE EMAIL=?";
    private static final int EMAIL_DELETE_QUERY_INDEX = 1;

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private static DataSource connect;

    private  JdbcTemplate jdbcTemplate;

    RowMapper<User> USER_MAPPER = (ResultSet rs, int rowNum) -> {
        return User.builder().id(rs.getInt(1))
                .email(rs.getString(2))
                .password(rs.getString(3))
                .firstName(rs.getString(4))
                .lastName(rs.getString(5))
                .balanceAmount(rs.getBigDecimal(6))
                .role(Role.getRoleByid(rs.getInt(7))).build();
    };

    RowMapper<User> USER_MAPPER_WITHOUT_ID = (ResultSet rs, int rowNum) -> {
        return User.builder()
                .email(rs.getString(1))
                .password(rs.getString(2))
                .firstName(rs.getString(3))
                .lastName(rs.getString(4))
                .balanceAmount(rs.getBigDecimal(5))
                .role(Role.getRoleByid(rs.getInt(6))).build();
    };


    @Override
    public void addUser(User user) throws DAOException {
        try (Connection con = connect.getConnection();
             PreparedStatement pst = con.prepareStatement(ADD_QUERY)) {
            prepareAddStatement(pst, user);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while adding a user" + user.toString()+ " " + e);
        }
    }

    @Override
    public void updateUser(User user) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int idUser = findIdUserByEmail(user);
            prepareUpdateStatement(statement, user, idUser);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while updating a user" + user.toString()+ " " + e);

        }
    }

    @Override
    public void deleteUser(User user) throws DAOException {
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            prepareDeleteStatement(statement, user);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while deleting a user" + user.toString()+ " " + e);
        }
    }

    @Override
    public List<User> getAllUsersPagination(int limit, int offset) throws DAOException {

        List<User> users = jdbcTemplate.query(SELECT_ALL, USER_MAPPER, limit, offset);
        return users;
    }


    public static int findIdUserByEmail(User user) throws DAOException {
        Integer userID = null;
        try (Connection connection = connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ID_BY_EMAIL)) {
            prepareSelectStatement(statement, user.getEmail());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                userID = rs.getInt(SELECT_ID_USER_ID_QUERY_INDEX);
            }

        } catch (SQLException e) {
            logger.error("Error with select by id"+ " " + e);
        }
        return userID;
    }

    public User getUserById(int userId) throws DAOException {
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(FIND_USER_BY_ID,
                    USER_MAPPER_WITHOUT_ID, userId);
            user.setId(userId);
        } catch (Exception e) {
            throw new DAOException("Cant find user by this id:" + userId+ " " + e);
        }
        return user;

    }

    private static void prepareSelectStatement(PreparedStatement statement, String email) throws SQLException {
        statement.setString(SELECT_ID_EMAIL_QUERY_INDEX, email);
    }

    private void prepareDeleteStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(EMAIL_DELETE_QUERY_INDEX, user.getEmail());
    }

    private void prepareAddStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(EMAIL_ADD_QUERY_INDEX, user.getEmail());
        statement.setString(PASSWORD_ADD_QUERY_INDEX, user.getPassword());
        statement.setString(FIRST_NAME_ADD_QUERY_INDEX, user.getFirstName());
        statement.setString(LAST_NAME_ADD_QUERY_INDEX, user.getLastName());
        statement.setBigDecimal(BALANCE_AMOUNT_ADD_QUERY_INDEX, user.getBalanceAmount());
        statement.setInt(ROLE_INDEX_ADD_QUERY, user.getRole().getId());

    }

    private void prepareUpdateStatement(PreparedStatement statement, User user, int idUser) throws SQLException, DAOException {
        statement.setString(UPDATE_QUERY_EMAIL_INDEX, user.getEmail());
        statement.setString(UPDATE_QUERY_PASSWORD_INDEX, user.getPassword());
        statement.setString(UPDATE_QUERY_FIRST_NAME_INDEX, user.getFirstName());
        statement.setString(UPDATE_QUERY_LAST_NAME_INDEX, user.getLastName());
        statement.setBigDecimal(UPDATE_QUERY_BALANCE_AMOUNT_INDEX, user.getBalanceAmount());
        statement.setInt(UPDATE_QUERY_ROLE_INDEX, user.getRole().getId());
        statement.setInt(UPDATE_QUERY_CONDITION_USER_ID, idUser);
    }

    @Autowired
    public static void setConnect(DataSource connect) {
        UserDAOImpl.connect = connect;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
