/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoVoid:Tipo -> 

public class TipoVoid extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getSubfix() {
		return "";
	}
	
}

