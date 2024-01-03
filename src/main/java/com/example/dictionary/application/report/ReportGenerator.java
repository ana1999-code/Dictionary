package com.example.dictionary.application.report;

import net.sf.dynamicreports.report.exception.DRException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ReportGenerator {

    void generate() throws IOException, DRException;
}
