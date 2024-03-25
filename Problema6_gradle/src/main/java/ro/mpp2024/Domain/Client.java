package ro.mpp2024.Domain;

public class Client extends Entity {
    private String nume;
    private String nrTelefon;
    private int id;

    public Client(int id,String nume, String nrTelefon) {
        super(id);
        this.nume = nume;
        this.nrTelefon = nrTelefon;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }
    public String getNrTelefon() {
        return nrTelefon;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "CLIENT: {" +
                "id: " + id +
                " nume: " + nume  +
                " nrTelefon: " + nrTelefon  +
                "}";
    }
}
