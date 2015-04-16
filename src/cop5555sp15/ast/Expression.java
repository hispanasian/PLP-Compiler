package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class Expression extends ASTNode {
	
	String expressionType = null;
	String descriptor = null;

	public void setFields(String type, String desc)
	{
		setType(type);
		setDesc(desc);
	}

	public String getType() {
		return expressionType;
	}

	public void setType(String type) {
		this.expressionType = type;
	}

	public String getDesc() {
		if(descriptor == null) return expressionType;
		else return descriptor;
	}

	public void setDesc(String desc) { this.descriptor = desc; }

	Expression(Token firstToken) {
		super(firstToken);
	}

}
