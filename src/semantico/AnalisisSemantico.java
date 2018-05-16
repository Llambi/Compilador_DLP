package semantico;

import ast.AST;
import main.GestorErrores;

/**
 * Esta clase coordina las dos fases del An�lisis Sem�ntico:
 * 1- Fase de Identificaci�n
 * 2- Fase de Inferencia
 * 
 * No es necesario modificar esta clase. En su lugar hay que modificar las clases
 * que son llamadas desde aqu�: "Identificacion.java" y "ComprobacionDeTipos.java"
 * 
 * @author Ra�l Izquierdo
 *
 */
public class AnalisisSemantico {
	
	public AnalisisSemantico(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}
	
	public void analiza(AST raiz) {
		Identificacion identificacion = new Identificacion(gestorErrores);
		raiz.accept(identificacion, null);

		if (gestorErrores.hayErrores())
			return;

		ComprobacionDeTipos comprobacion = new ComprobacionDeTipos(gestorErrores);
		raiz.accept(comprobacion, null);
	}

	private GestorErrores gestorErrores;
}
