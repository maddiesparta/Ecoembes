package ecoembes.dto;

import java.util.Date;

import ecoembes.entity.FillLevel;

public class UsageDTO {
	private Date date;
	private FillLevel fill_level;
	
	public UsageDTO() {
	}
	public UsageDTO(Date date, FillLevel fill_level) {
		super();
		this.date = date;
		this.fill_level = fill_level;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public FillLevel getFill_level() {
		return fill_level;
	}
	public void setFill_level(FillLevel fill_level) {
		this.fill_level = fill_level;
	}
	
}
