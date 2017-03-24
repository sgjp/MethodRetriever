package com.srlab.methodretriever.utility;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodInfo {

	/**
	 * @param args
	 */
	private String methodName;
	private String returnType;
	private ArrayList<String> parameters;
	private String className;
	private String superClassName;
	private ArrayList<String> implementedInterfaces;
	private boolean anonymous;
	
	public MethodInfo(MethodDeclaration md,ASTNode container){
		
		this.parameters = new ArrayList();
		this.implementedInterfaces = new ArrayList();
		
		this.methodName=md.getName().getFullyQualifiedName();
		if(md.getReturnType2()!=null){
			this.returnType=md.getReturnType2().toString();
		}
		int numOfParameters=md.parameters().size();
		ArrayList<SingleVariableDeclaration> svdList = new ArrayList(md.parameters());
		
		for(SingleVariableDeclaration svd:svdList){
			parameters.add(svd.getType().toString());
		}
		//a container can be a type declaration or class instance creation for a anonymous class declaration
		if(container instanceof TypeDeclaration){
			TypeDeclaration typeDeclaration = (TypeDeclaration)container;
			this.className = typeDeclaration.getName().getFullyQualifiedName();
			if(typeDeclaration.getSuperclassType()!=null)
			this.superClassName = typeDeclaration.getSuperclassType().toString();
			if(typeDeclaration.superInterfaceTypes().size()>0){
				for(Object obj:typeDeclaration.superInterfaceTypes()){
					
					implementedInterfaces.add(obj.toString());
				}
			}
		}
		
		else if(container instanceof ClassInstanceCreation){
			ClassInstanceCreation ci =(ClassInstanceCreation)container;
			if(ci.getAnonymousClassDeclaration()!=null){
				this.anonymous = true;
				this.className = ci.getType().toString();
				
			}
		}
		
	}
	
	public void print(){
		System.out.println("Method Name:"+this.getMethodName()+" ReturnType: "+this.getReturnType()+" parameters: "+this.getParameters());
		System.out.println("IsAnonymous: "+this.isAnonymous()+" ClassName: "+this.className+" Super Class: "+this.getSuperClassName()+" Interfaces: "+this.getImplementedInterfaces());
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public ArrayList<String> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<String> parameters) {
		this.parameters = parameters;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public ArrayList<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void setImplementedInterfaces(ArrayList<String> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
