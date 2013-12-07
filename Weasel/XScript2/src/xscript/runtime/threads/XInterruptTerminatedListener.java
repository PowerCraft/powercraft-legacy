package xscript.runtime.threads;

import xscript.runtime.XVirtualMachine;

public interface XInterruptTerminatedListener {

	public void onInterruptTerminated(XVirtualMachine virtualMachine, XThread interrupt);

}
