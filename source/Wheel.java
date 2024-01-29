import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class Wheel extends JPanel {
	
	public static enum Shape {
		CIRCLE,
		UMBRELLA
	}
	
	Image _image = null;
	private boolean hasBorders = false;
	private double _delta;
	private Point2D _imagePosition;
	private Point2D _rotationCenter;
	private double _rotationAngle = 0;
	private double _zoomFactor = 1;
	
	private ArrayList<Color> _colors;
	int _colorCounter = 0;
	
	private Shape _shape = Shape.CIRCLE;
	private final int BORDER = 10;
	private int _radius;
	private Point2D _center = new Point2D.Double();
	
	ArrayList<String> _stringList;
	private int _noElem;
	private final int LIMIT = 100;
	private final int MAXFONTSIZE = 80, MINFONTSIZE = 10;
	private final Font DEFAULTFONT = new Font("TimesRoman", Font.PLAIN, 12);
	private Font _font = DEFAULTFONT;

	private boolean _spinOnOff = false;
	private double _spinSpeed = 0;
	private double _maxSpinSpeed = 8000;
	private double _spinDeceleration = -5000;
	private Timer _speedTimer;
	private long _timeStart, _timeEnd;
	private double _rotationAngleStart, _rotationAngleEnd;
	private int _refreshRate = 100;
	private Point2D _mouseDragPosition;

	@Override
	public void setBounds(int x, int y, int width, int height) {
		_image = null;
		super.setBounds(x, y, width, height);
	}
	
	public void hasBorders(boolean borders) {
		/*
		 * Borders on/off.
		 * If switched on, borders of sections and circle + circle center will be visible.
		 */
		hasBorders = borders;
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public void setShape(Shape shape) {
		/*
		 * Set the shape of the wheel.
		 * Options in Shape enum.
		 */
		_shape = shape;
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public double getRotationAngle() {
		/*
		 * Get current rotation of the wheel.
		 */
		return _rotationAngle;
	}
	
	public void setRotationAngle(double rotationAngle) {
		/*
		 * Set the current rotation of the wheel.
		 */
		_rotationAngle = rotationAngle % 360;
		this.repaint();
	}
	
	public ArrayList<Color> getColorScheme() {
		/*
		 * Get ArrayList of colors used for sections of the wheel.
		 */
		return _colors;
	}
	
	public void setColorScheme(ArrayList<Color> colors) {
		/*
		 * Set ArrayList of colors used for sections of the wheel.
		 */
		_colors = colors;
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public void addColor(Color color) {
		/*
		 * Add a new color to the existing color scheme for the sections of the wheel.
		 */
		if(_colors == null)
			_colors = new ArrayList<Color>();
		_colors.add(color);
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public int getRadius() {
		/*
		 * Get radius of the wheel.
		 * The radius is set in DrawImage method based on the dimensions of this.
		 */
		return _radius;
	}
	
	public ArrayList<String> getListOfStrings() {
		/*
		 * Get list of strings displayed inside the sections of the wheel.
		 */
		return _stringList;
	}
	
	public void setListOfStrings(ArrayList<String> list) throws Exception {
		/*
		 * Set list of strings displayed inside the sections of the wheel.
		 * The initial list is set in constructor method and can be changed during runtime.
		 */
		_noElem = list.size();
		if(_noElem > LIMIT)
			throw new Exception("String list is larger then limit (" + LIMIT + ")");
		_delta = (double)360 / (double)_noElem;
		_stringList = list;
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	@Override
	public Font getFont() {
		/*
		 * Get current font of the displayed strings in the wheel.
		 */
		return _font;
	}
	
	@Override
	public void setFont(Font font) {
		/*
		 * Set current font of the displayed strings in the wheel.
		 */
		super.setFont(font);
		_font = font;
		_image = null;
		_spinOnOff = false;
		setRotationAngle(0);
		this.repaint();
	}
	
	public double getSpinSpeed() {
		/*
		 * Get current spinning speed.
		 * If the spinning is off, it returns 0.
		 */
		return _spinOnOff ? _spinSpeed : 0;
	}
	
	public double getMaxSpinSpeed() {
		/*
		 * Get current speed limit.
		 */
		return _maxSpinSpeed;
	}
	
	public void setMaxSpinSpeed(double speed) {
		/*
		 * Set current speed limit.
		 */
		_spinOnOff = false;
		_maxSpinSpeed = speed;
	}
	
	public double getSpinDeceleration() {
		return _spinDeceleration;
	}
	
	public void setSpinDeceleration(double deceleration) throws Exception {
		if(deceleration > 0)
			throw new Exception("Illegal parameter value: acceleration must be < 0");
		_spinDeceleration = deceleration;
	}
	
	public boolean isSpinning() {
		/*
		 * Check if the wheel is spinning.
		 */
		return _spinOnOff;
	}
	
	public String getSelectedString() {
		/*
		 * Get current selection.
		 * Returns the string which is displayed in the section of the wheel that is currently positioned between 0 and delta degrees.
		 * The idea is to get the number of deltas in current rotationAngle.
		 * This number is added to the size of the string arraylist, and then MODed by the size of the string arraylist,
		 * in order to avoid negative indices.
		 */
		return _stringList.get((int)Math.floor(_noElem + (_rotationAngle % 360) / _delta) % _noElem);
	}
	
	public Wheel(ArrayList<String> listOfStrings) throws Exception {
		/*
		 * Constructor of the class.
		 * Sets the string arraylist, adds mouse listeners and stast TimerTask to measure the rotation speed.
		 */
		setListOfStrings(listOfStrings);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				_mouseDragPosition = new Point2D.Double(500,500);
				// to stop the spinning if the circle is clicked on
				double distance = Math.sqrt(Math.pow(10,2) + Math.pow(10,2));
				if(distance <= _radius)
				{
					spinStop();
				}
				// to measure initial speed
				_timeStart = System.currentTimeMillis();
				_rotationAngleStart = 50;
			}
			@Override
			public void mouseReleased(MouseEvent e) {

				// to measure initial speed
				_timeEnd = System.currentTimeMillis();
				_rotationAngleEnd = _rotationAngle;
				double initialSpeed = 8000;
				initialSpeed = (int)Math.signum(initialSpeed) * Math.min(Math.abs(initialSpeed), _maxSpinSpeed);
				try {
					spinStartAsync(Math.abs(initialSpeed), (int)Math.signum(initialSpeed), _spinDeceleration);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});


		
		// start TimerTask to measure current speed
		TimerTask timerTask = new speedTimerTask();
		_speedTimer = new Timer(true);
		_speedTimer.schedule(timerTask, 0);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		/*
		 * Paintcomponent - if the image is null, create it and then draw it whilst keeping the current rotation.
		 * The image can be larger than the displaying area, so after it is drawn it needs to be placed properly.
		 */
		super.paintComponent(g);
		
		if(_image == null) {
			_image = drawImage();
			_rotationCenter = new Point2D.Double(
					this.getWidth() - _image.getWidth(null) + _center.getX(),
					this.getHeight() / 2
				);
			_imagePosition = new Point2D.Double(
						(int)(this.getWidth() - _image.getWidth(null)),
						(int)(this.getHeight() / 2 - _center.getY())
					);
		}
		
		Graphics2D gPanel = (Graphics2D) g;
		gPanel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gPanel.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		gPanel.rotate(Math.toRadians(_rotationAngle), _rotationCenter.getX(), _rotationCenter.getY());
		gPanel.drawImage(_image, (int)_imagePosition.getX(), (int)_imagePosition.getY(), null);
	}
	
	private BufferedImage drawImage()
	{
		/*
		 * Calculate all the necessary parameters for the wheel and draw it section by section.
		 */
		int width = this.getWidth(), height = this.getHeight();
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();

		// Calculate radius
		_radius = Math.min(img.getWidth(), img.getHeight()) / 2 - BORDER;
		
		double stringDistanceFromEdge = 0.05 * _radius;
		int fontSize, stringWidth, maxStringWidth;
		
		maxStringWidth = (int)(_radius - 2 * stringDistanceFromEdge);
		fontSize = calcFontSize(g2d, stringDistanceFromEdge, maxStringWidth);
		g2d.setFont(new Font(_font.getFamily(), _font.getStyle(), fontSize));
		
		// Adjust the parameters (for "zoom in") - if the font size is too small
		if(fontSize < MINFONTSIZE) {
			_zoomFactor = (double)MINFONTSIZE / fontSize;
			width += (int) 2 * ((_zoomFactor * _radius) - _radius);
			height += (int) 2 * ((_zoomFactor * _radius) - _radius);
			_radius = (int)(_zoomFactor * _radius);
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = (Graphics2D) img.getGraphics();
			maxStringWidth = (int)(_radius - 2 * stringDistanceFromEdge);
			fontSize = calcFontSize(g2d, stringDistanceFromEdge, maxStringWidth);
		}
		
		// Calculate center point
		_center = new Point2D.Double((double)img.getWidth() / 2, (double)img.getHeight() / 2);
		
		// Set rendering hints
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.rotate(Math.toRadians(_rotationAngle),_center.getX(), _center.getY());
		
		// Draw center point
		if(hasBorders) {
			g2d.setColor(Color.BLACK);
			g2d.fillArc((int)_center.getX() - (int)Math.floor(Math.max(0.01 * _radius, 1)), (int)_center.getY() - (int)Math.floor(Math.max(0.01 * _radius, 1)), (int)Math.floor(Math.max(0.01 * 2 * _radius, 2)), (int)Math.floor(Math.max(0.01 * 2 * _radius, 2)), 0, 360);
		}
		
		// Divide circle and draw strings
		FontMetrics fontMetrics;
		if(_colors == null)
			_colors = getDefaultColorList();
		_colorCounter = 0;
		for(int i = _noElem - 1; i >= 0; i--)
		{
			// Draw section border
			if(hasBorders) {
				g2d.setColor(Color.BLACK);
				g2d.drawLine((int)_center.getX(), (int)_center.getY(), (int)_center.getX() + _radius, (int)_center.getY());
			}
			// Fill section depending on the chosen shape
			g2d.setColor(_colors.get(_colorCounter++ % _colors.size()));
			if(_shape == Shape.UMBRELLA)
				fillTriangle(g2d);
			else //if(_shape == Shape.CIRCLE)
				fillArc(g2d);
			// Draw string - rotate half delta, then draw then rotate the other half (to have the string in the middle)
			g2d.rotate(Math.toRadians(_delta / 2), _center.getX(), _center.getY());
			g2d.setColor(Color.BLACK);
			fontMetrics = g2d.getFontMetrics();
			stringWidth = fontMetrics.stringWidth(_stringList.get(i));
			g2d.drawString(_stringList.get(i), (int)(_center.getX() + maxStringWidth - stringWidth + stringDistanceFromEdge), (int)(_center.getY() + (double)fontMetrics.getHeight() / 2 - fontMetrics.getMaxDescent()));
			g2d.rotate(Math.toRadians(_delta / 2), _center.getX(), _center.getY());
		}
		
		return img;
	}
	
	private int calcFontSize(Graphics g, double stringDistanceFromEdge, int maxStringWidth) {
		/*
		 * Calculates the optimal font size for the strings inside the sections.
		 * The strings need to be positioned next to the broader end of the section.
		 * The optimal size will depend on the longest string length and maximum height of the section
		 * in the left border of the rectangle surrounding the string.
		 */
		
		// Find the longest string
		String tmpString = "";
		for(int i = _noElem - 1; i >= 0; i--) {
			if(_stringList.get(i).length() > tmpString.length())
				tmpString = _stringList.get(i);
		}
		
		// Set it to max font size and calculate rectangle
		int fontSize = MAXFONTSIZE;
		g.setFont(new Font(_font.getFamily(), _font.getStyle(), fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();
		Rectangle2D stringBounds = fontMetrics.getStringBounds(tmpString, g);
		
		// Adjust string height / font size
		int maxHeight = (int)Math.floor(2 * stringDistanceFromEdge * Math.sin(Math.toRadians(_delta / 2)));
		if(stringBounds.getHeight() > maxHeight) {
			fontSize = (int)Math.floor(fontSize * maxHeight / stringBounds.getHeight());
			g.setFont(new Font(_font.getFamily(), _font.getStyle(), fontSize));
			fontMetrics = g.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		
		// Adjust string width
		// If the string is too narrow, increase font until it fits
		double K = stringBounds.getWidth() / stringBounds.getHeight();
		maxHeight = (int)Math.floor(2 * (_radius - stringDistanceFromEdge) * Math.tan(Math.toRadians(_delta / 2)) / (1 + 2 * K * Math.tan(Math.toRadians(_delta / 2))));
		while(stringBounds.getWidth() < maxStringWidth) {
				g.setFont(new Font(_font.getFamily(), _font.getStyle(), ++fontSize));
				fontMetrics = g.getFontMetrics();
				stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		// If the string is too wide, decrease font until it fits
		while(stringBounds.getWidth() > maxStringWidth) {
			g.setFont(new Font(_font.getFamily(), _font.getStyle(), --fontSize));
			fontMetrics = g.getFontMetrics();
			stringBounds = fontMetrics.getStringBounds(tmpString, g);
		}
		
		return Math.min(fontSize, MAXFONTSIZE);
	}
	
	private void fillArc(Graphics g2d) {
		g2d.fillArc((int)_center.getX() - _radius, (int)_center.getY() - _radius, 2 * _radius, 2 * _radius, 0, (int)- Math.ceil(_delta)); // use ceil because of decimal part (would be left empty)
		if(hasBorders) {
			g2d.setColor(Color.black);
			g2d.drawArc((int)_center.getX() - _radius, (int)_center.getY() - _radius, 2 * _radius, 2 * _radius, 0, (int)- Math.ceil(_delta));
		}
	}
	
	private void fillTriangle(Graphics2D g2d) {
		/*
		 * Method that draws section as a triangle (in case Shape=UMBRELLA was chosen)
		 */
		int[] xpoints = new int[3];
		xpoints[0] = (int)_center.getX();
		xpoints[1] = (int)_center.getX() + _radius;
		int dx = (int) (2 * _radius * Math.pow(Math.sin(Math.toRadians(_delta / 2)), 2));
		xpoints[2] = xpoints[1] - dx;
		int[] ypoints = new int[3];
		ypoints[0] = (int)_center.getY();
		ypoints[1] = (int)_center.getY();
		int dy = (int) (2 * _radius * Math.sin(Math.toRadians(_delta / 2)) * Math.cos(Math.toRadians(_delta / 2)));
		ypoints[2] = ypoints[1] + dy;
		g2d.fillPolygon(xpoints, ypoints, 3);
		if(hasBorders) {
			g2d.setColor(Color.black);
			g2d.drawLine(xpoints[1], ypoints[1], xpoints[2], ypoints[2]);
		}
	}
	
	private class SpinRunnable implements Runnable {
		/*
		 * Runnable class that handles the spinning of the wheel.
		 * It sets the rotation angle by calculating the speed through time based on deceleration.
		 * Each setRotationAngle call will cause the wheel to be redrawn.
		 */
		private double spinSpeed;
		private int spinDirection;
		private double spinDeceleration;

	    public SpinRunnable(double speed, int direction, double deceleration) {
	        this.spinSpeed = speed;
	        this.spinDirection = direction;
	        this.spinDeceleration = deceleration;
	    }

	    public void run()
		{
	    	_spinOnOff = true;
			int sleepTime = 1000 / _refreshRate;
			double delta;
			while(_spinOnOff && spinSpeed > 0)
			{
				delta = spinDirection * (spinSpeed / _refreshRate);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setRotationAngle(getRotationAngle() + delta);
				spinSpeed += spinDeceleration / _refreshRate;
			}
			_spinOnOff = false;
		}
	}
	
	public void spinStartAsync(double speed, int direction, double deceleration) throws Exception
	{
		/*
		 * Method that starts the spinning thread.
		 * Parameters:
		 * speed => degrees per second
		 * direction => "< 0" = clockwise , "> 0" = counter-clockwise, "=0" = stand still
		 * deceleration => "< 0" = degrees per second per second reducing speed, "= 0" = perpetual spin, "> 0" = throw exception
		 */
		
		if(deceleration > 0)
			throw new Exception("Illegal parameter value: acceleration must be < 0");
		SpinRunnable spinRunnable = new SpinRunnable(speed, direction, deceleration);
		Thread t = new Thread(spinRunnable);
		t.start();
	}
	
	public void spinStop()
	{
		/*
		 * Sets the flag to stop the spinnning.
		 */
		_spinOnOff = false;
	}
	
	private class speedTimerTask extends TimerTask {
		/*
		 * TimerTask class that monitors and refreshes the _spinSpeed
		 * The speed is calculated as a difference of two rotation angles over a period of time.
		 * We add the 360 to the "now" angle and then MOD it by 360 to avoid miscalculation when passing the full circle.
		 */
		@Override
		public void run() {
			double prevAngle, nowAngle;
			long sleepTime = 100;
			while(true) {
				prevAngle = getRotationAngle();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				nowAngle = getRotationAngle();
				nowAngle = (nowAngle + Math.signum(nowAngle) * 360) % 360;
				_spinSpeed = Math.abs(nowAngle - prevAngle) * (1000 / sleepTime);
			}
		}
	}
	
	private ArrayList<Color> getDefaultColorList() {
		/*
		 * Returns default color list.
		 * To be used in case when no explicit color list is set.
		 */
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.BLUE);
		colors.add(Color.CYAN);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.GRAY);
		colors.add(Color.GREEN);
		colors.add(Color.LIGHT_GRAY);
		colors.add(Color.MAGENTA);
		colors.add(Color.ORANGE);
		colors.add(Color.PINK);
		colors.add(Color.RED);
		colors.add(Color.WHITE);
		colors.add(Color.YELLOW);
		return colors;
	}
}
