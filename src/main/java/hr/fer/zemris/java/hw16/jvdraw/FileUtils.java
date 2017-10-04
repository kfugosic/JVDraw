package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 *  Skup metoda koje nam služe za učitavanje i spremanje
 *  slika u njihovom tekstovnom i slikovnom obliku.
 *  
 *  Očekivano je od korisnika da tekstualne zapise svojih crteža sprema s nastavkom .jvd.
 *  Osim tekstualnog zapisa, korisnik svoj rad može spremiti i kao png/jpg/gif sliku.
 *  
 * @author Kristijan Fugosic
 *
 */
public class FileUtils {

	/**
	 * Primjerak razreda jvDraw
	 */
	private JVDraw jvDraw;
	/**
	 * Meni
	 */
	private JMenu fileMenu;
	/**
	 * Model koji sadrži sve geometrijske likove
	 */
	private DrawingModel drawingModel;
	/**
	 * Putanja do trenutno otvorene slike
	 */
	private static Path openedImagePath = null;

	/**
	 * Konstruktor
	 * @param jvDraw JVDraw
	 * @param fileMenu Meni
	 * @param drawingModel Model koji sadrži sve geometrijske likove
	 */
	public FileUtils(JVDraw jvDraw, JMenu fileMenu, DrawingModel drawingModel) {
		this.jvDraw = jvDraw;
		this.fileMenu = fileMenu;
		this.drawingModel = drawingModel;
	}

