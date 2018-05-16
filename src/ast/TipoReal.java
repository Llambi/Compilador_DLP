/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	TipoReal:Tipo -> 

public class TipoReal extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	@Override
	public int getSize() {
		return 4;
	}

	@Override
	public String getSubfix() {
		return "F";
	}

}

