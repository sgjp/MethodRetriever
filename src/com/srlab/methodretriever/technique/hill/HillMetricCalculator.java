package com.srlab.methodretriever.technique.hill;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.ContinueStatement;

import com.srlab.methodretriever.utility.BigIntegerHammingDistanceCalculator;
import com.srlab.methodretriever.utility.Parameters;
import com.srlab.methodretriever.utility.SimHash;



public class HillMetricCalculator {

	private int lines;
	private int numOfArguments;
	private BigInteger typeHashCode;
	private int cyclometicComplexity;
	private String methodReturnType;
	private HashMap<Integer,Integer> hmBasicTokenFrequencyCount;
	private String cloneClassId;
	private String cloneId;

	public HillMetricCalculator(String methodBody, double queryMethodLenght, String cloneId, String cloneClassId) {
		this.methodReturnType = null;
		this.lines = -1;
		this.numOfArguments = -1;
		this.typeHashCode = null;
		this.cyclometicComplexity = 1;
		this.hmBasicTokenFrequencyCount = new HashMap();
		this.cloneClassId = cloneClassId;
		this.cloneId = cloneId;
		
		this.parse(methodBody);
		this.processTokens(methodBody, queryMethodLenght);
	}
	
	public HillMetricCalculator(String methodBody, double queryMethodLenght) {
		this.methodReturnType = null;
		this.lines = -1;
		this.numOfArguments = -1;
		this.typeHashCode = null;
		this.cyclometicComplexity = 1;
		this.hmBasicTokenFrequencyCount = new HashMap();
		
		this.parse(methodBody);
		this.processTokens(methodBody, queryMethodLenght);
	}
	
	public String getCloneClassId() {
		return cloneClassId;
	}

	public String getCloneId() {
		return cloneId;
	}

	public double getEuclideanDistance(HillMetricCalculator metricCalculator){
		double result=0;
		result = result + (this.getLines()-metricCalculator.getLines())*(this.getLines()-metricCalculator.getLines());
		result = result + (this.getNumOfArguments()-metricCalculator.getNumOfArguments())*(this.getNumOfArguments()-metricCalculator.getNumOfArguments());
		result = result + (this.getCyclometicComplexity()-metricCalculator.getCyclometicComplexity())*(this.getCyclometicComplexity()-metricCalculator.getCyclometicComplexity());
		
		int distance = BigIntegerHammingDistanceCalculator.calculate(this.getTypeHashCode(),metricCalculator.getTypeHashCode());
		result = result + (distance*distance);
		
		int vector1[] = this.getFeatureVector();
		int vector2[] = metricCalculator.getFeatureVector();
		
		for(int i=0;i<vector1.length; i++){
			result = result+ ((vector1[i]-vector2[i])*(vector1[i]-vector2[i]));
		}
		
		return Math.sqrt(result);
	}
	
	public double getEuclideanDistance(int lines, int numOfArguments, int cyclometicComplexity, BigInteger typeHashCode,int vector2[]){
		double result=0;
		result = result + (this.getLines()-lines)*(this.getLines()-lines);
		result = result + (this.getNumOfArguments()-numOfArguments)*(this.getNumOfArguments()-numOfArguments);
		result = result + (this.getCyclometicComplexity()-cyclometicComplexity)*(this.getCyclometicComplexity()- cyclometicComplexity);
		
		int distance = BigIntegerHammingDistanceCalculator.calculate(this.getTypeHashCode(),typeHashCode);
		result = result + (distance*distance);
		
		int vector1[] = this.getFeatureVector();
		
		for(int i=0;i<vector1.length; i++){
			result = result+ ((vector1[i]-vector2[i])*(vector1[i]-vector2[i]));
		}
		
		return Math.sqrt(result);
	}
	
	public int[] getFeatureVector(){
		int vector[] = new int[Parameters.HillTotalFeatures];
		vector[0]=0;
		vector[1]=0;
		vector[2]=0;
		vector[3]=0;
		for(int token:this.hmBasicTokenFrequencyCount.keySet()){
			int position = FeaturePositionFinder.getInstance().getFeaturePosition(token);
			vector[position]=this.hmBasicTokenFrequencyCount.get(token);
		}
		return vector;
	}
	private void incrementCyclometicComplexityValue() {
		this.cyclometicComplexity = this.cyclometicComplexity + 1;
	}

	private int getNumberOfLines(String input) {
		String[] lines = input.split("\r\n|\r|\n");
		return lines.length;
	}

