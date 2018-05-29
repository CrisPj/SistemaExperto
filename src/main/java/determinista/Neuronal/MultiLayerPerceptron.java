/*
 *     mlp-java, Copyright (C) 2012 Davide Gessa
 * 
 * 	This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package determinista.Neuronal;

public class MultiLayerPerceptron implements Cloneable
{
	protected double  learningRate;
	protected Layer[] layers;
	protected Function function;

	public MultiLayerPerceptron(int[] layers, double learningRate, Function fun)
	{
		this.learningRate = learningRate;
		function = fun;
		
		this.layers = new Layer[layers.length];
		
		for(int i = 0; i < layers.length; i++)
		{			
			if(i != 0)
			{
				this.layers[i] = new Layer(layers[i], layers[i - 1]);
			}
			else
			{
				this.layers[i] = new Layer(layers[i], 0);
			}
		}
	}

	public double[] execute(double[] input)
	{
		int i;
		int j;
		int k;
		double new_value;
		
		double output[] = new double[layers[layers.length - 1].Length];
		
		// Put input
		for(i = 0; i < layers[0].Length; i++)
		{
			layers[0].Neurons[i].Value = input[i];
		}
		
		// Execute - hiddens + output
		for(k = 1; k < layers.length; k++)
		{
			for(i = 0; i < layers[k].Length; i++)
			{
				new_value = 0.0;
				for(j = 0; j < layers[k - 1].Length; j++)
					new_value += layers[k].Neurons[i].Weights[j] * layers[k - 1].Neurons[j].Value;
				
				new_value += layers[k].Neurons[i].Bias;
				
				layers[k].Neurons[i].Value = function.evalute(new_value);
			}
		}
		
		
		// Get output
		for(i = 0; i < layers[layers.length - 1].Length; i++)
		{
			output[i] = layers[layers.length - 1].Neurons[i].Value;
		}
		
		return output;
	}


	public double backPropagate(double[] input, double[] output)
	{
		double new_output[] = execute(input);
		double error;
		int i;
		int j;
		int k;

		for(i = 0; i < layers[layers.length - 1].Length; i++)
		{
			error = output[i] - new_output[i];
			layers[layers.length - 1].Neurons[i].Delta = error * function.evaluteDerivate(new_output[i]);
		} 
	
		
		for(k = layers.length - 2; k >= 0; k--)
		{

			for(i = 0; i < layers[k].Length; i++)
			{
				error = 0.0;
				for(j = 0; j < layers[k + 1].Length; j++)
					error += layers[k + 1].Neurons[j].Delta * layers[k + 1].Neurons[j].Weights[i];
								
				layers[k].Neurons[i].Delta = error * function.evaluteDerivate(layers[k].Neurons[i].Value);
			}

			for(i = 0; i < layers[k + 1].Length; i++)
			{
				for(j = 0; j < layers[k].Length; j++)
					layers[k + 1].Neurons[i].Weights[j] += learningRate * layers[k + 1].Neurons[i].Delta *
							layers[k].Neurons[j].Value;
				layers[k + 1].Neurons[i].Bias += learningRate * layers[k + 1].Neurons[i].Delta;
			}
		}	
		
		error = 0.0;
		
		for(i = 0; i < output.length; i++)
		{
			error += Math.abs(new_output[i] - output[i]);
		}

		error = error / output.length;
		return error;
	}
}

