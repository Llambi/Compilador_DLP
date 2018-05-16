/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionCast:Expresion -> tipo:Tipo  expresion:Expresion

public class ExpresionCast extends AbstractExpresion {

	public ExpresionCast(Tipo tipoCast, Expresion expresion) {
		this.tipoCast = tipoCast;
		this.expresion = expresion;

		searchForPositions(tipoCast, expresion);	// Obtener linea/columna a partir de los hijos
	}

	public ExpresionCast(Object tipo, Object expresion) {
		this.tipoCast = (Tipo) tipo;
		this.expresion = (Expresion) expresion;

		searchForPositions(tipo, expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Tipo getTipoCast() {
		return tipoCast;
	}
	public void setTipoCast(Tipo tipo) {
		this.tipoCast = tipo;
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

	private Tipo tipoCast;
	private Expresion expresion;
}

