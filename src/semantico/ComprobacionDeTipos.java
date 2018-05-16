package semantico;

import java.util.List;
import java.util.stream.Collectors;

import ast.Campo;
import ast.DefFuncion;
import ast.DefStruct;
import ast.DefVariable;
import ast.Expresion;
import ast.ExpresionAccesoArray;
import ast.ExpresionAccesoCampo;
import ast.ExpresionAritmetica;
import ast.ExpresionCast;
import ast.ExpresionChar;
import ast.ExpresionComparacion;
import ast.ExpresionInt;
import ast.ExpresionLlamadaFuncion;
import ast.ExpresionLogica;
import ast.ExpresionMenosUnario;
import ast.ExpresionNegacion;
import ast.ExpresionReal;
import ast.ExpresionVariable;
import ast.Position;
import ast.Sentencia;
import ast.SentenciaAsignacion;
import ast.SentenciaIf;
import ast.SentenciaPrint;
import ast.SentenciaPrintLN;
import ast.SentenciaPrintSP;
import ast.SentenciaRead;
import ast.SentenciaReturn;
import ast.SentenciaWhile;
import ast.Tipo;
import ast.TipoArray;
import ast.TipoChar;
import ast.TipoComplejo;
import ast.TipoInt;
import ast.TipoReal;
import ast.TipoVoid;
import main.GestorErrores;
import visitor.DefaultVisitor;

public class ComprobacionDeTipos extends DefaultVisitor
{

	public ComprobacionDeTipos(GestorErrores gestor)
	{
		this.gestorErrores = gestor;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {

		// super.visit(node, param);

		// super.visit(node, param);

		if (node.getParametros() != null)
			for (DefVariable child : node.getParametros())
			{
				child.accept(this, param);
				predicado(simpleTodo(child.getTipo()), "Los parametros deben ser simples", node.getStart());
			}

		if (node.getDefLocales() != null)
			for (DefVariable child : node.getDefLocales())
				child.accept(this, param);

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
			{
				child.setScope(node);
				child.accept(this, param);
			}

		if (node.getTipoRetorno() != null)
			node.getTipoRetorno().accept(this, param);

		if (node.getTipoRetorno() != null)
			predicado(simpleTodo(node.getTipoRetorno()), "El tipo de retorno debe ser simple", node.getStart());

		return null;
	}

	// class SentenciaAsignacion { Expresion left; Expresion right; }
	public Object visit(SentenciaAsignacion node, Object param) {

		super.visit(node, param);

		predicado(node.getLeft().isModificable(), "La variable debe ser modificable", node.getStart());
		predicado(simpleTodo(node.getLeft().getTipo()), "La variable debe ser simple", node.getStart());
		predicado(isIgualTipo(node.getLeft().getTipo(), node.getRight().getTipo()),
				"Los tipos debe coincidir [" + node.getLeft().getTipo() + ", " + node.getRight().getTipo() + "]", node.getStart());

		return null;
	}

	// class SentenciaPrint { Expresion expresion; }
	public Object visit(SentenciaPrint node, Object param) {

		super.visit(node, param);

		if (node.getExpresion() != null)
		{
			predicado(simpleTodo(node.getExpresion().getTipo()), "La Expresion a imprimir no es de tipo simple.", node.getExpresion().getStart());
		}
		return null;
	}

	// class SentenciaPrintSP { Expresion expresion; }
	public Object visit(SentenciaPrintSP node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
		{
			node.getExpresion().accept(this, param);
			predicado(simpleTodo(node.getExpresion().getTipo()), "La Expresion a imprimir no es de tipo simple.", node.getExpresion().getStart());
		}
		return null;
	}

	// class SentenciaPrintLN { Expresion expresion; }
	public Object visit(SentenciaPrintLN node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
		{
			node.getExpresion().accept(this, param);
			predicado(simpleTodo(node.getExpresion().getTipo()), "La Expresion a imprimir no es de tipo simple.", node.getExpresion().getStart());
		}
		return null;
	}

	// class SentenciaRead { Expresion expresion; }
	public Object visit(SentenciaRead node, Object param) {

		super.visit(node, param);

		predicado(simpleTodo(node.getExpresion().getTipo()), "Debe ser un tipo simple", node.getStart());
		predicado(node.getExpresion().isModificable(), "Debe ser moficable para read", node.getStart());

		return null;
	}

	// class SentenciaReturn { List<Expresion> expresion; }
	public Object visit(SentenciaReturn node, Object param) {

		super.visit(node, param);

		if (node.getScope() == null)
		{
			predicado(node.getExpresion() == null, "La funcion no debe tener return", node.getStart());
		}
		else
		{
			if (node.getExpresion() != null)
			{
				predicado(isIgualTipo(node.getExpresion().getTipo(), node.getScope().getTipoRetorno()), "El tipo de retorno debe coincidir",
						node.getStart());
			}
			else
			{
				predicado(node.getScope().getTipoRetorno() instanceof TipoVoid , "La funcion debe tener un retorno", node.getStart());
			}
		}
		return null;
	}

	// class SentenciaWhile { Expresion condicion; List<Sentencia> sentencias; }
	public Object visit(SentenciaWhile node, Object param) {

		// super.visit(node, param);

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias()){
				child.setScope(node.getScope());
				child.accept(this, param);
			}
		
		predicado(node.getCondicion().getTipo() instanceof TipoInt,
				"El tipo de la condición debe ser Int", node.getStart());

		return null;
		
	}

