package generacionDeCodigo;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import ast.Campo;
import ast.DefFuncion;
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
import ast.Programa;
import ast.Sentencia;
import ast.SentenciaAsignacion;
import ast.SentenciaIf;
import ast.SentenciaPrint;
import ast.SentenciaPrintLN;
import ast.SentenciaPrintSP;
import ast.SentenciaProcedimiento;
import ast.SentenciaRead;
import ast.SentenciaReturn;
import ast.SentenciaWhile;
import ast.Tipo;
import ast.TipoArray;
import ast.TipoComplejo;
import ast.TipoVoid;
import visitor.DefaultVisitor;

public class SeleccionDeInstrucciones extends DefaultVisitor
{

	public SeleccionDeInstrucciones(Writer writer, String sourceFile)
	{
		this.writer = new PrintWriter(writer);
		this.sourceFile = sourceFile;
		this.contador = 0;
	}

	// class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {

		genera("#SOURCE \"" + this.sourceFile + "\"");
		genera("CALL main");
		genera("HALT");

		super.visit(node, param);

		return null;
	}

	// class DefFuncion { String identificador; List<DefVariable> parametros; Tipo
	// tipoRetorno; List<DefVariable> defLocales; List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {

		// super.visit(node, param);
		int sizeLocales = 0;
		int sizeParametros = 0;
		genera("#LINE " + node.getStart().getLine());
		genera(node.getIdentificador() + ":");

		for (DefVariable child : node.getParametros())
		{
			sizeParametros += child.getTipo().getSize();
		}

		for (DefVariable child : node.getDefLocales())
		{
			sizeLocales += child.getTipo().getSize();
		}

		genera("ENTER " + sizeLocales);

		if (node.getSentencias() != null)
			for (Sentencia child : node.getSentencias())
				child.accept(this, param);

		if (node.getTipoRetorno() instanceof TipoVoid)
			genera("RET 0, " + sizeLocales + ", " + sizeParametros);

		return null;
	}

	// class SentenciaAsignacion { Expresion left; Expresion right; }
	public Object visit(SentenciaAsignacion node, Object param) {

		// super.visit(node, param);
		genera("#LINE " + node.getEnd().getLine());
		if (node.getLeft() != null)
			node.getLeft().accept(this, "DIRECCION");

		if (node.getRight() != null)
			node.getRight().accept(this, "VALOR");
		genera("STORE" + node.getRight().getTipo().getSubfix());
		return null;
	}

	// class SentenciaPrint { Expresion expresion; }
	public Object visit(SentenciaPrint node, Object param) {

		// super.visit(node, param);
		genera("#LINE " + node.getEnd().getLine());
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera("OUT" + node.getExpresion().getTipo().getSubfix());
		return null;
	}

	// class SentenciaPrintSP { Expresion expresion; }
	public Object visit(SentenciaPrintSP node, Object param) {

		// super.visit(node, param);
		genera("#LINE " + node.getEnd().getLine());
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera("OUT" + node.getExpresion().getTipo().getSubfix());
		genera("PUSHB 32"); // Codigo espacio en blanco
		genera("OUTB");
		return null;
	}

	// class SentenciaPrintLN { Expresion expresion; }
	public Object visit(SentenciaPrintLN node, Object param) {

		// super.visit(node, param);
		genera("#LINE " + node.getEnd().getLine());
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera("OUT" + node.getExpresion().getTipo().getSubfix());
		genera("PUSHB 10"); // Codigo salto de linea
		genera("OUTB");
		genera("PUSHB 13"); // Codigo retorno de carro
		genera("OUTB");
		return null;
	}

	// class SentenciaRead { Expresion expresion; }
	public Object visit(SentenciaRead node, Object param) {

		// super.visit(node, param);
		genera("#line " + node.getEnd().getLine());
		node.getExpresion().accept(this, "DIRECCION");
		genera("IN");
		genera("STORE" + node.getExpresion().getTipo().getSubfix());
		return null;
	}

	// class SentenciaReturn { List<Expresion> expresion; }
	public Object visit(SentenciaReturn node, Object param) {

		// super.visit(node, param);
		int sizeLocales = 0;
		int sizeParametros = 0;

		for (DefVariable child : node.getScope().getParametros())
		{
			sizeParametros += child.getTipo().getSize();
		}

		for (DefVariable child : node.getScope().getDefLocales())
		{
			sizeLocales += child.getTipo().getSize();
		}

		if (node.getExpresion() == null)
		{
			genera("RET 0, " + sizeLocales + ", " + sizeParametros);
		}
		else
		{
			genera("#line " + node.getEnd().getLine());
			node.getExpresion().accept(this, "VALOR");
			genera("RET " + node.getExpresion().getTipo().getSize() + ", " + sizeLocales + ", " + sizeParametros);
		}

		return null;
	}

	// class SentenciaWhile { Expresion condicion; List<Sentencia> sentencias; }
	public Object visit(SentenciaWhile node, Object param) {

		// super.visit(node, param);
		genera("#line " + node.getStart().getLine());
		int count = nextCont();
		genera("inicioWhile" + count + ":");
		node.getCondicion().accept(this, "VALOR");
		genera("JZ finWhile" + count);

		for (Sentencia child : node.getSentencias())
			child.accept(this, param);
		genera("JMP inicioWhile" + count);

		genera("finWhile" + count + ":");
		return null;
	}

	// class SentenciaIf { Expresion condicion; List<Sentencia> sentenciasIf;
	// List<Sentencia> sentenciasElse; }
	public Object visit(SentenciaIf node, Object param) {

		// super.visit(node, param);
		genera("#line " + node.getEnd().getLine());
		int count = nextCont();
		node.getCondicion().accept(this, "VALOR");

		if (!node.getSentenciasElse().isEmpty() )
		{
			genera("JZ else" + count);
		}
		else
		{
			genera("JZ finif" + count);
		}

		for (int i = 0; i < node.getSentenciasIf().size(); i++)
		{
			node.getSentenciasIf().get(i).accept(this, param);
			if (i == node.getSentenciasIf().size() - 1 && !(node.getSentenciasIf().get(i) instanceof SentenciaReturn))
			{
				genera("JMP finif" + count);
			}
		}

		if (!node.getSentenciasElse().isEmpty())
		{
			genera("else" + count + ":");
			for (Sentencia child : node.getSentenciasElse())
				child.accept(this, param);
		}

		genera("finif" + count + ":");

		return null;
	}

	// class SentenciaProcedimiento { String identificador; List<Expresion> entrada;
	// }
	public Object visit(SentenciaProcedimiento node, Object param) {

		// super.visit(node, param);
		genera("#line " + node.getEnd().getLine());
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, param);
		genera("CALL " + node.getIdentificador());
		if (!(node.getDefinicion().getTipoRetorno() instanceof TipoVoid))
		{
			genera("POP" + node.getDefinicion().getTipoRetorno().getSubfix());
		}
		return null;
	}

	// class ExpresionCast { Tipo tipo; Expresion expresion; }
	public Object visit(ExpresionCast node, Object param) {

		// super.visit(node, param);
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera(node.getExpresion().getTipo().getSubfix() + "2" + node.getTipo().getSubfix());
		return null;
	}

	// class ExpresionChar { Tipo tipo; String value; }
	public Object visit(ExpresionChar node, Object param) {

		// super.visit(node, param);
		if ("'\\n'".equals(node.getValue()))
		{
			genera("PUSHB 10");
		}
		else
		{
			genera("PUSHB " + (int) (node.getValue().charAt(1)));
		}
		return null;
	}

	// class ExpresionInt { Tipo tipo; String value; }
	public Object visit(ExpresionInt node, Object param) {

		// super.visit(node, param);
		genera("PUSHI " + node.getValue());
		return null;
	}

	// class ExpresionReal { Tipo tipo; String value; }
	public Object visit(ExpresionReal node, Object param) {

		// super.visit(node, param);
		genera("PUSHF " + node.getValue());
		return null;
	}

	// class ExpresionAritmetica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionAritmetica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, "VALOR");

		if (node.getRight() != null)
			node.getRight().accept(this, "VALOR");
		switch (node.getSymbol())
		{
		case "+":
			genera("ADD" + node.getTipo().getSubfix());
			break;
		case "-":
			genera("SUB" + node.getTipo().getSubfix());
			break;
		case "*":
			genera("MUL" + node.getTipo().getSubfix());
			break;
		case "/":
			genera("DIV" + node.getTipo().getSubfix());
			break;
		default:
			break;
		}
		return null;
	}

	// class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		if (param != null)
			switch ((String) param)
			{
			case "DIRECCION":
				if (node.getDefinicion().getScope() == "local")
				{
					genera("PUSHA BP");
					genera("PUSH " + node.getDefinicion().getDireccion());
					genera("ADD");
				}
				else if (node.getDefinicion().getScope() == "parametro")
				{
					genera("PUSHA BP");
					genera("PUSH " + node.getDefinicion().getDireccion());
					genera("ADD");
				}
				else
				{
					genera("PUSHA " + node.getDefinicion().getDireccion());
				}
				break;
			case "VALOR":
				visit(node, "DIRECCION");
				genera("LOAD" + node.getTipo().getSubfix());
				break;
			default:
				break;
			}

		return null;
	}

	// class ExpresionAccesoArray { Expresion expresion1; Expresion expresion2; }
	public Object visit(ExpresionAccesoArray node, Object param) {

		// super.visit(node, param);
		if (node.getExpresion1() != null)
			node.getExpresion1().accept(this, "DIRECCION");
		genera("PUSH " + ((TipoArray) (node.getExpresion1().getTipo())).getTipo().getSize());
		if (node.getExpresion2() != null)
		{
			node.getExpresion2().accept(this, "VALOR");
		}
		genera("MUL");
		genera("ADD");
		if (param == "VALOR")
		{
			genera("LOAD" + ((TipoArray) node.getExpresion1().getTipo()).getTipo().getSubfix());
		}

		return null;
	}

	// class ExpresionAccesoCampo { Expresion expresion; String nombre; }
	public Object visit(ExpresionAccesoCampo node, Object param) {

		// super.visit(node, param);

		node.getExpresion().accept(this, "DIRECCION");
		// genera("PUSH " + ((Var)node.getStruct()).getDefinicion().getDireccion());
		List<Campo> lista = ((TipoComplejo) (node.getExpresion()).getTipo()).getDefinicion().getCampos();
		Tipo tipo = null;
		for (Campo var : lista)
		{
			if (var.getIdentificador().equals(node.getNombre()))
			{
				genera("PUSH " + var.getDireccion());
				tipo = var.getTipo();
			}
		}
		genera("ADD");
		if (param == "VALOR")
		{
			genera("LOAD" + tipo.getSubfix());
		}

		return null;
	}

	// class ExpresionLlamadaFuncion { String identificador; List<Expresion>
	// entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {

		// super.visit(node, param);
		genera("#LINE " + node.getEnd().getLine());
		if (node.getEntrada() != null)
			for (Expresion child : node.getEntrada())
				child.accept(this, "VALOR");
		genera("CALL " + node.getIdentificador());
		if (node.getDefinicion().getTipoRetorno() instanceof TipoVoid)
			genera("POP" + node.getDefinicion().getTipoRetorno().getSubfix());
		return null;
	}

	// class ExpresionComparacion { Expresion left; String symbol; Expresion right;
	// }
	public Object visit(ExpresionComparacion node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, "VALOR");

		if (node.getRight() != null)
			node.getRight().accept(this, "VALOR");

		switch (node.getSymbol())
		{
		case "<":
			genera("LT" + node.getLeft().getTipo().getSubfix());
			break;
		case "<=":
			genera("LE" + node.getLeft().getTipo().getSubfix());
			break;
		case ">":
			genera("GT" + node.getLeft().getTipo().getSubfix());
			break;
		case ">=":
			genera("GE" + node.getLeft().getTipo().getSubfix());
			break;
		case "==":
			genera("EQ" + node.getLeft().getTipo().getSubfix());
			break;
		case "!=":
			genera("NE" + node.getLeft().getTipo().getSubfix());
			break;
		default:
			break;
		}

		return null;
	}

	// class ExpresionLogica { Expresion left; String symbol; Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {

		// super.visit(node, param);

		if (node.getLeft() != null)
			node.getLeft().accept(this, "VALOR");

		if (node.getRight() != null)
			node.getRight().accept(this, "VALOR");

		switch (node.getSymbol())
		{
		case "&&":
			genera("AND");
			break;
		case "||":
			genera("OR");
			break;
		default:
			break;
		}

		return null;
	}

	// class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {

		// super.visit(node, param);

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera("NOT");
		return null;
	}

	// class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {

		// super.visit(node, param);
		genera("PUSH " + node.getExpresion().getTipo().getSubfix() + " 0");
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, "VALOR");
		genera("SUB" + node.getExpresion().getTipo().getSubfix());

		return null;
	}

	// Método auxiliar recomendado -------------
	private void genera(String instruccion) {
		writer.println(instruccion);
	}

	private int nextCont() {
		// int aux = contador;
		// contador++;
		// return aux;
		return contador++;
	}

	private int contador;
	private PrintWriter writer;
	private String sourceFile;
}
