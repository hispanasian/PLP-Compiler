package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public abstract class Type extends ASTNode {

	Type(Token firstToken) {
		super(firstToken);
	}
	
	abstract String getJVMType();

	abstract String getDesc();

	abstract String getSignature();

}
