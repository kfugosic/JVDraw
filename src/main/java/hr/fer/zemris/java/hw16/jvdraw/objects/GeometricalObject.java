package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Implementacija geometrijskog lika.
 * 
 * @author Kristijan Fugosic
 *
 */
public abstract class GeometricalObject {

	/**
	 * Nacrtaj geometrijski lik.
	 * Pomaci se koriste pri spremanju slike radi ustede prostora.
	 * 
	 * @param g Graphics
	 * @param shiftX Pomak gore 
	 * @param shiftY Pomak dolje
	 */
	public abstract void drawObject(Graphics g, int shiftX, int shiftY);
	
	/**
	 * Privremeni prikaz objekta.
	 * 
	 * @param firstPos Pozicija prvog klika.
	 * @param secondPos Pozicija na kojoj se trenutno nalazi pokazivac.
	 * @param g Graphics
	 */
	public abstract void preview(Point firstPos, Point secondPos, Graphics g);

	/**
	 * Izmjeni geom. lik
	 * @return True ako se izmjena dogodila, false inace
	 */
	public abstract boolean edit();

	/**
	 * Vrati tekstualni prikaz geom. lika kao format za spremanje u .jvd
	 * @return Tekst 
	 */
	public abstract String getSavingFormat();
	
	/**
	 * Resetiraj brojac
	 */
	public abstract void resetCounter();
	
	/**
	 * Vrati minimalnu vrijednost X komponente.
	 * @return Min X
	 */
	public abstract int getMinX();
	
	/**
	 * Vrati maksimalnu vrijednost X komponente.
	 * @return Max X
	 */
	public abstract int getMaxX();
	
	/**
	 * Vrati minimalnu vrijednost Y komponente.
	 * @return Min Y
	 */
	public abstract int getMinY();
	
	/**
	 * Vrati maksimalnu vrijednost Y komponente.
	 * @return Max Y
	 */
	public abstract int getMaxY();
	
	@Override
	public abstract String toString();

}
