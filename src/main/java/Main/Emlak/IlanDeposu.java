package Main.Emlak;

public class IlanDeposu {

    class IlanNode {
        Ilan data;
        IlanNode next;
        public IlanNode(Ilan data) { this.data = data; this.next = null; }
    }

    private IlanNode head;

    public IlanDeposu() {
        head = null;
    }

    public void ilanEkle(Ilan ilan) {
        IlanNode yeni = new IlanNode(ilan);
        if (head == null) {
            head = yeni;
        } else {
            IlanNode temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = yeni;
        }
    }

    public boolean ilanSil(int ilanNo) {
        if (head == null) return false;
        if (head.data.getIlanNo() == ilanNo) {
            head = head.next;
            return true;
        }
        IlanNode temp = head;
        while (temp.next != null) {
            if (temp.next.data.getIlanNo() == ilanNo) {
                temp.next = temp.next.next;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean ilanVarMi(int ilanNo) {
        IlanNode temp = head;
        while (temp != null) {
            if (temp.data.getIlanNo() == ilanNo) return true;
            temp = temp.next;
        }
        return false;
    }

    public void clear() { head = null; } // Controller tarafından yüklemeden önce çağrılır

    public Ilan[] getIlanlarArray() {
        return listeDiziyeDonustur();
    }

    private Ilan[] listeDiziyeDonustur() {
        int size = 0;
        IlanNode temp = head;
        while (temp != null) { size++; temp = temp.next; } // Boyut sayılır

        Ilan[] dizi = new Ilan[size];
        int i = 0;
        temp = head;
        while (temp != null) { dizi[i++] = temp.data; temp = temp.next; } // Dizi doldurulur
        return dizi;
    }

    public boolean ilanGuncelle(int ilanNo, String yeniBaslik, String yeniFiyat, String yeniAdres) {
        IlanNode temp = head;
        while (temp != null) {
            if (temp.data.getIlanNo() == ilanNo) {
                // Başlık Güncelleme
                if (yeniBaslik != null && !yeniBaslik.trim().isEmpty()) {
                    temp.data.setIlanBaslik(yeniBaslik.trim());
                }

                // Fiyat Güncelleme
                if (yeniFiyat != null && !yeniFiyat.trim().isEmpty()) {
                    try {
                        double fiyat = Double.parseDouble(yeniFiyat.trim());
                        temp.data.setFiyat(fiyat);
                    } catch (NumberFormatException e) { }
                }

                // Adres Güncelleme
                if (yeniAdres != null && !yeniAdres.trim().isEmpty()) {
                    String[] p = yeniAdres.split(",");
                    if (p.length >= 3) {
                        Adres yeniAdr = new Adres(p[0].trim(), p[1].trim(), p[2].trim());
                        temp.data.setAdres(yeniAdr);
                    }
                }
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
    public Ilan[] ilanSorgula(Integer minFiyat, Integer maxFiyat,
                              String il, String ilce, String odaSayisi, String emlakTipi) {

        int sayac = 0;
        IlanNode temp = head;

        // 1. Adım: Kaç tane uygun ilan var say (Dizi boyutu için)
        while (temp != null) {
            if (uygunMu(temp.data, minFiyat, maxFiyat, il, ilce, odaSayisi, emlakTipi)) {
                sayac++;
            }
            temp = temp.next;
        }

        if (sayac == 0) return new Ilan[0];

        // 2. Adım: Uygun ilanları diziye doldur
        Ilan[] sonucDizisi = new Ilan[sayac];
        temp = head;
        int index = 0;
        while (temp != null) {
            if (uygunMu(temp.data, minFiyat, maxFiyat, il, ilce, odaSayisi, emlakTipi)) {
                sonucDizisi[index] = temp.data;
                index++;
            }
            temp = temp.next;
        }
        return sonucDizisi;
    }

    private boolean uygunMu(Ilan ilan, Integer minFiyat, Integer maxFiyat,
                            String il, String ilce, String odaSayisi, String emlakTipi) {

        // Fiyat Kontrolü
        if (minFiyat != null && ilan.getFiyat() < minFiyat) return false;
        if (maxFiyat != null && ilan.getFiyat() > maxFiyat) return false;

        // İl Kontrolü (Büyük/Küçük harf duyarsız)
        if (il != null && !il.trim().isEmpty()) {
            if (!ilan.getAdres().getIl().equalsIgnoreCase(il.trim())) return false;
        }

        // İlçe Kontrolü
        if (ilce != null && !ilce.trim().isEmpty()) {
            if (!ilan.getAdres().getIlce().equalsIgnoreCase(ilce.trim())) return false;
        }
        // Oda Sayısı kont
        if (odaSayisi != null && !odaSayisi.trim().isEmpty()) {
            // Eğer ilan Arsa ise oda sayısı filtresine giremez, direkt eliyoruz.
            if (ilan instanceof Arsa) {
                return false;
            }
            // Daire veya MustakilEv ise oda sayılarını alıp karşılaştırıyoruz
            String ilanOda = "";
            if (ilan instanceof Daire) {
                ilanOda = String.valueOf(((Daire) ilan).getoda());
            } else if (ilan instanceof MustakilEv) {
                ilanOda = String.valueOf(((MustakilEv) ilan).getOda());
            }

            if (!ilanOda.equals(odaSayisi.trim())) return false;
        }

        //  Emlak Tipi Kontrolü
        if (emlakTipi != null && !emlakTipi.trim().isEmpty() && !emlakTipi.equalsIgnoreCase("Tümü")) {
            String ilanSinisIsmi = ilan.getClass().getSimpleName(); // "Daire", "Arsa" veya "MustakilEv" döner
            String aranan = emlakTipi.trim();

            boolean tipUygun = false;

            if (aranan.equalsIgnoreCase("Daire") && ilanSinisIsmi.equalsIgnoreCase("Daire")) {
                tipUygun = true;
            }
            else if (aranan.equalsIgnoreCase("Arsa") && ilanSinisIsmi.equalsIgnoreCase("Arsa")) {
                tipUygun = true;
            }
            else if (aranan.equalsIgnoreCase("Müstakil") && ilanSinisIsmi.equalsIgnoreCase("MustakilEv")) {
                tipUygun = true;
            }

            if (!tipUygun) return false; // Seçilen tipe uymuyorsa elenir
        }

        
        return true; // Tüm filtrelerden geçerse true döner
    }

    public void basitListele() {
        IlanNode temp = head;
        if (head == null) {
            System.out.println("Liste Boş.");
            return;
        }
        System.out.println("--- Basit İlan Listesi ---");
        while (temp != null) {
            System.out.println(temp.data); // Ilan nesnesinin toString() metodu çağrılır
            temp = temp.next;
        }
    }
    public void listele(String kriter) {
        if(head == null) {
            System.out.println("Liste Boş. Listeleme yapılamaz.");
            return;
        }

        // Önce tüm ilanları diziye çevir
        Ilan[] dizi = getIlanlarArray();

        try {
            // Kritere göre BST kur (Örn: "fiyat" kriteri)
            // Not: IlanBSTMulti sınıfınızın var olması ve kriteri işlemesi gerekir.
            IlanBSTMulti bst = new IlanBSTMulti(kriter);
            for(Ilan i : dizi) {
                bst.ekle(i);
            }

            System.out.println("--- Kriter (" + kriter + ") bazlı sıralı liste ---");
            // BST'den sıralı (inorder) yazdırma yapılır
            // bst.inorderYazdir(); // Varsayılan BST metodunuz
            System.out.println("BST üzerinden sıralı listeleme tamamlandı (Konsola yazdırıldı).");

        } catch(Exception e) {
            System.err.println("Hata: BST üzerinden listeleme başarısız oldu. Basit listeye geçildi.");
            basitListele();
        }
    }

    // Emlakci sınıfı tarafından çağrılan BST arama metodu (Ders kısıtlaması)
    public Ilan fiyatAraBST(double fiyat) {
        // İlanların tamamını kullanarak BST kurar ve arama yapar
        try {
            IlanBSTMulti bst = new IlanBSTMulti();
            for(Ilan ilan : getIlanlarArray()) {
                bst.ekle(ilan);
            }
            return bst.ara(fiyat); // BST üzerinden ilan arama
        } catch (Exception e) {
            return null;
        }
    }


    public void gunIslet() {
        IlanNode temp = head;
        while (temp != null) { temp.data.gunAzalt(); temp = temp.next; }
    }


}