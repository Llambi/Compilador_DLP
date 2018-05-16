/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionVariable:Expresion -> variable:String

public class ExpresionVariable extends AbstractExpresion {

	public ExpresionVariable(String variable) {
		this.variable = variable;
	}

	public ExpresionVariable(Object variable) {
		this.variable = (variable instanceof Token) ? ((Token)variable).getLexeme() : (String) variable;

		searchForPositions(variable);	// Obtener linea/columna a partir de los hijos
	}

	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	public void setDefinicion(DefVariable definicion) {
		this.definicion= definicion;
	}
	
	public DefVariable getDefinicion() {
		return definicion;
	}

	private DefVariable definicion;
	private String variable;

	
}

