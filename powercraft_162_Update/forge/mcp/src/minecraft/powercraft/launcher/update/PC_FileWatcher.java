package powercraft.launcher.update;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PC_FileWatcher {

	private File file;
	private HashMap<String, Long> files = new HashMap<String, Long>();
	
	public PC_FileWatcher(File file){
		this.file = file;
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++){
			this.files.put(files[i].getName(), files[i].lastModified());
		}
	}
	
	public List<WatchEvent> pollEvents(){
		List<WatchEvent> list = new ArrayList<WatchEvent>();
		List<String> filesNotDone = new ArrayList<String>(files.keySet());
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++){
			String name = files[i].getName();
			long lastModified =  files[i].lastModified();
			if(filesNotDone.contains(name)){
				filesNotDone.remove(name);
				if(this.files.get(name)!=lastModified){
					list.add(new WatchEvent(files[i], Event.MODIFY));
					this.files.put(name, lastModified);
				}
			}else{
				list.add(new WatchEvent(files[i], Event.CREATE));
				this.files.put(name, lastModified);
			}
		}
		for(String fileName:filesNotDone){
			list.add(new WatchEvent(new File(file, fileName), Event.DELETE));
		}
		return list;
	}
	
	
	public static class WatchEvent{
		public File file;
		public Event event;
		public WatchEvent(){}
		public WatchEvent(File file, Event event){
			this.file = file;
			this.event = event;
		}
	}
	
	public static enum Event{
		CREATE,
		DELETE,
		MODIFY
	}
	
}
