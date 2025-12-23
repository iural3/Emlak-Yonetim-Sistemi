package Main.User;

public class Node {
    private Musteri data;
    private Node next;

    public Node(Musteri data) {
        this.data = data;
        this.next = null;
    }

    public Musteri getData() {
        return data;
    }

    public void setData(Musteri data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}