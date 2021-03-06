/*
    Path finding by Dijkstra algorithm
    Ali Abdolazimi
    Mohammad Amirdoost
    Sajjad Moghayyad
*/

package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JPanel {

    public MainWindow(){
        super.setLayout(new BorderLayout());
        setTopPanel();
        setButtons();
    }

    private void setTopPanel() {
        JLabel info = new JLabel("Dijkstra Shortest Path Problem & DP TSP Problem by MAS");
        info.setForeground(new Color(230, 220, 250));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(130, 50, 250));
        panel.add(info);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panel, BorderLayout.NORTH);
    }

    private void setButtons(){
        JButton runPathFinder = new JButton();
        setupIcon(runPathFinder, "run");
        runPathFinder.setToolTipText("run path finder");
        JButton runTSP = new JButton();
        setupIcon(runTSP, "run1");
        runTSP.setToolTipText("run TSP solver");
        JButton reset = new JButton();
        setupIcon(reset, "reset");
        final JButton info = new JButton();
        setupIcon(info, "info");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PathFindingDrawUtils.parseColor("#DDDDDD"));
        buttonPanel.add(runPathFinder);
        buttonPanel.add(info);
        buttonPanel.add(runTSP);

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Click on empty space to create new node\n" +
                        "Drag from node to node to create an edge\n" +
                        "Click on edges to set the weight\n\n" +
                        "Combinations:\n" +
                        "Shift + Left Click       :    Set node as source\n" +
                        "Shift + Right Click     :    Set node as destination\n" +
                        "Ctrl  + Drag               :    Reposition Node\n" +
                        "Ctrl  + Click                :    Get Path of Node\n" +
                        "Ctrl  + Shift + Click   :    Delete Node/Edge\n");
            }
        });

        runTSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame j = new JFrame();
                j.setTitle("Path finding by Dijkstra Algorithm");

                j.setSize(new Dimension(900, 600));
                j.add(new TSPProblem());
                j.setVisible(true);
            }
        });
        
        runPathFinder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame j = new JFrame();
                j.setTitle("Path finding by Dijkstra Algorithm");

                j.setSize(new Dimension(900, 600));
                j.add(new PathFindingProblem());
                j.setVisible(true);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupIcon(JButton button, String img){
        try {
            Image icon = ImageIO.read(getClass().getResource(
                    "/resources/" + img + ".png"));
            ImageIcon imageIcon = new ImageIcon(icon);
            button.setIcon(imageIcon);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
