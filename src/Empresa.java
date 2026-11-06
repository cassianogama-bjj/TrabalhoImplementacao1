import java.util.ArrayList;

public class Empresa {
    private String nome;
    private ArrayList<Viagem> viagens;

    public Empresa(String nome){
        this.nome = nome;
        this.viagens = new ArrayList<>();
    }

    public void adicionarViagem(Viagem v){
        viagens.add(v);
    }

    public void removerViagem(Viagem v){
        if(v.getPassagem().isEmpty()){
            viagens.remove(v);
        }else{
            System.out.println("Erro! Não é possível remover uma viagem que possui passagens vendidas!");
        }
    }

    public Viagem buscarViagem(String codigo){
        for(Viagem v : viagens){
            if(v.getCodigo().equalsIgnoreCase(codigo)){
                return v;
            }
        }
        return null;
    }
    public String getNome(){
        return this.nome;
    }

    public ArrayList<Viagem> getViagens(){
        return this.viagens;
    }
}