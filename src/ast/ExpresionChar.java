/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionChar:Expresion -> tipo:Tipo  value:String

public class ExpresionChar extends AbstractExpresion {

	public ExpresionChar(Tipo tipo, String value) {
		this.tipo = tipo;
		this.value = value;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public ExpresionChar(Object tipo, Object value) {
		this.tipo = (Tipo) tipo;
		this.value = (value instanceof Token) ? ((Token)value).getLexeme() : (String) value;

		searchForPositions(tipo, value);	// Obtener linea/columna a partir de los hijos
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String value;
}

