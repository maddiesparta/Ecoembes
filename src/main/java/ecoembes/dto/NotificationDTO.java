package ecoembes.dto;

public class NotificationDTO {
	private int dumpsters;
	private int packages;
	private float tons;

	public NotificationDTO(int dumpsters2, int packages2, float tons2) {
		this.dumpsters = dumpsters;
		this.packages = packages;
		this.tons = tons;
	}

	public int getDumpsters() {
		return dumpsters;
	}

	public void setDumpsters(int dumpsters) {
		this.dumpsters = dumpsters;
	}

	public int getPackages() {
		return packages;
	}

	public void setPackages(int packages) {
		this.packages = packages;
	}

	public float getTons() {
		return tons;
	}

	public void setTons(float tons) {
		this.tons = tons;
	}
	
}
