package codieval.exceptions;

public class CompilationErrorException extends Exception {
	private String message;
	public CompilationErrorException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
