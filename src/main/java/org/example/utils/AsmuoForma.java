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

public class AsmuoForma extends JPanel {
    private JTextField vardasField;
    private JTextField pavardeField;
    private JTextField gimimoDataField;
    private JTextField adresasField;
    private JTextField elPastasField;
    private JTextField telefonasField;

    public AsmuoForma() {
        setupForm();
    }

    private void setupForm() {
        setLayout(new GridLayout(7, 2));

        JLabel vardasLabel = new JLabel("Vardas:");
        add(vardasLabel);
        vardasField = new JTextField(20);
        add(vardasField);

        JLabel pavardeLabel = new JLabel("Pavardė:");
        add(pavardeLabel);
        pavardeField = new JTextField(20);
        add(pavardeField);

        JLabel gimimoDataLabel = new JLabel("Gimimo data (yyyy-MM-dd):");
        add(gimimoDataLabel);
        gimimoDataField = new JTextField(20);
        add(gimimoDataField);

        JLabel adresasLabel = new JLabel("Adresas:");
        add(adresasLabel);
        adresasField = new JTextField(20);
        add(adresasField);

        JLabel elPastasLabel = new JLabel("El. paštas:");
        add(elPastasLabel);
        elPastasField = new JTextField(20);
        add(elPastasField);

        JLabel telefonasLabel = new JLabel("Telefonas:");
        add(telefonasLabel);
        telefonasField = new JTextField(20);
        add(telefonasField);

        JButton saveButton = new JButton("Išsaugoti");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsmuo();
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

    private void saveAsmuo() {
        try {
            String vardas = vardasField.getText().trim();
            String pavarde = pavardeField.getText().trim();
            String gimimoDataText = gimimoDataField.getText().trim();
            LocalDate gimimoData = LocalDate.parse(gimimoDataText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String adresas = adresasField.getText().trim();
            String elPastas = elPastasField.getText().trim();
            String telefonas = telefonasField.getText().trim();

            // Prisijungiame prie MySQL duomenų bazės
            String url = "jdbc:mysql://localhost:3306/baigiamasis";
            String user = "root";
            String password = "jakuliene";

            Connection connection = DriverManager.getConnection(url, user, password);

            // Sukuriame SQL užklausą, kuri įrašo duomenis į asmuo lentelę
            String sql = "INSERT INTO asmuo (Vardas, Pavarde, Gimimo_data, Adresas, El_pastas, Telefonas) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, vardas);
            statement.setString(2, pavarde);
            statement.setDate(3, java.sql.Date.valueOf(gimimoData));
            statement.setString(4, adresas);
            statement.setString(5, elPastas);
            statement.setString(6, telefonas);

            // Įvykdome užklausą
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Asmuo išsaugotas sėkmingai.");
                clearForm();
            }

            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Klaida įrašant duomenis: " + ex.getMessage(), "Klaida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        vardasField.setText("");
        pavardeField.setText("");
        gimimoDataField.setText("");
        adresasField.setText("");
        elPastasField.setText("");
        telefonasField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Asmens Forma");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new AsmuoForma(), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
