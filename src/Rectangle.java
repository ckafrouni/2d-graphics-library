import java.awt.*;

public class Rectangle implements Shape {
	private final double width, height;
	private final Color strokeColor;
	private final Color fillColor;
	
	public Rectangle(double width, double height, Color strokeColor, Color fillColor) {
		this.width = width;
		this.height = height;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
	}
	
	public Rectangle(double width, double height, Color color) {
		this(width, height, color, color);
	}
	
	@Override
	public void draw(Painter pt) {
		pt.setStrokeColor(strokeColor);
		pt.setFillColor(fillColor);
		pt.startFill();
		
		pt.penDown();
		for (int i = 0; i < 2; i++) {
			pt.move(width);
			pt.turn(90);
			pt.move(height);
			pt.turn(90);
		}
		pt.penUp();
		
		pt.endFill();
		pt.resetColor();
	}
}
