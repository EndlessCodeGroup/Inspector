package ru.endlesscode.inspector.service

public interface TextStorage {

    /**
     * Stores given [text] (synchronously).
     *
     * @return replacement of stored text. For example it can be URL of remote host with stored text
     */
    public suspend fun storeText(text: String): String
}
