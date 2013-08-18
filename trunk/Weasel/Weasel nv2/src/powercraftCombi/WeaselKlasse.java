package powercraftCombi;

@WeaselClass(weaselName="TestKlasse")
public class WeaselKlasse {

	public void a(){
		
	}
	
	@WeaselClass.Invisible
	public void b(){
		
	}
	
	@ExternalWeaselFunction(weaselNames="doIt", namespaces="World")
	public void c(){
		
	}
}
