/**
 * @generated VGen 1.3.3
 */

package visitor;

import java.io.*;

import ast.*;
import java.util.*;

/**
 * ASTPrinter. Utilidad que ayuda a validar un arbol AST:
 * 	-	Muestra la estructura del árbol en HTML.
 * 	-	Destaca los hijos/propiedades a null.
 * 	-	Muestra a qué texto apuntan las posiciones de cada nodo (linea/columna)
 * 		ayudando a decidir cual de ellas usar en los errores y generación de código.
 * 
 * Esta clase se genera con VGen. El uso de esta clase es opcional (puede eliminarse del proyecto). 
 * 
 */
public class ASTPrinter extends DefaultVisitor {

	/**
	 * toHtml. Muestra la estructura del AST indicando qué hay en las posiciones (linea y columna) de cada nodo.
	 * 
	 * @param sourceFile	El fichero del cual se ha obtenido el AST
	 * @param raiz				El AST creado a partir de sourceFile
	 * @param filename		Nombre del fichero HMTL a crear con la traza del AST
	 */

	public static void toHtml(String sourceFile, AST raiz, String filename) {
		toHtml(sourceFile, raiz, filename, 4);
	}
	
	public static void toHtml(AST raiz, String filename) {
		toHtml(null, raiz, filename);
	}

