package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw16.jvdraw.models.JColorArea;

/**
 * Implementacija kruga sa svim potrebnim metodama, implementira sučelje {@link GeometricalObject}.
 * 
 * @author Kristijan Fugosic
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * X Kordinata središta kružnice
	 */
	private int centerX;
	/**
	 * Y Kordinata središta kružnice
	 */
	private int centerY;
	/**
	 * Radijus kružnice
	 */
	private int radius;
	/**
	 * ID Kružnice
	 */
	private int id;
	/**
	 * Primarna boja (obrub)
	 */
	private Color fgColor;
	/**
	 * Sekundarna boja (Boja ispune ako je tip kruznjice filled = true)
	 */
	private Color bgColor;
	/**
	 * True ako je riječ o krugu, false ako je riječ o kružnici bez ispune
	 */
	private boolean filled;
	/**
	 * Brojac kruznica
	 */
	private static int counter = 1;
	/**
	 * Brojac krugova
	 */
	private static int counterFilled = 1;

	/**
	 * Konstruktor koji se koristi samo u iznimnim slučajevima (pristup brojacu).
	 * Inače ne koristiti!!!
	 */
	public Circle() {
	}
	
	/**
	 * Konstruktor koji se koristi samo u iznimnim slučajevima (prikaz privremenog geom. lika).
	 * Inače ne koristiti!!!
	 * 
	 * @param fgColor Boja obruba/kruznice
	 * @param bgColor Boja ispune kruga 
	 * @param filled  True ako stvaramo krug, false ako zelimo samo kruznicu
	 */
	public Circle(Color fgColor, Color bgColor, boolean filled) {
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.filled = filled;
	}

	/**
	 * Konstruktor za kreiranje kruznice ili kruga, ovisno o parametru filled.
	 * 
	 * @param center Kordinate središta 
	 * @param radius Radijus
	 * @param fgColor Boja obruba/kruznice
	 * @param bgColor Boja ispune kruga 
	 * @param filled  True ako stvaramo krug, false ako zelimo samo kruznicu
	 */
	public Circle(Point center, int radius, Color fgColor, Color bgColor, boolean filled) {
		centerX = (int) center.getX();
		centerY = (int) center.getY();
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.filled = filled;
		if(filled){
			id = counterFilled++;
		} else {
			id = counter++;
		}	
	}
	
	/**
	 * Konstruktor za kreiranje kruznice ili kruga, ovisno o parametru filled.
	 * 
	 * @param firstPos Kordinate prvog klika 
	 * @param secondPos Kordinate drugog klika
	 * @param fgColor Boja obruba/kruznice
	 * @param bgColor Boja ispune kruga 
	 * @param filled  True ako stvaramo krug, false ako zelimo samo kruznicu
	 */
	public Circle(Point firstPos, Point secondPos, Color fgColor, Color bgColor, boolean filled) {
		centerX = (int) firstPos.getX();
		centerY = (int) firstPos.getY();
		radius = (int) Point.distance(firstPos.getX(), firstPos.getY(), secondPos.getX(), secondPos.getY());
		this.fgColor = fgColor;
		this.bgColor = bgColor;
		this.filled = filled;
		if(filled){
			id = counterFilled++;
		} else {
			id = counter++;
		}	
	}

	@Override
	public void drawObject(Graphics g, int shiftX, int shiftY) {
		g.setColor(fgColor);
		int shiftedCenterX = centerX - shiftX;
		int shiftedCenterY = centerY - shiftY;
		if (filled) {
			g.fillOval(shiftedCenterX - radius - 1, shiftedCenterY - radius - 1, radius * 2 + 2, radius * 2 + 2);
			g.setColor(bgColor);
			g.fillOval(shiftedCenterX - radius, shiftedCenterY - radius, radius * 2, radius * 2);
		} else {
			g.drawOval(shiftedCenterX - radius, shiftedCenterY - radius, radius * 2, radius * 2);
		}

	}

	@Override
	public void preview(Point firstPos, Point secondPos, Graphics g) {
		int cX = (int) firstPos.getX();
		int cY = (int) firstPos.getY();
		int r = (int) Point.distance(firstPos.getX(), firstPos.getY(), secondPos.getX(), secondPos.getY());

		g.setColor(fgColor);
		if (filled) {
			g.fillOval(cX - r, cY - r, r * 2, r * 2);
			g.setColor(bgColor);
			g.fillOval(cX - r + 1, cY - r + 1, r * 2 - 2, r * 2 - 2);
		} else {
			g.drawOval(cX - r, cY - r, r * 2, r * 2);
		}

	}

	@Override
	public boolean edit() {
		JTextField centerX = new JTextField(String.valueOf(this.centerX));
		JTextField centerY = new JTextField(String.valueOf(this.centerY));
		JTextField newRadius = new JTextField(String.valueOf(this.radius));
		JColorArea fgColorArea = new JColorArea(fgColor);
		JColorArea bgColorArea = new JColorArea(bgColor);
		
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Center - X coordinate:"));
		panel.add(centerX);
		panel.add(new JLabel("Center - Y coordinate:"));
		panel.add(centerY);
		panel.add(new JLabel("Radius :"));
		panel.add(newRadius);
		panel.add(new JLabel("Foreground color:"));
		panel.add(fgColorArea);
		if(filled){
			panel.add(new JLabel("Background color:"));
			panel.add(bgColorArea);
		}

		int result = JOptionPane.showConfirmDialog(null, panel, "Please enter new values for this line.",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int cX = 0;
			int cY = 0;
			int r = 0;
			Color newFgColor = null;
			Color newBgColor = null;
			try {
				cX = Integer.parseInt(centerX.getText());
				cY = Integer.parseInt(centerY.getText());
				r = Integer.parseInt(newRadius.getText());
				newFgColor = fgColorArea.getCurrentColor();
				newBgColor = bgColorArea.getCurrentColor();
			} catch (Exception e) {
				return edit();
			}
			this.centerX = cX;
			this.centerY = cY;
			this.radius = r;
			this.fgColor = newFgColor;
			this.bgColor = newBgColor;
			return true;
		}
		return false;
	}

	@Override
	public String getSavingFormat() {
		String string = null;
		if(filled){
			string = String.format("FCIRCLE %d %d %d %d %d %d %d %d %d \r\n", 
					centerX, centerY, radius, 
					fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
					bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());		
		} else {
			string = String.format("CIRCLE %d %d %d %d %d %d \r\n", 
					centerX, centerY, radius, 
					fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());			
		}
		return string;
	}
	

	@Override
	public String toString() {
		if (filled) {
			return "Filled circle " + id;
		}
		return "Circle " + id;
	}
	
	@Override
	public void resetCounter() {
		counter = 1;
	}

	@Override
	public int getMinX() {
		return centerX - radius;
	}

	@Override
	public int getMaxX() {
		return centerX + radius;
	}

	@Override
	public int getMinY() {
		return centerY - radius;
	}

	@Override
	public int getMaxY() {
		return centerY + radius;
	}
	
}
