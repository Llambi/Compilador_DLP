/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	SentenciaPrintSP:Sentencia -> expresion:Expresion

public class SentenciaPrintSP extends AbstractSentencia {

	public SentenciaPrintSP(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public SentenciaPrintSP(Object expresion) {
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

