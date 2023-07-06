import java.sql.*;
import java.util.Scanner;

public class UASRINITodoListCRUD {

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_db", "root", "");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Tambah Todo");
                System.out.println("2. Tampilkan Todo");
                System.out.println("3. Update Todo");
                System.out.println("4. Hapus Todo");
                System.out.println("0. Keluar");
                System.out.print("Pilih menu: ");
                int menu = scanner.nextInt();

                switch (menu) {
                    case 1:
                        System.out.print("Masukkan Todo: ");
                        String todo = scanner.nextLine();
                        System.out.print("Masukkan Kategori: ");
                        String kategori = scanner.nextLine();
                        System.out.print("Masukkan Tanggal Selesai (YYYY-MM-DD): ");
                        String tanggalSelesai = scanner.nextLine();
                        tambahTodo(connection, todo, kategori, tanggalSelesai);
                        break;
                    case 2:
                        tampilkanTodoList(connection);
                        break;
                    case 3:
                        System.out.print("Masukkan ID Todo yang akan diupdate: ");
                        int idUpdate = scanner.nextInt();
                        scanner.nextLine(); // Membuang new line
                        System.out.print("Masukkan Todo baru: ");
                        String todoBaru = scanner.nextLine();
                        System.out.print("Masukkan Kategori baru: ");
                        String kategoriBaru = scanner.nextLine();
                        System.out.print("Masukkan Tanggal Selesai baru (YYYY-MM-DD): ");
                        String tanggalSelesaiBaru = scanner.nextLine();
                        updateTodo(connection, idUpdate, todoBaru, kategoriBaru, tanggalSelesaiBaru);
                        break;
                    case 4:
                        System.out.print("Masukkan ID Todo yang akan dihapus: ");
                        int idHapus = scanner.nextInt();
                        hapusTodo(connection, idHapus);
                        break;
                    case 0:
                        System.out.println("Terima kasih!");
                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Menu tidak valid.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tambahTodo(Connection connection, String todo, String kategori, String tanggalSelesai) throws SQLException {
        String query = "INSERT INTO todo (todo, kategori, tanggal_selesai, status) VALUES (?, ?, ?, 'Belum Selesai')";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, todo);
        statement.setString(2, kategori);
        statement.setString(3, tanggalSelesai);
        statement.executeUpdate();
        System.out.println("Todo berhasil ditambahkan.");
    }

    public static void tampilkanTodoList(Connection connection) throws SQLException {
        String query = "SELECT * FROM todo";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String todo = resultSet.getString("todo");
            String kategori = resultSet.getString("kategori");
            String tanggalSelesai = resultSet.getString("tanggal_selesai");
            String status = resultSet.getString("status");
            System.out.println("ID: " + id);
            System.out.println("Todo: " + todo);
            System.out.println("Kategori: " + kategori);
            System.out.println("Tanggal Selesai: " + tanggalSelesai);
            System.out.println("Status: " + status);
            System.out.println("------------------------");
        }
    }

    public static void updateTodo(Connection connection, int id, String todoBaru, String kategoriBaru, String tanggalSelesaiBaru) throws SQLException {
        String query = "UPDATE todo SET todo = ?, kategori = ?, tanggal_selesai = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, todoBaru);
        statement.setString(2, kategoriBaru);
        statement.setString(3, tanggalSelesaiBaru);
        statement.setInt(4, id);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Todo berhasil diupdate.");
        } else {
            System.out.println("Todo dengan ID tersebut tidak ditemukan.");
        }
    }

    public static void hapusTodo(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM todo WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Todo berhasil dihapus.");
        } else {
            System.out.println("Todo dengan ID tersebut tidak ditemukan.");
        }
    }
}