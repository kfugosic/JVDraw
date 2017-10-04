package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * Label koji se nalazi na dnu prozora programa i sadrži informacije o trenutno
 * odabranoj primarnoj i sekundarnoj boji.
 * 
 * @author Kristijan Fugosic
 *
 */
public class BottomLabel extends JLabel implements ColorChangeListener {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * JColorArea za primarnu boju
	 */
	private JColorArea fgColorArea;
	/**
	 * JColorArea za sekundarnu boju
	 */
	private JColorArea bgColorArea;
	/**
	 * Primarna boja
	 */
	private Color foreground;
	/**
	 * Sekundarna boja
	 */
	private Color background;
	
	/**
	 * Konstruktor
	 * @param fgColor JColorArea za primarnu boju
	 * @param bgColor JColorArea za sekundarnu boju
	 */
	public BottomLabel(JColorArea fgColor, JColorArea bgColor) {
		this.fgColorArea = fgColor;
		this.bgColorArea = bgColor;
		foreground = fgColor.getCurrentColor();
		background = bgColor.getCurrentColor();
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buildText();
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		if (source == fgColorArea) {
			foreground = newColor;
		} else if (source == bgColorArea) {
			background = newColor;
		}
		buildText();
	}

	/**
	 * Generiraj tekst za trenutno odabrane boje.
	 */
	private void buildText() {
		String text = 
				String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).", 
						foreground.getRed(), foreground.getGreen(), foreground.getBlue(),
						background.getRed(), background.getGreen(), background.getBlue()
						);
		this.setText(text);
	}

}
