package service;
import model.Task;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final HistoryLinkedList<Task> historyLinkedTaskList;
    private final Map<Integer, Node<Task>> historyMapTask;

    public InMemoryHistoryManager() {
        historyLinkedTaskList = new HistoryLinkedList<>();
        historyMapTask = new HashMap<>();
    }

    @Override
    public void add(Task task) { //добавить просмотр задачи
        Node<Task> node = historyLinkedTaskList.addLast(task);
        if (historyMapTask.containsKey(task.getTaskId())) {
            remove(task.getTaskId());
        }
        historyMapTask.put(task.getTaskId(), node);
    }


    @Override
    public void remove(int taskId) { //удалить просмотр
        Node<Task> node = historyMapTask.get(taskId);
        if (node != null) {
            historyLinkedTaskList.removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyLinkedTaskList.getTasks(); //вернуть историю просмотров
    }


    class HistoryLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        private Node<T> addLast(T element) {  //добавить задачу в конец
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            return newNode;
        }

        private void removeNode(Node<T> node) { //удалить узел
            final Node<T> next = node.next;
            final Node<T> prev = node.prev;

            if (prev == null) { //если предыдущий узел null
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) { //если следующий узел null
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
        }


        private ArrayList<Task> getTasks() { // собираем все задачи из addLast в обычный ArrayList
            ArrayList<Task> taskList = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                taskList.add((Task) node.data);
                node = node.next;
            }
            return taskList;
        }
    }
}
