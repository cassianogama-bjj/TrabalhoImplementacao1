public class Passagem {
    private int poltrona;
    private TipoPassagem tipo;
    private double valorPago;
    private Passageiro passageiro;

    public Passagem(int poltrona, TipoPassagem tipo, double valorPago, Passageiro passageito){
        this.poltrona = poltrona;
        this.tipo = tipo;
        this.valorPago= valorPago;
        this.passageiro = passageito;
    }

    public double calcularValor(double base, int idososGratuitos){
        return 0;
    }
}
