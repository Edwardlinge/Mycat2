package io.mycat.sqlparser.util.simpleParser2;

public interface IdRecorder {
    void startRecordTokenChar(int startOffset);

    void recordTokenChar(int c);

    void endRecordTokenChar(int endOffset);
}