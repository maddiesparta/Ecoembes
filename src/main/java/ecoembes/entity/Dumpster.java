package ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "dumpster")
public class Dumpster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dumpster_id;
	
	@Column(nullable = false, unique = false)
    private String postalCode;
	
	@Column(nullable = false)
    private String location;
	
	@Column(nullable = false)
    private int capacity;
	
	@Column(nullable = false)
    private float estimated_weight;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FillLevel fill_level; // GREEN, ORANGE, RED
	
	@Column(nullable = false)
    private int container_number;
	
	// One-to-many relationship with Usage entity
    @OneToMany(mappedBy = "dumpster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Usage> usage = new ArrayList<>();
    
    // One-to-many relationship with Allocation entity
    @OneToMany(mappedBy = "dumpster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Allocation> allocations = new ArrayList<>();
	
	
	//private String dumpster_id; No se si hay que hacerlo tambien para este attr
	//private String location;
	//private String postal_code;
	//private int capacity; //estimated total number of containers it can hold
	//private float estimated_weight; //tons
	//private FillLevel fill_level; //GREEN, ORANGE, RED // No tengo muy claro como ponerlo 
	//private int container_number; //number of containers in the dumpster
	//private List<Usage> usage;
	//private List<Allocation> allocations;
	
	public Dumpster() {
	}
	// sin dumpster_id ahora
	public Dumpster(String location, String postal_code, int capacity,
			 int container_number) {
		super();
		this.location = location;
		this.postalCode = postal_code;
		this.capacity = capacity;
		this.container_number = container_number;
		// Determine fill level based on container number (it can also be set directly)
		if(container_number < capacity / 3) {
			this.fill_level = FillLevel.GREEN;
		} else if(container_number < (2 * capacity) /3) {
			this.fill_level = FillLevel.ORANGE;
		} else {
			this.fill_level = FillLevel.RED;
		}
		// Estimate weight based on fill level
		if(this.fill_level == FillLevel.GREEN) {
			this.estimated_weight = (float) (0.024 * 0.3f);
		} else if(this.fill_level == FillLevel.ORANGE) {
			this.estimated_weight = (float) (0.024 * 0.6f);
		} else {
			this.estimated_weight = (float) 0.024;
		}
	}

	
	public Dumpster(Long dumpster_id, String postal_code, String location, int capacity, int container_number) {
		super();
		this.dumpster_id = dumpster_id;
		this.postalCode = postal_code;
		this.location = location;
		this.capacity = capacity;
		this.container_number = container_number;
	}
	
	public Long getDumpster_id() {
		return dumpster_id;
	}

//	public void setDumpster_id(String dumpster_id) {
//		this.dumpster_id = dumpster_id;
//	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPostal_code() {
		return postalCode;
	}

	public void setPostal_code(String postal_code) {
		this.postalCode = postal_code;
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
	public void setUsage(List<Usage> usage) {
		this.usage = usage;
	}
	public List<Usage> getUsage() {
		return usage;
	}
	
	public List<Allocation> getAllocations() {
		return allocations;
	}
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocations, capacity, container_number, dumpster_id, estimated_weight, fill_level,
				location, postalCode, usage);
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
		return Objects.equals(allocations, other.allocations) && capacity == other.capacity
				&& container_number == other.container_number && Objects.equals(dumpster_id, other.dumpster_id)
				&& Float.floatToIntBits(estimated_weight) == Float.floatToIntBits(other.estimated_weight)
				&& fill_level == other.fill_level && Objects.equals(location, other.location)
				&& Objects.equals(postalCode, other.postalCode) && Objects.equals(usage, other.usage);
	}
	

	
}
