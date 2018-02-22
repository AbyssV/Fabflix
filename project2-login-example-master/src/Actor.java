




public class Actor {

	
	private String stagename;
	
	private int year;

	
	public Actor(){
		
	}
	
	public Actor( String stagename, int year) {
		
		this.stagename = stagename;
		this.year  = year;
		
	}


	public String getStagename() {
		return stagename;
	}

	public void setStagename(String stagename) {
		this.stagename = stagename;
	}


	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");

		sb.append("Title:" + getStagename());
		sb.append(", ");
		sb.append("BirthYear:" + getYear());
		sb.append(". ");

		
		return sb.toString();
	}
}