package ecoembes.entity;

import java.time.LocalDate;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usage_id;
	
	@Column(nullable = false, unique = true)
    private String postal_code;
	
	@Column(nullable = false)
	private LocalDate date;
	
	//private LocalDate date;
	//private String usage_id;
	private FillLevel fill_level; //GREEN, ORANGE, RED
	public Usage() {
	}
	public Usage(LocalDate date, Long usage_id, FillLevel fill_level) {
		super();
		this.date = date;
		this.fill_level = fill_level;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Long getUsage_id() {
		return usage_id;
	}
//	public void setUsage_id(String usage_id) {
//		this.usage_id = usage_id;
//	}
	public FillLevel getFill_level() {
		return fill_level;
	}
	public void setFill_level(FillLevel fill_level) {
		this.fill_level = fill_level;
	}
	@Override
	public int hashCode() {
		return Objects.hash(date, fill_level, usage_id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usage other = (Usage) obj;
		return Objects.equals(date, other.date) && fill_level == other.fill_level
				&& Objects.equals(usage_id, other.usage_id);
	}
	
	
}
