public class Onibus {
    private String placa;
    private String modelo;
    private int capacidade;

    public Onibus(String placa, String modelo, int capacidade){
        this.placa = placa;
        this.modelo = modelo;
        this.capacidade = capacidade;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getCapacidade() {
        return capacidade;
    }
}
