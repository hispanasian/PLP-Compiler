package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ReturnStatement extends Statement {
	Expression expression;


	public ReturnStatement(Token firstToken, Expression expression) {
		super(firstToken);
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitReturnStatement(this,arg);
	}

}
