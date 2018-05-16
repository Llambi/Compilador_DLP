/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	SentenciaWhile:Sentencia -> condicion:Expresion  sentencias:Sentencia*

public class SentenciaWhile extends AbstractSentencia {

	public SentenciaWhile(Expresion condicion, List<Sentencia> sentencias) {
		this.condicion = condicion;
		this.sentencias = sentencias;

		searchForPositions(condicion, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SentenciaWhile(Object condicion, Object sentencias) {
		this.condicion = (Expresion) condicion;
		this.sentencias = (List<Sentencia>) sentencias;

		searchForPositions(condicion, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<Sentencia> getSentencias() {
		return sentencias;
	}
	public void setSentencias(List<Sentencia> sentencias) {
		this.sentencias = sentencias;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<Sentencia> sentencias;
}

