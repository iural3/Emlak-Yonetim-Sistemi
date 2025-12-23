package Main.User;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class MusteriController {

    private MusteriKuyrugu musteriVeriYapisi;
    private final String DOSYA_ADI = "musteri_verileri.txt";

    public MusteriController(MusteriKuyrugu musteriVeriYapisi) {
        this.musteriVeriYapisi = musteriVeriYapisi;
        verileriDisktenYukle();
    }


    private boolean verileriDiskeKaydet() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(DOSYA_ADI));
            Musteri[] musteriler = musteriVeriYapisi.kuyruguDiziyeCevir();

            for (int i = 0; i < musteriler.length; i = i + 1) {
                String line = musteriler[i].toFileString();
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("KRİTİK I/O HATASI: Veri dosyaya yazılamadı: " + e.getMessage());
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) { /* Yoksay */ }
            }
        }
    }

    private void verileriDisktenYukle() {
        File file = new File(DOSYA_ADI);
        if (!file.exists()) { return; }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;

            musteriVeriYapisi.temizle();

            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    Musteri m = Musteri.fromFileString(line);
                    if (m != null) { musteriVeriYapisi.ekle(m); }
                }
            }

        } catch (IOException e) {
            System.err.println("HATA: Veri dosyası okunamadı: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) { /* Yoksay */ }
            }
        }
    }



    /** * Müşteri ekler. False dönmesi ID Çakışması veya I/O hatası demektir.
     */
    public boolean musteriEkle(Musteri m) {
        // KISITLAMA KONTROLÜ: ID Çakışması
        if (musteriVeriYapisi.musteriBul(m.getkullaniciId()) != null) {
            return false;
        }

        musteriVeriYapisi.ekle(m);
        return verileriDiskeKaydet(); // Kalıcılık sonucunu döndürür
    }

    public boolean musteriSil(int id) {
        boolean basarili = musteriVeriYapisi.sil(id);
        if (basarili) {
            return verileriDiskeKaydet();
        }
        return false;
    }

    public Musteri musteriBul(int id) {
        return musteriVeriYapisi.musteriBul(id);
    }

    public Musteri[] tumMusterileriGetir() {
        return musteriVeriYapisi.kuyruguDiziyeCevir();
    }
}