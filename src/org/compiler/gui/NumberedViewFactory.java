package org.compiler.gui;

import javax.swing.text.*;

// Referenced classes of package frames:
//            NumberedParagraphView

class NumberedViewFactory implements ViewFactory {

    NumberedViewFactory() {
    }

    public View create(Element elem) {
	String kind = elem.getName();
	if (kind != null) {
	    if (kind.equals("content"))
		return new LabelView(elem);
	    if (kind.equals("paragraph"))
		return new NumberedParagraphView(elem);
	    if (kind.equals("section"))
		return new BoxView(elem, 1);
	    if (kind.equals("component"))
		return new ComponentView(elem);
	    if (kind.equals("icon"))
		return new IconView(elem);
	}
	return new LabelView(elem);
    }
}