package service;
import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

private static final int HISTORY_SIZE_TO_COMPARE = 9;

  @Override
    public List<Task> getHistory() {
    if (viewHistory.isEmpty()) {
            System.out.println("No view history");
        }
      List<Task>copiedViewHistory = List.copyOf(viewHistory);
        return copiedViewHistory;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (viewHistory.size() > HISTORY_SIZE_TO_COMPARE) {
            viewHistory.remove(0);
        }
        viewHistory.add(task);
        }
}
