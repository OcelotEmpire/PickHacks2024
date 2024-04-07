package main;

import org.opencv.core.Mat;

public record Photograph(Mat frame, float timeStamp) {}
