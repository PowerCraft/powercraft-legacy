/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package powercraft.weasel.jep.function;


import java.util.Stack;

import org.lwjgl.util.Color;

import powercraft.weasel.jep.ParseException;



/**
 * Converts an object into its numeric representation.
 * 
 * @author MightyPork
 */
public class MakeColor extends PostfixMathCommand {
	int type = 0;
	public MakeColor(int rgbHsv) {
		numberOfParameters = 3;
		this.type = rgbHsv;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		
		Object p3 = inStack.pop();
		Object p2 = inStack.pop();
		Object p1 = inStack.pop();

		try {
			int i1 = ((Number)p1).intValue();
			int i2 = ((Number)p2).intValue();
			int i3 = ((Number)p3).intValue();
			
			if(type==0)	inStack.push(clr(i1,i2,i3));
			if(type==1) {
				Color cc = new Color();
				cc.fromHSB(i1/255F,i2/255F,i3/255F);	
				inStack.push(clr(cc.getRed(),cc.getGreen(),cc.getBlue()));
			}
			
		}catch(ClassCastException c) {
			throw new ParseException("Cant make color of "+p1+","+p2+","+p3+".");
		}
		return;
	}
	
	private int clr(int r, int g, int b) {
		return 
					Math.round(Math.min(255,Math.max(0,r))) << 16 |
					Math.round(Math.min(255,Math.max(0,g))) << 8 |
					Math.round(Math.min(255,Math.max(0,b)));
	}
}
