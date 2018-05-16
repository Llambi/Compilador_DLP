/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionMenosUnario:Expresion -> expresion:Expresion

public class ExpresionMenosUnario extends AbstractExpresion {

	public ExpresionMenosUnario(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public ExpresionMenosUnario(Object expresion) {
		this.expresion = (Expresion) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion() {
		return expresion;
	}
	public void setExpresion(Expresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresion;
}

