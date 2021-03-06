/*
 * Java CSV is a stream based library for reading and writing
 * CSV and other delimited data.
 *   
 * Copyright (C) Bruce Dunwiddie bruce@csvreader.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package org.dangcat.document.csv;

import java.io.*;
import java.nio.charset.Charset;

/**
 * A stream based writer for writing delimited text data to a file or a stream.
 */
public class CsvWriter {
    private Charset charset = null;
    private boolean closed = false;
    private String fileName = null;
    private boolean firstColumn = true;
    private boolean initialized = false;
    private Writer outputStream = null;
    private String systemRecordDelimiter = System.getProperty("line.separator");
    private boolean useCustomRecordDelimiter = false;
    // this holds all the values for switches that the user is allowed to set
    private WriteUserSettings userSettings = new WriteUserSettings();

    /**
     * Creates a {@link com.csvreader.CsvWriter CsvWriter} object using an
     * OutputStream to write data to.
     *
     * @param outputStream The stream to write the column delimited data to.
     * @param delimiter    The character to use as the column delimiter.
     * @param charset      The {@link java.nio.charset.Charset Charset} to use while
     *                     writing the data.
     */
    public CsvWriter(OutputStream outputStream, char delimiter, Charset charset) {
        this(new OutputStreamWriter(outputStream, charset), delimiter);
    }

    /**
     * Creates a {@link com.csvreader.CsvWriter CsvWriter} object using a file
     * as the data destination.&nbsp;Uses a comma as the column delimiter and
     * ISO-8859-1 as the {@link java.nio.charset.Charset Charset}.
     *
     * @param fileName The path to the file to output the data.
     */
    public CsvWriter(String fileName) {
        this(fileName, Letters.COMMA, Charset.forName("ISO-8859-1"));
    }

    /**
     * Creates a {@link com.csvreader.CsvWriter CsvWriter} object using a file
     * as the data destination.
     *
     * @param fileName  The path to the file to output the data.
     * @param delimiter The character to use as the column delimiter.
     * @param charset   The {@link java.nio.charset.Charset Charset} to use while
     *                  writing the data.
     */
    public CsvWriter(String fileName, char delimiter, Charset charset) {
        if (fileName == null)
            throw new IllegalArgumentException("Parameter fileName can not be null.");

        if (charset == null)
            throw new IllegalArgumentException("Parameter charset can not be null.");

        this.fileName = fileName;
        this.userSettings.Delimiter = delimiter;
        this.charset = charset;
    }

    public CsvWriter(Writer writer) {
        this(writer, Letters.COMMA);
    }

    /**
     * Creates a {@link com.csvreader.CsvWriter CsvWriter} object using a Writer
     * to write data to.
     *
     * @param outputStream The stream to write the column delimited data to.
     * @param delimiter    The character to use as the column delimiter.
     */
    public CsvWriter(Writer outputStream, char delimiter) {
        if (outputStream == null)
            throw new IllegalArgumentException("Parameter outputStream can not be null.");

        this.outputStream = outputStream;
        this.userSettings.Delimiter = delimiter;
        this.initialized = true;
    }

    private void checkClosed() throws IOException {
        if (this.closed)
            throw new IOException("This instance of the CsvWriter class has already been closed.");
    }

