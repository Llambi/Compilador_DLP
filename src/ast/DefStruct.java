/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	DefStruct:Definicion -> identificador:String  campos:Campo*

public class DefStruct extends AbstractDefinicion {

	public DefStruct(String identificador, List<Campo> campos) {
		this.identificador = identificador;
		this.campos = campos;

		searchForPositions(campos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefStruct(Object identificador, Object campos) {
		this.identificador = (identificador instanceof Token) ? ((Token)identificador).getLexeme() : (String) identificador;
		this.campos = (List<Campo>) campos;

		searchForPositions(identificador, campos);	// Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<Campo> getCampos() {
		return campos;
	}
	
	public Campo getCampo(String name) {
		return this.campos.stream().filter(campo->campo.getIdentificador().equals(name)).findFirst().orElse(null);
	}
	public void setCampos(List<Campo> campos) {
		this.campos = campos;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String identificador;
	private List<Campo> campos;
}

