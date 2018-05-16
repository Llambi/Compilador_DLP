package visitor;
/**
 * @generated VGen 1.3.3
 */

// package <nombre paquete>;

import ast.*;

/*
Plantilla para Visitors.
Para crear un nuevo Visitor cortar y pegar este código y ya se tendrá un visitor que compila y 
que al ejecutarlo recorrerá todo el árbol (sin hacer nada aún en él).
Solo quedará añadir a cada método visit aquello adicional que tenga que realizar sobre su nodo del AST.
*/

public class Reconstructor extends DefaultVisitor
{

	// ---------------------------------------------------------
	// Tareas a realizar en cada método visit:
	//
	// Si en algún método visit NO SE QUIERE HACER NADA más que recorrer los hijos
	// entonces se puede
	// borrar (dicho método se heredará de DefaultVisitor con el código de
	// recorrido).
	//
	// Lo siguiente es para cuando se quiera AÑADIR alguna funcionalidad adicional a
	// un visit:
	//
	// - El código que aparece en cada método visit es aquel que recorre los hijos.
	// Es el mismo código
	// que está implementado en el padre (DefaultVisitor). Por tanto la llamada a
	// 'super.visit' y el
	// resto del código del método hacen lo mismo (por ello 'super.visit' está
	// comentado).
	//
	// - Lo HABITUAL será borrar todo el código de recorrido dejando solo la llamada
	// a 'super.visit'. De esta
	// manera cada método visit se puede centrar en la tarea que tiene que realizar
	// sobre su nodo del AST.
	//
	// - La razón de que aparezca el código de recorrido de los hijos es por si se
	// necesita realizar alguna
	// tarea DURANTE el mismo (por ejemplo ir comprobando su tipo). En este caso ya
	// se tiene implementado
	// dicho recorrido y solo habrá que incrustar las acciones adicionales en el
	// mismo. En este caso
	// la llamada a 'super.visit' deberá ser borrada.
	// ---------------------------------------------------------

	// class DefVariable { Campo campo; }
	@Override
	public Object visit(DefVariable node, Object param) {

		// super.visit(node, param);
		System.out.print("var " + node.getIdentificador() + " : ");

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		System.out.println(";");
		return null;
	}

