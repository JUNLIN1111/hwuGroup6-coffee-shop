
public class InvalidOrderException extends Exception {
	public InvalidOrderException(){
		super("This Order is invalid.");
	}
	
	public InvalidOrderException(String message){
		super(message);
	}
}

class InvalidOrderIdException extends InvalidOrderException{
	public InvalidOrderIdException(String message){
		super(message);
	}
}

class InvalidOrderTimeStampException extends InvalidOrderException{
	public InvalidOrderTimeStampException(String message){
		super(message);
	}
}

class IllegalitemListException extends InvalidOrderException{
	public IllegalitemListException(String message){
		super(message);
	}
}
