package ecoembes.dto;

import java.util.Objects;

import ecoembes.entity.FillLevel;

public class DumpsterDTO {
	private String dumpster_id;
	private String location;
	private int postal_code;
	private FillLevel fill_level; //GREEN, ORANGE, RED
	private int container_number; //number of containers in the dumpster
	
	public DumpsterDTO() {
	}

	public DumpsterDTO(String dumpster_id, String location, int postal_code, FillLevel fill_level,
			int container_number) {
		super();
		this.dumpster_id = dumpster_id;
		this.location = location;
		this.postal_code = postal_code;
		this.fill_level = fill_level;
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

	
	
	
}
