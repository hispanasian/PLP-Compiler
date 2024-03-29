package cop5555sp15.ast;

import cop5555sp15.TokenStream.Token;

public class ListType extends Type {
	Type type;
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitListType(this,arg);
	}
	public ListType(Token firstToken, Type type) {
		super(firstToken);
		this.type = type;
	}
	@Override
	public String getJVMType() {
		String elementType = type.getJVMType();
		if(elementType.equals("I")) elementType = "Ljava/lang/Integer;";
		else if(elementType.equals("Z")) elementType = "Ljava/lang/Boolean;";
		return "Ljava/util/List<"+elementType+">;";
	}

	@Override
	public String getDesc() {
		return "Ljava/util/List;";
	}

	@Override
	public String getSignature() {
		return getJVMType();
	}

	public String getElementType() { return type.getJVMType(); }
	
	public static String prefix(){
		return "Ljava/util/List";
	}

}
