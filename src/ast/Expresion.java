/**
 * @generated VGen 1.3.3
 */

package ast;

public interface Expresion extends AST
{
	public void setModificable(boolean b);
	public boolean isModificable();
	public void setTipo(Tipo tipo);
	public Tipo getTipo();
}
