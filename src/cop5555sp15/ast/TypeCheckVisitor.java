package cop5555sp15.ast;

import cop5555sp15.TokenStream;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TypeConstants;
import cop5555sp15.symbolTable.SymbolTable;

public class TypeCheckVisitor implements ASTVisitor, TypeConstants {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		ASTNode node;

		public TypeCheckException(String message, ASTNode node) {
			super(node.firstToken.lineNumber + ":" + message);
			this.node = node;
		}
	}

	SymbolTable symbolTable;

	public TypeCheckVisitor(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	boolean check(boolean condition, String message, ASTNode node)
			throws TypeCheckException {
		if (condition)
			return true;
		throw new TypeCheckException(message, node);
	}

	/**
	 * Ensure that types on left and right hand side are compatible.
	 */
	@Override
	public Object visitAssignmentStatement(
			AssignmentStatement assignmentStatement, Object arg)
			throws Exception {
		LValue lval = assignmentStatement.lvalue;
		Expression exp = assignmentStatement.expression;

		Declaration ldec = (Declaration)lval.visit(this, arg); // Ensure lval exists
		exp.visit(this, arg); // Determine type of expression

		// Time to find the type
		if(ldec instanceof VarDec)
		{
			Type ltype = ((VarDec) ldec).type;
			if(ltype instanceof UndeclaredType)
			{ // Infer type from expression
				throw new UnsupportedOperationException("not yet implemented");
			}
			// Type check
			else check(ltype.getJVMType().equals(exp.getType()),
						"cannot assign expression to a variable of different type", assignmentStatement);
		}
		else if(ldec instanceof ClosureDec)
		{
			throw new UnsupportedOperationException("not yet implemented");
		}
		return null;
	}

	/**
	 * Ensure that both types are the same, save and return the result type
	 */
	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		// First, find the types of the sub expressions
		String e0 = (String)binaryExpression.expression0.visit(this, arg);
		String e1 = (String)binaryExpression.expression1.visit(this, arg);
		check(e0.equals(e1), "expressions are of different types", binaryExpression);

		// Now, verify that the correct op is being used
		if(e0.equals(intType))
		{
			if(binaryExpression.op.kind == Kind.PLUS ||
					binaryExpression.op.kind == Kind.MINUS ||
					binaryExpression.op.kind == Kind.TIMES ||
					binaryExpression.op.kind == Kind.DIV)
			{
				binaryExpression.setType(intType);
			}
			else if (binaryExpression.op.kind == Kind.EQUAL ||
					binaryExpression.op.kind == Kind.NOTEQUAL ||
					binaryExpression.op.kind == Kind.LT ||
					binaryExpression.op.kind == Kind.LE ||
					binaryExpression.op.kind == Kind.GT ||
					binaryExpression.op.kind == Kind.GE )
			{
				binaryExpression.setType(booleanType);
			}
			else check(false,"two intLit expressions can only use +, -, *, /, ==, !=, <, <=, >=, or > as operands", binaryExpression);
		}
		else if(e0.equals(booleanType))
		{
			check(binaryExpression.op.kind == Kind.EQUAL ||
					binaryExpression.op.kind == Kind.NOTEQUAL ||
					binaryExpression.op.kind == Kind.AND ||
					binaryExpression.op.kind == Kind.BAR,
					"two booleanLit expression can only use ==, !=, &, or | as operands", binaryExpression);
			binaryExpression.setType(booleanType);
		}
		else if(e0.equals(stringType))
		{
			if(binaryExpression.op.kind == Kind.PLUS) binaryExpression.setType(stringType);
			else if(binaryExpression.op.kind == Kind.EQUAL || binaryExpression.op.kind == Kind.NOTEQUAL
					)
			{
				binaryExpression.setType(booleanType);
			}
			else check(false,"two stringLit expressions can only use +, ==, or != as operands", binaryExpression);
		}
		else check(false, "expression is an unknown type", binaryExpression);

		check(binaryExpression.expressionType.equals(intType) ||
						binaryExpression.expressionType.equals(booleanType) ||
						binaryExpression.expressionType.equals(stringType),
				"the expression type cannot be determined", binaryExpression);

		return binaryExpression.expressionType;
	}

	/**
	 * Blocks define scopes. Check that the scope nesting level is the same at
	 * the end as at the beginning of block
	 */
	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		int numScopes = symbolTable.enterScope();
		// visit children
		for (BlockElem elem : block.elems) {
			elem.visit(this, arg);
		}
		int numScopesExit = symbolTable.leaveScope();
		check(numScopesExit > 0 && numScopesExit == numScopes,
				"unbalanced scopes", block);
		return null;
	}

	/**
	 * Sets the expressionType to booleanType and returns it
	 * 
	 * @param booleanLitExpression
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object visitBooleanLitExpression(
			BooleanLitExpression booleanLitExpression, Object arg)
			throws Exception {
		booleanLitExpression.setType(booleanType);
		return booleanType;
	}

	/**
	 * A closure defines a new scope Visit all the declarations in the
	 * formalArgList, and all the statements in the statementList construct and
	 * set the JVMType, the argType array, and the result type
	 * 
	 * @param closure
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object visitClosure(Closure closure, Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Make sure that the name has not already been declared and insert in
	 * symbol table. Visit the closure
	 */
	@Override
	public Object visitClosureDec(ClosureDec closureDec, Object arg) {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Check that the given name is declared as a closure Check the argument
	 * types The type is the return type of the closure
	 */
	@Override
	public Object visitClosureEvalExpression(
			ClosureEvalExpression closureExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitClosureExpression(ClosureExpression closureExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitExpressionStatement(
			ExpressionStatement expressionStatement, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Check that name has been declared in scope Get its type from the
	 * declaration.
	 * @return returns the JVM type of this expression
	 */
	@Override
	public Object visitIdentExpression(IdentExpression identExpression,
			Object arg) throws Exception {
		// Check that ident exists
		Declaration dec = symbolTable.lookup(identExpression.firstToken.getText());
		check(dec != null, "cannot use undeclared variable", identExpression);

		// Time to find the type
		if(dec instanceof VarDec)
		{
			Type type = ((VarDec) dec).type;
			check(type.getJVMType() != null, "variable type cannot be determined or" +
					" variable was never assigned a value", identExpression);
			identExpression.setType(type.getJVMType());
		}
		else if(dec instanceof ClosureDec)
		{
			throw new UnsupportedOperationException("not yet implemented");
		}
		return identExpression.getType();
	}

	@Override
	/**
	 * @return	Returns the declaration of this value
	 */
	public Object visitIdentLValue(IdentLValue identLValue, Object arg)
			throws Exception {
		Declaration dec = symbolTable.lookup(identLValue.firstToken.getText());
		check(dec != null, "cannot use undeclared variable", identLValue);

		// Time to find the type
		if(dec instanceof VarDec)
		{
			Type type = ((VarDec) dec).type;
			identLValue.setType(type.getJVMType());
		}
		else if(dec instanceof ClosureDec)
		{
			throw new UnsupportedOperationException("not yet implemented");
		}
		return dec;
	}

	@Override
	public Object visitIfElseStatement(IfElseStatement ifElseStatement,
			Object arg) throws Exception {
		String etype = (String)ifElseStatement.expression.visit(this, arg);
		check(etype.equals(booleanType),
				"if expects an expression that evaluates to a boolean",
				ifElseStatement);

		ifElseStatement.ifBlock.visit(this, arg);
		ifElseStatement.elseBlock.visit(this, arg);
		return null;
	}

	/**
	 * expression type is boolean
	 */
	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg)
			throws Exception {
		String etype = (String)ifStatement.expression.visit(this, arg);
		check(etype.equals(booleanType),
				"if expects an expression that evaluates to a boolean",
				ifStatement);

		ifStatement.block.visit(this, arg);
		return null;
	}

	/**
	 * expression type is int
	 */
	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression,
			Object arg) throws Exception {
		intLitExpression.setType(intType);
		return intType;
	}

	@Override
	public Object visitKeyExpression(KeyExpression keyExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitKeyValueExpression(
			KeyValueExpression keyValueExpression, Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitKeyValueType(KeyValueType keyValueType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	// visit the expressions (children) and ensure they are the same type
	// the return type is "Ljava/util/ArrayList<"+type0+">;" where type0 is the
	// type of elements in the list
	// this should handle lists of lists, and empty list. An empty list is
	// indicated by "Ljava/util/ArrayList;".
	@Override
	public Object visitListExpression(ListExpression listExpression, Object arg)
			throws Exception {

		// Find the type of the expressions in the list and ensure they are all the same
		String type = "";
		if(listExpression.expressionList.size() > 0)
		{
			type = listExpression.expressionList.get(0).getType();
			for(int i = 1; i < listExpression.expressionList.size(); i++)
			{
				check(!type.equals(listExpression.expressionList.get(i).getType()),
				"Not all expressions in the list are of the same type",
				listExpression);
			}
		}

		// Check that ident exists and that it is a VarDec
		Declaration dec = symbolTable.lookup(listExpression.firstToken.getText());
		check(dec != null, "cannot use undeclared variable", listExpression);

		check(dec instanceof VarDec,
				"cannot assign a list to a Closure",
				listExpression);
		Type dectype = ((VarDec)dec).type;

		// If the list is not empty, verify that it is of the same type as the ident
		if(type.equals("")) type = emptyList;
		else
		{
			// First, check if we are using int or boolean. If so, change their types to their
			// object counterparts
			if(type.equals(intType)) type = intObjectType;
			else if(type.equals(booleanType)) type = booleanObjectType;

			// Now verify the type of the expression and the type of the ident
			type = "Ljava/util/ArrayList<"+type+">;";
			check(type.equals(dectype.getJVMType()),
					"",
					listExpression);
			listExpression.setType(type);
		}

		return type;
	}

	/** gets the type from the enclosed expression */
	@Override
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitListType(ListType listType, Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitMapListExpression(MapListExpression mapListExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitPrintStatement(PrintStatement printStatement, Object arg)
			throws Exception {
		printStatement.expression.visit(this, null);
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		if (arg == null) {
			program.JVMName = program.name;
		} else {
			program.JVMName = arg + "/" + program.name;
		}
		// ignore the import statement
		if (!symbolTable.insert(program.name, null)) {
			throw new TypeCheckException("name already in symbol table",
					program);
		}
		program.block.visit(this, true);
		return null;
	}

	@Override
	public Object visitQualifiedName(QualifiedName qualifiedName, Object arg) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Checks that both expressions have type int.
	 * 
	 * Note that in spite of the name, this is not in the Expression type
	 * hierarchy.
	 */
	@Override
	public Object visitRangeExpression(RangeExpression rangeExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	// nothing to do here
	@Override
	public Object visitReturnStatement(ReturnStatement returnStatement,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	/**
	 * Size takes in a List, hence the type of the expression must be of Ljava/util/List
	 */
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg)
			throws Exception {
		check(sizeExpression.getType().contains("Ljava/util/List"),
				"Size() must be given a list",
				sizeExpression);
		return intType;
	}

	@Override
	public Object visitStringLitExpression(
			StringLitExpression stringLitExpression, Object arg)
			throws Exception {
		stringLitExpression.setType(stringType);
		return stringType;
	}

	/**
	 * if ! and boolean, then boolean else if - and int, then int else error
	 */
	@Override
	public Object visitUnaryExpression(UnaryExpression unaryExpression,
			Object arg) throws Exception {
		String etype = (String)unaryExpression.expression.visit(this, arg);

		switch(unaryExpression.op.kind)
		{
			case MINUS:
				check(etype.equals(intType), "Expected an int to be prefixed by -", unaryExpression);
				unaryExpression.setType(intType);
				break;
			case NOT:
				check(etype.equals(booleanType), "Expected a boolean to be prefixed by !", unaryExpression);
				unaryExpression.setType(booleanType);
				break;
			default:
				// This should never happen
				check(unaryExpression.op.kind != Kind.MINUS || unaryExpression.op.kind != Kind.NOT,
						"Unary expression found prefixed with a token that was not ! or -",
						unaryExpression);
				break;
		}
		return unaryExpression.getType();
	}

	@Override
	public Object visitUndeclaredType(UndeclaredType undeclaredType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"undeclared types not supported");
	}

	@Override
	public Object visitValueExpression(ValueExpression valueExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * check that this variable has not already been declared in the same scope.
	 */
	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws Exception {
		check(symbolTable.insert(varDec.identToken.getText(), varDec),
				"cannot re-declare variable in the same scope", varDec);
		return null;
	}

	/**
	 * All checking will be done in the children since grammar ensures that the
	 * rangeExpression is a rangeExpression.
	 */
	@Override
	public Object visitWhileRangeStatement(
			WhileRangeStatement whileRangeStatement, Object arg)
			throws Exception {
		throw new UnsupportedOperationException("not yet implemented");

	}

	@Override
	public Object visitWhileStarStatement(
			WhileStarStatement whileStarStatement, Object arg) throws Exception {
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg)
			throws Exception {
		String etype = (String)whileStatement.expression.visit(this, arg);
		check(etype.equals(booleanType),
				"while expects an expression that evaluates to a boolean",
				whileStatement);
		whileStatement.block.visit(this, arg);
		return null;
	}

}
