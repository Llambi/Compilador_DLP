/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	ExpresionAccesoCampo:Expresion -> expresion:Expresion  nombre:String

public class ExpresionAccesoCampo extends AbstractExpresion {

	public ExpresionAccesoCampo(Expresion expresion, String nombre) {
		this.expresion = expresion;
		this.nombre = nombre;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public ExpresionAccesoCampo(Object expresion, Object nombre) {
		this.expresion = (Expresion) expresion;
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;

		searchForPositions(expresion, nombre);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion() {
		return expresion;
	}
	public void setExpresion(Expresion expresion) {
		this.expresion = expresion;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresion;
	private String nombre;
}

