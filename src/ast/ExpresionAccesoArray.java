/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionAccesoArray:Expresion -> expresion1:Expresion  expresion2:Expresion

public class ExpresionAccesoArray extends AbstractExpresion {

	public ExpresionAccesoArray(Expresion expresion1, Expresion expresion2) {
		this.expresion1 = expresion1;
		this.expresion2 = expresion2;

		searchForPositions(expresion1, expresion2);	// Obtener linea/columna a partir de los hijos
	}

	public ExpresionAccesoArray(Object expresion1, Object expresion2) {
		this.expresion1 = (Expresion) expresion1;
		this.expresion2 = (Expresion) expresion2;

		searchForPositions(expresion1, expresion2);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion1() {
		return expresion1;
	}
	public void setExpresion1(Expresion expresion1) {
		this.expresion1 = expresion1;
	}

	public Expresion getExpresion2() {
		return expresion2;
	}
	public void setExpresion2(Expresion expresion2) {
		this.expresion2 = expresion2;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresion1;
	private Expresion expresion2;
}

