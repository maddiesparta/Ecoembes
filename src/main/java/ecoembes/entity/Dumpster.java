package ecoembes.entity;

import java.util.Objects;

public class Dumpster {
	private String dumpster_id;
	private String location;
	private int postal_code;
	private int capacity; //estimated total number of containers it can hold
	private float estimated_weight;//tons
	private FillLevel fill_level; //GREEN, ORANGE, RED
	private int container_number; //number of containers in the dumpster
	
	public Dumpster() {
	}

	public Dumpster(String dumpster_id, String location, int postal_code, int capacity, float estimated_weight,
			 int container_number) {
		super();
		this.dumpster_id = dumpster_id;
		this.location = location;
		this.postal_code = postal_code;
		this.capacity = capacity;
		this.estimated_weight = estimated_weight;
		if(container_number < capacity / 3) {
			this.fill_level = FillLevel.GREEN;
		} else if(container_number < (2 * capacity) /3) {
			this.fill_level = FillLevel.ORANGE;
		} else {
			this.fill_level = FillLevel.RED;
		}
		this.container_number = container_number;
	}

	public String getDumpster_id() {
		return dumpster_id;
	}

	public void setDumpster_id(String dumpster_id) {
		this.dumpster_id = dumpster_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(int postal_code) {
		this.postal_code = postal_code;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public float getEstimated_weight() {
		return estimated_weight;
	}

	public void setEstimated_weight(float estimated_weight) {
		this.estimated_weight = estimated_weight;
	}

	public FillLevel getFill_level() {
		return fill_level;
	}

	public void setFill_level(FillLevel fill_level) {
		this.fill_level = fill_level;
	}

	public int getContainer_number() {
		return container_number;
	}

	public void setContainer_number(int container_number) {
		this.container_number = container_number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, container_number, dumpster_id, estimated_weight, fill_level, location,
				postal_code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dumpster other = (Dumpster) obj;
		return capacity == other.capacity && container_number == other.container_number
				&& Objects.equals(dumpster_id, other.dumpster_id)
				&& Float.floatToIntBits(estimated_weight) == Float.floatToIntBits(other.estimated_weight)
				&& fill_level == other.fill_level && Objects.equals(location, other.location)
				&& postal_code == other.postal_code;
	}
}
