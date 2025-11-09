package ecoembes.dto;

import ecoembes.entity.FillLevel;

public class AreaSnapshotDTO {
	private int postal_code;
	private int total_dumpsters;
	private int green_fill_level_count;
	private int orange_fill_level_count;
	private int red_fill_level_count;
	
	public AreaSnapshotDTO() {
	}
	
	public AreaSnapshotDTO(int postal_code, int total_dumpsters, int green_fill_level_count,
			int orange_fill_level_count, int red_fill_level_count) {
		super();
		this.postal_code = postal_code;
		this.total_dumpsters = total_dumpsters;
		this.green_fill_level_count = green_fill_level_count;
		this.orange_fill_level_count = orange_fill_level_count;
		this.red_fill_level_count = red_fill_level_count;
	}

	public int getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(int postal_code) {
		this.postal_code = postal_code;
	}

	public int getTotal_dumpsters() {
		return total_dumpsters;
	}

	public void setTotal_dumpsters(int total_dumpsters) {
		this.total_dumpsters = total_dumpsters;
	}

	public int getGreen_fill_level_count() {
		return green_fill_level_count;
	}

	public void setGreen_fill_level_count(int green_fill_level_count) {
		this.green_fill_level_count = green_fill_level_count;
	}

	public int getOrange_fill_level_count() {
		return orange_fill_level_count;
	}

	public void setOrange_fill_level_count(int orange_fill_level_count) {
		this.orange_fill_level_count = orange_fill_level_count;
	}

	public int getRed_fill_level_count() {
		return red_fill_level_count;
	}

	public void setRed_fill_level_count(int red_fill_level_count) {
		this.red_fill_level_count = red_fill_level_count;
	}
	
}
