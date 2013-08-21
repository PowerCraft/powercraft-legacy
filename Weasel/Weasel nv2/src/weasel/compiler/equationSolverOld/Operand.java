package weasel.compiler.equationSolverOld;

import java.awt.Color;


public class Operand {
	private String name;
	private double value;

	protected Operand() {
	}

	public Operand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)

	public String toString() {
		return name;
	}
	
	public String toEncryptedString() {
		return name;
	}

	public String toClassView() {
		return this.getClass().getName();
	}
	
public Operand simplify() {
		return this;	
	}
}
