package ecoembes.dto;

import java.time.LocalDate;

import ecoembes.entity.FillLevel;
import ecoembes.entity.Usage;

public class UsageDTO {
	private LocalDate date;
	private FillLevel fill_level;
	
	public UsageDTO() {
	}
	public UsageDTO(LocalDate date, FillLevel fill_level) {
		super();
		this.date = date;
		this.fill_level = fill_level;
	}
	public UsageDTO(Usage u) {
		this.date = u.getDate();
		this.fill_level = u.getFill_level();
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
	
}
