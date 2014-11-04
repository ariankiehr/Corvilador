package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMResta {
	private static ASMResta instance = null;
	private List<String> sentencias;
	private String elemento;

	private ASMResta() {
		this.sentencias = new LinkedList<String>();
	}

	public static ASMResta getInstance() {
		if (instance == null) {
			instance = new ASMResta();
		}
		return instance;

	}

	public String getElemento() {
		return this.elemento;
	}

	public List<String> generarResta(String elemIzq, String elemDer) {
		this.sentencias = new LinkedList<String>();
		if (RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null) {
			// es un registro izq
			if (RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null) {
				// es registro der

				// REG - REG 3
				sentencias.add("SUB " + Names.getName(elemIzq) + ", "
						+ Names.getName(elemDer));
				RegistryManager.getInstance().desocuparRegistro(
						Names.getName(elemDer));
				this.elemento = Names.getName(elemIzq);

			} else {
				// es variable o consta der
				// REG - VAR 2
				sentencias.add("SUB " + Names.getName(elemIzq) + ", "
						+ Names.getName(elemDer));
				this.elemento = Names.getName(elemIzq);
			}

		} else {
			// es variable o constante izq

			if (RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null) {
				// es registro der
				// VAR -REG 4
				String reg = RegistryManager.getInstance().obtenerRegistro();
				sentencias.add("MOV " + reg + ", " + Names.getName(elemIzq));
				sentencias.add("SUB " + reg + ", " + Names.getName(elemDer));
				RegistryManager.getInstance().desocuparRegistro(
						Names.getName(elemDer));
				this.elemento = Names.getName(elemDer);

			} else {
				// es variable o consta der
				// VAR - VAR 1
				String regaux = RegistryManager.getInstance().obtenerRegistro();
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add("MOV " + regaux + ", " + Names.getName(elemIzq));
				sentencias.add("SUB " + regaux + ", " + Names.getName(elemDer));
				this.elemento = regaux;
			}

		}
		return this.sentencias;
	}
}
