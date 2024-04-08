import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;				// import the Graphics class
import java.awt.Graphics2D;				// import the Graphics2D class
import java.awt.GridLayout;
import java.awt.event.ActionEvent;		// A JPanel is a simple container for graphics
import javax.swing.Timer;				// Timer kicks out ActionEvents
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;	// The class needs to listen for ActionEvents
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;				// A Timer object kicks out ActionEvents at regular intervals
import javax.swing.JSlider;

import Cell.Cell;
import World.Wind;
import World.World;

// Don't worry too much about "extends JPanel" and "implements ActionListener"
// We will cover that in excruciating detail in the future
public class Simple_Graphics extends JPanel implements ActionListener{
	private final int DELAY = 100;	// delay for ActionListener Timer. Change this to change the speed of the simulation
	private Timer timer;			// Timer object to control repainting.
	private World grid; 			// needs a reference to the class that contains the matrix
	private int cellSize;
	
	/**
	 * Constructor
	 * @param grid - the World object to be rendered
	 */
	public Simple_Graphics(World grid, int cellSize) {
		this.grid = grid;			// store the reference to the grid
		this.cellSize = cellSize;
		initUI();
		initTimer();				// initialize the timer
	}

	// Inside the initUI() method of Simple_Graphics class
	private void initUI() {
		JFrame frame = new JFrame("Fire Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int frameSize = grid.getSize() * this.cellSize;

		frame.setPreferredSize(new java.awt.Dimension(frameSize + 25, frameSize + 110));
	
		JButton toggleButton = new JButton("Stop");
		toggleButton.setActionCommand("Toggle");
		toggleButton.addActionListener(this);
	
		JButton spawnFireButton = new JButton("Spawn Fire");
		spawnFireButton.setActionCommand("Spawn Fire");  // Set action command for Spawn Fire button
		spawnFireButton.addActionListener(this);

		// Create a JComboBox for selecting wind direction
		String[] directionOptions = { "None", "North","North_East", "East", "South_East", "South","South_West", "West", "North_West"};
		String[] strengthOptions = { "None", "Weak", "Moderate", "Strong" };

		JComboBox<String> directionComboBox = new JComboBox<>(directionOptions);
		JComboBox<String> strengthComboBox = new JComboBox<>(strengthOptions);
		
		directionComboBox.setSelectedItem("None"); // Default selection
		strengthComboBox.setSelectedItem("None"); // Default selection

		directionComboBox.addActionListener(e -> {
			String selectedDirection = (String) directionComboBox.getSelectedItem();
			if (selectedDirection.equals("None")) {
				// If "None" is selected, set the other combo box to "None"
				grid.getWind().setDirection(null);
				strengthComboBox.setSelectedItem("None");
			} else {
				// Implement logic to set the wind direction based on the selection
				grid.getWind().setDirection(Wind.Direction.valueOf(selectedDirection.toUpperCase()));
			}
		});

		strengthComboBox.addActionListener(e -> {
			String selectedStrength = (String) strengthComboBox.getSelectedItem();
			if (selectedStrength.equals("None")) {
				// If "None" is selected, set the other combo box to "None"
				grid.getWind().setStrength(null);
				directionComboBox.setSelectedItem("None");
			} else {
				if (directionComboBox.getSelectedItem().equals("None")) {
					directionComboBox.setSelectedItem("North");
				}
				// Implement logic to set the wind strength based on the selection
				grid.getWind().setStrength(Wind.Strength.valueOf(selectedStrength.toUpperCase()));
			}
		});

		JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, DELAY);
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = speedSlider.getValue();
                timer.setDelay(value);
            }
        });

		// Create a JPanel to hold the buttons, combo boxes, and slider
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 5));  // 1 rows, 4 columns, with gaps
        controlPanel.add(toggleButton);
		controlPanel.add(spawnFireButton);
		controlPanel.add(new JLabel("Wind Direction:"));
        controlPanel.add(directionComboBox);
        controlPanel.add(new JLabel("Wind Strength:"));
        controlPanel.add(strengthComboBox);
        

        // Create another JPanel for the slider
        JPanel sliderPanel = new JPanel(new FlowLayout());  // FlowLayout centers the components horizontally
        sliderPanel.add(new JLabel("Simulation Speed:"));
        sliderPanel.add(speedSlider);

        // Create a container JPanel to hold both panels vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Stack components vertically
        mainPanel.add(controlPanel);
        mainPanel.add(sliderPanel);

        frame.add(mainPanel, BorderLayout.SOUTH);  // Add the main panel to the frame
		frame.add(this, BorderLayout.CENTER); // Add the Simple_Graphics panel to the frame

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

	/**
	 * Method to initialize and start the Timer object
	 */
	private void initTimer() {
		timer = new Timer(DELAY, this);	// create a new Timer object with delay and reference to ActionListener class
		timer.start();					// start the timer
	}

	// method to render the world in Java 2D Graphics
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	
		Cell[][] cells = grid.getPresentGrid();
		int currentCellSize = this.cellSize;
	
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				Color color = cells[i][j].getColor();
				g2d.setColor(color);
	
				// Calculate the position of the cell in the JPanel
				int x = j * currentCellSize;
				int y = i * currentCellSize;
	
				// Draw a filled rectangle representing the cell
				g2d.fillRect(x, y, currentCellSize, currentCellSize);
			}
		}
	}

	@Override // method will be called each time timer "ticks"
	public void actionPerformed(ActionEvent arg0) {
		// This method represents a time step
		String command = arg0.getActionCommand();
		if (command != null) {
			if (command.equals("Toggle")) {
				if (timer.isRunning()) {
					timer.stop();
					((JButton) arg0.getSource()).setText("Start");
				} else {
					timer.start();
					((JButton) arg0.getSource()).setText("Stop");
				}
			} else if (command.equals("Spawn Fire")) {
				grid.setSpawnFire(true);
				// Example: grid.getWind().setDirection(Wind.Direction.NORTH);
			} else {
				grid.update();
				this.repaint(); // call the repaint method, which will call paintComponent
			}
		} else {
			grid.update();
			this.repaint();					
		}

	}

	/*
	 * If you put all your drawing code in doDrawing, you don't need to worry about this method
	 */
	@Override // automatically called by "repaint"
	public void paintComponent(Graphics g){
		super.paintComponent(g);		// call the superclass method. DON'T REMOVE
		doDrawing(g);					// call the doDrawing method. THIS IS YOURS
	}
}