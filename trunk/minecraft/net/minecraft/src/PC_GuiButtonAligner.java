package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Gui helper for aligning buttons on screen.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PC_GuiButtonAligner {

	/**
	 * Get layout of given buttons
	 * 
	 * @param buttons map of: button ID -> translation key of label
	 * @param minWidth minimal button width
	 * @param distance space between 2 buttons
	 * @return structure of: ( layout[id,start x, width, text], total width )
	 */
	private static PC_Struct2<ArrayList<PC_Struct4<Integer, Integer, Integer, String>>, Integer> getButtonsLayout(
			Map<Integer, String> buttons, int minWidth, int distance) {

		int cursorX = 0;
		// button number, start x, width, text (translated)
		ArrayList<PC_Struct4<Integer, Integer, Integer, String>> layout = new ArrayList<PC_Struct4<Integer, Integer, Integer, String>>();

		for (Entry<Integer, String> a : buttons.entrySet()) {
			String label = PC_Lang.tr(a.getValue());
			int width = PC_Utils.mc().fontRenderer.getStringWidth(label);
			width += 16;
			width = Math.max(width, minWidth);

			layout.add(new PC_Struct4<Integer, Integer, Integer, String>(a.getKey(), cursorX, width, label));
			cursorX += width;
			cursorX += distance;
		}
		cursorX -= distance;

		return new PC_Struct2<ArrayList<PC_Struct4<Integer, Integer, Integer, String>>, Integer>(layout, cursorX);

	}



	/**
	 * Align set of buttons with given spaces to right, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param buttons Map of [ID ; translation key]
	 * @param minWidth minimal button width allowed
	 * @param distance distance between 2 buttons horizontally
	 * @param top top y position of the buttons
	 * @param right x position of the right side aligning to
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void alignToRight(List controllist, Map<Integer, String> buttons, int minWidth, int distance, int top, int right) {
		PC_Struct2<ArrayList<PC_Struct4<Integer, Integer, Integer, String>>, Integer> layoutset = getButtonsLayout(buttons, minWidth,
				distance);
		ArrayList<PC_Struct4<Integer, Integer, Integer, String>> buttonsList = layoutset.get1();
		int totalWidth = layoutset.get2();
		int xStart = right - totalWidth;
		int yStart = top;

		for (PC_Struct4<Integer, Integer, Integer, String> btn : buttonsList) {
			GuiButton but = new GuiButton(btn.a, xStart + btn.b, yStart, btn.d);
			but.width = btn.c;
			controllist.add(but);
		}
	}

	/**
	 * Align set of buttons with given spaces to left, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param buttons Map of [ID ; translation key]
	 * @param minWidth minimal button width allowed
	 * @param distance distance between 2 buttons horizontally
	 * @param top top y position of the buttons
	 * @param left x position of the left side aligning to
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void alignToLeft(List controllist, Map<Integer, String> buttons, int minWidth, int distance, int top, int left) {
		PC_Struct2<ArrayList<PC_Struct4<Integer, Integer, Integer, String>>, Integer> layoutset = getButtonsLayout(buttons, minWidth,
				distance);
		ArrayList<PC_Struct4<Integer, Integer, Integer, String>> buttonsList = layoutset.get1();
		int xStart = left;
		int yStart = top;

		for (PC_Struct4<Integer, Integer, Integer, String> btn : buttonsList) {
			GuiButton but = new GuiButton(btn.a, xStart + btn.b, yStart, btn.d);
			but.width = btn.c;
			controllist.add(but);
		}
	}

	/**
	 * Align set of buttons with given spaces to center, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param buttons Map of [ID ; translation key]
	 * @param minWidth minimal button width allowed
	 * @param distance distance between 2 buttons horizontally
	 * @param top top y position of the buttons
	 * @param center x position of the center
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void alignToCenter(List controllist, Map<Integer, String> buttons, int minWidth, int distance, int top, int center) {
		PC_Struct2<ArrayList<PC_Struct4<Integer, Integer, Integer, String>>, Integer> layoutset = getButtonsLayout(buttons, minWidth,
				distance);
		ArrayList<PC_Struct4<Integer, Integer, Integer, String>> buttonsList = layoutset.get1();
		int totalWidth = layoutset.get2();
		int xStart = center - totalWidth / 2;
		int yStart = top;

		for (PC_Struct4<Integer, Integer, Integer, String> btn : buttonsList) {
			GuiButton but = new GuiButton(btn.a, xStart + btn.b, yStart, btn.d);
			but.width = btn.c;
			controllist.add(but);
		}
	}



	/**
	 * Align set of buttons with given spaces to right, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param ID buton ID
	 * @param text button transl. key
	 * @param minWidth minimal button width allowed
	 * @param top top y position of the buttons
	 * @param right x position of the right side aligning to
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void alignSingleToRight(List controllist, int ID, String text, int minWidth, int top, int right) {

		HashMap<Integer, String> buttons = new HashMap<Integer, String>();
		buttons.put(ID, text);


		alignToRight(controllist, buttons, minWidth, 0, top, right);
	}

	/**
	 * Align set of buttons with given spaces to left, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param ID buton ID
	 * @param text button transl. key
	 * @param minWidth minimal button width allowed
	 * @param top top y position of the buttons
	 * @param left x position of the left side aligning to
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void alignSingleToLeft(List controllist, int ID, String text, int minWidth, int top, int left) {

		HashMap<Integer, String> buttons = new HashMap<Integer, String>();
		buttons.put(ID, text);


		alignToLeft(controllist, buttons, minWidth, 0, top, left);
	}

	/**
	 * Align set of buttons with given spaces to center, automatically adjusting their widths.
	 * 
	 * @param controllist Gui's control list
	 * @param ID buton ID
	 * @param text button transl. key
	 * @param minWidth minimal button width allowed
	 * @param top top y position of the buttons
	 * @param center x position of the center
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void alignSingleToCenter(List controllist, int ID, String text, int minWidth, int top, int center) {

		HashMap<Integer, String> buttons = new HashMap<Integer, String>();
		buttons.put(ID, text);

		alignToCenter(controllist, buttons, minWidth, 0, top, center);
	}

}
