package cop5555sp15.ast;

import com.sun.org.apache.xpath.internal.operations.NotEquals;
import org.objectweb.asm.*;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TypeConstants;

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
		LValue lval = assignmentStatement.lvalue;

		// Make the stack look like:
		// bottom:[ .. | this(object holding lvalue) | val ]:top
		mv.visitVarInsn(ALOAD, 0);
		assignmentStatement.expression.visit(this, arg); // Load expression to the top of the stack
		assignmentStatement.lvalue.visit(this, arg); // Load expression to the top of the stack
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
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
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
				identExpression.getType());
		return null;
	}

	@Override
	/**
	 * Stores the top most element of the stack into the this variable. Note, the stack should look
	 * like:
	 * bottom:[ .. | this(object holding lvalue) | val ]:top
	 * TODO: Currently, this method treats all variables as global and in the same class, change this
	 */
	public Object visitIdentLValue(IdentLValue identLValue, Object arg)
			throws Exception {
		MethodVisitor mv = ((InheritedAttributes) arg).mv;
		mv.visitFieldInsn(PUTFIELD, className, identLValue.firstToken.getText(), identLValue.getType());
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
	public Object visitListExpression(ListExpression listExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
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
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
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
		fv = cw.visitField(0, varDec.identToken.getText(), varDec.type.getJVMType(), null, null);
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
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

	@Override
	public Object visitUndeclaredType(UndeclaredType undeclaredType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"code generation not yet implemented");
	}

}
