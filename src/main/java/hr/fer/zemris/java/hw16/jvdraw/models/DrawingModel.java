package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * 
 * Model za crtanje koji sadrži popis svih geom. likova te listenera 
 * koje obavještava o promjenama u vezi geom. likova.
 * 
 * @author Kristijan Fugosic
 *
 */
public class DrawingModel implements IDrawingModel {

	/**
	 * Lista objekata (geom. likova)
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	/**
	 * Lista listenera
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();
	/**
	 * Bilježi je li se dogodila promjena
	 */
	private boolean changeOccured = false;
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {	
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		int index = objects.size()-1;
		for (DrawingModelListener listener : listeners) {
			listener.objectsAdded(this, index, index);
		}
		changeOccured = true;
	}

	public void update(int index){
		GeometricalObject toUpdate = objects.get(index);
		boolean updated = toUpdate.edit();
		if(updated){
			for (DrawingModelListener listener : listeners) {
				listener.objectsChanged(this, index, index);
			}	
		}
		changeOccured = true;
	}
	

	public void remove(int index) {
		objects.remove(index);
		for (DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, index, index);
		}
		changeOccured = true;
	}

	@Override
	public void clear() {
		new Line().resetCounter();
		new Circle().resetCounter();
		for(int i=0; i < objects.size(); i++){
			remove(i);
		}
		changeOccured = true;
	}
	
	@Override
	public void addDrawingModelListener(DrawingModelListener l) throws RuntimeException  {
		if(l == null){
			throw new RuntimeException("Can't add null to listeners list.");
		}
		if(!listeners.contains(l)){
			listeners.add(l);
		}
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) throws RuntimeException  {
		if(l == null){
			throw new RuntimeException("This list doesn't contain null values.");
		}
		listeners.remove(l);
	}

	/**
	 * Vrati listu objekata
	 * @return Lista geom. likova
	 */
	public List<GeometricalObject> getObjects() {
		return objects;
	}
	
	/**
	 * Dohvati boolean za pracenje promjena
	 * @return Boolean
	 */
	public boolean isChangeOccured() {
		return changeOccured;
	}
	
	/**
	 * Postavi boolean za pracenje promjena
	 * @param changeOccured Boolean
	 */
	public void setChangeOccured(boolean changeOccured) {
		this.changeOccured = changeOccured;
	}
	
	/**
	 * Vrati minimalne dimenzije crteza (makni nepotreban rub).
	 * @return sirina i visina
	 */
	public Dimension getDimension(){
		if(objects.isEmpty()){
			return new Dimension(1,1);
		}
		int maxX= -1;
		int minX= Integer.MAX_VALUE;
		int maxY= -1;
		int minY= Integer.MAX_VALUE;
		for (GeometricalObject object : objects) {
			if(object.getMaxX() > maxX){
				maxX = object.getMaxX();
			}
			if(object.getMaxY() > maxY){
				maxY = object.getMaxY();
			}
			if(object.getMinX() < minX){
				minX = object.getMinX();
			}
			if(object.getMinY() < minY){
				minY = object.getMinY();
			}
		}
		if(minY < 0){
			minY = 0;
		}
		if(minX < 0){
			minX = 0;
		}
		return new Dimension(maxX-minX, maxY-minY);
	}
	
	/**
	 * Vrati vrijednosti za koje treba pomaknuti sve objekte.
	 * @return sirina i visina
	 */
	public Dimension getShift(){
		if(objects.isEmpty()){
			return new Dimension(0,0);
		}
		int minX= Integer.MAX_VALUE;
		int minY= Integer.MAX_VALUE;
		for (GeometricalObject object : objects) {
			if(object.getMinX() < minX){
				minX = object.getMinX();
			}
			if(object.getMinY() < minY){
				minY = object.getMinY();
			}
		}
		if(minY < 0){
			minY = 0;
		}
		if(minX < 0){
			minX = 0;
		}
		return new Dimension(minX, minY);		
	}
	
}
