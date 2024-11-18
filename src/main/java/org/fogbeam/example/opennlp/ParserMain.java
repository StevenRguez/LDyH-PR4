package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.Span;

/**
 * @file ParserMain.java
 * @brief Programa principal para el análisis sintáctico de oraciones utilizando OpenNLP.
 *
 * Este programa carga un modelo de análisis sintáctico, procesa una oración de entrada
 * y genera un árbol sintáctico que describe su estructura gramatical.
 */
public class ParserMain {
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(ParserMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de análisis sintáctico, analiza una oración de entrada y
	 * genera un árbol sintáctico que representa su estructura gramatical. También muestra
	 * el árbol sintáctico en forma de texto y como un árbol codificado.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		InputStream modelIn = null; /**< Flujo de entrada para cargar el modelo de análisis sintáctico. */
		try {
			// Carga el modelo preentrenado de análisis sintáctico desde un archivo.
			modelIn = new FileInputStream("models/en-parser-chunking.bin");
			ParserModel model = new ParserModel(modelIn);

			// Inicializa el analizador sintáctico con el modelo cargado.
			Parser parser = ParserFactory.create(model);

			// Oración de entrada para analizar.
			String sentence = "The quick brown fox jumps over the lazy dog.";
			String[] tokens = sentence.split(" "); // Tokenización básica

			// Construcción inicial del árbol sintáctico
			Parse parse = new Parse(sentence,
					new Span(0, sentence.length()),
					AbstractBottomUpParser.INC_NODE,
					1,
					0);

			// Insertar tokens en el árbol de análisis
			int start = 0;
			for (int i = 0; i < tokens.length; i++) {
				int end = start + tokens[i].length();
				parse.insert(new Parse(sentence,
						new Span(start, end),
						AbstractBottomUpParser.TOK_NODE,
						1,
						i));
				start = end + 1; // Avanza al siguiente token (considera el espacio)
			}

			// Procesa el análisis sintáctico
			Parse result = parser.parse(parse);

			// Muestra el árbol de análisis en formato de texto
			if (LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("Parsed tree (text format): %s", result.toString()));
			}

			// Muestra el árbol de análisis en formato codificado
			if (LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info("Parsed tree (encoded format):");
				result.showCodeTree();
			}
		}
		catch (IOException e) {
			// Registrar detalles del error para depuración
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, String.format("Error loading the model: %s", e.getMessage()), e);
			}
		} finally {
			// Cierra el flujo de entrada del modelo si está abierto.
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					if (LOGGER.isLoggable(Level.WARNING)) {
						LOGGER.log(Level.WARNING, String.format("Error closing the model stream: %s", e.getMessage()), e);
					}
				}
			}
		}

		// Indica que el programa ha finalizado.
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Done");
		}
	}
}


