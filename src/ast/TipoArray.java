/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoArray:Tipo -> tam:String  tipo:Tipo

public class TipoArray extends AbstractTipo
{

	public TipoArray(String tam, Tipo tipo)
	{
		this.tam = tam;
		this.tipo = tipo;

		searchForPositions(tipo); // Obtener linea/columna a partir de los hijos
	}

	public TipoArray(Object tam, Object tipo)
	{
		this.tam = (tam instanceof Token) ? ((Token) tam).getLexeme() : (String) tam;
		this.tipo = (Tipo) tipo;

		searchForPositions(tam, tipo); // Obtener linea/columna a partir de los hijos
	}

	public String getTam() {
		return tam;
	}

	public void setTam(String tam) {
		this.tam = tam;
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

	private String tam;
	private Tipo tipo;

	@Override
	public int getSize() {
		return Integer.valueOf(tam) * this.tipo.getSize();
	}

	@Override
	public String getSubfix() {
		return this.tipo.getSubfix();
	}
}
