// DONE
public class DVD {

	// Fields:

	private String title;		// Title of this DVD
	private String rating;		// Rating of this DVD
	private int runningTime;	// Running time of this DVD in minutes

	public DVD(String dvdTitle, String dvdRating, int dvdRunningTime)
	{
		this.title = dvdTitle;
		this.rating = dvdRating;
		this.runningTime = dvdRunningTime;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getRating()
	{
		return this.rating;
	}

	public int getRunningTime()
	{
		return this.runningTime;
	}

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	public void setRating(String newRating) {
		this.rating = newRating;
	}

	public void setRunningTime(int newRunningTime) {
		this.runningTime = newRunningTime;
	}

	/** Specification
	 *     Example: ANGELS AND DEMONS/PG-13/138min
	 *
	 *     Format: <title>/<rating>/<time>
	 */
	public String toString() {
		return this.title.toUpperCase() + "/" + this.rating.toUpperCase() + "/" + this.runningTime + "min\n";
	}


}