    private void checkInit() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null)
                this.outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), this.charset));

            this.initialized = true;
        }
    }

    /**
     * Closes and releases all related resources.
     */
    public void close() {
        if (!this.closed) {
            this.close(true);
            this.closed = true;
        }
    }

    private void close(boolean closing) {
        if (!this.closed) {
            if (closing)
                this.charset = null;

            try {
                if (this.initialized)
                    this.outputStream.close();
            } catch (Exception e) {
                // just eat the exception
            }

            this.outputStream = null;
            this.closed = true;
        }
    }

    /**
     * Ends the current record by sending the record delimiter.
     *
     * @throws IOException Thrown if an error occurs while writing data to
     *                     the destination stream.
     */
    public void endRecord() throws IOException {
        this.checkClosed();
        this.checkInit();

        if (this.useCustomRecordDelimiter)
            this.outputStream.write(this.userSettings.RecordDelimiter);
        else
            this.outputStream.write(this.systemRecordDelimiter);
        this.firstColumn = true;
    }

    /**
     *
     */
    @Override
    protected void finalize() {
        this.close(false);
    }

    /**
     * Clears all buffers for the current writer and causes any buffered data to
     * be written to the underlying device.
     *
     * @throws IOException Thrown if an error occurs while writing data to
     *                     the destination stream.
     */
    public void flush() throws IOException {
        this.outputStream.flush();
    }

    public char getComment() {
        return this.userSettings.Comment;
    }

    public void setComment(char comment) {
        this.userSettings.Comment = comment;
    }

    /**
     * Gets the character being used as the column delimiter.
     *
     * @return The character being used as the column delimiter.
     */
    public char getDelimiter() {
        return this.userSettings.Delimiter;
    }

    /**
     * Sets the character to use as the column delimiter.
     *
     * @param delimiter The character to use as the column delimiter.
     */
    public void setDelimiter(char delimiter) {
        this.userSettings.Delimiter = delimiter;
    }

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int escapeMode) {
        this.userSettings.EscapeMode = escapeMode;
    }

    /**
     * Whether fields will be surrounded by the text qualifier even if the
     * qualifier is not necessarily needed to escape this field.
     *
     * @return Whether fields will be forced to be qualified or not.
     */
    public boolean getForceQualifier() {
        return this.userSettings.ForceQualifier;
    }

    /**
     * Use this to force all fields to be surrounded by the text qualifier even
     * if the qualifier is not necessarily needed to escape this field. Default
     * is false.
     *
     * @param forceQualifier Whether to force the fields to be qualified or not.
     */
    public void setForceQualifier(boolean forceQualifier) {
        this.userSettings.ForceQualifier = forceQualifier;
    }

    public char getRecordDelimiter() {
        return this.userSettings.RecordDelimiter;
    }

    /**
     * Sets the character to use as the record delimiter.
     *
     * @param recordDelimiter The character to use as the record delimiter.
     *                        Default is combination of standard end of line characters for
     *                        Windows, Unix, or Mac.
     */
    public void setRecordDelimiter(char recordDelimiter) {
        this.useCustomRecordDelimiter = true;
        this.userSettings.RecordDelimiter = recordDelimiter;
    }

    public String getRecordDelimiterText() {
        if (this.useCustomRecordDelimiter)
            return new String(new char[]{this.userSettings.RecordDelimiter});
        return this.systemRecordDelimiter;
    }

    /**
     * Gets the character to use as a text qualifier in the data.
     *
     * @return The character to use as a text qualifier in the data.
     */
    public char getTextQualifier() {
        return this.userSettings.TextQualifier;
    }

    /**
     * Sets the character to use as a text qualifier in the data.
     *
     * @param textQualifier The character to use as a text qualifier in the
     *                      data.
     */
    public void setTextQualifier(char textQualifier) {
        this.userSettings.TextQualifier = textQualifier;
    }

    public WriteUserSettings getUserSettings() {
        return this.userSettings;
    }

    public void setUserSettings(WriteUserSettings userSettings) {
        this.userSettings = userSettings;
    }

    /**
     * Whether text qualifiers will be used while writing data or not.
     *
     * @return Whether text qualifiers will be used while writing data or not.
     */
    public boolean getUseTextQualifier() {
        return this.userSettings.UseTextQualifier;
    }

    /**
     * Sets whether text qualifiers will be used while writing data or not.
     *
     * @param useTextQualifier Whether to use a text qualifier while writing
     *                         data or not.
     */
    public void setUseTextQualifier(boolean useTextQualifier) {
        this.userSettings.UseTextQualifier = useTextQualifier;
    }

    /**
     * Writes another column of data to this record.&nbsp;Does not preserve
     * leading and trailing whitespace in this column of data.
     *
     * @param content The data for the new column.
     * @throws IOException Thrown if an error occurs while writing data to
     *                     the destination stream.
     */
    public void write(String content) throws IOException {
        this.write(content, false);
    }

    /**
     * Writes another column of data to this record.
     *
     * @param content        The data for the new column.
     * @param preserveSpaces Whether to preserve leading and trailing whitespace
     *                       in this column of data.
     * @throws IOException Thrown if an error occurs while writing data to
     *                     the destination stream.
     */
    public void write(String content, boolean preserveSpaces) throws IOException {
        this.checkClosed();
        this.checkInit();

        if (content == null)
            content = "";

        if (!this.firstColumn)
            this.outputStream.write(this.userSettings.Delimiter);

        boolean textQualify = this.userSettings.ForceQualifier;

        if (!preserveSpaces && content.length() > 0)
            content = content.trim();

        if (!textQualify
                && this.userSettings.UseTextQualifier
                && (content.indexOf(this.userSettings.TextQualifier) > -1 || content.indexOf(this.userSettings.Delimiter) > -1
                || (!this.useCustomRecordDelimiter && (content.indexOf(Letters.LF) > -1 || content.indexOf(Letters.CR) > -1))
                || (this.useCustomRecordDelimiter && content.indexOf(this.userSettings.RecordDelimiter) > -1)
                || (this.firstColumn && content.length() > 0 && content.charAt(0) == this.userSettings.Comment) ||
                // check for empty first column, which if on its own line must
                // be qualified or the line will be skipped
                (this.firstColumn && content.length() == 0))) {
            textQualify = true;
        }

        if (this.userSettings.UseTextQualifier && !textQualify && content.length() > 0 && preserveSpaces) {
            char firstLetter = content.charAt(0);

            if (firstLetter == Letters.SPACE || firstLetter == Letters.TAB)
                textQualify = true;

            if (!textQualify && content.length() > 1) {
                char lastLetter = content.charAt(content.length() - 1);

                if (lastLetter == Letters.SPACE || lastLetter == Letters.TAB)
                    textQualify = true;
            }
        }

        if (textQualify) {
            this.outputStream.write(this.userSettings.TextQualifier);

            if (this.userSettings.EscapeMode == WriteUserSettings.ESCAPE_MODE_BACKSLASH) {
                content = CsvWriterUtils.replace(content, "" + Letters.BACKSLASH, "" + Letters.BACKSLASH + Letters.BACKSLASH);
                content = CsvWriterUtils.replace(content, "" + this.userSettings.TextQualifier, "" + Letters.BACKSLASH + this.userSettings.TextQualifier);
            } else
                content = CsvWriterUtils.replace(content, "" + this.userSettings.TextQualifier, "" + this.userSettings.TextQualifier + this.userSettings.TextQualifier);
        } else if (this.userSettings.EscapeMode == WriteUserSettings.ESCAPE_MODE_BACKSLASH) {
            content = CsvWriterUtils.replace(content, "" + Letters.BACKSLASH, "" + Letters.BACKSLASH + Letters.BACKSLASH);
            content = CsvWriterUtils.replace(content, "" + this.userSettings.Delimiter, "" + Letters.BACKSLASH + this.userSettings.Delimiter);

            if (this.useCustomRecordDelimiter)
                content = CsvWriterUtils.replace(content, "" + this.userSettings.RecordDelimiter, "" + Letters.BACKSLASH + this.userSettings.RecordDelimiter);
            else {
                content = CsvWriterUtils.replace(content, "" + Letters.CR, "" + Letters.BACKSLASH + Letters.CR);
                content = CsvWriterUtils.replace(content, "" + Letters.LF, "" + Letters.BACKSLASH + Letters.LF);
            }

            if (this.firstColumn && content.length() > 0 && content.charAt(0) == this.userSettings.Comment) {
                if (content.length() > 1)
                    content = "" + Letters.BACKSLASH + this.userSettings.Comment + content.substring(1);
                else
                    content = "" + Letters.BACKSLASH + this.userSettings.Comment;
            }
        }

        this.outputStream.write(content);

        if (textQualify)
            this.outputStream.write(this.userSettings.TextQualifier);

        this.firstColumn = false;
    }

    public void writeComment(String commentText) throws IOException {
        this.checkClosed();
        this.checkInit();
        this.outputStream.write(this.userSettings.Comment);
        this.outputStream.write(commentText);

        if (this.useCustomRecordDelimiter)
            this.outputStream.write(this.userSettings.RecordDelimiter);
        else
            this.outputStream.write(this.systemRecordDelimiter);

        this.firstColumn = true;
    }

    /**
     * Writes a new record using the passed in array of values.
     *
     * @param values Values to be written.
     * @throws IOException Thrown if an error occurs while writing data to the
     *                     destination stream.
     */
    public void writeRecord(String[] values) throws IOException {
        this.writeRecord(values, false);
    }

    /**
     * Writes a new record using the passed in array of values.
     *
     * @param values         Values to be written.
     * @param preserveSpaces Whether to preserver leading and trailing spaces in
     *                       columns while writing out to the record or not.
     * @throws IOException Thrown if an error occurs while writing data to the
     *                     destination stream.
     */
    public void writeRecord(String[] values, boolean preserveSpaces) throws IOException {
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++)
                this.write(values[i], preserveSpaces);
            this.endRecord();
        }
    }
}