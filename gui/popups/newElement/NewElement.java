package gui.popups.newElement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;

import gui.Gui;
import gui.draw.Draw;
import util.Graph;
import gui.draw.PanelPaint;

public class NewElement extends JDialog
{
    // ===== Variables used to create the settings window =====
    JTabbedPane tabbedPane;

    // === BLANK ===
        JPanel blankPanel;

        // Radio Button Group
        private ButtonGroup elementsButton;
            //The radio buttons and the items inside
            private JRadioButton graphButton,
                                automatonButton;
        
        //Checkbox
            private JCheckBox isOriented,
                                isWeighted;
        
        // name
            private JLabel nameLabel;
            private JTextField nameTextField;
        
        // Buttons
            private JButton validation,
                            cancel;
    
    // === MATRIX ===
        JPanel matrixPanel,
                informationMatrixPanel,
                inputMatrixPanel;
        
            // number of vertex
                private JLabel numberVertexLabel;
                private JTextField numberVertexTextField;
                private JButton numberVertexButton;
            // type of figure
                // Radio Button Group
                private ButtonGroup elementsMatrixButton;
                //The radio buttons and the items inside
                private JRadioButton graphMatrixButton,
                                    automatonMatrixButton;
                private int type = 1, nbVertex;
            // matrix
                private ArrayList<JTextField> cellsList;
                private JButton createFigure;


    private static int nbrElementCreated = 0;

