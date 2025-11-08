package ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RecyclingPlant {
	private Long plant_id;
	private String plant_name;
	private float total_capacity;
	private List<Dumpster> dumpsters = new ArrayList<>();

	
	public RecyclingPlant() {
	}
	
	public RecyclingPlant(Long plant_id, String plant_name, float total_capacity, List<Dumpster> dumpsters) {
		super();
		this.plant_id = plant_id;
		this.plant_name = plant_name;
		this.total_capacity = total_capacity;
		this.dumpsters = dumpsters;
	}

	public Long getPlant_id() {
		return plant_id;
	}

	public void setPlant_id(Long plant_id) {
		this.plant_id = plant_id;
	}

	public String getPlant_name() {
		return plant_name;
	}

	public void setPlant_name(String plant_name) {
		this.plant_name = plant_name;
	}

	public float getTotal_capacity() {
		return total_capacity;
	}

	public void setTotal_capacity(float total_capacity) {
		this.total_capacity = total_capacity;
	}
	
	public List<Dumpster> getDumpsters() {
		return dumpsters;
	}
	
	public void setDumpsters(List<Dumpster> dumpsters) {
		this.dumpsters = dumpsters;
	}

	@Override
	public int hashCode() {
		return Objects.hash(plant_id, plant_name, total_capacity, dumpsters);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecyclingPlant other = (RecyclingPlant) obj;
		return Objects.equals(plant_id, other.plant_id) && Objects.equals(plant_name, other.plant_name)
				&& Float.floatToIntBits(total_capacity) == Float.floatToIntBits(other.total_capacity) && Objects.equals(dumpsters, other.dumpsters);
	}	
	
	
}
