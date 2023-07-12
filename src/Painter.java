import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class that provides a 2D painter functionality
 */
public class Painter extends JPanel {
	
	private double x, y;
	private double angle;
	private boolean isPenDown;
	private boolean isFilling;
	private Color color = Color.BLACK;
	private double strokeSize = 1.0;
	private int delay = 20;  // delay in milliseconds
	private int arrowSize = 20;
	
	private final Queue<Runnable> actions = new LinkedList<>();
	private final List<Point> fillPoints = new ArrayList<>();
	
	/**
	 * Initialize a new Painter object
	 *
	 * @param width  Width of the painter graphics window
	 * @param height Height of the painter graphics window
	 * @param title  Title of the painter graphics window
	 */
	public Painter(int width, int height, String title) {
		this.setPreferredSize(new Dimension(width, height));
		this.x = width / 2.0;
		this.y = height / 2.0;
		
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Set the delay between painter actions
	 *
	 * @param delay Delay in milliseconds
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * Set the size of the arrow representing the painter
	 *
	 * @param size Size of the arrow in pixels
	 */
	public void setArrowSize(int size) {
		this.arrowSize = size;
	}
	
	/**
	 * Draw an arrow representing the painter
	 */
	public void drawArrow() {
		actions.add(() -> {
			this.startFill();
			this.turn(150);
			this.move(arrowSize);
			this.turn(120);
			this.move(arrowSize);
			this.turn(120);
			this.endFill();
		});
	}
	
	/**
	 * Move the painter by a specified distance
	 *
	 * @param distance Distance to move the painter
	 */
	public void move(double distance) {
		actions.add(() -> {
			double newX = x + (distance * Math.cos(Math.toRadians(angle)));
			double newY = y - (distance * Math.sin(Math.toRadians(angle)));
			drawAndMove(newX, newY);
		});
	}
	
	/**
	 * Move the painter to a specified position
	 *
	 * @param newX X-coordinate of the new position
	 * @param newY Y-coordinate of the new position
	 */
	public void goTo(double newX, double newY) {
		actions.add(() -> drawAndMove(newX, newY));
	}
	
	private void drawAndMove(double newX, double newY) {
		if (isPenDown) {
			Graphics2D g = (Graphics2D) this.getGraphics();
			g.setColor(this.color);
			g.setStroke(new BasicStroke((float) this.strokeSize));
			g.drawLine((int) Math.round(x), (int) Math.round(y), (int) Math.round(newX), (int) Math.round(newY));
			g.dispose();
		}
		if (isFilling) {
			fillPoints.add(new Point((int) Math.round(newX), (int) Math.round(newY)));
		}
		this.x = newX;
		this.y = newY;
	}
	
	/**
	 * Set the X-coordinate of the painter
	 *
	 * @param newX New X-coordinate
	 */
	public void setX(double newX) {
		actions.add(() -> drawAndMove(newX, y));
	}
	
	/**
	 * Set the Y-coordinate of the painter
	 *
	 * @param newY New Y-coordinate
	 */
	public void setY(double newY) {
		actions.add(() -> drawAndMove(x, newY));
	}
	
	/**
	 * Turn the painter counter-clockwise by a specified angle
	 *
	 * @param angle Angle to turn the painter
	 */
	public void turn(double angle) {
		actions.add(() -> this.angle += angle);
	}
	
	/**
	 * Turn the painter left by 90 degrees
	 */
	public void turnLeft() {
		actions.add(() -> this.angle += 90);
	}
	
	
	/**
	 * Turn the painter right by 90 degrees
	 */
	public void turnRight() {
		actions.add(() -> this.angle -= 90);
	}
	
	/**
	 * Turn the painter around by 180 degrees
	 */
	public void turnAround() {
		actions.add(() -> this.angle += 180);
	}
	
	/**
	 * Set the painter's angle
	 *
	 * @param angle New angle for the painter
	 */
	public void setAngle(double angle) {
		actions.add(() -> this.angle = angle);
	}
	
	
	/**
	 * Lift the pen (stop drawing)
	 */
	public void penUp() {
		actions.add(() -> this.isPenDown = false);
	}
	
	/**
	 * Lower the pen (start drawing)
	 */
	public void penDown() {
		actions.add(() -> this.isPenDown = true);
	}
	
	/**
	 * Set the pen's color
	 *
	 * @param color New color for the pen
	 */
	public void setColor(Color color) {
		actions.add(() -> this.color = color);
	}
	
	/**
	 * Reset the pen's color back to black
	 * (same as setColor(Color.BLACK))
	 */
	public void resetColor() {
		actions.add(() -> this.color = Color.BLACK);
	}
	
	/**
	 * Set the pen's size
	 *
	 * @param size New size for the pen
	 */
	public void setStrokeSize(double size) {
		actions.add(() -> this.strokeSize = size);
	}
	
	/**
	 * Start filling shapes
	 */
	public void startFill() {
		actions.add(() -> {
			this.isFilling = true;
			this.fillPoints.clear();
			this.fillPoints.add(new Point((int) Math.round(x), (int) Math.round(y)));
		});
	}
	
	/**
	 * Stop filling shapes and fill the last shape
	 */
	public void endFill() {
		actions.add(() -> {
			this.isFilling = false;
			Graphics g = this.getGraphics();
			g.setColor(this.color);
			if (!fillPoints.isEmpty()) {
				int[] xPoints = new int[fillPoints.size()];
				int[] yPoints = new int[fillPoints.size()];
				for (int i = 0; i < fillPoints.size(); i++) {
					xPoints[i] = fillPoints.get(i).x;
					yPoints[i] = fillPoints.get(i).y;
				}
				g.fillPolygon(xPoints, yPoints, fillPoints.size());
			}
			g.dispose();
		});
	}
	
	/**
	 * Execute all the painter's pending actions
	 */
	public void execute() {
		SwingUtilities.invokeLater(() -> {
			Timer timer = new Timer(delay, e -> {
				if (!actions.isEmpty()) {
					actions.poll().run();
				}
			});
			timer.start();
		});
	}
	
	/**
	 * Clear all the painter's graphics
	 */
	public void clear() {
		actions.add(() -> {
			Graphics g = this.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.dispose();
		});
	}
	
	/**
	 * Draw a shape
	 *
	 * @param shape Shape to draw
	 */
	public void draw(Shape shape) {
		shape.draw(this);
	}
	
	
	/**
	 * Main method to test the TurtleGraphics class
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		Painter pt = new Painter(800, 600, "painter Graphics");
		
		// Example usage
		pt.setColor(Color.RED);
		pt.startFill();
		pt.move(100);
		pt.turn(90);
		pt.move(100);
		pt.turn(90);
		pt.move(100);
		pt.turn(90);
		pt.move(100);
		pt.endFill();
		
		pt.move(100);
		pt.setColor(Color.BLACK);
		pt.penDown();
		pt.move(100);
		pt.turn(120);
		pt.move(100);
		pt.turn(120);
		pt.move(100);
		
		pt.setStrokeSize(6);
		pt.setColor(Color.BLUE);
		pt.turn(60);
		pt.move(100);
		pt.turn(120);
		pt.move(100);
		pt.turn(120);
		pt.move(100);
		
		pt.setColor(Color.GREEN);
		pt.goTo(20, 20);
		pt.setColor(Color.YELLOW);
		pt.setY(200);
		pt.setColor(Color.ORANGE);
		pt.move(100);
		
		pt.execute();
	}
}
