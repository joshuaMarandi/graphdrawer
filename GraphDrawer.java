import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphDrawer extends JFrame {
    private JTextField xInput;
    private JTextField yInput;
    private GraphPanel graphPanel;

    public GraphDrawer() {
        setTitle("Graph Drawer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        xInput = new JTextField(15);
        yInput = new JTextField(15);
        JButton drawButton = new JButton("Draw Graph");

        inputPanel.add(new JLabel("X Values (comma-separated):"));
        inputPanel.add(xInput);
        inputPanel.add(new JLabel("Y Values (comma-separated):"));
        inputPanel.add(yInput);
        inputPanel.add(drawButton);

        // Graph panel
        graphPanel = new GraphPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);

        // Action listener for the button
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String xText = xInput.getText();
                String yText = yInput.getText();
                
                try {
                    String[] xValues = xText.split(",");
                    String[] yValues = yText.split(",");
                    if (xValues.length != yValues.length) {
                        throw new IllegalArgumentException("X and Y must have the same number of values!");
                    }

                    int[] xPoints = new int[xValues.length];
                    int[] yPoints = new int[yValues.length];

                    for (int i = 0; i < xValues.length; i++) {
                        xPoints[i] = Integer.parseInt(xValues[i].trim());
                        yPoints[i] = Integer.parseInt(yValues[i].trim());
                    }

                    graphPanel.setPoints(xPoints, yPoints);
                    graphPanel.repaint();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GraphDrawer.this, "Invalid input! " + ex.getMessage());
                }
            }
        });
    }


    private static class GraphPanel extends JPanel {
        private int[] xPoints;
        private int[] yPoints;

        public void setPoints(int[] xPoints, int[] yPoints) {
            this.xPoints = xPoints;
            this.yPoints = yPoints;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw axes
            int width = getWidth();
            int height = getHeight();
            g2d.drawLine(50, height - 50, width - 50, height - 50); // X-axis
            g2d.drawLine(50, height - 50, 50, 50);                 // Y-axis

            if (xPoints != null && yPoints != null) {
                // Scale points to fit the panel
                int maxX = Integer.MIN_VALUE;
                int maxY = Integer.MIN_VALUE;
                for (int x : xPoints) {
                    maxX = Math.max(maxX, x);
                }
                for (int y : yPoints) {
                    maxY = Math.max(maxY, y);
                }

                int graphWidth = width - 100;
                int graphHeight = height - 100;

                int[] scaledX = new int[xPoints.length];
                int[] scaledY = new int[yPoints.length];

                for (int i = 0; i < xPoints.length; i++) {
                    scaledX[i] = 50 + (xPoints[i] * graphWidth / maxX);
                    scaledY[i] = height - 50 - (yPoints[i] * graphHeight / maxY);
                }
                

                // Draw points and lines
                g2d.setColor(Color.BLUE);
                for (int i = 0; i < scaledX.length - 1; i++) {
                    g2d.drawLine(scaledX[i], scaledY[i], scaledX[i + 1], scaledY[i + 1]);
                }
                for (int i = 0; i < scaledX.length; i++) {
                    g2d.fillOval(scaledX[i] - 3, scaledY[i] - 3, 6, 6);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphDrawer drawer = new GraphDrawer();
            drawer.setVisible(true);
        });
    }
}
