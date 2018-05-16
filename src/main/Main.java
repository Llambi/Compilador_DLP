package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import ast.AST;
import generacionDeCodigo.GeneracionDeCodigo;
import semantico.AnalisisSemantico;
import visitor.ASTPrinter;

/**
 * Clase que inicia el compilador e invoca a todas sus fases.
 * 
 * No es necesario modificar este fichero. En su lugar hay que modificar: - Para
 * An�lisis Sint�ctico: 'sintactico/sintac.y' y 'sintactico/lexico.l' - Para
 * An�lisis Sem�ntico: 'semantico/Identificacion.java' y
 * 'semantico/ComprobacionDeTipos.java' - Para Generaci�n de C�digo:
 * 'generacionDeCodigo/GestionDeMemoria.java' y
 * 'generacionDeCodigo/SeleccionDeInstrucciones.java'
 *
 * @author Ra�l Izquierdo
 * 
 */
public class Main
{
//	public static final String programa = "src/resources/Hipoteca.txt";
//  Entrada a usar durante el desarrollo
//	public static final String programa = "src/resources/ejemplo.txt";
//	public static final String programa = "src/resources/1. Funciones.txt";
//	public static final String programa = "src/resources/2. Estructuras.txt";
//	public static final String programa = "src/resources/3. Variables.txt";
//	public static final String programa = "src/resources/Tipos.txt";
//	public static final String programa = "src/resources/memoria.txt";
//	public static final String programa = "src/resources/CodigoBasico.txt";
//	public static final String programa = "src/resources/Test1.txt";
//	public static final String programa = "src/resources/Test2.txt";
//	public static final String programa = "src/resources/Test3.txt";
//	public static final String programa = "src/resources/Test4.txt";
	public static final String programa = "src/resources/pruebaFinal.txt";

	public static void main(String[] args) throws Exception {
		GestorErrores gestor = new GestorErrores();

		AST raiz = compile(programa, gestor); // Poner args[0] en vez de "programa" en la versi�n final
		if (!gestor.hayErrores())
			System.out.println("El programa se ha compilado correctamente.");

		ASTPrinter.toHtml(programa, raiz, "Traza arbol"); // Utilidad generada por VGen (opcional)
	}

	/**
	 * M�todo que coordina todas las fases del compilador
	 */
	public static AST compile(String sourceName, GestorErrores gestor) throws Exception {

		// 1. Fases de An�lisis L�xico y Sint�ctico
		Yylex lexico = new Yylex(new FileReader(sourceName), gestor);
		Parser sintactico = new Parser(lexico, gestor, false);
		sintactico.parse();

		AST raiz = sintactico.getAST();
		if (raiz == null) // Hay errores o el AST no se ha implementado a�n
			return null;

		// 2. Fase de An�lisis Sem�ntico
		AnalisisSemantico semantico = new AnalisisSemantico(gestor);
		semantico.analiza(raiz);
		if (gestor.hayErrores())
			return raiz;

		// 3. Fase de Generaci�n de C�digo
		File sourceFile = new File(sourceName);
		Writer out = new FileWriter(new File(sourceFile.getParent(), "00-salida.txt"));

		GeneracionDeCodigo generador = new GeneracionDeCodigo();
		generador.genera(sourceFile.getName(), raiz, out);
		out.close();

		return raiz;
	}
}
