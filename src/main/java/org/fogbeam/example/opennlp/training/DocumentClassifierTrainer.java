package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * @file DocumentClassifierTrainer.java
 * @brief Clase para entrenar un modelo de clasificación de documentos utilizando OpenNLP.
 *
 * Esta clase utiliza datos de entrenamiento en formato de texto para generar un modelo
 * de clasificación de documentos basado en categorías predefinidas.
 */
public class DocumentClassifierTrainer
{
	/**
	 * @brief Método principal para entrenar un modelo de clasificación de documentos.
	 *
	 * Este método procesa un archivo de datos de entrenamiento, entrena un modelo de
	 * categorización de documentos y guarda el modelo entrenado en un archivo para
	 * su posterior uso.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		DoccatModel model = null; /**< Modelo de clasificación de documentos generado. */
		InputStream dataIn = null; /**< Flujo de entrada para leer los datos de entrenamiento. */

		try
		{
			// Carga los datos de entrenamiento desde un archivo.
			dataIn = new FileInputStream("training_data/en-doccat.train");

			// Convierte las líneas de texto en un flujo de muestras de documentos.
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

			// Entrena el modelo utilizando las muestras de documentos.
			model = DocumentCategorizerME.train(
					"en",          // Idioma del modelo.
					sampleStream   // Flujo de datos de entrenamiento.
			);
		}
		catch (IOException e)
		{
			// Maneja errores al leer o procesar los datos de entrenamiento.
			e.printStackTrace();
		}
		finally
		{
			// Cierra el flujo de entrada si está abierto.
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException e)
				{
					// Error al cerrar el flujo de entrada.
					e.printStackTrace();
				}
			}
		}

		OutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */
		String modelFile = "models/en-doccat.model"; /**< Ruta del archivo donde se guardará el modelo. */

		try
		{
			// Guarda el modelo entrenado en un archivo.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		}
		catch (IOException e)
		{
			// Maneja errores al guardar el modelo.
			e.printStackTrace();
		}
		finally
		{
			// Cierra el flujo de salida si está abierto.
			if (modelOut != null)
			{
				try
				{
					modelOut.close();
				}
				catch (IOException e)
				{
					// Error al cerrar el flujo de salida, el modelo puede ser inválido.
					e.printStackTrace();
				}
			}
		}

		// Indica que el entrenamiento ha finalizado correctamente.
		System.out.println("done");
	}
}

