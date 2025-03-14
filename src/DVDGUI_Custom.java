import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DVDGUI_Custom implements DVDUserInterface {
    private DVDCollection dvdlist;

    private JFrame guiFrame;
    private JPanel mainPanel;
    private JPanel dvdControlsJPanel;
    private JPanel loadDvdJPanel;
    private DVDCollectionDisplayPanel dvdCollectionDisplayPanel;

    public DVDGUI_Custom(DVDCollection dvdCollection) {
        this.dvdlist = dvdCollection;
        this.guiFrame = new JFrame("DVD Collection GUI");
        this.guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.guiFrame.setSize(600, 600);
        this.guiFrame.setMinimumSize(new Dimension(600, 600));
        this.guiFrame.setResizable(false);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

        this.dvdCollectionDisplayPanel = new DVDCollectionDisplayPanel(this.dvdlist);

        this.loadDvdJPanel = Helper.createLoadSavePanel((type, filename) -> {
            System.out.println("Perform: " + type);
            if (type.equals("Load")) {
                this.dvdlist.loadData(filename);
                this.dvdCollectionDisplayPanel.update();
            }
            if (type.equals("Save")) {
                this.dvdlist.save();
                System.out.println(this.dvdlist);
            }
            if (type.equals("Exit")) {
                System.exit(0);
            }
        });
        this.dvdControlsJPanel = Helper.createDVDControlsPanel((type, name, rating, runningTime) -> {
            System.out.println("Perform: " + type);
            if (type.equals("Add/Modify")) {
                this.dvdlist.addOrModifyDVD(name, rating, runningTime);
                System.out.println(this.dvdlist);
                this.dvdCollectionDisplayPanel.update();
            }
            if (type.equals("Remove")) {
                this.dvdlist.removeDVD(name);
                System.out.println(this.dvdlist);
                this.dvdCollectionDisplayPanel.update();
            }
            if (type.equals("GetByRating")) {
                String dvds = this.dvdlist.getDVDsByRating(rating);
                System.out.println(dvds);
            }
            if (type.equals("GetTotalRunningTime")) {
                System.out.println(this.dvdlist.getTotalRunningTime());
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.add(this.loadDvdJPanel);
        topPanel.add(this.dvdControlsJPanel);
        this.mainPanel.add(topPanel);
        this.mainPanel.add(this.dvdCollectionDisplayPanel);

        this.guiFrame.add(this.mainPanel);
        this.guiFrame.setVisible(true);
    }

    @Override
    public void processCommands() {
        // TODO: errmmm...
    }

    private static class Helper {
        @FunctionalInterface
        interface DVDInteractConsumer {
            void accept(String type, String name, String rating, String runningTime);
        }
        @FunctionalInterface
        interface DVDLoadSaveInteractConsumer {
            void accept(String type, String filename);
        }
        public static JPanel createDVDControlsPanel(DVDInteractConsumer buttonClicked) {
            JPanel controlsPanel = new JPanel();
            controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
            controlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            controlsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            controlsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // Setup name field
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel dvdLabel = new JLabel("Name");
            labelPanel.add(dvdLabel);
            JTextField dvdNameField = new JTextField(8);
            labelPanel.add(dvdNameField);
            controlsPanel.add(labelPanel);

            // Setup rating field
            JPanel ratingPanel = new JPanel();
            ratingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel ratingLabel = new JLabel("Rating");
            JTextField ratingField = new JTextField(8);
            ratingPanel.add(ratingLabel);
            ratingPanel.add(ratingField);
            controlsPanel.add(ratingPanel);

            // Setup running time field
            JPanel runningTimePanel = new JPanel();
            runningTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel runningTimeLabel = new JLabel("Running Time");
            JTextField runningTimeField = new JTextField(8);
            runningTimePanel.add(runningTimeLabel);
            runningTimePanel.add(runningTimeField);
            controlsPanel.add(runningTimePanel);

            // Controls buttons
            JButton addNewDVDButton = new JButton("Add/Modify DVD");
            addNewDVDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("Add/Modify", dvdNameField.getText(), ratingField.getText(), runningTimeField.getText());
                }
            });
            JButton removeDVDButton = new JButton("Remove DVD");
            removeDVDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("Remove", dvdNameField.getText(), "", "");
                }
            });
            JButton getDVDByRatingButton = new JButton("Get DVDs by rating");
            getDVDByRatingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("GetByRating", "", ratingField.getText(), "");
                }
            });
            JButton getTotalRunningTimeButton = new JButton("Get total running time");
            getTotalRunningTimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("GetTotalRunningTime", "", "", "");
                }
            });
            controlsPanel.add(addNewDVDButton);
            controlsPanel.add(removeDVDButton);
            controlsPanel.add(getDVDByRatingButton);
            controlsPanel.add(getTotalRunningTimeButton);

            controlsPanel.add(Box.createVerticalGlue());
            return controlsPanel;
        }
        public static JPanel createLoadSavePanel(DVDLoadSaveInteractConsumer buttonClicked) {
            JPanel loadSavePanel = new JPanel();
            loadSavePanel.setLayout(new BoxLayout(loadSavePanel, BoxLayout.X_AXIS));
            loadSavePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            loadSavePanel.setAlignmentY(Component.TOP_ALIGNMENT);
            loadSavePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JTextField filename = new JTextField(8);

            JButton load = new JButton("Load");
            load.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("Load", filename.getText());
                }
            });

            JButton save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("Save", "");
                }
            });

            JButton exit = new JButton("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttonClicked.accept("Exit", "");
                }
            });

            loadSavePanel.add(filename);
            loadSavePanel.add(load);
            loadSavePanel.add(save);
            loadSavePanel.add(exit);
            return loadSavePanel;
        }
    }
}
