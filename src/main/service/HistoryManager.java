package main.service;

import main.model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    final static ArrayList<Task> viewHistory = new ArrayList<>();

    ArrayList<Task> getHistory();
    void add(Task task); //должен помечать задачи как просмотренные
}
