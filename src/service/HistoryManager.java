package service;
import java.util.ArrayList;
import java.util.List;

import model.Task;



public interface HistoryManager {
    List<Task> getHistory();
    void add(Task task); //должен помечать задачи как просмотренные
}
