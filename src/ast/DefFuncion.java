/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	DefFuncion:Definicion -> identificador:String  parametros:DefVariable*  tipoRetorno:Tipo  defLocales:DefVariable*  sentencias:Sentencia*

public class DefFuncion extends AbstractDefinicion {

	public DefFuncion(String identificador, List<DefVariable> parametros, Tipo tipoRetorno, List<DefVariable> defLocales, List<Sentencia> sentencias) {
		this.identificador = identificador;
		this.parametros = parametros;
		this.tipoRetorno = tipoRetorno;
		this.defLocales = defLocales;
		this.sentencias = sentencias;

		searchForPositions(parametros, tipoRetorno, defLocales, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefFuncion(Object identificador, Object parametros, Object tipoRetorno, Object defLocales, Object sentencias) {
		this.identificador = (identificador instanceof Token) ? ((Token)identificador).getLexeme() : (String) identificador;
		this.parametros = (List<DefVariable>) parametros;
		this.tipoRetorno = (Tipo) tipoRetorno;
		this.defLocales = (List<DefVariable>) defLocales;
		this.sentencias = (List<Sentencia>) sentencias;

		searchForPositions(identificador, parametros, tipoRetorno, defLocales, sentencias);	// Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<DefVariable> getParametros() {
		return parametros;
	}
	public void setParametros(List<DefVariable> parametros) {
		this.parametros = parametros;
	}

	public Tipo getTipoRetorno() {
		return tipoRetorno;
	}
	public void setTipoRetorno(Tipo tipoRetorno) {
		this.tipoRetorno = tipoRetorno;
	}

	public List<DefVariable> getDefLocales() {
		return defLocales;
	}
	public void setDefLocales(List<DefVariable> defLocales) {
		this.defLocales = defLocales;
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

	private String identificador;
	private List<DefVariable> parametros;
	private Tipo tipoRetorno;
	private List<DefVariable> defLocales;
	private List<Sentencia> sentencias;
}

