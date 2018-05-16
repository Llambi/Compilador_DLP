/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	DefVariable:Definicion -> identificador:String  tipo:Tipo

public class DefVariable extends AbstractDefinicion
{

	public DefVariable(String identificador, Tipo tipo)
	{
		this.identificador = identificador;
		this.tipo = tipo;

		searchForPositions(tipo); // Obtener linea/columna a partir de los hijos
	}

	public DefVariable(Object identificador, Object tipo)
	{
		this.identificador = (identificador instanceof Token) ? ((Token) identificador).getLexeme() : (String) identificador;
		this.tipo = (Tipo) tipo;

		searchForPositions(identificador, tipo); // Obtener linea/columna a partir de los hijos
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

	public void setDireccion(int num) {
		this.direccion = num;
	}

	public int getDireccion() {
		return this.direccion;
	}

	public String getScope() {
		// TODO Auto-generated method stub
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	private String identificador;
	private Tipo tipo;
	private int direccion;
	private String scope;

}
