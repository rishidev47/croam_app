#include <jni.h>
#include <vector>
#include <string>
#include "mfcc.h"
using namespace std;


extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_example_croam_CRoamService_nativeMFCC(
        JNIEnv* env,
        jobject obj,
        jdoubleArray input_buffer_java) {
    jdouble* input_buffer = env->GetDoubleArrayElements(input_buffer_java, 0);
    vector<double> buffer(env->GetArrayLength(input_buffer_java));
    for(int i = 0; i < buffer.size(); i++) {
        buffer[i] = input_buffer[i];
    }
    vector<float> mfcc_result = process_mfcc(buffer);
    jfloatArray mfcc_result_java = env->NewFloatArray(mfcc_result.size());
    float* data = (float*)malloc(sizeof(float) * mfcc_result.size());
    for(int i = 0; i < mfcc_result.size(); i++) {
        data[i] = mfcc_result[i];
    }
    env->SetFloatArrayRegion(mfcc_result_java, 0, mfcc_result.size(), data);
    free(data);
    env->ReleaseDoubleArrayElements(input_buffer_java, input_buffer, 0);
    return mfcc_result_java;
}

