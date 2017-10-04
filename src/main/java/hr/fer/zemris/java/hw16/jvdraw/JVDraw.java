package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw16.jvdraw.models.BottomLabel;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.models.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.models.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.models.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Okvir koji sadrži meni za pohranu/učitavanje slika, 
 * Toolbar sa opcijama za biranje boja i crtanje, te
 * listu svih nacrtanih geometrijskih likova i prostor za crtanje.
 * 
 * @author Kristijan Fugosic
 *
 */
public class JVDraw extends JFrame {

	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * String za akciju brisanja
	 */
	private static final String delAction = "delete";
	/**
	 * Primarna boja (koristi se kao boja linije i kružnice/obruba)
	 */
	private JColorArea fgColor = new JColorArea(Color.BLACK);
	/**
	 * Sekundarna boja (koristi se kao ispuna kruga)
	 */
	private JColorArea bgColor = new JColorArea(Color.WHITE);
	/**
	 * Tip geometrijskog lika koji je trenutno izabran (toggle gumbima)
	 */
	private DrawingObjectType objectType;
	/**
	 * Grupa toggle buttona (line, circle, filled circle)
	 */
	private ButtonGroup buttons;
	
	/**
	 * Konstruktor sa pocetnim postavkama prozora
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(150,150);
		setSize(1000, 600);
		setTitle("JVDraw!");

		initGUI();
	}

	/**
	 * Inicijaliziraj GUI, dodaj sve elemente.
	 */
	private void initGUI() {

		Container cp = getContentPane();

		cp.setLayout(new BorderLayout());

		createToolbar();

		BottomLabel bottomColorInfo = new BottomLabel(fgColor, bgColor);
		fgColor.addColorChangeListener(bottomColorInfo);
		bgColor.addColorChangeListener(bottomColorInfo);
		getContentPane().add(bottomColorInfo, BorderLayout.PAGE_END);

		DrawingModel drawingModel = new DrawingModel();
		JDrawingCanvas canvas = new JDrawingCanvas(drawingModel, this);
		getContentPane().add(canvas, BorderLayout.CENTER);
		
		DrawingObjectListModel listModel = new DrawingObjectListModel(drawingModel);
		drawingModel.addDrawingModelListener(listModel);
		JList<GeometricalObject> list = new JList<>(listModel);
		initList(list, drawingModel);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(100, 400));
		getContentPane().add(scrollPane, BorderLayout.LINE_END);		
		
		createMenus(drawingModel);

	}

	/**
	 * Dodaj listenere na listu geometrijskih likova 
	 * (kako bi reagirali na dvoklik i delete elemenata liste)
	 * 
	 * @param list lista geometrijskih likova 
	 * @param drawingModel Model za crtanje
	 */
	private void initList(JList<GeometricalObject> list, DrawingModel drawingModel) {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					int index = list.getSelectedIndex();
					drawingModel.update(index);
					list.clearSelection();
				}
			}
		});
		
		KeyStroke delKey = KeyStroke.getKeyStroke("DELETE");
		list.getInputMap().put(delKey, delAction);
		list.getActionMap().put(delAction, new AbstractAction() {	
			/***/
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if(index == -1){
					return;
				}
				drawingModel.remove(index);
			}
		});
	}

	/**
	 * Napravi alatnu traku
	 */
	private void createToolbar() {
		JToolBar toolbar = new JToolBar("Alatna traka");
		toolbar.setFloatable(true);

		toolbar.addSeparator();

		toolbar.add(fgColor);
		toolbar.addSeparator();
		toolbar.add(bgColor);
		
		toolbar.addSeparator();
		
		addToggleButtons(toolbar);
		
		getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}

	/**
	 * Dodaj gumbove na alatnu traku
	 * @param toolbar Alatna traka
	 */
	private void addToggleButtons(JToolBar toolbar) {
		
		JToggleButton line = new JToggleButton("Line");
		line.setActionCommand("Line");
		JToggleButton circle = new JToggleButton("Circle");
		circle.setActionCommand("Circle");
		JToggleButton filledCircle = new JToggleButton("Filled circle");
		filledCircle.setActionCommand("FCircle");
		
		line.addActionListener(new ToggleButtonListener());
		circle.addActionListener(new ToggleButtonListener());
		filledCircle.addActionListener(new ToggleButtonListener());
		
		toolbar.add(line);
		toolbar.add(circle);
		toolbar.add(filledCircle);		
		
		buttons = new ButtonGroup();
		buttons.add(line);
		buttons.add(circle);
		buttons.add(filledCircle);
		
	}

	/**
	 * Stvori menije.
	 * @param drawingModel Model za crtanje
	 */
	private void createMenus(DrawingModel drawingModel) {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		new FileUtils(this, fileMenu, drawingModel).initFileMenu();
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Listener koji reagira na promjenu selektiranog gumba za odabir geom. lika.
	 * 
	 * @author Kristijan Fugosic
	 *
	 */
	public class ToggleButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonAC = buttons.getSelection().getActionCommand();
			switch (buttonAC) {
			case "Line":
				objectType = DrawingObjectType.LINE;
				break;
			case "Circle":
				objectType = DrawingObjectType.CIRCLE;
				break;
			case "FCircle":
				objectType = DrawingObjectType.FILLED_CIRCLE;
				break;
			default:
				break;
			}
		}	
	}

	/**
	 * Dohvati primarnu boju.
	 * @return Primarna boja
	 */
	public Color getFgColor() {
		return fgColor.getCurrentColor();
	}
	
	/**
	 * Dohvati sekundarnu boju.
	 * @return Sekundarna boja
	 */
	public Color getBgColor() {
		return bgColor.getCurrentColor();
	}
	
	/**
	 * Vrati koj tip geom. lika je trenutno odabran toggle gumbom.
	 * @return Trenutno selektiran tip geom. lika
	 */
	public DrawingObjectType getObjectType() {
		return objectType;
	}
	
	/**
	 * Otvori prozor.
	 * @param args Ne ocekuje argumente
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}
	
}
