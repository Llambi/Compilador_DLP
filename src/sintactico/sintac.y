%{
import java.io.*;
import java.util.*;
import ast.*;
import main.*;
import lexico.*;
@SuppressWarnings("all")
%}
%TOKEN RETURN WHILE IF ELSE MENOSELSE ID STRUCT CHAR INT REAL VAR PRINT PRINTSP PRINTLN READ CAST AND
%TOKEN LITCHAR LITENT LITREAL MENOSUNARIO IGUALDAD DESIGUALDAD MENORIGUAL MAYORIGUAL OR 

%nonassoc MENOSELSE
%nonassoc ELSE
%left '!' AND OR
%left '>' '<' MENORIGUAL MAYORIGUAL DESIGUALDAD IGUALDAD
%left '+' '-'
%left '*' '/' '%'
%right MENOSUNARIO
%left '['']''.'
%nonassoc '('')' 
%%

programa	
	:  listaDefinicion 					{ raiz = new Programa($1); }
	;

listaDefinicion
	:									{ $$ = new ArrayList<Definicion>(); }
	|	listaDefinicion definicion		{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>)$1; definiciones.add((Definicion)$2); $$ = definiciones;}
	;

definicion
	:	definicionVariable				{ $$ = $1; }
	|	definicionStruct				{ $$ = $1; }
	|	definicionFuncion				{ $$ = $1; }
	;

definicionVariables
	:	listaDefinicionVariables		{ $$ = $1; }
	|									{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); $$ = definiciones; }
	;	

listaDefinicionVariables
	:	definicionVariable								{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>();
															definiciones.add(((Definicion)$1)); 
															$$ = definiciones; }
															
	|	listaDefinicionVariables definicionVariable		{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) $1;
															definiciones.add((Definicion)$2);
															$$ = definiciones; }
	;

definicionVariable
	:	VAR ID ':' tipo ';'				{ $$ = new DefVariable($2, ((Tipo)$4)); }
	;

tipo
	:	ID								{ $$ = new TipoComplejo($1); }
	|	REAL							{ $$ = new TipoReal(); }
	|	INT								{ $$ = new TipoInt(); }
	|	CHAR							{ $$ = new TipoChar(); }
	|	'[' LITENT ']' tipo				{ $$ = new TipoArray($2, $4); }
	;

tipoRetorno
	:	':' tipo		{ $$ = $2; }
	|					{ $$ = new TipoVoid();}
	;

definicionStruct
	:	STRUCT ID '{' listaCamposStruct '}' ';'		{ ArrayList<Campo> campos = new ArrayList<Campo>();
														campos.addAll((ArrayList<Campo>)($4));
														$$ = new DefStruct($2, campos); }
	;

listaCamposStruct
	:	camposStruct								{ ArrayList<Campo> campos = new ArrayList<Campo>(); campos.add((Campo)$1); $$ = campos; }
	|	listaCamposStruct camposStruct				{ ArrayList<Campo> campos = (ArrayList<Campo>) $1; campos.add((Campo)$2); $$ = campos; }
	;

camposStruct
	:	ID ':' tipo ';'								{ $$ = new Campo($1, $3); }
	;

definicionFuncion
	:	ID '(' parametros ')'  tipoRetorno '{' definicionVariables listaSentencia '}'			{ 	$$ = new DefFuncion($1, ((ArrayList<Definicion>)$3), ((Tipo)$5), ((ArrayList<Definicion>)$7), ((ArrayList<Sentencia>)$8)); }
	;

parametros
	: 											{ $$ = new ArrayList<Definicion>(); }
	| listaParametro							{ $$ = (ArrayList<Definicion>)$1; }
	;

listaParametro
	:	parametro								{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); 
													definiciones.add((DefVariable)$1); 
													$$ = definiciones; 
												}
	|	listaParametro ',' parametro			{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) $1;
													definiciones.add((DefVariable)$3); 
													$$ = definiciones;
												}
	;

parametro
	:	ID ':' tipo			{ $$ = new DefVariable($1, ((Tipo)$3)); }
	;

listaSentencia
	:									{ $$ = new ArrayList<Sentencia>(); }
	| listaSentencia sentencia			{ List<Sentencia> lista = (ArrayList<Sentencia>) $1;
											Sentencia sent = (Sentencia)$2;
											lista.add(sent);
											$$ = lista;	}
	;

