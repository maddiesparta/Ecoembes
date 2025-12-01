package ecoembes.dto;

public class AssignmentRequestDTO {
    private int dumpsterCount;
    private int packageCount;
    private String assignedBy;
    
    public AssignmentRequestDTO() {}
    
    // Getters y Setters
    public int getDumpsterCount() { return dumpsterCount; }
    public void setDumpsterCount(int dumpsterCount) { this.dumpsterCount = dumpsterCount; }
    
    public int getPackageCount() { return packageCount; }
    public void setPackageCount(int packageCount) { this.packageCount = packageCount; }
    
    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
}
