import java.awt.*;

public class Square implements Shape {
	private final Rectangle square;
	
	public Square(double side) {
		this(side, Color.BLACK);
	}
	
	public Square(double side, Color color) {
		this.square = new Rectangle(side, side, color);
	}
	
	@Override
	public void draw(Painter pt) {
		square.draw(pt);
	}
}
