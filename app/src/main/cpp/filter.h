/*
 * Academic License - for use in teaching, academic research, and meeting
 * course requirements at degree granting institutions only.  Not for
 * government, commercial, or other organizational use.
 *
 * filter.h
 *
 * Code generation for function 'filter'
 *
 */

#ifndef BPAPP_FILTER_H
#define BPAPP_FILTER_H

/* Include files */
#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include "rtwtypes.h"
#include "wabp_types.h"

/* Function Declarations */
extern void filter(const double b[11], const double a[3], const double x[100],
                   double y[100]);

#endif //BPAPP_FILTER_H

/* End of code generation (filter.h) */