	private void processTokens(String input, double queryMethodLenght){
		
		String inputShortened="";
		String[] queryLines = input.split("\n");
		int linesToTest = (int) Math.round(queryLines.length * queryMethodLenght);
		for(int i=0 ; i < linesToTest ; i++){
			if (i==0){
				inputShortened=queryLines[i];
			}else{
				inputShortened = inputShortened + "\n" + queryLines[i];
			}
		}
		input = inputShortened;
		
		
		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(input.toCharArray());
		int token;
		try {
			while(true){
				try{
					token = scanner.getNextToken();
					if (token == ITerminalSymbols.TokenNameEOF) break;
					if(hmBasicTokenFrequencyCount.containsKey(token)){
						int count = hmBasicTokenFrequencyCount.get(token);
						hmBasicTokenFrequencyCount.put(token,count+1);
					}
					else{
						hmBasicTokenFrequencyCount.put(token,1);
					}
					//System.out.println(token + " : " + new String(scanner.getCurrentTokenSource()));
				}catch (InvalidInputException e){
					if(!e.getMessage().equals("Invalid_Float_Literal") && !e.getMessage().equals("Invalid_Hexa_Literal") && !e.getMessage().equals("Unterminated_Comment")){
						throw new InvalidInputException(e.getMessage());
					}
				}
			
			}
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	public void parse(final String methodBody) throws ClassCastException{
		//System.out.println(methodBody);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(methodBody.toCharArray());
		parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		
		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);

		final TypeDeclaration cu = (TypeDeclaration) parser.createAST(null);
		cu.accept(new ASTVisitor() {

			public boolean visit(MethodDeclaration methodDeclaration) {

				HillMetricCalculator.this.setLines(HillMetricCalculator.this
						.getNumberOfLines(methodBody));
				HillMetricCalculator.this.setNumOfArguments(methodDeclaration
						.parameters().size());
				if(methodDeclaration.getReturnType2()!=null){
				HillMetricCalculator.this.setMethodReturnType(methodDeclaration
						.getReturnType2().toString());
				HillMetricCalculator.this.setTypeHashCode(new SimHash(methodDeclaration
						.getReturnType2().toString(), 64).simHash());
				}
				else{
					HillMetricCalculator.this.setMethodReturnType("empty");
					HillMetricCalculator.this.setTypeHashCode(new SimHash("empty", 64).simHash());
				}
				return true;
			}

			public boolean visit(IfStatement ifStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				if (ifStatement.getElseStatement() != null) {
					Statement elseStatement = ifStatement.getElseStatement();
					if (!(elseStatement instanceof IfStatement))
						HillMetricCalculator.this
								.incrementCyclometicComplexityValue();
				}
				return true;
			}

			public boolean visit(ForStatement forStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(EnhancedForStatement enhancedForStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(DoStatement doStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(WhileStatement whileStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(ContinueStatement continueStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(BreakStatement breakStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(ReturnStatement returnStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(SwitchCase switchCaseStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(TryStatement tryStatement) {
				for(Object catchClause:tryStatement.catchClauses()){
					HillMetricCalculator.this.incrementCyclometicComplexityValue();
				}
				if(tryStatement.getFinally()!=null){
					HillMetricCalculator.this.incrementCyclometicComplexityValue();						
				}
				return true;
			}
			
			public boolean visit(ThrowStatement throwStatement) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				return true;
			}

			public boolean visit(InfixExpression infixExpression) {
				if ((infixExpression.getOperator() == InfixExpression.Operator.AND)
						|| (infixExpression.getOperator() == InfixExpression.Operator.OR)) {
					HillMetricCalculator.this.incrementCyclometicComplexityValue();
				}
				return true;
			}

			public boolean visit(ConditionalExpression conditionalExpression) {
				HillMetricCalculator.this.incrementCyclometicComplexityValue();
				if (conditionalExpression.getElseExpression() != null) {
					HillMetricCalculator.this.incrementCyclometicComplexityValue();
				}
				return true;
			}
		});
	}

	public void print() {
		System.out.println("Lines: " + this.getLines());
		System.out.println("Num of Argument: " + this.getNumOfArguments());
		System.out.println("Return Type: " + this.getMethodReturnType());
		System.out.println("Cyclometic Complexity: "
				+ this.getCyclometicComplexity());

	}

	public int getNumOfArguments() {
		return numOfArguments;
	}

	public void setNumOfArguments(int numOfArguments) {
		this.numOfArguments = numOfArguments;
	}

	public BigInteger getTypeHashCode() {
		return typeHashCode;
	}

	public void setTypeHashCode(BigInteger typehashCode) {
		this.typeHashCode = typehashCode;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getCyclometicComplexity() {
		return cyclometicComplexity;
	}

	public void setCyclometicComplexity(int cyclometicComplexity) {
		this.cyclometicComplexity = cyclometicComplexity;
	}

	public String getMethodReturnType() {
		return methodReturnType;
	}

	public void setMethodReturnType(String methodReturnType) {
		this.methodReturnType = methodReturnType;
	}

	public static void main(String args[]) {
		String methodBody = "public int add(int x){" + "\nif(x==0) return 1;"
				+ "\nelse if(x==2) return 2;" + "\nelse return 3;}";
		String methodBody2="public void readFile(FileReader f){BufferedReader br = new BufferedReader(new FileReader(\"file.txt\"));"+
				"\ntry {"+
				"\nStringBuilder sb = new StringBuilder();"+
				"\nString line = br.readLine();"+
				"\nwhile (line != null) {"+
				"\nsb.append(line);"+
				"\nsb.append(System.lineSeparator());"+
				"\nline = br.readLine();"+
				"\n}"+
				"\nString everything = sb.toString();"+
				"\n} finally {"+
				"\nbr.close();"+
				"\n}}";
		
		String methodBody3="public void readFile(FileReader f){BufferedReader br = new BufferedReader(new FileReader(\"file.txt\"));"+
		"\ntry {"+
		"\nStringBuilder sb = new StringBuilder();"+
		"\nString line = br.readLine();"+
		"\nwhile (line != null) {"+
		"\nline = br.readLine();"+
		"\n}"+
		"\nString everything = sb.toString();"+
		"\n} finally {"+
		"\nbr.close();"+
		"\n}}";
		HillMetricCalculator mc1 = new HillMetricCalculator(methodBody2, 1.0);
		HillMetricCalculator mc2 = new HillMetricCalculator(methodBody3, 1.0);
		
		mc1.print();
		mc2.print();
		System.out.println("FeatureVector1: "+Arrays.toString(mc1.getFeatureVector()));
		System.out.println("FeatureVector2: "+Arrays.toString(mc2.getFeatureVector()));
		System.out.println("Distance: "+mc1.getEuclideanDistance(mc2));
	}
}
