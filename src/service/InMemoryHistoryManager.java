package service;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

private static final int HISTORY_SIZE_TO_COMPARE = 10;

public final static List<Task> viewHistory = new ArrayList<>();

  @Override
    public List<Task> getHistory() {
    if (viewHistory.isEmpty()) {
            System.out.println("No view history");
        }
       return List.copyOf(viewHistory);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (viewHistory.size() >= HISTORY_SIZE_TO_COMPARE) {
            viewHistory.remove(0);
        }
        viewHistory.add(task);
        }
}
