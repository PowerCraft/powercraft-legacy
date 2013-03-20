package powercraft.launcher.updategui;

import java.io.File;

import javax.swing.JFileChooser;

import powercraft.launcher.PC_Launcher;
import powercraft.launcher.update.PC_UpdateManager;

public class PC_FileRequestThread extends Thread {
	
	public PC_FileRequestThread(){
		setDaemon(true);
		start();
	}
	
	@Override
	public void run(){
		JFileChooser chooser = new JFileChooser(PC_UpdateManager.downloadTarget);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showDialog(null, "Select directory of downloaded files");
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			PC_UpdateManager.setDownloadTarget(chooser.getSelectedFile());
		}
	}
	
}