    public NewElement(Gui gui, Draw drawArea)
    {

        super(gui, "New element",true);
        this.setMinimumSize(new Dimension(250,250));
        this.setSize(new Dimension(250,250));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        JDialog main = this;
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    main.setSize(new Dimension(250,250));
                    main.setLocationRelativeTo(null);
                }else{
                    main.setSize(new Dimension(400,400));
                    main.setLocationRelativeTo(null);
                }
            }
        });
        this.add(tabbedPane);

        this.addBlankTab(gui, drawArea);

        this.addMatrixTab(gui, drawArea);

        this.setVisible(true);
    }




    private void addBlankTab(Gui gui, Draw drawArea){

        
        // Blank tabulation
        blankPanel = new JPanel();
        blankPanel.setSize(new Dimension(250,250));
        blankPanel.setLayout(new GridLayout(4,2, 8,8));

        graphButton = new JRadioButton("Graph ");
        graphButton.setSelected(true);
        graphButton.setHorizontalAlignment(JRadioButton.CENTER);
        graphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //When Graph is selected, we enable the checkbox to create a weigthed/oriented graph
                isOriented.setEnabled(true);
                isWeighted.setEnabled(true);
            }
        });
        blankPanel.add(graphButton);

        automatonButton = new JRadioButton("Automaton ");
        automatonButton.setSelected(true);
        automatonButton.setHorizontalAlignment(JRadioButton.CENTER);
        automatonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //When Graph is selected, we enable the checkbox to create a weigthed/oriented graph
                isOriented.setEnabled(false);
                isWeighted.setEnabled(false);
            }
        });
        blankPanel.add(automatonButton);

        elementsButton = new ButtonGroup();
        elementsButton.add(graphButton);
        elementsButton.add(automatonButton);
        
        //Orientation

        isOriented = new JCheckBox("Oriented");
        isOriented.setHorizontalAlignment(JCheckBox.CENTER);
        blankPanel.add(isOriented);

        isWeighted = new JCheckBox("Weighted ");
        isWeighted.setHorizontalAlignment(JCheckBox.CENTER);
        blankPanel.add(isWeighted);

        // name figure
        nameLabel = new JLabel("Name :",SwingConstants.CENTER);
        blankPanel.add(nameLabel);

        nameTextField = new JTextField("Graph "+(nbrElementCreated++));
        nameTextField.setHorizontalAlignment(JTextField.CENTER);
        nameTextField.setPreferredSize(new Dimension(116,20));
        blankPanel.add(nameTextField);

        // buttons

        Action cancelAction = new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        cancelAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        cancel = new JButton(cancelAction);
        cancel.getActionMap().put("cancelNew", cancelAction);
        cancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke)cancelAction.getValue(Action.ACCELERATOR_KEY), "cancelNew");
        blankPanel.add(cancel);

        validation = new JButton("Create");
        validation.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                createElement(gui, drawArea);
            }
        });
        blankPanel.add(validation);


        tabbedPane.addTab("Blank", blankPanel);
    }

    private void addMatrixTab(Gui gui, Draw drawArea){
        // Matrix Tabulation

        matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(2, 1));

        informationMatrixPanel = new JPanel();
        informationMatrixPanel.setLayout(new GridLayout(2,3,8,8));

        numberVertexLabel = new JLabel("Number of vertex : ");
        informationMatrixPanel.add(numberVertexLabel);

        numberVertexTextField = new JTextField("1");
        numberVertexTextField.setHorizontalAlignment(JTextField.CENTER);
        numberVertexTextField.setSize(new Dimension(20,20));
        informationMatrixPanel.add(numberVertexTextField);

        numberVertexButton = new JButton("Run");
        numberVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    if ((Integer.parseInt(numberVertexTextField.getText())) > 0) {
                        nbVertex = Integer.parseInt(numberVertexTextField.getText());
                        inputMatrixPanel = new JPanel();
                        inputMatrixPanel.setLayout(new GridLayout(nbVertex,nbVertex,8,8));
                        matrixPanel.add(inputMatrixPanel);
                        cellsList = new ArrayList<JTextField>();
                        for (int i = 0; i < nbVertex*nbVertex; i++) {
                            JTextField temp = new JTextField();
                            numberVertexTextField.setHorizontalAlignment(JTextField.CENTER);
                            numberVertexTextField.setSize(new Dimension(20,20));
                            cellsList.add(temp);
                            inputMatrixPanel.add(temp);
                        }
                    }

                } catch (Exception exce) {
                    System.out.println(exce.toString());
                }
                
            }
        });
        informationMatrixPanel.add(numberVertexButton);


        graphMatrixButton = new JRadioButton("Graph ");
        graphMatrixButton.setSelected(true);
        graphMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 1;
            }
        });
        informationMatrixPanel.add(graphMatrixButton);

        automatonMatrixButton = new JRadioButton("Automaton ");
        automatonMatrixButton.setSelected(true);
        automatonMatrixButton.setHorizontalAlignment(JRadioButton.CENTER);
        automatonMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = 2;
            }
        });
        informationMatrixPanel.add(automatonMatrixButton);

        elementsMatrixButton = new ButtonGroup();
        elementsMatrixButton.add(graphMatrixButton);
        elementsMatrixButton.add(automatonMatrixButton);

        matrixPanel.add(informationMatrixPanel);


        
        

        tabbedPane.addTab("Matrix", matrixPanel);
    }


    //Once everything is selected, we read user inputs to create the graph/automaton
    private void createElement(Gui gui, Draw drawArea)
    {
        //We first read if its an automaton or a graph
        if(graphButton.isSelected())
        {
            
            //Creating the tabulation
            PanelPaint panelPaint = new PanelPaint(gui, drawArea);
            drawArea.addTab(nameTextField.getText(), null, panelPaint, nameTextField.getText()); //We create the new tab and retrieve it
            //Creating a new Graph
            //Each graph is associated with a tabulation
            Graph graph = new Graph(nameTextField.getText(), isOriented.isSelected(), isWeighted.isSelected(), panelPaint);
            //We then add these two components to the list of opened tabulations
            gui.getTabulations().put(panelPaint,graph);

            //We go to the new tabulation and draw the panel
            drawArea.setSelectedComponent(panelPaint);
            panelPaint.repaint();
        }
        else if(automatonButton.isSelected())
        {
            //Creating new automaton
        }

        //Once the automaton/graph is created, we close the JDialog
        this.dispose();
    }
}