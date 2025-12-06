package ecoembes.dto;

import java.time.LocalDate;

import ecoembes.entity.Allocation;

public class AllocationDTO {
	private Long allocationId;
	private Long dumpsterId;
	private Long plantId;
	private LocalDate date;
	private Long employeeId;
	
	public AllocationDTO() {
	}
	
	public AllocationDTO(Allocation allocation) {
		this.allocationId = allocation.getAllocation_id();
		this.dumpsterId = allocation.getDumpster().getDumpster_id();
		this.plantId = allocation.getPlant().getPlant_id();
		this.date = allocation.getDate();
		this.employeeId = allocation.getEmployee().getEmployee_id();
	}

	public Long getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
	}

	public Long getDumpsterId() {
		return dumpsterId;
	}

	public void setDumpsterId(Long dumpsterId) {
		this.dumpsterId = dumpsterId;
	}

	public Long getPlantId() {
		return plantId;
	}

	public void setPlantId(Long plantId) {
		this.plantId = plantId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	
}
