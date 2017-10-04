package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * 
 * Pruža informacije u trenutno odabranoj boji i prosljeđuje ih svojim listenerima.
 * 
 * @author Kristijan Fugosic
 *
 */
public interface IColorProvider {
	/**
	 * Vrati trenutnu boju
	 * @return Trenutna boja
	 */
	public Color getCurrentColor();

	/**
	 * Zakači listener.
	 * @param l Listener
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Makni listener.
	 * @param l Listener
	 */
	public void removeColorChangeListener(ColorChangeListener l);
	
}
