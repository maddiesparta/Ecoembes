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
@Table(name = "plant")
public class RecyclingPlant {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plant_id;
	
	@Column(nullable = false, unique = true)
    private String name;
	
	@Column(nullable = false)
    private float total_capacity;
	
	
	// One-to-many relationship with Allocation entity
	@OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Allocation> allocations = new ArrayList<>();
	
	public RecyclingPlant() {
	}
	// quitado el id
	public RecyclingPlant(String plant_name, float total_capacity) {
		super();
		this.name = plant_name;
		this.total_capacity = total_capacity;
	}

	public Long getPlant_id() {
		return plant_id;
	}

	public String getPlant_name() {
		return name;
	}

	public void setPlant_name(String plant_name) {
		this.name = plant_name;
	}

	public float getTotal_capacity() {
		return total_capacity;
	}

	public void setTotal_capacity(float total_capacity) {
		this.total_capacity = total_capacity;
	}

	public List<Allocation> getAllocations() {
		return allocations;
	}
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	@Override
	public int hashCode() { // Quitado dumpsters
		return Objects.hash(allocations, plant_id, name, total_capacity);
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
		return Objects.equals(allocations, other.allocations)
				&& Objects.equals(plant_id, other.plant_id)
				&& Objects.equals(name, other.name)
				&& Float.floatToIntBits(total_capacity) == Float.floatToIntBits(other.total_capacity);
	}
	
	
}
