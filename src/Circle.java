public class Circle implements Shape {
	private final double radius;
	
	
	public Circle(double radius) {
		this.radius = radius;
	}
	
	@Override
	public void draw(Painter pt) {
		pt.penUp();
		pt.move(radius);
		pt.turn(90);
		
		pt.penDown();
		pt.startFill();
		double length = 2 * Math.PI * radius / 30;
		for (int i = 0; i < 30; i++) {
			pt.move(Math.round(length));
			pt.turn(12);
		}
		pt.endFill();
		pt.penUp();
		
		pt.turn(90);
		pt.move(radius);
		pt.turn(180);
	}
}
