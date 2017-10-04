package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * Prilagođena komponenta za odabir boje.
 * 
 * @author Kristijan Fugosic
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Trenutno odabrana boja.
	 */
	private Color selectedColor;
	/**
	 * Lista listenera.
	 */
	List<ColorChangeListener> listeners = new LinkedList<>();
	
	/**
	 * Konstruktor koj prima inicijalnu boju i postavlja je kao trenutnu.
	 * @param initialColor Inicijalna boja
	 */
	public JColorArea(Color initialColor) {
		selectedColor = initialColor;
		addMouseListener(new ColorMouseListener());
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension size = new Dimension(15, 15);
		return size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(selectedColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}
	
	@Override
	public void addColorChangeListener(ColorChangeListener l) throws RuntimeException {
		if(l == null){
			throw new RuntimeException("Can't add null to listeners list.");
		}
		listeners.add(l);		
	}
	
	@Override
	public void removeColorChangeListener(ColorChangeListener l) throws RuntimeException {
		if(l == null){
			throw new RuntimeException("This list doesn't contain null values.");
		}
		listeners.remove(l);
	}

	/**
	 * Obavijesti sve listenere.
	 * @param oldColor Stara boja
	 * @param newColor Nova boja
	 */
	private void notifyListeners(Color oldColor, Color newColor){
		for (ColorChangeListener listener : listeners) {
			listener.newColorSelected(this, oldColor, newColor);
		}
	}

	/**
	 * Mouse listener koji reagira na klik i otvara prozor za odabir boje.
	 * @author Kristijan Fugosic
	 *
	 */
	private class ColorMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			new JColorChooser();
			Color newColor = JColorChooser.showDialog(null, "Choose a color", selectedColor);
			notifyListeners(selectedColor, newColor);
			selectedColor = newColor;
			repaint();
		}
		
	}
}

