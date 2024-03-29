package cop5555sp15.ast;

import org.objectweb.asm.*;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TypeConstants;

import java.util.List;

final class ExpArgPair {
	Object arg;
	Expression expression;

	public ExpArgPair(Object arg, Expression expression) {
		this.arg = arg;
		this.expression = expression;
	}
}

public class CodeGenVisitor implements ASTVisitor, Opcodes, TypeConstants {

	ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
	// Because we used the COMPUTE_FRAMES flag, we do not need to
	// insert the mv.visitFrame calls that you will see in some of the
	// asmifier examples. ASM will insert those for us.
	// FYI, the purpose of those instructions is to provide information
	// about what is on the stack just before each branch target in order
	// to speed up class verification.
	FieldVisitor fv;
	String className;
	String classDescriptor;

	// This class holds all attributes that need to be passed downwards as the
	// AST is traversed. Initially, it only holds the current MethodVisitor.
	// Later, we may add more attributes.
	static class InheritedAttributes {
		public InheritedAttributes(MethodVisitor mv) {
			super();
			this.mv = mv;
		}

		MethodVisitor mv;
	}

	@Override
	/**
	 * Puts the value from expression into lvalue.
	 * TODO: This method currently treats all variables as global to one object. Fix this
	 */
	public Object visitAssignmentStatement(
			AssignmentStatement assignmentStatement, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		ExpArgPair pair = new ExpArgPair(arg, assignmentStatement.expression);


		// Make the stack look like:
		// bottom:[ .. | this(object holding lvalue) ]:top
		mv.visitVarInsn(ALOAD, 0);

		// Now our stack looks like what we want
		assignmentStatement.lvalue.visit(this, pair); // Load lvalue to the top of the stack
		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;

		// Note, we are computing the return type based on the return type of the expression
		if(binaryExpression.getType().equals(intType))
		{
			// First, visit the sub expressions and put the result of e1 on the top of the stack and
			// the result of d0 under it.
			binaryExpression.expression0.visit(this, arg); // load 0
			binaryExpression.expression1.visit(this, arg); // load 1

			switch(binaryExpression.op.kind)
			{
				case PLUS:
					mv.visitInsn(IADD);
					break;
				case MINUS:
					mv.visitInsn(ISUB);
					break;
				case TIMES:
					mv.visitInsn(IMUL);
					break;
				case DIV:
					mv.visitInsn(IDIV);
					break;
			}
		}
		else if(binaryExpression.getType().equals(stringType))
		{
			switch(binaryExpression.op.kind)
			{
				case PLUS:
					// Use StribgBuilder to concatenate the two strings.
					mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
					mv.visitInsn(DUP);
					binaryExpression.expression0.visit(this, arg); // We first want expression0, Load 0
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
					mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");

					binaryExpression.expression1.visit(this, arg); // load 1
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
					break;
			}
		}
		else if(binaryExpression.getType().equals(booleanType))
		{
			if(binaryExpression.op.kind == Kind.AND || binaryExpression.op.kind == Kind.BAR)
			{
				switch(binaryExpression.op.kind) {
						case AND:
					{
						binaryExpression.expression0.visit(this, arg); // load0
						Label l1 = new Label();
						mv.visitJumpInsn(IFEQ, l1);
						binaryExpression.expression1.visit(this, arg); // load 1
						mv.visitJumpInsn(IFEQ, l1);
						mv.visitInsn(ICONST_1);
						Label l2 = new Label();
						mv.visitJumpInsn(GOTO, l2);
						mv.visitLabel(l1);
						mv.visitInsn(ICONST_0);
						mv.visitLabel(l2);
					}
					break;
					case BAR: {
						binaryExpression.expression0.visit(this, arg); // load 0
						Label l1 = new Label();
						mv.visitJumpInsn(IFNE, l1);
						binaryExpression.expression1.visit(this, arg); // load 1
						mv.visitJumpInsn(IFNE, l1);
						mv.visitInsn(ICONST_0);
						Label l2 = new Label();
						mv.visitJumpInsn(GOTO, l2);
						mv.visitLabel(l1);
						mv.visitInsn(ICONST_1);
						mv.visitLabel(l2);
					}
					break;
				}
			}
			// The boolean/relational cases
			else if(binaryExpression.op.kind == Kind.EQUAL ||
					binaryExpression.op.kind == Kind.NOTEQUAL||
					binaryExpression.op.kind == Kind.LT ||
					binaryExpression.op.kind == Kind.LE ||
					binaryExpression.op.kind == Kind.GT ||
					binaryExpression.op.kind == Kind.GE)
			{
				/**
				 * The following code is effectively:
				 * L0
				 *  load1
				 *  load2
				 *  IF_THECOMPARISON L1
				 *  ICONST_0:false
				 *  GOTO L2
				 * L1
				 *  ICONST_1:true
				 * L2
				 */

				// First, visit the sub expressions and put the result of e1 on the top of the stack and
				// the result of d0 under it.
				binaryExpression.expression0.visit(this, arg); // load 0
				binaryExpression.expression1.visit(this, arg); // load 1

				// Now for the asm
				Label l1 = new Label();
				int op = 0;
				switch(binaryExpression.op.kind)
				{
					case EQUAL:
						if(binaryExpression.expression0.getType().equals(intType)) op = IF_ICMPEQ;
						if(binaryExpression.expression0.getType().equals(booleanType)) op = IF_ICMPEQ;
						else if(binaryExpression.expression0.getType().equals(stringType)) op = IF_ACMPEQ;
						break;
					case NOTEQUAL:
						if(binaryExpression.expression0.getType().equals(intType)) op = IF_ICMPNE;
						if(binaryExpression.expression0.getType().equals(booleanType)) op = IF_ICMPNE;
						else if(binaryExpression.expression0.getType().equals(stringType)) op = IF_ACMPNE;
						break;

					// Note that only ints can do the following operations
					case LT: op = IF_ICMPLT;
						break;
					case LE: op = IF_ICMPLE;
						break;
					case GT: op = IF_ICMPGT;
						break;
					case GE: op = IF_ICMPGE;
						break;
				}
				mv.visitJumpInsn(op, l1);
				mv.visitInsn(ICONST_0);
				Label l2 = new Label();
				mv.visitJumpInsn(GOTO, l2);
				mv.visitLabel(l1);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(l2);
			}
		}
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		for (BlockElem elem : block.elems) {
			elem.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(
			BooleanLitExpression booleanLitExpression, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		mv.visitLdcInsn(booleanLitExpression.value); // Load
		return null;
	}

	@Override
	public Object visitClosure(Closure closure, Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitClosureDec(ClosureDec closureDeclaration, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitClosureEvalExpression(
			ClosureEvalExpression closureExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitClosureExpression(ClosureExpression closureExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	/**
	 * Stores the top most element of the list at a provided index. The stack should look like:
	 * bottom:[ .. | this(object holding lvalue) ]:top
	 * TODO: Currently, this method treats all variables as global and in the same class, change this
	 */
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		ExpArgPair pair = (ExpArgPair) arg;
		MethodVisitor mv = ((InheritedAttributes) pair.arg).mv;

		// First, let's make sure that the list can fit the element. So, add null to the list until
		// we have enough elements to hold the desired value.

		Label l0 = new Label();
		mv.visitJumpInsn(GOTO, l0);
		Label l1 = new Label();
		mv.visitLabel(l1);
//		mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{Opcodes.INTEGER}, 0, null);
//		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP); // make sure we sitll have the this reference for the set
		mv.visitFieldInsn(GETFIELD, className, expressionLValue.identToken.getText(),
				"Ljava/util/List;"); // get the list
		mv.visitInsn(ACONST_NULL);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
		mv.visitInsn(POP);
		mv.visitLabel(l0);
//		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, className, expressionLValue.identToken.getText(),
				"Ljava/util/List;"); // get the list
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
		expressionLValue.expression.visit(this, pair.arg); // Get index
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD); // increment the index by 1 so we can make sure the size can fit the index
		mv.visitJumpInsn(IF_ICMPLT, l1);
		
		// now we can continue on with life...

		// We want to make the stack look like:
		// bottom:[ .. | list | index | val ]:top

		// Let's first start by getting the list
		mv.visitFieldInsn(GETFIELD, className, expressionLValue.identToken.getText(),
				"Ljava/util/List;");
		expressionLValue.expression.visit(this, pair.arg); // Get index
		pair.expression.visit(this, pair.arg); // Get value to be stored

		// Handle some special cases
		if (pair.expression.getType().equals(intType))
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		else if (pair.expression.getType().equals(booleanType))
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);

		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", true);
		mv.visitInsn(POP);

		return null;
	}

	@Override
	public Object visitExpressionStatement(
			ExpressionStatement expressionStatement, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	/**
	 * Load the VALUE of the expression onto the top of the stack
	 * TODO: Currently, this method treats all variables as global and in the same class, change this
	 */
	public Object visitIdentExpression(IdentExpression identExpression,
			Object arg) throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		// Load
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, className, identExpression.identToken.getText(),
				identExpression.getDesc());
		return null;
	}

	@Override
	/**
	 * Stores the top most element of the stack into the this variable. Note, the stack should look
	 * like:
	 * bottom:[ .. | this(object holding lvalue) ]:top
	 * TODO: Currently, this method treats all variables as global and in the same class, change this
	 */
	public Object visitIdentLValue(IdentLValue identLValue, Object arg)
			throws Exception {
		ExpArgPair pair = (ExpArgPair) arg;
		MethodVisitor mv = ((InheritedAttributes) pair.arg).mv;
		String type = identLValue.getType();

		// Put expression onto the stack
		pair.expression.visit(this, pair.arg); // Load expression to the top of the stack

		// Now our stack looks like:
		// bottom:[ .. | this(object holding lvalue) | val ]:top
		// We can now proceed to put the value into the variable

		// Check if type is a list
		if(type.contains(listInterface)) type = listInterface+";";

		// Now put the object on the stack into the variable
		mv.visitFieldInsn(PUTFIELD, className, identLValue.firstToken.getText(), type);
		return null;
	}

	@Override
	/**
	 * This expects that the call to expression.visit will put the result of the expression on top
	 * of the stack (and the result will be a boolean).
	 */
	public Object visitIfElseStatement(IfElseStatement ifElseStatement,
			Object arg) throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		ifElseStatement.expression.visit(this, arg); // put the result of the expression on the stack

		Label l0 = new Label();
		mv.visitJumpInsn(IFEQ, l0); // if the expression is equal to 0 (false), jump to l0
		ifElseStatement.ifBlock.visit(this, arg); // Execute the if block
		Label l1 = new Label();
		mv.visitJumpInsn(GOTO, l1);
		mv.visitLabel(l0);
		ifElseStatement.elseBlock.visit(this, arg); // Execute else block
		mv.visitLabel(l1);

		return null;
	}

