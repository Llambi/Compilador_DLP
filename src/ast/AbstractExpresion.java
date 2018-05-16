/**
 * @generated VGen 1.3.3
 */

package ast;

public abstract class AbstractExpresion extends AbstractTraceable implements Expresion {
	public boolean isModificable() {
		return modificable;
	}

	public void setModificable(boolean modificable) {
		this.modificable = modificable;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	protected Tipo tipo;
	private boolean modificable;
}

