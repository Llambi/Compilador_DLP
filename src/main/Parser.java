package main;
//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "sintac.y"
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast.AST;
import ast.Campo;
import ast.DefFuncion;
import ast.DefStruct;
import ast.DefVariable;
import ast.Definicion;
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
import ast.TipoChar;
import ast.TipoComplejo;
import ast.TipoInt;
import ast.TipoReal;
import ast.TipoVoid;
import ast.Token;
@SuppressWarnings("all")
//#line 24 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short RETURN=257;
public final static short WHILE=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short MENOSELSE=261;
public final static short ID=262;
public final static short STRUCT=263;
public final static short CHAR=264;
public final static short INT=265;
public final static short REAL=266;
public final static short VAR=267;
public final static short PRINT=268;
public final static short PRINTSP=269;
public final static short PRINTLN=270;
public final static short READ=271;
public final static short CAST=272;
public final static short AND=273;
public final static short LITCHAR=274;
public final static short LITENT=275;
public final static short LITREAL=276;
public final static short MENOSUNARIO=277;
public final static short IGUALDAD=278;
public final static short DESIGUALDAD=279;
public final static short MENORIGUAL=280;
public final static short MAYORIGUAL=281;
public final static short OR=282;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    6,    6,    7,    7,
    3,    8,    8,    8,    8,    8,    9,    9,    4,   10,
   10,   11,    5,   12,   12,   14,   14,   15,   13,   13,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   18,   18,   19,   19,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   17,
};
final static short yylen[] = {                            2,
    1,    0,    2,    1,    1,    1,    1,    0,    1,    2,
    5,    1,    1,    1,    1,    4,    2,    0,    6,    1,
    2,    4,    9,    0,    1,    1,    3,    3,    0,    2,
    4,    5,    3,    3,    3,    3,    3,    2,    7,    7,
   11,    1,    0,    3,    1,    3,    7,    1,    1,    1,
    2,    3,    3,    3,    3,    3,    1,    3,    4,    4,
    3,    3,    3,    3,    3,    3,    2,    3,    3,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    3,    4,    5,    6,    0,
    0,    0,    0,    0,    0,   26,    0,    0,    0,    0,
    0,    0,    0,   20,   12,   15,   14,   13,    0,    0,
   28,    0,    0,   27,    0,    0,   21,    0,   11,   17,
    0,    0,   19,    0,    9,   29,    0,   22,   16,    0,
   10,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   48,   49,   50,    0,    0,    0,   23,   30,    0,    0,
   38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   37,    0,    0,    0,    0,    0,   34,   35,   36,   33,
    0,   46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   58,    0,    0,    0,
    0,    0,    0,    0,   59,   31,   60,   29,   29,   32,
    0,    0,    0,    0,    0,   39,    0,   47,    0,   29,
    0,   41,
};
final static short yydgoto[] = {                          1,
    2,    6,    7,    8,    9,   46,   47,   30,   33,   23,
   24,   14,   50,   15,   16,   68,   69,  105,  106,
};
final static short yysindex[] = {                         0,
    0, -212,  -39, -259, -249,    0,    0,    0,    0, -232,
  -92,  -18,  -17,   -9,    9,    0, -205,  -54,  -54,    7,
 -232,   15, -113,    0,    0,    0,    0,    0, -217,   17,
    0,  -54,  -64,    0,  -54,   18,    0,   -3,    0,    0,
 -201,   19,    0,  -54,    0,    0, -201,    0,    0,  352,
    0,  307,   53,   55,   62,   51,   51,   51,   51,   43,
    0,    0,    0,   51,   51,   51,    0,    0,   64,   72,
    0,   76,   51,   51,   51,   97,  123,  134,  158,  -54,
  506,  -44,  186,   51,   51,   51,   51,   51,   51,   51,
   51,   51,   51,   51,   51,   51,   51, -158,   51,   51,
    0,  224,  250,  499,   74,   70,    0,    0,    0,    0,
   54,    0,  506,  595,  595,  595,  595,  506,  595,  595,
  160,  160,  -44,  -44,  -44,  326,    0,  383,   86,    5,
    6,   71,   51,   92,    0,    0,    0,    0,    0,    0,
  499,   51,  468,  532,  409,    0, -127,    0,   14,    0,
  555,    0,
};
final static short yyrindex[] = {                         0,
    0,  141,    0,    0,    0,    0,    0,    0,    0,  104,
    0,    0,    0,    0,  105,    0,    0,    0,    0,   24,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  585,    0,    0,    0,    0,    0,  606,    0,    0,    0,
    0,    0,    0,    0,  433,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
    0,    0,    0,    0,  107,    0,    0,    0,    0,    0,
  289,  -26,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  107,
    0,    0,    0,  -15,    0,  109,    0,    0,    0,    0,
    0,    0,  611,  774,  855,  882,  894,  778,  905,  919,
  640,  704,    1,   27,   38,    0,    0,    0,    0,    0,
    0,  473,    0,    0,    0,    0,    0,    0,    0,    0,
    8,    0,    0,    0,    0,    0,  633,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  -27,    0,    0,    0,    0,   73,    0,    0,
  128,    0, -111,    0,  131,    0,  936,   58,    0,
};
final static int YYTABLESIZE=1201;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   10,   98,   11,   57,   57,   57,   57,   57,   57,   57,
   51,   36,   12,   45,   51,   51,   51,   51,   51,   51,
   51,   57,   57,   57,   57,   45,  143,  144,   45,   13,
   17,   20,   51,   51,   51,   51,   29,   54,  151,   18,
   19,   54,   54,   54,   54,   54,   97,   54,   44,    3,
    4,   44,   21,   57,    5,   57,   22,   38,   41,   54,
   54,   54,   54,   55,   32,    5,   51,   55,   55,   55,
   55,   55,   35,   55,   56,   39,   43,   48,   56,   56,
   56,   56,   56,   64,   56,   55,   55,   55,   55,   44,
   66,   31,   73,   54,   74,   65,   56,   56,   56,   56,
   96,   75,   80,  127,   40,   94,   92,   42,   93,   98,
   95,  100,   96,  133,  132,  134,   49,   94,   92,   55,
   93,   98,   95,   91,   99,   90,  137,  138,  139,  140,
   56,  142,  149,   96,  101,   91,  150,   90,   94,   92,
    1,   93,   98,   95,   24,   25,   18,   43,   22,   42,
   37,   34,  111,    0,   97,  107,   91,  129,   90,   96,
    0,    0,    0,    0,   94,   92,   97,   93,   98,   95,
   96,    0,    0,    0,    0,   94,   92,    0,   93,   98,
   95,  108,   91,    0,   90,    0,    0,   97,    0,    0,
    0,    0,  109,   91,   96,   90,   96,    0,    0,   94,
   92,   94,   93,   98,   95,   98,   95,   25,    0,   26,
   27,   28,    0,   97,    0,    0,  110,   91,    0,   90,
    0,    0,   96,    0,   97,    0,  112,   94,   92,    0,
   93,   98,   95,    0,    0,   57,    0,    0,    0,    0,
   57,   57,   57,   57,   57,   91,   51,   90,   97,    0,
   97,   51,   51,   51,   51,   51,    0,    0,    0,    0,
   96,    0,    0,    0,  130,   94,   92,    0,   93,   98,
   95,    0,    0,   54,    0,    0,   97,    0,   54,   54,
   54,   54,   54,   91,    0,   90,   96,    0,    0,    0,
  131,   94,   92,    0,   93,   98,   95,    0,    0,   55,
    0,    0,    0,    0,   55,   55,   55,   55,   55,   91,
   56,   90,   70,    0,   97,   56,   56,   56,   56,   56,
    0,    0,   60,    0,   61,   62,   63,    0,    0,   67,
    0,    0,   67,    0,    0,    0,   84,    0,    0,   64,
   97,   85,   86,   87,   88,   89,   66,   67,   84,   67,
    0,   65,    0,   85,   86,   87,   88,   89,    0,    0,
    0,    0,   96,    0,    0,   71,    0,   94,   92,   84,
   93,   98,   95,    0,   85,   86,   87,   88,   89,    0,
    0,   67,    0,    0,   64,   91,    0,   90,    0,    0,
    0,   66,    0,    0,    0,   84,   65,    0,    0,    0,
   85,   86,   87,   88,   89,    0,   84,    0,    0,    0,
    0,   85,   86,   87,   88,   89,   97,    0,  135,   96,
    0,    0,    0,    0,   94,   92,    0,   93,   98,   95,
   84,    0,    0,    0,    0,   85,   86,   87,   88,   89,
    0,  136,   91,    0,   90,   96,    0,    0,    0,  148,
   94,   92,    0,   93,   98,   95,    0,    0,   84,    0,
    0,    0,    0,   85,   86,   87,   88,   89,   91,   57,
   90,    0,    0,   97,   57,   57,   67,   57,   57,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,   57,   57,    0,   84,    0,    0,   97,
   64,   85,   86,   87,   88,   89,    0,   66,    0,   60,
    0,    0,   65,    0,   60,   60,    0,   60,   60,   60,
    0,    0,   84,   57,    0,    0,    0,   85,   86,   87,
   88,   89,   60,   60,   60,   96,    0,    0,    0,    0,
   94,   92,   96,   93,   98,   95,    0,   94,   92,    0,
   93,   98,   95,    0,    0,    0,    0,    0,   91,    0,
   90,   67,    0,   60,   64,   91,    0,   90,   70,    0,
   67,   66,    0,    0,    0,    0,   65,    0,   60,    0,
   61,   62,   63,    0,    0,    0,    0,   64,    0,   97,
    0,    0,  146,    0,   66,    0,   97,    0,   84,   65,
    0,    0,    0,   85,   86,   87,   88,   89,   52,   53,
   54,    0,    0,   55,    0,    0,    0,    8,    0,   56,
   57,   58,   59,   60,    8,   61,   62,   63,    0,    8,
    0,   96,    0,    0,    0,    0,   94,   92,    7,   93,
   98,   95,    0,    0,    0,    7,    0,    0,    0,    0,
    7,   68,    0,    0,   68,   84,  147,    0,    0,    0,
   85,   86,   87,   88,   89,   40,    0,    0,    0,   68,
    0,   68,   40,    0,    0,    0,    0,   40,    0,  152,
   52,   84,   52,   52,   52,   97,   85,   86,   87,   88,
   89,    0,    0,    0,    0,    0,    0,    0,   52,   52,
   52,   52,    0,   68,    0,   57,    0,    0,    0,    8,
   57,   57,   57,   57,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   52,   53,   54,    0,    0,   55,
    7,    0,   52,    0,    0,   56,   57,   58,   59,   60,
    0,   61,   62,   63,   53,   60,   53,   53,   53,    0,
   60,   60,   60,   60,   60,    0,    0,   40,    0,    0,
    0,    0,   53,   53,   53,   53,    0,    0,    0,    0,
    0,   84,    0,    0,    0,    0,   85,   86,   87,   88,
   89,    0,    0,   85,   86,   87,   88,    0,   52,   53,
   54,    0,    0,   55,    0,    0,   53,    0,    0,   56,
   57,   58,   59,   60,    0,   61,   62,   63,    0,    0,
    0,   52,   53,   54,   66,    0,   55,   66,   69,    0,
    0,   69,   56,   57,   58,   59,   60,    0,   61,   62,
   63,    0,   66,   66,   66,   66,   69,    0,   69,    0,
    0,    8,    8,    8,    0,    0,    8,    0,    0,    0,
    0,    0,    8,    8,    8,    8,    8,    0,    8,    8,
    8,    0,    7,    7,    7,    0,   66,    7,    0,    0,
   69,    0,    0,    7,    7,    7,    7,    7,    0,    7,
    7,    7,    0,   68,    0,    0,    0,    0,    0,   40,
   40,   40,   68,    0,   40,   65,    0,    0,   65,    0,
   40,   40,   40,   40,   40,    0,   40,   40,   40,    0,
    0,    0,   52,   65,   65,   65,   65,   52,   52,   52,
   52,   52,   63,    0,    0,   63,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,   64,    0,    0,
   63,   63,   63,   63,    0,   62,    0,   65,   62,    0,
    0,    0,   64,   64,   64,   64,    0,    0,    0,   61,
    0,    0,   61,   62,   62,   62,   62,    0,    0,    0,
    0,    0,    0,    0,   63,    0,   53,   61,   61,   61,
   61,   53,   53,   53,   53,   53,   64,   72,    0,    0,
    0,   76,   77,   78,   79,    0,    0,   62,    0,   81,
   82,   83,    0,    0,    0,    0,    0,    0,  102,  103,
  104,   61,    0,    0,    0,    0,    0,    0,    0,  113,
  114,  115,  116,  117,  118,  119,  120,  121,  122,  123,
  124,  125,  126,    0,  128,  104,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   66,    0,    0,    0,
   69,   66,   66,   66,   66,   66,    0,    0,    0,   69,
    0,    0,    0,    0,    0,    0,    0,    0,  141,    0,
    0,    0,    0,    0,    0,    0,    0,  145,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   65,    0,    0,
    0,    0,   65,   65,   65,   65,   65,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,    0,   63,
   63,   63,   63,   63,    0,    0,   64,    0,    0,    0,
    0,   64,   64,   64,   64,   64,    0,   62,    0,    0,
    0,    0,   62,   62,   62,   62,   62,    0,    0,    0,
    0,   61,    0,    0,    0,    0,   61,   61,   61,   61,
   61,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   40,   46,  262,   41,   42,   43,   44,   45,   46,   47,
   37,  125,  262,   41,   41,   42,   43,   44,   45,   47,
   47,   59,   60,   61,   62,   41,  138,  139,   44,  262,
  123,   41,   59,   60,   61,   62,   91,   37,  150,   58,
   58,   41,   42,   43,   44,   45,   91,   47,   41,  262,
  263,   44,   44,   91,  267,   93,  262,  275,  123,   59,
   60,   61,   62,   37,   58,  267,   93,   41,   42,   43,
   44,   45,   58,   47,   37,   59,   59,   59,   41,   42,
   43,   44,   45,   33,   47,   59,   60,   61,   62,   93,
   40,   19,   40,   93,   40,   45,   59,   60,   61,   62,
   37,   40,   60,  262,   32,   42,   43,   35,   45,   46,
   47,   40,   37,   44,   41,   62,   44,   42,   43,   93,
   45,   46,   47,   60,   61,   62,   41,  123,  123,   59,
   93,   40,  260,   37,   59,   60,  123,   62,   42,   43,
    0,   45,   46,   47,   41,   41,  123,   41,  262,   41,
   23,   21,   80,   -1,   91,   59,   60,  100,   62,   37,
   -1,   -1,   -1,   -1,   42,   43,   91,   45,   46,   47,
   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,
   47,   59,   60,   -1,   62,   -1,   -1,   91,   -1,   -1,
   -1,   -1,   59,   60,   37,   62,   37,   -1,   -1,   42,
   43,   42,   45,   46,   47,   46,   47,  262,   -1,  264,
  265,  266,   -1,   91,   -1,   -1,   59,   60,   -1,   62,
   -1,   -1,   37,   -1,   91,   -1,   41,   42,   43,   -1,
   45,   46,   47,   -1,   -1,  273,   -1,   -1,   -1,   -1,
  278,  279,  280,  281,  282,   60,  273,   62,   91,   -1,
   91,  278,  279,  280,  281,  282,   -1,   -1,   -1,   -1,
   37,   -1,   -1,   -1,   41,   42,   43,   -1,   45,   46,
   47,   -1,   -1,  273,   -1,   -1,   91,   -1,  278,  279,
  280,  281,  282,   60,   -1,   62,   37,   -1,   -1,   -1,
   41,   42,   43,   -1,   45,   46,   47,   -1,   -1,  273,
   -1,   -1,   -1,   -1,  278,  279,  280,  281,  282,   60,
  273,   62,  262,   -1,   91,  278,  279,  280,  281,  282,
   -1,   -1,  272,   -1,  274,  275,  276,   -1,   -1,   41,
   -1,   -1,   44,   -1,   -1,   -1,  273,   -1,   -1,   33,
   91,  278,  279,  280,  281,  282,   40,   59,  273,   61,
   -1,   45,   -1,  278,  279,  280,  281,  282,   -1,   -1,
   -1,   -1,   37,   -1,   -1,   59,   -1,   42,   43,  273,
   45,   46,   47,   -1,  278,  279,  280,  281,  282,   -1,
   -1,   93,   -1,   -1,   33,   60,   -1,   62,   -1,   -1,
   -1,   40,   -1,   -1,   -1,  273,   45,   -1,   -1,   -1,
  278,  279,  280,  281,  282,   -1,  273,   -1,   -1,   -1,
   -1,  278,  279,  280,  281,  282,   91,   -1,   93,   37,
   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,
  273,   -1,   -1,   -1,   -1,  278,  279,  280,  281,  282,
   -1,   59,   60,   -1,   62,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,   -1,   -1,  273,   -1,
   -1,   -1,   -1,  278,  279,  280,  281,  282,   60,   37,
   62,   -1,   -1,   91,   42,   43,  125,   45,   46,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   61,   62,   -1,  273,   -1,   -1,   91,
   33,  278,  279,  280,  281,  282,   -1,   40,   -1,   37,
   -1,   -1,   45,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   -1,  273,   91,   -1,   -1,   -1,  278,  279,  280,
  281,  282,   60,   61,   62,   37,   -1,   -1,   -1,   -1,
   42,   43,   37,   45,   46,   47,   -1,   42,   43,   -1,
   45,   46,   47,   -1,   -1,   -1,   -1,   -1,   60,   -1,
   62,  273,   -1,   91,   33,   60,   -1,   62,  262,   -1,
  282,   40,   -1,   -1,   -1,   -1,   45,   -1,  272,   -1,
  274,  275,  276,   -1,   -1,   -1,   -1,   33,   -1,   91,
   -1,   -1,  125,   -1,   40,   -1,   91,   -1,  273,   45,
   -1,   -1,   -1,  278,  279,  280,  281,  282,  257,  258,
  259,   -1,   -1,  262,   -1,   -1,   -1,   33,   -1,  268,
  269,  270,  271,  272,   40,  274,  275,  276,   -1,   45,
   -1,   37,   -1,   -1,   -1,   -1,   42,   43,   33,   45,
   46,   47,   -1,   -1,   -1,   40,   -1,   -1,   -1,   -1,
   45,   41,   -1,   -1,   44,  273,  125,   -1,   -1,   -1,
  278,  279,  280,  281,  282,   33,   -1,   -1,   -1,   59,
   -1,   61,   40,   -1,   -1,   -1,   -1,   45,   -1,  125,
   41,  273,   43,   44,   45,   91,  278,  279,  280,  281,
  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,   60,
   61,   62,   -1,   93,   -1,  273,   -1,   -1,   -1,  125,
  278,  279,  280,  281,  282,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,  262,
  125,   -1,   93,   -1,   -1,  268,  269,  270,  271,  272,
   -1,  274,  275,  276,   41,  273,   43,   44,   45,   -1,
  278,  279,  280,  281,  282,   -1,   -1,  125,   -1,   -1,
   -1,   -1,   59,   60,   61,   62,   -1,   -1,   -1,   -1,
   -1,  273,   -1,   -1,   -1,   -1,  278,  279,  280,  281,
  282,   -1,   -1,  278,  279,  280,  281,   -1,  257,  258,
  259,   -1,   -1,  262,   -1,   -1,   93,   -1,   -1,  268,
  269,  270,  271,  272,   -1,  274,  275,  276,   -1,   -1,
   -1,  257,  258,  259,   41,   -1,  262,   44,   41,   -1,
   -1,   44,  268,  269,  270,  271,  272,   -1,  274,  275,
  276,   -1,   59,   60,   61,   62,   59,   -1,   61,   -1,
   -1,  257,  258,  259,   -1,   -1,  262,   -1,   -1,   -1,
   -1,   -1,  268,  269,  270,  271,  272,   -1,  274,  275,
  276,   -1,  257,  258,  259,   -1,   93,  262,   -1,   -1,
   93,   -1,   -1,  268,  269,  270,  271,  272,   -1,  274,
  275,  276,   -1,  273,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  282,   -1,  262,   41,   -1,   -1,   44,   -1,
  268,  269,  270,  271,  272,   -1,  274,  275,  276,   -1,
   -1,   -1,  273,   59,   60,   61,   62,  278,  279,  280,
  281,  282,   41,   -1,   -1,   44,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   41,   -1,   -1,   44,   -1,   -1,
   59,   60,   61,   62,   -1,   41,   -1,   93,   44,   -1,
   -1,   -1,   59,   60,   61,   62,   -1,   -1,   -1,   41,
   -1,   -1,   44,   59,   60,   61,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   93,   -1,  273,   59,   60,   61,
   62,  278,  279,  280,  281,  282,   93,   52,   -1,   -1,
   -1,   56,   57,   58,   59,   -1,   -1,   93,   -1,   64,
   65,   66,   -1,   -1,   -1,   -1,   -1,   -1,   73,   74,
   75,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   84,
   85,   86,   87,   88,   89,   90,   91,   92,   93,   94,
   95,   96,   97,   -1,   99,  100,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,   -1,   -1,   -1,
  273,  278,  279,  280,  281,  282,   -1,   -1,   -1,  282,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  133,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  142,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  273,   -1,   -1,
   -1,   -1,  278,  279,  280,  281,  282,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  273,   -1,   -1,   -1,   -1,  278,
  279,  280,  281,  282,   -1,   -1,  273,   -1,   -1,   -1,
   -1,  278,  279,  280,  281,  282,   -1,  273,   -1,   -1,
   -1,   -1,  278,  279,  280,  281,  282,   -1,   -1,   -1,
   -1,  273,   -1,   -1,   -1,   -1,  278,  279,  280,  281,
  282,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=282;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"RETURN","WHILE","IF","ELSE",
