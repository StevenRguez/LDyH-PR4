package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 * @file ParserMain.java
 * @brief Programa principal para el análisis sintáctico de oraciones utilizando OpenNLP.
 *
 * Este programa carga un modelo de análisis sintáctico, procesa una oración de entrada
 * y genera un árbol sintáctico que describe su estructura gramatical.
 */
public class ParserMain
{
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
	public static void main(String[] args) throws Exception
	{
		InputStream modelIn = null; /**< Flujo de entrada para cargar el modelo de análisis sintáctico. */

		try
		{
			// Carga el modelo preentrenado de análisis sintáctico desde un archivo.
			modelIn = new FileInputStream("models/en-parser-chunking.bin");
			ParserModel model = new ParserModel(modelIn);

			// Inicializa el analizador sintáctico con el modelo cargado.
			Parser parser = ParserFactory.create(model);

			// Oración de entrada para analizar.
			String sentence = "The quick brown fox jumps over the lazy dog .";

			// Analiza la oración y obtiene las representaciones de los árboles sintácticos.
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			// Selecciona el árbol sintáctico principal.
			Parse parse = topParses[0];

			// Muestra el árbol sintáctico en formato de texto.
			System.out.println(parse.toString());

			// Muestra el árbol sintáctico en formato codificado (por ejemplo, para depuración).
			parse.showCodeTree();

		}
		catch (IOException e)
		{
			// Maneja errores durante la carga del modelo o el análisis.
			e.printStackTrace();
		}
		finally
		{
			// Cierra el flujo de entrada del modelo si está abierto.
			if (modelIn != null)
			{
				try
				{
					modelIn.close();
				}
				catch (IOException e)
				{
					// Maneja errores al cerrar el flujo de entrada.
				}
			}
		}

		// Indica que el programa ha finalizado.
		System.out.println("done");
	}
}
