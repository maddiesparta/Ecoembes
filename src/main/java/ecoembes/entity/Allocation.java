package ecoembes.entity;

import java.time.LocalDate;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Allocation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocation_id;
	
	@Column(nullable = false)
    private LocalDate date = LocalDate.now();
	
	@ManyToOne
	@JoinColumn(name = "dumpster_id", nullable = false)
	private Dumpster dumpster;
	
	@ManyToOne
	@JoinColumn(name = "plant_id", nullable = false)
	private RecyclingPlant plant;
	
	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	//private Long allocation_id; No se si quitarlo y usar el primero o dejarla como el id y no el generado
	//private LocalDate date;
	//private Dumpster dumpster;
	//private RecyclingPlant plant;
	//private Employee employee;
	
	public Allocation() {
	}
	// depende del id que usemos lo modificamos
	public Allocation(LocalDate date, Dumpster dumpster, RecyclingPlant plant, Employee employee) {
		super();
		this.date = LocalDate.now();
		this.dumpster = dumpster;
		this.plant = plant;
		this.employee = employee;
	}

	public Long getAllocation_id() {
		return allocation_id;
	}

//	public void setAllocation_id(Long allocation_id) {
//		this.allocation_id = allocation_id;
//	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Dumpster getDumpster() {
		return dumpster;
	}

	public void setDumpster(Dumpster dumpster) {
		this.dumpster = dumpster;
	}

	public RecyclingPlant getPlant() {
		return plant;
	}

	public void setPlant(RecyclingPlant plant) {
		this.plant = plant;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocation_id, date, dumpster, employee, plant);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Allocation other = (Allocation) obj;
		return Objects.equals(allocation_id, other.allocation_id) && Objects.equals(date, other.date)
				&& Objects.equals(dumpster, other.dumpster) && Objects.equals(employee, other.employee)
				&& Objects.equals(plant, other.plant);
	}
	@Override
	public String toString() {
		return "Allocation [allocation_id=" + allocation_id + ", date=" + date + ", dumpster=" + dumpster + ", plant="
				+ plant + ", employee=" + employee + "]";
	}
	
	
}
