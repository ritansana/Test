import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnect {
	
	static final String  DB_URL = "jdbc:postgresql://localhost:5432/DemoDb";
	static final String USER = "postgres";
	static final String PASS = "password";
	
	public static void main(String[] args) {
		try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
			
			Statement stmt = con.createStatement()){
			
			String createTableSQL ="CREATE TABLE IF NOT EXISTS Students(" + "id SERIAL PRIMARY KEY, " + "name VARCHAR(50)," +"marks INT)";
			stmt.executeUpdate(createTableSQL);
			System.out.println("create table succesfully");
			
			String insertSQL = "INSERT INTO Students(id, name, marks) " + " VALUES( 102, 'Tim', 85), " + "( 103, 'Den' , 90)";
			stmt.executeUpdate(insertSQL);
			System.out.println("Data inserted successfully");
			
			String selectSQL ="SELECT * from Students";
			ResultSet rs = stmt.executeQuery(selectSQL);
			
			while(rs.next()) {
				System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
			}
		}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}

	}


