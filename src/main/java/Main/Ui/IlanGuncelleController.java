package Main.Ui;

import Main.Emlak.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class IlanGuncelleController {

    @FXML private TextField txtIlanNo;
    @FXML private Button btnGetir; // İlan No yanına eklenecek buton

    // Mevcut Alanlar
    @FXML private TextField txtYeniBaslik;
    @FXML private TextField txtYeniFiyat;
    @FXML private TextField txtYeniIl;
    @FXML private TextField txtYeniIlce;
    @FXML private TextField txtYeniMahalle;

    // YENİ EKLENEN ALANLAR
    @FXML private TextField txtYeniMetrekare;
    @FXML private TextField txtYeniOda;
    @FXML private TextField txtYeniKat;

    @FXML private Button btnGuncelle;

    private Ilan bulunanIlan = null; // İşlem yapılacak ilan

    @FXML
    public void initialize() {
        // Butonlara tıklama olaylarını bağla
        btnGetir.setOnAction(e -> ilaniGetir());
        btnGuncelle.setOnAction(e -> guncellemeYap());

        // Başlangıçta güncelle butonu pasif olabilir veya alanlar temiz
        formuTemizle();
    }

    // --- 1. İLANI BUL VE GETİR ---
    private void ilaniGetir() {
        String noStr = txtIlanNo.getText().trim();
        if (noStr.isEmpty()) {
            uyariVer("Hata", "Lütfen bir İlan No giriniz.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(noStr);
        } catch (NumberFormatException e) {
            uyariVer("Hata", "İlan No sadece rakam olmalıdır.");
            return;
        }

        // İlanı Controller'dan bul
        bulunanIlan = null;
        if (MainViewController.ilanController != null) {
            for (Ilan i : MainViewController.ilanController.getIlanlar()) {
                if (i.getIlanNo() == id) {
                    bulunanIlan = i;
                    break;
                }
            }
        }

        if (bulunanIlan == null) {
            uyariVer("Bulunamadı", "Bu numaraya ait bir ilan bulunamadı.");
            formuTemizle();
            return;
        }

        // --- VERİLERİ KUTUCUKLARA DOLDUR ---

        // 1. Ortak Bilgiler (Placeholder olarak eski veriyi gösteriyoruz)
        txtYeniBaslik.setPromptText(bulunanIlan.getIlanBaslik());
        txtYeniFiyat.setPromptText(String.format("%.0f", bulunanIlan.getFiyat()));
        txtYeniIl.setPromptText(bulunanIlan.getAdres().getIl());
        txtYeniIlce.setPromptText(bulunanIlan.getAdres().getIlce());
        txtYeniMahalle.setPromptText(bulunanIlan.getAdres().getMahalle());

        // 2. Türe Göre Detaylar
        if (bulunanIlan instanceof Daire) {
            Daire d = (Daire) bulunanIlan;
            txtYeniMetrekare.setText(String.valueOf(d.getMetrekare()));
            txtYeniOda.setText(String.valueOf(d.getoda()));
            txtYeniKat.setText(String.valueOf(d.getkacincikat()));

            alanlariAc(true); // Oda ve Kat açık

        } else if (bulunanIlan instanceof MustakilEv) {
            MustakilEv m = (MustakilEv) bulunanIlan;
            txtYeniMetrekare.setText(String.valueOf(m.getMetrekare()));
            txtYeniOda.setText(String.valueOf(m.getOda()));
            txtYeniKat.setText(String.valueOf(m.getKackat()));

            alanlariAc(true); // Oda ve Kat açık

        } else if (bulunanIlan instanceof Arsa) {
            Arsa a = (Arsa) bulunanIlan;
            txtYeniMetrekare.setText(String.valueOf(a.getMetrekare()));

            // Arsada Oda ve Kat yoktur -> Temizle ve Kilitle
            txtYeniOda.clear();
            txtYeniKat.clear();
            txtYeniOda.setDisable(true);
            txtYeniKat.setDisable(true);
            txtYeniOda.setPromptText("Arsada Yok");
            txtYeniKat.setPromptText("Arsada Yok");
        }

        bilgiVer("İlan Getirildi", "İlan bilgileri yüklendi. Değiştirmek istediklerinizi düzenleyip 'Güncelle'ye basınız.");
    }

    // --- 2. GÜNCELLEME İŞLEMİ (SIKI KONTROLLER) ---
    private void guncellemeYap() {
        if (bulunanIlan == null) {
            uyariVer("Hata", "Önce İlan No girip 'Getir' butonuna basmalısınız.");
            return;
        }

        try {
            // --- BAŞLIK KONTROLÜ ---
            String baslik = txtYeniBaslik.getText().trim();
            if (!baslik.isEmpty()) { // Boş değilse güncelle
                if (baslik.length() > 30) throw new Exception("Başlık max 30 karakter olabilir.");
                if (!baslik.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+")) throw new Exception("Başlıkta rakam veya sembol olamaz.");

                int boslukSayisi = baslik.length() - baslik.replace(" ", "").length();
                if (boslukSayisi > 4) throw new Exception("Başlıkta max 4 boşluk olabilir.");

                bulunanIlan.setIlanBaslik(baslik);
            }

            // --- FİYAT KONTROLÜ ---
            String fiyatStr = txtYeniFiyat.getText().trim();
            if (!fiyatStr.isEmpty()) {
                if (!fiyatStr.matches("\\d+")) throw new Exception("Fiyat sadece rakam olmalı.");
                long fiyat = Long.parseLong(fiyatStr);
                if (fiyat < 1000000 || fiyat > 30000000) throw new Exception("Fiyat 1M ile 30M TL arasında olmalı.");

                bulunanIlan.setFiyat(fiyat);
            }

            // --- ADRES KONTROLLERİ ---
            String il = txtYeniIl.getText().trim();
            String ilce = txtYeniIlce.getText().trim();
            String mah = txtYeniMahalle.getText().trim();

            if (!il.isEmpty()) {
                if (!il.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+")) throw new Exception("İl isminde rakam/sembol olamaz.");
                if (il.length() > 15) throw new Exception("İl ismi max 15 karakter.");
                bulunanIlan.getAdres().setIl(il);
            }
            if (!ilce.isEmpty()) {
                if (!ilce.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+")) throw new Exception("İlçe isminde rakam/sembol olamaz.");
                if (ilce.length() > 15) throw new Exception("İlçe ismi max 15 karakter.");
                bulunanIlan.getAdres().setIlce(ilce);
            }
            if (!mah.isEmpty()) {
                if (!mah.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+")) throw new Exception("Mahalle isminde rakam/sembol olamaz.");
                if (mah.length() > 20) throw new Exception("Mahalle ismi max 20 karakter.");
                bulunanIlan.getAdres().setMahalle(mah);
            }

            // --- METREKARE KONTROLÜ ---
            String m2Str = txtYeniMetrekare.getText().trim();
            if (!m2Str.isEmpty()) {
                if (!m2Str.matches("\\d+(\\.\\d+)?")) throw new Exception("Metrekare sayı olmalı (örn: 120 veya 120.5).");
                double m2 = Double.parseDouble(m2Str);
                if (m2 < 70 || m2 > 500) throw new Exception("Metrekare 70 ile 500 arasında olmalı.");

                // Tipe göre set et
                if (bulunanIlan instanceof Daire) ((Daire)bulunanIlan).setMetrekare(m2);
                else if (bulunanIlan instanceof MustakilEv) ((MustakilEv)bulunanIlan).setMetrekare(m2);
                else if (bulunanIlan instanceof Arsa) ((Arsa)bulunanIlan).setMetrekare(m2);
            }

            // --- ODA VE KAT KONTROLÜ (Sadece Evler İçin) ---
            if (bulunanIlan instanceof Daire || bulunanIlan instanceof MustakilEv) {

                // ODA
                String odaStr = txtYeniOda.getText().trim();
                if (!odaStr.isEmpty()) {
                    if (!odaStr.matches("\\d+")) throw new Exception("Oda sayısı rakam olmalı.");
                    int oda = Integer.parseInt(odaStr);
                    if (oda < 1 || oda > 8) throw new Exception("Oda sayısı 1-8 arası olmalı.");

                    if (bulunanIlan instanceof Daire) ((Daire)bulunanIlan).setoda(oda);
                    else ((MustakilEv)bulunanIlan).setOda(oda);
                }

                // KAT
                String katStr = txtYeniKat.getText().trim();
                if (!katStr.isEmpty()) {
                    if (!katStr.matches("\\d+")) throw new Exception("Kat sayısı rakam olmalı.");
                    int kat = Integer.parseInt(katStr);
                    if (kat < 1 || kat > 4) throw new Exception("Kat sayısı 1-4 arası olmalı.");

                    if (bulunanIlan instanceof Daire) ((Daire)bulunanIlan).setkacincikat(kat);
                    else ((MustakilEv)bulunanIlan).setKackat(kat);
                }
            }

            // --- DOSYAYA KAYDET ---
            // Değişiklikler RAM'deki objeye yapıldı, şimdi TXT'ye yazalım.
            if (MainViewController.ilanController != null) {
                MainViewController.ilanController.txtKaydet();
                bilgiVer("Başarılı", "İlan başarıyla güncellendi.");

                // Formu temizle ve resetle
                formuTemizle();
                bulunanIlan = null;
            }

        } catch (NumberFormatException e) {
            uyariVer("Biçim Hatası", "Sayısal alanlara lütfen harf girmeyiniz.");
        } catch (Exception e) {
            uyariVer("Hata", e.getMessage());
        }
    }

    private void alanlariAc(boolean durum) {
        if (txtYeniOda != null) txtYeniOda.setDisable(!durum);
        if (txtYeniKat != null) txtYeniKat.setDisable(!durum);
    }

    private void formuTemizle() {
        txtIlanNo.clear();
        txtYeniBaslik.clear(); txtYeniBaslik.setPromptText("");
        txtYeniFiyat.clear(); txtYeniFiyat.setPromptText("");
        txtYeniIl.clear(); txtYeniIl.setPromptText("");
        txtYeniIlce.clear(); txtYeniIlce.setPromptText("");
        txtYeniMahalle.clear(); txtYeniMahalle.setPromptText("");

        if (txtYeniMetrekare != null) txtYeniMetrekare.clear();
        if (txtYeniOda != null) { txtYeniOda.clear(); txtYeniOda.setDisable(false); txtYeniOda.setPromptText(""); }
        if (txtYeniKat != null) { txtYeniKat.clear(); txtYeniKat.setDisable(false); txtYeniKat.setPromptText(""); }
    }

    private void uyariVer(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }

    private void bilgiVer(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        alert.showAndWait();
    }
}