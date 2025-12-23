package Main.Emlak;

class MultiBSTNode {
    Ilan data;
    MultiBSTNode left, right;

    MultiBSTNode(Ilan data) {
        this.data = data;
    }
}

class IlanBSTMulti {

    private MultiBSTNode root;
    private String kriter;

    // -----------------------------------
    // ✔ Parametresiz Constructor → Varsayılan kriter = fiyat
    // -----------------------------------
    public IlanBSTMulti() {
        this.kriter = "fiyat";
    }

    // -----------------------------------
    // ✔ Seçmeli kriter constructor
    // -----------------------------------
    public IlanBSTMulti(String kriter) {
        if (kriter == null || kriter.trim().isEmpty())
            this.kriter = "fiyat";
        else
            this.kriter = kriter.toLowerCase();
    }

    // -----------------------------
    // EKLEME
    // -----------------------------
    public void ekle(Ilan ilan) {
        root = ekleRecursive(root, ilan);
    }

    private MultiBSTNode ekleRecursive(MultiBSTNode node, Ilan ilan) {

        if (node == null)
            return new MultiBSTNode(ilan);

        if (karsilastir(ilan, node.data) < 0)
            node.left = ekleRecursive(node.left, ilan);
        else
            node.right = ekleRecursive(node.right, ilan);

        return node;
    }

    // -----------------------------
    // KARŞILAŞTIR (SIRALAMA KRİTERİ)
    // -----------------------------
    private double karsilastir(Ilan a, Ilan b) {

        if (kriter.equals("fiyat"))
            return a.getFiyat() - b.getFiyat();

        if (kriter.equals("ilanno"))
            return a.getIlanNo() - b.getIlanNo();

        if (kriter.equals("metrekare"))
            return a.getMetrekare() - b.getMetrekare();

        if (kriter.equals("il"))
            return a.getAdres().getIl().compareToIgnoreCase(b.getAdres().getIl());

        if (kriter.equals("ilce"))
            return a.getAdres().getIlce().compareToIgnoreCase(b.getAdres().getIlce());

        if (kriter.equals("mahalle"))
            return a.getAdres().getMahalle().compareToIgnoreCase(b.getAdres().getMahalle());

        // default fiyat
        return a.getFiyat() - b.getFiyat();
    }

    // -----------------------------
    // ✔ FİYATA GÖRE ARAMA
    // -----------------------------
    public Ilan ara(double fiyat) {
        if (!kriter.equals("fiyat")) {
            System.out.println("Uyarı: Bu BST fiyat kriterine göre oluşturulmadı!");
        }
        return araRecursive(root, fiyat);
    }

    private Ilan araRecursive(MultiBSTNode node, double fiyat) {
        if (node == null)
            return null;

        if (fiyat == node.data.getFiyat())
            return node.data;

        if (fiyat < node.data.getFiyat())
            return araRecursive(node.left, fiyat);

        return araRecursive(node.right, fiyat);
    }

    // -----------------------------
    // INORDER YAZDIRMA
    // -----------------------------
    public void inorderYazdir() {
        inorder(root);
    }

    private void inorder(MultiBSTNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println(node.data.toString());
        inorder(node.right);
    }
}
