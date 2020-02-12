//
// Created by Divyansh Shukla on 01/05/19.
//
#include <cmath>
using namespace std;

#include "fft.h"

pair<vector<double>, vector<double>> process_fft(const vector<double> &signal) {
    const int numPoints = signal.size();
    vector<double> real = signal;
    vector<double> imag(numPoints);
    // perform FFT using the real & imag array
    const double pi = M_PI;
    const int numStages = (int) (log(numPoints) / log(2));
    const int halfNumPoints = numPoints >> 1;
    int j = halfNumPoints;
    // FFT time domain decomposition carried out by "bit reversal sorting"
    // algorithm
    int k;
    for (int i = 1; i < numPoints - 2; i++) {
        if (i < j) {
            // swap
            double tempReal = real[j];
            double tempImag = imag[j];
            real[j] = real[i];
            imag[j] = imag[i];
            real[i] = tempReal;
            imag[i] = tempImag;
        }
        k = halfNumPoints;
        while (k <= j) {
            j -= k;
            k >>= 1;
        }
        j += k;
    }

    // loop for each stage
    for (int stage = 1; stage <= numStages; stage++) {
        int LE = 1;
        for (int i = 0; i < stage; i++) {
            LE <<= 1;
        }
        const int LE2 = LE >> 1;
        double UR = 1;
        double UI = 0;
        // calculate sine & cosine values
        const double SR =  cos(pi / LE2);
        const double SI = -sin(pi / LE2);
        // loop for each sub DFT
        for (int subDFT = 1; subDFT <= LE2; subDFT++) {
            // loop for each butterfly
            for (int butterfly = subDFT - 1; butterfly <= numPoints - 1; butterfly += LE) {
                int ip = butterfly + LE2;
                // butterfly calculation
                double tempReal = (double) (real[ip] * UR - imag[ip] * UI);
                double tempImag = (double) (real[ip] * UI + imag[ip] * UR);
                real[ip] = real[butterfly] - tempReal;
                imag[ip] = imag[butterfly] - tempImag;
                real[butterfly] += tempReal;
                imag[butterfly] += tempImag;
            }

            double tempUR = UR;
            UR = tempUR * SR - UI * SI;
            UI = tempUR * SI + UI * SR;
        }
    }
    return {real, imag};
}