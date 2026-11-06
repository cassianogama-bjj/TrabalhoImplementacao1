import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Viagem {
    private String codigo;
    private String origem;
    private String destino;
    private LocalDateTime partida;
    private double valorBase;
    private Onibus onibus;
    private ArrayList<Passagem> passagens;
    private LinkedList<Passagem> espera;

    public Viagem(String codigo,String origem, String destino, LocalDateTime partida, double valorBase, Onibus onibus){
        this.codigo = codigo;
        this.origem = origem;
        this.destino = destino;
        this.partida = partida;
        this.valorBase = valorBase;
        this.onibus = onibus;
        this.passagens = new ArrayList<>();
        this.espera = new LinkedList<>();

    }

    public boolean venderPassagem(Passagem p){
        if (p.getPoltrona() < 1 || p.getPoltrona() > onibus.getCapacidade()){
            return false;
        }

        for(Passagem vendida : passagens){
            if(vendida.getPoltrona() == p.getPoltrona()){
                return false;
            }
            if(vendida.getPassageiro().getDocumento().equals(p.getPassageiro().getDocumento())){
                return false;
            }
        }

        if(passagens.size() >= onibus.getCapacidade()){
            p.setPoltrona(0);
            adicionarEspera(p);
            return true;
        }

        passagens.add(p);
        return true;
    }

    public void adicionarEspera(Passagem p){
        espera.add(p);
    }

    public Passagem cancelarPassagem (int poltrona){
        Passagem cancelada = null;

        for(int i = 0; i < passagens.size(); i++){
            if(passagens.get(i).getPoltrona() == poltrona){
                cancelada = passagens.remove(i);
                break;
            }
        }

        if(cancelada != null && !espera.isEmpty()){
            Passagem proximaEspera = espera.removeFirst();

            proximaEspera.setPoltrona(poltrona);

            int idososGratuitos = 0;
            for(Passagem p : passagens){
                if(p.getTipo() == TipoPassagem.IDOSO && p.getValorPago() == 0.0){
                    idososGratuitos++;
                }
            }

            double novoValor = proximaEspera.calcularValor(this.valorBase, idososGratuitos);
            proximaEspera.setValorPago(novoValor);

            passagens.add(proximaEspera);
        }
        return cancelada;
    }

    public List<Integer> poltronasLivres(){
        List<Integer> livres = new ArrayList<>();

        for(int i = 1; i <= onibus.getCapacidade(); i++){
            boolean ocupada = false;
            for(Passagem p : passagens){
                if(p.getPoltrona() == i){
                    ocupada = true;
                    break;
                }
            }
            if(!ocupada){
                livres.add(i);
            }
        }
        return livres;
    }

    public double receita(){
        double total = 0.0;
        for(Passagem p : passagens){
            total += p.getValorPago();
        }
        return total;
    }

    public double ocupacao(){
        if(onibus.getCapacidade() == 0 ) return 0.0;
        return ((double) passagens.size() / onibus.getCapacidade() * 100.0);
    }

    public String getCodigo(){
        return this.codigo;
    }

    public String getOrigem(){
        return this.origem;
    }

    public String getDestino(){
        return this.destino;
    }

    public LocalDateTime getPartida(){
        return this.partida;
    }

    public double getValorBase(){
        return this.valorBase;
    }

    public Onibus getOnibus(){
        return this.onibus;
    }

    public ArrayList<Passagem> getPassagens(){
        return this.passagens;
    }

    public LinkedList<Passagem> getEspera(){
        return this.espera;
    }
}
