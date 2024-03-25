package ro.mpp2024.Domain;

public class Rezervare extends Entity {
    private Client client;
    private Excursie excursie;
    private int nrBilete;

    //private int id;
    public Rezervare(int id, Client client, Excursie excursie, int nrBilete) {
        super(id);
        this.client = client;
        this.excursie = excursie;
        this.nrBilete = nrBilete;
    }

    public Client getClient() {
        return client;
    }

    public Excursie getExcursie() {
        return excursie;
    }
    public int getNrBilete() {
        return nrBilete;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    public void setExcursie(Excursie excursie) {
        this.excursie = excursie;
    }
    public void setNrBilete(int nrBilete) {
        this.nrBilete = nrBilete;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "REZERVAREA: " +
                "ID: " + getId() +
                "\nClient: " + client.toString() +
                "\nExcursie: " + excursie.toString() +
                "\nNumar de bilete: " + nrBilete;
    }


}
