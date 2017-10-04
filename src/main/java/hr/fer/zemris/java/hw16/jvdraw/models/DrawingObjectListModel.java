package hr.fer.zemris.java.hw16.jvdraw.models;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Model za listu. Prosljeđuje listi informaciju o dodavanju/brisanju/promjeni objekata.
 * 
 * @author Kristijan Fugosic
 *
 */
public final class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener  {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Model za crtanje
	 */
	DrawingModel drawingModel;
	
	/**
	 * Konstruktor koj prima model za crtanje
	 * @param drawingModel Model
	 */
	public DrawingObjectListModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
	}
	
	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

	@Override
	public void objectsAdded(IDrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
	}

	@Override
	public void objectsRemoved(IDrawingModel source, int index0, int index1) {
		fireIntervalRemoved(this, index0, index1);
	}

	@Override
	public void objectsChanged(IDrawingModel source, int index0, int index1) {
		fireContentsChanged(this, index0, index1);
	}

}
