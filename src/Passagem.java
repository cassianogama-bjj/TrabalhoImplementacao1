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
        if (this.tipo == TipoPassagem.INTEIRA){
            return base;
        }else if(this.tipo == TipoPassagem.ESTUDANTE){
            return base/2;
        }else if(idososGratuitos < 2){
            return 0.0;
        }else{
            return base/2;
        }
    }

    public int getPoltrona() {
        return poltrona;
    }

    public void setPoltrona(int poltrona) {
        this.poltrona = poltrona;
    }

    public TipoPassagem getTipo() {
        return tipo;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }
}