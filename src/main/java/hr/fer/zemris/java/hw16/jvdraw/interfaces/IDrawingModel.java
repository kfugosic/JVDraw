package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * 
 * Model za crtanje koji sadrži popis svih geom. likova te listenera 
 * koje obavještava o promjenama u vezi geom. likova.
 * 
 * @author Kristijan Fugosic
 *
 */
public interface IDrawingModel {
	/**
	 * Dohvaća broj geom. likova
	 * @return Broj geom. likova
	 */
	public int getSize();

	/**
	 * Dohvati objekt na zadnom indexu
	 * @param index Index
	 * @return Geom. lik
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Dodaj objekt u listu.
	 * @param object Geom. lik
	 */
	public void add(GeometricalObject object);
	
	/**
	 * Osvježi objekt
	 * @param index Index
	 */
	public void update(int index);

	/**
	 * Makni objekt iz liste
	 * @param index Index
	 */
	public void remove(int index);

	/**
	 * Dodaj listener.
	 * @param l listener
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Makni listener.
	 * @param l listener
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Očisti listu objekata.
	 */
	public void clear();
	
}