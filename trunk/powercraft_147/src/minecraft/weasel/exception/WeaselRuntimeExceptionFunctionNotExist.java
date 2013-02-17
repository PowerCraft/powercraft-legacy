package weasel.exception;

public class WeaselRuntimeExceptionFunctionNotExist extends WeaselRuntimeException {

	public WeaselRuntimeExceptionFunctionNotExist(String function) {
		super("Function \""+function+"()\" does not exist");
	}

}
