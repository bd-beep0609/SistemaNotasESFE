package view;

import controller.EstudianteController;
import controller.CarreraController;
import controller.NivelController;
import controller.GrupoController;
import model.Carrera;
import model.Nivel;
import model.Grupo;
import model.Estudiante;
import javax.swing.*;
import java.awt.*;

public class EstudianteDialog extends JDialog {
    private EstudianteController estudianteController;
    private CarreraController carreraController;
    private NivelController nivelController;
    private GrupoController grupoController;
    private Estudiante editingEstudiante;

    private JTextField txtCarnet, txtNombre, txtApellido;
    private JComboBox<Carrera> cbCarrera;
    private JComboBox<Nivel> cbNivel;
    private JComboBox<Grupo> cbGrupo;
    private JComboBox<String> cbEstado;
    private JButton btnSave, btnCancel;
    private int editingId = -1;

    public EstudianteDialog(JFrame parent, EstudianteController estudianteController, Estudiante estudianteToEdit) {
        super(parent, estudianteToEdit == null ? "Nuevo Estudiante" : "Editar Estudiante", true);
        this.estudianteController = estudianteController;
        this.carreraController = new CarreraController();
        this.nivelController = new NivelController();
        this.grupoController = new GrupoController();
        this.editingEstudiante = estudianteToEdit;

        setSize(500, 480);
        setLocationRelativeTo(parent);
        initComponents();
        cargarCombos();

        if (estudianteToEdit != null) {
            editingId = estudianteToEdit.getId();
            loadEstudianteData();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Carnet:*"), gbc);
        gbc.gridx = 1;
        txtCarnet = new JTextField(15);
        panel.add(txtCarnet, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nombre:*"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Apellido:*"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(15);
        panel.add(txtApellido, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Carrera:*"), gbc);
        gbc.gridx = 1;
        cbCarrera = new JComboBox<>();
        cbCarrera.setPreferredSize(new Dimension(200, 25));
        cbCarrera.addActionListener(e -> cargarNiveles());
        panel.add(cbCarrera, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nivel:*"), gbc);
        gbc.gridx = 1;
        cbNivel = new JComboBox<>();
        cbNivel.setPreferredSize(new Dimension(200, 25));
        cbNivel.addActionListener(e -> cargarGrupos());
        panel.add(cbNivel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Grupo:*"), gbc);
        gbc.gridx = 1;
        cbGrupo = new JComboBox<>();
        cbGrupo.setPreferredSize(new Dimension(200, 25));
        panel.add(cbGrupo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        panel.add(cbEstado, gbc);

        add(panel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnSave = new JButton(editingEstudiante == null ? "Guardar" : "Actualizar");
        btnCancel = new JButton("Cancelar");
        panelBotones.add(btnSave);
        panelBotones.add(btnCancel);
        add(panelBotones, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void cargarCombos() {
        for (Carrera c : carreraController.getAllCarreras()) {
            cbCarrera.addItem(c);
        }
        cargarNiveles();
    }

    private void cargarNiveles() {
        cbNivel.removeAllItems();
        for (Nivel n : nivelController.getAllNiveles()) {
            cbNivel.addItem(n);
        }
        cargarGrupos();
    }

    private void cargarGrupos() {
        cbGrupo.removeAllItems();
        for (Grupo g : grupoController.getAllGrupos()) {
            cbGrupo.addItem(g);
        }
    }

    private void loadEstudianteData() {
        txtCarnet.setText(editingEstudiante.getCarnet());
        txtNombre.setText(editingEstudiante.getNombre());
        txtApellido.setText(editingEstudiante.getApellido());

        for (int i = 0; i < cbCarrera.getItemCount(); i++) {
            if (cbCarrera.getItemAt(i).getNombre().equals(editingEstudiante.getCarreraNombre())) {
                cbCarrera.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cbNivel.getItemCount(); i++) {
            if (cbNivel.getItemAt(i).getNombre().equals(editingEstudiante.getNivelNombre())) {
                cbNivel.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cbGrupo.getItemCount(); i++) {
            if (cbGrupo.getItemAt(i).getNumero() == editingEstudiante.getGrupoNumero()) {
                cbGrupo.setSelectedIndex(i);
                break;
            }
        }

        cbEstado.setSelectedIndex(editingEstudiante.isActivo() ? 0 : 1);
    }

    private void save() {
        String carnet = txtCarnet.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        Carrera carrera = (Carrera) cbCarrera.getSelectedItem();
        Nivel nivel = (Nivel) cbNivel.getSelectedItem();
        Grupo grupo = (Grupo) cbGrupo.getSelectedItem();
        boolean activo = cbEstado.getSelectedIndex() == 0;

        if (carnet.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carnet, Nombre y Apellido son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito;
        if (editingEstudiante == null) {
            exito = estudianteController.addEstudiante(carnet, nombre, apellido,
                    carrera.getId(), nivel.getId(), grupo.getId(), activo);
            if (exito) JOptionPane.showMessageDialog(this, "Estudiante creado exitosamente");
            else JOptionPane.showMessageDialog(this, "Error: El carnet ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            exito = estudianteController.updateEstudiante(editingId, carnet, nombre, apellido,
                    carrera.getId(), nivel.getId(), grupo.getId(), activo);
            if (exito) JOptionPane.showMessageDialog(this, "Estudiante actualizado");
            else JOptionPane.showMessageDialog(this, "Error al actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (exito) dispose();
    }
}