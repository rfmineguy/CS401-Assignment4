import java.util.*;

/**
 * 	Program to display and modify a simple DVD collection
 */

public class DVDManager {

	public static void main(String[] args) {
		DVDUserInterface dlInterface;
		DVDCollection dl = new DVDCollection();

		// Scanner scan = new Scanner(System.in);

		// System.out.println("Enter name of data file to load:");
		// String filename = scan.nextLine();
		// dl.loadData(filename);

		dlInterface = new DVDGUI_Custom(dl);
		dlInterface.processCommands();
	}

}