"MENOSELSE","ID","STRUCT","CHAR","INT","REAL","VAR","PRINT","PRINTSP","PRINTLN",
"READ","CAST","AND","LITCHAR","LITENT","LITREAL","MENOSUNARIO","IGUALDAD",
"DESIGUALDAD","MENORIGUAL","MAYORIGUAL","OR",
};
final static String yyrule[] = {
"$accept : programa",
"programa : listaDefinicion",
"listaDefinicion :",
"listaDefinicion : listaDefinicion definicion",
"definicion : definicionVariable",
"definicion : definicionStruct",
"definicion : definicionFuncion",
"definicionVariables : listaDefinicionVariables",
"definicionVariables :",
"listaDefinicionVariables : definicionVariable",
"listaDefinicionVariables : listaDefinicionVariables definicionVariable",
"definicionVariable : VAR ID ':' tipo ';'",
"tipo : ID",
"tipo : REAL",
"tipo : INT",
"tipo : CHAR",
"tipo : '[' LITENT ']' tipo",
"tipoRetorno : ':' tipo",
"tipoRetorno :",
"definicionStruct : STRUCT ID '{' listaCamposStruct '}' ';'",
"listaCamposStruct : camposStruct",
"listaCamposStruct : listaCamposStruct camposStruct",
"camposStruct : ID ':' tipo ';'",
"definicionFuncion : ID '(' parametros ')' tipoRetorno '{' definicionVariables listaSentencia '}'",
"parametros :",
"parametros : listaParametro",
"listaParametro : parametro",
"listaParametro : listaParametro ',' parametro",
"parametro : ID ':' tipo",
"listaSentencia :",
"listaSentencia : listaSentencia sentencia",
"sentencia : exp '=' exp ';'",
"sentencia : ID '(' argumentos ')' ';'",
"sentencia : READ exp ';'",
"sentencia : PRINT exp ';'",
"sentencia : PRINTSP exp ';'",
"sentencia : PRINTLN exp ';'",
"sentencia : RETURN exp ';'",
"sentencia : RETURN ';'",
"sentencia : WHILE '(' exp ')' '{' listaSentencia '}'",
"sentencia : IF '(' exp ')' '{' listaSentencia '}'",
"sentencia : IF '(' exp ')' '{' listaSentencia '}' ELSE '{' listaSentencia '}'",
"argumentos : listaExp",
"argumentos :",
"listaExp : listaExp ',' exp",
"listaExp : exp",
"exp : '(' exp ')'",
"exp : CAST '<' tipo '>' '(' exp ')'",
"exp : LITCHAR",
"exp : LITENT",
"exp : LITREAL",
"exp : '-' exp",
"exp : exp '+' exp",
"exp : exp '-' exp",
"exp : exp '*' exp",
"exp : exp '/' exp",
"exp : exp '%' exp",
"exp : ID",
"exp : exp '.' ID",
"exp : exp '[' exp ']'",
"exp : ID '(' argumentos ')'",
"exp : exp '<' exp",
"exp : exp '>' exp",
"exp : exp MENORIGUAL exp",
"exp : exp MAYORIGUAL exp",
"exp : exp DESIGUALDAD exp",
"exp : exp IGUALDAD exp",
"exp : '!' exp",
"exp : exp AND exp",
"exp : exp OR exp",
};

