package by.itacademy.javaenterprise.command.Impl.user;

import by.itacademy.javaenterprise.command.Command;
import by.itacademy.javaenterprise.dao.TrainingDAO;
import by.itacademy.javaenterprise.entity.Training;
import by.itacademy.javaenterprise.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("UpdateTraining")
public class UpdateTrainingCommand implements Command<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateTrainingCommand.class);
    @Autowired
    TrainingDAO<Training> training;


    @Override
    public void execute(Integer trainingId) throws DAOException {
        List<Training> list = training.getAllTrainingsPagination(100, 0);
        for (Training item : list) {
            if (item.getId() == 1) {
               item.setDate(item.getDate().plusDays(3L));
                System.out.println(item);
                training.updateTraining(item);
            }
        }
    }
}
