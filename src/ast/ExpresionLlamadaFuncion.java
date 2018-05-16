/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	ExpresionLlamadaFuncion:Expresion -> identificador:String  entrada:Expresion*

public class ExpresionLlamadaFuncion extends AbstractExpresion {

	public ExpresionLlamadaFuncion(String identificador, List<Expresion> entrada) {
		this.identificador = identificador;
		this.entrada = entrada;

		searchForPositions(entrada);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public ExpresionLlamadaFuncion(Object identificador, Object entrada) {
		this.identificador = (identificador instanceof Token) ? ((Token)identificador).getLexeme() : (String) identificador;
		this.entrada = (List<Expresion>) entrada;

		searchForPositions(identificador, entrada);	// Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<Expresion> getEntrada() {
		return entrada;
	}
	public void setEntrada(List<Expresion> entrada) {
		this.entrada = entrada;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	public void setDefinicion(DefFuncion definicion) {
		this.definicion=definicion;
	}
	
	public DefFuncion getDefinicion() {
		return this.definicion;
	}

	private DefFuncion definicion;
	private String identificador;
	private List<Expresion> entrada;
	
}

