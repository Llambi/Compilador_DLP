/**
 * @generated VGen 1.3.3
 */

package visitor;

import ast.*;

public interface Visitor {
	public Object visit(Programa node, Object param);
	public Object visit(DefVariable node, Object param);
	public Object visit(DefStruct node, Object param);
	public Object visit(DefFuncion node, Object param);
	public Object visit(Campo node, Object param);
	public Object visit(TipoVoid node, Object param);
	public Object visit(TipoInt node, Object param);
	public Object visit(TipoReal node, Object param);
	public Object visit(TipoChar node, Object param);
	public Object visit(TipoArray node, Object param);
	public Object visit(TipoComplejo node, Object param);
	public Object visit(SentenciaAsignacion node, Object param);
	public Object visit(SentenciaPrint node, Object param);
	public Object visit(SentenciaPrintSP node, Object param);
	public Object visit(SentenciaPrintLN node, Object param);
	public Object visit(SentenciaRead node, Object param);
	public Object visit(SentenciaReturn node, Object param);
	public Object visit(SentenciaWhile node, Object param);
	public Object visit(SentenciaIf node, Object param);
	public Object visit(SentenciaProcedimiento node, Object param);
	public Object visit(ExpresionCast node, Object param);
	public Object visit(ExpresionChar node, Object param);
	public Object visit(ExpresionInt node, Object param);
	public Object visit(ExpresionReal node, Object param);
	public Object visit(ExpresionAritmetica node, Object param);
	public Object visit(ExpresionVariable node, Object param);
	public Object visit(ExpresionAccesoArray node, Object param);
	public Object visit(ExpresionAccesoCampo node, Object param);
	public Object visit(ExpresionLlamadaFuncion node, Object param);
	public Object visit(ExpresionComparacion node, Object param);
	public Object visit(ExpresionLogica node, Object param);
	public Object visit(ExpresionNegacion node, Object param);
	public Object visit(ExpresionMenosUnario node, Object param);
}
