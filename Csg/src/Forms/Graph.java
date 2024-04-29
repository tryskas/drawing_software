package Forms;

import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;


public class Graph {

    private JFrame frame;
    private JPanel rectPanel;
      
    private boolean btnCreatingRectangle = false;
    
    private boolean btnUnion = false;
    private boolean btnInter = false;
    private boolean btnMove = false;
    private boolean btnResize = false;
    private boolean btnInfo = false;
    
    private int startX = -1, startY = -1, endX = -1, endY = -1;
    private Shap selectedShape1, selectedShape2 = null, old=null;
    
    public List<Shap> shaps = new ArrayList();


    public Graph() {
    	
    	int choice = JOptionPane.showConfirmDialog(null, "Do you want to load from the latest version of the project?", "Loading choice", JOptionPane.YES_NO_OPTION);
        
        // Si l'utilisateur choisit de charger à partir du fichier de sérialisation
        if (choice == JOptionPane.YES_OPTION) {
            // Désérialiser le fichier
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shapes.ser"));
                shaps = (List<Shap>) ois.readObject();
                ois.close();
            } catch (FileNotFoundException e) {
                // Le fichier de sérialisation n'existe pas encore
                e.printStackTrace();
            } catch (IOException e) {
                // Erreur de lecture du fichier
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // La classe des objets sérialisés n'a pas été trouvée
                e.printStackTrace();
            }
        }

        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);  //size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar toolBar = new JToolBar();
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        
// ------------------------------- btn Info -------------------------------
        JButton InfoBtn = new JButton("Info");
        InfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBool();
                btnInfo = true;
                rectPanel.removeMouseListener(mouseListener);
                rectPanel.addMouseListener(mouseListener);
            }
        });
        toolBar.add(InfoBtn);
// ------------------------------- btn Info -------------------------------
        
// ---------------------------- btn create rectangle ----------------------------
        JButton creatRectBtn = new JButton("New Rectangle");
        creatRectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	resetBool();
                btnCreatingRectangle = true;
                rectPanel.removeMouseListener(mouseListener);
                rectPanel.addMouseListener(mouseListener);
            }
        });
        toolBar.add(creatRectBtn);
// ---------------------------- btn create rectangle ----------------------------


     // ---------------------------- btn resize ----------------------------
     JButton resizeBtn = new JButton("Resize");
     resizeBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             resetBool();
             btnResize = true;
             rectPanel.removeMouseListener(mouseListener);
             rectPanel.addMouseListener(mouseListener);
         }
     });
     toolBar.add(resizeBtn);
     // ---------------------------- btn resize ----------------------------
     
// --------------------------------- btn Union ---------------------------------
        JButton unionBtn = new JButton("Union");
        unionBtn.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		resetBool();
                btnUnion = true;
                rectPanel.removeMouseListener(mouseListener);
                rectPanel.addMouseListener(mouseListener);
            }
        });
        toolBar.add(unionBtn);   
// --------------------------------- btn Union ---------------------------------

// --------------------------------- btn Inter ---------------------------------
        JButton interBtn = new JButton("Inter");
        interBtn.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		resetBool();
                btnInter = true;
                rectPanel.removeMouseListener(mouseListener);
                rectPanel.addMouseListener(mouseListener);
            }
        });
        toolBar.add(interBtn);
// --------------------------------- btn Inter ---------------------------------
        
// --------------------------------- btn Move ---------------------------------
        JButton moveBtn = new JButton("Move");
        moveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 resetBool();
            	 btnMove = true;
            	 rectPanel.removeMouseListener(mouseListener);
                 rectPanel.addMouseListener(mouseListener);
            }
        });
        toolBar.add(moveBtn);
// --------------------------------- btn Move ---------------------------------
        
// --------------------------------- btn Save ---------------------------------
        JButton saveBtn = new JButton("Enregistrer");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    serializeShapes("shapes.ser");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        toolBar.add(saveBtn);
// --------------------------------- btn Save ---------------------------------

// --------------------- Panel for printing rectangles ------------------------
        rectPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
        
               for (Shap shap : shaps) {
                   shap.draw(g);
               }
            }
        };
        frame.getContentPane().add(rectPanel, BorderLayout.CENTER);   

