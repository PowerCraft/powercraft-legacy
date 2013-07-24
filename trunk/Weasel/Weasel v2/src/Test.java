
public class Test {

	public int t;
	
	public Test(int i) {
		t = i;
	}
	
	public static class Test2 extends Test{

		public int t;
		
		public Test2(int i, int j) {
			super(i);
			t=j;
		}
		
	}
	
	
	public static void main(String[] args){
		Test t = new Test2(1, 2);
		System.out.println(t.t);
	}
	
}
