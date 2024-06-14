package org.example.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SertifikatasForma extends JPanel {
    private JTextField pavadinimasField;
    private JTextField sertifikavimoDataField;
    private JTextField galiojimoDataField;
    private JTextField asmuoIdField;

    public SertifikatasForma() {
        setupForm();
    }

    private void setupForm() {
        setLayout(new GridLayout(5, 2));

        JLabel pavadinimasLabel = new JLabel("Pavadinimas:");
        add(pavadinimasLabel);
        pavadinimasField = new JTextField(20);
        add(pavadinimasField);

        JLabel sertifikavimoDataLabel = new JLabel("Sertifikavimo data (yyyy-MM-dd):");
        add(sertifikavimoDataLabel);
        sertifikavimoDataField = new JTextField(20);
        add(sertifikavimoDataField);

        JLabel galiojimoDataLabel = new JLabel("Galiojimo data (yyyy-MM-dd):");
        add(galiojimoDataLabel);
        galiojimoDataField = new JTextField(20);
        add(galiojimoDataField);

        JLabel asmuoIdLabel = new JLabel("Asmuo ID:");
        add(asmuoIdLabel);
        asmuoIdField = new JTextField(20);
        add(asmuoIdField);

        JButton saveButton = new JButton("Išsaugoti");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSertifikatas();
            }
        });
        add(saveButton);

        JButton clearButton = new JButton("Išvalyti");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        add(clearButton);
    }

    private void saveSertifikatas() {
        try {
            String pavadinimas = pavadinimasField.getText().trim();
            String sertifikavimoDataText = sertifikavimoDataField.getText().trim();
            LocalDate sertifikavimoData = LocalDate.parse(sertifikavimoDataText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String galiojimoDataText = galiojimoDataField.getText().trim();
            LocalDate galiojimoData = LocalDate.parse(galiojimoDataText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String asmuoIdText = asmuoIdField.getText().trim();
            int asmuoId = Integer.parseInt(asmuoIdText);

            // Prisijungiame prie MySQL duomenų bazės
            String url = "jdbc:mysql://localhost:3306/baigiamasis";
            String user = "root";
            String password = "jakuliene";

            Connection connection = DriverManager.getConnection(url, user, password);

            // Sukuriame SQL užklausą, kuri įrašo duomenis į sertifikatas lentelę
            String sql = "INSERT INTO sertifikatas (Pavadinimas, Sertifikavimo_data, Galiojimo_data, Asmuo_ID) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, pavadinimas);
            statement.setDate(2, java.sql.Date.valueOf(sertifikavimoData));
            statement.setDate(3, java.sql.Date.valueOf(galiojimoData));
            statement.setInt(4, asmuoId);

            // Įvykdome užklausą
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Sertifikatas išsaugotas sėkmingai.");
                clearForm();
            }

            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Klaida įrašant duomenis: " + ex.getMessage(), "Klaida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        pavadinimasField.setText("");
        sertifikavimoDataField.setText("");
        galiojimoDataField.setText("");
        asmuoIdField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Sertifikato Forma");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new SertifikatasForma(), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
