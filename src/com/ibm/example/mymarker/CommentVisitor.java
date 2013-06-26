package com.ibm.example.mymarker;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LineComment;

public class CommentVisitor extends ASTVisitor {

    CompilationUnit compilationUnit;

    private IFile fileToSeekComments;
    
    private String[] source;

    public CommentVisitor(CompilationUnit compilationUnit, String[] source, IFile fileToSeekComments) {

        super();
        this.compilationUnit = compilationUnit;
        this.source = source;
        this.fileToSeekComments = fileToSeekComments;
    }

    public boolean visit(LineComment node) {

        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        String lineComment = source[startLineNumber].trim();

        System.out.println("Comentario da Linha " + lineComment);
        
        System.out.println("GEt start Position () " + node.getStartPosition());

        int startLineNumber2 = compilationUnit.getLineNumber(node.getStartPosition()) ;
        
        System.out.println("Numero da Linha que o Eclipse mostra..." + startLineNumber2);
        
        
        
		try {
			IMarker marker = this.fileToSeekComments.createMarker("com.ibm.mymarkers.mymarker");
			marker.setAttribute(IMarker.MESSAGE, "Please look at the above SQL UPDATE statement and then see where it is been used. Furthermore, remove it and use the generated code. For instance:\n" + 
					"				(i) Firstly create an instance of the Cliente object\n" + 
					"				(ii) Secondly, set all attributes of this object that you would like to update\n" + 
					"				(iii) Thirdly, create an instance of the JDBCClienteDAO object\n" + 
					"				(iv) Fourthly, call the method update\n" + 
					"				 Source-code example:\n" + 
					"				 Cliente arg = new Cliente();\n" + 
					"				 arg.setSomething();\n" + 
					"				 JDBCClienteDAO argDAO = new JDBCClienteDAO();\n" + 
					"				 argDAO.update(arg);");
	        marker.setAttribute(IMarker.PRIORITY, 0);
	        marker.setAttribute(IMarker.LINE_NUMBER, startLineNumber2);
	        marker.setAttribute(IMarker.SEVERITY, 0);
	        marker.setAttribute(IMarker.CHAR_START, node.getStartPosition());
	        marker.setAttribute(IMarker.CHAR_END, node.getStartPosition() + node.getLength());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        return true;
    }

    public boolean visit(BlockComment node) {

        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;

        StringBuffer blockComment = new StringBuffer();

        for (int lineCount = startLineNumber ; lineCount<= endLineNumber; lineCount++) {

            String blockCommentLine = source[lineCount].trim();
            blockComment.append(blockCommentLine);
            if (lineCount != endLineNumber) {
                blockComment.append("\n");
            }
        }

        System.out.println(blockComment.toString());

        return true;
    }

    public void preVisit(ASTNode node) {

    }
}