package Main.User;

public class Musteri {
    private int kullaniciId;
    private String ad;
    private String eposta;
    private String telefon;
    private String tc;
    private String emlakTipi;
    private double butce;
    private double minM2;
    private int minOda;

    public Musteri(int kullaniciId, String ad, String eposta, String telefon, String tc,
                   String emlakTipi, double butce, double minM2, int minOda) {
        this.kullaniciId = kullaniciId;
        this.ad = ad;
        this.eposta = eposta;
        this.telefon = telefon;
        this.tc = tc;
        this.emlakTipi = emlakTipi;
        this.butce = butce;
        this.minM2 = minM2;
        this.minOda = minOda;
    }

    // ================= GETTER/SETTER METOTLARI =================
    public int getkullaniciId() { return kullaniciId; }
    public String getAd() { return ad; }
    public String geteposta() { return eposta; }
    public String gettelefon() { return telefon; }
    public String getTc() { return tc; }
    public String getEmlakTipi() { return emlakTipi; }
    public double getButce() { return butce; }
    public double getMinM2() { return minM2; }
    public int getMinOda() { return minOda; }


    public void setAd(String ad) { this.ad = ad; }
    public void seteposta(String eposta) { this.eposta = eposta; }

    public String toFileString() {
        return kullaniciId + "|" + ad + "|" + eposta + "|" + telefon + "|" + tc + "|" +
                emlakTipi + "|" + butce + "|" + minM2 + "|" + minOda;
    }

    public static Musteri fromFileString(String line) {
        String[] parts = manualSplit(line, '|');

        if (parts.length < 9) { return null; }

        try {
            return new Musteri(
                    Integer.parseInt(parts[0].trim()),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    Double.parseDouble(parts[6].trim()),
                    Double.parseDouble(parts[7].trim()),
                    Integer.parseInt(parts[8].trim())
            );
        } catch (NumberFormatException e) {
            System.err.println("HATA: Dosyadan okunan satır sayısal formatı bozdu.");
            return null;
        }
    }
// Kısıtlamaya uygun String.split yerine kendi fonksiyonumuz
    private static String[] manualSplit(String str, char delimiter) {
        // Basit split implementasyonu
        String[] parts = new String[10];
        int partIndex = 0;
        int lastIndex = 0;

        for (int i = 0; i < str.length(); i = i + 1) {
            if (str.charAt(i) == delimiter) {
                parts[partIndex] = str.substring(lastIndex, i);
                lastIndex = i + 1;
                partIndex = partIndex + 1;
            }
        }
        parts[partIndex] = str.substring(lastIndex);

        String[] result = new String[partIndex + 1];
        for (int j = 0; j < partIndex + 1; j = j + 1) { result[j] = parts[j]; }
        return result;
    }


    private static int manualParseInt(String s) { return Integer.parseInt(s.trim()); }
    private static double manualParseDouble(String s) { return Double.parseDouble(s.trim()); }
}