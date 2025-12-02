package ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;
	
	@Column(nullable = false, unique = true)
    private String employee_name;
	
	@Column(nullable = false, unique = true)
    private String email;
	
	@Column(nullable = false, unique = true)
    private String password;
	
	// One-to-many relationship with Allocations entity
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Allocation> allocations = new ArrayList<>();
	
	public Employee() {
	}
	
	public Employee(String employee_name, String email, String password) {
		super();
		this.employee_name = employee_name;
		this.email = email;
		this.password = password;
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public Long getEmployee_id() {
		return employee_id;
	}

//	public void setEmployee_id(String employee_id) {
//		this.employee_id = employee_id;
//	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Allocation> getAllocations() {
		return allocations;
	}
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocations, email, employee_id, employee_name, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(allocations, other.allocations) && Objects.equals(email, other.email)
				&& employee_id == other.employee_id && Objects.equals(employee_name, other.employee_name)
				&& Objects.equals(password, other.password);
	}
	
	
}
