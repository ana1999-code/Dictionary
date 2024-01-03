package com.example.dictionary.application.report;

import net.sf.dynamicreports.report.exception.DRException;

import java.io.FileNotFoundException;

public interface ReportGenerator {

    void generate() throws FileNotFoundException, DRException;
}
