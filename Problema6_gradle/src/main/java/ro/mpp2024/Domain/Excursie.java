package ro.mpp2024.Domain;

public class Excursie implements IEntity<Integer> {
    private String obiectiv;
    private String firmaTransport;
    private int oraPlecare;
    private int pret;
    private int locuriDisponibile;

    private int id;

    public Excursie(String obiectiv, String firmaTransport, int oraPlecare, int pret, int locuriDisponibile) {
        this.obiectiv = obiectiv;
        this.firmaTransport = firmaTransport;
        this.oraPlecare = oraPlecare;
        this.pret = pret;
        this.locuriDisponibile = locuriDisponibile;
    }

    public String getObiectiv() {
        return obiectiv;
    }
    public String getFirmaTransport() {
        return firmaTransport;
    }
    public int getOraPlecare() {
        return oraPlecare;
    }
    public int getPret() {
        return pret;
    }
    public int getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public void setObiectiv(String obiectiv) {
        this.obiectiv = obiectiv;
    }
    public void setFirmaTransport(String firmaTransport) {
        this.firmaTransport = firmaTransport;
    }
    public void setOraPlecare(int oraPlecare) {
        this.oraPlecare = oraPlecare;
    }
    public void setPret(int pret) {
        this.pret = pret;
    }
    public void setLocuriDisponibile(int locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "EXCURSIA: {" +
                "ID: " + getId() +
                " Obiectiv: " + obiectiv +
                " Firma de transport: " + firmaTransport +
                " Ora de plecare: " + oraPlecare +
                " Pret: " + pret +
                " Locuri disponibile: " + locuriDisponibile + "}";
    }


}
