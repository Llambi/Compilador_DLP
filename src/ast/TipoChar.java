/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoChar:Tipo -> 

public class TipoChar extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public String getSubfix() {
		return "B";
	}

}

