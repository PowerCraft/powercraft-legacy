package powercraftCombi;

@WeaselClassMarker(weaselName="TestKlasse")
public class WeaselKlasse {

	public void a(){
		
	}
	
	@WeaselClassMarker.Invisible
	public void b(){
		
	}
	
	@Named(weaselNames="doIt", namespaces="World")
	public void c(){
		
	}
}
