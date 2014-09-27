package org.compiler.gui;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class NumberedEditorKit extends StyledEditorKit {

	public NumberedEditorKit() {
	}

	public ViewFactory getViewFactory() {
		return new NumberedViewFactory();
	}

	private static final long serialVersionUID = 1L;
}