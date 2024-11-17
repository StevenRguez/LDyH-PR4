package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file NameFinderTrainer.java
 * @brief Clase para entrenar un modelo de reconocimiento de nombres propios utilizando OpenNLP.
 *
 * Este programa entrena un modelo NER (Named Entity Recognition) basado en un conjunto
 * de datos de entrenamiento, específicamente para la detección de nombres de personas.
 */
public class NameFinderTrainer
{
	/**
	 * @brief Método principal para entrenar un modelo de detección de nombres propios.
	 *
	 * Este método utiliza datos de entrenamiento en formato de texto para crear un modelo
	 * capaz de identificar entidades nombradas (nombres de personas) en un texto tokenizado.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception En caso de errores durante el entrenamiento o escritura del modelo.
	 */
	public static void main(String[] args) throws Exception
	{
		Charset charset = Charset.forName("UTF-8"); /**< Conjunto de caracteres utilizado para leer los datos. */

		// Carga los datos de entrenamiento desde un archivo.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-ner-person.train"), charset);

		// Convierte las líneas de texto en muestras de entrenamiento para el detector de nombres.
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		TokenNameFinderModel model; /**< Modelo de detección de nombres propio generado. */

		try
		{
			// Entrena el modelo utilizando los datos de entrenamiento proporcionados.
			model = NameFinderME.train(
					"en",                       // Idioma del modelo.
					"person",                   // Tipo de entidad a reconocer (nombres de personas).
					sampleStream,               // Flujo de datos de entrenamiento.
					TrainingParameters.defaultParams(), // Parámetros de entrenamiento predeterminados.
					(byte[]) null,              // Datos de contexto adicionales (nulo en este caso).
					Collections.<String, Object>emptyMap() // Información adicional del modelo (vacío).
			);
		}
		finally
		{
			// Cierra el flujo de datos de entrenamiento.
			sampleStream.close();
		}

		BufferedOutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */

		try
		{
			String modelFile = "models/en-ner-person.model"; /**< Ruta del archivo donde se guardará el modelo. */

			// Guarda el modelo entrenado en el archivo especificado.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		}
		finally
		{
			// Cierra el flujo de salida si está abierto.
			if (modelOut != null)
			{
				modelOut.close();
			}
		}

		// Indica que el entrenamiento ha finalizado correctamente.
		System.out.println("done");
	}
}

