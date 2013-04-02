package powercraft.launcher.updategui;

import javax.swing.JFileChooser;

import powercraft.launcher.update.PC_UpdateManager;

public class PC_FileRequestThread extends Thread {
	
	public PC_FileRequestThread() {
		setDaemon(true);
		start();
	}
	
	@Override
	public void run() {
		JFileChooser chooser = new JFileChooser(PC_UpdateManager.downloadTarget);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showDialog(null, "Select Download Directory");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			PC_UpdateManager.setDownloadTarget(chooser.getSelectedFile());
		}
	}
	
}
