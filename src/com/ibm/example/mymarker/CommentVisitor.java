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
			marker.setAttribute(IMarker.MESSAGE, "Uma boa mensagem deve vir aqui....");
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