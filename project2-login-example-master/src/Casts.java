
public class Casts{

	private String movieTitle;
	//private String starId;
	//private String movieId;
	private String stagename;
	
	public Casts(){
		
	}
	
	public Casts(String movieTitle, String stagename) {
		this.movieTitle = movieTitle;
		this.stagename = stagename;

		
	}
	/**
	public String getstarId() {
		return starId;
	}

	public void setstarId(String starId) {
		this.starId = starId;
	}

	public String getmovieId() {
		return movieId;
	}

	public void setmovieId(String movieId) {
		this.movieId = movieId;
	}
	**/
	
	public String getMovietitle() {
		return movieTitle;
	}

	public void setMovietitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	
	public String getStagename() {
		return stagename;
	}

	public void setStagename(String stagename) {
		this.stagename = stagename;
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("stars_in_movies Details - ");

		sb.append("movieTitle:" + getMovietitle());
		sb.append(", ");
		sb.append("stagename:" + getStagename());
		sb.append(".");
		
		return sb.toString();
	}
}