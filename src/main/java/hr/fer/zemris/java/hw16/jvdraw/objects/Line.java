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
 * Implementacija linije sa svim potrebnim metodama, implementira sučelje {@link GeometricalObject}.
 * 
 * @author Kristijan Fugosic
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Pocetna X kordinata
	 */
	private int startX;
	/**
	 * Pocetna Y kordinata
	 */
	private int startY;
	/**
	 * Krajnja X kordinata
	 */
	private int endX;
	/**
	 * Krajnja Y kordinata
	 */
	private int endY;
	/**
	 * ID linije
	 */
	private int id;
	/**
	 * Boja linije
	 */
	private Color color;
	/**
	 * Brojac koliko je linija kreirano
	 */
	private static int counter = 1;

	/**
	 * Konstruktor koji se koristi samo u iznimnim slučajevima (pristup brojacu).
	 * Inače ne koristiti!!!
	 */
	public Line() {
	}
	
	/**
	 * Konstruktor koji se koristi samo u iznimnim slučajevima (prikaz privremenog geom. lika).
	 * Inače ne koristiti!!!
	 * @param foregroundColor Boja linije
	 */
	public Line(Color foregroundColor) {
		color = foregroundColor;
	}

	/**
	 * Konstruktor za liniju.
	 * @param startingPos Početne kordinate
	 * @param endingPos Krajnje kordinate
	 * @param foregroundColor Boja linije
	 */
	public Line(Point startingPos, Point endingPos, Color foregroundColor) {
		startX = (int) startingPos.getX();
		startY = (int) startingPos.getY();
		endX = (int) endingPos.getX();
		endY = (int) endingPos.getY();
		color = foregroundColor;
		id = counter++;
	}

	@Override
	public void drawObject(Graphics g, int shiftX, int shiftY) {
		g.setColor(color);
		g.drawLine(startX-shiftX, startY-shiftY, endX-shiftX, endY-shiftY);
	}

	@Override
	public void preview(Point firstPos, Point secondPos, Graphics g) {
		g.setColor(color);
		g.drawLine((int) firstPos.getX(), (int) firstPos.getY(), (int) secondPos.getX(), (int) secondPos.getY());
	}

	@Override
	public boolean edit() {
		JTextField startX = new JTextField(String.valueOf(this.startX));
		JTextField startY = new JTextField(String.valueOf(this.startY));
		JTextField endX = new JTextField(String.valueOf(this.endX));
		JTextField endY = new JTextField(String.valueOf(this.endY));
		JColorArea colorArea = new JColorArea(color);
		
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(new JLabel("Starting point - X coordinate:"));
		panel.add(startX);
		panel.add(new JLabel("Starting point - Y coordinate:"));
		panel.add(startY);
		panel.add(new JLabel("Ending point - X coordinate:"));
		panel.add(endX);
		panel.add(new JLabel("Ending point - Y coordinate:"));
		panel.add(endY);
		panel.add(new JLabel("Color:"));
		panel.add(colorArea);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Please enter new values for this line.",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int sX = 0;
			int sY = 0;
			int eX = 0;
			int eY = 0;
			Color newColor = null;
			try {
				sX = Integer.parseInt(startX.getText());
				sY = Integer.parseInt(startY.getText());
				eX = Integer.parseInt(endX.getText());
				eY = Integer.parseInt(endY.getText());
				newColor = colorArea.getCurrentColor();
			} catch (Exception e) {
				return edit();
			}
			this.startX = sX;
			this.startY = sY;
			this.endX = eX;
			this.endY = eY;
			this.color = newColor;
			return true;
		}
		return false;
	}

	@Override
	public String getSavingFormat() {
		String string = String.format("LINE %d %d %d %d %d %d %d \r\n", 
				startX, startY, endX, endY, color.getRed(), color.getGreen(), color.getBlue());
		return string;
	}
	

	@Override
	public String toString() {
		return "Line " + id;
	}
	
	@Override
	public void resetCounter() {
		counter = 1;
	}

	@Override
	public int getMinX() {
		if(startX < endX){
			return startX;
		} else {
			return endX;
		}
	}

	@Override
	public int getMaxX() {
		if(startX > endX){
			return startX;
		} else {
			return endX;
		}
	}

	@Override
	public int getMinY() {
		if(startY < endY){
			return startY;
		} else {
			return endY;
		}
	}

	@Override
	public int getMaxY() {
		if(startY > endY){
			return startY;
		} else {
			return endY;
		}
	}
}