	@Override
	/**
	 * This expects that the call to expression.visit will put the result of the expression on top
	 * of the stack (and the result will be a boolean).
	 */
	public Object visitIfStatement(IfStatement ifStatement, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		ifStatement.expression.visit(this, arg); // put the result of the expression on the stack

		Label l0 = new Label();
		mv.visitJumpInsn(IFEQ, l0); // if the expression is equal to 0 (false), jump to l0
		ifStatement.block.visit(this, arg); // execute the if block
		mv.visitLabel(l0);

		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression,
			Object arg) throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		mv.visitLdcInsn(intLitExpression.value); // Load
		return null;
	}

	@Override
	public Object visitKeyExpression(KeyExpression keyExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitKeyValueExpression(
			KeyValueExpression keyValueExpression, Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitKeyValueType(KeyValueType keyValueType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	/**
	 * Puts the REFERENCE to an object that is created from the listExpression onto the top of the
	 * stack
	 */
	public Object visitListExpression(ListExpression listExpression, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		List<Expression> expression = listExpression.expressionList;

		// First, create the new list
		mv.visitTypeInsn(NEW, "java/util/ArrayList");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);

		// Now, populate the List with the values
		for(int i = 0; i < expression.size(); i++)
		{
			mv.visitInsn(DUP);  // Keep two references of the object on top of the stack. This way
								// we can keep a reference for future adds/leave a copy on top after
								// populating it

			expression.get(i).visit(this, arg); // Put the expression onto the stack

			// Handle some special cases
			if(expression.get(i).getType().equals(intType))
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			else if(expression.get(i).getType().equals(booleanType))
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);

			// Continue
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP); // remove returned boolean
		}

		// The call to DUP should ensure that this is on the top of the stack
		return null;
	}

	@Override
	/**
	 * Load the VALUE of the element in the list located at the provided expression onto the top of
	 * the stack
	 * TODO: Currently, this method treats all variables as global and in the same class, change this
	 */
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		// Load object onto stack
		mv.visitVarInsn(ALOAD, 0); // this
		mv.visitFieldInsn(GETFIELD, className, listOrMapElemExpression.identToken.getText(),
				"Ljava/util/List;");
		listOrMapElemExpression.expression.visit(this, arg); // find index we want to load
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);;

		// Now we want to check if we have an integer or boolean. If we do, cast it to an int or
		// bool (respectively)
		if(listOrMapElemExpression.getType().equals(intType))
		{
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
		}
		else if(listOrMapElemExpression.getType().equals(booleanType))
		{
			mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
		}
		else if(listOrMapElemExpression.getType().equals(stringType))
		{
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
		}

		return null;
	}

	@Override
	public Object visitListType(ListType listType, Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitMapListExpression(MapListExpression mapListExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitPrintStatement(PrintStatement printStatement, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(printStatement.firstToken.getLineNumber(), l0);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
				"Ljava/io/PrintStream;");
		printStatement.expression.visit(this, arg); // adds code to leave value
													// of expression on top of
													// stack.
													// Unless there is a good
													// reason to do otherwise,
													// pass arg down the tree
		String etype = printStatement.expression.getType();
		if (etype.equals("I") || etype.equals("Z")
				|| etype.equals("Ljava/lang/String;")) {
			String desc = "(" + etype + ")V";
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
					desc, false);
		} else
			throw new UnsupportedOperationException(
					"printing list or map not yet implemented");
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		className = program.JVMName;
		classDescriptor = 'L' + className + ';';
		cw.visit(52, // version
				ACC_PUBLIC + ACC_SUPER, // access codes
				className, // fully qualified classname
				null, // signature
				"java/lang/Object", // superclass
				new String[] { "cop5555sp15/Codelet" } // implemented interfaces
		);
		cw.visitSource(null, null); // maybe replace first argument with source
									// file name

		// create init method
		{
			MethodVisitor mv;
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(3, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V", false);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", classDescriptor, null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// generate the execute method
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "execute", // name of top
																	// level
																	// method
				"()V", // descriptor: this method is parameterless with no
						// return value
				null, // signature.  This is null for us, it has to do with generic types
				null // array of strings containing exceptions
				);
		mv.visitCode();
		Label lbeg = new Label();
		mv.visitLabel(lbeg);
		mv.visitLineNumber(program.firstToken.lineNumber, lbeg);
		program.block.visit(this, new InheritedAttributes(mv));
		mv.visitInsn(RETURN);
		Label lend = new Label();
		mv.visitLabel(lend);
		mv.visitLocalVariable("this", classDescriptor, null, lbeg, lend, 0);
		mv.visitMaxs(0, 0);  //this is required just before the end of a method. 
		                     //It causes asm to calculate information about the
		                     //stack usage of this method.
		mv.visitEnd();

		
		cw.visitEnd();
		return cw.toByteArray();
	}

	@Override
	public Object visitQualifiedName(QualifiedName qualifiedName, Object arg) {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitRangeExpression(RangeExpression rangeExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitReturnStatement(ReturnStatement returnStatement,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	/**
	 * This expects that expression.visit() will put the referenced object onto the stack
	 */
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv; // this should be the
		sizeExpression.expression.visit(this, arg); // put expression (list) on top of the stack
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
		return null;
	}

	@Override
	public Object visitStringLitExpression(
			StringLitExpression stringLitExpression, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv; // this should be the
		// first statement
		// of all visit
		// methods that
		// generate
		// instructions
		mv.visitLdcInsn(stringLitExpression.value); // Load
		return null;
	}

	@Override
	/**
	 * This method expects that the expression (when visited) will put the value of the expression
	 * on the top of the stack. In turn, this method will put the value of this UnaryExpression
	 * on top of the stack.
	 */
	public Object visitUnaryExpression(UnaryExpression unaryExpression,
			Object arg) throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		unaryExpression.expression.visit(this, arg); // Put the value of the expression on top of the stack

		switch (unaryExpression.op.kind)
		{
			case MINUS:
				mv.visitInsn(INEG);
				break;
			case NOT:
				Label l0 = new Label();
				mv.visitJumpInsn(IFEQ, l0); // if val is equal to 0, jump to l1 (put 1 on stack)
				mv.visitInsn(ICONST_0);
				Label l1 = new Label();
				mv.visitJumpInsn(GOTO, l1);
				mv.visitLabel(l0);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(l1);
				break;
			default:
				throw new Exception("Parsing failed, this is not a Unary Expression");
		}
		return null;
	}

	@Override
	public Object visitValueExpression(ValueExpression valueExpression,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws Exception {
		fv = cw.visitField(0, varDec.identToken.getText(), varDec.type.getDesc(), varDec.type.getSignature(), null);
		fv.visitEnd(); // maybe put this at the end of the program? @TODO: check
		return null;
	}

	@Override
	public Object visitWhileRangeStatement(
			WhileRangeStatement whileRangeStatement, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitWhileStarStatement(WhileStarStatement whileStarStatment,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	/**
	 * This expects that the call to expression.visit will put the result of the expression on top
	 * of the stack (and the result will be a boolean).
	 */
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		Label l0 = new Label();
		Label l1 = new Label();
		mv.visitLabel(l0);
		whileStatement.expression.visit(this, arg); // put the result of the expression on the stack
		mv.visitJumpInsn(IFEQ, l1); // if the expression is equal to 0 (false), jump to l1
		whileStatement.block.visit(this, arg);
		mv.visitJumpInsn(GOTO, l0); // loop
		mv.visitLabel(l1);

		return null;
	}

	@Override
	public Object visitUndeclaredType(UndeclaredType undeclaredType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

}