//#line 175 "sintac.y"
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
//#line 584 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "sintac.y"
{ raiz = new Programa(val_peek(0)); }
break;
case 2:
//#line 28 "sintac.y"
{ yyval = new ArrayList<Definicion>(); }
break;
case 3:
//#line 29 "sintac.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>)val_peek(1); definiciones.add((Definicion)val_peek(0)); yyval = definiciones;}
break;
case 4:
//#line 33 "sintac.y"
{ yyval = val_peek(0); }
break;
case 5:
//#line 34 "sintac.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 35 "sintac.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 39 "sintac.y"
{ yyval = val_peek(0); }
break;
case 8:
//#line 40 "sintac.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); yyval = definiciones; }
break;
case 9:
//#line 44 "sintac.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>();
															definiciones.add(((Definicion)val_peek(0))); 
															yyval = definiciones; }
break;
case 10:
//#line 48 "sintac.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) val_peek(1);
															definiciones.add((Definicion)val_peek(0));
															yyval = definiciones; }
break;
case 11:
//#line 54 "sintac.y"
{ yyval = new DefVariable(val_peek(3), ((Tipo)val_peek(1))); }
break;
case 12:
//#line 58 "sintac.y"
{ yyval = new TipoComplejo(val_peek(0)); }
break;
case 13:
//#line 59 "sintac.y"
{ yyval = new TipoReal(); }
break;
case 14:
//#line 60 "sintac.y"
{ yyval = new TipoInt(); }
break;
case 15:
//#line 61 "sintac.y"
{ yyval = new TipoChar(); }
break;
case 16:
//#line 62 "sintac.y"
{ yyval = new TipoArray(val_peek(2), val_peek(0)); }
break;
case 17:
//#line 66 "sintac.y"
{ yyval = val_peek(0); }
break;
case 18:
//#line 67 "sintac.y"
{ yyval = new TipoVoid();}
break;
case 19:
//#line 71 "sintac.y"
{ ArrayList<Campo> campos = new ArrayList<Campo>();
														campos.addAll((ArrayList<Campo>)(val_peek(2)));
														yyval = new DefStruct(val_peek(4), campos); }
