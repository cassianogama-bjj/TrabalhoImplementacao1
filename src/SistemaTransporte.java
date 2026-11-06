import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public static void main(String[] args) {
    Locale.setDefault(Locale.US);

    Empresa empresa = new Empresa("Empresa C.A.P viagens");

    DateTimeFormatter fmtDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    File arquivo = new File("dados/viagens.txt");
    if(arquivo.exists()){
        try(Scanner sc = new Scanner(arquivo)){
            if (sc.hasNextLine()){
                String primeiraLinha = sc.nextLine().trim();
                empresa = new Empresa(primeiraLinha);
            }

            int numLinha = 1;
            while(sc.hasNextLine()){
                numLinha++;
                String linhaViagem = sc.nextLine().trim();
                if(linhaViagem.isEmpty()) continue;

                String[] camposViagem = linhaViagem.split(";");

                if(camposViagem.length < 9){
                    System.out.println("Aviso! Linha malformada na linha " + numLinha + ", ignorando registro...");
                    continue;
                }

                try{
                    String codigo = camposViagem[0].trim();
                    String origem = camposViagem[1].trim();
                    String destino = camposViagem[2].trim();
                    LocalDateTime partida = LocalDateTime.parse(camposViagem[3].trim(), fmtDataHora);
                    double valorBase = Double.parseDouble(camposViagem[4].trim());

                    String placa = camposViagem[5].trim();
                    String modelo = camposViagem[6].trim();
                    int capacidade = Integer.parseInt(camposViagem[7].trim());
                    int nPassagens = Integer.parseInt(camposViagem[8].trim());

                    Onibus onibus = new Onibus(placa, modelo, capacidade);
                    Viagem viagem = new Viagem(codigo, origem, destino, partida, valorBase, onibus);

                    int idososGratuitosCount = 0;
                    for(int i = 0; i < nPassagens && sc.hasNextLine(); i++){
                        numLinha++;
                        String linhaPassagem = sc.nextLine().trim();
                        String[] camposPassagem = linhaPassagem.split(";");

                        if(camposPassagem.length < 5) continue;

                        int poltrona = Integer.parseInt(camposPassagem[0].trim());
                        String nomePassageiro = camposPassagem[1].trim();
                        String documento = camposPassagem[1].trim();
                        TipoPassagem tipo = TipoPassagem.valueOf(camposPassagem[3].trim());
                        double valorPago = Double.parseDouble(camposPassagem[4].trim());

                        Passageiro passageiro = new Passageiro(nomePassageiro, documento);
                        Passagem passagem = new Passagem(poltrona, tipo, valorPago, passageiro);

                        if(tipo == TipoPassagem.IDOSO && valorPago == 0.0){
                            idososGratuitosCount++;
                        }

                        viagem.getPassagens().add(passagem);
                    }

                    empresa.adicionarViagem(viagem);

                } catch (Exception e){
                    System.out.println("Erro  ao processar registro na linha " + numLinha + ": " + e.getMessage());
                }
            }
            System.out.println("Dados carregados com sucesso de dados/viagens.txt!");
        }catch (FileNotFoundException e){
            System.out.println("Arquivo de entrada não encontrado. Iniciando com dados vazios...");
        }
    }else{
        System.out.println("Arquivo dados/viagens.txt não encontrado. Iniciando Sistema Transporte vazio...");
    }

    Scanner teclado = new Scanner(System.in);
    int opcao = 0;

    do{
        System.out.println("\n---SISTEMA DE TRANSPORTE - " + empresa.getNome() + " ---");
        System.out.println("1. Listar viagens principais com resumo");
        System.out.println("2. Detalhar uma viagem (mostrar passagens e lista de espera)");
        System.out.println("3. Cadastrar nova viagem");
        System.out.println("4. Vender passagem (com poltrona livres e oferta de espera)");
        System.out.println("5. Remover uma viagem");
        System.out.println("6. Cancelar passagem (com poltronas livres e oferta de espera)");
        System.out.println("7. Buscar viagem pelo código");
        System.out.println("8. Operações específicas (Listar viagens de um dia)");
        System.out.println("9. Gerar relatórios (Ocupação e Manifesto)");
        System.out.println("10. Salvar dados em arquivo");
        System.out.println("11. Sair");
        System.out.println("Escolha uma opção: ");

        if (!teclado.hasNextInt()){
            System.out.println("Erro: Digite um número válido!");
            teclado.next();
            continue;
        }
        opcao = teclado.nextInt();
        teclado.nextLine();

        switch (opcao){
            case 1:
                System.out.println("\n--- LISTA DE VIAGENS ---");
                for (Viagem v : empresa.getViagens()){
                    System.out.println("Código: " + v.getCodigo() + "|" + v.getOrigem() + " -> " + v.getDestino() +
                            " | Partida: " + v.getPartida().format(fmtDataHora) +
                            " | Ocupação: " + String.format("%.1f", v.ocupacao()) + "%");
                }
                break;
            case 2:
                System.out.println("Digite o código da viagem: ");
                String codBusca = teclado.nextLine();
                Viagem vBusca = empresa.buscarViagem(codBusca);
                if (vBusca != null){
                    System.out.println("\nViagem: " + vBusca.getCodigo() + " (" + vBusca.getOrigem() + " -> " + vBusca.getDestino() + ")");
                    System.out.println("Passagens Vendidas (" + vBusca.getPassagens().size() + "): ");
                    for(Passagem p : vBusca.getPassagens()){
                        System.out.println(" Poltrona " + p.getPoltrona() + ": " + p.getPassageiro().getNome() +
                                " [ " + p.getTipo() + "] - R$ " + p.getValorPago());
                    }
                    System.out.println("Lista de Espera (" + vBusca.getEspera().size() + "):");
                    for(Passagem p : vBusca.getEspera()){
                        System.out.println(" Aguardando: " + p.getPassageiro().getNome() + " [" + p.getTipo() + "]");
                    }
                }else{
                    System.out.println("Viagem não encontrada!");
                }
                break;
            case 3:
                try {


                    System.out.println("Código da viagem: ");
                    String c = teclado.nextLine();
                    System.out.println("Origem: ");
                    String orig = teclado.nextLine();
                    System.out.println("Destino: ");
                    String dest = teclado.nextLine();
                    System.out.println("Data e hora de partida (dd/MM/yyyy HH:mm:ss): ");
                    LocalDateTime part = LocalDateTime.parse(teclado.nextLine(), fmtDataHora);
                    System.out.println("Valor base da passagem: ");
                    double vb = Double.parseDouble(teclado.nextLine());
                    System.out.println("Placa do ônibus: ");
                    String placa = teclado.nextLine();
                    System.out.println("Modelo do ônibus: ");
                    String modelo = teclado.nextLine();
                    System.out.println("Capacidade de passageiros: ");
                    int cap = Integer.parseInt(teclado.nextLine());

                    Onibus o = new Onibus(placa, modelo, cap);
                    Viagem novaV = new Viagem(c, orig, dest, part, vb, o);
                    empresa.adicionarViagem(novaV);
                    System.out.println("Viagem cadastrada com sucesso!");
                }catch (Exception e){
                    System.out.println("Erro nos dados informados: " + e.getMessage());
                }
                break;
            case 4:
                System.out.println("Código da viagem: ");
                Viagem vVenda = empresa.buscarViagem(teclado.nextLine());
                if (vVenda != null){
                    List<Integer> livres = vVenda.poltronasLivres();
                    System.out.println("Poltronas livres: " + livres);
                    if(livres.isEmpty()){
                        System.out.println("Ônibus lotado! O passageiro irá para a lista de espera.");
                    }

                    System.out.println("Número da poltrona desejada: ");
                    int pol = Integer.parseInt(teclado.nextLine());
                    System.out.println("Nome do passageiro: ");
                    String nomeP = teclado.nextLine();
                    System.out.println("Documento do passageiro: ");
                    String docP = teclado.nextLine();
                    System.out.println("Tipo(INTEIRA, ESTUDANTE, IDOSO)");
                    TipoPassagem tp = TipoPassagem.valueOf(teclado.nextLine().toUpperCase());

                    Passageiro pas = new Passageiro(nomeP, docP);

                    int idososLivresAtual = 0;
                    for(Passagem pEx : vVenda.getPassagens()){
                        if(pEx.getTipo()  == TipoPassagem.IDOSO && pEx.getValorPago() == 0.0){
                            idososLivresAtual++;
                        }
                    }

                    Passagem tempP = new Passagem(pol, tp, 0.0, pas);
                    double valorCalculado = tempP.calcularValor(vVenda.getValorBase(), idososLivresAtual);
                    Passagem novaPassagem = new Passagem(pol, tp, valorCalculado, pas);

                    boolean sucesso = vVenda.venderPassagem(novaPassagem);
                    if(sucesso){
                        System.out.println("Passagem vendida com sucesso! Valor: R$ " + valorCalculado);
                    }else{
                        System.out.println("Viagem lotada ou poltrona ocupada. Passageiro adicionado à lista de espera.");
                    }
                }else{
                    System.out.println("Viagem não encontrada.");
                }
                break;
            case 5:
                System.out.println("Código da viagem a remover:");
                Viagem vRem = empresa.buscarViagem(teclado.nextLine());
                if(vRem != null){
                    if(vRem.getPassagens().isEmpty()){
                        empresa.removerViagem(vRem);
                        System.out.println("Viagem removida com sucesso!");
                    }else{
                        System.out.println("Erro: Não é possível remover viagem com passagens vendidas!");
                    }
                }else{
                    System.out.println("Viagem não encontrada.");
                }
                break;
            case 6:
                System.out.println("Código da viagem: ");
                Viagem vCanc = empresa.buscarViagem(teclado.nextLine());
                if(vCanc != null){
                    System.out.println("Número da poltrona a cancelar: ");
                    int polCan = Integer.parseInt(teclado.nextLine());
                    Passagem cancelada = vCanc.cancelarPassagem(polCan);
                    if(cancelada != null){
                        System.out.println("Passagem da poltrona " + polCan + " cancelada com sucesso.");
                    }else{
                        System.out.println("Não foi encontrada passagem nessa poltrona.");
                    }
                }else{
                    System.out.println("Viagem não encotrada.");
                }
                break;
            case 7:
                System.out.println("Digite o código da viagem: ");
                Viagem vB = empresa.buscarViagem(teclado.nextLine());
                if(vB != null){
                    System.out.println("Encontrada: " + vB.getCodigo() + " | " + vB.getOrigem() + " -> " + vB.getDestino());
                }else{
                    System.out.println("Viagem não localizada.");
                }
                break;
            case 8:
                System.out.println("Digite a data desejada (dd/MM/yyyy)");
                String dataFiltro = teclado.nextLine();
                System.out.println("\n--- VIAGENS DO DIA " + dataFiltro + " ---");
                for(Viagem v : empresa.getViagens()){
                    if(v.getPartida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).equals(dataFiltro)){
                        System.out.println("Código: " + v.getCodigo() + " | Horário: " + v.getPartida().toLocalTime() + " | " + v.getOrigem() + " -> " + v.getDestino());
                    }
                }
                break;
            case 9:
                try{
                    File dirSaida = new File("saidas");
                    if(!dirSaida.exists()) dirSaida.mkdir();

                    try(PrintWriter pwOcup = new PrintWriter(new File("saidas/ocupacao.txt"))){
                        double receitaTotal = 0.0;
                        for(Viagem v : empresa.getViagens()){
                            pwOcup.println(v.getCodigo() + " " + v.getPartida().format(fmtDataHora) + " " + v.getOrigem() + " -> " + v.getDestino());
                            int vendidas = v.getPassagens().size();
                            int cap = v.getOnibus().getCapacidade();
                            double pcts = (double) vendidas / cap * 100;
                            pwOcup.println("  " + vendidas + "/" + cap + " (" + String.format("%.1f", pcts) + "%) R$ " + String.format("%.2f", v.receita()) + "espera: " + v.getEspera().size());
                            receitaTotal += v.receita();
                        }
                        pwOcup.println("Receita total: R$ " + String.format("%.2f", receitaTotal));
                    }

                    for (Viagem v : empresa.getViagens()){
                        try(PrintWriter pwMan = new PrintWriter(new File("saidas/manifesto_" + v.getCodigo() + ".txt"))){
                            pwMan.println("Manifesto da Viagem: " + v.getCodigo());
                            for(Passagem p : v.getPassagens()){
                                pwMan.println("Poltrona " + p.getPoltrona() + "; " + p.getPassageiro().getNome() + "; " +
                                        p.getPassageiro().getDocumento() + "; " + p.getTipo() + "; R$ " + p.getValorPago());
                            }
                        }
                    }
                    System.out.println("Relatórios gerados com sucesso na pasta saidas/!");
                }catch(Exception e){
                    System.out.println("Erro ao gerar relatórios: " + e.getMessage());
                }
                break;
            case 10:
                try(PrintWriter writer = new PrintWriter(new File("dados/viagens.txt"))){
                    writer.println(empresa.getNome());
                    for(Viagem v : empresa.getViagens()){
                        writer.println(v.getCodigo() + ";" + v.getOrigem() + ";" + v.getDestino() + ";" +
                                v.getPartida().format(fmtDataHora) + ";" + v.getValorBase() + ";" +
                                v.getOnibus().getPlaca() + ";" + v.getOnibus().getModelo() + ";" +
                                v.getOnibus().getCapacidade() + ";" + v.getPassagens().size());
                        for(Passagem p : v.getPassagens()){
                            writer.println(p.getPoltrona() + ";" + p.getPassageiro().getNome() + ";" +
                                    p.getPassageiro().getDocumento()  + ";" + p.getTipo() + ";" + p.getValorPago());
                        }
                    }
                    System.out.println("Dados salvos com sucesso em dados/viagens.txt!");
                }catch (FileNotFoundException e){
                    System.out.println("Erro ao salvar arquivo: " + e.getMessage());
                }
                break;
            case 11:
                System.out.println("Deseja salvar os dados antes de sair?(s/n)");
                String resp = teclado.nextLine();
                if (resp.equalsIgnoreCase("s")){
                    System.out.println("Dados salvos. Saindo...");
                }else{
                    System.out.println("Saindo sem salvar...");
                }
                break;
            default:
                System.out.println("Opção inválida! Escolha entre 1 e 11!");
        }
    }while (opcao != 11);
    teclado.close();
}