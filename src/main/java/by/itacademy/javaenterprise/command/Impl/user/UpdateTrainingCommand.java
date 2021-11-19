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
    private TrainingDAO<Training> trainingDAO;

    @Override
    public void execute(Integer trainingId) throws DAOException {
        List<Training> list = trainingDAO.getAllTrainingsPagination(100, 0);
        for (Training item : list) {
            item.setDate(item.getDate().plusDays(3L));
            trainingDAO.updateTraining(item);
            logger.info("Query executed {}", item);
        }
    }
}
