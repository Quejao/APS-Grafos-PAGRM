package apsgrafos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Torres
 */
public class Arquivo {

    private String nome;
    public Arquivo() {
    }

    public ArrayList<String> leitor(String diretorio) {
        this.nome = diretorio;
        ArrayList<String> texto = new ArrayList<>();
        File file = new File(diretorio);

        if (file.exists()) {

            try {

                FileReader reader = new FileReader(diretorio);
                BufferedReader buffer = new BufferedReader(reader);
                String linha = buffer.readLine();

                while (linha != null) {
                    texto.add(linha);
                    linha = buffer.readLine();
                }

                buffer.close();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            texto.add("");
        }
        return texto;
    }

    public void gravador(ArrayList<ArrayList<Rotulo>> rotulosMinimos, float mediaRotulos, float tempoTotal, float tempoMedio, int contador) {
        Random randomize = new Random();
        int id = randomize.nextInt(100);
        
        File file = new File("Saida/" + contador +" - " + getNome());

        try {
            FileWriter writer = new FileWriter(file);
            writer.write("Total de grafos no arquivo: " + rotulosMinimos.size());
            writer.write("\r\n");
            writer.write("\r\n");

            for (int i = 0; i < rotulosMinimos.size(); i++) {
                writer.write("Grafo: " + (i + 1));
                writer.write("\r\nRótulos geradores: ");

                for (int j = 0; j < rotulosMinimos.get(i).size(); j++) {
                    if (rotulosMinimos.get(i).get(j).getId().equals(-10)) {
                        writer.write("\r\nO grafo possui vértices inalcaçavéis portanto não é possível encontrar uma solução.");

                    } else {
                        writer.write("[");
                        writer.write(rotulosMinimos.get(i).get(j).getId().toString());
                        writer.write("]");
                    }
                }
                writer.write("\r\n");
                writer.write("\r\n");
            }

            writer.write("\r\nMédia de Rótulos: " + mediaRotulos);
            writer.write("\r\nTempo Total: " + tempoTotal + " ms");
            writer.write("\r\nTempo Médio: " + tempoMedio + " ms");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contador++;

    }

    public ArrayList<Grafo> geraMatrizesAdj(String arquivo) {

        String[] diretorio = arquivo.split("/");
        setNome(diretorio[diretorio.length - 1]);

        ArrayList<Grafo> grafos = new ArrayList<Grafo>();
        ArrayList<String> texto = leitor(arquivo);

        String[] linha = texto.get(0).split(" ");
        Integer tamanho = Integer.parseInt(linha[0]);
        Integer peso = Integer.parseInt(linha[1]);
        int inicio = 1, fim = inicio + tamanho;

        while (fim <= texto.size()) {

            List<String> text = texto.subList(inicio, fim);
            Collections.reverse(text);

            Grafo matriz = new Grafo(tamanho);

            for (Integer i = 0; i < tamanho; i++) {
                linha = text.get(i).split(" ");

                for (Integer j = 0; j < linha.length; j++) {
                    Integer n;
                    String s = linha[j];
                    if (s.equals("") || s.equals(peso.toString())) {
                        n = -1;
                    } else {
                        n = Integer.parseInt(linha[j]);
                    }

                    matriz.getMatrizAdjacencia()[i][j] = n;
                    matriz.getMatrizAdjacencia()[j][i] = n;

                }
                matriz.getMatrizAdjacencia()[i][i] = -1;
            }
            grafos.add(matriz);
            inicio = fim;
            fim = inicio + tamanho;

        }

        return grafos;
    }

    public ArrayList<Grafo> geraListasAdj(ArrayList<Grafo> matriz) {

        String[] diretorio = nome.split("/");
        setNome(diretorio[diretorio.length - 1]);

        ArrayList<Grafo> grafos = new ArrayList<Grafo>();

        for (Grafo grafoIterator : matriz) {

            Grafo grafo = new Grafo();

            for (Integer i = 0; i < grafoIterator.getMatrizAdjacencia().length; i++) {
                grafo.insereVertice(new Vertice(i.toString()));
            }

            for (Integer i = 0; i < grafoIterator.getMatrizAdjacencia().length; i++) {
                Vertice origem = grafo.getVerticeGrafo(i.toString());

                for (Integer j = 0; j < matriz.get(0).getMatrizAdjacencia().length; j++) {
                    Integer n = grafoIterator.getMatrizAdjacencia()[i][j];

                    if (!n.equals(-1)) {
                        Vertice destino = grafo.getVerticeGrafo(j.toString());
                        if (!grafo.existeAdjascencia(origem, destino)) {
                            grafo.insereArestaND(origem, new Aresta(destino, n));

                        }
                    }
                }
            }

            grafos.add(grafo);
        }

        return grafos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
