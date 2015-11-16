package model;

public class Province {
	   private int id;
	   private String ProvinceCode;
	   private String provinceName;
		
		
    public String getProvinceCode() {
		return ProvinceCode;
	}


	public void setProvinceCode(String provinceCode) {
		ProvinceCode = provinceCode;
	}
	
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}

	
	
	public String getProvinceName() {
		return provinceName;
	}
	
	
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	
}
