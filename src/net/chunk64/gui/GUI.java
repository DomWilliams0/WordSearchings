package net.chunk64.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GUI
{
	public static final Dimension WINDOW_SIZE = new Dimension(800, 800);
	public static Font FONT = loadFont();
	public static GUI INSTANCE;

	private PanelController controller;

	public GUI()
	{
		INSTANCE = this;

		JFrame frame = new JFrame("WordSearchings");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setSize(WINDOW_SIZE);
		frame.setLocationRelativeTo(null);

		controller = new PanelController(frame);
		frame.add(controller);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			System.err.println("Could not change ugly theme!");
		}
	}

	private static Font loadFont()
	{
		InputStream fontStream = GUI.class.getClassLoader().getResourceAsStream("consola.ttf"); // y u no load?s

		Font font;
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (FontFormatException | IOException e)
		{
			System.err.println("Could not load font!");
			font = Font.decode("Courier New");
		}
		return font;
	}

	public PanelController getController()
	{
		return controller;
	}
}
