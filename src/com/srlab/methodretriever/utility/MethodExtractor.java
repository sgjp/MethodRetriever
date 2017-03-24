package com.srlab.methodretriever.utility;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.srlab.methodretriever.technique.hill.HillMetricCalculator;

//given a source file the class extracts method body
public class MethodExtractor extends ASTVisitor{

    public static int index = 0;
    private HashMap hmMethodMap;
    private File currentFile;
    private String resultsLocation;
    
	public MethodExtractor(File file, String resultsLocation){
		this.hmMethodMap = new HashMap();
		this.resultsLocation = resultsLocation;
		this.browse(file);
	}

	public void browse(File file){
		if(file.isFile()&& Utility.isExtensionMatches(file.getName())){
			try {
				this.process(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(file.isDirectory()){
			for(File f:file.listFiles()){
				this.browse(f);
			}
		}
		
	}

	//this method find the methods and save them
	public void process(File source) throws IOException{
		this.currentFile = source;
		String content = FileUtils.readFileToString(source);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(this);
	}
	
	public boolean visit(TypeDeclaration typeDeclaration){
		
		for(MethodDeclaration methodDeclaration:typeDeclaration.getMethods()){
		//	System.out.println("Method: "+methodDeclaration.toString());
			MethodInfo methodInfo = new MethodInfo(methodDeclaration, typeDeclaration);
			//methodInfo.print();
			this.save(methodDeclaration);
		}
		return true;
	}
	public boolean visit(ClassInstanceCreation ci){
		
		if(ci.getAnonymousClassDeclaration()!=null){
		List<BodyDeclaration> bodyDeclarationList=ci.getAnonymousClassDeclaration().bodyDeclarations();
			for(BodyDeclaration bodyDeclaration:bodyDeclarationList){
				if(bodyDeclaration instanceof MethodDeclaration){
					MethodDeclaration md = (MethodDeclaration)bodyDeclaration;
					MethodInfo methodInfo = new MethodInfo(md,ci);
					//methodInfo.print();
					this.save(md);
				}
			}
		}
				
		return true;
	}
	public void save(MethodDeclaration md){
		try {
		//	IOUtils.write(md.toString(), new BufferedWriter(new FileWriter(this.currentFile.getParent()+File.separator+index)));
			//IOUtils.write(md.toString(), new BufferedWriter(new FileWriter("results.txt")),true);
			
			HillMetricCalculator mc = new HillMetricCalculator(md.toString());
			FileUtils.writeStringToFile(new File(resultsLocation+"/"+index+".txt"), md.toString(), Charset.defaultCharset(), true);
			index++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
