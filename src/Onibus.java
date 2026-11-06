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
        return this.placa;
    }

    public String getModelo() {
        return this.modelo;
    }

    public int getCapacidade() {
        return this.capacidade;
    }
}
