package view;

import controller.EstudianteController;
import controller.NotaController;
import controller.ModuloController;
import model.Estudiante;
import model.Modulo;
import javax.swing.*;
import java.awt.*;

public class NotaDialog extends JDialog {
    private EstudianteController estudianteController;
    private NotaController notaController;
    private ModuloController moduloController;

    private JComboBox<Estudiante> cbEstudiante;
    private JComboBox<Modulo> cbModulo;
    private JTextField txtNota;
    private JTextArea txtObservacion;
    private JButton btnSave, btnCancel;

    public NotaDialog(JFrame parent, EstudianteController estudianteController, NotaController notaController) {
        super(parent, "Registrar Nota", true);
        this.estudianteController = estudianteController;
        this.notaController = notaController;
        this.moduloController = new ModuloController();

        setSize(550, 450);
        setLocationRelativeTo(parent);
        initComponents();
        cargarEstudiantes();
        cargarModulos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estudiante:*"), gbc);
        gbc.gridx = 1;
        cbEstudiante = new JComboBox<>();
        cbEstudiante.setPreferredSize(new Dimension(300, 25));
        panel.add(cbEstudiante, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Módulo:*"), gbc);
        gbc.gridx = 1;
        cbModulo = new JComboBox<>();
        cbModulo.setPreferredSize(new Dimension(300, 25));
        panel.add(cbModulo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nota (0-10):*"), gbc);
        gbc.gridx = 1;
        txtNota = new JTextField(10);
        txtNota.setPreferredSize(new Dimension(100, 25));
        panel.add(txtNota, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Observación:"), gbc);
        gbc.gridx = 1;
        txtObservacion = new JTextArea(3, 25);
        txtObservacion.setLineWrap(true);
        JScrollPane scrollObs = new JScrollPane(txtObservacion);
        scrollObs.setPreferredSize(new Dimension(300, 60));
        panel.add(scrollObs, gbc);

        add(panel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnSave = new JButton("Registrar Nota");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnSave);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void cargarEstudiantes() {
        cbEstudiante.removeAllItems();
        for (Estudiante e : estudianteController.getEstudiantesActivos()) {
            cbEstudiante.addItem(e);
        }
        cbEstudiante.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Estudiante) {
                    Estudiante e = (Estudiante) value;
                    value = e.getCarnet() + " - " + e.getNombreCompleto() + " (" + e.getCursoDisplay() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
    }

    private void cargarModulos() {
        cbModulo.removeAllItems();
        for (Modulo m : moduloController.getAllModulos()) {
            cbModulo.addItem(m);
        }
    }

    private void save() {
        Estudiante estudiante = (Estudiante) cbEstudiante.getSelectedItem();
        Modulo modulo = (Modulo) cbModulo.getSelectedItem();

        if (estudiante == null || modulo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione estudiante y módulo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double nota;
        try {
            nota = Double.parseDouble(txtNota.getText().trim());
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "La nota debe estar entre 0 y 10", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String observacion = txtObservacion.getText().trim();
        if (observacion.isEmpty()) observacion = "Sin observación";

        if (notaController.addNota(estudiante.getId(), modulo.getId(), nota, observacion)) {
            JOptionPane.showMessageDialog(this, "Nota registrada exitosamente");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar nota", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}