	// class DefStruct { String identificador; List<Campo> campos; }
	@Override
	public Object visit(DefStruct node, Object param) {

		// super.visit(node, param);
		System.out.println("Struct " + node.getIdentificador() + " {");
		if (node.getCampos() != null)
			for (Campo child : node.getCampos())
				child.accept(this, param);
		System.out.println("}");
		return null;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	@Override
	public Object visit(DefFuncion node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getIdentificador() + "(");
		if (node.getParametros() != null)
			for (int i = 0; i < node.getParametros().size(); i++)
				// TODO: comas de los parametros
				node.getParametros().get(i).accept(this, param);
		System.out.print(") :");
		if (node.getTipoRetorno() != null)
			node.getTipoRetorno().accept(this, param);
		System.out.println(" {");
		if (node.getDefLocales() != null)
			for (DefVariable child : node.getDefLocales())
				child.accept(this, param);

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);
		System.out.println(" }");
		return null;
	}

	// class Campo { String identificador; Tipo tipo; }
	@Override
	public Object visit(Campo node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getIdentificador() + " : ");
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		System.out.println(";");
		return null;
	}

	// class TipoVoid { }
	@Override
	public Object visit(TipoVoid node, Object param) {
		System.out.print("void");
		return null;
	}

	// class TipoInt { }
	@Override
	public Object visit(TipoInt node, Object param) {
		System.out.print("int");
		return null;
	}

	// class TipoReal { }
	@Override
	public Object visit(TipoReal node, Object param) {
		System.out.print("float");
		return null;
	}

	// class TipoChar { }
	@Override
	public Object visit(TipoChar node, Object param) {
		System.out.print("char");
		return null;
	}

	// class TipoArray { String tam; Tipo tipo; }
	@Override
	public Object visit(TipoArray node, Object param) {

		// super.visit(node, param);
		System.out.print("[]");
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		return null;
	}

	// class TipoComplejo { String nombreTipo; }
	@Override
	public Object visit(TipoComplejo node, Object param) {
		System.out.print(node.getNombreTipo());
		return null;
	}

	// class SentenciaAsignacion { Expresion left; Expresion right; }
	@Override
	public Object visit(SentenciaAsignacion node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		System.out.print(" = ");
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		System.out.println(";");
		return null;
	}

	// class SentenciaPrint { Expresion expresion; }
	@Override
	public Object visit(SentenciaPrint node, Object param) {

		// super.visit(node, param);
		System.out.print("Print(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(");");
		return null;
	}

	// class SentenciaPrintSP { Expresion expresion; }
	@Override
	public Object visit(SentenciaPrintSP node, Object param) {

		// super.visit(node, param);
		System.out.print("PrintSP(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(");");
		return null;
	}

	// class SentenciaPrintLN { Expresion expresion; }
	@Override
	public Object visit(SentenciaPrintLN node, Object param) {

		// super.visit(node, param);
		System.out.print("PrintLN(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(");");
		return null;
	}

	// class SentenciaRead { Expresion expresion; }
	@Override
	public Object visit(SentenciaRead node, Object param) {

		// super.visit(node, param);
		System.out.print(" Read(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(");");
		return null;
	}

	// class SentenciaReturn { List<Expresion> expresion; }
	@Override
	public Object visit(SentenciaReturn node, Object param) {

		// super.visit(node, param);
		System.out.print("return ");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(";");
		return null;
	}

	// class SentenciaWhile { Expresion condicion; List<Sentencia> sentencias; }
	@Override
	public Object visit(SentenciaWhile node, Object param) {

		// super.visit(node, param);
		System.out.print("while(");
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		System.out.println(") {");
		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);
		System.out.println("}");
		return null;
	}

	// class SentenciaIf { Expresion condicion; List<Sentencia> sentenciasIf;
	// List<Sentencia> sentenciasElse; }
	@Override
	public Object visit(SentenciaIf node, Object param) {

		// super.visit(node, param);
		System.out.print("if(");
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		System.out.println("){");
		if (node.getSentenciasIf() != null)
			for (Sentencia child : node.getSentenciasIf())
				child.accept(this, param);
		System.out.println("}");
		if (node.getSentenciasElse() != null)
		{
			System.out.println("else {");
			for (Sentencia child : node.getSentenciasElse())
				child.accept(this, param);
			System.out.println("}");
		}
		return null;
	}

	// ES UNA LLAMADA A FUNCION --> class SentenciaProcedimiento { String
	// identificador; List<Expresion> entrada;
	// }
	@Override
	public Object visit(SentenciaProcedimiento node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getIdentificador() + "(");
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);
		System.out.println(");");
		return null;
	}

	// class ExpresionCast { Tipo tipo; Expresion expresion; }
	@Override
	public Object visit(ExpresionCast node, Object param) {

		// super.visit(node, param);
		System.out.print("cast<");
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		System.out.print(">(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(");");
		return null;
	}

	// class ExpresionChar { Tipo tipo; String value; }
	@Override
	public Object visit(ExpresionChar node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getValue());
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		return null;
	}

	// class ExpresionInt { Tipo tipo; String value; }
	@Override
	public Object visit(ExpresionInt node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getValue());
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		return null;
	}

	// class ExpresionReal { Tipo tipo; String value; }
	public Object visit(ExpresionReal node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getValue());
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		return null;
	}

	// class ExpresionAritmetica { Expresion left; String symbol; Expresion right; }
	@Override
	public Object visit(ExpresionAritmetica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		System.out.print(node.getSymbol());
		if (node.getRight() != null)
			node.getRight().accept(this, param);

		return null;
	}

	// class ExpresionVariable { String variable; }
	@Override
	public Object visit(ExpresionVariable node, Object param) {
		System.out.println(node.getVariable());
		return null;
	}

	// class ExpresionAccesoArray { Expresion expresion1; Expresion expresion2; }
	@Override
	public Object visit(ExpresionAccesoArray node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion1() != null)
			node.getExpresion1().accept(this, param);
		System.out.print("[");
		if (node.getExpresion2() != null)
			node.getExpresion2().accept(this, param);
		System.out.print("]");
		return null;
	}

	// class ExpresionAccesoCampo { Expresion expresion; String nombre; }
	@Override
	public Object visit(ExpresionAccesoCampo node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println("." + node.getNombre() + ";");
		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	@Override
	public Object visit(ExpresionLlamadaFuncion node, Object param) {

		// super.visit(node, param);
		System.out.print(node.getIdentificador() + "(");
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);
		System.out.println(");");
		return null;
	}

	// class ExpresionComparacion { Expresion left; String symbol; Expresion right;
	// }
	@Override
	public Object visit(ExpresionComparacion node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		System.out.print(node.getSymbol());
		if (node.getRight() != null)
			node.getRight().accept(this, param);

		return null;
	}

	// class ExpresionLogica { Expresion left; String symbol; Expresion right; }
	@Override
	public Object visit(ExpresionLogica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		System.out.print(node.getSymbol());
		if (node.getRight() != null)
			node.getRight().accept(this, param);

		return null;
	}

	// class ExpresionNegacion { Expresion expresion; }
	@Override
	public Object visit(ExpresionNegacion node, Object param) {

		// super.visit(node, param);
		System.out.print("!(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(")");
		return null;
	}

	// class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {

		// super.visit(node, param);
		System.out.println("-(");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		System.out.println(")");
		return null;
	}
}
