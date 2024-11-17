package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file PartOfSpeechTaggerTrainer.java
 * @brief Clase para entrenar un modelo de etiquetado gramatical (Part-of-Speech) usando OpenNLP.
 *
 * Este programa entrena un modelo de etiquetado gramatical (POS) a partir de un conjunto
 * de datos de entrenamiento. Los modelos generados permiten etiquetar palabras de un texto
 * con su categoría gramatical (por ejemplo, sustantivo, verbo, adjetivo).
 */
public class PartOfSpeechTaggerTrainer
{
	/**
	 * @brief Método principal para entrenar un modelo de etiquetado gramatical (POS).
	 *
	 * Este método procesa un archivo de datos de entrenamiento en formato texto,
	 * entrena un modelo de etiquetado gramatical y lo guarda en un archivo para
	 * su posterior uso.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 */
	public static void main(String[] args)
	{
		POSModel model = null; /**< Modelo de etiquetado gramatical generado. */
		InputStream dataIn = null; /**< Flujo de entrada para leer los datos de entrenamiento. */

		try
		{
			// Carga los datos de entrenamiento desde un archivo en formato texto.
			dataIn = new FileInputStream("training_data/en-pos.train");

			// Convierte las líneas de texto en muestras de entrenamiento para el etiquetador gramatical.
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

			// Entrena el modelo utilizando los datos proporcionados y parámetros predeterminados.
			model = POSTaggerME.train(
					"en",                        // Idioma del modelo (inglés en este caso).
					sampleStream,                // Flujo de datos de entrenamiento.
					TrainingParameters.defaultParams(), // Parámetros de entrenamiento predeterminados.
					null,                        // Diccionario adicional (nulo en este caso).
					null                         // Información adicional (nulo en este caso).
			);
		}
		catch (IOException e)
		{
			// Manejo de errores durante la lectura o procesamiento de los datos de entrenamiento.
			e.printStackTrace();
		}
		finally
		{
			// Cierra el flujo de datos de entrada si está abierto.
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException e)
				{
					// El cierre del flujo falló; no afecta al entrenamiento finalizado.
					e.printStackTrace();
				}
			}
		}

		OutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */
		String modelFile = "models/en-pos.model"; /**< Ruta del archivo donde se guardará el modelo entrenado. */

		try
		{
			// Guarda el modelo entrenado en el archivo especificado.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		}
		catch (IOException e)
		{
			// Manejo de errores durante la escritura del modelo en el archivo.
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
					// El cierre del flujo falló; el modelo guardado podría estar corrupto.
					e.printStackTrace();
				}
			}
		}

		// Indica que el entrenamiento y el guardado del modelo han finalizado.
		System.out.println("done");
	}
}

