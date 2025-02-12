package com.exemplo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.github.javafaker.Faker;

class Cliente implements Serializable {
    String nome;
    String sobrenome;
    String telefone;
    String endereco;
    int creditScore;

    public Cliente(String nome, String sobrenome, String telefone, String endereco, int creditScore) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.creditScore = creditScore;
    }

    @Override
    public String toString() {
        return nome + " " + sobrenome + " - " + telefone + " - " + endereco + " - Credit Score: " + creditScore;
    }
}

public class ClienteGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Cliente> clientes;
    private static final String FILE_NAME = "clientes.dat";
    private Faker faker;

    public ClienteGUI() {
        setTitle("Gerenciamento de Clientes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        clientes = carregarClientes();
        faker = new Faker();
        
        tableModel = new DefaultTableModel(new String[]{"Nome", "Sobrenome", "Telefone", "Endereço", "Credit Score"}, 0);
        table = new JTable(tableModel);
        atualizarTabela();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnAdd = new JButton("Adicionar Cliente");
        JButton btnRemove = new JButton("Remover Cliente");
        JButton btnSort = new JButton("Ordenar Clientes");
        JButton btnGenerate = new JButton("Gerar Clientes Aleatórios");
        panel.add(btnAdd);
        panel.add(btnRemove);
        panel.add(btnSort);
        panel.add(btnGenerate);
        add(panel, BorderLayout.SOUTH);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarCliente();
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerCliente();
            }
        });

        btnSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarClientes();
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarClientesAleatorios();
            }
        });
    }

    private void adicionarCliente() {
        String nome = JOptionPane.showInputDialog("Nome:");
        String sobrenome = JOptionPane.showInputDialog("Sobrenome:");
        String telefone = JOptionPane.showInputDialog("Telefone:");
        String endereco = JOptionPane.showInputDialog("Endereço:");
        int creditScore = Integer.parseInt(JOptionPane.showInputDialog("Credit Score:"));

        clientes.add(new Cliente(nome, sobrenome, telefone, endereco, creditScore));
        salvarClientes();
        atualizarTabela();
    }

    private void removerCliente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            clientes.remove(selectedRow);
            salvarClientes();
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover.");
        }
    }

    private void ordenarClientes() {
        clientes.sort((c1, c2) -> c1.nome.compareToIgnoreCase(c2.nome));
        salvarClientes();
        atualizarTabela();
    }

    private void gerarClientesAleatorios() {
        for (int i = 0; i < 10; i++) {
            String nome = faker.name().firstName();
            String sobrenome = faker.name().lastName();
            String telefone = faker.phoneNumber().cellPhone();
            String endereco = faker.address().fullAddress();
            int creditScore = faker.number().numberBetween(300, 850);
            clientes.add(new Cliente(nome, sobrenome, telefone, endereco, creditScore));
        }
        salvarClientes();
        atualizarTabela();
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{c.nome, c.sobrenome, c.telefone, c.endereco, c.creditScore});
        }
    }

    private List<Cliente> carregarClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void salvarClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClienteGUI().setVisible(true));
    }
}
