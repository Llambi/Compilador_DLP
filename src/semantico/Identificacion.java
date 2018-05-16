package semantico;

import java.util.HashMap;
import java.util.Map;

import ast.Campo;
import ast.DefFuncion;
import ast.DefStruct;
import ast.DefVariable;
import ast.Expresion;
import ast.ExpresionLlamadaFuncion;
import ast.ExpresionVariable;
import ast.Position;
import ast.Sentencia;
import ast.SentenciaProcedimiento;
import ast.TipoComplejo;
import main.GestorErrores;
import visitor.DefaultVisitor;

public class Identificacion extends DefaultVisitor
{

	public Identificacion(GestorErrores gestor)
	{
		this.gestorErrores = gestor;
	}

	/*
	 * Poner aqu� los visit necesarios. Si se ha usado VGen solo hay que copiarlos
	 * de la clase 'visitor/_PlantillaParaVisitors.txt'.
	 */

	// class DefVariable { String identificador; Tipo tipo; }
	public Object visit(DefVariable node, Object param) {

		DefVariable definicion = variables.getFromTop(node.getIdentificador());

		predicado(definicion == null, "Variable ya definida: " + node.getIdentificador(), node.getStart());

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		variables.put(node.getIdentificador(), node);
		return null;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {
		DefFuncion definicion = funciones.get(node.getIdentificador());
		predicado(definicion == null, "Funcion repetida: " + node.getIdentificador(), node.getStart());
		funciones.put(node.getIdentificador(), node);
		variables.set();
		if (node.getParametros() != null)
			for (DefVariable child : node.getParametros()) {
				child.accept(this, null);
				child.setScope("parametro");
			}

		if (node.getTipoRetorno() != null)
			node.getTipoRetorno().accept(this, param);

		if (node.getDefLocales() != null)
			for (DefVariable child : node.getDefLocales()) {
				child.accept(this, null);
				child.setScope("local");
			}

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);
		variables.reset();
		return null;
	}

	// class DefStruct { String identificador; List<Campo> campos; }
	public Object visit(DefStruct node, Object param) {
		DefStruct definicion = structs.get(node.getIdentificador());
		predicado(definicion == null, "Struct ya definida: " + node.getIdentificador(), node.getStart());

		predicado(node.getCampos().stream().map(campo -> campo.getIdentificador()).distinct().count() == node.getCampos().size(),
				"Campos repetidos en el Struct: " + node.getIdentificador(), node.getStart());

		structs.put(node.getIdentificador(), node);

		if (node.getCampos() != null)
			for (Campo child : node.getCampos())
			{
				child.accept(this, param);
			}

		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		DefFuncion definicion = funciones.get(node.getIdentificador());
		predicado(definicion != null, "Funcion no definida: " + node.getIdentificador(), node.getStart());
		node.setDefinicion(definicion); // Enlazar referencia con definici�n
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);

		return null;
	}

	// class SentenciaProcedimiento { String identificador; List<Expresion> entrada;
	// }
	public Object visit(SentenciaProcedimiento node, Object param) {
		DefFuncion definicion = funciones.get(node.getIdentificador());
		predicado(definicion != null, "Procedimiento no definido: " + node.getIdentificador(), node.getStart());
		node.setDefinicion(definicion); // Enlazar referencia con definici�n
		return null;
	}

	// class TipoComplejo { String nombreTipo; }
	public Object visit(TipoComplejo node, Object param) {
		DefStruct definicion = structs.get(node.getNombreTipo());
		predicado(definicion != null, "Struct no definida: " + node.getNombreTipo(), node.getStart());
		node.setDefinicion(definicion); // Enlazar referencia con definici�n
		return null;
	}

	// class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		DefVariable definicion = variables.getFromAny(node.getVariable());
		predicado(definicion != null, "Variable no definida: " + node.getVariable(), node.getStart());
		node.setDefinicion(definicion);
		return null;
	}

	/**
	 * M�todo auxiliar opcional para ayudar a implementar los predicados de la
	 * Gram�tica Atribuida.
	 * 
	 * Ejemplo de uso: predicado(variables.get(nombre), "La variable no ha sido
	 * definida", expr.getStart()); predicado(variables.get(nombre), "La variable no
	 * ha sido definida", null);
	 * 
	 * NOTA: El m�todo getStart() indica la linea/columna del fichero fuente de
	 * donde se ley� el nodo. Si se usa VGen dicho m�todo ser� generado en
	 * todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion
	 *            Debe cumplirse para que no se produzca un error
	 * @param mensajeError
	 *            Se imprime si no se cumple la condici�n
	 * @param posicionError
	 *            Fila y columna del fichero donde se ha producido el error. Es
	 *            opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Identificacion", mensajeError, posicionError);
	}

	private Map<String, DefFuncion> funciones = new HashMap<String, DefFuncion>();
	private Map<String, DefStruct> structs = new HashMap<String, DefStruct>();
	private ContextMap<String, DefVariable> variables = new ContextMap<String, DefVariable>();
	private GestorErrores gestorErrores;
}