break;
case 20:
//#line 77 "sintac.y"
{ ArrayList<Campo> campos = new ArrayList<Campo>(); campos.add((Campo)val_peek(0)); yyval = campos; }
break;
case 21:
//#line 78 "sintac.y"
{ ArrayList<Campo> campos = (ArrayList<Campo>) val_peek(1); campos.add((Campo)val_peek(0)); yyval = campos; }
break;
case 22:
//#line 82 "sintac.y"
{ yyval = new Campo(val_peek(3), val_peek(1)); }
break;
case 23:
//#line 86 "sintac.y"
{ 	yyval = new DefFuncion(val_peek(8), ((ArrayList<Definicion>)val_peek(6)), ((Tipo)val_peek(4)), ((ArrayList<Definicion>)val_peek(2)), ((ArrayList<Sentencia>)val_peek(1))); }
break;
case 24:
//#line 90 "sintac.y"
{ yyval = new ArrayList<Definicion>(); }
break;
case 25:
//#line 91 "sintac.y"
{ yyval = (ArrayList<Definicion>)val_peek(0); }
break;
case 26:
//#line 95 "sintac.y"
{ ArrayList<Definicion> definiciones = new ArrayList<Definicion>(); 
													definiciones.add((DefVariable)val_peek(0)); 
													yyval = definiciones; 
												}