	// class SentenciaIf { Expresion condicion; List<Sentencia> sentenciasIf;
	// List<Sentencia> sentenciasElse; }
	public Object visit(SentenciaIf node, Object param) {

		// super.visit(node, param);

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);

		if (node.getSentenciasIf() != null)
			for (Sentencia child : node.getSentenciasIf())
			{
				child.setScope(node.getScope());
				child.accept(this, param);
			}

		if (node.getSentenciasElse() != null)
			for (Sentencia child : node.getSentenciasElse())
			{
				child.setScope(node.getScope());
				child.accept(this, param);
			}

		predicado(node.getCondicion().getTipo() instanceof TipoInt, "El tipo de la condición debe ser Int", node.getStart());

		return null;
	}

	// class ExpresionCast { Tipo tipo; Expresion expresion; }
	public Object visit(ExpresionCast node, Object param) {

		// super.visit(node, param);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);

		predicado(simpleTodo(node.getExpresion().getTipo()), "La expresion no es de tipo simple", node.getExpresion().getStart());
		predicado(simpleTodo(node.getTipoCast()), "El tipo del casteo no es valido", node.getStart());
		node.setTipo(node.getTipoCast());
		node.setModificable(false);
		return null;
	}

	// class ExpresionChar { Tipo tipo; String value; }
	public Object visit(ExpresionChar node, Object param) {

		// super.visit(node, param);
		node.setTipo(new TipoChar());
		node.setModificable(false);
		return null;
	}

	// class ExpresionInt { Tipo tipo; String value; }
	public Object visit(ExpresionInt node, Object param) {

		// super.visit(node, param);
		node.setTipo(new TipoInt());
		node.setModificable(false);
		return null;
	}

	// class ExpresionReal { Tipo tipo; String value; }
	public Object visit(ExpresionReal node, Object param) {

		// super.visit(node, param);
		node.setTipo(new TipoReal());
		node.setModificable(false);
		return null;
	}

	// class ExpresionAritmetica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionAritmetica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);

		if (node.getRight() != null)
			node.getRight().accept(this, param);
		predicado(simpleNumber(node.getLeft().getTipo()), "La expresion izquierda no es de tipo numerico.", node.getStart());
		predicado(simpleNumber(node.getRight().getTipo()), "La expresion derecha no es de tipo numerico.", node.getStart());
		node.setTipo(node.getLeft().getTipo());
		node.setModificable(false);
		return null;
	}

	// class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		
		//super.visit(node.getDefinicion(), param);

		node.setModificable(true);
		if(node.getDefinicion()!=null)
		node.setTipo(node.getDefinicion().getTipo());

		return null;
	}

	// class ExpresionAccesoArray { Expresion expresion1; Expresion expresion2; }
	public Object visit(ExpresionAccesoArray node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion1() != null)
			node.getExpresion1().accept(this, param);

		if (node.getExpresion2() != null)
			node.getExpresion2().accept(this, param);

		predicado(node.getExpresion1().getTipo() instanceof TipoArray, "La expresion no es un array.", node.getStart());
		predicado(node.getExpresion2().getTipo() instanceof TipoInt, "El indice no es de tipo entero", node.getStart());
		if (node.getExpresion1().getTipo() instanceof TipoArray)
			node.setTipo(((TipoArray) node.getExpresion1().getTipo()).getTipo());
		node.setModificable(true);
		return null;
	}

	// class ExpresionAccesoCampo { Expresion expresion; String nombre; }
	public Object visit(ExpresionAccesoCampo node, Object param) {

		// super.visit(node, param);
		if (node.getExpresion() != null)
		{
			node.getExpresion().accept(this, param);
			predicado(node.getExpresion().getTipo() instanceof TipoComplejo, "La expresion no es un tipo Complejo", node.getStart());
		}
		if (node.getExpresion().getTipo() instanceof TipoComplejo)
		{
			predicado(((TipoComplejo) node.getExpresion().getTipo()).getDefinicion() != null, "Estructura no definida",
					node.getExpresion().getStart());
			predicado(((TipoComplejo) node.getExpresion().getTipo()).getDefinicion().getCampos().stream()
					.anyMatch(campo -> campo.getIdentificador().equals(node.getNombre())), "Campo no definido", node.getStart());

			List<Campo> aux = ((TipoComplejo) node.getExpresion().getTipo()).getDefinicion().getCampos().stream()
					.filter(x -> x.getIdentificador().equals(node.getNombre())).collect(Collectors.toList());
			if (aux.size() > 0)
				node.setTipo(aux.get(0).getTipo());
			node.setModificable(true);
		}
		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {

		// super.visit(node, param);

		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);
		if (node.getEntrada().size() == node.getDefinicion().getParametros().size())
			for (int i = 0; i < node.getEntrada().size(); i++)
			{
				predicado(node.getEntrada().get(i).getTipo().getClass() == node.getDefinicion().getParametros().get(i).getTipo().getClass(),
						"El tipo del parametro: " + node.getDefinicion().getParametros().get(i).getIdentificador() + " No es correcto.",
						node.getEntrada().get(i).getStart());
			}
		node.setTipo(node.getDefinicion().getTipoRetorno());
		node.setModificable(false);
		return null;
	}

	// class ExpresionComparacion { Expresion left; String symbol; Expresion right;
	// }
	public Object visit(ExpresionComparacion node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
		{
			node.getLeft().accept(this, param);
			predicado(simpleNumber(node.getLeft().getTipo()), "El elemento izquierdo no es de tipo numerico", node.getLeft().getStart());
		}
		if (node.getRight() != null)
		{
			node.getRight().accept(this, param);
			predicado(node.getRight().getTipo().getClass() == node.getLeft().getTipo().getClass(),
					"El elemento derecho no es del mismo tipo que el izquierdo", node.getRight().getStart());
		}
		node.setTipo(new TipoInt());
		node.setModificable(false);
		return null;
	}

	// class ExpresionLogica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
		{
			node.getLeft().accept(this, param);
			predicado(node.getLeft().getTipo() instanceof TipoInt, "El elemento izquierdo no es del tipo int", node.getLeft().getStart());
		}
		if (node.getRight() != null)
		{
			node.getRight().accept(this, param);
			predicado(node.getRight().getTipo() instanceof TipoInt, "El elemento derecho no es del tipo int", node.getRight().getStart());
		}
		node.setTipo(new TipoInt());
		node.setModificable(false);
		return null;
	}

	// class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
		{
			node.getExpresion().accept(this, param);
			predicado(node.getExpresion().getTipo() instanceof TipoInt, "El elemento a negar no es del tipo int", node.getExpresion().getStart());
		}
		node.setTipo(new TipoInt());
		node.setModificable(false);
		return null;
	}

	// class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
		{
			node.getExpresion().accept(this, param);
			predicado(node.getExpresion().getTipo() instanceof TipoInt, "El elemento a negar no es del tipo int", node.getExpresion().getStart());
		}
		node.setTipo(new TipoInt());
		node.setModificable(false);
		return null;
	}

	/**
	 * Mï¿½todo auxiliar opcional para ayudar a implementar los predicados de la
	 * Gramï¿½tica Atribuida.
	 * 
	 * Ejemplo de uso (suponiendo implementado el mï¿½todo "esPrimitivo"):
	 * predicado(esPrimitivo(expr.tipo), "La expresiï¿½n debe ser de un tipo
	 * primitivo", expr.getStart()); predicado(esPrimitivo(expr.tipo), "La
	 * expresiï¿½n debe ser de un tipo primitivo", null);
	 * 
	 * NOTA: El mï¿½todo getStart() indica la linea/columna del fichero fuente de
	 * donde se leyï¿½ el nodo. Si se usa VGen dicho mï¿½todo serï¿½ generado en
	 * todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion
	 *            Debe cumplirse para que no se produzca un error
	 * @param mensajeError
	 *            Se imprime si no se cumple la condiciï¿½n
	 * @param posicionError
	 *            Fila y columna del fichero donde se ha producido el error. Es
	 *            opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Comprobacion de tipos", mensajeError, posicionError);
	}

	private boolean simpleNumber(Tipo node) {
		if (node instanceof TipoInt || node instanceof TipoReal)
		{
			return true;
		}
		return false;
	}

	private boolean simpleTodo(Tipo node) {
		if (node instanceof TipoInt || node instanceof TipoReal || node instanceof TipoChar || node instanceof TipoVoid)
		{
			return true;
		}
		return false;
	}

	private boolean isIgualTipo(Tipo tipo1, Tipo tipo2) {
		return tipo1 != null && tipo2 != null ? tipo1.getClass().isAssignableFrom(tipo2.getClass()) : false;
	}

	private GestorErrores gestorErrores;
}
