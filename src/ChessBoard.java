import java.awt.*;

public class ChessBoard implements Shape {
	private final double step;
	private final Square whiteSquare;
	private final Square blackSquare;
	
	/**
	 * Initialize a new black and white ChessBoard object
	 *
	 * @param length Length of the chess board
	 */
	public ChessBoard(int length) {
		this(length, Color.WHITE, Color.BLACK);
	}
	
	/**
	 * Initialize a new ChessBoard object
	 *
	 * @param length Length of the chess board
	 * @param light  Color of the light squares
	 * @param dark   Color of the dark squares
	 */
	public ChessBoard(double length, Color light, Color dark) {
		this.step = length / 8;
		this.whiteSquare = new Square(step, light);
		this.blackSquare = new Square(step, dark);
	}
	
	/**
	 * Draw a line of squares
	 *
	 * @param pt      Painter to draw with
	 * @param isBlack Whether the first square is black
	 * @return Whether the last square was black
	 */
	private boolean drawLine(Painter pt, boolean isBlack) {
		for (int j = 0; j < 8; j++) {
			
			if (isBlack) blackSquare.draw(pt);
			else whiteSquare.draw(pt);
			
			isBlack = !isBlack;
			
			pt.move(step);
		}
		return !isBlack;
	}
	
	/**
	 * Move the painter to the next line
	 *
	 * @param pt Painter to move
	 */
	private void nextLine(Painter pt) {
		pt.turnLeft();
		pt.move(step);
		
		pt.turnLeft();
		pt.move(8 * step);
		pt.turnAround();
	}
	
	
	@Override
	public void draw(Painter pt) {
		boolean isBlack = true;
		for (int i = 0; i < 8; i++) {
			isBlack = this.drawLine(pt, isBlack);
			this.nextLine(pt);
		}
	}
	
	public static void main(String[] args) {
		int windowHeight = 800;
		int windowWidth = 1600;
		
		Painter pt = new Painter(windowWidth, windowHeight, "Paul's Chess Board");
		pt.setDelay(0);
		pt.setStrokeSize(5);
		
		pt.goTo(0, 800);
		
		ChessBoard chessBoard1 = new ChessBoard(windowHeight);
		ChessBoard chessBoard2 = new ChessBoard(500, Color.DARK_GRAY, Color.ORANGE);
		ChessBoard chessBoard3 = new ChessBoard(300, Color.LIGHT_GRAY, Color.white);
		
		pt.draw(chessBoard1);
		
		pt.move(windowHeight);
		pt.turnRight();
		pt.move(windowHeight);
		pt.turnLeft();
		
		pt.draw(chessBoard2);
		pt.draw(chessBoard3);
		
		pt.execute();
	}
}