break;
case 27:
//#line 99 "sintac.y"
{ ArrayList<Definicion> definiciones = (ArrayList<Definicion>) val_peek(2);
													definiciones.add((DefVariable)val_peek(0)); 
													yyval = definiciones;
												}
break;
case 28:
//#line 106 "sintac.y"
{ yyval = new DefVariable(val_peek(2), ((Tipo)val_peek(0))); }
break;
case 29:
//#line 110 "sintac.y"
{ yyval = new ArrayList<Sentencia>(); }
break;
case 30:
//#line 111 "sintac.y"
{ List<Sentencia> lista = (ArrayList<Sentencia>) val_peek(1);
											Sentencia sent = (Sentencia)val_peek(0);
											lista.add(sent);
											yyval = lista;	}
break;
case 31:
//#line 118 "sintac.y"
{ yyval = new SentenciaAsignacion( val_peek(3),  val_peek(1)); }
break;
case 32:
//#line 119 "sintac.y"
{ yyval = new SentenciaProcedimiento( val_peek(4), val_peek(2)); }
break;
case 33:
//#line 120 "sintac.y"
{ yyval = new SentenciaRead( val_peek(1)); }
break;
case 34:
//#line 121 "sintac.y"
{ yyval = new SentenciaPrint( val_peek(1)); }
break;
case 35:
//#line 122 "sintac.y"
{ yyval = new SentenciaPrintSP( val_peek(1)); }
break;
case 36:
//#line 123 "sintac.y"
{ yyval = new SentenciaPrintLN( val_peek(1)); }
break;
case 37:
//#line 124 "sintac.y"
{ yyval = new SentenciaReturn( val_peek(1)); }
break;
case 38:
//#line 125 "sintac.y"
{ yyval = new SentenciaReturn( null ); }
break;
case 39:
//#line 126 "sintac.y"
{ yyval = new SentenciaWhile( val_peek(4), val_peek(1)); }
break;
case 40:
//#line 127 "sintac.y"
{ yyval = new SentenciaIf( val_peek(4), val_peek(1), new ArrayList<Sentencia>()); }
break;
case 41:
//#line 128 "sintac.y"
{ yyval = new SentenciaIf( val_peek(8), val_peek(5), val_peek(1)); }
break;
case 42:
//#line 132 "sintac.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 133 "sintac.y"
{ yyval = new ArrayList<Expresion>(); }
break;
case 44:
//#line 137 "sintac.y"
{ List<Expresion> expresiones = (ArrayList<Expresion>)val_peek(2);
										Expresion expresion = (Expresion)val_peek(0);
										expresiones.add(expresion);
										yyval = expresiones; }
