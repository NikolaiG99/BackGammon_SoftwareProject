package graphical_display;

/**
 * This class contains "symbolic" constants for x-y coordinates for possible positions
 * of different objects.
 */
public class BoardCoordinateConstants{
	/**
	 * An array of the x-coordinates for each point.
	 * This array stores data in elements [1] to [24],
	 * [0] is bear off, [25] is bear off, [26] is bar, [27] is bar
	 */
	public final static int [] COORDS_POINT
		= new int []{
				580, 530, 490, 445, 400, 360, 316, 240, 198, 155,115, 70, 25,
				25, 70, 115, 155, 198, 240, 316, 360, 400, 445, 490, 530, 580, 278, 278
		}; 
	
	/**
	 * An array of the y-coordinates for each row on the top of the board.
	 * This array stores data in elements [1] to [6], where 1 is the top most row
	 * [0] is bear off/bar for red
	 */
	public final static int [] COORDS_TOP_ROW
	= new int []{
			170, 25, 55, 85, 115, 145, 175
	}; 
	
	/**
	 * An array of the y-coordinates for each row on the bottom of the board.
	 * This array stores data in elements [1] to [6], where 1 is the bottom most row
	 * [0] is bear off/bar for black
	 */
	public final static int [] COORDS_BOTTOM_ROW
	= new int []{
			208, 362, 332, 302, 272, 242, 212
	}; 
	
	public final static int MIDDLE_ROW = 193;
}