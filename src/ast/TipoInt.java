/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoInt:Tipo -> 

public class TipoInt extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public String getSubfix() {
		return "I";
	}

}

