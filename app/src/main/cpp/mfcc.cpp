//
// Created by Divyansh Shukla on 02/05/19.
//

#include <cmath>
using namespace std;

#include "fft.h"
#include "mfcc.h"
#include "matrix_multiplication.h"

vector<float> process_mfcc(vector<double> &doubleInputBuffer) {
    const vector<vector<double>> mfccResult = dctMfcc(doubleInputBuffer);
    return finalshape(mfccResult);
}

//MFCC into 1d
vector<float> finalshape(const vector<vector<double>> &mfccSpecTro){
    vector<float> finalMfcc(mfccSpecTro[0].size() * mfccSpecTro.size());
    int k = 0;
    for (int i = 0; i < mfccSpecTro[0].size(); i++){
        for (int j = 0; j < mfccSpecTro.size(); j++){
            finalMfcc[k] = (float) mfccSpecTro[j][i];
            k = k+1;
        }
    }
    return finalMfcc;
}

//DCT to mfcc, librosa
vector<vector<double>> dctMfcc(vector<double> &y) {
    vector<vector<double>> melSpectr = melSpectrogram(y);
    const vector<vector<double>> specTroGram = powerToDb(melSpectr);
    const vector<vector<double>> dctBasis = dctFilter(n_mfcc, n_mels);
    vector<vector<double>> mfccSpecTro(n_mfcc, vector<double>(specTroGram[0].size()));
    for (int i = 0; i < n_mfcc; i++){
        for (int j = 0; j < specTroGram[0].size(); j++){
            for (int k = 0; k < specTroGram.size(); k++){
                mfccSpecTro[i][j] += dctBasis[i][k]*specTroGram[k][j];
            }
        }
    }
    return mfccSpecTro;
}


//mel spectrogram, librosa
vector<vector<double>> melSpectrogram(vector<double> &y){
    vector<vector<double>> melBasis = melFilter();
    vector<vector<double>> spectro = stftMagSpec(y);
    vector<vector<double>> melS = matmul(melBasis, spectro);
    return melS;
}


//stft, librosa
vector<vector<double>> stftMagSpec(vector<double> &y){
    //Short-time Fourier transform (STFT)
    const vector<double> fftwin = getWindow();
    //pad y with reflect mode so it's centered. This reflect padding implementation is
    // not perfect but works for this demo.
    vector<double> ypad(n_fft+y.size());
    for (int i = 0; i < n_fft/2; i++){
        ypad[(n_fft/2)-i-1] = y[i+1];
        ypad[(n_fft/2)+y.size()+i] = y[y.size()-2-i];
    }
    for (int j = 0; j < y.size(); j++){
        ypad[(n_fft/2)+j] = y[j];
    }


    const vector<vector<double>> frame = yFrame(ypad);
    vector<vector<double>> fftmagSpec(1+n_fft/2, vector<double>(frame[0].size()));
    vector<double> fftFrame(n_fft);
    for (int k = 0; k < frame[0].size(); k++){
        for (int l =0; l < n_fft; l++){
            fftFrame[l] = fftwin[l]*frame[l][k];
        }
        vector<double> magSpec = magSpectrogram(fftFrame);
        for (int i =0; i < 1+n_fft/2; i++){
            fftmagSpec[i][k] = magSpec[i];
        }
    }
    return fftmagSpec;
}

vector<double> magSpectrogram(vector<double> &frame){
    vector<double> magSpec(frame.size());
    auto [real, imag] = process_fft(frame);
    for (int m = 0; m < frame.size(); m++) {
        magSpec[m] = real[m] * real[m] + imag[m] * imag[m];
    }
    return magSpec;
}


//get hann window, librosa
vector<double> getWindow(){
    //Return a Hann window for even n_fft.
    //The Hann window is a taper formed by using a raised cosine or sine-squared
    //with ends that touch zero.
    vector<double> win(n_fft);
    for (int i = 0; i < n_fft; i++){
        win[i] = 0.5 - 0.5 * cos(2.0*M_PI*i/n_fft);
    }
    return win;
}

//frame, librosa
vector<vector<double>> yFrame(vector<double> &ypad){
    const int n_frames = 1 + (ypad.size() - n_fft) / hop_length;
    vector<vector<double>> winFrames(n_fft, vector<double>(n_frames));
    for (int i = 0; i < n_fft; i++){
        for (int j = 0; j < n_frames; j++){
            winFrames[i][j] = ypad[j*hop_length+i];
        }
    }
    return winFrames;
}

//power to db, librosa
vector<vector<double>> powerToDb(vector<vector<double>> &melS){
    //Convert a power spectrogram (amplitude squared) to decibel (dB) units
    //  This computes the scaling ``10 * log10(S / ref)`` in a numerically
    //  stable way.
    vector<vector<double>> log_spec(melS.size(), vector<double>(melS[0].size()));
    double maxValue = -100;
    for (int i = 0; i < melS.size(); i++){
        for (int j = 0; j < melS[0].size(); j++){
            double magnitude = abs(melS[i][j]);
            if (magnitude > 1e-10){
                log_spec[i][j]=10.0*log10(magnitude);
            }else{
                log_spec[i][j]=10.0*(-10);
            }
            if (log_spec[i][j] > maxValue){
                maxValue = log_spec[i][j];
            }
        }
    }

    //set top_db to 80.0
    for (int i = 0; i < melS.size(); i++){
        for (int j = 0; j < melS[0].size(); j++){
            if (log_spec[i][j] < maxValue - 80.0){
                log_spec[i][j] = maxValue - 80.0;
            }
        }
    }
    //ref is disabled, maybe later.
    return log_spec;
}

