/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoComplejo:Tipo -> nombreTipo:String

public class TipoComplejo extends AbstractTipo
{

	public TipoComplejo(String nombreTipo)
	{
		this.nombreTipo = nombreTipo;
	}

	public TipoComplejo(Object nombreTipo)
	{
		this.nombreTipo = (nombreTipo instanceof Token) ? ((Token) nombreTipo).getLexeme() : (String) nombreTipo;

		searchForPositions(nombreTipo); // Obtener linea/columna a partir de los hijos
	}

	public String getNombreTipo() {
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

	public void setDefinicion(DefStruct definicion) {
		this.definicion = definicion;
	}

	public DefStruct getDefinicion() {
		return definicion;
	}

	private DefStruct definicion;
	private String nombreTipo;

	@Override
	public int getSize() {
		return this.definicion.getCampos().stream().mapToInt(campo -> campo.getTipo().getSize()).sum();
	}

	@Override
	public String getSubfix() {
		return "";
	}

}
