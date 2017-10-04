package hr.fer.zemris.java.hw16.jvdraw.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.DrawingObjectType;
import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Prilagođena komponenta koja služi kao prostor za crtanje.
 * 
 * @author Kristijan Fugosic
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Model za crtanje
	 */
	DrawingModel drawingModel;
	/**
	 * Privremeni geom. lik koji se koristi za prikaz nakon prvog klika korisnika
	 */
	GeometricalObject preview;
	/**
	 * Referenca na glavni okvir
	 */
	JVDraw JVDraw;
	/**
	 * Pozicija prvog klika
	 */
	Point firstPos;
	/**
	 * 
	 */
	Point secondPos;

	/**
	 * Konstruktor koji postavlja reference na drawingModel i JVDraw, 
	 * te postavlja potrebne listenere na prostor za crtanje.
	 * 
	 * @param drawingModel Model
	 * @param JVDraw JVDraw
	 */
	public JDrawingCanvas(DrawingModel drawingModel, JVDraw JVDraw) {
		this.drawingModel = drawingModel;
		this.JVDraw = JVDraw;
		drawingModel.addDrawingModelListener(this);
		addMouseListener(new ColorMouseListener());
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (firstPos != null) {
					secondPos = e.getPoint();
					repaint();
				}
			}
		});

	}

	@Override
	public void objectsAdded(IDrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsRemoved(IDrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	public void objectsChanged(IDrawingModel source, int index0, int index1) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		List<GeometricalObject> objects = drawingModel.getObjects();

		for (GeometricalObject object : objects) {
			object.drawObject(g, 0, 0);
		}

		if (firstPos != null) {
			preview.preview(firstPos, secondPos, g);
		}
	}

	/**
	 * 
	 * Generira privremene, odnosno stalne nove geometrijske likove
	 * nakon prvog, odnosno drugog klika.
	 * 
	 * @author Kristijan Fugosic
	 *
	 */
	private class ColorMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			DrawingObjectType objectType = JVDraw.getObjectType();
			if(objectType == null){
				return;
			}
			Point mousePos = e.getPoint();
			Color fg = JVDraw.getFgColor();
			Color bg = JVDraw.getBgColor();
			if(firstPos == null) {
				if(objectType == DrawingObjectType.LINE){
					preview = new Line(fg);
				} else if(objectType == DrawingObjectType.CIRCLE){
					preview = new Circle(fg, bg, false);
				} else if(objectType == DrawingObjectType.FILLED_CIRCLE){
					preview = new Circle(fg, bg, true);
				}
				firstPos = mousePos;
			} else {
				GeometricalObject newObject = null;
				if(objectType == DrawingObjectType.LINE){
					newObject = new Line(firstPos, mousePos, fg);
				} else if(objectType == DrawingObjectType.CIRCLE){
					newObject = new Circle(firstPos, mousePos, fg, bg, false);
				} else if(objectType == DrawingObjectType.FILLED_CIRCLE){
					newObject = new Circle(firstPos, mousePos, fg, bg, true);
				}
				drawingModel.add(newObject);
				firstPos = null;
			}
		}

}

}
