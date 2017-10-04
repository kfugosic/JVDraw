package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * 
 * Sučelje koje implementira svaki razred kome je bitna informacija o promjeni boje.
 * 
 * @author Kristijan Fugosic
 *
 */
public interface ColorChangeListener {
	
	/**
	 * Dojavljuje da je došlo do promjene boje.
	 * 
	 * @param source Provider
	 * @param oldColor Stara boja
	 * @param newColor Nova boja
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}