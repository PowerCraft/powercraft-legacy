package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Outer.Level1a;
import weasel.compiler.equationSolverNew.Outer.Level1b.Level2bN;
import weasel.compiler.equationSolverNew.Outer.Level1b.Level2bS;

public class WeaselTokenFunction implements IWeaselTokenTreeElement {
	
	private final String name;
	private final WeaselToken generics[];
	
	public WeaselTokenFunction(String name, WeaselToken... gens){
		this.name = name;
		generics = gens;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	private <T> List<T> instance(){
		
		return new ArrayList<T>();
	}

	@Override
	public void addSub(IWeaselTokenTreeElement... te) {
		subs.addAll(Arrays.asList(te));
	}

	@Override
	public String toEncryptedString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toClassView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeaselToken simplify() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void test(){
		Outer o = new Outer();
		o.new Level1a();
		new Outer().new Level1a().do1();
		new Outer.Level1b().do2();
		new Outer.Level1b().new Level2bN();
		new Outer.Level1b.Level2bS();
		
		Level1a a[] = new Outer.Level1a[12];
		new Outer.Level1b().do2();
		Level2bN b[] = new Outer.Level1b.Level2bN[]{new Outer.Level1b().new Level2bN()};
		Level2bS c[] = new Outer.Level1b.Level2bS[12];
	}
}

class Outer{
	public class Level1a{
		public class Level2aN{
			
		}
		public void do1(){
			
		}
	}
	
	public static class Level1b{
		public class Level2bN{
			
		}
		public static class Level2bS{
			
		}
		public void do2(){
			
		}
	}
}
