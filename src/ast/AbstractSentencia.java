/**
 * @generated VGen 1.3.3
 */

package ast;

public abstract class AbstractSentencia extends AbstractTraceable implements Sentencia
{
	private DefFuncion scope;

	public DefFuncion getScope() {
		return scope;
	}

	public void setScope(DefFuncion scope) {
		this.scope = scope;
	}
}
