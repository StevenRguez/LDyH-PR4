package org.fogbeam.example.opennlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.util.logging.Level;
import java.util.logging.Logger;

// Ejemplo de uso: java TokenizerMain input1.txt input2.txt input3.txt output.txt
// mvn exec:java -Dexec.mainClass="org.fogbeam.example.opennlp.TokenizerMain" -Dexec.args="training_data/en-doccat.train salida.txt"

/**
 * @file TokenizerMain.java
 * @brief Programa principal para la tokenización de múltiples archivos de texto usando OpenNLP.
 *
 * Este programa permite procesar múltiples archivos de texto proporcionados como argumentos,
 * aplica un modelo de tokenización para separar el texto en tokens, y guarda el resultado
 * en un archivo de salida único.
 */
public class TokenizerMain
{
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(TokenizerMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo recibe una lista de archivos de texto como entrada, procesa el contenido
	 * de cada archivo utilizando un modelo de tokenización, y escribe los tokens generados
	 * en un archivo de salida único.
	 *
	 * @param args Argumentos de línea de comandos:
	 *             - Nombres de archivos de entrada.
	 *             - Último argumento debe ser el nombre del archivo de salida.
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		// Validar que se hayan proporcionado al menos un archivo de entrada y uno de salida.
		if (args.length < 2)
		{
			System.err.println("Uso: java TokenizerMain <archivo1> <archivo2> ... <archivoSalida>");
			System.exit(1);
		}

		// Nombre del archivo de salida.
		String outputFileName = args[args.length - 1];

		// Lista de archivos de entrada.
		List<File> inputFiles = new ArrayList<>();
		for (int i = 0; i < args.length - 1; i++)
		{
			inputFiles.add(new File(args[i]));
		}

		// Cargar el modelo de tokenización.
		InputStream modelIn = new FileInputStream("models/en-token.model");
		TokenizerModel model = new TokenizerModel(modelIn);
		Tokenizer tokenizer = new TokenizerME(model);

		try (FileWriter writer = new FileWriter(outputFileName))
		{
			// Procesar cada archivo de entrada.
			for (File inputFile : inputFiles)
			{
				if (!inputFile.exists())
				{
					System.err.println("El archivo " + inputFile.getName() + " no existe. Se omitirá.");
					continue;
				}

				// Leer el contenido del archivo de entrada.
				String content = readFileContent(inputFile);

				// Tokenizar el contenido.
				String[] tokens = tokenizer.tokenize(content);

				// Escribir los tokens en el archivo de salida.
				for (String token : tokens)
				{
					writer.write(token + "\n");
				}
				writer.write("\n"); // Separador entre archivos.
			}
		}
		catch (IOException e)
		{
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model: {0}", e.getMessage());

		}
		finally
		{
			if (modelIn != null)
			{
				modelIn.close();
			}
		}

		System.out.println("Tokenización completada. Resultado guardado en: " + outputFileName);
	}

	/**
	 * @brief Lee el contenido de un archivo de texto.
	 *
	 * Este metodo lee un archivo línea por línea y devuelve el contenido completo como una cadena.
	 *
	 * @param file Archivo de entrada.
	 * @return Contenido del archivo como cadena.
	 * @throws IOException En caso de errores al leer el archivo.
	 */
	private static String readFileContent(File file) throws IOException
	{
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				content.append(line).append("\n");
			}
		}
		return content.toString();
	}
}
