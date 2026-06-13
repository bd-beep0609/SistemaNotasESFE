import view.LoginForm;
import db.DatabaseConnection;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicializar base de datos
        DatabaseConnection.initDatabase();

        // Iniciar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}