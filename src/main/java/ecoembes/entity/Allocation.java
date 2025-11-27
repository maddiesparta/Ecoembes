package ecoembes.entity;

import java.time.LocalDate;

import java.util.Objects;

public class Allocation {
	private Long allocation_id;
	private LocalDate date;
	private Dumpster dumpster;
	private RecyclingPlant plant;
	private Employee employee;
	
	public Allocation() {
	}

	public Allocation(Long allocation_id, LocalDate date, Dumpster dumpster, RecyclingPlant plant, Employee employee) {
		super();
		this.allocation_id = allocation_id;
		this.date = LocalDate.now();
		this.dumpster = dumpster;
		this.plant = plant;
		this.employee = employee;
	}

	public Long getAllocation_id() {
		return allocation_id;
	}

	public void setAllocation_id(Long allocation_id) {
		this.allocation_id = allocation_id;
	}

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
	
	
}
