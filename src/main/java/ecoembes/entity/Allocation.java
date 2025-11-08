package ecoembes.entity;

import java.util.Date;
import java.util.Objects;

public class Allocation {
	private Long allocation_id;
	private Long plant_id;
	private Long employee_id;
	private int dumpster_id;

	public Allocation() {
	}

	public Allocation(Long allocation_id, Long plant_id, Long employee_id, int dumpster_id, Date date) {
		super();
		this.allocation_id = allocation_id;
		this.plant_id = plant_id;
		this.employee_id = employee_id;
		this.dumpster_id = dumpster_id;
	}

	public Long getAllocation_id() {
		return allocation_id;
	}

	public void setAllocation_id(Long allocation_id) {
		this.allocation_id = allocation_id;
	}

	public Long getPlant_id() {
		return plant_id;
	}

	public void setPlant_id(Long plant_id) {
		this.plant_id = plant_id;
	}

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public int getDumpster_id() {
		return dumpster_id;
	}

	public void setDumpster_id(int dumpster_id) {
		this.dumpster_id = dumpster_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocation_id, dumpster_id, employee_id, plant_id);
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
		return allocation_id == other.allocation_id;
	}

	
	
	
}