//dct, librosa
vector<vector<double>> dctFilter(int n_filters, int n_input){
    //Discrete cosine transform (DCT type-III) basis.
    vector<vector<double>> basis(n_filters, vector<double>(n_input));
    vector<double> samples(n_input);
    for (int i = 0; i < n_input; i++){
        samples[i] = (1 + 2*i) * M_PI/(2.0*(n_input));
    }
    for (int j = 0; j < n_input; j++){
        basis[0][j] = 1.0/sqrt(n_input);
    }
    for (int i = 1; i < n_filters; i++){
        for (int j = 0; j < n_input; j++){
            basis[i][j] = cos(i*samples[j]) * sqrt(2.0/(n_input));
        }
    }
    return basis;
}


//mel, librosa
vector<vector<double>> melFilter(){
    //Create a Filterbank matrix to combine FFT bins into Mel-frequency bins.
    // Center freqs of each FFT bin
    const vector<double> fftFreqs = fftFreq();
    //'Center freqs' of mel bands - uniformly spaced between limits
    const vector<double> melF = melFreq(n_mels+2);

    vector<double> fdiff(melF.size()-1);
    for (int i = 0; i < melF.size()-1; i++){
        fdiff[i] = melF[i+1]-melF[i];
    }

    vector<vector<double>> ramps(melF.size(), vector<double>(fftFreqs.size()));
    for (int i = 0; i < melF.size(); i++){
        for (int j = 0; j < fftFreqs.size(); j++){
            ramps[i][j] = melF[i]-fftFreqs[j];
        }
    }

    vector<vector<double>> weights(n_mels, vector<double>(1+n_fft/2));
    for (int i = 0; i < n_mels; i++){
        for (int j = 0; j < fftFreqs.size(); j++){
            double lowerF = -ramps[i][j] / fdiff[i];
            double upperF = ramps[i+2][j] / fdiff[i+1];
            if (lowerF > upperF && upperF>0){
                weights[i][j] = upperF;
            }else if (lowerF > upperF && upperF<0){
                weights[i][j] = 0;
            }else if (lowerF < upperF && lowerF>0){
                weights[i][j] =lowerF;
            }else if (lowerF < upperF && lowerF<0){
                weights[i][j] = 0;
            }else {}
        }
    }

    vector<double> enorm(n_mels);
    for (int i = 0; i < n_mels; i++){
        enorm[i] = 2.0 / (melF[i+2]-melF[i]);
        for (int j = 0; j < fftFreqs.size(); j++){
            weights[i][j] *= enorm[i];
        }
    }
    return weights;

    //need to check if there's an empty channel somewhere
}

//fft frequencies, librosa
vector<double> fftFreq() {
    //Alternative implementation of np.fft.fftfreqs
    vector<double> freqs(1+n_fft/2);
    for (int i = 0; i < 1+n_fft/2; i++){
        freqs[i] = 0 + (sampleRate/2)/(n_fft/2) * i;
    }
    return freqs;
}

//mel frequencies, librosa
vector<double> melFreq(int numMels) {
    //'Center freqs' of mel bands - uniformly spaced between limits
    vector<double> LowFFreq(1);
    vector<double> HighFFreq(1);
    LowFFreq[0] = fMin;
    HighFFreq[0] = fMax;
    const vector<double> melFLow = freqToMel(LowFFreq);
    const vector<double> melFHigh = freqToMel(HighFFreq);
    vector<double> mels(numMels);
    for (int i = 0; i < numMels; i++) {
        mels[i] = melFLow[0] + (melFHigh[0] - melFLow[0]) / (numMels-1) * i;
    }
    return melToFreq(mels);
}


//mel to hz, htk, librosa
vector<double> melToFreqS(vector<double> &mels) {
    vector<double> freqs(mels.size());
    for (int i = 0; i < mels.size(); i++) {
        freqs[i] = 700.0 * (pow(10, mels[i]/2595.0) - 1.0);
    }
    return freqs;
}


// hz to mel, htk, librosa
vector<double> freqToMelS(vector<double> &freqs) {
    vector<double> mels(freqs.size());
    for (int i = 0; i < freqs.size(); i++){
        mels[i] = 2595.0 * log10(1.0 + freqs[i]/700.0);
    }
    return mels;
}

//mel to hz, Slaney, librosa
vector<double> melToFreq(vector<double> &mels) {
    // Fill in the linear scale
    const double f_min = 0.0;
    const double f_sp = 200.0 / 3;
    vector<double> freqs(mels.size());

    // And now the nonlinear scale
    const double min_log_hz = 1000.0;                         // beginning of log region (Hz)
    const double min_log_mel = (min_log_hz - f_min) / f_sp;  // same (Mels)
    const double logstep = log(6.4) / 27.0;

    for (int i = 0; i < mels.size(); i++) {
        if (mels[i] < min_log_mel){
            freqs[i] =  f_min + f_sp * mels[i];
        }else{
            freqs[i] = min_log_hz * exp(logstep * (mels[i] - min_log_mel));
        }
    }
    return freqs;
}


// hz to mel, Slaney, librosa
vector<double> freqToMel(vector<double> &freqs) {
    const double f_min = 0.0;
    const double f_sp = 200.0 / 3;
    vector<double> mels(freqs.size());

    // Fill in the log-scale part

    const double min_log_hz = 1000.0;                         // beginning of log region (Hz)
    const double min_log_mel = (min_log_hz - f_min) / f_sp ;  // # same (Mels)
    const double logstep = log(6.4) / 27.0;              // step size for log region

    for (int i = 0; i < freqs.size(); i++) {
        if (freqs[i] < min_log_hz){
            mels[i] = (freqs[i] - f_min) / f_sp;
        }else{
            mels[i] = min_log_mel + log(freqs[i]/min_log_hz) / logstep;
        }
    }
    return mels;
}

// log10
double log10(double value) {
    return log(value) / log(10);
}