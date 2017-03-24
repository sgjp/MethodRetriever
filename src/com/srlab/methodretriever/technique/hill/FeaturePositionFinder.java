package com.srlab.methodretriever.technique.hill;

import java.util.HashMap;

import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

//if a token type it provides the feature position
public class FeaturePositionFinder {
	
	public static FeaturePositionFinder instance = null;
	private HashMap<Integer,Integer>  hmTokenTypeToPosition;
	
	private FeaturePositionFinder(){
		this.hmTokenTypeToPosition = new HashMap();
		this.init();
	}
	public static FeaturePositionFinder getInstance(){
		if(instance ==null){
			instance = new FeaturePositionFinder();
			return instance;
		}
		else return instance;
	}
	
	protected void init(){
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameabstract,4);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameAND,5);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameAND_AND,6);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameAND_EQUAL,7);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameassert,8);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameAT,9);
		hmTokenTypeToPosition.put( ITerminalSymbols.TokenNameboolean,10);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamebreak,11);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamebyte,12);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamebyte,13);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamecase,14);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamecatch,15);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamechar,16);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCharacterLiteral,17);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameclass,18);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOLON,19);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOMMA,20);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOMMENT_BLOCK,21);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOMMENT_BLOCK,22);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOMMENT_JAVADOC,23);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameCOMMENT_LINE,24);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameconst,25);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamecontinue,26);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamedefault,27);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamedefault,28);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameDIVIDE,29);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameDIVIDE_EQUAL,30);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamedo,31);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameDOT,32);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamedouble,33);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameDoubleLiteral,34);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameELLIPSIS,35);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameelse,36);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameenum,37);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameEOF,38);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameEQUAL,39);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameEQUAL_EQUAL,40);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameERROR,41);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameextends,42);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamefalse,43);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamefinal,44);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamefinally,45);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamefloat,46);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameFloatingPointLiteral,47);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamefor,48);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamegoto,49);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameGREATER,50);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameGREATER_EQUAL,51);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameIdentifier,52);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameif,53);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameimplements,54);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameimport,55);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameinstanceof,56);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameint,57);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameIntegerLiteral,58);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameinterface,59);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLBRACE,60);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLBRACKET,61);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLEFT_SHIFT,62);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLEFT_SHIFT_EQUAL,63);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLESS,64);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLESS_EQUAL,65);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamelong,66);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLongLiteral,67);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameLPAREN,68);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameMINUS,69);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameMINUS_EQUAL,70);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameMINUS_MINUS,71);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameMULTIPLY,72);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameMULTIPLY_EQUAL,73);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamenative,74);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamenew,75);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameNOT,76);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameNOT_EQUAL,77);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamenull,78);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameOR,79);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameOR_EQUAL,80);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameOR_OR,81);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamepackage,82);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamePLUS,83);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamePLUS_EQUAL,84);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamePLUS_PLUS,85);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameprivate,86);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameprotected,87);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamepublic,88);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameQUESTION,89);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameRBRACE,90);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameRBRACKET,91);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameREMAINDER,92);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameREMAINDER_EQUAL,93);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamereturn,94);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameRIGHT_SHIFT,95);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameRIGHT_SHIFT_EQUAL,96);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameRPAREN,97);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameSEMICOLON,98);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameshort,99);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamestatic,100);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamestrictfp,101);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameStringLiteral,102);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamesuper,103);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameswitch,104);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamesynchronized,105);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamethis,106);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamethrow,107);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamethrows,108);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNametransient,109);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNametrue,110);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNametry,111);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameTWIDDLE,112);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameUNSIGNED_RIGHT_SHIFT,113);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL,114);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamevoid,115);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamevolatile,116);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNamewhile,117);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameWHITESPACE,118);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameWHITESPACE,119);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameXOR,120);
		hmTokenTypeToPosition.put(ITerminalSymbols.TokenNameXOR_EQUAL,121);
	}
	
	public  int getFeaturePosition(int tokenType){
		if(this.hmTokenTypeToPosition.containsKey(tokenType)==false)
			return -1;
		else return this.hmTokenTypeToPosition.get(tokenType);
		
	}
}
