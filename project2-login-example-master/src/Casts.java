
public class Casts{

	private String starId;

	private String movieId;

	
	public Casts(){
		
	}
	
	public Casts(String starId, String movieId) {
		this.starId = starId;
		this.movieId = movieId;

		
	}
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


	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("stars_in_movies Details - ");

		sb.append("starId:" + getstarId());
		sb.append(", ");
		sb.append("movieId:" + getmovieId());
		sb.append(".");
		
		return sb.toString();
	}
}