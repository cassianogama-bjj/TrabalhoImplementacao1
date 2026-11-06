public class Passageiro {
    private String nome;
    private String documento;

    public Passageiro(String nome, String documento){
        this.nome = nome;
        this.documento = documento;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDocumento() {
        return this.documento;
    }

}