	/**
	 * Close program action
	 */
	private Action exitAction = new AbstractAction("Exit") {

		/***/
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (drawingModel.isChangeOccured()) {
				int option = JOptionPane.showConfirmDialog(null, "Do you want to save your image?", "Info",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.CANCEL_OPTION) {
					return;
				} else if (option == JOptionPane.YES_OPTION) {
					saveImage(false);
				}
			}
			jvDraw.dispose();
		}

	};

	/**
	 * Recreate image from saved file
	 */
	private Action openImageAction = new AbstractAction("Open") {

		/***/
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JVD File", "jvd");
			fc.setFileFilter(filter);
			
			fc.setDialogTitle("Open image");

			if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path filePath = fc.getSelectedFile().toPath();

			String text = loadFile(filePath);
			if (text == null) {
				return;
			}

			openedImagePath = filePath;
			drawingModel.clear();
			readObjects(text);

		}

	};

	/**
	 * Save image action
	 */
	private Action saveImageAction = new AbstractAction("Save") {

		/***/
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveImage(false);
		}
	};

	/**
	 * Save-As image action
	 */
	private Action saveImageAsAction = new AbstractAction("Save As") {

		/***/
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveImage(true);

		}
	};

	/**
	 * Export image action
	 */
	private Action exportImageAction = new AbstractAction("Export") {

		/***/
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportImage();
		}

		private void exportImage() {

			JFileChooser fc = new JFileChooser();

		    fc.addChoosableFileFilter(new FileNameExtensionFilter("JPG", "jpg"));
		    fc.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "png"));
		    fc.addChoosableFileFilter(new FileNameExtensionFilter("GIF", "gif"));
		
			fc.setDialogTitle("Export image");
			if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "Saving canceled, image isn't saved.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			Path chosenPath = fc.getSelectedFile().toPath();
			if (Files.exists(chosenPath)) {
				int stisnuto = JOptionPane.showConfirmDialog(null,
						"File already exists, are you sure you want to replace it?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (stisnuto == JOptionPane.NO_OPTION) {
					exportImage();
					return;
				}
			}
	
			String[] extensions = ((FileNameExtensionFilter) fc.getFileFilter()).getExtensions();
			
			String extension = null;
			for (String ext : extensions) {
				if (chosenPath.getFileName().endsWith("." + ext)) {
					extension = ext;
					break;
				}
			}
			
			if (extension == null) {
				chosenPath = Paths.get(chosenPath + "." + extensions[0]);
				extension = extensions[0];
			}
			
			drawImage(chosenPath, extension);

		}

		private void drawImage(Path path, String extension) {
			
			Dimension dim = drawingModel.getDimension();
			BufferedImage image = new BufferedImage(
					 (int)dim.getWidth(), (int)dim.getHeight(), BufferedImage.TYPE_3BYTE_BGR
					);
			
			Dimension shifts = drawingModel.getShift();
			int shiftX = (int) shifts.getWidth();
			int shiftY = (int) shifts.getHeight();
			
			Graphics2D g = image.createGraphics();
			
			g.fillRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
			List<GeometricalObject> listOfObjects = drawingModel.getObjects();
			for (GeometricalObject object : listOfObjects) {
				object.drawObject(g, shiftX, shiftY);
			}
			
			g.dispose();
			
			try {
				ImageIO.write(image, extension, path.toFile());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Export failed.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			JOptionPane.showMessageDialog(null, "Export successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
			
		}
	};

	/**
	 * Method for image saving
	 * 
	 * @param saveAs
	 *            if false then normal "Save", if true then "Save As"
	 */
	private void saveImage(boolean saveAs) {

		Path chosenPath = openedImagePath;

		if (chosenPath == null || saveAs) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JVD File", "jvd");
			fc.setFileFilter(filter);
			
			fc.setDialogTitle("Save file");
			if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "Saving canceled, image isn't saved.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			chosenPath = fc.getSelectedFile().toPath();
			if (Files.exists(chosenPath) && chosenPath != openedImagePath) {
				int stisnuto = JOptionPane.showConfirmDialog(null,
						"File already exists, are you sure you want to replace it?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (stisnuto == JOptionPane.NO_OPTION) {
					saveImage(saveAs);
					return;
				}
			}
		}

		String text = buildText();

		try {
			Files.write(chosenPath, text.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Image isn't saved, error occured.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(null, "Saving successful!", "Info", JOptionPane.INFORMATION_MESSAGE);

		drawingModel.setChangeOccured(false);

		openedImagePath = chosenPath;

		return;
	}

	/**
	 * Izgradi datoteku koristeći metodu getSavingFormat kojom raspolaže svaki tip geometrijskog lika
	 * @return Tekst za zapis u .jvd
	 */
	private String buildText() {
		List<GeometricalObject> objects = drawingModel.getObjects();
		StringBuilder sb = new StringBuilder();
		for (GeometricalObject object : objects) {
			sb.append(object.getSavingFormat());
		}
		return sb.toString();
	}

	/**
	 * Pročitaj datoteku i dodaj sve geometrijske likove u crtež
	 * @param text Tekst datoteke
	 */
	private void readObjects(String text) {
		String[] lines = text.split("\r\n");
		for (String string : lines) {
			GeometricalObject object = parseObject(string);
			if (object == null) {
				System.out.println("Invalid object detected.");
				continue;
			}
			drawingModel.add(object);
		}
		drawingModel.setChangeOccured(false);
	}

	/**
	 * Parsira danu liniju kako bi otkrio o kojem se geometrijskom liku radi, te kreira taj lik
	 * @param string Linija iz datoteke
	 * @return Geometrijski lik
	 */
	private GeometricalObject parseObject(String string) {
		GeometricalObject object = null;
		String[] att = string.split(" ");
		if (att[0].equals("LINE")) {
			if (att.length != 8) {
				return null;
			}
			try {
				int startX = Integer.parseInt(att[1]);
				int startY = Integer.parseInt(att[2]);
				int endX = Integer.parseInt(att[3]);
				int endY = Integer.parseInt(att[4]);
				int r = Integer.parseInt(att[5]);
				int g = Integer.parseInt(att[6]);
				int b = Integer.parseInt(att[7]);
				object = new Line(new Point(startX, startY), new Point(endX, endY), new Color(r, g, b));
			} catch (Exception e) {
				return null;
			}
		} else if (att[0].equals("CIRCLE")) {
			if (att.length != 7) {
				System.out.println(string);
				return null;
			}
			try {
				int centerX = Integer.parseInt(att[1]);
				int centerY = Integer.parseInt(att[2]);
				int radius = Integer.parseInt(att[3]);
				int r = Integer.parseInt(att[4]);
				int g = Integer.parseInt(att[5]);
				int b = Integer.parseInt(att[6]);
				object = new Circle(new Point(centerX, centerY), radius, new Color(r, g, b), Color.WHITE, false);
			} catch (Exception e) {
				System.out.println(string);
				return null;
			}
		} else if (att[0].equals("FCIRCLE")) {
			if (att.length != 10) {
				return null;
			}
			try {
				int centerX = Integer.parseInt(att[1]);
				int centerY = Integer.parseInt(att[2]);
				int radius = Integer.parseInt(att[3]);
				int r = Integer.parseInt(att[4]);
				int g = Integer.parseInt(att[5]);
				int b = Integer.parseInt(att[6]);
				int rBg = Integer.parseInt(att[7]);
				int gBg = Integer.parseInt(att[8]);
				int bBg = Integer.parseInt(att[9]);
				object = new Circle(new Point(centerX, centerY), radius, new Color(r, g, b), new Color(rBg, gBg, bBg),
						true);
			} catch (Exception e) {
				return null;
			}
		}

		return object;

	}

	/**
	 * Pročitaj tekst datoteke, vrati ga, izbaci dijalog pogreške ukoliko nešto pođe po zlu.
	 * @param filePath Putanja do datoteke
	 * @return Tekst iz datoteke
	 */
	private String loadFile(Path filePath) {
		if (!Files.isReadable(filePath)) {
			JOptionPane.showMessageDialog(null, "Couldn't open file!", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		String text = null;
		try {
			text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Couldn't open file!", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return text;
	}

	/**
	 * Dodaj menu iteme u meni.
	 */
	public void initFileMenu() {
		fileMenu.add(new JMenuItem(openImageAction));
		fileMenu.add(new JMenuItem(saveImageAction));
		fileMenu.add(new JMenuItem(saveImageAsAction));
		fileMenu.add(new JMenuItem(exportImageAction));
		fileMenu.add(new JMenuItem(exitAction));
	}

}
