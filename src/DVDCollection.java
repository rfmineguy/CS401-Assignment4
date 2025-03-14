import java.io.*;
import java.util.Scanner;

public class DVDCollection {
	// Data fields

	/** The current number of DVDs in the array */
	private int numdvds;

	/** The array to contain the DVDs */
	private DVD[] dvdArray;

	/** The name of the data file that contains dvd data */
	private String sourceName;

	/** Boolean flag to indicate whether the DVD collection was
	 modified since it was last saved. */
	private boolean modified;

	/**
	 *  Constructs an empty directory as an array
	 *  with an initial capacity of 7. When we try to
	 *  insert into a full array, we will double the size of
	 *  the array first.
	 */
	public DVDCollection() {
		numdvds = 0;
		dvdArray = new DVD[7];
	}

	/** Specification
	 *  numdvds = 3
	 * 	dvdArray.length = 7
	 * 	dvdArray[0] = ANGELS AND DEMONS/PG-13/138min
	 * 	dvdArray[1] = STAR TREK/R/127min
	 * 	dvdArray[2] = UP/PG/96min
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("numdvds = ").append(this.numdvds).append("\n");
		ret.append("dvdArray.length = ").append(this.dvdArray.length).append("\n");
		for (int i = 0; i < this.numdvds; i++) {
			DVD d = this.dvdArray[i];
			ret.append("dvdArray[%d] = ".formatted(i)).append(d.toString());
		}

		return ret.toString();
	}

	// O(nlogn)
	public void addOrModifyDVD(String title, String rating, String runningTime) {
		// NOTE: Be careful. Running time is a string here
		// since the user might enter non-digits when prompted.
		// If the array is full and a new DVD needs to be added,
		// double the size of the array first.

		// 1. Convert runningTime to an integer
		int runningTimeInt = 0;
		try {
			runningTimeInt = Integer.parseInt(runningTime);
		} catch (NumberFormatException e) {
			if (!runningTime.isEmpty()) return;
		}

		// 2. Check if rating is a valid rating
		if (!isValidRating(rating)) {
			System.err.println("Invalid rating");
			return;
		}

		// 3. Check if the dvd exists or not (by title)
		// O(logn)
		int l = 0, r = this.numdvds - 1;
		int mid = l;
		while (l <= r) {
			mid = l + ((r - l) / 2);
			System.out.println("dvd[mid]: " + this.dvdArray[mid]);
			int cmp = compareTitleLower(this.dvdArray[mid].getTitle(), title);
			if (cmp == 0) break; // found (mid is the found index)
			if (cmp > 0) r = mid - 1;
			if (cmp < 0) l = mid + 1;
		}
		System.out.println("l = " + l + ", r = " + r + ", mid = " + mid);
		if (l <= r) {
			// we found the element, modify the found element
			this.dvdArray[mid].setTitle(title);
			this.dvdArray[mid].setRunningTime(runningTimeInt);
			this.dvdArray[mid].setRating(rating);
			System.out.println("DVDCollection: Modified dvd");
		}
		else {
			// the element was not found, we need to add it
			if (this.numdvds + 1 >= this.dvdArray.length) {
				setDvdArraySize(this.numdvds * 2);
			}
			// shift [mid, numdvds] one to the right
			for (int i = this.numdvds; i > mid; i--) {
				this.dvdArray[i] = this.dvdArray[i - 1];
			}
			// set mid to the new entry
			this.dvdArray[l] = new DVD(title, rating, runningTimeInt);
			this.numdvds++;
		}
		this.modified = true;
	}
	public void removeDVD(String title) {
		// @TODO: Figure out how to refactor this binary search into a function
		int l = 0, r = this.numdvds - 1;
		int mid = l;
		while (l <= r) {
			mid = l + ((r - l) / 2);
			int cmp = compareTitle(this.dvdArray[mid].getTitle(), title); // case of title comparison is relevant
			if (cmp == 0) break; // found (mid is the found index)
			if (cmp > 0) r = mid - 1;
			if (cmp < 0) l = mid + 1;
		}
		if (l <= r) { // remove element 'mid'
			for (int i = mid; i < this.numdvds - 1; i++)
				this.dvdArray[i] = this.dvdArray[i + 1];
			this.numdvds--;
		}
		else { /* do nothing */ }
		this.modified = true;
	}
	public String getDVDsByRating(String rating) {
		if (!isValidRating(rating)) {
			return "Invalid rating: " + rating;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.numdvds; i++) {
			DVD d = this.dvdArray[i];
			if (d == null) continue;
			if (d.getRating().equalsIgnoreCase(rating)) { // rating case is insignificant
				builder.append(d.toString());
			}
		}
		return builder.toString();
	}
	public int getTotalRunningTime() {
		int total = 0;
		for (int i = 0; i < this.numdvds; i++) {
			DVD d = this.dvdArray[i];
			if (d == null) continue;
			total += d.getRunningTime();
		}
		return total;
	}
	public void loadData(String filename) {
		File f = null;
		Scanner sf = null;
		try {
			f = new File(filename);
			sf = new Scanner(f);
		} catch (FileNotFoundException e) {
			return;
		}

		this.dvdArray = new DVD[7];
		this.numdvds = 0;
		while (sf.hasNextLine()) {
			String line = sf.nextLine();
			if (line.length() == 0) continue;
			String[] words = line.split(",");
			if (words.length != 3) {
				continue;
			}
			this.addOrModifyDVD(words[0], words[1], words[2]);
		}

		sf.close();
		this.sourceName = filename;
	}
	public void save() {
		if (!this.modified) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.numdvds; i++) {
			DVD d = this.dvdArray[i];
			if (d == null) continue;
			builder.append(d.getTitle().toUpperCase());
			builder.append(',');
			builder.append(d.getRating().toUpperCase());
			builder.append(',');
			builder.append(d.getRunningTime());
			builder.append('\n');
		}

		try {
			FileWriter f = new FileWriter(this.sourceName);;
			f.write(builder.toString());
			f.close();
		} catch (Exception e) {
			return;
		}

		this.modified = false;
	}

	// Additional private helper methods go here:
	private int compareTitleLower(String a, String b) {
		return a.toLowerCase().compareTo(b.toLowerCase());
	}
	private int compareTitle(String a, String b) {
		return a.compareTo(b);
	}
	private void setDvdArraySize(int newSize) {
		DVD[] newDvds = new DVD[newSize];
		for (int i = 0; i < this.numdvds; i++) {
			newDvds[i] = dvdArray[i];
		}
		dvdArray = newDvds;
	}
	private boolean isValidRating(String rating) {
		return rating.equals("G") || rating.equals("PG")
				|| rating.equals("PG-13") || rating.equals("R")
				|| rating.equals("NC-17");
	}
}