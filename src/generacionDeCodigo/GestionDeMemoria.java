package generacionDeCodigo;

import ast.Campo;
import ast.DefFuncion;
import ast.DefStruct;
import ast.DefVariable;
import visitor.DefaultVisitor;

/**
 * Clase encargada de asignar direcciones a las variables
 */
public class GestionDeMemoria extends DefaultVisitor
{

	private int sumaTamañoVariables = 0;
	/*
	 * Poner aquí los visit necesarios. Si se ha usado VGen solo hay que copiarlos
	 * de la clase 'visitor/_PlantillaParaVisitors.txt'.
	 */

	// public Object visit(Programa prog, Object param) {
	// ...
	// }

	// class DefVariable { Tipo tipo; String nombre; }
	public Object visit(DefVariable node, Object param) {

		// super.visit(node, param);
		node.setDireccion(sumaTamañoVariables);
		if (node.getTipo() != null)
		{
			node.getTipo().accept(this, param);
			sumaTamañoVariables += node.getTipo().getSize();
		}
		return null;
	}

	// class DefStruct { String identificador; List<Campo> campos; }
	public Object visit(DefStruct node, Object param) {

		// super.visit(node, param);
		int pos=0;
		if (node.getCampos() != null)
			for (Campo child : node.getCampos())
			{
				child.setDireccion(pos);
				child.accept(this, param);
				pos += child.getTipo().getSize();
			}
		return null;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {

		// super.visit(node, param);
		int pos = 4;
		for (int i = node.getParametros().size() - 1; i >= 0; i--)
		{
			node.getParametros().get(i).setDireccion(pos);
			pos += node.getParametros().get(i).getTipo().getSize();
		}
		pos = 0;
		for (int i = 0; i < node.getDefLocales().size(); i++)
		{
			pos -= node.getDefLocales().get(i).getTipo().getSize();
			node.getDefLocales().get(i).setDireccion(pos);
		}

		return null;
	}

}
