package ecoembes.entity;

import java.time.LocalDate;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Usage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usage_id;
	
	@ManyToOne(optional = false)
	private Dumpster dumpster;
	
	
	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private FillLevel fill_level; //GREEN, ORANGE, RED
	
	public Usage() {
	}
	public Usage(Dumpster dumpster, LocalDate date, FillLevel fill_level) {
		this.dumpster = dumpster;
		this.date = date;
		this.fill_level = fill_level;
	}
	public Long getUsage_id() {
		return usage_id;
	}
	public void setUsage_id(Long usage_id) {
		this.usage_id = usage_id;
	}
	public Dumpster getDumpster() {
		return dumpster;
	}
	public void setDumpster(Dumpster dumpster) {
		this.dumpster = dumpster;
	}

	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public FillLevel getFill_level() {
		return fill_level;
	}
	public void setFill_level(FillLevel fill_level) {
		this.fill_level = fill_level;
	}
	@Override
	public int hashCode() {
		return Objects.hash(date, dumpster, fill_level, usage_id);
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
		return Objects.equals(date, other.date) && Objects.equals(dumpster, other.dumpster)
				&& fill_level == other.fill_level 
				&& Objects.equals(usage_id, other.usage_id);
	}
	
}
