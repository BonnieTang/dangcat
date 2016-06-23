package org.dangcat.business.code.service;

import java.io.File;

import org.dangcat.business.code.CodeGenerator;

public class ValidatorCodeGenerator extends CodeGenerator
{
    private static final String TEMPLATE = "Validator.java.template";

    @Override
    protected String getOutputFile()
    {
        String outputFile = "main" + File.separator + "java";
        outputFile += File.separator + this.getPackageName().replace(".", File.separator);
        outputFile += File.separator + "validator";
        outputFile += File.separator + this.getParams().get("JndiName") + "Validator.java";
        return outputFile;
    }

    @Override
    protected String getTemplate()
    {
        return TEMPLATE;
    }
}
