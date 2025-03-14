import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class DVDCollectionDisplayPanel extends JPanel {
    private DVDCollection collection;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    public DVDCollectionDisplayPanel(DVDCollection collection) {
        super(); // explicitly initialize the panel
        this.collection = collection;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        update();

        scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        update();
    }

    public void update() {
        String[] parsedDvds = parseDvdCollectionString();
        if (parsedDvds == null) {
            System.err.println("Something went wrong parsing the data file");
            return;
        }
        this.contentPanel.removeAll();
        contentPanel.setPreferredSize(new Dimension(this.getWidth(), 1000));
        for (String parsedDvd : parsedDvds) {
            String[] dvdComponents = parsedDvd.split("/");
            JPanel testPanel = new JPanel();
            testPanel.setPreferredSize(new Dimension(200, 100));
            testPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            testPanel.add(new JLabel(parsedDvd));
            testPanel.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked on dvd panel: " + parsedDvd);
                    JPanel popupPanel = new JPanel();
                    popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));
                    // Open a popup with layout for changing the dvd attributes. Note that the title never changes

                    JLabel ratingLabel = new JLabel("Rating");
                    JTextField ratingField = new JTextField(8);
                    popupPanel.add(ratingLabel);
                    popupPanel.add(ratingField);

                    JLabel runningTimeLabel = new JLabel("Running Time");
                    JTextField runningTimeField = new JTextField(8);
                    popupPanel.add(runningTimeLabel);
                    popupPanel.add(runningTimeField);

                    int result = JOptionPane.showConfirmDialog(null, popupPanel, "Enter details for " + dvdComponents[0], JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.CANCEL_OPTION) return;

                    System.out.println("New rating: " + ratingField.getText());
                    System.out.println("New running time: " + runningTimeField.getText());

                    System.out.println(Arrays.toString(dvdComponents));
                    String rating = !ratingField.getText().isEmpty() ? ratingField.getText() : dvdComponents[1];
                    String runningTime = !runningTimeField.getText().isEmpty() ? runningTimeField.getText() : dvdComponents[2].substring(0, dvdComponents.length - 3);
                    System.out.println("Modifying: " + dvdComponents[0] + " with rating=" + rating + ", runningTime=" + runningTime);
                    collection.addOrModifyDVD(dvdComponents[0], rating, runningTime);
                    update();
                    System.out.println(collection);
                }
                public void mousePressed(MouseEvent e) {

                }
                public void mouseReleased(MouseEvent e) {

                }
                public void mouseEntered(MouseEvent e) {

                }
                public void mouseExited(MouseEvent e) {

                }
            });
            contentPanel.add(testPanel);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // return list of dvds in order
    private String[] parseDvdCollectionString() {
        System.out.println("Parse dvd collection string");
        int numDvds = 0;
        String[] lines = collection.toString().split("\\R");
        String[] returnArray = null;
        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i].split(" ");
            if (i == 0) {
                numDvds = Integer.parseInt(line[2]);
                returnArray = new String[numDvds];
            }
            else if (i == 1) continue; // we dont care about the length of the internal array
            else {
                // here we are parsing the dvd entries
                String[] tokens = lines[i].split("=");
                int index = i - 2;
                String content = tokens[1].trim();
                returnArray[index] = content;
            }
        }
        return returnArray;
    }
}
