//
// Created by Divyansh Shukla on 01/05/19.
//

#ifndef CTEST_MATRIX_MULTIPLICATION_H
#define CTEST_MATRIX_MULTIPLICATION_H

#include <vector>
using namespace std;

vector<vector<double>> matmul(vector<vector<double>> &matrix1, vector<vector<double>> &matrix2);
vector<vector<double>> transpose_mat(vector<vector<double>> &mat);
double sdot_8(const vector<double> &x, const vector<double> &y);
#endif //CTEST_MATRIX_MULTIPLICATION_H