break;
case 45:
//#line 141 "sintac.y"
{ Expresion expresion = (Expresion)val_peek(0);
										List<Expresion> expresiones = new ArrayList<Expresion>();
										expresiones.add(expresion);
										yyval = expresiones; }
break;
case 46:
//#line 148 "sintac.y"
{ yyval = val_peek(1); }
break;
case 47:
//#line 149 "sintac.y"
{ yyval = new ExpresionCast(val_peek(4),  val_peek(1));}
break;
case 48:
//#line 150 "sintac.y"
{ yyval = new ExpresionChar(new TipoChar(), val_peek(0));}
break;
case 49:
//#line 151 "sintac.y"
{ yyval = new ExpresionInt(new TipoInt(),val_peek(0));}
break;
case 50:
//#line 152 "sintac.y"
{ yyval = new ExpresionReal(new TipoReal(), val_peek(0));}
break;
case 51:
//#line 153 "sintac.y"
{ yyval = new ExpresionMenosUnario(val_peek(0));}
break;
case 52:
//#line 154 "sintac.y"
{ yyval = new ExpresionAritmetica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 53:
//#line 155 "sintac.y"
{ yyval = new ExpresionAritmetica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 54:
//#line 156 "sintac.y"
{ yyval = new ExpresionAritmetica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 55:
//#line 157 "sintac.y"
{ yyval = new ExpresionAritmetica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 56:
//#line 158 "sintac.y"
{ yyval = new ExpresionAritmetica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 57:
//#line 159 "sintac.y"
{ yyval = new ExpresionVariable(val_peek(0)); }
break;
case 58:
//#line 160 "sintac.y"
{ yyval = new ExpresionAccesoCampo(val_peek(2), val_peek(0)); }
break;
case 59:
//#line 161 "sintac.y"
{ yyval = new ExpresionAccesoArray(val_peek(3), val_peek(1)); }
break;
case 60:
//#line 162 "sintac.y"
{ yyval = new ExpresionLlamadaFuncion(val_peek(3), val_peek(1)); }
break;
case 61:
//#line 163 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 62:
//#line 164 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 63:
//#line 165 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 64:
//#line 166 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 65:
//#line 167 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 66:
//#line 168 "sintac.y"
{ yyval = new ExpresionComparacion(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 67:
//#line 169 "sintac.y"
{ yyval = new ExpresionNegacion(val_peek(0)); }
break;
case 68:
//#line 170 "sintac.y"
{ yyval = new ExpresionLogica(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 69:
//#line 171 "sintac.y"
{ yyval = new ExpresionLogica(val_peek(2), val_peek(1), val_peek(0)); }
break;
//#line 1029 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
