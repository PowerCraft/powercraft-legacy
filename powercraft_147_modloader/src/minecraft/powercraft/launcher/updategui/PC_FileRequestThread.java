package powercraft.launcher.updategui;

import java.io.File;

import javax.swing.JFileChooser;

public class PC_FileRequestThread extends Thread {

	private PC_GuiUpdate gui;
	private File downloadTarget;
	
	public PC_FileRequestThread(PC_GuiUpdate gui, File downloadTarget){
		this.gui = gui;
		this.downloadTarget = downloadTarget;
		setDaemon(true);
		start();
	}
	
	@Override
	public void run(){
		JFileChooser chooser = new JFileChooser(downloadTarget);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showDialog(null, "Select Download Directory");
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			gui.setDownloadTarget(chooser.getSelectedFile());
		}
	}
	
}
