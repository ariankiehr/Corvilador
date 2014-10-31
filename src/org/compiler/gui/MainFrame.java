package org.compiler.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.text.EditorKit;

import org.compiler.asm.CodeGenerator;
import org.compiler.lex.LexicalAnalyzer;
import org.compiler.symboltable.SymbolTable;
import org.compiler.syntactic.Parser;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	LexicalAnalyzer lexico;

	private JButton jButton2;
	private JTextArea simbolos;
	private JTextArea syntaxErrors;
	private JTextArea tokens;
	private JTextArea errores;
	private JTextArea warnings;
	private JTextArea syntaxTree;
	private JTextArea syntaxDetection;
	private JScrollPane jScrollPane6;
	private JScrollPane jScrollPane5;
	private JScrollPane jScrollPane4;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane7;
	private JScrollPane jScrollPane8;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane9;
	private JPanel jPanel6;
	private JPanel jPanel5;
	private JPanel jPanel4;
	private JPanel jPanel3;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JTabbedPane jTabbedPane1;
	private JMenuItem jMenu1;
	private JMenuBar jMenuBar1;

	private JEditorPane jEditorPane3;
	private EditorKit editor;
	private File file = null;

	public static void init() {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				MainFrame inst = new MainFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}

		});

	}

	public MainFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		editor = new NumberedEditorKit();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(2);
			getContentPane().setLayout(new BorderLayout(10,10));
			setResizable(false);
			setTitle("Compilador");
			jMenuBar1 = new JMenuBar();
			setJMenuBar(jMenuBar1);
			jMenu1 = new JMenuItem();
			jMenuBar1.add(jMenu1);
			jMenu1.setText("Abrir");
			jMenu1.addActionListener(this);

			jScrollPane3 = new JScrollPane();
			getContentPane().add(jScrollPane3, BorderLayout.WEST);

			jScrollPane2 = new JScrollPane();
			jScrollPane3.setViewportView(jScrollPane2);

			jScrollPane2.setPreferredSize(new Dimension(600, 324));
			jEditorPane3 = new JEditorPane();
			jEditorPane3.setEditorKit(editor);
			jEditorPane3.setEditable(false);
			jScrollPane2.setViewportView(jEditorPane3);
			
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(jTabbedPane1 , BorderLayout.SOUTH);
			jPanel1 = new JPanel();
			jTabbedPane1.addTab("Tokens", null, jPanel1, null);
			jPanel1.setLayout(null);
			jScrollPane1 = new JScrollPane();
			jPanel1.add(jScrollPane1);
			jScrollPane1.setBounds(12, 12, 1165, 68);
			tokens = new JTextArea();
			jScrollPane1.setViewportView(tokens);
			tokens.setEditable(false);
			tokens.setFont(new Font("Lucida Console", 0, 11));
			jPanel2 = new JPanel();
			jTabbedPane1.addTab("Warnings", null, jPanel2, null);
			jPanel2.setLayout(null);
			jScrollPane4 = new JScrollPane();
			jPanel2.add(jScrollPane4);
			jScrollPane4.setBounds(12, 12, 1165, 68);
			warnings = new JTextArea();
			jScrollPane4.setViewportView(warnings);
			warnings.setEditable(false);
			jPanel3 = new JPanel();
			jTabbedPane1.addTab("Lexical errors", null, jPanel3, null);
			jPanel3.setLayout(null);
			jScrollPane5 = new JScrollPane();
			jPanel3.add(jScrollPane5);
			jScrollPane5.setBounds(12, 12, 1165, 68);
			errores = new JTextArea();
			jScrollPane5.setViewportView(errores);
			errores.setEditable(false);
			jPanel4 = new JPanel();
			jTabbedPane1.addTab("Symbol table", null, jPanel4, null);
			jPanel4.setLayout(null);
			jScrollPane6 = new JScrollPane();
			jPanel4.add(jScrollPane6);
			jScrollPane6.setBounds(12, 12, 1165, 68);
			simbolos = new JTextArea();
			jScrollPane6.setViewportView(simbolos);
			simbolos.setEditable(false);

			jPanel5 = new JPanel();
			jTabbedPane1.addTab("Syntax Error", null, jPanel5, null);
			jPanel5.setLayout(null);
			jScrollPane7 = new JScrollPane();
			jPanel5.add(jScrollPane7);
			jScrollPane7.setBounds(12, 12, 1165, 68);
			syntaxErrors = new JTextArea();
			jScrollPane7.setViewportView(syntaxErrors);
			syntaxErrors.setEditable(false);

			jPanel6 = new JPanel();
			jTabbedPane1.addTab("Syntax Detections", null, jPanel6, null);
			jPanel6.setLayout(null);
			jScrollPane8 = new JScrollPane();
			jPanel6.add(jScrollPane8);
			jScrollPane8.setBounds(12, 12, 1165, 68);
			syntaxDetection = new JTextArea();
			jScrollPane8.setViewportView(syntaxDetection);
			syntaxDetection.setEditable(false);
			
			
			jScrollPane9 = new JScrollPane();
			getContentPane().add(jScrollPane9, BorderLayout.EAST);

			JScrollPane jScrollPane20 = new JScrollPane();
			jScrollPane9.setViewportView(jScrollPane20);

			jScrollPane20.setPreferredSize(new Dimension(600, 324));
			syntaxTree = new JTextArea();
			syntaxTree.setEditable(false);
			jScrollPane20.setViewportView(syntaxTree);


			jButton2 = new JButton();
			getContentPane().add(jButton2, BorderLayout.NORTH);
			jButton2.setText("Compilar");

			jButton2.addActionListener(this);
			pack();
			setSize(1205, 700);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(jButton2)) {
			eventoCompilar();
		}
		if (ae.getSource().equals(jMenu1)) {
			eventoAbrir();
		}

	}

	private void eventoAbrir() {
		String aux = "";
		String texto = "";
		try {
			JFileChooser fileChosser = new JFileChooser();
			if (file != null) {
			    fileChosser.setCurrentDirectory(file);
			}
			fileChosser.showOpenDialog(this);
			file = fileChosser.getSelectedFile();

			if (file != null) {
				FileReader archivos = new FileReader(file);
				BufferedReader lee = new BufferedReader(archivos);
				while ((aux = lee.readLine()) != null) {
					texto += aux + "\n";
				}
				lee.close();
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex + ""
					+ "\nNo se ha encontrado el archivo", "ADVERTENCIA",
					JOptionPane.WARNING_MESSAGE);
		}

		jEditorPane3.setText(texto);
		clear();

	}

	private void clear() {
		tokens.setText("");
		warnings.setText("");
		errores.setText("");
		simbolos.setText("");
		syntaxDetection.setText("");
		syntaxErrors.setText("");
		syntaxTree.setText("");
	}

	private void eventoCompilar() {
	    	if(file==null) {
	    	JOptionPane.showMessageDialog(null, "No se ha encontrado archivo", "ADVERTENCIA",
			JOptionPane.WARNING_MESSAGE);
	    	    return;
	    	}
		clear();
		lexico = new LexicalAnalyzer(file);

		while (lexico.hasMoreTokens()) {
			tokens.append(lexico.nextToken().toString() + '\n');
		}
		lexico.setNextToken(0);

		Parser par = new Parser(false);
		par.parsear(lexico);
		
		for (String warning : LexicalAnalyzer.warnings) {
			warnings.append(warning + '\n');
		}

		for (String error : LexicalAnalyzer.errors) {
			errores.append(error + '\n');
		}

		simbolos.append(SymbolTable.getInstance().toString());

		for (String error : Parser.errors) {
			syntaxErrors.append(error + '\n');
		}

		for (String detection : Parser.detections) {
			syntaxDetection.append(detection + '\n');
		}
		if(Parser.tree == null || !Parser.errors.isEmpty() || !LexicalAnalyzer.errors.isEmpty()){ 
			syntaxTree.setText("No hay arbol");
		}else{
			syntaxTree.setText(Parser.tree.toString());

			new CodeGenerator(file);
		}
		

		SymbolTable.reset(); //se limpia la tabla de simbolos por si se abre otro archivo

	}

}