	// tabWidth deberían ser los espacios correspondientes a un tabulador en eclipse.
	// Normalmente no será necesario especificarlo. Usar mejor los dos métodos anteriores.
	public static void toHtml(String sourceFile, AST raiz, String filename, int tabWidth) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(filename.endsWith(".html") ? filename : filename + ".html"));
			generateHeader(writer);
			writer.println("[ASTPrinter] -------------------------------- line:col  line:col");
			if (raiz != null) {
				ASTPrinter tracer = new ASTPrinter(writer, loadLines(sourceFile, tabWidth));
				raiz.accept(tracer, new Integer(0));
			} else
				writer.println("raiz == null");
			writer.println(ls + ls + "[ASTPrinter] --------------------------------");
			generateFooter(writer);
			writer.close();
			System.out.println(ls + "ASTPrinter: Fichero '" + filename + ".html' generado con éxito. Abra el fichero para validar el árbol AST generado.");
		} catch (IOException e) {
			System.out.println(ls + "ASTPrinter: No se ha podido crear el fichero " + filename);
			e.printStackTrace();
		}
	}

	private static void generateHeader(PrintWriter writer) {
		writer.println("<html>\r\n" +
				"<head>\r\n" +
				"<style type=\"text/css\">\r\n" +
				".value { font-weight: bold; }\r\n" +
				".dots { color: #888888; }\r\n" +
				".type { color: #BBBBBB; }\r\n" +
				".pos { color: #CCCCCC; }\r\n" +
				".sourceText { color: #BBBBBB; }\r\n" +
				".posText {\r\n" +
				"	color: #BBBBBB;\r\n" +
				"	text-decoration: underline; font-weight: bold;\r\n" +
				"}\r\n" +
				".null {\r\n" +
				"	color: #FF0000;\r\n" +
				"	font-weight: bold;\r\n" +
				"	font-style: italic;\r\n" +
				"}\r\n" +
			//	 "pre { font-family: Arial, Helvetica, sans-serif; font-size: 11px; }\r\n" +
			//	"pre { font-size: 11px; }\r\n" +
				"</style>\r\n" +
				"</head>\r\n" +
				"\r\n" +
				"<body><pre>");
	}

	private static void generateFooter(PrintWriter writer) {
		writer.println("</pre>\r\n" +
				"</body>\r\n" +
				"</html>");
	}

	private ASTPrinter(PrintWriter writer, List<String> sourceLines) {
		this.writer = writer;
		this.sourceLines = sourceLines;
	}

	// ----------------------------------------------

	//	class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "Programa", node, false);

		visit(indent + 1, "definiciones", "List<Definicion>",node.getDefiniciones());
		return null;
	}

	//	class DefVariable { String identificador;  Tipo tipo; }
	public Object visit(DefVariable node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefVariable", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		print(indent + 1, "direccion", "int", node.getDireccion());
		return null;
	}

	//	class DefStruct { String identificador;  List<Campo> campos; }
	public Object visit(DefStruct node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefStruct", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "campos", "List<Campo>",node.getCampos());
		return null;
	}

	//	class DefFuncion { String identificador;  List<DefVariable> parametros;  Tipo tipoRetorno;  List<DefVariable> defLocales;  List<Sentencia> sentencias; }
	public Object visit(DefFuncion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefFuncion", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "parametros", "List<DefVariable>",node.getParametros());
		visit(indent + 1, "tipoRetorno", "Tipo",node.getTipoRetorno());
		visit(indent + 1, "defLocales", "List<DefVariable>",node.getDefLocales());
		visit(indent + 1, "sentencias", "List<Sentencia>",node.getSentencias());
		return null;
	}

	//	class Campo { String identificador;  Tipo tipo; }
	public Object visit(Campo node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "Campo", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		print(indent + 1, "direccion", "int", node.getDireccion());
		return null;
	}

	//	class TipoVoid {  }
	public Object visit(TipoVoid node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoVoid", node, true);

		return null;
	}

	//	class TipoInt {  }
	public Object visit(TipoInt node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoInt", node, true);

		return null;
	}

	//	class TipoReal {  }
	public Object visit(TipoReal node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoReal", node, true);

		return null;
	}

	//	class TipoChar {  }
	public Object visit(TipoChar node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoChar", node, true);

		return null;
	}

	//	class TipoArray { String tam;  Tipo tipo; }
	public Object visit(TipoArray node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoArray", node, false);

		print(indent + 1, "tam", "String", node.getTam());
		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		return null;
	}

	//	class TipoComplejo { String nombreTipo; }
	public Object visit(TipoComplejo node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "TipoComplejo", node, "nombreTipo", node.getNombreTipo());
		return null;
	}

	//	class SentenciaAsignacion { Expresion left;  Expresion right; }
	public Object visit(SentenciaAsignacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaAsignacion", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class SentenciaPrint { Expresion expresion; }
	public Object visit(SentenciaPrint node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrint", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaPrintSP { Expresion expresion; }
	public Object visit(SentenciaPrintSP node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrintSP", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaPrintLN { Expresion expresion; }
	public Object visit(SentenciaPrintLN node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrintLN", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaRead { Expresion expresion; }
	public Object visit(SentenciaRead node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaRead", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaReturn { List<Expresion> expresion; }
	public Object visit(SentenciaReturn node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaReturn", node, false);

		visit(indent + 1, "expresion", "List<Expresion>",node.getExpresion());
		return null;
	}

	//	class SentenciaWhile { Expresion condicion;  List<Sentencia> sentencias; }
	public Object visit(SentenciaWhile node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaWhile", node, false);

		visit(indent + 1, "condicion", "Expresion",node.getCondicion());
		visit(indent + 1, "sentencias", "List<Sentencia>",node.getSentencias());
		return null;
	}

	//	class SentenciaIf { Expresion condicion;  List<Sentencia> sentenciasIf;  List<Sentencia> sentenciasElse; }
	public Object visit(SentenciaIf node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaIf", node, false);

		visit(indent + 1, "condicion", "Expresion",node.getCondicion());
		visit(indent + 1, "sentenciasIf", "List<Sentencia>",node.getSentenciasIf());
		visit(indent + 1, "sentenciasElse", "List<Sentencia>",node.getSentenciasElse());
		return null;
	}

	//	class SentenciaProcedimiento { String identificador;  List<Expresion> entrada; }
	public Object visit(SentenciaProcedimiento node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaProcedimiento", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "entrada", "List<Expresion>",node.getEntrada());
		return null;
	}

	//	class ExpresionCast { Tipo tipo;  Expresion expresion; }
	public Object visit(ExpresionCast node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionCast", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class ExpresionChar { Tipo tipo;  String value; }
	public Object visit(ExpresionChar node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionChar", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		print(indent + 1, "value", "String", node.getValue());
		return null;
	}

	//	class ExpresionInt { Tipo tipo;  String value; }
	public Object visit(ExpresionInt node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionInt", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		print(indent + 1, "value", "String", node.getValue());
		return null;
	}

	//	class ExpresionReal { Tipo tipo;  String value; }
	public Object visit(ExpresionReal node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionReal", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		print(indent + 1, "value", "String", node.getValue());
		return null;
	}

	//	class ExpresionAritmetica { Expresion left;  String symbol;  Expresion right; }
	public Object visit(ExpresionAritmetica node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionAritmetica", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "symbol", "String", node.getSymbol());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExpresionVariable { String variable; }
	public Object visit(ExpresionVariable node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "ExpresionVariable", node, "variable", node.getVariable());
		return null;
	}

	//	class ExpresionAccesoArray { Expresion expresion1;  Expresion expresion2; }
	public Object visit(ExpresionAccesoArray node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionAccesoArray", node, false);

		visit(indent + 1, "expresion1", "Expresion",node.getExpresion1());
		visit(indent + 1, "expresion2", "Expresion",node.getExpresion2());
		return null;
	}

	//	class ExpresionAccesoCampo { Expresion expresion;  String nombre; }
	public Object visit(ExpresionAccesoCampo node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionAccesoCampo", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		print(indent + 1, "nombre", "String", node.getNombre());
		return null;
	}

	//	class ExpresionLlamadaFuncion { String identificador;  List<Expresion> entrada; }
	public Object visit(ExpresionLlamadaFuncion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionLlamadaFuncion", node, false);

		print(indent + 1, "identificador", "String", node.getIdentificador());
		visit(indent + 1, "entrada", "List<Expresion>",node.getEntrada());
		return null;
	}

	//	class ExpresionComparacion { Expresion left;  String symbol;  Expresion right; }
	public Object visit(ExpresionComparacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionComparacion", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "symbol", "String", node.getSymbol());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExpresionLogica { Expresion left;  String symbol;  Expresion right; }
	public Object visit(ExpresionLogica node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionLogica", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "symbol", "String", node.getSymbol());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExpresionNegacion { Expresion expresion; }
	public Object visit(ExpresionNegacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionNegacion", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class ExpresionMenosUnario { Expresion expresion; }
	public Object visit(ExpresionMenosUnario node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExpresionMenosUnario", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	// -----------------------------------------------------------------
	// Métodos invocados desde los métodos visit -----------------------

	private void printName(int indent, String name, AST node, boolean empty) {
		String text = ls + tabula(indent) + name + " &rarr;  ";
		text = String.format("%1$-" + 93 + "s", text);
		if (empty)
			text = text.replace(name, valueTag(name));
		writer.print(text + getPosition(node));
	}

	private void print(int indent, String name, String type, Object value) {
		write(indent, formatValue(value) + "  " + typeTag(type));
	}

	private void print(int indent, String attName, String type, List<? extends Object> children) {
		write(indent, attName + "  " + typeTag(type) + " = ");
		if (children != null)
			for (Object child : children)
				write(indent + 1, formatValue(child));
		else
			writer.print(" " + valueTag(null));
	}

	// Versión compacta de una linea para nodos que solo tienen un atributo String
	private void printCompact(int indent, String nodeName, AST node, String attName, Object value) {
		String fullName = nodeName + '.' + attName;
		String text = ls + tabula(indent) + '\"' + value + "\"  " + fullName;
		text = String.format("%1$-" + 88 + "s", text);
		// text = text.replace(value.toString(), valueTag(value));
		text = text.replace(fullName, typeTag(fullName));
		writer.print(text + getPosition(node));
	}

	private void visit(int indent, String attName, String type, List<? extends AST> children) {
		write(indent, attName + "  " + typeTag(type) + " = ");
		if (children != null)
			for (AST child : children)
				child.accept(this, indent + 1);
		else
			writer.print(" " + valueTag(null));
	}

	private void visit(int indent, String attName, String type, AST child) {
		if (child != null)
			child.accept(this, new Integer(indent));
		else
			write(indent, valueTag(null) + "  " + attName + ':' + typeTag(type));
	}

	// -----------------------------------------------------------------
	// Métodos auxiliares privados -------------------------------------

	private void write(int indent, String text) {
		writer.print(ls + tabula(indent) + text);
	}

	private static String tabula(int count) {
		StringBuffer cadena = new StringBuffer("<span class=\"dots\">");
		for (int i = 0; i < count; i++)
			cadena.append(i % 2 == 0 && i > 0 ? "|  " : "·  ");
		return cadena.toString() + "</span>";
	}

	private String typeTag(String type) {
		if (type.equals("String"))
			return "";
		return "<span class=\"type\">" + type.replace("<", "&lt;").replace(">", "&gt;") + "</span>";
	}

	private String valueTag(Object value) {
		if (value == null)
			return "<span class=\"null\">null</span>";
		return "<span class=\"value\">" + value + "</span>";
	}

	private String formatValue(Object value) {
		String text = valueTag(value);
		if (value instanceof String)
			text = "\"" + text + '"';
		return text;
	}


	// -----------------------------------------------------------------
	// Métodos para mostrar las Posiciones -----------------------------

	private String getPosition(Traceable node) {
		String text = node.getStart() + "  " + node.getEnd();
		text = "<span class=\"pos\">" + String.format("%1$-" + 13 + "s", text) + "</span>";
		text = text.replace("null", "<span class=\"null\">null</span>");
		String sourceText = findSourceText(node);
		if (sourceText != null)
			text += sourceText;
		return text;
	}

	private String findSourceText(Traceable node) {
		if (sourceLines == null)
			return null;

		Position start = node.getStart();
		Position end = node.getEnd();
		if (start == null || end == null)
			return null;

		String afterText, text, beforeText;
		if (start.getLine() == end.getLine()) {
			String line = sourceLines.get(start.getLine() - 1);
			afterText = line.substring(0, start.getColumn() - 1);
			text = line.substring(start.getColumn() - 1, end.getColumn());
			beforeText = line.substring(end.getColumn());
		} else {
			String firstLine = sourceLines.get(start.getLine() - 1);
			String lastLine = sourceLines.get(end.getLine() - 1);

			afterText = firstLine.substring(0, start.getColumn() - 1);

			text = firstLine.substring(start.getColumn() - 1);
			text += "</span><span class=\"sourceText\">" + " ... " + "</span><span class=\"posText\">";
			text += lastLine.substring(0, end.getColumn()).replaceAll("^\\s+", "");

			beforeText = lastLine.substring(end.getColumn());
		}
		return "<span class=\"sourceText\">" + afterText.replaceAll("^\\s+", "")
				+ "</span><span class=\"posText\">" + text
				+ "</span><span class=\"sourceText\">" + beforeText + "</span>";
	}

	private static List<String> loadLines(String sourceFile, int tabWidth) {
		if (sourceFile == null)
			return null;
		try {
			String spaces = new String(new char[tabWidth]).replace("\0", " ");
			
			List<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			String line;
			while ((line = br.readLine()) != null)
				lines.add(line.replace("\t", spaces));
			br.close();
			return lines;
		} catch (FileNotFoundException e) {
			System.out.println("Warning. No se pudo encontrar el fichero fuente '" + sourceFile + "'. No se mostrará informaicón de posición.");
			return null;
		} catch (IOException e) {
			System.out.println("Warning. Error al leer del fichero fuente '" + sourceFile + "'. No se mostrará informaicón de posición.");
			return null;
		}
	}


	private List<String> sourceLines;
	private static String ls = System.getProperty("line.separator");
	private PrintWriter writer;
}

