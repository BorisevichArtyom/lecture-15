package by.itacademy.javaenterprise.command.Impl.user;

import by.itacademy.javaenterprise.command.Command;
import by.itacademy.javaenterprise.dao.TrainingDAO;
import by.itacademy.javaenterprise.entity.Training;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AddTrainingCommand implements Command<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AddTrainingCommand.class);
    @Autowired
    TrainingDAO<Training> training;
    @Override
    public void execute(Integer id)  {
       Training trainingdate = Training.builder().id(id).date(LocalDateTime.of(2015, 12, 8, 12, 30)).userID(1).build();
        try {
            training.addTraining(trainingdate);
        } catch (DAOException e) {
           logger.error("Error while adding training",e);
        }
    }
}
