package ecoembes.entity;

import java.util.Date;
import java.util.Objects;

public class Allocation {
	private Long allocation_id;
	private Date date;
	
	public Allocation() {
	}

	public Allocation(Long allocation_id, Date date) {
		super();
		this.allocation_id = allocation_id;
		this.date = date;
	}

	public Long getAllocation_id() {
		return allocation_id;
	}

	public void setAllocation_id(Long allocation_id) {
		this.allocation_id = allocation_id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allocation_id, date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Allocation other = (Allocation) obj;
		return Objects.equals(allocation_id, other.allocation_id) && Objects.equals(date, other.date);
	}


}
