package at.htl.bank.model;

public class SparKonto extends BankKonto{

    private double zinsSatz = 0.03;

    public SparKonto(String name, double zinsSatz) {
        super(name);
        this.zinsSatz = zinsSatz;
    }

    public SparKonto(String name, double anfangsbestand, double zinsSatz) {
        super(name, anfangsbestand);
        this.zinsSatz = zinsSatz;
    }

    public void zinsenAnrechnung(){
        kontoStand = kontoStand * zinsSatz;
    }

}
