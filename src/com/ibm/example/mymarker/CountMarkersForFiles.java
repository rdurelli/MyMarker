package com.ibm.example.mymarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class CountMarkersForFiles implements IEditorActionDelegate {

	public CountMarkersForFiles() {
		super();
	}
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart editor) {		
	}
	
	
	private void createHighlightForIFile(IFile fileToCreateTheHighlight) throws JavaModelException {
		
		ICompilationUnit unit =  JavaCore.createCompilationUnitFrom(fileToCreateTheHighlight);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    parser.setSource(unit);
	    parser.setResolveBindings(true);
		CompilationUnit parse =  (CompilationUnit) parser.createAST(null);
		
		String[] source = unit.getSource().split("\n");
		
		for (Comment comment : (List<Comment>) parse.getCommentList()) {

	        comment.accept(new CommentVisitor(parse, source, fileToCreateTheHighlight));
	    }
		
	}
	
	/*
	 * 
	 * This is used to find all the markers for an IResource and any sub resources.
	 * Then output the number of markers that are returned
	 */
	@Override
	public void run(IAction action) {
		
				
			TreeSelection selection = MyMarkerFactory.getTreeSelection();
			if (selection.getFirstElement() instanceof IOpenable) {
				IResource resource = (IResource) ((IAdaptable) selection.getFirstElement()).getAdapter(IResource.class);
				
				
				
				if (resource instanceof IFile) {
					
					IFile fileToCreateTheHighlight = (IFile) resource;
					
					try {
						this.createHighlightForIFile(fileToCreateTheHighlight);
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//chamar uma classe para cuidar de IFILE
					
					
				} else if (resource instanceof IProject) {
					
					//chamar uma classe para cuidar de IProject..
					
				}
				
				IFile fileTeste = (IFile) resource;
				
				ICompilationUnit unit =  JavaCore.createCompilationUnitFrom(fileTeste);
				
				
				
				ASTParser parser = ASTParser.newParser(AST.JLS3);
			    parser.setKind(ASTParser.K_COMPILATION_UNIT);
			    parser.setSource(unit);
			    parser.setResolveBindings(true);
				CompilationUnit parse =  (CompilationUnit) parser.createAST(null);	
				
				MethodVisitor visitor = new MethodVisitor();
			    parse.accept(visitor);
			    
			    String[] teste = null;
				try {
					teste = unit.getSource().split("\n");
				} catch (JavaModelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			    for (Comment comment : (List<Comment>) parse.getCommentList()) {

			        comment.accept(new CommentVisitor(parse, teste, fileTeste));
			    }
			    
			    for (MethodDeclaration method : visitor.getMethods()) {
			        System.out.print("Method name: " + method.getName()
			            + " Return type: " + method.getReturnType2());
			      }
				
				try {
					
					IMarker marker = fileTeste.createMarker(IMarker.TASK);
					marker.setAttribute(IMarker.MESSAGE, "TESTE DO IMAKAR");
		            marker.setAttribute(IMarker.PRIORITY, 2);
		            marker.setAttribute(IMarker.LINE_NUMBER, 18);
		            marker.setAttribute(IMarker.SEVERITY, 0);
		            
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
				}
				
				System.out.println("Nome do resource " + resource.getName());
				
				List<IMarker> markers = MyMarkerFactory.findAllMarkers(resource);
				
				MessageDialog dialog = new MessageDialog(MyMarkerPlugin.getShell(), "Marker Count", null, markers.size() + " marker(s)", MessageDialog.INFORMATION, new String[] {"OK"}, 0);
				dialog.open();
			}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