sentencia
	:	exp		'=' exp ';'															{ $$ = new SentenciaAsignacion( $1,  $3); }
	|	ID		'(' argumentos ')' ';'												{ $$ = new SentenciaProcedimiento( $1, $3); }
	|	READ 	exp ';'																{ $$ = new SentenciaRead( $2); }
	|	PRINT	exp ';'																{ $$ = new SentenciaPrint( $2); }
	|	PRINTSP	exp ';'																{ $$ = new SentenciaPrintSP( $2); }
	|	PRINTLN	exp ';'																{ $$ = new SentenciaPrintLN( $2); }
	|	RETURN  exp ';'																{ $$ = new SentenciaReturn( $2); }
	|	RETURN ';'																	{ $$ = new SentenciaReturn( null ); }
	|	WHILE 	'(' exp ')' '{' listaSentencia '}'									{ $$ = new SentenciaWhile( $3, $6); }
	|	IF 		'(' exp ')' '{' listaSentencia '}' 			%prec MENOSELSE			{ $$ = new SentenciaIf( $3, $6, new ArrayList<Sentencia>()); }
	|	IF 		'(' exp ')' '{' listaSentencia '}' ELSE '{' listaSentencia '}'		{ $$ = new SentenciaIf( $3, $6, $10); }
	;

argumentos
	:	listaExp									{ $$ = $1; }
	|												{ $$ = new ArrayList<Expresion>(); }
	;

listaExp
	:	listaExp ',' exp			{ List<Expresion> expresiones = (ArrayList<Expresion>)$1;
										Expresion expresion = (Expresion)$3;
										expresiones.add(expresion);
										$$ = expresiones; }
	|	exp							{ Expresion expresion = (Expresion)$1;
										List<Expresion> expresiones = new ArrayList<Expresion>();
										expresiones.add(expresion);
										$$ = expresiones; }
	;

exp
	:	'('exp')'									{ $$ = $2; }
	|	CAST '<'tipo'>' '('exp')'					{ $$ = new ExpresionCast($3,  $6);}
	|	LITCHAR										{ $$ = new ExpresionChar(new TipoChar(), $1);}
	|	LITENT										{ $$ = new ExpresionInt(new TipoInt(),$1);}
	|	LITREAL										{ $$ = new ExpresionReal(new TipoReal(), $1);}
	|	'-' 	exp		%prec MENOSUNARIO			{ $$ = new ExpresionMenosUnario($2);}
	|	exp		'+' 	exp	   						{ $$ = new ExpresionAritmetica($1, $2, $3); }
	|	exp 	'-'		exp	 						{ $$ = new ExpresionAritmetica($1, $2, $3); }
	|	exp 	'*'		exp	  						{ $$ = new ExpresionAritmetica($1, $2, $3); }
	|	exp 	'/' 	exp	 						{ $$ = new ExpresionAritmetica($1, $2, $3); }
	|	exp 	'%' 	exp							{ $$ = new ExpresionAritmetica($1, $2, $3); }
	|	ID											{ $$ = new ExpresionVariable($1); }
	|	exp		'.'		ID							{ $$ = new ExpresionAccesoCampo($1, $3); }
	|	exp		'[' exp ']'							{ $$ = new ExpresionAccesoArray($1, $3); }
	|	ID 		'(' argumentos ')'					{ $$ = new ExpresionLlamadaFuncion($1, $3); }
	|	exp 	'<' exp								{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	exp 	'>' exp	 							{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	exp 	MENORIGUAL 		exp					{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	exp 	MAYORIGUAL 		exp					{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	exp 	DESIGUALDAD 	exp					{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	exp 	IGUALDAD 		exp					{ $$ = new ExpresionComparacion($1, $2, $3); }
	|	'!' 	exp									{ $$ = new ExpresionNegacion($2); }
	|	exp 	AND 			exp					{ $$ = new ExpresionLogica($1, $2, $3); }
	|	exp 	OR 				exp					{ $$ = new ExpresionLogica($1, $2, $3); }
	;

%%
/* No es necesario modificar esta sección ------------------ */

public Parser(Yylex lex, GestorErrores gestor, boolean debug) {
	this(debug);
	this.lex = lex;
	this.gestor = gestor;
}

// Métodos de acceso para el main -------------
public int parse() { return yyparse(); }
public AST getAST() { return raiz; }

// Funciones requeridas por Yacc --------------
void yyerror(String msg) {
	Token lastToken = (Token) yylval;
	gestor.error("Sintáctico", "Token = " + lastToken.getToken() + ", lexema = \"" + lastToken.getLexeme() + "\". " + msg, lastToken.getStart());
}

int yylex() {
	try {
		int token = lex.yylex();
		yylval = new Token(token, lex.lexeme(), lex.line(), lex.column());
		return token;
	} catch (IOException e) {
		return -1;
	}
}

private Yylex lex;
private GestorErrores gestor;
private AST raiz;
