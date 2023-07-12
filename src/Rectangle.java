import java.awt.*;

public class Rectangle implements Shape {
	private final double width, height;
	private final Color fillColor;
	
	public Rectangle(double width, double height, Color fillColor) {
		this.width = width;
		this.height = height;
		this.fillColor = fillColor;
	}
	
	@Override
	public void draw(Painter pt) {
		pt.setColor(fillColor);
		pt.startFill();
		
		for (int i = 0; i < 2; i++) {
			pt.move(width);
			pt.turn(90);
			pt.move(height);
			pt.turn(90);
		}
		
		pt.endFill();
		pt.resetColor();
	}
}
