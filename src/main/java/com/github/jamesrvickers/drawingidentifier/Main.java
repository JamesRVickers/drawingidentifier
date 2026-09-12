package com.github.jamesrvickers.drawingidentifier;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingCanvas::createAndShowGui);
    }
}