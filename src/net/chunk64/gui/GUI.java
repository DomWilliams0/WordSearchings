package net.chunk64.gui;

import javax.swing.*;
import java.awt.*;

public class GUI
{
	public static final Dimension WINDOW_SIZE = new Dimension(800, 800);
	public static GUI INSTANCE;
	private PanelController controller;

	public GUI()
	{
		INSTANCE = this;
		JFrame frame = new JFrame("WordSearchings");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(WINDOW_SIZE);
		frame.setLocationRelativeTo(null);

		controller = new PanelController(frame);
		frame.add(controller);
	}

	public PanelController getController()
	{
		return controller;
	}
}
