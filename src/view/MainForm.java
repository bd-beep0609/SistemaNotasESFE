package view;

import controller.*;
import model.Estudiante;
import model.Nota;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainForm extends JFrame {
    private UserController userController;
    private EstudianteController estudianteController;
    private NotaController notaController;
    private Usuario currentUser;

    private JTabbedPane tabbedPane;
    private JTable tableEstudiantes;
    private DefaultTableModel tableEstudiantesModel;
    private JTable tableNotas;
    private DefaultTableModel tableNotasModel;
    private JLabel lblStats;
    private JTextField txtBuscar;

    public MainForm(UserController userController) {
        this.userController = userController;
        this.currentUser = userController.getCurrentUser();
        this.estudianteController = new EstudianteController();
        this.notaController = new NotaController();

        setTitle("Sistema de Notas - ESFE");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initMenu();
        initComponents();
        loadEstudiantesTable();
        loadNotasTable();
        updateStats();

        String rol = currentUser.isAdmin() ? "Administrador" : "Usuario";
        JOptionPane.showMessageDialog(this, "Bienvenido " + currentUser.getNombre() + " (" + rol + ")",
                "Sistema de Notas ESFE", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuMant = new JMenu("Mantenimiento");
        JMenuItem itemEstudiantes = new JMenuItem("Estudiantes");
        itemEstudiantes.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            loadEstudiantesTable();
        });
        menuMant.add(itemEstudiantes);

        JMenuItem itemNotas = new JMenuItem("Registrar Nota");
        itemNotas.addActionListener(e -> openNotaDialog());
        menuMant.add(itemNotas);
        menuBar.add(menuMant);

        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemPromedios = new JMenuItem("Promedios por Estudiante");
        itemPromedios.addActionListener(e -> showPromediosReport());
        menuReportes.add(itemPromedios);
        menuBar.add(menuReportes);

        JMenu menuPerfil = new JMenu("Perfil");
        JMenuItem itemChangePassword = new JMenuItem("Cambiar Contraseña");
        itemChangePassword.addActionListener(e -> new ChangePasswordDialog(this, userController).setVisible(true));
        menuPerfil.add(itemChangePassword);

        JMenuItem itemLogout = new JMenuItem("Cerrar Sesión");
        itemLogout.addActionListener(e -> {
            userController.logout();
            dispose();
            new LoginForm().setVisible(true);
        });
        menuPerfil.add(itemLogout);
        menuBar.add(menuPerfil);

        setJMenuBar(menuBar);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📚 Estudiantes", createEstudiantesPanel());
        tabbedPane.addTab("📝 Notas Registradas", createNotasPanel());
        add(tabbedPane, BorderLayout.CENTER);

        lblStats = new JLabel(" ");
        lblStats.setBorder(BorderFactory.createEtchedBorder());
        add(lblStats, BorderLayout.SOUTH);
    }

    private JPanel createEstudiantesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNew = new JButton("➕ Nuevo Estudiante");
        JButton btnEdit = new JButton("✏️ Modificar");
        JButton btnDelete = new JButton("🗑️ Eliminar");
        JButton btnRefresh = new JButton("🔄 Actualizar");
        panelBotones.add(btnNew);
        panelBotones.add(btnEdit);
        panelBotones.add(btnDelete);
        panelBotones.add(btnRefresh);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("🔍 Buscar:"));
        txtBuscar = new JTextField(25);
        panelBusqueda.add(txtBuscar);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelBotones, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);
        panel.add(panelSuperior, BorderLayout.NORTH);

        tableEstudiantesModel = new DefaultTableModel(new String[]{"ID", "Carnet", "Nombre", "Apellido", "Curso", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableEstudiantes = new JTable(tableEstudiantesModel);
        panel.add(new JScrollPane(tableEstudiantes), BorderLayout.CENTER);

        btnNew.addActionListener(e -> {
            EstudianteDialog dialog = new EstudianteDialog(this, estudianteController, null);
            dialog.setVisible(true);
            loadEstudiantesTable();
            updateStats();
        });

        btnEdit.addActionListener(e -> {
            int row = tableEstudiantes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante");
                return;
            }
            int id = (int) tableEstudiantesModel.getValueAt(row, 0);
            Estudiante est = estudianteController.findById(id);
            if (est != null) {
                EstudianteDialog dialog = new EstudianteDialog(this, estudianteController, est);
                dialog.setVisible(true);
                loadEstudiantesTable();
                updateStats();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = tableEstudiantes.getSelectedRow();
            if (row == -1) return;
            int id = (int) tableEstudiantesModel.getValueAt(row, 0);
            String nombre = (String) tableEstudiantesModel.getValueAt(row, 2);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar a " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                estudianteController.deleteEstudiante(id);
                loadEstudiantesTable();
                updateStats();
            }
        });

        btnRefresh.addActionListener(e -> {
            loadEstudiantesTable();
            updateStats();
        });

        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterEstudiantes(); }
        });

        return panel;
    }

    private JPanel createNotasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrarNota = new JButton("➕ Registrar Nueva Nota");
        JButton btnRefresh = new JButton("🔄 Actualizar");
        panelBotones.add(btnRegistrarNota);
        panelBotones.add(btnRefresh);
        panel.add(panelBotones, BorderLayout.NORTH);

        tableNotasModel = new DefaultTableModel(new String[]{"ID", "Estudiante", "Módulo", "Nota", "Fecha", "Observación"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableNotas = new JTable(tableNotasModel);
        panel.add(new JScrollPane(tableNotas), BorderLayout.CENTER);

        btnRegistrarNota.addActionListener(e -> openNotaDialog());
        btnRefresh.addActionListener(e -> loadNotasTable());

        return panel;
    }

    private void filterEstudiantes() {
        tableEstudiantesModel.setRowCount(0);
        String searchText = txtBuscar.getText().trim();
        List<Estudiante> estudiantes = estudianteController.getAllEstudiantes();
        for (Estudiante e : estudiantes) {
            if (searchText.isEmpty() ||
                    e.getCarnet().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getNombre().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getApellido().toLowerCase().contains(searchText.toLowerCase()) ||
                    e.getCursoDisplay().toLowerCase().contains(searchText.toLowerCase())) {
                tableEstudiantesModel.addRow(new Object[]{e.getId(), e.getCarnet(), e.getNombre(), e.getApellido(), e.getCursoDisplay(), e.getActivoText()});
            }
        }
    }

    private void loadEstudiantesTable() { filterEstudiantes(); }

    private void loadNotasTable() {
        tableNotasModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Nota n : notaController.getAllNotas()) {
            String fecha = n.getFecha() != null ? sdf.format(n.getFecha()) : "";
            tableNotasModel.addRow(new Object[]{n.getId(), n.getEstudianteNombre(), n.getModuloNombre(), n.getValor(), fecha, n.getObservacion()});
        }
    }

    private void updateStats() {
        int total = estudianteController.getAllEstudiantes().size();
        int activos = estudianteController.getEstudiantesActivos().size();
        int software = estudianteController.getCantidadEstudiantesByCarreraNombre("Software");
        lblStats.setText("📊 Total: " + total + " estudiantes | ✅ Activos: " + activos + " | 💻 Software: " + software);
    }

    private void openNotaDialog() {
        NotaDialog dialog = new NotaDialog(this, estudianteController, notaController);
        dialog.setVisible(true);
        loadNotasTable();
    }

    private void showPromediosReport() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("📊 REPORTE DE PROMEDIOS\n══════════════════════\n\n");
        for (Estudiante e : estudianteController.getAllEstudiantes()) {
            double promedio = notaController.getPromedioByEstudiante(e.getId());
            String estado = promedio >= 6.0 ? "✅ APROBADO" : "❌ REPROBADO";
            reporte.append(String.format("%s - %s\n   Promedio: %.2f - %s\n\n", e.getCarnet(), e.getNombreCompleto(), promedio, estado));
        }
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scroll, "Reporte de Promedios", JOptionPane.INFORMATION_MESSAGE);
    }
}