package by.itacademy.javaenterprise.borisevich;

import by.itacademy.javaenterprise.borisevich.entity.Role;
import by.itacademy.javaenterprise.borisevich.command.Command;
import by.itacademy.javaenterprise.borisevich.dao.impl.UserDAOImpl;
import by.itacademy.javaenterprise.borisevich.entity.User;
import by.itacademy.javaenterprise.borisevich.exception.DAOException;
import by.itacademy.javaenterprise.borisevich.spring.SpringConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.exception.LiquibaseException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Main {
    private Command<Integer> qualifierExample;

    public Main(Command<Integer> qualifierExample) {
        this.qualifierExample = qualifierExample;
    }

    public void executeSmth(Integer number) throws DAOException {
        qualifierExample.execute(number);
    }


    public static void main(String[] args) throws DAOException, SQLException, LiquibaseException {
        User user = User.builder().email("griiib@tut.by")
                .password("Bicep").firstName("Denis")
                .lastName("Samusenko").balanceAmount(new BigDecimal("123.44"))
                .role(Role.ATHLETE).build();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserDAOImpl daoImpl = context.getBean(UserDAOImpl.class);
        HikariDataSource hikariDataSource=context.getBean(HikariDataSource.class);

        System.out.println(daoImpl.getAllUsersPagination(3, 1));
        System.out.println(daoImpl.getUserById(4));
    }
}
