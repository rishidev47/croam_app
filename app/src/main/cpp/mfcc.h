//
// Created by Divyansh Shukla on 02/05/19.
//

#ifndef CTEST_MFCC_H
#define CTEST_MFCC_H

#include <vector>
using namespace std;

const int n_mfcc = 40;
const double fMin = 0.0;
const int n_fft = 256;
const int hop_length = 128;
const int n_mels = 128;

const double sampleRate = 16000.0;
const double fMax = sampleRate/2.0;

vector<float> process_mfcc(vector<double> &doubleInputBuffer);

//MFCC into 1d
vector<float> finalshape(const vector<vector<double>> &mfccSpecTro);

//DCT to mfcc, librosa
vector<vector<double>> dctMfcc(vector<double> &y);

//mel spectrogram, librosa
vector<vector<double>> melSpectrogram(vector<double> &y);

//stft, librosa
vector<vector<double>> stftMagSpec(vector<double> &y);

vector<double> magSpectrogram(vector<double> &frame);

//get hann window, librosa
vector<double> getWindow();

//frame, librosa
vector<vector<double>> yFrame(vector<double> &ypad);

//power to db, librosa
vector<vector<double>> powerToDb(vector<vector<double>> &melS);

//dct, librosa
vector<vector<double>> dctFilter(int n_filters, int n_input);


//mel, librosa
vector<vector<double>> melFilter();

//fft frequencies, librosa
vector<double> fftFreq();

//mel frequencies, librosa
vector<double> melFreq(int numMels);

//mel to hz, htk, librosa
vector<double> melToFreqS(vector<double> &mels);

// hz to mel, htk, librosa
vector<double> freqToMelS(vector<double> &freqs);

//mel to hz, Slaney, librosa
vector<double> melToFreq(vector<double> &mels);

// hz to mel, Slaney, librosa
vector<double> freqToMel(vector<double> &freqs);

// log10
double log10(double value);
#endif //CTEST_MFCC_H
