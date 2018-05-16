/**
 * @generated VGen 1.3.3
 */

package visitor;

import ast.*;
import java.util.*;

/*
DefaultVisitor. Implementación base del visitor para ser derivada por nuevos visitor.
	No modificar esta clase. Para crear nuevos visitor usar el fichero "_PlantillaParaVisitors.txt".
	DefaultVisitor ofrece una implementación por defecto de cada nodo que se limita a visitar los nodos hijos.
*/
public class DefaultVisitor implements Visitor
{

	// class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {
		visitChildren(node.getDefiniciones(), param);
		return null;
	}

	// class DefVariable { String identificador; Tipo tipo; }
	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class DefStruct { String identificador; List<Campo> campos; }
	public Object visit(DefStruct node, Object param) {
		visitChildren(node.getCampos(), param);
		return null;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {
		visitChildren(node.getParametros(), param);
		if (node.getTipoRetorno() != null)
			node.getTipoRetorno().accept(this, param);
		visitChildren(node.getDefLocales(), param);
		visitChildren(node.getSentencias(), param);
		return null;
	}

	// class Campo { String identificador; Tipo tipo; }
	public Object visit(Campo node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class TipoVoid { }
	public Object visit(TipoVoid node, Object param) {
		return null;
	}

	// class TipoInt { }
	public Object visit(TipoInt node, Object param) {
		return null;
	}

	// class TipoReal { }
	public Object visit(TipoReal node, Object param) {
		return null;
	}

	// class TipoChar { }
	public Object visit(TipoChar node, Object param) {
		return null;
	}

	// class TipoArray { String tam; Tipo tipo; }
	public Object visit(TipoArray node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class TipoComplejo { String nombreTipo; }
	public Object visit(TipoComplejo node, Object param) {
		return null;
	}

	// class SentenciaAsignacion { Expresion left; Expresion right; }
	public Object visit(SentenciaAsignacion node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	// class SentenciaPrint { Expresion expresion; }
	public Object visit(SentenciaPrint node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class SentenciaPrintSP { Expresion expresion; }
	public Object visit(SentenciaPrintSP node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class SentenciaPrintLN { Expresion expresion; }
	public Object visit(SentenciaPrintLN node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class SentenciaRead { Expresion expresion; }
	public Object visit(SentenciaRead node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class SentenciaReturn { List<Expresion> expresion; }
	public Object visit(SentenciaReturn node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class SentenciaWhile { Expresion condicion; List<Sentencia> sentencias; }
	public Object visit(SentenciaWhile node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getSentencias(), param);
		return null;
	}

	// class SentenciaIf { Expresion condicion; List<Sentencia> sentenciasIf;
	// List<Sentencia> sentenciasElse; }
	public Object visit(SentenciaIf node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getSentenciasIf(), param);
		visitChildren(node.getSentenciasElse(), param);
		return null;
	}

	// class SentenciaProcedimiento { String identificador; List<Expresion> entrada;
	// }
	public Object visit(SentenciaProcedimiento node, Object param) {
		visitChildren(node.getEntrada(), param);
		return null;
	}

	// class ExpresionCast { Tipo tipo; Expresion expresion; }
	public Object visit(ExpresionCast node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class ExpresionChar { Tipo tipo; String value; }
	public Object visit(ExpresionChar node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class ExpresionInt { Tipo tipo; String value; }
	public Object visit(ExpresionInt node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class ExpresionReal { Tipo tipo; String value; }
	public Object visit(ExpresionReal node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	// class ExpresionAritmetica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionAritmetica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	// class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		return null;
	}

	// class ExpresionAccesoArray { Expresion expresion1; Expresion expresion2; }
	public Object visit(ExpresionAccesoArray node, Object param) {
		if (node.getExpresion1() != null)
			node.getExpresion1().accept(this, param);
		if (node.getExpresion2() != null)
			node.getExpresion2().accept(this, param);
		return null;
	}

	// class ExpresionAccesoCampo { Expresion expresion; String nombre; }
	public Object visit(ExpresionAccesoCampo node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		visitChildren(node.getEntrada(), param);
		return null;
	}

	// class ExpresionComparacion { Expresion left; String symbol; Expresion right;
	// }
	public Object visit(ExpresionComparacion node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	// class ExpresionLogica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	// class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	// Método auxiliar -----------------------------
	protected void visitChildren(List<? extends AST> children, Object param) {
		if (children != null)
			for (AST child : children)
				child.accept(this, param);
	}
}
