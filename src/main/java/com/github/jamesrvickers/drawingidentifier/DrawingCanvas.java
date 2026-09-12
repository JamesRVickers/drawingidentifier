package com.github.jamesrvickers.drawingidentifier;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class DrawingCanvas extends JPanel {
    private final BufferedImage image;
    private Graphics2D graphics;
    private int lastX, lastY;

    DrawingCanvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                graphics.drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public static void createAndShowGui() {
        JFrame frame = new JFrame("Drawing Identifier");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingCanvas canvas = new DrawingCanvas(400, 400);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> canvas.clear());

        JButton identifyButton = new JButton("Identify");
        identifyButton.addActionListener(e -> {
            BufferedImage image = canvas.getImage();
            try {
                File drawingsDir = new File("data/drawings");
                File drawingInfoDir = new File("data/drawing_info");
                drawingsDir.mkdirs();
                drawingInfoDir.mkdirs();

                File outputFile = new File(drawingsDir, "drawing_" + System.currentTimeMillis() + ".png");
                ImageIO.write(image, "png", outputFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JPanel controls = new JPanel();
        controls.add(clearButton);
        controls.add(identifyButton);

        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void clear() {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.BLACK);
        repaint();
    }

    BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}