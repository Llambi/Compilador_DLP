/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	SentenciaIf:Sentencia -> condicion:Expresion  sentenciasIf:Sentencia*  sentenciasElse:Sentencia*

public class SentenciaIf extends AbstractSentencia {

	public SentenciaIf(Expresion condicion, List<Sentencia> sentenciasIf, List<Sentencia> sentenciasElse) {
		this.condicion = condicion;
		this.sentenciasIf = sentenciasIf;
		this.sentenciasElse = sentenciasElse;

		searchForPositions(condicion, sentenciasIf, sentenciasElse);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SentenciaIf(Object condicion, Object sentenciasIf, Object sentenciasElse) {
		this.condicion = (Expresion) condicion;
		this.sentenciasIf = (List<Sentencia>) sentenciasIf;
		this.sentenciasElse = (List<Sentencia>) sentenciasElse;

		searchForPositions(condicion, sentenciasIf, sentenciasElse);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<Sentencia> getSentenciasIf() {
		return sentenciasIf;
	}
	public void setSentenciasIf(List<Sentencia> sentenciasIf) {
		this.sentenciasIf = sentenciasIf;
	}

	public List<Sentencia> getSentenciasElse() {
		return sentenciasElse;
	}
	public void setSentenciasElse(List<Sentencia> sentenciasElse) {
		this.sentenciasElse = sentenciasElse;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<Sentencia> sentenciasIf;
	private List<Sentencia> sentenciasElse;
}

