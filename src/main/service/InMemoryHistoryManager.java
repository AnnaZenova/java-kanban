package main.service;
import main.model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    //ArrayList<Task> viewHistory = new ArrayList<>();
   // InMemoryTaskManager taskManager = new InMemoryTaskManager();

  @Override
    public ArrayList<Task> getHistory() {
    if (viewHistory.isEmpty()) {
            System.out.println("No view history");
        }
        return viewHistory;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (viewHistory.size() > 9) {
            viewHistory.remove(0);
        }
        viewHistory.add(task);
        System.out.println("Добавлена задача с ID: " + task.getTaskId());
    }

}
