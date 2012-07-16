/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package weasel.jep.function;


import java.util.Calendar;
import java.util.Stack;

import net.minecraft.src.ModLoader;

import weasel.Calc;
import weasel.jep.ParseException;
import weasel.obj.WeaselInteger;


/**
 * mc time func
 */
public class TimeFunc extends PostfixMathCommand {
	
	private TimeFuncType type;
	
	public enum TimeFuncType{
		H,M,S,RH,RM,RS,SECS_ALL,SECS_ALL_R,MOON,FTIME,FTIME_R;
	}
	
	public TimeFunc(TimeFuncType type) {
		numberOfParameters = 0;
		this.type = type;
	}
	
	private int getMcTimeSecs() {
		try{
			long time = ModLoader.getMinecraftInstance().theWorld.getWorldInfo().getWorldTime();			
		
			time += 6000;			
			time = time%24000;
		
			double reals = time / (24000F / 86400F);
			return (int) Math.round(reals);
		}catch(Throwable t) {
			return -1;
		}
	}
	
	private int getMoon() {
		try{
			return ModLoader.getMinecraftInstance().theWorld.getMoonPhase(0);
		}catch(Throwable t) {
			return -1;
		}		
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		
		int num = 0;
		
		Calendar date = Calendar.getInstance();
		
		int mctime = getMcTimeSecs();
		
		switch(type) {
			case H:
				num = (int) Math.floor(mctime/3600F);
				break;
			case M:
				num = (int) Math.floor((mctime%3600F)/60);
				break;
			case S:
				num = (int) Math.floor(mctime%60F);
				break;
			case RH:
				num = date.get(Calendar.HOUR);
				break;
			case RM:
				num = date.get(Calendar.MINUTE);
				break;
			case RS:
				num = date.get(Calendar.SECOND);
				break;
			case SECS_ALL:
				num = mctime;
				break;
			case SECS_ALL_R:
				num = date.get(Calendar.HOUR)*3600+date.get(Calendar.MINUTE)*60+date.get(Calendar.SECOND);
				break;
			case MOON:
				num = getMoon();
				break;
				
			case FTIME:
				String a = "";				
				String b = "";
				
				b = (int)Math.floor(mctime/3600F)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				a += ":";
				
				b = (int)Math.floor((mctime%3600F)/60)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				a += ":";
				
				b = (int)Math.floor(mctime%60F)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				
				inStack.push(a);
				return;
				
			case FTIME_R:
				
				a = "";				
				b = "";
				
				b = date.get(Calendar.HOUR)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				a += ":";
				
				b = date.get(Calendar.MINUTE)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				a += ":";
				
				b = date.get(Calendar.SECOND)+"";
				if(b.length()==1) b = "0"+b;
				a += b;
				
				inStack.push(a);
				return;
				
		}
		
		inStack.push(num); //push the result on the inStack
		return;
	}
}
