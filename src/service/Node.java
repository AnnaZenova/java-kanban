package service;
import model.Task;

class Node {

    Task data;
    Node next;
    Node prev;

    Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}