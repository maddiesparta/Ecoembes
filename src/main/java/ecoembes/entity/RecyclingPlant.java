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
    private String plant_name;
	
	@Column(nullable = false)
    private float total_capacity;
	
	@Column(nullable = false)
    private float current_capacity;
	
	// EN EL DIAGRAMA DE CLASES YA NO HAY RELACION DIRECTA ENTRE DUMPSTER Y RECPLANT --> esto hace falta?
	// lo he puesto por la lista de dumpsters que habia antes como atributo
	// One-to-many relationship with Dumpster entity
//    @OneToMany(mappedBy = "recyclingPlant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Dumpster> dumpsters = new ArrayList<>();
	
	// Creo que no hace falta porque para el mappedBy, no hay attributo recyclingPlant en Dumpster
	
	// One-to-many relationship with Allocation entity
	@OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Allocation> allocations = new ArrayList<>();
	
	
	//private String plant_id;
	//private String plant_name;
	//private float total_capacity;
	//private float current_capacity;
	//private List<Dumpster> dumpsters = new ArrayList<>(); --> estaba mal creo, no tiene que estar
	//private List<Allocation> allocations;

	
	public RecyclingPlant() {
	}
	// quitado el id
	public RecyclingPlant(String plant_name, float total_capacity) {
		super();
		this.plant_name = plant_name;
		this.total_capacity = total_capacity;
		this.current_capacity = 0;
	}

	public Long getPlant_id() {
		return plant_id;
	}

//	public void setPlant_id(String plant_id) {
//		this.plant_id = plant_id;
//	}

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
	
//	public List<Dumpster> getDumpsters() {
//		return dumpsters;
//	}
	
//	public void setDumpsters(List<Dumpster> dumpsters) {
//		this.dumpsters = dumpsters;
//	}
	
	public float getCurrent_capacity() {
		return current_capacity;
	}
	
	public void setCurrent_capacity(float current_capacity) {
		this.current_capacity = current_capacity;
	}
	
	public List<Allocation> getAllocations() {
		return allocations;
	}
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	@Override
	public int hashCode() { // Quitado dumpsters
		return Objects.hash(allocations, current_capacity, plant_id, plant_name, total_capacity);
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
				&& Float.floatToIntBits(current_capacity) == Float.floatToIntBits(other.current_capacity)
				&& Objects.equals(plant_id, other.plant_id)
				&& Objects.equals(plant_name, other.plant_name)
				&& Float.floatToIntBits(total_capacity) == Float.floatToIntBits(other.total_capacity);
	} // Quitado && Objects.equals(dumpsters, other.dumpsters) &&
	
	
}
