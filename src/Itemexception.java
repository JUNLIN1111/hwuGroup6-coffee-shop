
public class Itemexception extends Exception {
	public Itemexception(){
		super("This Order is invalid.");
	}
	
	public Itemexception(String message){
		super(message);
	}
}

class IllegalDescriptionException extends Itemexception{
	public IllegalDescriptionException(String message){
		super(message);
	}
}

class IllegalCostException extends Itemexception{
	public IllegalCostException(String message){
		super(message);
	}
}

class IllegalCategoryException extends Itemexception{
	public IllegalCategoryException(String message){
		super(message);
	}
}

class IllegalItemIdException extends Itemexception{
	public IllegalItemIdException(String message){
		super(message);
	}
}
