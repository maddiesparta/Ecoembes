package ecoembes.entity;

import java.sql.Date;
import java.util.Objects;

public class Usage {
	private Date date;
	private String usage_id;
	private FillLevel fill_level; //GREEN, ORANGE, RED
	public Usage() {
	}
	public Usage(Date date, String usage_id, FillLevel fill_level) {
		super();
		this.date = date;
		this.usage_id = usage_id;
		this.fill_level = fill_level;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUsage_id() {
		return usage_id;
	}
	public void setUsage_id(String usage_id) {
		this.usage_id = usage_id;
	}
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
