//
// Created by Divyansh Shukla on 01/05/19.
//

#include "matrix_multiplication.h"

double sdot_8(const vector<double> &x, const vector<double> &y) {
    int n = x.size();
    int i, n8 = n>>3<<3;
    double s, t[8];
    t[0] = t[1] = t[2] = t[3] = t[4] = t[5] = t[6] = t[7] = 0.0f;
    for (i = 0; i < n8; i += 8) {
        t[0] += x[i+0] * y[i+0];
        t[1] += x[i+1] * y[i+1];
        t[2] += x[i+2] * y[i+2];
        t[3] += x[i+3] * y[i+3];
        t[4] += x[i+4] * y[i+4];
        t[5] += x[i+5] * y[i+5];
        t[6] += x[i+6] * y[i+6];
        t[7] += x[i+7] * y[i+7];
    }
    for (s = 0.0f; i < n; ++i) s += x[i] * y[i];
    s += t[0] + t[1] + t[2] + t[3] + t[4] + t[5] + t[6] + t[7];
    return s;
}

vector<vector<double>> transpose_mat(vector<vector<double>> &mat) {
    vector<vector<double>> mat_trans(mat[0].size(), vector<double>(mat.size()));
    for(int i = 0; i < mat.size(); i++) {
        for(int j = 0; j < mat[0].size(); j++) {
            mat_trans[j][i] = mat[i][j];
        }
    }
    return mat_trans;
}

vector<vector<double>> matmul(vector<vector<double>> &matrix1, vector<vector<double>> &matrix2) {
    auto matrix2_tr = transpose_mat(matrix2);
    vector<vector<double>> matrix_product(matrix1.size(), vector<double>(matrix2[0].size()));
    for(int i = 0; i < matrix1.size(); i++) {
        for(int j = 0; j < matrix2[0].size(); j++) {
            matrix_product[i][j] = sdot_8(matrix1[i], matrix2_tr[j]);
        }
    }
    return matrix_product;
}