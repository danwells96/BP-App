#include <jni.h>

extern "C"
JNIEXPORT jdouble

JNICALL
Java_com_project_paulo_bpapp_Graph_mult(
        JNIEnv *env,
jobject /* this */, double a, double b) {
return a * b;
}