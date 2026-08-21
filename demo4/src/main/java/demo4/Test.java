package demo4;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class Test {
	
	
	@PostMapping("/search")
	public String search(@RequestBody Employee employee) {

	    int id = employee.getId();

	    return "Searching Employee " + id;
	}
	
	
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable int id) {

	    try {
	        Class.forName("org.postgresql.Driver");

	        Connection con = DriverManager.getConnection(
	                "jdbc:postgresql://localhost:5432/Demo4",
	                "postgres",
	                "password");

	        Statement stmt = con.createStatement();

	        ResultSet rs = stmt.executeQuery(
	                "SELECT * FROM Employee WHERE id=" + id);

	        if (rs.next()) {

	            Employee emp = new Employee();

	            emp.setId(rs.getInt("id"));
	            emp.setName(rs.getString("name"));
	            emp.setSalary(rs.getInt("salary"));
	            emp.setDepartment(rs.getString("department"));

	            con.close();

	            return emp;
	        }

	        con.close();
	        return null;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	@GetMapping("/employees")
	public List<Employee> getEmployees() {

	    String DB_URL = "jdbc:postgresql://localhost:5432/Demo4";
	    String USER = "postgres";
	    String PASS = "password";

	    List<Employee> employees = new ArrayList<>();

	    try {

	        Class.forName("org.postgresql.Driver");

	        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

	        Statement stmt = con.createStatement();

	        ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");

	        while(rs.next()) {

	            Employee emp = new Employee();

	            emp.setId(rs.getInt("id"));
	            emp.setName(rs.getString("name"));
	            emp.setSalary(rs.getInt("salary"));
	            emp.setDepartment(rs.getString("department"));

	            employees.add(emp);
	        }

	        con.close();

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return employees;
	}
	
	
	@GetMapping("/read/{id}")
	public String readById(@PathVariable int id) {

	    String DB_URL = "jdbc:postgresql://localhost:5432/Demo4";
	    String USER = "postgres";
	    String PASS = "password";

	    try {
	        Class.forName("org.postgresql.Driver");

	        Connection con =
	                DriverManager.getConnection(DB_URL, USER, PASS);

	        Statement stmt = con.createStatement();

	        ResultSet rs =
	                stmt.executeQuery(
	                        "SELECT * FROM Employee WHERE id=" + id);

	        if (rs.next()) {

	            return "ID=" + rs.getInt("id")
	                    + ", Name=" + rs.getString("name")
	                    + ", Salary=" + rs.getInt("salary")
	                    + ", Department=" + rs.getString("department");
	        }

	        con.close();

	        return "Employee not found";

	    } catch (Exception e) {
	        e.printStackTrace();
	        return e.getMessage();
	    }
	}
	
	@PostMapping("/create")
	public String create(@RequestBody Employee employee) {

	    String DB_URL = "jdbc:postgresql://localhost:5432/Demo4";
	    String USER = "postgres";
	    String PASS = "password";

	    try {
	        Class.forName("org.postgresql.Driver");

	        Connection con =
	            DriverManager.getConnection(DB_URL, USER, PASS);

	        Statement stmt = con.createStatement();

	        String sql =
	        	    "INSERT INTO Employee(id,name,salary,department) VALUES(" +
	        	    employee.getId() + ",'" +
	        	    employee.getName() + "'," +
	        	    employee.getSalary() + ",'" +
	        	    employee.getDepartment() + "')";

	        stmt.executeUpdate(sql);

	        con.close();

	        return "Employee Inserted Successfully";

	    } catch(Exception e) {
	        e.printStackTrace();
	        return e.getMessage();
	    }
	}
				

	@PutMapping("/update/{id}")
	public String update(@PathVariable int id,
	                     @RequestBody Employee employee) {

	    String DB_URL = "jdbc:postgresql://localhost:5432/Demo4";
	    String USER = "postgres";
	    String PASS = "password";

	    try {
	        Class.forName("org.postgresql.Driver");

	        Connection con =
	                DriverManager.getConnection(DB_URL, USER, PASS);

	        Statement stmt = con.createStatement();

	        String sql =
	        	    "UPDATE Employee " +
	        	    "SET name='" + employee.getName() +
	        	    "', salary=" + employee.getSalary() +
	        	    ", department='" + employee.getDepartment() +
	        	    "' WHERE id=" + id;

	        int rows = stmt.executeUpdate(sql);

	        con.close();

	        if (rows > 0) {
	            return "Employee updated successfully";
	        } else {
	            return "No student found with ID " + id;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return e.getMessage();
	    }
	}
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable int id) {

	    String DB_URL = "jdbc:postgresql://localhost:5432/Demo4";
	    String USER = "postgres";
	    String PASS = "password";

	    try {
	        Class.forName("org.postgresql.Driver");

	        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

	        Statement stmt = con.createStatement();

	        String sql = "DELETE FROM Employee WHERE id = " + id;

	        int rows = stmt.executeUpdate(sql);

	        con.close();

	        if (rows > 0) {
	            return "Employee with ID " + id + " deleted successfully";
	        } else {
	            return "No Employee found with ID " + id;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return e.getMessage();
	    }
	}			
	}


