/**
 * @generated VGen 1.3.3
 */

package ast;

public abstract class AbstractTipo extends AbstractTraceable implements Tipo
{
	@Override
	public abstract String getSubfix() ;
	
	@Override
	public int hashCode() {
		
		return getSubfix().hashCode();
	}
}
