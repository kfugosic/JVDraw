package hr.fer.zemris.java.hw16.jvdraw.interfaces;

/**
 * 
 * Sučelje koje implementira svaki razred kome je bitna informacija o 
 * dodavanju/brisanju/uređivanju geometrijskih likova.
 * 
 * @author Kristijan Fugosic
 *
 */
public interface DrawingModelListener {

	/**
	 * Dojavi da je objekt dodan
	 * @param source Izvor
	 * @param index0 Index od
	 * @param index1 Index do 
	 */
	public void objectsAdded(IDrawingModel source, int index0, int index1);
	
	/**
	 * Dojavi da je objekt dodan
	 * @param source Izvor
	 * @param index0 Index od
	 * @param index1 Index do 
	 */
	public void objectsRemoved(IDrawingModel source, int index0, int index1);

	/**
	 * Dojavi da je objekt dodan
	 * @param source Izvor
	 * @param index0 Index od
	 * @param index1 Index do 
	 */
	public void objectsChanged(IDrawingModel source, int index0, int index1);
}