// ================================= Mouse Dragged =================================
        rectPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (btnMove && selectedShape1 != null) {
                    int deltaX = e.getX() - startX;
                    int deltaY = e.getY() - startY;
                    selectedShape1.move(deltaX, deltaY);
                    startX = e.getX();
                    startY = e.getY();
                    rectPanel.repaint();
                    serializeShapes("shapes.ser");
                }
            }
        });
    }
 // ================================= Mouse Dragged =================================

// ================================= Click Mouse =====================================
    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
        	
// ---------------------------- function create rectangle ----------------------------
            if (btnCreatingRectangle) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (startX == -1 && startY == -1) {
                        startX = e.getX();
                        startY = e.getY();
                    } else {
                        endX = e.getX();
                        endY = e.getY();
                        btnCreatingRectangle = false;
                        
                        //ajoute un rectangle a la liste.
                        Shap shap = new Shap();
                        shap.addRectangle(new Rectangle(Math.min(startX, endX), Math.min(startY, endY), Math.abs(endX - startX), Math.abs(endY - startY)));
                        shaps.add(shap);
                        
                        rectPanel.removeMouseListener(this);
                        rectPanel.repaint();
                        serializeShapes("shapes.ser");
                        System.err.println("Rectangle créé avec les coordonnées : (" + startX + ", " + startY + ") et (" + endX + ", " + endY + ")");
                        startX=-1; startY=-1;
                    }
                }
            }       
// ---------------------------- function create rectangle ----------------------------
            
// --------------------------------- function Union ----------------------------------
            else if (btnUnion) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                	
                	// 1) select 2 shaps
                    selectShap(e.getX(), e.getY(), true);
                	
                    // 2) do an inter
                    if (selectedShape1 != null && selectedShape2 != null) {
                        	
                	   Shap unionResult = union(selectedShape1, selectedShape2);
                	   
                	   shaps.remove(selectedShape1);
                       shaps.remove(selectedShape2);
                       shaps.add(unionResult);
                       
                       System.err.println("New shap id= " + unionResult.getId());
                       selectedShape1 = null;
                       selectedShape2 = null;
                       rectPanel.repaint();
                       serializeShapes("shapes.ser");
                   }
                }
            }
// --------------------------------- function Union ----------------------------------
            
// --------------------------------- function Inter ----------------------------------
            else if (btnInter) {
            	if (SwingUtilities.isLeftMouseButton(e)) {
            		
            		// 1) select 2 shaps
                    selectShap(e.getX(), e.getY(), true);
                	
                    // 2) do an inter
                    if (selectedShape1 != null && selectedShape2 != null) {
                        	
                	   Shap InterResult = inter(selectedShape1, selectedShape2);
                	   
                	   shaps.remove(selectedShape1);
                       shaps.remove(selectedShape2);
                       shaps.add(InterResult);
                       
                       System.err.println("New shap id= " + InterResult.getId());
                       selectedShape1 = null;
                       selectedShape2 = null;
                       rectPanel.repaint();
                       serializeShapes("shapes.ser");
                   }
                }
            }
// --------------------------------- function Inter ----------------------------------

// --------------------------------- function move ----------------------------------
	         else if (btnMove) {
	        	 if (SwingUtilities.isLeftMouseButton(e)) {
	         		
	         		// 1) select shap 1
	             	if (selectedShape1 == null) {
	                     for (int i = shaps.size() - 1; i >= 0; i--) {
	                         Shap shape = shaps.get(i);
	                         if (shape.isTouch(e.getX(), e.getY())) {
	                             selectedShape1 = shape;
	                             startX = e.getX();
	                             startY = e.getY();
	                             System.err.println("Selected id= " + shape.getId());
	                             break;
	                         }
	                     }
	                 }
	            }
	         }
// --------------------------------- function move ----------------------------------
            
// --------------------------------- function Resize ----------------------------------
	         else if (btnResize) {
	             if (SwingUtilities.isLeftMouseButton(e)) {
	                 
	                 for (int i = shaps.size() - 1; i >= 0; i--) {
	                     Shap shape = shaps.get(i);
	                     if (shape.isTouch(e.getX(), e.getY())) {
	                         selectedShape1 = shape;
	                         System.err.println("Resize");
	                         System.err.println("Selected id= " + shape.getId());
	                         for (Rectangle rect : shape.getRectangles()) {
	                             System.err.println("X1 = " + rect.getX() + " Y1 = " + rect.getY() + " X2 = " + (rect.getX()+rect.getWidth()) + " Y2 = " + (rect.getY()+rect.getHeight()) );
	                         }
	                         //rectPanel.repaint();
	                         //serializeShapes("shapes.ser");
	                         System.err.println("test :");
	                         break;
	                     }
	                 }
	             }
	         }
