package service;
import java.util.List;
import model.Task;

public interface HistoryManager {
    void add(Task task); //должен помечать задачи как просмотренные

    void remove(int taskId);

    List<Task> getHistory();
}
