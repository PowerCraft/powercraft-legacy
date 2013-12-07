package xscript.runtime.threads;

import java.util.ArrayList;
import java.util.List;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.method.XMethod;

public class XThreadProvider {

	private XVirtualMachine virtualMachine;
	private List<XThread> threads = new ArrayList<XThread>();
	private List<XThread> interrupts = new ArrayList<XThread>();
	private List<XInterruptTerminatedListener> interruptTerminatedListeners = new ArrayList<XInterruptTerminatedListener>();
	private int nextThreadID=1;
	private int activeThreadID;
	
	public XThreadProvider(XVirtualMachine virtualMachine){
		this.virtualMachine = virtualMachine;
	}
	
	public void markVisible() {
		for(XThread thread:threads){
			thread.markVisible();
		}
		for(XThread thread:interrupts){
			thread.markVisible();
		}
	}

	private XThread getNextInterrupt(){
		int index = 0;
		XThread interrupt;
		do{
			if(index>=interrupts.size())
				return null;
			interrupt = interrupts.get(index);
			interrupt.sleepUpdate();
			if(interrupt.getThreadState()==XThreadState.ERRORED || interrupt.getThreadState()==XThreadState.TERMINATED){
				interrupts.remove(index);
				for(XInterruptTerminatedListener interruptTerminatedListener:interruptTerminatedListeners){
					interruptTerminatedListener.onInterruptTerminated(virtualMachine, interrupt);
				}
				continue;
			}
			index++;
		}while(interrupt.getThreadState()!=XThreadState.RUNNING);
		return interrupt;
	}
	
	private XThread getNextThread(){
		XThread thread = threads.get(activeThreadID);
		thread.sleepUpdate();
		if(thread.getThreadState()!=XThreadState.RUNNING){
			if(thread.getThreadState()==XThreadState.ERRORED || thread.getThreadState()==XThreadState.TERMINATED){
				threads.remove(activeThreadID);
				activeThreadID--;
			}
			int startThread = activeThreadID;
			do{
				activeThreadID++;
				if(activeThreadID>=threads.size()){
					activeThreadID = 0;
				}
				if(activeThreadID==startThread)
					return null;
				thread = threads.get(activeThreadID);
				thread.sleepUpdate();
				if(thread.getThreadState()==XThreadState.ERRORED || thread.getThreadState()==XThreadState.TERMINATED){
					threads.remove(activeThreadID);
					if(activeThreadID<startThread){
						startThread--;
					}
					activeThreadID--;
				}
			}while(thread.getThreadState()==XThreadState.RUNNING);
		}
		return thread;
	}
	
	public int run(int numInstructions){
		XThread thread;
		while(numInstructions>0){
			thread = getNextInterrupt();
			if(thread==null){
				thread = getNextThread();
			}
			if(thread==null){
				return numInstructions;
			}
			thread.run();
			numInstructions--;
		}
		return 0;
	}
	
	public void start(String name, XMethod method, XGenericClass[] generics, long[] params) {
		XThread thread = new XThread(virtualMachine, name, method, generics, params);
		threads.add(thread);
	}
	
	public void interrupt(String name, byte[] userData, XMethod method, XGenericClass[] generics, long[] params){
		XThread interrupt = new XThread(virtualMachine, name, method, generics, params);
		interrupts.add(interrupt);
		interrupt.setUserData(userData);
	}
	
	public void importantInterrupt(String name, XMethod method, XGenericClass[] generics, long[] params){
		XThread interrupt = new XThread(virtualMachine, name, method, generics, params);
		interrupts.add(0, interrupt);
	}
	
	public String getNextDefaultThreadName() {
		return "Thread-"+nextThreadID++;
	}
	
}