//--------------------------------- function Resize ----------------------------------
            
//--------------------------------- function info ----------------------------------
	         else if (btnInfo) {
	             if (SwingUtilities.isLeftMouseButton(e)) {
	                 System.err.println("Info :");
	                 for (int i = shaps.size() - 1; i >= 0; i--) {
	                     Shap shape = shaps.get(i);
	                     if (shape.isTouch(e.getX(), e.getY())) {
	                         selectedShape1 = shape;
	                         System.err.println("Selected id= " + shape.getId());
	
	                         Graphics g = rectPanel.getGraphics();
	                         shape.selectdraw(g);
	                         if(old!=null){
	                             System.err.println("OLD :");
	                             for (Rectangle rect : old.getRectangles()) {
	                                 System.err.println("OLD X1 = " + rect.getX() + " Y1 = " + rect.getY() + " X2 = " + (rect.getX()+rect.getWidth()) + " Y2 = " + (rect.getY()+rect.getHeight()) );
	                             }
	                         }else{
	                             for (Rectangle rect : shape.getRectangles()) {
	                                 System.err.println("X1 = " + rect.getX() + " Y1 = " + rect.getY() + " X2 = " + (rect.getX()+rect.getWidth()) + " Y2 = " + (rect.getY()+rect.getHeight()) );
	                             }
	                         
	                             System.err.println("1 OLD : "+old);
	                             Shap old=shape;
	                             System.err.println("2 OLD : "+old);
	                         }
	                         break;
	                     }else {
	                    	 System.err.println("Hors du cadre");	
	                    	 
	                    	 }
	                 }
	             }
	         }
//--------------------------------- function info ----------------------------------
        }
    };
// ================================= Click Mouse =====================================
    
    public void resetBool() {
    	this.btnCreatingRectangle = false;
    	this.btnInter = false;
    	this.btnUnion = false;
    	this.btnMove = false;
    	this.startX = -1;
    	this.startY = -1;
    	this.endX = -1;
    	this.endY = -1;
    	this.selectedShape1=null;
    	this.selectedShape2=null;
    	this.old=null;
    }
    
    public void serializeShapes(String filename) {
    	System.err.println("start save");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(shaps);
            oos.close();
            System.err.println("save done !!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Shap union(Shap shape1, Shap shape2) {
    	
        Shap unionResult = new Shap();
        
    	for (Rectangle rect : shape1.getRectangles()) {
            unionResult.addRectangle(rect);
        }

        for (Rectangle rect : shape2.getRectangles()) {
            unionResult.addRectangle(rect);
        }
        return unionResult;
    }
    
    public Shap inter(Shap shape1, Shap shape2) {
        Shap interResult = new Shap();
        
        for (Rectangle rect1 : shape1.getRectangles()) {
            for (Rectangle rect2 : shape2.getRectangles()) {
                Rectangle intersect = rect1.intersection(rect2);
                if (intersect.getWidth() > 0 && intersect.getHeight() > 0) {
                    interResult.addRectangle(intersect);
                }
            }
        }
        
        return interResult;
    }
    
    public void selectShap(int Xpos, int Ypos, boolean isTwoSelection) {
    	if (selectedShape1 == null) {
    		for (int i = shaps.size() - 1; i >= 0; i--) {
                Shap shape = shaps.get(i);
                if (shape.isTouch(Xpos, Ypos)) {
                    selectedShape1 = shape;
                    System.err.println("Selected id= " + shape.getId());
                    break;
                }
            }
        } 
    	
    	// 2) select shap 2
    	if (isTwoSelection) {
    		 for (int i = shaps.size() - 1; i >= 0; i--) {
                 Shap shape = shaps.get(i);
                 if (shape.isTouch(Xpos, Ypos) && !shape.equals(selectedShape1)) {
                     selectedShape2 = shape;
                     System.err.println("Selected id= " + shape.getId());
                     break;
                 }
             }
    	}
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Graph window = new Graph();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
