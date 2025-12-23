package Main.User;

public class MusteriKuyrugu {

    class Node {
        Musteri data;
        Node next;
        public Node(Musteri data) { this.data = data; this.next = null; }
    }

    private Node front;
    private Node rear;

    public MusteriKuyrugu() { front = null; rear = null; }

    // ================= EKLEME =================
    public void ekle(Musteri m) {
        Node yeni = new Node(m);
        if (rear == null) { front = rear = yeni; return; }
        rear.next = yeni;
        rear = yeni;
    }

    // ================= BULMA =================
    public Musteri musteriBul(int id) {
        Node temp = front;
        while (temp != null) {
            if (temp.data.getkullaniciId() == id) { return temp.data; }
            temp = temp.next;
        }
        return null;
    }

    // ================= SİLME =================
    public boolean sil(int id) {
        if (front == null) return false;

        if (front.data.getkullaniciId() == id) {
            front = front.next;
            if (front == null) rear = null;
            return true;
        }

        Node temp = front;
        while (temp.next != null) {
            if (temp.next.data.getkullaniciId() == id) {
                temp.next = temp.next.next;
                if (temp.next == null) rear = temp;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }


    public Musteri[] kuyruguDiziyeCevir() {
        int boyut = 0;
        Node temp = front;
        while (temp != null) { boyut = boyut + 1; temp = temp.next; }

        Musteri[] dizi = new Musteri[boyut];
        temp = front;
        int i = 0;
        while (temp != null) {
            dizi[i] = temp.data;
            i = i + 1;
            temp = temp.next;
        }
        return dizi;
    }

    public void temizle() { front = null; rear = null; }
}